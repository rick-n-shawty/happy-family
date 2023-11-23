package GUI.components;

import java.awt.GridBagLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class LuckyDip extends JPanel {
    private MyLabel textLabel;
    public LuckyDip(){
        textLabel = new MyLabel("Lucky Dip!", 20);
        this.setVisible(true);
        this.setLayout(new GridBagLayout());
        this.add(textLabel); 
    }
    public void setLabelColor(Color color){
        textLabel.setBackground(color);
    }
    public void setTextLabel(String text){
        textLabel.setText(text);
    }
}
