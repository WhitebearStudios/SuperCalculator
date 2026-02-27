import java.util.HashMap;

public class Operation {
    public enum SOpType implements OperationType{
        SQRT
    }
    public enum DOpType implements OperationType{
        ADD,
        SUB,
        MUL,
        DIV,
        POW
    }
    
    private boolean isSOp;
    public boolean isSingleOperandOperation(){ return isSOp;}
    
    private Operand op1;
    private Operand op2;
    
    public Operand op1(){return op1;}
    public Operand op2(){return op2;}
    
    
    OperationType getOperationType(String opStr){
        if(opStr.equals("+")) return DOpType.ADD;
        else if(opStr.equals("-")) return DOpType.SUB;
        else if(opStr.equals("*")) return DOpType.MUL;
        else if(opStr.equals("/")) return DOpType.DIV;
        else if(opStr.equals("^")) return DOpType.POW;
        
        
        else if(opStr.equals("[sqrt]")) return SOpType.SQRT;
        
        return null;
    }
    
    
    private OperationType operator;
    private String operatorStr; //Needed for easy operation to string conversion
    public String operatorStr(){return operatorStr;}
    
    
    static double getNumVal(String num, HashMap<String, Double> letterValues, HashMap<String, Double> specialSymbols){
        try{
            return Double.parseDouble(num);
        }
        catch(NumberFormatException e){
            if(letterValues.containsKey(num)) return letterValues.get(num);
            else if(specialSymbols.containsKey(num)) return specialSymbols.get(num);
            else{
                System.out.println("ERROR: letter "+num+" not defined");
                return 0;
            }
        }
    }
    
    
    
    public Operation(){} //Initialize to fill in child operations before this one
    public Operation(Operand operand, String opType){
        this.op1 = operand;
        
        operatorStr = opType;
        operator = getOperationType(opType);
        
        isSOp = true;
    }
    
    public Operation(Operand o1, Operand o2, String opType){
        this.op1 = o1;
        this.op2 = o2;
        
        operatorStr = opType;
        operator = getOperationType(opType);
        
        isSOp = false;
    }
    
    
    
    public double evaluate(HashMap<String, Double> letterValues, HashMap<String, Double> specialSymbols){
        double numVal1, numVal2;
        
        
        if(op1.isValue()) numVal1 = getNumVal(op1.value(), letterValues, specialSymbols);
        else numVal1 = op1.op().evaluate(letterValues, specialSymbols);
        
        
        if(isSingleOperandOperation()){
            if(operator == SOpType.SQRT){
                //System.out.println("sqrt "+num1);
                return Math.sqrt(numVal1);
            }
            
            System.out.println(operatorStr+" is an invalid single operand operation!");
        }
        else{
            if(op2.isValue()) numVal2 = getNumVal(op2.value(), letterValues, specialSymbols);
            else numVal2 = op2.op().evaluate(letterValues, specialSymbols);
            
            
            if(operator == DOpType.ADD){
                return numVal1 + numVal2;
            }
            else if (operator == DOpType.SUB){
                return numVal1 - numVal2;
            }
            else if (operator == DOpType.MUL){
                //System.out.println("* "+num1+" and "+num2);
                return numVal1 * numVal2;
            }
            else if (operator ==DOpType.DIV){
                return numVal1 / numVal2;
            }
            else if (operator == DOpType.POW){
                return Math.pow(numVal1, numVal2);
            }
            
            System.out.println(operatorStr+" is an invalid double operand operation!");
        }
        
        return 0.0;
    }
}