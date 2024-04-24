package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;

public class Wall extends GameObjects{

    float x,y;
    BufferedImage img;

    public Wall(float x, float y, BufferedImage img) {
        super(x,y,img);
    }

//    public void drawImage(Graphics g) {
//
//        Graphics2D g2d = (Graphics2D) g;
//        g.drawImage(this.img,(int)x,(int)y,null);
//        g2d.setColor(Color.YELLOW);
//        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
//    }
}
