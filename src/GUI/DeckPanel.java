package GUI;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import GUI.components.MyIcon;
import GUI.components.MyLabel;
import assets.Const;


public class DeckPanel extends JPanel {
    private JPanel iconPanel;
    private MyLabel iconLabel;
    private JPanel countPanel;
    private MyLabel countLabel;
    private MyIcon backIcon = new MyIcon("../res/images/Back-card.png");

    public DeckPanel() {
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setLayout(new BorderLayout());

        iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setBorder(BorderFactory.createEmptyBorder(15, 1, 15, 1));
        iconLabel = new MyLabel();
        try{
            Path imagePath = Paths.get("../res/images/Back-card.png");
            BufferedImage image = ImageIO.read(imagePath.toFile()); 
            backIcon = new MyIcon(image);
            iconLabel.setIcon(backIcon);
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("DECK-PANEL ISSUE");
        }
        // backIcon = new MyIcon(imagerUrl);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconPanel.add(iconLabel);

        countPanel = new JPanel();
        countPanel.setLayout(new GridLayout(1, 1));
        countPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        countPanel.setOpaque(false);
        countLabel = new MyLabel("Number of cards: ", 14);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setVerticalAlignment(SwingConstants.CENTER);
        countPanel.add(countLabel);


        this.setBackground(Const.yellowish);
        this.add(iconPanel, BorderLayout.NORTH);
        this.add(countPanel, BorderLayout.SOUTH);
    }

    public Point getPosition() {
        Point location = this.iconLabel.getLocation();
        Point newLocation = SwingUtilities.convertPoint(iconPanel, location, this);
        return newLocation;
    }

    public void updateDeckNum(int num) {
        countLabel.setText("Number of cards: " + num);
    }
}
