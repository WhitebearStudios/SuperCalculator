import java.util.HashMap;

public class Expression extends Operand {
    private String rawStr;
    public String rawStr(){
        return rawStr;
    }
    
    
    
    public Expression(String value, String rawStr){
        super(value);
        
        this.rawStr = rawStr;
    }
    public Expression(Operation op, String rawStr){
        super(op);
        
        this.rawStr = rawStr;
    }
    public Expression(Operand op, String rawStr){
        if(op.isValue()){
            value = op.value();
            isValue = true;
        }
        else{
            this.op = op.op();
            isValue = false;
        }
        
        this.rawStr = rawStr;
    }
    
    
    public double evaluate(HashMap<String, Double> letterDefs){
        if(isValue()){
            return MathStatement.getNumVal(value(), letterDefs);
        }else{
            return op().evaluate(letterDefs);
        }
    }
    
    
    
    
    public String toString(){
        return rawStr;
    }
}