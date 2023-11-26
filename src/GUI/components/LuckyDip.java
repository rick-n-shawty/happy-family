package GUI.components;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class LuckyDip extends JPanel {
    private MyLabel textLabel;
    public LuckyDip(){
        textLabel = new MyLabel("Lucky Dip!", 22);
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setVisible(true);
        this.setLayout(new GridLayout(1,1));
        this.add(textLabel); 
    }
    public void setLabelColor(Color color){
        this.setBackground(color);
        textLabel.setBackground(color);
        this.repaint();
    }
    public void setTextLabel(String text){
        textLabel.setText(text);
        this.revalidate();
    }
}
