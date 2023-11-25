package GUI.components;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

import assets.Const;
public class MyIcon extends ImageIcon {
    private ImageIcon icon; 
    public MyIcon(String filePath){
        icon = new ImageIcon(filePath);
        Image scaledImage = icon.getImage().getScaledInstance(Const.CARD_ICON_WIDTH, Const.CARD_ICON_HEIGHT, Image.SCALE_SMOOTH); 
        this.setImage(scaledImage);
    }
    public MyIcon(URL url){
        icon = new ImageIcon(url);
        Image scaledImage = icon.getImage().getScaledInstance(Const.CARD_ICON_WIDTH, Const.CARD_ICON_HEIGHT, Image.SCALE_SMOOTH); 
        this.setImage(scaledImage);
    }
}
