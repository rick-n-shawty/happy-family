import java.util.ArrayList;
public class Player {
    String reset = "\u001B[0m";
    String green = "\u001B[32m"; 
    private boolean isBot; 
    private int id;
    private ArrayList<Card> hand;
    public Player(boolean isBot, int playerId){
        this.isBot = isBot;
        this.id = playerId;
        hand = new ArrayList<>();
    }

    public String toString(){
        return "IsBot: " + this.isBot + " Id: " + this.id;
    }
    public void setCard(Card card){
        hand.add(card);
    }
    public void showHand(){
        String row = "";
        int count = 0;
        for(int i = 0; i < hand.size(); i++){
            row += green + "| " + hand.get(i).toString() + " |" + reset;
            if(count >=4){
                System.out.println(row);
                count = 0; 
                row = ""; 
            }
            count +=1; 
        }
        System.out.println(row);
    }

    public int getId(){
        return this.id; 
    }
    public int getCardsNum(){
        return this.hand.size();
    }
    public boolean hasFamily(String familyName){
        // checks if player has the card of a specifc family in their hand 
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i).getFamily().equals(familyName)){
                return true;
            }
        }
        return false;
    }
}
