package GUI;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import javax.swing.JFrame;
import logic.CardDeck;
import logic.Player;

public class MyFrame extends JFrame {
    GamePanel panel;

    public MyFrame(Player[] players, CardDeck deck) {
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setTitle("Happy Family");

        panel = new GamePanel(players, deck);


        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = (int) (getWidth());
                int panelHeight = (int) (getHeight());
                panel.resize(panelWidth, panelHeight);
                panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
                panel.revalidate();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
            }
        });
        this.add(panel);
        this.pack();
    }

    public GamePanel getPanel() {
        return this.panel;
    }
}
