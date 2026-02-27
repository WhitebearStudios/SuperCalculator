import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class StringToExpression{
    //Helper constants and static data --------------------
    private static final String DIGITS = "1234567890";
    private static final String LETTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNMπμ";
    private static String[] OPERATORS = {"+", "-", "*", "/", "^", "[sqrt]"};
    private static boolean strIsOp(String s){
        for(String op : OPERATORS){
            if(op.equals(s)) return true;
        }
        return false;
    }
    private static  String[] DOUBLEOPERANDOPERATORS = {"+", "-", "*", "/", "^"};
    private static boolean isDOp(String s){
        for(String op : DOUBLEOPERANDOPERATORS){
            if(op.equals(s)) return true;
        }
        return false;
    }
    private static  String[] SINGLEOPERANDOPERATORS = {"[sqrt]"};
    private static boolean isSOp(String s){
        for(String op : SINGLEOPERANDOPERATORS){
            if(op.equals(s)) return true;
        }
        return false;
    }
    private static  String[] WORDOPERATORS = {"sqrt"};
    private static  String[][] ORDEROFOPERATIONS = {
        {"[sqrt]", "^"},
        {"*", "/"},
        {"+", "-"}
    };
    private static final int LASTOPSET = ORDEROFOPERATIONS.length-1;
    private static boolean opIsInSet(String str, int i){
        for(String op : ORDEROFOPERATIONS[i]){
            if(op.equals(str)) return true;
        }
        return false;
    }
    
    

    private static List<String> strDatas;
    private static HashMap<String, Double> lettersAndSymbolsFound = new HashMap<String, Double>();
    
    //-----------------------------------------------------
    public static LoneExpression stringToExpression(String str){
        boolean isValid = strToDataListPreProcess(str);
        
        if(!isValid){
            System.out.println("Noooooooooooooooooooo");
            return null;
        }
        else{
            Operand op = processExprBranch(strDatas, LASTOPSET);
            
            String prefix = ExpressionFunNotation.prefixNotation(op);
            String postfix = ExpressionFunNotation.postfixNotation(op);
            
            return new LoneExpression(new Expression(op, str), ExpressionFunNotation.height(), prefix, postfix, lettersAndSymbolsFound);
        }
    }
    
    public static Equation stringsToEquation(String l, String r){
       boolean isValid = strToDataListPreProcess(l);
        
        if(!isValid){
            System.out.println("Noooooooooooooooooooo");
            return null;
        }
        else{
            Operand op1 = processExprBranch(strDatas, LASTOPSET);
            
           isValid = strToDataListPreProcess(r, false);
           
           if(!isValid){
               System.out.println("Noooooooooooooooooooo2");
                return null;
           }
           else{
                Operand op2 = processExprBranch(strDatas, LASTOPSET);
               
                return new Equation(new Expression(op1, l), new Expression(op2, r), lettersAndSymbolsFound);
           }
        }
    }
    
    
    
    
    
    
    
    
    public static void errorPopUp(String error){
        JOptionPane.showMessageDialog(null, error);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //Processing
    static boolean strToDataListPreProcess(String expressionStr) {return strToDataListPreProcess(expressionStr, true);}
    static boolean strToDataListPreProcess(String expressionStr, boolean clearDefsFound){
        strDatas = new ArrayList<String>();
        if(clearDefsFound) lettersAndSymbolsFound.clear();
        boolean isValid = true;
        
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
                boolean isOp = JOptionPane.showConfirmDialog(null, "Is "+WORDOPERATORS[i]+" at character "+opIndex+" an operation in your expression?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION;
                    
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
        
        
        //Separate useful data in input
        //i.e. (32+4)A -> [(,32,+,4,),*,A]
        String numberStr = "";
        for(int i=0; i< expressionStr.length(); i++){
            String thisChar = expressionStr.substring(i, i+1);
            //System.out.println("this char = "+thisChar);
            
            //Find empty ()
            if(i<expressionStr.length()-1){
                if(thisChar.equals("(") && expressionStr.substring(i+1, i+2).equals(")")){
                    errorPopUp("ERROR: Empty parentheses at char "+i);
                    isValid = false;
                }
            }
            
            
            if(DIGITS.indexOf(thisChar) != -1){ //If char is a digit
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
                    if(thisChar.equals("e")){
                        if(JOptionPane.showConfirmDialog(null, "Is e at character "+i+" euler's number?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
                            lettersAndSymbolsFound.put(thisChar, Math.E);
                        }
                        else if(JOptionPane.showConfirmDialog(null, "Is e at character "+i+" the elementary charge?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
                            lettersAndSymbolsFound.put(thisChar, 1.6E-19);
                        }
                        else lettersAndSymbolsFound.put(thisChar, null);
                    }
                    else if(thisChar.equals("c")){
                        if(JOptionPane.showConfirmDialog(null, "Is c at character "+i+" the speed of light? (m/s)", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
                            lettersAndSymbolsFound.put(thisChar, 300000000.0);
                        }
                        else lettersAndSymbolsFound.put(thisChar, null);
                    }
                    else{
                        lettersAndSymbolsFound.put(thisChar, null);
                    }
                    
                    if(i < expressionStr.length() - 1){
                        String nextChar = expressionStr.substring(i+1, i+2);
                        if(DIGITS.indexOf(nextChar) != -1 || LETTERS.indexOf(nextChar) != -1 || nextChar.equals("(") || nextChar.equals("[")){
                            //Hidden multiplication detected: letter * something
                            strDatas.add("*");
                        }
                    }
                }
                else if(strIsOp(thisChar) || thisChar.equals("(") || thisChar.equals(")")){
                    if(i < expressionStr.length() - 1){
                        String nextChar = expressionStr.substring(i+1, i+2);
                        if(thisChar.equals(")")){
                            if(DIGITS.indexOf(nextChar) != -1 || LETTERS.indexOf(nextChar) != -1 || nextChar.equals("("))
                            {
                                //Hidden multiplication detected: ) * something
                                strDatas.add("*");
                            }
                        }
                        else if(strIsOp(nextChar)){
                            errorPopUp("ERROR: missing number in between two operations at char "+i);
                            isValid = false;
                        }
                    }
                    else if(thisChar.equals("(")){
                        errorPopUp("ERROR: open parenthesis not allowed at end of expression");
                        isValid = false;
                    }   
                }
                else{
                    errorPopUp("ERROR: invalid symbol at char "+i);
                    isValid = false;
                }
            }
        }
        //Add last number if still accumulating
        if(numberStr != ""){
            strDatas.add(numberStr);
        }
        
        System.out.println("Data list: "+strDatas);
        
        if(!isValid) return false;
        
        
        
        
        //Pre-check expression: if open not equal to close parentheses then going to fail
        if(strDatas.size() == 0){
            errorPopUp("ERROR: Expression is null");
            return false;
        }
        
        if(isDOp(strDatas.get(0))){
           errorPopUp("ERROR: Missing operand at char 0");
        }
        if(strIsOp(strDatas.get(strDatas.size()-1))){
            errorPopUp("ERROR: Missing operand at char "+(strDatas.size()-1));
        }
        
        int deltaParentheses = 0;
        
        for(int i=0; i<strDatas.size(); i++){
            if(strDatas.get(i).equals("(")) deltaParentheses++;
            else if(strDatas.get(i).equals(")")) deltaParentheses--;
        }
        
        if(deltaParentheses > 0){
            errorPopUp("ERROR: "+deltaParentheses+" too many open parentheses");
            return false;
        }
        else if (deltaParentheses < 0){
            errorPopUp("ERROR: "+(-deltaParentheses)+" too many closing parentheses");
            return false;
        }
        
        return true;
    }
    
    //Recursion!!!!
    //     /\
    //    /! \      
    //   -----
    //Fill in operand of current operation
    static Operand processExprBranch(List<String> strDatas, int opSet){
        System.out.println("OpSet: "+opSet+" analyzing: "+strDatas);
        /*if(opSet < 0){
            System.out.println("Errrorrrrrrrrrr");
            return null;
        }*/
        
        //Base case: is size is 1 then should be value
        if(strDatas.size() == 1) return new Operand(strDatas.get(0));
        
        //General Case
        //Find op is opSet:
        //If found then reduce list and repeat
        //Else move to next opSet
        int level = 0;
        for(int i=strDatas.size()-1; i>=0; i--){
            String s = strDatas.get(i);
            if(s.equals("(")) level++;
            else if(s.equals(")")) level--;
            
            if(opIsInSet(s, opSet) && level == 0){
                System.out.println("Next operation: "+s);
                if(isSOp(s)){
                    if(strDatas.get(i+1).equals("(")) return new Operand(new Operation(processExprBranch(strDatas.subList(i+2, strDatas.size()-1), LASTOPSET), s));
                    else if(isSOp(strDatas.get(i+1))) return new Operand(new Operation(processExprBranch(strDatas.subList(i+2, strDatas.size()), LASTOPSET), s));
                    else return new Operand(new Operation(processExprBranch(strDatas.subList(i+1, strDatas.size()), LASTOPSET), s));
                
                    
                }else{
                    List<String> left, right;
                    if(strDatas.get(i-1).equals(")")) left = strDatas.subList(1, i-1);
                    else left = strDatas.subList(0, i);
                    if(strDatas.get(i+1).equals("(")) right = strDatas.subList(i+2, strDatas.size()-1);
                    //else if(isSOp(strDatas.get(i+1))) right = strDatas.subList(i+2, strDatas.size());
                    else right = strDatas.subList(i+1, strDatas.size());
                    
                    return new Operand(new Operation(processExprBranch(left, LASTOPSET), processExprBranch(right, LASTOPSET), s));
                }
            }
        }
        //No operators in op set left: check next opSet
        //All operators should be removed by the time opSet = 0 and base case should activate
        return processExprBranch(strDatas, opSet-1);
    }
}