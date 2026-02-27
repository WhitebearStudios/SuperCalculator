import java.util.HashMap;

public class Equation extends MathStatement {
    //# or Operation = # or operation
    Expression expr1;
    public Expression expr1(){return expr1;}
    Expression expr2;
    public Expression expr2(){return expr2;}
    
    public Equation(Expression firstExpression, Expression secondExpression, HashMap<String, Double> letterDefs){
        expr1 = firstExpression;
        expr2 = secondExpression;
        
        this.letterDefs = letterDefs;
    }
    public void rearrange(Expression e1, Expression e2){
        expr1 = e1;
        expr2 = e2;
    }
    
    public double evalLeft(){
        return expr1.evaluate(letterDefs);
    }
    public double evalRight(){
        return expr2.evaluate(letterDefs);
    }
    
    
    public String toString(){
        return expr1+" = "+expr2;
    }
}