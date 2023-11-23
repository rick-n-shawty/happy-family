package GUI.components;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EndGameWindow extends JPanel {
    private MyLabel winnerLabel;
    private MyLabel promptLabel;
    private JPanel winnerPanel; 
    private JPanel inputPanel; 
    private JButton yesBtn;
    private JButton noBtn;
    public EndGameWindow(){
        winnerPanel = new JPanel(); 
        winnerPanel.setLayout(new GridLayout(2, 1));
        winnerLabel = new MyLabel("",24);
        promptLabel = new MyLabel("Do you wish to play again?", 16);
        winnerPanel.add(winnerLabel);
        winnerPanel.add(promptLabel); 

        inputPanel = new JPanel(); 
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        yesBtn = new JButton("Yes");
        noBtn = new JButton("No");

        this.setVisible(true);
        this.setLayout(new GridLayout(2,1));
        this.add(winnerPanel);
        this.add(inputPanel); 
    }
    public JButton getYesBtn(){
        return this.yesBtn;
    }
    public JButton getNoBtn(){
        return this.noBtn;
    }
    public void setWinner(String winner){
        String text = winner + " WON";
        this.winnerLabel.setText(text);
    }
}
