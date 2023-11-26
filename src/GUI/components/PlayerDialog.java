package GUI.components;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayerDialog extends JPanel {
    private JButton yesBtn; 
    private JButton noBtn; 
    private MyLabel titleLabel; 
    public PlayerDialog(){
        this.setVisible(true);
        yesBtn = new JButton("Yes"); 
        noBtn = new JButton("No"); 
        titleLabel = new MyLabel("",17);
        titleLabel.setOpaque(true);
        this.setLayout(new GridLayout(3,1));
        this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        this.add(titleLabel);
        this.add(yesBtn);
        this.add(noBtn); 
    }
    public JButton getYesBtn(){
        return this.yesBtn; 
    }
    public JButton getNoBtn(){
        return this.noBtn; 
    }
    public void setTitleText(String quest, Color color){
        titleLabel.setText(quest);
        titleLabel.setBackground(color);
    }
    public void hideBtns(){
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.setLayout(new GridLayout(1,1));
        this.remove(yesBtn);
        this.remove(noBtn);
        this.revalidate();
        this.repaint();
    }
    public void showBtns(){
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setLayout(new GridLayout(3,1));
        this.add(yesBtn);
        this.add(noBtn);
        this.revalidate();
        this.repaint();
    }
    public void clear(){
        titleLabel.setText("");
    }


}
