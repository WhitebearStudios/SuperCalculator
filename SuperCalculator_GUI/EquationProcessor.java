public class EquationProcessor{
    //Only one use of var plz
    public static void solveForVar(Equation equ, String v){
        Expression e1 = equ.expr1();
        Expression e2 = equ.expr2();
        
        boolean solveLeft = e1.toString().contains(v);
        
        Expression eWithVar, eResult;
        if(solveLeft){
            eWithVar = e1;
            eResult = e2;
            System.out.println("Var in left side");
        }else{
            eWithVar = e2;
            eResult = e1;
            System.out.println("Var in right side");
        }
        
        //Reduce eWithVar until var is operand
        int i=0;
        while(!(v+" ").equals(eWithVar.toString())){
            //System.out.println(eWithVar+"    =     "+eResult);
            
            if(eResult.isValue()){
                //System.out.println("v: "+getOperationValue(eWithVar, v).value()+" op: "+getOppositeOp(eWithVar.op().operatorStr()));
                Operation newOp = new Operation(new Operand(eResult.value()), getOperationValue(eWithVar, v), getOppositeOp(eWithVar.op().operatorStr()));
                eResult = new Expression(newOp, ExpressionFunNotation.exprToStr(new Operand(newOp)));
                
                Operand newWithVar = getOperationOp(eWithVar, v);
                eWithVar = new Expression(newWithVar, ExpressionFunNotation.exprToStr(newWithVar));
            }
            else{
                //System.out.println("v: "+getOperationValue(eWithVar, v).value()+" op: "+getOppositeOp(eWithVar.op().operatorStr()));
                Operation newOp = new Operation(new Operand(eResult.op()), getOperationValue(eWithVar, v), getOppositeOp(eWithVar.op().operatorStr()));
                eResult = new Expression(newOp, ExpressionFunNotation.exprToStr(new Operand(newOp)));
            
                Operand newWithVar = getOperationOp(eWithVar, v);
                eWithVar = new Expression(newWithVar, ExpressionFunNotation.exprToStr(newWithVar));
            }
            
            i++;
            if(i>100) break;
        }
        
        equ.rearrange(eWithVar, eResult);
    }
    
    private static String getOppositeOp(String operator){
        switch(operator){
            case "+":
                return "-";
            case "-":
                return "+";
            case "*":
                return "/";
            case "/":
                return "*";
            default:
                return "";
        }
    }
    
    private static Operand getOperationOp(Expression expr, String v){
        //System.out.println("ExprOp: "+expr);
        if(!expr.op().op1().isValue()){
            return new Operand(expr.op().op1().op());
        }
        else if (!expr.op().op2().isValue()){
            return new Operand(expr.op().op2().op());
        }
        else if(v.equals(expr.op().op1().value()) || v.equals(expr.op().op2().value())){
            return new Operand(v);
        }
        else return null;
    }
    //Choose the operand of the expression that is a number but not the var we are solving for
    private static Operand getOperationValue(Expression expr, String v){
        //System.out.println("ExprVal: "+expr);
        if(expr.op().op1().isValue() && !v.equals(expr.op().op1().value())){
            return new Operand(expr.op().op1().value());
        }
        else if (expr.op().op2().isValue() && !v.equals(expr.op().op2().value())){
            return new Operand(expr.op().op2().value());
        }
        else return null;
    }
}