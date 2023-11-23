package assets;

import java.awt.Color;

public class Const {
    public static final int NUMBER_OF_CARDS = 44;
    public static final Color FONT_COLOR = Color.BLACK;
    public static final String FONT_STYLE = "Arial"; 
    public static final int CARD_ICON_WIDTH = 75; 
    public static final int CARD_ICON_HEIGHT = 110; 
    public static final String[] faces = {"Mr", "Mrs", "Master", "Miss"};
    public static final int FRAME_WIDTH = 850;
    public static final int CARD_DECK_X = FRAME_WIDTH - 112;
    public static final int CARD_DECK_Y = 167;
    public static final String[] BOT_NAMES = {"You","Zaeem", "Deborah", "Sam"};
    public static final String myRegex = "[,\\s]";
    public Const(){}
    public static String convertToLower(String input){
        return input.toLowerCase().replaceAll(myRegex, "");
    }
}
