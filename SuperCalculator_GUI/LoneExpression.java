import java.util.HashMap;

public class LoneExpression extends MathStatement{
    
    
    Expression expr;
    public Expression getExpr(){return expr;}
    
    
    private int height;
    public int height(){
        return height;
    }
    private String prefixNot;
    public String prefixNot(){
        return prefixNot;
    }
    private String postfixNot;
    public String postfixNot(){
        return postfixNot;
    }
    
    
    
    public LoneExpression(Expression expr, int height, String prefixNot, String postfixNot, HashMap<String, Double> letterDefs){
        this.expr = expr;
        
        this.height = height;
        this.prefixNot = prefixNot;
        this.postfixNot = postfixNot;
        
        this.letterDefs = letterDefs;
    }
    
    public double evaluate(){
        return expr.evaluate(letterDefs);
    }
    
    public String toString(){return expr.toString();}
}