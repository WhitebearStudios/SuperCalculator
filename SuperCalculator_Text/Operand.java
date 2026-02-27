public class Operand {
    private String value;
    private Operation op;
    
    public String value(){return value;}
    public Operation op(){return op;}
    
    public Operand(){
        op = new Operation();
    }
    public Operand(String value){
        this.value = value;
        isValue = true;
    }
    public Operand(Operation op){
        this.op = op;
        isValue = false;
    }
    
    private boolean isValue;
    public boolean isValue(){return isValue;}
    
    public String toString(){
        if(isValue()) return "Val: "+value;
        else return "Operation "+op.operatorStr();
    }
}