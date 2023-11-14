package logic;
public class Card {
    private String family; 
    private String face; 
    public Card(String cardFace, String cardFam){
        this.face = cardFace; 
        this.family = cardFam; 
    }
    public String toString(){
        return this.face + " " + this.family;
    }
    public String getFamily(){
        return this.family;
    }
}
