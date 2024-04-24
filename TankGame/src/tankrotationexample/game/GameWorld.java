/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources;
import tankrotationexample.menus.EndGamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Sound bgMusic;
    private Tank t1;
    private Tank t2;
    private Launcher lf;
    public long tick = 1;
    public boolean isRunning = true;


//    Sound bg = Resources.getSound("bg");
    private List<GameObjects> gameObjects = new ArrayList<>(500);
    private List<Animation> anims = new ArrayList<>(20);


    /**
     * 
     * @param lf
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {

                this.resetGame();
                bgMusic = Resources.getSound("bg");
                bgMusic.setVolume(0.05f);
                bgMusic.setLooping();
                bgMusic.playSound();
            while (isRunning) {
                this.t1.update(this); // update tank
                this.t2.update(this);
//                if(t1.getHitBox().intersects(this.t2.getHitBox())){//if two tanks meet
//                    this.t1.setX(50);
//                    this.t1.setY(50);//reset tank's location
//                }
                this.checkCollisions();// explosion by self
                this.anims.forEach(a -> a.update());
                this.gameObjects.removeIf(g -> g.hasCollided);// remove powerup if the tank meet it
                this.anims.removeIf(a -> !a.isRunning);
                this.repaint();   // redraw game

                if (!t2.getIsLiving()) {

                    this.lf.Winner = 1;
                    this.lf.initEndPanel();
                    this.lf.setFrame("end");
                    t1.hasCollided = true;
                    t2.hasCollided = true;
                    InitializeGame();
                    isRunning = false;
                    return;
                }else if(!t1.getIsLiving()){
                    this.lf.Winner = 2;
                    this.lf.initEndPanel();

                    this.lf.setFrame("end");
                    t1.hasCollided = true;
                    t2.hasCollided = true;
                    InitializeGame();
                    isRunning = false;
                    return;
                }


                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */
                Thread.sleep(7);
            }
                /*
                 * simulate an end game event
                 * we will do this with by ending the game when ~8 seconds has passed.
                 * This will need to be changed since the will always close after 8 seconds
                 */

            }
         catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }


    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
        this.t2.setX(1600);
        this.t2.setY(1200);
        isRunning = true;

    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    private void checkCollisions(){
        for (int i = 0; i < this.gameObjects.size(); i++) {
            GameObjects ob1 = this.gameObjects.get(i);
            if(ob1 instanceof Wall || ob1 instanceof PowerUp){ continue;}//obj1 : tank or bullet
            for (int j = 0; j < this.gameObjects.size(); j++) {
                if(i==j) continue;
                GameObjects ob2 = this.gameObjects.get(j);
                if(ob1.getHitBox().intersects(ob2.getHitBox())) {
//                    System.out.println(ob1+ "--------->"+ob2);
                    if (ob2 instanceof PowerUp && !(ob1 instanceof Bullet)) {
                        if(ob1 instanceof Tank){
                        ob2.hasCollided = true;
                        ((Tank) ob1).collideWith(ob2);
                        Resources.getSound("powerup").playSound();}
                    }
                    if (ob2 instanceof Wall && !(ob1 instanceof Tank)){
                        if(ob1 instanceof Bullet){
                            ob1.hasCollided = true;
                            if(ob2 instanceof Breakable){
                                ob2.hasCollided = true;
                            }

                            Bullet b = (Bullet) ob1;
                            this.anims.add(new Animation(b.x, b.y, Resources.getAnimation("shoot")));
                        }
                        Resources.getSound("alreadyHit").playSound();
                    }
                    if(ob1 instanceof Tank && ob2 instanceof  Wall){
                        Tank t = (Tank)ob1;
                        t.AwayFromWall();

                    }
                    if(ob1 instanceof Bullet && !(ob2 instanceof Wall)){
                        if(ob2 instanceof Tank ){
                            if(((Bullet) ob1).getId()== ((Tank) ob2).getId()){continue; }
                        ob1.hasCollided = true;
                        ((Tank) ob2).Damage();
                        }
                    }

                }
            }
        }
    }

    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                GameConstants.WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);


        try(BufferedReader mapReader = new BufferedReader(new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("maps/map1.csv")))) {
            for (int i = 0; mapReader.ready(); i++) {
                String[] gameObjects = mapReader.readLine().split(",");
                for (int j = 0; j < gameObjects.length; j++) {
                    String objectType = gameObjects[j];
                    if(Objects.equals("0", objectType)) continue;
                    this.gameObjects.add(GameObjects.gameObjectFactory(objectType,j*30,i*30));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        t1 = new Tank(1400, 300, 0, 1,Resources.getSprite("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);


        t2 = new Tank(800, 500, 0, 2, Resources.getSprite("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);
        this.gameObjects.add(t1);
        this.gameObjects.add(t2);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.black);
        buffer.fillRect(0,0,GameConstants.WORLD_WIDTH,GameConstants.WORLD_HEIGHT);
        drawFloor(buffer);
        for (int i = 0; i < gameObjects.size(); i++) {
            if(gameObjects.get(i) instanceof Tank ){continue;}
            this.gameObjects.get(i).drawImage(buffer);
        }
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);

        for (int i = 0; i < this.anims.size(); i++) {
                Animation a=anims.get(i);
                a.drawImage(buffer);
        }
        drawSplitScreen(g2,world);
//        g2.drawImage(world, 0, 0, null);
        drawMiniMap(g2,world);
    }
//    static BufferedImage getBulletImage(){ // can't for many images(just for one images)
//        return GameWorld.bullet;
//    }

    void drawFloor(Graphics2D buffer){
        for (int i = 0; i < GameConstants.WORLD_WIDTH; i+=320) {
            for (int j = 0; j < GameConstants.WORLD_HEIGHT; j+=320) {
                buffer.drawImage(Resources.getSprite("floor"), i,j,null);
            }
        }
    }
    void drawMiniMap(Graphics2D g, BufferedImage world){
        BufferedImage mm = world.getSubimage(0,0,GameConstants.WORLD_WIDTH,GameConstants.WORLD_HEIGHT);
        AffineTransform at = new AffineTransform();

//        at.translate(GameConstants.GAME_SCREEN_WIDTH/2f-(GameConstants.WORLD_WIDTH*.2f)/2f,
//                GameConstants.GAME_SCREEN_HEIGHT-(GameConstants.GAME_SCREEN_HEIGHT*.2f));
        g.scale(.2,.2);
        g.drawImage(mm,2000,2200,null);
//        g.drawImage(mm,at,null);

    }
    void drawSplitScreen(Graphics2D g, BufferedImage world){
        BufferedImage lh = world. getSubimage(t1.getScreenX(),
                t1.getScreenY(),
                GameConstants.GAME_SCREEN_WIDTH/2,
                GameConstants.GAME_SCREEN_HEIGHT);

        BufferedImage rh = world. getSubimage(t2.getScreenX(),
                t2.getScreenY(),
                GameConstants.GAME_SCREEN_WIDTH/2,
                GameConstants.GAME_SCREEN_HEIGHT);
        g.drawImage(lh,0,0,null);
        g.drawImage(rh,GameConstants.GAME_SCREEN_WIDTH/2,0,null);
    }
    public void addGameObject(Bullet b){
        this.gameObjects.add(b);
    }

    public void addAnimation(Animation animation) {
        this.anims.add(animation);
    }



}
