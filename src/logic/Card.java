package logic;

import assets.Const;

public class Card {
    private String family; 
    private String face; 
    public Card(String cardFace, String cardFam){
        this.face = cardFace; 
        this.family = cardFam; 
    }
    public String toString(){
        String name = this.face + " " + this.family; 
        String finalName = Const.convertToLower(name);
        return finalName;
    }
    public String getFamily(){
        return this.family;
    }
    public String getFace(){
        return this.face; 
    }
    public boolean comparison(String cardName){
        cardName = Const.convertToLower(cardName);
        return this.toString().equals(cardName);
    }
}
