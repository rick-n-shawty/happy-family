import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logic.Game;

public class Main{
    public static void main(String args[]){ 
        // makes sure the UI is compatible with all devices 
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e){
            e.printStackTrace();
        }
        Game game = new Game();
        game.start();
    }
}