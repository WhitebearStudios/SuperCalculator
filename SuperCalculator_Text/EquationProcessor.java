public class EquationProcessor {
    Equation equation;
    
    public EquationProcessor(Expression expression1, Expression expression2){
        equation = new Equation(expression1, expression2);
    }
}