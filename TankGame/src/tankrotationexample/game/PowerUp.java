package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp extends GameObjects{

    public PowerUp(float x, float y, BufferedImage img) {
        super(x,y,img);
    }

    abstract void applyPower(PowerUpEffects t);


//    @Override
//    public void drawImage(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        g.drawImage(this.img,(int)x,(int)y,null);
//        g2d.setColor(Color.WHITE);
//        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
//
//    }
}
