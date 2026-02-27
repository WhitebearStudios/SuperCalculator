import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;

public class Main {
    private static String mainInput;
    
    private static JFrame f;
    private static JTextField input;
    private static JLabel title, title2;
    private static Font bigFont;
    private static JPanel p1, p2;
    private static JScrollPane p2Scroll;
    private static JButton solveForVar;
    
    private static boolean isEqu;
    private static LoneExpression expr;
    private static Equation equ;
    private static String defs[];
    
    private static Double currResult;
    
    private static ArrayList<JComponent> letterDefEntries = new ArrayList<JComponent>();
    
    public static void main(String[] args) {
        //creating instance of JFrame
        f= new JFrame();

        //Add menu stuff
        title = new JLabel("Start by entering an expression or equation:");
        title.setPreferredSize(new Dimension(400, 40)); // x, y, width, height
        title.setVerticalAlignment(SwingConstants.TOP);
        title.setForeground(Color.black);
        
        //Make font bigger
        Font currFont = title.getFont();
        bigFont = currFont.deriveFont(15f);
        
        title.setFont(bigFont);
        
        
        
        //Text entry
        input = new JTextField();
        input.setPreferredSize(new Dimension(500, 40));
        
        
        //Done button
        JButton done = new JButton("Done");
        done.setPreferredSize(new Dimension(80, 40));
        done.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            tryEnterEqu();
         }          
      });
        
        
        
        //Symbols label
        JLabel l1 = new JLabel("Symbols help:");
        l1.setPreferredSize(new Dimension(600, 30)); // x, y, width, height
        

        //Symbols buttons
        JButton sqrtB = new SmallButton("√");
        sqrtB.setPreferredSize(new Dimension(25, 25));
        
        sqrtB.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            addSymbol("sqrt");
         }          
      });
        
        JButton expB = new SmallButton("x^()");
        expB.setPreferredSize(new Dimension(35, 25));
    
        expB.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            addSymbol("^");
         }          
      });
      
      p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p1.setPreferredSize(new Dimension(600,550));
        
      p1.add(title);
      p1.add(input);
      p1.add(done);
      p1.add(l1);
      p1.add(sqrtB);
      p1.add(expB);
      
      
      //MENU 2-------------------------
      
      title2 = new JLabel("Start by entering an expression or equation:");
        title2.setPreferredSize(new Dimension(400, 125)); // x, y, width, height
        title2.setFont(bigFont);
        title2.setForeground(Color.black);
      
      
      //Setup panel
      p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setPreferredSize(new Dimension(600,550));
      p2.add(title2);
      
      
        p1.setBackground(Color.gray);
        p2.setBackground(Color.gray);
        
        
        menuNewLine(p2);
        
        
        //Evaluate button
        JButton evalB = new JButton("Evaluate");
        evalB.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            tryEvaluate();
         }          
        });
        p2.add(evalB);
        
        //Solve for var button
        solveForVar = new JButton("Solve for Variable");
        solveForVar.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showOptionDialog(
                null,                       // Parent component (null for default)
                "Choose a variable to solve for:",        // Message to display
                "",            // Title of the dialog
                JOptionPane.YES_NO_CANCEL_OPTION,  // Option type
                JOptionPane.QUESTION_MESSAGE,     // Message type
                null,                       // Icon (null for default)
                defs,                    // Array of button options
                defs[0]
            );
            
            EquationProcessor.solveForVar(equ, defs[choice]);
            updateTitle2();
         }          
        });
        solveForVar.setVisible(false);
        p2.add(solveForVar);
        
        
        //Back button---------------------------------------
        JButton newEqu = new JButton("New Expression");
        newEqu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            f.remove(p2Scroll);
            p1.setVisible(true);
            
            //Clear input field
            input.setText("");
            
            //Reset expr
            for(JComponent c : letterDefEntries){
                p2.remove(c);
            }
            letterDefEntries.clear();
            
            expr = null;
            equ = null;
            currResult = null;
         }          
      });
      
      
      //Init menu--------------------------------------------
       p2Scroll = new JScrollPane(p2);
        
        f.add(p1);
        //f.add(p2Scroll);
        f.add(newEqu, BorderLayout.PAGE_END);
        
        f.setSize(600, 300);
        //f.setLayout(new FlowLayout());
        f.setVisible(true);
    }
    
    static void addSymbol(String s){
        int cp = input.getCaretPosition();
        String currText = input.getText();
        //System.out.println(cp);
        //System.out.println(currText);
        input.setText(currText.substring(0, cp) + s+"()"+currText.substring(cp));
        input.setCaretPosition(cp+s.length()+1);
        input.requestFocus();
    }
    
    
    static void tryEnterEqu(){
        mainInput = input.getText();
        
        int equSign = mainInput.indexOf("=");
        if(equSign > -1){
            System.out.println("Equation mode!");
            isEqu = true;
            
            equ = StringToExpression.stringsToEquation(mainInput.substring(0, equSign), mainInput.substring(equSign+1));
            
            if(equ != null) modifyMenu();
        }
        else{
            System.out.println("Expression mode!");
            isEqu = false;
            
            expr = StringToExpression.stringToExpression(mainInput);
        
            if(expr != null) modifyMenu();
        }
    }
    
    
    private static void modifyMenu(){
        p1.setVisible(false);
        f.add(p2Scroll);
        
        //Display var value entries
        
        if(isEqu) defs = equ.defNames();
        else defs = expr.defNames();
        
        int cIndex = 2;
        
        for(int i=0; i<defs.length; i++){
            String v = defs[i];
            
            if(isEqu){
                if(equ.isLetterDefined(v)) continue;
            }else{
                if(expr.isLetterDefined(v)) continue;
            }
            
            JLabel lbl = new JLabel(v+":");
            lbl.setPreferredSize(new Dimension(25, 25));
            
            JTextField in = new JTextField();
            in.setPreferredSize(new Dimension(100, 25));
            // Listen for changes in the text
            in.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    warn();
                  }
                  @Override
                  public void removeUpdate(DocumentEvent e) {
                    warn();
                  }
                  @Override
                public void insertUpdate(DocumentEvent e) {
                    warn();
                }
    
                public void warn() {
                    boolean y=true;
                    String text = in.getText();
                    try{
                        double d = Double.parseDouble(text);
                    }
                    catch(NumberFormatException e){
                        y=false;
                        if(!text.substring(text.length()-1).equals(".")){
                            JOptionPane.showMessageDialog(null,
                                "Error: Please enter a number", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if(y){
                        if(isEqu) equ.addDef(v, Double.parseDouble(in.getText()));
                        else expr.addDef(v, Double.parseDouble(in.getText()));
                    }
                }
            });
            
            p2.add(lbl, cIndex);
            cIndex++;
            p2.add(in, cIndex);
            cIndex++;
            
            letterDefEntries.add(lbl);
            letterDefEntries.add(in);
            letterDefEntries.add(menuNewLine(p2, cIndex));
            cIndex++;
        }
        //Update ScrollPane
        p2Scroll.revalidate();
        
        
        
        if(isEqu){
            updateTitle2();
        
            if(!equ.expr1().isValue() || !equ.expr2.isValue()){
                solveForVar.setVisible(true);
            }
        
            
            
            
            
        }else{
            //Display result if no input needed
            if(expr.defsValid()){
                currResult = expr.evaluate();
                updateTitle1();
            }
            else updateTitle1();
        }
    }
    private static void tryEvaluate(){
        //Check if all letter defs are valid
        if(isEqu){
            if(equ.defsValid()){
                String[] lr = {"Left", "Right"};
                int side = JOptionPane.showOptionDialog(
                    null,                       // Parent component (null for default)
                    "Choose a side:",        // Message to display
                    "",            // Title of the dialog
                    JOptionPane.YES_NO_CANCEL_OPTION,  // Option type
                    JOptionPane.QUESTION_MESSAGE,     // Message type
                    null,                       // Icon (null for default)
                    lr,                    // Array of button options
                    "Left"
                );
                
                if(side == 0){
                    currResult = equ.evalLeft();
                    updateTitle2();
                }
                else{
                    currResult = equ.evalRight();
                    updateTitle2();
                }
            }
            else StringToExpression.errorPopUp("Cannot evaluate because some letters aren't defined!");
        }
        else{
            if(expr.defsValid()){
                currResult = expr.evaluate();
                
                updateTitle1();
            }
            else StringToExpression.errorPopUp("Cannot evaluate because some letters aren't defined!");
        }
    }
    
    public static JLabel menuNewLine(JPanel p){
        JLabel nl = new JLabel();
        nl.setPreferredSize(new Dimension(800, 1));
        p.add(nl);
        return nl;
    }
    public static JLabel menuNewLine(JPanel p, int index){
        JLabel nl = new JLabel();
        nl.setPreferredSize(new Dimension(800, 1));
        p.add(nl, index);
        return nl;
    }
    
    
    private static void updateTitle1(){
        title2.setText("<html>Expression processed successfully!<br><br>"+expr
            +"<br><br>Prefix Notation: "+expr.prefixNot()+"<br>Postfix Notation: "+expr.postfixNot()+
            "<br>Result: "+currResult);
    }
    private static void updateTitle2(){
        title2.setText("<html>Equation processed successfully!<br><br>"+equ+"<br><br>Result: "+currResult);
    }
}