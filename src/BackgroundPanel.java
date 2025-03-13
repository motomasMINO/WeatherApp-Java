import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            // 背景画像を読み込む
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("背景画像を読み込めませんでした: " + imagePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImage != null) {
            // 背景画像を描画する
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}