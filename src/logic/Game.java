package logic;
import GUI.GamePanel;
import GUI.MyFrame;
import java.security.SecureRandom;
import assets.Const;

public class Game {
    private CardDeck cardDeck; 
    private boolean isGameOver = false; 
    private Player[] players;
    private int CurrentPlayerId = 0; // tracks the player whose turn it is
    private SecureRandom randomFn = new SecureRandom();
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
        String playerToAsk;
        String chosenCard;
        Player currentPlayer;
        Card askedCard;
        Card receivedCard; 
        Player winner; 
        boolean tie;
        boolean playAgain; 

        while(!isGameOver){
            gamePanel.display();
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
                    // take the card from the player and animate it
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
                    chosenCard = Const.convertToLower(chosenCard);

                    if(drawnCard.comparison(chosenCard) && currentPlayer.getFamilyFound()){
                        // display happy family 
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }else if(drawnCard.comparison(chosenCard) && !currentPlayer.getFamilyFound()){
                        // lucky dip 
                        gamePanel.displayLuckyDip(currentPlayer.getColor());
                    }else if(!drawnCard.comparison(chosenCard) && currentPlayer.getFamilyFound()){
                        // did not get the desired card but collected another family 
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }else if(!drawnCard.comparison(chosenCard) && !currentPlayer.getFamilyFound()){
                        // did not get desired card and did not colllect a family 
                        CurrentPlayerId++; 
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
                // chosenCard = cardDeck.getNextCard().toString(); 
                gamePanel.display();
                gamePanel.displayBotInput(currentPlayer, askedPlayer, chosenCard);

                if(askedPlayer.hasCard(chosenCard)){
                    gamePanel.dealCard(askedPlayer, currentPlayer);
                    while (gamePanel.isTimerRunning()) {
                        // wait
                    }
                    receivedCard = askedPlayer.getCard(chosenCard);
                    currentPlayer.setCard(receivedCard);
                    if(currentPlayer.getFamilyFound()){
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }
                }else{
                    gamePanel.dealCard(currentPlayer);
                    while (gamePanel.isTimerRunning()) {
                        // wait
                    }
                    receivedCard = cardDeck.popTheCard();
                    currentPlayer.setCard(receivedCard);


                    if(!receivedCard.comparison(chosenCard) && currentPlayer.getFamilyFound()){
                        // did not get desired card but collected a family 
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }else if(!receivedCard.comparison(chosenCard) && !currentPlayer.getFamilyFound()){
                        // did not get desired card and did not collect a family 
                        if(CurrentPlayerId == players.length - 1){
                            CurrentPlayerId = 0;
                        }else{
                            CurrentPlayerId++;
                        }
                    }else if(receivedCard.comparison(chosenCard) && currentPlayer.getFamilyFound()){
                        // got the right card and collected a family 
                        gamePanel.displayHappyFamily(currentPlayer.getColor());
                    }else if(receivedCard.comparison(chosenCard) && !currentPlayer.getFamilyFound()){
                        // got the right card but did not collect a family 
                        System.out.println("LUCKY DIP FROM IF 1");
                        gamePanel.displayLuckyDip(currentPlayer.getColor());
                    }
                }
                
            }
        }   
    }

    public boolean isInputValid(Player currentPlayer, String playerName, String cardName){
        // check the spelling of the card 
        String finalCardName = Const.convertToLower(cardName); 
        if(!cardDeck.isValidCard(finalCardName)){
            ErrorMessage = "Invalid card name"; 
            return false;
        }
        // check if player is allowed to request that card 
        if(!currentPlayer.hasFamily(finalCardName)){
            ErrorMessage = "You don't have a card from that family to ask for " + cardName; 
            return false;
        }else if(currentPlayer.hasCard(finalCardName)){
            ErrorMessage = "You already have that card!";
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
        int maxCount = 0; 
        int index = 0; 
        for(int i = 0; i < players.length; i++){
            if(players[i].getCollectedFamilies() > maxCount){
                maxCount = players[i].getCollectedFamilies(); 
                index = i; 
            }
        }
        // check if two players collected the same number of cards
        for(int j = 0; j < players.length; j++){
            if(j == index) continue; 
            else if(players[j].getCollectedFamilies() == maxCount){
                return true;
            }
        }
        return false;
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
    }
}
