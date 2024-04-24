package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Bullet extends GameObjects{
    float R = 6;

    float angle = 0;
    int id;

    public Bullet(float x, float y, float angle,int id, BufferedImage img) {
        super(x,y,img);
        this.angle = angle;
        this.id = id;
        this.hitbox = new Rectangle((int)x,(int)y,this.img.getWidth(),this.img.getHeight());
    }
    public int getId(){
        return id;
    }
    void BulletShift(){
        if(id==1){
            this.img = Resources.getSprite("bullet1");
        }else if(id==2){
            this.img = Resources.getSprite("bullet2");

        }
    }

    void update(){
        x += Math.round(R * Math.cos(Math.toRadians(angle)));
        y += Math.round(R * Math.sin(Math.toRadians(angle)));
//        checkBorder();
        this.hitbox.setLocation((int)x,(int)y);
        BulletShift();

    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        rotation.scale(1.5,1.5);//make the bullet be bigger
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.WHITE);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
//        g2d.drawRect((int)x,(int)y,this.img.getWidth()*1, this.img.getHeight()*1);

    }
    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.WORLD_WIDTH - 88) {
            x = GameConstants.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.WORLD_HEIGHT - 80) {
            y = GameConstants.WORLD_HEIGHT - 80;
        }
    }



}
