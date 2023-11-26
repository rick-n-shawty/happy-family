package assets;

import java.awt.Color;

public class Const {
    public static final int NUMBER_OF_CARDS = 44;
    public static final Color FONT_COLOR = Color.BLACK;
    public static final String FONT_STYLE = "Arial"; 
    public static final int CARD_ICON_WIDTH = 75; 
    public static final int CARD_ICON_HEIGHT = 110; 
    // panel colors 
    public static final Color mainPlayerColor = new Color(42, 157, 143);
    public static final Color apricot = new Color(255,206,190);
    public static final Color yellowish = new Color(222, 164, 80);
    // player colors 
    public static final Color orange = new Color(184, 61, 2);
    public static final Color purple = new Color(138, 80, 130);
    public static final Color brightPink = new Color(205,44,108); 
    public static final Color blue = new Color(37, 66, 82);
    public static final Color[] playerColors = {mainPlayerColor, orange, brightPink, purple};

    public static final String[] faces = {"Mr", "Mrs", "Master", "Miss"};
    public static final String[] families = {
        "Block, The Barber",
        "Bones, The Butcher",
        "Bun, The Baker",
        "Bung, The Brewer",
        "Chip, The Carpenter",
        "Dose, The Doctor",
        "Grits, The Grocer",
        "Pots, The Painter",
        "Soot, The Sweep",
        "Tape, The Tailor", 
        "Dip, The Dyer"
    };
    public static final int FRAME_WIDTH = 850;
    public static int CARD_DECK_X = FRAME_WIDTH - 112;
    public static int CARD_DECK_Y = 167;
    public static final String[] BOT_NAMES = {"You","Zaeem", "Deborah", "Sam"};
    public static final String myRegex = "[,\\s]";
    public Const(){}
    public static String convertToLower(String input){
        return input.toLowerCase().replaceAll(myRegex, "");
    }
}
