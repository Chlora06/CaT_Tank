package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObjects implements PowerUpEffects{

    private float screenX;
    private float screenY;
    private float vx;
    private float vy;
    private float angle;
    private int id;


    private  long coolDown = 250;

    private  long timeLastShot = 0;

    private int live = 3;
    private int health = 100;
    private int damage = 15;
    private  int TrueLive = 3;
    private boolean isLiving = true;

    private float R = 5;
    private float ROTATIONSPEED = 1.0f;


    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;
    List<Bullet> ammo = new ArrayList<>(50);


    Tank(float x, float y,  float angle, int id, BufferedImage img) {
        super(x,y,img);
        this.angle = angle;
        this.id = id;
    }

    public float getX(){ return this.x; }

    public float getY() {return this.y;}

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleShootPressed() { this.shootPressed = true; }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToggleShootPressed() { this.shootPressed = false; }

    public void Damage(){
       health = health-damage;
    }

    public boolean getIsLiving(){
        return isLiving;
    }

    void update(GameWorld gw) {

        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }
        shapeShift(gw);
        if(this.shootPressed && (this.timeLastShot + coolDown)< System.currentTimeMillis()){
            if(this.id==1 && shootPressed){
                Resources.getSound("t1hit").playSound();
                this.img = Resources.getSprite("tank1.1");
            }
            if(this.id==2 && shootPressed){
                Resources.getSound("t2hit").playSound();
                this.img = Resources.getSprite("tank2.2");

            }
            this.timeLastShot =System.currentTimeMillis();
//            System.out.println("Tank shot a bullet.");
                Bullet b = new Bullet(setBulletStartX(),setBulletStartY(),angle,id, Resources.getSprite("bullet1"));
                gw.addGameObject(b); //(remove bullets)
                gw.addAnimation(new Animation(b.x,b.y,Resources.getAnimation("shoot")));
                this.ammo.add(b);
        }

        centerScreen();
        this.ammo.forEach(bullet-> bullet.update());
        removeBullet();


        if(this.health<=0){
            if(TrueLive==1 && this.health<=100){
                isLiving = false;

            }
            TrueLive--;
            this.health=100;
        }
    }
    void shapeShift(GameWorld gw){

        if(this.id==1 && !shootPressed){
            this.img = Resources.getSprite("tank1");

        }
        if(this.id ==2 && !shootPressed){
            this.img = Resources.getSprite("tank2");

        }

    }
    private int setBulletStartX() {
        float cx = 25f*(float) Math.cos(Math.toRadians(angle));
        return (int) x + this.img.getWidth() /2 +(int)cx-4;
    }
    private int setBulletStartY() {
        float cy = -9f*(float) Math.cos(Math.toRadians(angle));
        return (int) y + this.img.getWidth() /2 +(int)cy-4;
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        this.hitbox.setLocation((int)x,(int)y);
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int)x,(int)y);

    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.WORLD_WIDTH - 84) {
            x = GameConstants.WORLD_WIDTH - 84;
        }
        if (y < 35) {
            y = 35;
        }
            if (y >= GameConstants.WORLD_HEIGHT - 145) {
            y = GameConstants.WORLD_HEIGHT - 145;
        }
    }

    private void centerScreen(){
        this.screenX = this.x - GameConstants.GAME_SCREEN_WIDTH/4f;
        this.screenY = this.y - GameConstants.GAME_SCREEN_HEIGHT/4f;

        if(this.screenX < 0) screenX =0;
        if(this.screenY < 0) screenY =0;

        if(this.screenX > GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f ){
           this.screenX = GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f;

        }
        if(this.screenY > GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT){
            this.screenY = GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }

    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle+" ,vx = "+ vx+" , vy="+ vy;
    }

    public int getScreenX(){
        return (int)screenX;
    }
    public int getScreenY(){
        return (int)screenY;
    }

    public void setCoolDown(long newCoolDown){
        this.coolDown = newCoolDown;
        if(this.coolDown < 250){
            this.coolDown = 250;
        }
    }

    public void removeBullet(){
        this.ammo.removeIf(bullet -> bullet.hasCollided);//delete bullet if it can hit the wall

    }
    public int getId() {
        return id;
    }



    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
//        rotation.scale(1,1);//make the tank be bigger
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
//        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
//        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
//        g2d.fillRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight()); // fill square in red
        g2d.setColor(Color.GREEN);
        if(this.health<50){
            g2d.setColor(Color.RED);
        }
        g2d.drawRect((int)x,(int)y-20,100,15);//the total health
        g2d.fillRect((int)x,(int)y-20,this.health,15);

        for (int i = 0; i < live; i++) {
            g2d.drawOval((int) x + 15 * i, (int) y + 80, 10, 10);
        }

        for (int j = 0; j < TrueLive; j++) {
                g2d.fillOval((int)x+15*j,(int)y+80, 10,10);
        }

    }

    public void AwayFromWall(){
        if(UpPressed){
            if(vx>0 || vx<0){
                x=x-vx;
                unToggleUpPressed();
            }
            if(vy>0||vy<0){
                y=y-vy;
                unToggleUpPressed();
            }
        }
        if(DownPressed){
                if(vx<0 || vx>0){
                    x=x+vx;
                    unToggleDownPressed();
                }
                if(vy<0||vy>0){
                    y=y+vy;
                    unToggleDownPressed();
                }
            }


    }



    void collideWith(GameObjects other){
       if(other instanceof Health){
           this.health();
       }
       if(other instanceof Speed){
           this.speed();
       }
       if(other instanceof Damage){
           this.damage();
       }
    }

    @Override
    public void health() {
        if(TrueLive == 3 ){
            if(this.health<=50){
                health = health+50;
            } else if (this.health>=50 ){
                this.health = 100;
            }
        }else if(TrueLive < 3){
            if(this.health<=50){
                health = health+50;
            } else if (this.health>50 && this.health<100){
                this.health = 50-(100-health);
                TrueLive = TrueLive+1;
            }else if (this.health>=100){
                this.health = 100;
            }
        }
    }

    @Override
    public void speed() {
        if(R< 10){
            R = R + 2;
        }
    }

    @Override
    public void damage() {
       coolDown-=50;
    }
}
