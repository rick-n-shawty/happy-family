package GUI;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logic.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.jar.JarEntry;
import GUI.components.MyIcon;
import GUI.components.MyLabel;
import assets.Const;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
public class PlayerPanel extends JPanel{
    private Player player;
    private JPanel infoPanel; 
    private MyLabel nameInfoLabel;
    private MyLabel familiesInfoLabel;
    private MyLabel cardsInfoLabel;
    private JPanel cardHolderPanel;
    private MyIcon cardBackIcon;
    private JLabel cardLabel;
    public PlayerPanel(Player player, Boolean isBot){
        // layout for the bots 
        this.player = player;
        this.setVisible(true);
        this.setLayout(new GridLayout(1, 2));
        this.setBackground(player.getColor());
        
        infoPanel = new JPanel(); 
        infoPanel.setLayout(new GridLayout(3, 1));
        nameInfoLabel = new MyLabel("Name: " + player.getName());
        nameInfoLabel.setVisible(true);
        familiesInfoLabel = new MyLabel("Families: " + player.getCollectedFamilies());
        familiesInfoLabel.setVisible(true);
        cardsInfoLabel = new MyLabel("Cards: " + player.getCardsNum());
        cardsInfoLabel.setVisible(true);

        infoPanel.setOpaque(false);
        infoPanel.add(nameInfoLabel);
        infoPanel.add(cardsInfoLabel);
        infoPanel.add(familiesInfoLabel);
        
        cardHolderPanel = new JPanel();
        cardHolderPanel.setLayout(new GridBagLayout());
        cardLabel = new JLabel(); 
        cardLabel.setVisible(true);
        cardBackIcon = new MyIcon("/Users/sultkh/Desktop/HappyFam/res/images/Back-card.png");

        cardLabel.setIcon(cardBackIcon);
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardHolderPanel.add(cardLabel);
        
        this.add(infoPanel);
        this.add(cardHolderPanel);
    }
    public void updateBotInfo(){
        cardsInfoLabel.setText("Cards: " + player.getCardsNum());
        this.revalidate();
        this.repaint();
    }
    public Point getCardPositon(){
        if(this.cardLabel == null){
            // for the position of the player 
            Point point = new Point(Const.FRAME_WIDTH - 300, 435); 
            return point;
        }
        return this.cardLabel.getLocationOnScreen();
    }
    // FOR PLAYER 
    private JPanel cardContainer;
    private JPanel familiesContainer; 
    

    public PlayerPanel(Player player){
        // layout for the main player
        this.setVisible(true);
        this.player = player; 
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setOpaque(true);
        this.setBackground(Color.GREEN);
        cardContainer = new JPanel(); 
        cardContainer.setVisible(true);
        cardContainer.setBackground(Color.BLUE);
        cardContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
        cardContainer.setPreferredSize(new Dimension(Const.FRAME_WIDTH - 150, 280));

        familiesContainer = new JPanel(); 
        familiesContainer.setBackground(Color.RED);
        familiesContainer.setPreferredSize(new Dimension(150, 280));

        this.add(cardContainer);
        this.add(familiesContainer);
    }
    public void attachCards(){
        if(!this.player.isBot()){
            // attach cards on hand
            this.cardContainer.removeAll();
            String [] hand = this.player.getHand(); 
            for(int i = 0; i < hand.length; i++){
                JLabel cardLabel = new JLabel(); 
                cardLabel.setVisible(true);
                cardLabel.setOpaque(true);
                MyIcon cardFaceIcon = new MyIcon("/Users/sultkh/Desktop/HappyFam/res/images/faceImages/" + hand[i] + ".png"); 
                cardLabel.setIcon(cardFaceIcon);
                this.cardContainer.add(cardLabel);
            }

            // display collected families 
            this.familiesContainer.removeAll();
            String [] families = player.getCollectedFamiliesNames();
            familiesContainer.setLayout(new GridLayout(families.length + 1,1));
            MyLabel tempLabel = new MyLabel("Collected families: ");
            familiesContainer.add(tempLabel); 
            for(int i = 0; i < families.length; i++){
                if(families[i] != null){
                    tempLabel = new MyLabel(i + 1 + " " +families[i]);
                    familiesContainer.add(tempLabel);
                }
            }
            cardContainer.revalidate();
            cardContainer.repaint();
        }
    }

    
}
