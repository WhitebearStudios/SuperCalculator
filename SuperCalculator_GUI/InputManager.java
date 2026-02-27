import javax.swing.JOptionPane;

public class InputManager{
    public static double decimalInput(String prompt){
        do{
            String i = JOptionPane.showInputDialog(prompt);
            
            try{
                return Double.parseDouble(i);
            }
            catch(NumberFormatException e){
                i = ""; //Do nothing
            }
        } while(true);
    }
    public static double intInput(String prompt){
        do{
            String i = JOptionPane.showInputDialog(prompt);
            
            try{
                return Integer.parseInt(i);
            }
            catch(NumberFormatException e){
                i = ""; //Do nothing
            }
        } while(true);
    }
}