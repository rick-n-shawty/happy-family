package GUI.components;

import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.Color;


public class BotDialogWindow extends JPanel{
    private MyLabel qLabel;
    private MyLabel aLabel; 
    private String question;
    private String answer; 
    public BotDialogWindow(){
        
        qLabel = new MyLabel(question,16);
        qLabel.setVisible(true); 
        qLabel.setOpaque(true);
        aLabel = new MyLabel(answer,16);
        aLabel.setVisible(true);
        aLabel.setOpaque(true);
        
        this.setVisible(true);
        this.setLayout(new GridLayout(2, 1));
        this.add(qLabel);
        this.add(aLabel);
    }
    public void setQuestion(String q, Color color){
        this.question = q;
        this.answer = "";
        this.qLabel.setText(this.question);
        this.qLabel.setBackground(color);
        this.revalidate();
        this.repaint();
    }
    public void setAnswer(String a, Color color){
        this.answer = a; 
        this.aLabel.setText(a);
        this.aLabel.setBackground(color);
        this.revalidate();
        this.repaint();
    }
    public void clear(){
        this.answer = "";
        this.question = "";
    }
    

}
