import java.util.Scanner;
import java.util.InputMismatchException;

public class InputManager {
    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void pause(){
        System.out.println("\nPress Enter to Continue\n");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
    
    public static boolean yesNoInput(String prompt, boolean answerOnNewLine){
        Scanner scanner = new Scanner(System.in);
        String in;
        
        while(true){
            if(answerOnNewLine) System.out.println(prompt);
            else System.out.print(prompt);
            
            
            in = scanner.nextLine();
            
            in = in.toLowerCase();
            
            if(in.equals("y") || in.equals("yes")){
                return true;
            }
            else if(in.equals("n") || in.equals("no")){
                return false;
            }
        }
    }
    public static boolean yesNoInput(String prompt){
        return yesNoInput(prompt, true);
    }
    
    
    
    
    public static int numberInput(String prompt){
        return numberInput(prompt, true);
    }
    public static int numberInput(String prompt, boolean answerOnNewLine){
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            if(answerOnNewLine) System.out.println(prompt);
            else System.out.print(prompt);
            
            
            try{
                return scanner.nextInt();
            }
            catch(InputMismatchException e){
                continue;
            }
        }
    }
    public static int numberInput(String prompt, int min, int max){
        return numberInput(prompt, true, min, max);
    }
    public static int numberInput(String prompt, boolean answerOnNewLine, int min, int max){
        Scanner scanner = new Scanner(System.in);
        
        int userInput = min - 1;
        
        while(true){
            if(answerOnNewLine) System.out.println(prompt);
            else System.out.print(prompt);
            
            try{
                userInput = scanner.nextInt();
                
                if(userInput >= min && userInput <= max) break;
                else System.out.println("Number must be at or between "+min+" and "+max);
            }
            catch(InputMismatchException e){
                System.out.println("That is not a number.");
            }
            
            scanner.nextLine();
        }
        
        return userInput;
    }
    
    
    
    
    
    
    public static double decimalInput(String prompt){
        return decimalInput(prompt, true);
    }
    public static double decimalInput(String prompt, boolean answerOnNewLine){
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            if(answerOnNewLine) System.out.println(prompt);
            else System.out.print(prompt);
            
            
            try{
                return scanner.nextDouble();
            }
            catch(InputMismatchException e){
                continue;
            }
        }
    }
    public static double decimalInput(String prompt, double min, double max){
        return decimalInput(prompt, true, min, max);
    }
    public static double decimalInput(String prompt, boolean answerOnNewLine, double min, double max){
        Scanner scanner = new Scanner(System.in);
        
        double userInput = min - 1;
        
        while(true){
            if(answerOnNewLine) System.out.println(prompt);
            else System.out.print(prompt);
            
            try{
                userInput = scanner.nextDouble();
                
                if(userInput >= min && userInput <= max) break;
                else System.out.println("Number must be at or between "+min+" and "+max);
            }
            catch(InputMismatchException e){
                System.out.println("That is not a number.");
            }
            
            scanner.nextLine();
        }
        
        return userInput;
    }
}