package GUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import logic.CardDeck;
import logic.Game;
import logic.Player;
import assets.Const;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;

import GUI.components.InputWindow;
import GUI.components.MyIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.text.PlainDocument;

public class GamePanel extends JPanel implements ActionListener {
    private final int PANEL_WIDTH = 850;
    private final int PANEL_HEIGHT = 650;
    private final int TIMER_DELAY = 1;
    JPanel topPanel = new JPanel();
    InputWindow inputWindow = new InputWindow();
    JPanel centerPanel = new JPanel();
    JPanel leftCenter;
    Player[] players;
    CardDeck cardDeck;
    JLabel deckIconLabel = new JLabel();
    JLabel deckCountLabel = new JLabel("Number of cards: ");
    PlayerPanel playerPanel;
    Timer timer;
    int xVelocity = 1;
    int yVelocity = 1;
    int x = Const.CARD_DECK_X;
    int y = Const.CARD_DECK_Y;
    double destinationX = 0;
    double destinationY = 0;

    Player animatedPlayer; 

    private String playerNameInput; 
    private String cardNameInput; 
    
    MyIcon cardBackImg = new MyIcon("/Users/sultkh/Desktop/HappyFam/res/images/Back-card.png");
    public GamePanel(Player[] players, CardDeck deck) {
        timer = new Timer(TIMER_DELAY, this);
        this.players = players;
        this.cardDeck = deck;
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());

        topPanel.setPreferredSize(new Dimension(this.getWidth(), 120));
        topPanel.setLayout(new GridLayout(1, 3));

        centerPanel.setPreferredSize(new Dimension(this.getWidth(), 250));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setBackground(Color.MAGENTA);
        leftCenter = new JPanel();
        leftCenter.setPreferredSize(new Dimension(PANEL_WIDTH - 150, 250));
        leftCenter.setBackground(Color.ORANGE);
        leftCenter.setLayout(new GridBagLayout());

        JPanel rightCenter = new JPanel();
        rightCenter.setLayout(new BorderLayout());
        rightCenter.setPreferredSize(new Dimension(150, 250));
        rightCenter.setBackground(Color.BLUE);

        deckIconLabel.setVisible(true);
        deckIconLabel.setOpaque(false);
        deckIconLabel.setPreferredSize(new Dimension(150, 200));
        MyIcon deckIcon = new MyIcon("/Users/sultkh/Desktop/HappyFam/res/images/Back-card.png");
        deckIconLabel.setIcon(deckIcon);
        deckIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deckCountLabel.setVisible(true);
        deckCountLabel.setOpaque(true);
        deckCountLabel.setPreferredSize(new Dimension(150, 50));
        
        rightCenter.add(deckIconLabel, BorderLayout.NORTH);
        rightCenter.add(deckCountLabel, BorderLayout.SOUTH);
        
        centerPanel.add(leftCenter);
        centerPanel.add(rightCenter);
        
        inputWindow.submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                playerNameInput = inputWindow.playerName.getText();
                cardNameInput = inputWindow.cardName.getText();
            }
        });
        createPanels();
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(playerPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(cardBackImg.getImage(), x, y, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Destination: " + destinationX + " " + destinationY);
        // System.out.println("Origin: " + x + " " + y);
        updateCoordinates();
        double deltaX = Math.round(x - destinationX);
        double deltaY = Math.round(y - destinationY);
        // System.out.println(destinationX + " " + destinationY);
        // System.out.println("Deltas: " + deltaX + " " + deltaY);
        if(deltaX > 0){
            x -= xVelocity;
        }else if(deltaX < 0){
            x += xVelocity;
        }else if(deltaX == 0){
            x = (int)destinationX;
        }
        if(deltaX == 0 && deltaY > 0){
            y -= yVelocity; 
        }else if(deltaX == 0 && deltaY < 0){
            y += yVelocity; 
        }else if(deltaX == 0 && deltaY == 0){
            y = (int) destinationY;
            timer.stop(); 
            x = Const.CARD_DECK_X;
            y = Const.CARD_DECK_Y;
        }
        this.repaint();
    }

    public void dealCard(Player player) {
        animatedPlayer = player; 
        Point cardLocation = player.getLocation();
        SwingUtilities.convertPointFromScreen(cardLocation, this);
        destinationX = cardLocation.getX() - this.getLocationOnScreen().x;
        destinationY = cardLocation.getY() - this.getLocationOnScreen().y;
        timer.start();            
    }

    public boolean isTimerRunning(){
        return timer.isRunning();
    }
    public void display() { // main display method 
        deckCountLabel.setText("");
        deckCountLabel.setText("Number of cards: " + cardDeck.getSize());
        playerPanel.attachCards();
        for(int i = 0; i < players.length; i++){
            if(players[i].isBot()){
                players[i].getPlayerPanel().updateBotInfo();
            }
        }
        this.revalidate();
        this.repaint();
    }
    public String[] displayInput(){ // appends inputWindow and waits for the input 
        display();
        leftCenter.removeAll();
        leftCenter.add(inputWindow);
        String [] arr = new String[2];
        while(arr[0] == null || arr[1] == null){
            arr[0] = playerNameInput; 
            arr[1] = cardNameInput;
        }
        playerNameInput = null;
        cardNameInput = null;
        return arr;
    }
    // Creates player-panels 
    public void createPanels() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].isBot()) {
                PlayerPanel botPanel = new PlayerPanel(players[i], this.players[i].isBot());
                players[i].setPanel(botPanel);
                this.topPanel.add(players[i].getPlayerPanel());
            } else {
                playerPanel = new PlayerPanel(players[i]);
                playerPanel.setPreferredSize(new Dimension(this.getWidth(), 280));
                players[i].setPanel(playerPanel);
            }
        }
    }


    public void updateCoordinates(){
        Point cardLocation = animatedPlayer.getLocation();
        SwingUtilities.convertPointFromScreen(cardLocation, this);
        destinationX = cardLocation.getX() - this.getLocationOnScreen().x;
        destinationY = cardLocation.getY() - this.getLocationOnScreen().y;
    }

}
