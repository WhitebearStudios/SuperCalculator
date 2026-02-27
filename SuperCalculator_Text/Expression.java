public class Expression {
    private String rawStr;
    public String rawStr(){
        return rawStr;
    }
    
    private String parenthesizedExprStr;
    public String parenthesizedExprStr(){
        return parenthesizedExprStr;
    }
    
    private Operand operationTreeOrValue;
    public Operand operationTreeOrValue() {return operationTreeOrValue;}
    
    public String value(){ return operationTreeOrValue.value(); }
    public Operation expressionTree() {return operationTreeOrValue.op(); }
    public boolean isValue() {return operationTreeOrValue.isValue();}
    
    
    private int height;
    public int height(){
        return height;
    }
    
    
    public void expressionProcessed(String parenthesizedExprStr, Operand operationTreeOrValue, int height){
        this.parenthesizedExprStr = parenthesizedExprStr;
        
        this.operationTreeOrValue = operationTreeOrValue;
        
        this.height = height;
    }
    public Expression(String rawStr){
        this.rawStr = rawStr;
        
        operationTreeOrValue = new Operand();
    }
    
    
    public String toString(){return rawStr;}
}