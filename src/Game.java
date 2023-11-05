import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.*;
import java.util.Random;
import assets.Const;
public class Game {
    private CardDeck cardDeck; 
    private Scanner scanner;
    private boolean isGameOver = false; 
    private Player[] players;
    private String[] faces;
    private String[] families;
    private int CurrentPLayerId; // tracks the player whose turn it is
    private Random random;
    String reset = "\u001B[0m";
    String red = "\u001B[31m";   
    String green = "\u001B[32m"; 
    String yellow = "\u001B[33m"; 

    public Game(){
        cardDeck = new CardDeck();
        scanner = new Scanner(System.in); 
        initializeGame();
        CurrentPLayerId = 0; // initially is set to 0 coz main player has the id of 0 
        faces = cardDeck.getFaces(); 
        families = cardDeck.getFamilies(); 
        random = new Random();
    }
    public void start(){
        cardDeck.shuffleDeck();
        dealCards();
        display();
        int playerToAsk; // id of the player who will be asked
        String chosenCard;
        Player askedPlayer;
        Player currentPlayer;
        Card askedCard;
        while(!isGameOver){
            if(gameOver()){ // checks the state of the game
                System.out.println("GAME OVER");
                break;
            }

            if(CurrentPLayerId == 0){
                System.out.println(green + "PLAYER 0'S TURN" + reset);
                currentPlayer = players[0];

                // Choose the player to ask 
                System.out.println("What player do you want to ask?");
                System.out.print("");
                try{
                    playerToAsk = scanner.nextInt();
                    if(playerToAsk >= players.length){
                        System.out.println("provide valid index");
                        scanner.nextLine();
                        continue; 
                    }else if(playerToAsk <=0 ){
                        System.out.println("Cannot ask yourself!");
                        scanner.nextLine();
                        continue;
                    }
                    scanner.nextLine();
                }catch(InputMismatchException e){
                    System.out.println("please provide valid input!");
                    scanner.nextLine(); // consumes the invalid input 
                    continue;
                }

                askedPlayer = players[playerToAsk];
                System.out.println("What card do you want to ask for?");
                // Player enters the desired card 
                chosenCard = scanner.nextLine();
                if(!isAllowedToAsk(chosenCard, players[0])){ // if player is NOT allowed to ask, loop again or choose another card???
                    continue; 
                }
                // Take the card from one player and give to the other 

                // 1 - Check if the player has the card  
                if(askedPlayer.hasCard(chosenCard)){
                    // if player has the card they must give it away 
                    // and current player gets another turn
                    askedCard = askedPlayer.getCard(chosenCard); 
                    currentPlayer.setCard(askedCard);
                    display();
                    continue;
                }else{
                    // if player does not have card, we take the card from the deck 
                    System.out.println("Not at home, pick a card!");
                    askedCard = cardDeck.popTheCard(); 
                    if(askedCard.toString().equals(chosenCard)){
                        // if players gets the card they wanted thet are to keep their turn 
                        currentPlayer.setCard(askedCard);
                        continue;
                    }else{
                        // otherwise turn goes to another player
                        currentPlayer.setCard(askedCard);
                    }
                }


                // increment CurrentPlayerId
                CurrentPLayerId++;
                display();
            }else{
                System.out.println(red + "PLAYER " + CurrentPLayerId + "'s TURN" + reset);
                currentPlayer = players[CurrentPLayerId]; 
                // choose the player to ask 
                do{
                    playerToAsk = random.nextInt(0, players.length); 
                }while(playerToAsk == CurrentPLayerId);
                askedPlayer = players[playerToAsk];

                // choose a card 
                chosenCard = currentPlayer.chooseCard();
                System.out.println("Bot chose: " + chosenCard);
                System.out.println("Bot's cards: ");
                currentPlayer.showHand(red);
                // ask if player has the card 
                if(askedPlayer.hasCard(chosenCard) && askedPlayer.isBot()){
                    // if player is bot we juse swap the cards 
                    // currentPlayer gets another turn 
                    askedCard = askedPlayer.getCard(chosenCard); 
                    currentPlayer.setCard(askedCard);
                    display();
                    continue;
                }else if(askedPlayer.hasCard(chosenCard) && !askedPlayer.isBot()){
                    // if player is not bot 
                    // currentPlayer gets another turn 
                    System.out.println("Hey Player 0! Can I have " + chosenCard + ", please?");
                    askedCard = askedPlayer.getCard(chosenCard); 
                    currentPlayer.setCard(askedCard);
                    display();
                    continue; 
                }else{
                    // if player does not have a card, take from the deck 
                    askedCard = cardDeck.popTheCard(); 
                    if(askedCard.toString().equals(chosenCard)){
                        // lucky dip, currentPlayer gets another turn 
                        currentPlayer.setCard(askedCard);
                        display();
                        continue;
                    } 
                    currentPlayer.setCard(askedCard);
                }
                if(CurrentPLayerId + 1 == players.length){
                    CurrentPLayerId = 0;
                }else{
                    CurrentPLayerId++; 
                }
                sleep(2000);
                display();
            }
        }   
        scanner.close();         
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
    public boolean isAllowedToAsk(String card, Player player){
        // check the spelling of the card 
        Pattern pattern = Pattern.compile("^(.*?) (.*)$");
        Matcher matcher = pattern.matcher(card);
        if(matcher.matches()){
            String face = matcher.group(1); 
            String family = matcher.group(2);
            // check if player has the card of the same family 
            if(!player.hasFamily(family)){
                System.out.println(red + "You need to have at least one card from the family" + reset);
                return false; 
            }else if(player.hasCard(face + " " + family)){
                System.out.println(red + "You already have that card" + reset);
                return false; 
            }
            return true;
        }else{
            System.out.println("Input mistmatch");
            return false;
        }
    }
    public void initializeGame(){ 
        // Initialize the number of players and players array 
        System.out.println("How many players?");
        int num = scanner.nextInt(); 
        while (num > Const.MAX_NUM_OF_PLAYERS || num < Const.MIN_NUM_OF_PLAYERS) {
            if(num > Const.MAX_NUM_OF_PLAYERS){
                System.out.println("Maximum number of players is: " + Const.MAX_NUM_OF_PLAYERS);
                num = scanner.nextInt();
            }else if(num < Const.MIN_NUM_OF_PLAYERS){
                System.out.println("Minimum number of players is: " + Const.MIN_NUM_OF_PLAYERS);
                num = scanner.nextInt();
            }
        }
        players = new Player[num];
        for(int i = 0; i < num; i++){
            players[i] = new Player(i > 0, i);
        }
    }
    
    
    public void dealCards(){ 

        // Function called in the beginning of the game to deal cards based on the number of players
        if(players.length == 2){
            // give 8 cards to each player if there are 2 players
            for(int i = 0; i < 8; i++){
                players[i % 2].setCard(cardDeck.popTheCard());
                players[i % 2].setCard(cardDeck.popTheCard());
            }
        }else{
            // give six cards to each players if there are more than 2 players
            for(int i = 0; i < players.length; i++){
                for(int j = 0; j < 6; j++){
                    players[i].setCard(cardDeck.popTheCard());
                }
            }
        }
    }
    public void display(){
        System.out.println("*************************************************");
        System.out.println("----------------------------------------------");
        for(int i = 0; i < cardDeck.getFamilies().length; i++){
            if(i < cardDeck.getFaces().length){
                System.out.println(i + " " + cardDeck.getFamilies()[i] + "\t\t\b\b\b\b|| " + i + " " + cardDeck.getFaces()[i]);
            }else{
                System.out.println(i + " " + cardDeck.getFamilies()[i] + "\t\t\b\b\b\b||");
            }
        }
        System.out.println("----------------------------------------------");
        
        String row1 = red + "|" + reset; 
        String row2 = yellow + "CARDS REAMAINING: " + cardDeck.getSize() + reset;
        for(int k = 0; k < players.length; k++){
            if(players[k].getId() != 0){
                row1 += red + "Player " + players[k].getId() + " Cards: " + players[k].getCardsNum()  + " | " + reset;
            }
        }
        System.out.println("");
        System.out.println(row1);
        System.out.println("");
        System.out.println("");
        System.out.println(row2);
        System.out.println("");
        System.out.println("");
        players[0].showHand(green);

        System.out.println("*************************************************");
    }
    public void sleep(int time){
        try{
            Thread.sleep(time);
        }catch(Exception e){}
    }
    public void showPlayers(){
        for(int i = 0; i < players.length; i++){
            System.out.println(players[i].toString());
        }
    }
}
