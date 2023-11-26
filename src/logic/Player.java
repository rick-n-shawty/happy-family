package logic;
import java.awt.Color;
import java.util.HashMap;
import GUI.PlayerPanel;
import assets.Const;
import java.awt.Point;
public class Player {
    private boolean isBot; 
    private Color color;
    private int id;
    private PlayerPanel panel;
    private HashMap<String, Integer>familyStackCount;
    private HashMap<String, Card> hand;
    private String name;
    private boolean isFamilyFound = false;
    public Player(boolean isBot, int playerId, String playerName){
        this.isBot = isBot;
        this.id = playerId;
        familyStackCount = new HashMap<>();
        hand = new HashMap<>(); 
        this.name = playerName;
    }
    // GETTERS 
    public String[] getHand(){
        String [] cardsArray = new String[hand.size()];
        int i = 0;
        for(String key : this.hand.keySet()){
            cardsArray[i] = key;
            i++;
        }
        return cardsArray;
    }
    public String getChosenCard(){
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
            String finalName = cardName.toLowerCase().replaceAll(Const.myRegex, "");
            if(!this.hand.containsKey(finalName)){
                return cardName; 
            }
        }

        // if smth is off, pick a random card 
        return "";
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
    public String getName(){
        return this.name;
    }
    public String[] getCollectedFamiliesNames(){
        String[] arr = new String[familyStackCount.size()];
        int i = 0; 
        for(String key : familyStackCount.keySet()){
            if(familyStackCount.get(key) == 4){
                arr[i] = key; 
                i++;
            }
        }
        return arr; 
    }
    public int getCardsNum(){
        return this.hand.size();
    }
    public int getId(){
        return this.id; 
    }
    public boolean hasFamily(String cardName){
        // checks if player has the card of a specifc family in their hand 
        for(String key : this.hand.keySet()){
            String subStr = Const.convertToLower(this.hand.get(key).getFamily());
            if(cardName.contains(subStr)){
                return true;
            }
        }
        return false;
    }
    public boolean hasCard(String cardName){
        String finalName = cardName.toLowerCase().replaceAll(Const.myRegex, ""); 
        return this.hand.containsKey(finalName);
    }
    public Card getCard(String cardName){
        String finalName = cardName.toLowerCase().replaceAll(Const.myRegex, "");
        for(String key : hand.keySet()){
            if(key.equals(finalName)){
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
    public boolean getFamilyFound(){
        return this.isFamilyFound; 
    }
    public Point getLocation(){
        return this.panel.getCardPositon();
    }
    public PlayerPanel getPlayerPanel(){
        return this.panel;
    }
    public Color getColor(){
        return this.color;
    }

    // SETTERS 
    public void setCard(Card card){
        isFamilyFound = false; 
        this.hand.put(card.toString(), card); 
        if(this.familyStackCount.containsKey(card.getFamily())){
            int count = this.familyStackCount.get(card.getFamily()); 
            count++; 
            this.familyStackCount.put(card.getFamily(), count);
        }else{
            this.familyStackCount.put(card.getFamily(), 1);
        }
        
        if(this.familyStackCount.get(card.getFamily()) == Const.faces.length){
            // if user collected a full family, remove that family cards from the hand 
            isFamilyFound = true;
            for(int i = 0; i < Const.faces.length; i++){
                String cardName = Const.faces[i] + " " + card.getFamily();
                cardName = cardName.toLowerCase().replaceAll(Const.myRegex, ""); 
                hand.remove(cardName);
            }
        }
    }
    public void setPanel(PlayerPanel botPanel){
        this.panel = botPanel; 
    }

    public void reset(){
        this.familyStackCount.clear(); 
        this.hand.clear();
    }
    public void setColor(Color color){
        this.color = color;
    }

}
