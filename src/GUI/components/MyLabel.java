package GUI.components;
import javax.swing.JLabel;
import assets.Const;
import java.awt.Font;
public class MyLabel extends JLabel {
    public MyLabel(String text, int fontSize){
        this.setForeground(Const.FONT_COLOR);
        this.setVisible(true);
        this.setFont(new Font(Const.FONT_STYLE, Font.BOLD, fontSize));
        this.setText(text);
    }
    public MyLabel(String text){
        this.setForeground(Const.FONT_COLOR);
        this.setVisible(true);
        this.setFont(new Font(Const.FONT_STYLE, Font.BOLD, 13));
        this.setText(text);
    }
    public MyLabel(){
        this.setForeground(Const.FONT_COLOR);
        this.setVisible(true);
        this.setFont(new Font(Const.FONT_STYLE, Font.BOLD, 13));
    }
    public MyLabel(int size){
        this.setForeground(Const.FONT_COLOR);
        this.setVisible(true);
        this.setFont(new Font(Const.FONT_STYLE, Font.BOLD, size));
    }
}
