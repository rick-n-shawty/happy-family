package GUI;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import javax.swing.JFrame;

import logic.CardDeck;
import logic.Player;
public class MyFrame extends JFrame {
    GamePanel panel; 
    
    public MyFrame(Player[] players, CardDeck deck){
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        
        panel = new GamePanel(players, deck);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                int panelWidth = (int) (getWidth()); // 50% of frame's width
                int panelHeight = (int) (getHeight()); // 50% of frame's height
                panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
                panel.revalidate();        
            }
        });
        this.add(panel);
        this.pack();
        System.out.println(panel.getWidth() + " " + this.getHeight());
    }

    public GamePanel getPanel(){
        return this.panel;
    }
}
