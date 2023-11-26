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
import GUI.components.LuckyDip;
import GUI.components.BotDialogWindow;
import GUI.components.EndGameWindow;
import GUI.components.MyIcon;
import GUI.components.PlayerDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    private int CARD_DECK_X = Const.CARD_DECK_X;
    private int CARD_DECK_Y = Const.CARD_DECK_Y;
    private final int PANEL_WIDTH = 850;
    private final int PANEL_HEIGHT = 650;
    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JPanel leftCenter;
    private JPanel rightCenter;
    private Player[] players;
    private CardDeck cardDeck;
    private DeckPanel deckPanel;
    private PlayerPanel playerPanel;
    // ANIMATION VARIABLES
    private Timer timer;
    private final int TIMER_DELAY = 2;
    private int xVelocity = 1;
    private int yVelocity = 1;
    private int x = Const.CARD_DECK_X;
    private int y = Const.CARD_DECK_Y;
    private double destinationX = 0;
    private double destinationY = 0;
    // POP-UP WINDOWS
    private InputWindow inputWindow = new InputWindow();
    private BotDialogWindow botDialogWindow = new BotDialogWindow();
    private PlayerDialog playerDialogWindow = new PlayerDialog();
    private LuckyDip luckyDipWindow = new LuckyDip();
    private EndGameWindow endGameWindow = new EndGameWindow();

    private boolean isReplyYes;
    private boolean isReplyClicked = false;
    private boolean isInputEntered = false;
    private boolean playAgain;

    private String playerNameInput;
    private String cardNameInput;
    private MyIcon cardBackImg;
    private MyIcon deckIcon;

    // LOCKS
    private static final Object lock = new Object();
    private static final Object inputLock = new Object();
    private static final Object endLock = new Object();

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
        leftCenter.setBackground(Const.yellowish);
        leftCenter.setLayout(new GridBagLayout());

        // CARD DECK CONTAINER
        rightCenter = new JPanel();
        rightCenter.setLayout(new GridBagLayout());
        rightCenter.setPreferredSize(new Dimension(150, 250));
        rightCenter.setBackground(Const.yellowish);

        deckPanel = new DeckPanel();
        deckPanel.updateDeckNum(cardDeck.getSize());
        URL imageURL = this.getClass().getResource("/res/images/Back-card.png");
        cardBackImg = new MyIcon(imageURL);
        rightCenter.add(deckPanel);
        //

        centerPanel.add(leftCenter);
        centerPanel.add(rightCenter);
        // BUTTON LISTENERS
        inputWindow.submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerNameInput = inputWindow.playerName.getText();
                cardNameInput = inputWindow.cardName.getText();
                synchronized (inputLock) {
                    isInputEntered = true;
                    inputLock.notify();
                }
            }
        });
        playerDialogWindow.getYesBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isReplyYes = true;
                synchronized (lock) {
                    isReplyClicked = true;
                    lock.notify();
                }
            }
        });
        playerDialogWindow.getNoBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isReplyYes = false;
                synchronized (lock) {
                    isReplyClicked = true;
                    lock.notify();
                }
            }
        });
        endGameWindow.getYesBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playAgain = true;
                synchronized (endLock) {
                    endLock.notify();
                }
            }
        });
        endGameWindow.getNoBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playAgain = false;
                synchronized (endLock) {
                    endLock.notify();
                }
            }
        });

        createPanels();
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(playerPanel, BorderLayout.SOUTH);
    }

    public void resize(int newWidth, int newHeight) {

        topPanel.setPreferredSize(new Dimension(newWidth, (int) (newHeight * 0.20)));
        centerPanel.setPreferredSize(new Dimension(newWidth, (int) (newHeight * 0.40)));
        leftCenter.setPreferredSize(new Dimension((int) (newWidth * 0.80), (int) (newHeight * 0.40)));
        rightCenter.setPreferredSize(new Dimension((int) (newWidth * 0.20), (int) (newHeight * 0.40)));

        Point location = deckPanel.getPosition(); 
        location = SwingUtilities.convertPoint(deckPanel, location, rightCenter);
        location = SwingUtilities.convertPoint(rightCenter, location, centerPanel);
        location = SwingUtilities.convertPoint(centerPanel, location, this);
        // System.out.println(location);
        CARD_DECK_X = (int)(location.getX() + 1);
        CARD_DECK_Y = (int)(location.getY() - 15);

        playerPanel.setPreferredSize(new Dimension(newWidth, (int) (newHeight * 0.40)));
        playerPanel.resize(newWidth, (int) (newHeight * 0.40));

        this.revalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        if(timer.isRunning()){
            g2D.drawImage(cardBackImg.getImage(), x, y, null);
            // g2D.drawImage(cardBackImg.getImage(), CARD_DECK_X, CARD_DECK_Y, null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double deltaX = Math.round(x - destinationX);
        double deltaY = Math.round(y - destinationY);
        if (deltaX > 0) {
            x -= xVelocity;
        } else if (deltaX < 0) {
            x += xVelocity;
        } else if (deltaX == 0) {
            x = (int) destinationX;
        }
        if (deltaX == 0 && deltaY > 0) {
            y -= yVelocity;
        } else if (deltaX == 0 && deltaY < 0) {
            y += yVelocity;
        } else if (deltaX == 0 && deltaY == 0) {
            y = (int) destinationY;
            x = Const.CARD_DECK_X;
            y = Const.CARD_DECK_Y;
            timer.stop();
        }
        this.repaint();
    }

    public void dealCard(Player originPlayer, Player destPlayer) {
        Point origin = originPlayer.getLocation();
        Point dest = destPlayer.getLocation();

        if (originPlayer.isBot() && destPlayer.isBot()) {
            origin = SwingUtilities.convertPoint(originPlayer.getPlayerPanel(), origin, topPanel);
            origin = SwingUtilities.convertPoint(topPanel, origin, this);
            dest = SwingUtilities.convertPoint(destPlayer.getPlayerPanel(), dest, topPanel);
            dest = SwingUtilities.convertPoint(topPanel, dest, this);
        } else {
            if (originPlayer.isBot()) {
                // main player is the destPLayer
                origin = SwingUtilities.convertPoint(originPlayer.getPlayerPanel(), origin, topPanel);
                origin = SwingUtilities.convertPoint(topPanel, origin, this);
                dest = SwingUtilities.convertPoint(destPlayer.getPlayerPanel(), dest, playerPanel);
                dest = SwingUtilities.convertPoint(playerPanel, dest, this);
            } else {
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

    public void dealCard(Player player) {
        x = CARD_DECK_X;
        y = CARD_DECK_Y;
        Point location = player.getLocation();
        location = SwingUtilities.convertPoint(player.getPlayerPanel(), location, topPanel);
        location = SwingUtilities.convertPoint(topPanel, location, this);
        destinationX = location.getX();
        destinationY = location.getY();
        timer.start();
    }

    public boolean isTimerRunning() {
        return timer.isRunning();
    }

    // DISPLAY FUNCTIONS
    public void display() { // main display method
        leftCenter.removeAll();
        deckPanel.updateDeckNum(cardDeck.getSize());
        playerPanel.attachCards();
        for (int i = 0; i < players.length; i++) {
            if (players[i].isBot()) {
                players[i].getPlayerPanel().updateBotInfo();
            }
        }
        this.revalidate();
        this.repaint();
    }

    public String[] displayInput() {
        // appends inputWindow and waits for the input
        display();
        leftCenter.add(inputWindow);
        String[] arr = new String[2];
        synchronized (inputLock) { // locks the thread to get the input
            try {
                inputLock.wait();
            } catch (Exception e) {
            }
        }
        arr[0] = playerNameInput;
        arr[1] = cardNameInput;
        return arr;
    }

    public void displayError(String errMsg) {
        display();
        JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayHappyFamily(Color color) {
        leftCenter.removeAll();
        luckyDipWindow.setLabelColor(color);
        luckyDipWindow.setTextLabel("Happy Family!!!");
        leftCenter.add(luckyDipWindow);
        this.repaint();
        sleep(3500);

    }

    public void displayBotInput(Player mainPlayer, Player askedPlayer, String card) {
        leftCenter.removeAll();
        boolean isBot = askedPlayer.isBot();
        boolean hasCard = askedPlayer.hasCard(card);
        String mainName = mainPlayer.getName().toUpperCase();
        String askedName = askedPlayer.getName().toUpperCase();
        String question;
        Color mainColor = mainPlayer.getColor();
        botDialogWindow.clear();
        playerDialogWindow.clear();
        if (isBot) {
            // bot is asking another bot
            leftCenter.add(botDialogWindow);
            question = mainName + ": Hey " + askedPlayer.getName() + ", do you have " + card + " ???";
            botDialogWindow.setQuestion(question, mainColor);
            botDialogWindow.setAnswer("", null);
            this.repaint();
            sleep(4000);
            String answer = "";
            if (hasCard) {
                answer = askedName + ": Yeah, I have it";
            } else {
                answer = askedName + ": No, I do not have it...";
            }
            botDialogWindow.setAnswer(answer, askedPlayer.getColor());
            sleep(4000);
            this.repaint();
        } else {
            // bot is asking user
            question = mainName + ": Hey, Do you have " + card + " ???";
            playerDialogWindow.setTitleText(question, mainColor);
            playerDialogWindow.showBtns();
            leftCenter.add(playerDialogWindow);

            synchronized (lock) { // locks the thread to get the input
                try {
                    lock.wait();
                } catch (Exception e) {
                }
            }

            String msg = mainName;
            if ((isReplyYes && hasCard) || (!isReplyYes && !hasCard)) {
                msg += ": Thanks for your honesty!";
                playerDialogWindow.setTitleText(msg, mainColor);
            } else if (!isReplyYes && hasCard) {
                msg += ": Don't lie to me!!!";
                playerDialogWindow.setTitleText(msg, mainColor);
            } else if (isReplyClicked && !hasCard) {
                msg += ": You don't have that card!!!";
                playerDialogWindow.setTitleText(msg, mainColor);
            }
            playerDialogWindow.hideBtns();
            this.repaint();
            sleep(2500);
            isReplyClicked = false;
        }
        leftCenter.removeAll();
        this.revalidate();
        this.repaint();
    }

    public void displayLuckyDip(Color color) {
        leftCenter.removeAll();
        luckyDipWindow.setLabelColor(color);
        luckyDipWindow.setTextLabel("Lucky Dip!");
        leftCenter.add(luckyDipWindow);
        this.repaint();
        sleep(3500);
    }

    public boolean displayEndGame(Player winner) {
        leftCenter.removeAll();
        String name = winner.getName();
        endGameWindow.setLabelText(name + " WON!");
        leftCenter.add(endGameWindow);
        this.repaint();
        synchronized (endLock) {
            try {
                endLock.wait();
            } catch (Exception e) {
            }
        }
        return playAgain;
    }

    public boolean displayEndGame(String draw) {
        leftCenter.removeAll();
        endGameWindow.setLabelText(draw);
        leftCenter.add(endGameWindow);
        this.repaint();
        synchronized (endLock) {
            try {
                endLock.wait();
            } catch (Exception e) {
            }
        }
        return playAgain;
    }

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

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println("GamePanel, Sleep Function Error");
        }
    }
}
