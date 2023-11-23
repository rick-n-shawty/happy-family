package GUI;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import logic.CardDeck;
import logic.Player;
import assets.Const;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import GUI.components.InputWindow;
import GUI.components.BotDialogWindow;
import GUI.components.MyIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    private final int PANEL_WIDTH = 850;
    private final int PANEL_HEIGHT = 650;
    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JPanel leftCenter;
    private Player[] players;
    private CardDeck cardDeck;
    private JLabel deckIconLabel = new JLabel();
    private JLabel deckCountLabel = new JLabel("Number of cards: ");
    private PlayerPanel playerPanel;
    // ANIMATION VARIABLES 
    private Timer timer;
    private final int TIMER_DELAY = 1;
    private int xVelocity = 1;
    private int yVelocity = 1;
    private int x = Const.CARD_DECK_X;
    private int y = Const.CARD_DECK_Y;
    private double destinationX = 0;
    private double destinationY = 0;
    // POP-UP WINDOWS 
    private InputWindow inputWindow = new InputWindow();
    private BotDialogWindow botDialogWindow = new BotDialogWindow();
    private String playerNameInput; 
    private String cardNameInput; 
    private MyIcon cardBackImg = new MyIcon("/Users/sultkh/Desktop/HappyFam/res/images/Back-card.png");
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

    public void dealCard(Player originPlayer, Player destPlayer) {
        Point origin = originPlayer.getLocation(); 
        Point dest = destPlayer.getLocation();

        if(originPlayer.isBot() && destPlayer.isBot()){
            origin = SwingUtilities.convertPoint(originPlayer.getPlayerPanel(), origin, topPanel);
            origin = SwingUtilities.convertPoint(topPanel, origin, this);
            dest = SwingUtilities.convertPoint(destPlayer.getPlayerPanel(), dest, topPanel);
            dest = SwingUtilities.convertPoint(topPanel, dest, this);
        }else{
            if(originPlayer.isBot()){
                // main player is the destPLayer
                origin = SwingUtilities.convertPoint(originPlayer.getPlayerPanel(), origin, topPanel);
                origin = SwingUtilities.convertPoint(topPanel, origin, this);
                dest = SwingUtilities.convertPoint(destPlayer.getPlayerPanel(), dest, playerPanel);
                dest = SwingUtilities.convertPoint(playerPanel, dest, this);
            }else{           
                // main player is the originPLayer     
                origin = SwingUtilities.convertPoint(originPlayer.getPlayerPanel(), origin, playerPanel);
                origin = SwingUtilities.convertPoint(playerPanel, origin, this);
                // System.out.println("HAHAHHAHA" + originPlayer.isBot());
                dest = SwingUtilities.convertPoint(destPlayer.getPlayerPanel(), dest, topPanel);
                dest = SwingUtilities.convertPoint(topPanel, dest, this);
            }
        }


        x = origin.x;
        y = origin.y; 
        destinationX = dest.x; 
        destinationY = dest.y;

        timer.start();            
    }
    public void dealCard(Player player){
        x = Const.CARD_DECK_X; 
        y = Const.CARD_DECK_Y; 
        Point location = player.getLocation();
        location = SwingUtilities.convertPoint(player.getPlayerPanel(), location, topPanel);        
        location = SwingUtilities.convertPoint(topPanel, location, this);
        destinationX = location.getX(); 
        destinationY = location.getY();
        timer.start();
    }

    public boolean isTimerRunning(){
        return timer.isRunning();
    }
    public void display() { // main display method 
        leftCenter.removeAll();
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
    public String[] displayInput(){
        // appends inputWindow and waits for the input 
        display();
        leftCenter.add(inputWindow);
        String [] arr = new String[2];
        playerNameInput = null;
        cardNameInput = null;
        while(arr[0] == null || arr[1] == null){
            arr[0] = playerNameInput; 
            arr[1] = cardNameInput;
        }
        playerNameInput = null;
        cardNameInput = null;
        return arr;
    }
    public void displayError(String errMsg){
        display();
        JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
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
    public void sleep(int ms){
        try{
            Thread.sleep(ms);
        }catch(InterruptedException e){}
    }
    public void displayBotInput(Player mainPlayer, Player askedPlayer, String card){
        leftCenter.removeAll();
        boolean isBot = askedPlayer.isBot(); 
        boolean hasCard = askedPlayer.hasCard(card); 
        String mainName = mainPlayer.getName().toUpperCase();
        String askedName = askedPlayer.getName().toUpperCase();
        String question;
        Color mainColor = mainPlayer.getColor(); 
        botDialogWindow.clear();
        leftCenter.add(botDialogWindow);
        botDialogWindow.setCustomLayout(isBot);
        if(isBot){
            question = mainName + ": Hey " + askedPlayer.getName() + ", do you have " + card + " ???";
            botDialogWindow.setQuestion(question, mainColor);
            botDialogWindow.setAnswer("", null);
            this.repaint();
            sleep(4000);
            String answer = "";
            if(hasCard){
                answer = askedName + ": Yeah, I have it";
            }else{
                answer = askedName + ": No, I do not have it...";
            }
            botDialogWindow.setAnswer(answer, askedPlayer.getColor());
            sleep(4000);
            this.repaint();
        }else{
            question = mainName + ": Hey, Do you have " + card + " ???";
            botDialogWindow.setQuestion(question, mainColor);

            this.repaint();
            sleep(10000);
        }
    }
}
