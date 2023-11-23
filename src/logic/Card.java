package logic;
public class Card {
    private String family; 
    private String face; 
    public Card(String cardFace, String cardFam){
        this.face = cardFace; 
        this.family = cardFam; 
    }
    public String toString(){
        String name = this.face + " " + this.family; 
        String finalName = name.toLowerCase().replaceAll("[,\\s]", "");
        return finalName;
    }
    public String getFamily(){
        return this.family;
    }
    public String getFace(){
        return this.face; 
    }
    public boolean comparison(String cardName){
        cardName = cardName.toLowerCase().replaceAll("[,\\s]", "");
        return this.toString().equals(cardName);
    }
}
