package logic;
import assets.MyList;

import java.util.HashMap;
import java.security.SecureRandom;
import assets.Const;
public class CardDeck {

    private SecureRandom random = new SecureRandom();
    private HashMap<String, Integer> allCards = new HashMap<>(); // helps to check if the requested card exists 
    private final String[] faces = {
        "Mr", 
        "Mrs", 
        "Master",
        "Miss"
    };
    private final String[] families = {
        "Block, The Barber",
        "Bones, The Butcher",
        "Bun, The Baker",
        "Bung, The Brewer",
        "Chip, The Carpenter",
        "Dose, The Doctor",
        "Grits, The Grocer",
        "Pot, The Painter",
        "Soot, The Sweep",
        "Tape, The Tailor", 
        "Dip, The Dyer"
    };
    private MyList<Card> deck;
    public CardDeck(){
        deck = new MyList<>(Const.NUMBER_OF_CARDS);
        initializeDeck();
    }
    public void initializeDeck(){
        int mainIndex = 0;
        for(int i = 0; i < faces.length; i++){
            for(int j = 0; j < families.length; j++){
            
                Card card = new Card(faces[i], families[j]);
                String cardName = card.toString().toLowerCase().replaceAll("[,\\s]", ""); 
                allCards.put(cardName, 1);
                deck.add(mainIndex, card); 
                mainIndex++; 
            }
        }
    }
    public void shuffleDeck(){
        for(int i = 0; i < deck.size(); i++){
            int randomIndex = random.nextInt(0, deck.size()); 
            Card currentCard = deck.get(i);
            Card tempCard = deck.get(randomIndex); 
            deck.remove(randomIndex);
            deck.add(randomIndex, currentCard); 
            deck.remove(i);
            deck.add(i, tempCard);
        }
    }
    public Card popTheCard(){
        Card card = deck.get(deck.size() - 1);
        deck.remove(deck.size() - 1); 
        return card;
    }

    // Getters 
    public String[] getFamilies(){
        return this.families;
    }
    public String[] getFaces(){
        return this.faces;
    }
    public int getSize(){
        return this.deck.size();
    }
    
    public boolean isValidCard(String cardName){
        return allCards.containsKey(cardName);
    }
    public boolean isEmpty(){
        if(this.deck.size() == 0){
            return true;
        }else{
            return false;
        }
    }
}
