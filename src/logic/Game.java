package logic;
import java.awt.Color;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.regex.*;

import javax.swing.SwingUtilities;

import GUI.GamePanel;
import GUI.MyFrame;

import java.util.Random;
import assets.Const;
public class Game {
    private CardDeck cardDeck; 
    // private Scanner scanner;
    private boolean isGameOver = false; 
    private Player[] players;
    private String[] faces;
    private String[] families;
    private int CurrentPlayerId = 0; // tracks the player whose turn it is
    private Random random = new Random();
    String reset = "\u001B[0m";
    String red = "\u001B[31m";   
    private MyFrame frame;
    private GamePanel gamePanel;
    private Player askedPlayer;
    public Game(){
        players = new Player[4];
        cardDeck = new CardDeck();
        initializeGame();

        frame = new MyFrame(players, cardDeck);
        gamePanel = frame.getPanel();
        faces = cardDeck.getFaces(); 
        families = cardDeck.getFamilies(); 
        
        
    }
    public void start(){
        cardDeck.shuffleDeck();
        dealCards();
        gamePanel.display();
        String playerToAsk; // id of the player who will be asked
        String chosenCard;
        Player currentPlayer;
        Card askedCard;
        while(!isGameOver){
            if(CurrentPlayerId == 0){
                currentPlayer = players[CurrentPlayerId]; 

                // Choose player and card 
                String[] res = gamePanel.displayInput();
                playerToAsk = res[0];
                chosenCard = res[1]; 
                // validate the input 
                boolean canProceed = isInputValid(currentPlayer, playerToAsk, chosenCard);

                if(canProceed == false){
                    // display the error message 
                    continue;
                }
                // check if player being asked has the card 
                if(askedPlayer.hasCard(chosenCard)){
                    // if they do, take their card with the animation 
                    askedCard = askedPlayer.getCard(chosenCard);
                    currentPlayer.setCard(askedCard);
                }else{

                    // if they dont,take the card from the deck 
                }
                CurrentPlayerId++;



               

                // CurrentPlayerId++;
            }else{
               
                // sleep(2000);
            }
        }   
    }
    public boolean isInputValid(Player currentPlayer, String playerName, String cardName){
        // check the spelling of the card 
        String finalCardName = cardName.toLowerCase().replaceAll("[,\\s]", ""); 
        if(!cardDeck.isValidCard(finalCardName)){
            System.out.println("Card does not exist");
            return false;
        }
        // check if player is allowed to request that card 
        if(!currentPlayer.hasFamily(cardName)){
            System.out.println("Does not have that family");
            return false;
        }

        // check if player with the provided name exists 
        String finalPlayerName = playerName.toLowerCase().replaceAll("[,\\s]", "");
        for(int i = 0; i < players.length; i++){
            String name = players[i].getName().toLowerCase().replaceAll("[,\\s]", "");
            if(name.equals(finalPlayerName)){
                askedPlayer = players[i];
                return true;
            }
        }
        System.out.println("player does not exist");
        askedPlayer = null;
        return false;
    }
    public Player determineWinner(){
        int maxFamCount = 0;
        Player winner = players[0];
        for(int i = 0; i < players.length; i++){
            if(players[i].getCollectedFamilies() > maxFamCount){
                maxFamCount = players[i].getCollectedFamilies(); 
                winner = players[i]; 
            }
            // CONSIDER THE DRAW SCENARIO LATER ON
        }
        return winner;
    }
    public boolean gameOver(){
        // game ends if someone has no cards left OR the stock runs out 
        isGameOver = true; 
        if(cardDeck.getSize() == 0){
            isGameOver = true; 
            return isGameOver;
        }
        for(int i = 0; i < players.length; i++){
            if(players[i].getCardsNum() == 0){
                return isGameOver;
            }
        }
        return false; 
    }

    public void initializeGame(){ 
        for(int i = 0; i < 4; i++){
            players[i] = new Player(i != 0, i, Const.BOT_NAMES[i]);
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            players[i].setColor(new Color(r,g,b));
        }
    }
    
    
    public void dealCards(){ 
        // deal 6 cards to each player 
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < players.length; j++){
                gamePanel.dealCard(players[j]);
                while (gamePanel.isTimerRunning()) {
                    // waits until animation is finished 
                    // System.out.println("waiting..." + gamePanel.isTimerRunning());
                }
                Card card = cardDeck.popTheCard();
                players[j].setCard(card);
                gamePanel.revalidate();
                gamePanel.repaint();
            }
        }
    }
}
