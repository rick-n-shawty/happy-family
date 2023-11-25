package logic;
import GUI.GamePanel;
import GUI.MyFrame;
import java.security.SecureRandom;

import javax.swing.JFrame;

import assets.Const;
public class Game {
    private CardDeck cardDeck; 
    private boolean isGameOver = false; 
    private Player[] players;
    private int CurrentPlayerId = 0; // tracks the player whose turn it is
    private SecureRandom randomFn = new SecureRandom();
    String reset = "\u001B[0m";
    String red = "\u001B[31m";   
    private MyFrame frame;
    private GamePanel gamePanel;
    private Player askedPlayer;
    private String ErrorMessage; 
    public Object animationLock = new Object();
    public Game(){
        players = new Player[4];
        cardDeck = new CardDeck();
        cardDeck.shuffleDeck();
        initializeGame();
        frame = new MyFrame(players, cardDeck);
        gamePanel = frame.getPanel();        
    }
    public void resetGame(boolean playAgain){
        // reset players 
        if(!playAgain){
            System.exit(0);
        }
        for(int i = 0; i < players.length; i++){
            players[i].reset();
        }
        isGameOver = false; 
        // reset deck 
        cardDeck.initializeDeck(); 
        cardDeck.shuffleDeck();
        gamePanel.display();
        dealCards(); 
        gamePanel.display();
    }
    public void start(){
        cardDeck.shuffleDeck();
        dealCards();
        gamePanel.display();
        String playerToAsk; // id of the player who will be asked
        String chosenCard;
        Player currentPlayer;
        Card askedCard;
        Card wantedCard; 
        Player winner; 
        boolean tie;
        boolean playAgain; 

        while(!isGameOver){
            isGameOver = gameOver();
            tie = isTie();
            if(isGameOver && !tie){
                // display winner 
                winner = determineWinner(); 
                playAgain = gamePanel.displayEndGame(winner);
                resetGame(playAgain);
                continue;
            }else if(isGameOver && tie){
                // display draw 
                playAgain = gamePanel.displayEndGame("It is a draw!"); 
                resetGame(playAgain);           
                continue;
            }

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
                    gamePanel.displayError(ErrorMessage.isBlank() ? "Error" : ErrorMessage);
                    continue;
                }
                // check if player being asked has the card 
                if(askedPlayer.hasCard(chosenCard)){
                    // take the card from the player 
                    // animate the card 
                    gamePanel.dealCard(askedPlayer, currentPlayer);
                    while(gamePanel.isTimerRunning()){
                        // wait for animation 
                    }
                    askedCard = askedPlayer.getCard(chosenCard);
                    currentPlayer.setCard(askedCard);
                    if(currentPlayer.getFamilyFound()){
                        // player collected full family, display happy family 
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }
                    gamePanel.display();
                    CurrentPlayerId = 0;  
                }else{
                    // draw the card from a deck
                    gamePanel.dealCard(currentPlayer);
                    while(gamePanel.isTimerRunning()){
                        // wait 
                    }
                    gamePanel.display();
                    Card drawnCard = cardDeck.popTheCard();
                    currentPlayer.setCard(drawnCard);
                    chosenCard = chosenCard.toLowerCase().replaceAll(Const.myRegex, "");
                    
                    if(drawnCard.toString().equals(chosenCard)){
                        // if player got a card they asked for, they get a second turn 
                        gamePanel.displayLuckyDip(currentPlayer.getColor());
                        CurrentPlayerId = 0;
                    }else{
                        CurrentPlayerId++;
                    }
                    if(currentPlayer.getFamilyFound()){
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }
                }

            }else{
                currentPlayer = players[CurrentPlayerId];
                // pick a player randomly 
                int randomIndex = randomFn.nextInt(0,players.length); 
                if(randomIndex == currentPlayer.getId()){
                    randomIndex = randomFn.nextInt(currentPlayer.getId()); 
                    askedPlayer = players[randomIndex];
                }

                askedPlayer = players[randomIndex]; 
                chosenCard = currentPlayer.getChosenCard(); 
                gamePanel.display();
                gamePanel.displayBotInput(currentPlayer, askedPlayer, chosenCard);
                if(askedPlayer.hasCard(chosenCard)){
                    gamePanel.dealCard(askedPlayer, currentPlayer);
                    while (gamePanel.isTimerRunning()) {
                        // wait
                    }
                    wantedCard = askedPlayer.getCard(chosenCard);
                    currentPlayer.setCard(wantedCard);
                }else{
                    gamePanel.dealCard(currentPlayer);
                    while (gamePanel.isTimerRunning()) {
                        // wait
                    }
                    wantedCard = cardDeck.popTheCard();
                    currentPlayer.setCard(wantedCard);
                    if(!wantedCard.comparison(chosenCard)){
                        if(CurrentPlayerId == players.length - 1){
                            CurrentPlayerId = 0;
                        }else{
                            CurrentPlayerId++;
                        }
                    }else{
                        // if the drawn card was the wanted card 
                        gamePanel.displayLuckyDip(currentPlayer.getColor());
                    }
                }

            }
        }   
    }
    public boolean isInputValid(Player currentPlayer, String playerName, String cardName){
        // check the spelling of the card 
        String finalCardName = Const.convertToLower(cardName); 
        System.out.println("input: " + finalCardName);
        if(!cardDeck.isValidCard(finalCardName)){
            ErrorMessage = "Invalid card name"; 
            return false;
        }
        // check if player is allowed to request that card 
        if(!currentPlayer.hasFamily(finalCardName)){
            ErrorMessage = "You don't have a card from that family to ask for " + cardName; 
            return false;
        }

        // check if player with the provided name exists 
        String finalPlayerName = Const.convertToLower(playerName);
        for(int i = 0; i < players.length; i++){
            String name = Const.convertToLower(players[i].getName());
            if(name.equals(finalPlayerName)){
                askedPlayer = players[i];
                return true;
            }
        }
        ErrorMessage = "Player '" + playerName + "' does not exist";
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
        }
        return winner;
    }
    public boolean gameOver(){
        // game ends if someone has no cards left OR the stock runs out 
        if(cardDeck.isEmpty()){
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
    public boolean isTie(){
        int count = players[0].getCollectedFamilies(); 
        for(int i = 1; i < players.length; i++){
            if(players[i].getCollectedFamilies() != count) return false; 
        }
        return true;

    }
    public void initializeGame(){ 
        for(int i = 0; i < 4; i++){
            players[i] = new Player(i != 0, i, Const.BOT_NAMES[i]);
            players[i].setColor(Const.playerColors[i]);
        }
    }
    
    
    public void dealCards(){ 
        // deal 6 cards to each player 
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < players.length; j++){
                gamePanel.dealCard(players[j]);
                    while (gamePanel.isTimerRunning()){
                        // waits until animation is finished 
                    }
                Card card = cardDeck.popTheCard();
                players[j].setCard(card);

                gamePanel.revalidate();
                gamePanel.repaint();
                gamePanel.display();
            }
        }
        players[0].makeWinner();
    }

    public MyFrame getFrame(){
        return this.frame;
    }
}
