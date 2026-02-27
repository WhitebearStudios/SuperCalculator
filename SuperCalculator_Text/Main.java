import java.util.Scanner;
import java.util.ArrayList;


//Word operation should be all lowercase

public class Main
{
    private static ExpressionStringProcessor expression1; 
    private static ExpressionStringProcessor expression2;
    private static EquationProcessor equation;
    
    public static void main(String[] args)
    {
        boolean quit = false;
        boolean newExpr = false;
        
        int choice = 0;
        
        String userStr = "";
        
        //Ask for input
        Scanner scanner = new Scanner(System.in);
        
        boolean equationMode;
        
        boolean valid;
        
        while(!quit)
        {
            valid = false;
            equationMode = false;
            
            while(!valid){
                System.out.println("Enter your expression or equation ('quit' to quit):");
            
                userStr = scanner.nextLine();
            
                if(userStr.toLowerCase().equals("quit")){
                    quit = true;
                    break;
                }
                
                System.out.println();
            
                if(userStr.indexOf("=") != -1){
                    System.out.println("Equation detected!\n");
                    
                    equationMode = true;
                    
                    String userExprStr1 = userStr.substring(0, userStr.indexOf("="));
                    String userExprStr2 = userStr.substring(userStr.indexOf("=") + 1, userStr.length());
                    
                    if(userExprStr2.indexOf("=") != -1){
                        System.out.println("Only two expressions equal or one expression allowed");
                        continue;
                    }
                
                    expression1 = new ExpressionStringProcessor(userExprStr1);
                    expression2 = new ExpressionStringProcessor(userExprStr2, expression1.letterDefs());
                    
                    equation = new EquationProcessor(expression1.expression(), expression2.expression());
                
                    valid = expression1.isValidExpression() && expression2.isValidExpression();
                }
                else{
                    System.out.println("Expression detected!\n");
                    
                    expression1 = new ExpressionStringProcessor(userStr);
                    
                    valid = expression1.isValidExpression();
                }
            }
            if(quit) break;
            
            InputManager.pause();
            
            while(!newExpr){
                InputManager.clearScreen();
                
                System.out.println("Your expression:\n"+userStr+"\n");
                
                if(equationMode){
                    choice = InputManager.numberInput("Choose Action:\n1) Quit\n2)Select left side\n3)Select right side\n4)Solve for variable", 1, 4);
                    
                    if(choice == 1){
                        quit = true;
                        break;
                    }
                    else if(choice == 2){
                        System.out.println();
                        System.out.println("Selected "+expression1.rawString()+"\n");
                        int quitOptions = expressionOptions(expression1);
                        
                        if(quitOptions == 1) break;
                        else if(quitOptions == 2){
                            quit = true;
                            break;
                        }
                    }
                    else if(choice == 3){
                        System.out.println();
                        System.out.println("Selected "+expression2.rawString()+"\n");
                        int quitOptions = expressionOptions(expression2);
                        
                        if(quitOptions == 1) break;
                        else if(quitOptions == 2){
                            quit = true;
                            break;
                        }
                    }
                    else{
                        if(expression1.letterDefs().size() == 0 && expression2.letterDefs().size() == 0){
                            System.out.println("\nYour equation has no variables!\n");
                        }
                        else{
                            
                        }
                    }
                }else{
                    //Expression mode
                    int quitOptions = expressionOptions(expression1);
                    if(quitOptions == 1) break;
                    else if(quitOptions == 2){
                        quit = true;
                        break;
                    }
                }
            }
            
            
        }
        
    }
    
    
    public static int expressionOptions(ExpressionStringProcessor expr){
        
        int choice = InputManager.numberInput("Choose Action:\n1)Overview\n2)Fully parenthesize\n3)Prefix notation\n4)Postfix Notation\n5)Evaluate\n6)New expression/equation\n7)Quit", 1, 7);
        
        if(choice == 1){
            System.out.println();
            System.out.println(expr);
            
            InputManager.pause();
        }
        else if(choice == 2){
            System.out.println();
            System.out.println(expr.expression().parenthesizedExprStr());
            
            InputManager.pause();
        }
        else if(choice == 3){
            System.out.println();
            System.out.println(expr.prefixNot());
            
            InputManager.pause();
        }
        else if(choice == 4){
            System.out.println();
            System.out.println(expr.postfixNot());
            
            InputManager.pause();
        }
        else if(choice == 5){
            System.out.println();
            System.out.println("Answer: "+expr.evaluateExpression());
            
            InputManager.pause();
        }
        else if(choice == 6) return 1;
        else if(choice == 7) return 2;
        
        return 0;
    }
    
}