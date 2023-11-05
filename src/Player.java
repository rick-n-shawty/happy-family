import java.util.ArrayList;
import java.util.HashMap;
import assets.Const;
public class Player {
    String reset = "\u001B[0m";
    String green = "\u001B[32m"; 
    String pink = "\u001B[35m";
    private boolean isBot; 
    private int id;
    private HashMap<String, Integer>familyStackCount;
    private HashMap<String, Card> hand;
    public Player(boolean isBot, int playerId){
        this.isBot = isBot;
        this.id = playerId;
        familyStackCount = new HashMap<>();
        hand = new HashMap<>(); 
    }
    // GETTERS 
    public String chooseCard(){
        // find the family that is almost completed
        int count = 0;  
        String family = ""; 
        for(String key : this.familyStackCount.keySet()){
            if(this.familyStackCount.get(key) >= count && this.familyStackCount.get(key) < 4){
                count = this.familyStackCount.get(key);
                family = key; 
            }
        }
        // find the appropriate face for the card 
        for(int i = 0; i < Const.faces.length; i++){
            String cardName = Const.faces[i] + " " + family;
            if(!this.hand.containsKey(cardName)){
                return cardName; 
            }
        }
        return "";

    }
    public String toString(){
        return "IsBot: " + this.isBot + " Id: " + this.id;
    }
    public int getCollectedFamilies(){
        int count = 0; 
        for(int val : this.familyStackCount.values()){
            if(val == Const.faces.length){
                count++; 
            }
        }
        return count;
    }
    public int getCardsNum(){
        return this.hand.size();
    }
    public int getId(){
        return this.id; 
    }
    public boolean hasFamily(String familyName){
        // checks if player has the card of a specifc family in their hand 
        for(String key : this.hand.keySet()){
            if(this.hand.get(key).getFamily().equals(familyName)){
                return true;
            }
        }
        return false;
    }
    public boolean hasCard(String cardName){
        return this.hand.containsKey(cardName);
    }
    public Card getCard(String cardName){
        for(String key : hand.keySet()){
            if(key.equals(cardName)){
                Card temp = this.hand.get(key); 
                this.hand.remove(key); 
                int count = this.familyStackCount.get(temp.getFamily());
                count--;
                this.familyStackCount.put(temp.getFamily(), count);
                return temp;
            }
        }
        // we won't run into the error since we check if card is present before calling getCard()
        return new Card("error", "error");
    }
    public boolean isBot(){
        return this.isBot;
    }
    // SETTERS 
    public void setCard(Card card){
        this.hand.put(card.toString(), card); 
        if(this.familyStackCount.containsKey(card.getFamily())){
            int count = this.familyStackCount.get(card.getFamily()); 
            count++; 
            this.familyStackCount.put(card.getFamily(), count);
        }else{
            this.familyStackCount.put(card.getFamily(), 1);
        }
        if(this.familyStackCount.get(card.getFamily()) == Const.faces.length){
            System.out.println(pink + "HAPPY FAMILY" + reset);
        }
    }
    // VOIDS
    public void showHand(String color){
        String row = "";
        System.out.println("Player " + this.id);
        for(String key : this.hand.keySet()){
            row = color + "| " + this.hand.get(key).toString() + " |" + reset; 
            System.out.println(row);
            row = "";
        }
    }
}
