package GUI;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import logic.Player;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.GridLayout;
import GUI.components.MyIcon;
import GUI.components.MyLabel;
import assets.Const;
import javax.imageio.ImageIO;
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
    private String CARD_BACK_PATH = "../res/images/Back-card.png";
    // private URL imageUrl; 
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
        cardHolderPanel.setBackground(player.getColor());
        cardHolderPanel.setLayout(new GridBagLayout());
        cardLabel = new JLabel(); 
        cardLabel.setVisible(true);

        Path imagePath = Paths.get(CARD_BACK_PATH);
        try{
            BufferedImage image = ImageIO.read(imagePath.toFile());
            cardBackIcon = new MyIcon(image);
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("ATTEMPT TO READ1:" + imagePath.toAbsolutePath());
        }
        // cardBackIcon = new MyIcon("res/images/Back-card.png");
        
        cardLabel.setIcon(cardBackIcon);
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardHolderPanel.add(cardLabel);
        
        this.add(infoPanel);
        this.add(cardHolderPanel);
    }
    public void updateBotInfo(){
        cardsInfoLabel.setText("Cards: " + player.getCardsNum());
        familiesInfoLabel.setText("Families: " + player.getCollectedFamilies());
        this.revalidate();
        this.repaint();
    }
    public Point getCardPositon(){
        Point location;
        if(this.cardLabel == null){
            // for the position of the player
            int x = (int)(cardContainer.getWidth()/2); 
            int y = 0;
            return new Point(x,y);
        }
        location = this.cardLabel.getLocation();
        Point newLocation = SwingUtilities.convertPoint(cardHolderPanel, location, this);
        return newLocation;
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
        cardContainer = new JPanel(); 
        cardContainer.setVisible(true);
        cardContainer.setBackground(player.getColor());
        cardContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
        cardContainer.setPreferredSize(new Dimension(Const.FRAME_WIDTH - 150, 280));
        
        familiesContainer = new JPanel(); 
        familiesContainer.setBackground(player.getColor());
        familiesContainer.setPreferredSize(new Dimension(150, 280));
        
        this.add(cardContainer);
        this.add(familiesContainer);
    }
    public void resize(int newWidth, int newHeight){
        if(!player.isBot()){
            cardContainer.setPreferredSize(new Dimension((int)(this.getWidth() * 0.80), this.getHeight()));
            familiesContainer.setPreferredSize(new Dimension((int)(this.getWidth() * 0.20), this.getHeight()));
            this.revalidate();
            this.repaint();
        }
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
                MyIcon cardFaceIcon; 
                Path imagePath = Paths.get("../res/images/faceImages/" + hand[i] + ".png");
                try{
                    // String str = "res/images/faceImages/" + hand[i] + ".png";
                    BufferedImage image = ImageIO.read(imagePath.toFile());
                    cardFaceIcon = new MyIcon(image);
                    cardLabel.setIcon(cardFaceIcon);
                }catch(IOException e){
                    e.printStackTrace();
                    // System.out.println("PLAYER PANEL ISSUE ATTACH");
                    System.out.println("ATTEMPT TO READ2:" + imagePath.toAbsolutePath());
                }
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
