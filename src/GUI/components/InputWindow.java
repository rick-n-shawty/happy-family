package GUI.components;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
public class InputWindow extends JPanel implements ActionListener{
    public JTextField playerName; 
    public JTextField cardName;
    private String name; 
    private String card;
    public JButton submitBtn; 
    private boolean isClicked = false; 
    public InputWindow(){
        this.setVisible(true);
        playerName = new JTextField(20);
        cardName = new JTextField(20);
        submitBtn = new JButton("submit");
        submitBtn.addActionListener(this);
        JLabel playerNameLabel = new JLabel("What player do you want to ask?");
        JLabel cardNameLabel = new JLabel("What card do you want?");

        this.setLayout(new GridLayout(5, 1));
        this.add(playerNameLabel);
        this.add(playerName);
        this.add(cardNameLabel);
        this.add(cardName);
        this.add(submitBtn);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Button was clicked");
        // System.out.println(playerName.getText());
        // System.out.println(cardName.getText());
        // name = playerName.getText();
        // card = cardName.getText();
        // this.isClicked = true; 
    }
}
