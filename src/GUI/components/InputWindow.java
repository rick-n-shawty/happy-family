package GUI.components;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
public class InputWindow extends JPanel implements ActionListener{
    private JTextField playerName; 
    private JTextField cardName;
    private String name; 
    private String card;
    private JButton button; 
    private boolean isClicked = false; 
    public InputWindow(){
        this.setVisible(true);
        playerName = new JTextField(20);
        cardName = new JTextField(20);
        button = new JButton("submit");
        button.addActionListener(this);
        JLabel playerNameLabel = new JLabel("What player do you want to ask?");
        JLabel cardNameLabel = new JLabel("What card do you want?");

        this.setLayout(new GridLayout(5, 1));
        this.add(playerNameLabel);
        this.add(playerName);
        this.add(cardNameLabel);
        this.add(cardName);
        this.add(button);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println(playerName.getText());
        // System.out.println(cardName.getText());
        name = playerName.getText();
        card = cardName.getText();
        this.isClicked = true; 
    }
    public boolean isSubmitted(){
        return isClicked;
    }
    public String[] getInput(){
        isClicked = false;
        String [] arr = {name, card};
        // System.out.println("inputWindow: [Player, card]");
        name = "";
        card = "";
        return arr; 
    }
}
