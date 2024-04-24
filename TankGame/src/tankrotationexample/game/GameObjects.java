package tankrotationexample.game;

import tankrotationexample.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class GameObjects {
    protected float x,y;
    protected BufferedImage img;
    protected Rectangle hitbox;
    protected boolean hasCollided;

    public GameObjects(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x,(int)y,this.img.getWidth(),this.img.getHeight());
        this.hasCollided = false;
    }

    public Rectangle getHitBox(){
        return this.hitbox.getBounds();
    }


    public static GameObjects gameObjectFactory( String type, float x, float y) {
        switch (type) {
            case "2", "9" -> {
                return new Wall(x, y, Resources.getSprite("unbreak"));
            }
            case "3" -> {
                return new Breakable(x, y, Resources.getSprite("break"));
            }
            case "4" -> {//damage
                return new Damage(x, y, Resources.getSprite("damage"));
            }
            case "5" -> {//speed
                return new Speed(x, y, Resources.getSprite("speed"));
            }
            case "6" -> {//health
                return new Health(x, y, Resources.getSprite("health"));
            }
        }
        return null;
    }
    public void drawImage(Graphics g){
        Graphics2D g2d = (Graphics2D) g;


        g.drawImage(this.img, (int)x,(int)y, null);
    }
}
