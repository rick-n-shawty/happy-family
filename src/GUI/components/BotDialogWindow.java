package GUI.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;


public class BotDialogWindow extends JPanel implements ActionListener{
    private MyLabel qLabel;
    private MyLabel aLabel; 
    private String question;
    private String answer; 
    private JButton yesBtn;
    private JButton noBtn;
    private boolean isInputReady = false;
    private boolean isSaidYes;
    public BotDialogWindow(){
        yesBtn = new JButton("Yes");
        noBtn = new JButton("No");
        yesBtn.addActionListener(this);
        noBtn.addActionListener(this);
        
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
    
    public void setCustomLayout(boolean forBots){
        this.removeAll();
        if(forBots){
            this.setLayout(new GridLayout(2,1));
            this.add(qLabel);
            this.add(aLabel);
        }else{
            yesBtn = new JButton("Yes");
            noBtn = new JButton("No");
            this.setLayout(new GridLayout(3,1));
            yesBtn.addActionListener(this);
            noBtn.addActionListener(this);
            this.add(qLabel); 
            this.add(yesBtn);
            this.add(noBtn);
        }
        
        this.revalidate();
        this.repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(yesBtn)){
            isSaidYes = true;
        }else{
            isSaidYes = false; 
        }
        isInputReady = true;     
    }
    public boolean getReply(){

        return isSaidYes;
    }
    public boolean isInputReady(){
        boolean temp = isInputReady;
        isInputReady = false;
        return temp;
    }
}
