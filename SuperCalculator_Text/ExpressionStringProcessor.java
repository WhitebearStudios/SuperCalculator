import java.util.*;

public class ExpressionStringProcessor {
    final String DIGITS = "1234567890";
    final String LETTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    String[] OPERATORS = {"+", "-", "*", "/", "^", "[sqrt]"};
    boolean strIsOp(String s){
        for(String op : OPERATORS){
            if(op.equals(s)) return true;
        }
        return false;
    }
    private String[] DOUBLEOPERANDOPERATORS = {"+", "-", "*", "/", "^"};
    private String[] SINGLEOPERANDOPERATORS = {"[sqrt]"};
    boolean strIsSOp(String s){
        for(String op : SINGLEOPERANDOPERATORS){
            if(op.equals(s)) return true;
        }
        return false;
    }
    private String[] WORDOPERATORS = {"sqrt"};
    private String[][] ORDEROFOPERATIONS = {
        {"[sqrt]", "^"},
        {"*", "/"},
        {"+", "-"}
    };
    boolean opIsInSet(String str, int i){
        for(String op : ORDEROFOPERATIONS[i]){
            if(op.equals(str)) return true;
        }
        return false;
    }
    
    
    
    private HashMap<String, Double> specialSymbols = new HashMap<String, Double>();
    
    private HashMap<String, Double> letterDefs = new HashMap<String, Double>();
    public HashMap<String, Double> letterDefs(){
        return letterDefs;
    }



    private ArrayList<String> equStrDatas;
    public ArrayList<String> equStrDatas(){
        return equStrDatas;
    }

    private Expression expression;
    public Expression expression(){
        return expression;
    }
    
    private String prefixNot = "", postfixNot = "";
    public String prefixNot(){
        return prefixNot;
    }
    public String postfixNot(){
        return postfixNot;
    }
    public String rawString(){ return expression.rawStr();}
    String parenthesizedExprStr;
    public String getParenthesizedExprStr(){
        return parenthesizedExprStr;
    }
    

    private boolean validExpr;
    public boolean isValidExpression(){
        return validExpr;
    }

    
    public ExpressionStringProcessor(){
        specialSymbols.put("π", Math.PI);
        
        validExpr = false;
    }
    public ExpressionStringProcessor(String exprStr){
        init(exprStr);
    }
    public ExpressionStringProcessor(String exprStr, HashMap<String, Double> letterDefs){
        this.letterDefs = letterDefs;
        
        init(exprStr);
    }
    void init(String expressionStr){
        expression = new Expression(expressionStr);
        
        specialSymbols.put("π", Math.PI);
        
        if(expressionStr.indexOf("e") != -1 && !letterDefs.containsKey("e")){
            int eDef = InputManager.numberInput("What is e in your expression?\n1) Define myself\n2) Euler's Number\n3) Elementary Charge", true, 1, 3);
            if(eDef == 1) letterDefs.put("e", null);
            else if(eDef == 2) specialSymbols.put("e", Math.E);
            else if(eDef == 3) specialSymbols.put("e", 1.60217663E-19);
        }
        
        //Change string to list of components
        equStrDatas = processExprString(expressionStr);
        
        //Make sure components are valid
        validExpr = seeIfValidExpression();
        
        if(validExpr){
            fullyParenthesize();
            parenthesizedExprStr = "";
            for(String s : equStrDatas){
                parenthesizedExprStr += s;
            }
            
            if(!validExpr) return; //Stop if parentheses messed up
            
            System.out.println("\nSuccessfully parenthesized operators!\n"+parenthesizedExprStr+"\n");
            System.out.println(equStrDatas);
            
            //Write in prefix and postfix, find height, get operation tree
            
            Operand expressionTreeOrValue = getEvaluationTree(equStrDatas, expression.operationTreeOrValue());
            
            expression.expressionProcessed(parenthesizedExprStr, expressionTreeOrValue, findHeightOfExpression());
            
            traverseTreePrefix(expression.operationTreeOrValue());
            traverseTreePostfix(expression.operationTreeOrValue());
        }
    }
    
    
    void traverseTreePrefix(Operand branch){
        if(branch.isValue()){
            prefixNot += " " + branch.value();
            System.out.println("tyutyutyu: " +prefixNot);
            return;
        }
        
        
        //Root left right
        
        //Operator
        prefixNot += " " + branch.op().operatorStr();
        System.out.println("dfgdfgdfg: " +prefixNot);
        
        //First operand
        if(branch.op().op1().isValue()){
            prefixNot += " " + branch.op().op1().value();
        }
        else traverseTreePrefix(branch.op().op1());
        
        //Second operand
        if(!branch.op().isSingleOperandOperation()){
            if(branch.op().op2().isValue()){
                prefixNot += " " + branch.op().op2().value();
            }
            else traverseTreePrefix(branch.op().op2());
        }
    }
    void traverseTreePostfix(Operand branch){
        if(branch.isValue()){
            postfixNot += " " + branch.value();
            return;
        }
        
        
        //left right root
        
        //First operand
        if(branch.op().op1().isValue()){
            postfixNot += " " + branch.op().op1().value();
        }
        else traverseTreePostfix(branch.op().op1());
        
        //Second operand
        if(!branch.op().isSingleOperandOperation()){
            if(branch.op().op2().isValue()){
                postfixNot += " " + branch.op().op2().value();
            }
            else traverseTreePostfix(branch.op().op2());
        }
        
        //Operator
        postfixNot += " " + branch.op().operatorStr();
    }

    
    public String toString(){
        if(!validExpr) return "Invalid expression";
        
        return "Fully parenthesized: "+equStrDatas+"\n"+
        
        "Height of expression: "+expression.height()+"\n"+
        
        "Prefix version: "+prefixNot+"\n"+
        "Postfix version: "+postfixNot;
    }
    
    
    
    
    //Pre-check expression: if open not equal to close parentheses then going to fail
    //Or if expr = ()()()... then no data at all
    boolean seeIfValidExpression(){
        if(equStrDatas == null || equStrDatas.size() == 0){
            System.out.println("ERROR: Expression is null");
            return false;
        }
        
        int deltaParentheses = 0;
        
        for(int i=0; i<equStrDatas.size(); i++){
            if(equStrDatas.get(i).equals("(")) deltaParentheses++;
            else if(equStrDatas.get(i).equals(")")) deltaParentheses--;
        }
        
        if(deltaParentheses > 0) System.out.println("ERROR: "+deltaParentheses+" too many open parentheses");
        else if (deltaParentheses < 0) System.out.println("ERROR: "+(-deltaParentheses)+" too many closing parentheses");
        
        return deltaParentheses == 0;
    }
    
    //Separate string into list of numbers and symbols
    ArrayList<String> processExprString(String expressionStr){
        
        
        ArrayList<String> strDatas = new ArrayList<String>();
        
        expressionStr = expressionStr.replace(" ", ""); //Delete spaces
        
        //Replace square brackets with parentheses
        expressionStr = expressionStr.replace("[", "(");
        expressionStr = expressionStr.replace("]", ")");
        
        
        //Ask for word operations like is ln natural log or is it l*n
        //then add square brackets around word operations
        String subExpr = new String(expressionStr);
        int opIndex = 0;
        expressionStr = "";

        for(int i=0; i<WORDOPERATORS.length; i++){
            int j = subExpr.indexOf(WORDOPERATORS[i]);
            while(j >= 0){
                boolean isOp = InputManager.yesNoInput("Is "+WORDOPERATORS[i]+" at character "+opIndex+" an operation in your expression?");
                    
                expressionStr += subExpr.substring(0, j);
                if(isOp) expressionStr += "[";
                expressionStr += WORDOPERATORS[i];
                if(isOp) expressionStr += "]";
                
                subExpr = subExpr.substring(j+WORDOPERATORS[i].length());
                
                opIndex += j+WORDOPERATORS[i].length();
                
                j = subExpr.indexOf(WORDOPERATORS[i]);
            }
        }
        expressionStr += subExpr;
        
        
        //Separate useful data from input
        //i.e. (32+4)A -> [(,32,+,4,),*,A]
        String numberStr = "";
        for(int i=0; i< expressionStr.length(); i++){
            String thisChar = expressionStr.substring(i, i+1);
            //System.out.println("this char = "+thisChar);
            
            if(DIGITS.indexOf(thisChar) != -1){
                numberStr += thisChar;
            }
            else{
                if(numberStr != ""){
                    strDatas.add(numberStr);
                    numberStr = "";
                    
                    if(LETTERS.indexOf(thisChar) != -1 || thisChar.equals("(") || thisChar.equals("[")){
                        //Hidden multiplication detected: num * letter | num * (...)
                        strDatas.add("*");
                    }
                }
                if(thisChar.equals("[")){
                    int end = expressionStr.substring(i).indexOf("]") + i;
                    
                    strDatas.add(expressionStr.substring(i, end+1));
                    
                    i = end;
                    continue;
                }
                strDatas.add(thisChar);
                
                if(LETTERS.indexOf(thisChar) != -1){
                    if(i < expressionStr.length() - 1){
                        String nextChar = expressionStr.substring(i+1, i+2);
                        if(DIGITS.indexOf(nextChar) != -1 || LETTERS.indexOf(nextChar) != -1 || nextChar.equals("(")){
                            //Hidden multiplication detected: letter * something
                            strDatas.add("*");
                        }
                    }
                    
                    if(!letterDefs.containsKey(thisChar) && !specialSymbols.containsKey(thisChar)) letterDefs.put(thisChar, null);
                }
                else if(strIsOp(thisChar) || thisChar.equals("(") || thisChar.equals(")")){
                    if(i < expressionStr.length() - 1){
                        String nextChar = expressionStr.substring(i+1, i+2);
                        if(thisChar.equals(")")){
                            if(DIGITS.indexOf(nextChar) != -1 || LETTERS.indexOf(nextChar) != -1)
                            {
                                //Hidden multiplication detected
                                strDatas.add("*");
                            }
                        }
                        else if(strIsOp(nextChar)){
                            validExpr = false;
                            System.out.println("ERROR: missing number in between two operations at char "+i);
                            return null;
                        }
                    }
                    else if(thisChar.equals("(")){
                        validExpr = false;
                        System.out.println("ERROR: open parenthesis not allowed at end of expression");
                        return null;
                    }   
                }
                else{
                    validExpr = false;
                    System.out.println("ERROR: invalid symbol at char "+i);
                    return null;
                }
            }
        }
        if(numberStr != ""){
            strDatas.add(numberStr);
        }
        
        System.out.println(strDatas);
        
        return strDatas;
    }
    
    public void fullyParenthesize(){
        int currSet = 0;
        for(String[] ops : ORDEROFOPERATIONS){ //Row is set of ops to parenthesize concurrently
            for(String thisOp : ops){
                for(int i=0; i<equStrDatas.size(); i++){
                    if (equStrDatas.get(i).equals(thisOp)){
                        i = parenthesizeOperation(currSet, i); //Increase index if parenthesis was added before operation in list
                    
                        System.out.println("Next index: "+i);
                    }
                }
            }
            currSet++;
        }
    }
    
    int parenthesizeOperation(int opSet, int indexOfOp){
        if(indexOfOp < equStrDatas.size() - 1){
            //Recursively parenthesize inside ops first: ex   sqrtsqrt2 = (sqrt(sqrt2))
            if(opIsInSet(equStrDatas.get(indexOfOp+1), opSet)) parenthesizeOperation(opSet, indexOfOp+1);
        }
        
        System.out.println("Parenthesize operation at index "+indexOfOp);
        
        int leftParenthesisIndex = -1;
        
        //Insert left parenthesis
        
        if(strIsSOp(equStrDatas.get(indexOfOp))){
            //Check if parenthesis already there
            if(indexOfOp > 0){
                if(equStrDatas.get(indexOfOp - 1).equals("(")) leftParenthesisIndex = indexOfOp - 1;
                else{
                    equStrDatas.add(indexOfOp, "(");
                    indexOfOp++;
                }
            }
            else{
                //No parenthesis possible to the left so add one
                equStrDatas.add(0, "(");
                indexOfOp++;
            }
        }
        else{
            if(indexOfOp == 0){
                validExpr = false;
                System.out.println("ERROR: missing operand: operation at char 0");
                return equStrDatas.size(); //Set index to end of list to stop
            }
            
            if(indexOfOp == 1){
                //No parenthesis possible to the left so add one
                equStrDatas.add(0, "(");
                indexOfOp++;
            }
            else{
                //Check if parenthesis already there
                if(equStrDatas.get(indexOfOp - 2).equals("(")) leftParenthesisIndex = indexOfOp - 2;
                else if (equStrDatas.get(indexOfOp - 1).equals(")")){
                    //Operand is another operation so get to the other side of it
                    int level = 1;
                    int i = indexOfOp - 1;
                    while(level > 0){
                        i--;
                        if(i < 0){
                            validExpr = false;
                            System.out.println("ERROR: close parenthesis with no open at item "+(indexOfOp-2));
                            return equStrDatas.size(); //Set index to end of list to stop
                        }
                        
                        if(equStrDatas.get(i).equals(")")) level++;
                        else if (equStrDatas.get(i).equals("(")) level--;
                    }
                    
                    if(i>0 && equStrDatas.get(i-1).equals("(")){
                        //Parenthesis on other side already there
                        leftParenthesisIndex = i-1;
                    }
                    else{
                        equStrDatas.add(i, "(");
                        indexOfOp++;
                    }
                }
                else{
                    equStrDatas.add(indexOfOp - 1, "(");
                    indexOfOp++;
                }
            }
        }
        
        
        //Insert right parenthesis
        
        if(indexOfOp == equStrDatas.size() - 1){
            validExpr = false;
            System.out.println("ERROR: missing operand: operation at char "+(equStrDatas.size() - 1));
            return equStrDatas.size(); //Set index to end of list to stop
        }
        
        if(indexOfOp == equStrDatas.size() - 2){
            //No parenthesis possible to the right so add one
            equStrDatas.add(")");
        }
        else{
            //Check if parenthesis already there
            if(equStrDatas.get(indexOfOp + 2).equals(")") && leftParenthesisIndex >= 0)
            {
                //Both parentheses already there so don't add any
                return indexOfOp;
            }
            else if (equStrDatas.get(indexOfOp + 1).equals("(")){
                //Operand is another operation so get to the other side of it
                int level = 1;
                int i = indexOfOp + 1;
                while(level > 0){
                    i++;
                    if(i == equStrDatas.size()){
                        validExpr = false;
                        System.out.println("ERROR: open parenthesis with no close at item "+(indexOfOp+2));
                        return equStrDatas.size(); //Set index to end of list to stop
                    }
                    
                    if(equStrDatas.get(i).equals("(")) level++;
                    else if (equStrDatas.get(i).equals(")")) level--;
                }
                
                if(i == equStrDatas.size() - 1){
                    //Parenthesis on other side not already there
                    equStrDatas.add(i+1, ")");
                    
                    //We do actually need another left parenthesis so add one
                    if(leftParenthesisIndex >= 0){
                        equStrDatas.add(leftParenthesisIndex, "(");
                        indexOfOp++;
                    }
                }
                else if(!equStrDatas.get(i+1).equals(")")){
                    //Parenthesis on other side not already there
                    equStrDatas.add(i+1, ")");
                    
                    //We do actually need another left parenthesis so add one
                    if(leftParenthesisIndex >= 0){
                        equStrDatas.add(leftParenthesisIndex, "(");
                        indexOfOp++;
                    }
                }
            }
            else{
                equStrDatas.add(indexOfOp + 2, ")");
                
                //We do actually need another left parenthesis so add one
                if(leftParenthesisIndex >= 0){
                    equStrDatas.add(leftParenthesisIndex, "(");
                    indexOfOp++;
                }
            }
        }
        
        
        //System.out.println(equStrDatas);
        
        return indexOfOp;
    }
    
    
    //How many layers in the tree
    int findHeightOfExpression(){
        int maxHeight = 0;
        int height = 0;
        for(int i=0; i<equStrDatas.size(); i++){
            if(equStrDatas.get(i).equals("(")) height++;
            else if(equStrDatas.get(i).equals(")")){
                if(height > maxHeight) maxHeight = height;
                height = 0;
            }
        }
        return maxHeight;
    }
    
    Operand getEvaluationTree(List<String> datas, Operand branch){
        System.out.println("Analyzing sub-expression "+datas);
        
        branch = new Operand();
        
        //If just a value then return value
        if(!datas.contains("(")){
            branch = new Operand(datas.get(0));
            return branch;
        }
        
        int currLevel = 0;
        int openP = -1;
        
        for(int i=0; i<datas.size(); i++){
            String item = datas.get(i);
            
            if(item.equals("(")){
                currLevel++;
                if(currLevel == 1) openP = i;
            }
            if(item.equals(")")){
                currLevel--;
                //No operation found in parentheses so try again without those parentheses
                if(currLevel == 0) return getEvaluationTree(datas.subList(openP+1, i), branch);
            }
            
            if(currLevel == 1 && strIsOp(item)){
                System.out.println("Found operator "+item+" at i "+i);
                
                //Determine if operands are numbers or other operations and create new branch
                if(!strIsSOp(item)){
                    Operand leftBranch = getEvaluationTree(datas.subList(1, i), branch.op().op1());
                    
                    System.out.println("Left branch: "+leftBranch);
                    
                    Operand rightBranch = getEvaluationTree(datas.subList(i+1, datas.size() - 1), branch.op().op2());
                    
                    System.out.println("Right branch: "+rightBranch);
                    
                    branch = new Operand(new Operation(leftBranch, rightBranch, item));
                }
                else{
                    Operand nextBranch = getEvaluationTree(datas.subList(i+1, datas.size() - 1), branch.op().op1());
                    
                    System.out.println("Next branch: "+nextBranch);
                    branch = new Operand(new Operation(nextBranch, item));
                }
                
                break;
            }
        }
        
        System.out.println("done");
        
        return branch;
    }
    int endOfSubExpr(ArrayList<String> datas, int start){
        int currLevel = 0;
        
        for(int i=start; i<datas.size(); i++){
            String item = datas.get(i);
            
            if(item.equals("(")){
                currLevel++;
            }
            if(item.equals(")")){
                currLevel--;
                if(currLevel == 0) return i;
            }
        }
        return -1;
    }
    
    //Ask for variable inputs and evaluate numbers with operators
    public double evaluateExpression(){
        if(!validExpr) return 0;
        
        double answer;
        
        for(String l : letterDefs.keySet()){
            //System.out.println(l+" : "+letterDefs.get(l));
            //Ask user to define null letters
            if(letterDefs.get(l) == null){
                double def = InputManager.decimalInput("What value does "+l+" respresent?\n>>", false);
                letterDefs.put(l, def);
            }
            else{
                //Ask if change good letters
                System.out.println("\n"+l+" is "+letterDefs.get(l)+".\nDo you want to change it?");
                if(InputManager.yesNoInput(">>", false)){
                    double def = InputManager.decimalInput("What value does "+l+" respresent?\n>>", false);
                    letterDefs.put(l, def);
                }
            }
        }
        
        if(expression.isValue()) return Operation.getNumVal(expression.value(), letterDefs, specialSymbols);
        else answer = expression.expressionTree().evaluate(letterDefs, specialSymbols);
        
        return answer;
    }
}