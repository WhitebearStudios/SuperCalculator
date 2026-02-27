public class ExpressionFunNotation{
    private static int h;
    public static int height(){return h;}
    
    
    public static String prefixNotation(Operand op){return prefixBranch(op);}
    private static String prefixBranch(Operand op){
        if(op.isValue()) return op.value()+" ";
        h++;
        
        //Root
        String s = op.op().operatorStr() + " ";
        
        //Left
        if(op.op().isSingleOperandOperation())return s + prefixBranch(op.op().op1())+" ";
        s += prefixBranch(op.op().op1())+" ";
        
        //Right
        return s + prefixBranch(op.op().op2())+" ";
    }
    
    
    
    
    
    public static String postfixNotation(Operand op){return postfixBranch(op);}
    private static String postfixBranch(Operand op){
        if(op.isValue()) return op.value()+" ";
        
        //Left
        String s;
        h++;
        if(op.op().isSingleOperandOperation()) s = prefixBranch(op.op().op1())+" ";
        else {
            s = postfixBranch(op.op().op1())+" ";
        
            //Right
            s += postfixBranch(op.op().op2())+" ";
        }
        
        //Root
        return s + op.op().operatorStr() + " ";
    }
    
    
    public static String exprToStr(Operand op){return exprToStrBranch(op);}
    private static String exprToStrBranch(Operand op){
        if(op.isValue()) return op.value()+" ";
        
        String s;
        
        if(op.op().isSingleOperandOperation()){
            //Root
            s = "("+op.op().operatorStr() + " ";
            
            //Left (actually right)
            return s + exprToStrBranch(op.op().op1())+") ";
        }
        else{
            //Left
            s = "("+exprToStrBranch(op.op().op1())+" ";
            //Root
            s += op.op().operatorStr()+" ";
            //Right
            s += exprToStrBranch(op.op().op2())+") ";
            //System.out.println(s);
            
            return s;
        }
    }
}