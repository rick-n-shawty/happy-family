package GUI;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
