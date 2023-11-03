import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.*;

import assets.Const;
public class Game {
    private CardDeck cardDeck; 
    private Scanner scanner;
    private boolean isGameOver = false; 
    private Player[] players;
    private String[] faces;
    private String[] families;
    private int CurrentPLayerId; // tracks the player whose turn it is
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
    }
    public void start(){
        cardDeck.shuffleDeck();
        dealCards();
        display();
        int playerToAsk; // id of the player who will be asked
        String desiredCard;
        while(!isGameOver){
            if(CurrentPLayerId == 0){
                System.out.println("What player do you want to ask?");
                System.out.print("");
                playerToAsk = scanner.nextInt();
                scanner.nextLine();
                // Handle the case when player enters invalid id 
                if(playerToAsk > players.length || playerToAsk < 0){
                    System.out.println("PLAYER WITH ID " + playerToAsk + " DNE");
                    continue;
                }else if(playerToAsk == 0){
                    System.out.println("CANNOT ASK YOURSELF, CHOOSE A DIFFERENT PLAYER");
                    continue; 
                }
                System.out.println("What card do you want to ask for?");
                // Player enters the desired card 
                desiredCard = scanner.nextLine();
                if(!isAllowedToAsk(desiredCard, players[0])){ // if player is NOT allowed to ask, loop again or choose another card???
                    continue; 
                }
            }else{

            }
        }   
        scanner.close();         
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
                System.out.println("You need to have at least one card from the family");
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

        System.out.println(red + "Choosing cards: " + reset);
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
        players[0].showHand();
    }
    public void showPlayers(){
        for(int i = 0; i < players.length; i++){
            System.out.println(players[i].toString());
        }
    }
}
