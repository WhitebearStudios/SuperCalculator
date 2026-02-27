import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SmallButton extends JButton{
    public static Insets  smallMargin = new Insets(4, 1, 4, 1);
    
    public SmallButton(String text){
        super(text);
        super.setMargin(smallMargin);
    }
}