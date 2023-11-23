package GUI.components;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
public class InputWindow extends JPanel{
    public JTextField playerName; 
    public JTextField cardName;
    public JButton submitBtn; 
    private MyLabel playerNameLabel;
    private MyLabel cardNameLabel;
    public InputWindow(){
        this.setVisible(true);
        playerName = new JTextField(20);
        cardName = new JTextField(20);
        submitBtn = new JButton("Ask");
        playerNameLabel = new MyLabel("What player do you want to ask?");
        cardNameLabel = new MyLabel("What card do you want?");

        this.setLayout(new GridLayout(5, 1));
        this.add(playerNameLabel);
        this.add(playerName);
        this.add(cardNameLabel);
        this.add(cardName);
        this.add(submitBtn);
    }
}
