package tankrotationexample;

import tankrotationexample.game.GameWorld;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;


import static javax.imageio.ImageIO.read;

public class Resources {

    private static Map<String, BufferedImage> sprites = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();
    private static Map<String, List<BufferedImage>> animations = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException{
        return read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource(path)));
    }

//    private static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
//        AudioInputStream as = null;
//
//            as = AudioSystem.getAudioInputStream(
//                    Objects.requireNonNull(Resources.class.getClassLoader().getResource(path)));
//        Clip clip = null;
//        clip = AudioSystem.getClip();
//        clip.open(as);
//        Sound s = new Sound(clip);
//        s.setVolume(2f);
//        return s;
//    }

    private static void initSprites() {
        try {
            Resources.sprites.put("tank1", loadSprite("tank/tank1.png"));
            Resources.sprites.put("tank1.1", loadSprite("tank/tank1.1.png"));
            Resources.sprites.put("tank2", loadSprite("tank/tank2.png"));
            Resources.sprites.put("tank2.2", loadSprite("tank/tank2.1.png"));
            Resources.sprites.put("bullet1", loadSprite("bullet/bullet1.png"));
            Resources.sprites.put("bullet2", loadSprite("bullet/bullet2.png"));
            Resources.sprites.put("rocket", loadSprite("bullet/rocket1.png"));
            Resources.sprites.put("floor", loadSprite("floor/floor.bmp"));
            Resources.sprites.put("unbreak", loadSprite("walls/wall1.png"));
            Resources.sprites.put("break", loadSprite("walls/wall2.png"));
            Resources.sprites.put("menu", loadSprite("menu/title.png"));
            Resources.sprites.put("speed", loadSprite("powerups/speed.png"));
            Resources.sprites.put("health", loadSprite("powerups/health.png"));
            Resources.sprites.put("damage", loadSprite("powerups/damage.png"));
            Resources.sprites.put("t1win", loadSprite("End/t1win.png"));
            Resources.sprites.put("t2win", loadSprite("End/t2win.png"));


        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }

    private static void initSounds() {
        AudioInputStream audioStream;
        Clip c;
        Sound s;

        try {
            audioStream = AudioSystem.getAudioInputStream(
                    Resources.class.getClassLoader().getResource("sounds/Background.wav"));

            try {
                c = AudioSystem.getClip();
                c.open((audioStream));
                s = new Sound(c);
                s.setVolume(2f);
                Resources.sounds.put("bg", s);

                audioStream = AudioSystem.getAudioInputStream(
                        Resources.class.getClassLoader().getResource("sounds/pickPowerup.wav"));
                c = AudioSystem.getClip();
                c.open((audioStream));
                s = new Sound(c);
                Resources.sounds.put("powerup", s);

                audioStream = AudioSystem.getAudioInputStream(
                        Resources.class.getClassLoader().getResource("sounds/Tank1Hit.wav"));
                c = AudioSystem.getClip();
                c.open((audioStream));
                s = new Sound(c);
                s.setVolume(1.5f);
                Resources.sounds.put("t1hit", s);

                audioStream = AudioSystem.getAudioInputStream(
                        Resources.class.getClassLoader().getResource("sounds/Tank2Hit.wav"));
                c = AudioSystem.getClip();
                c.open((audioStream));
                s = new Sound(c);
                s.setVolume(1.5f);
                Resources.sounds.put("t2hit", s);

                audioStream = AudioSystem.getAudioInputStream(
                        Resources.class.getClassLoader().getResource("sounds/alreadyHit.wav"));
                c = AudioSystem.getClip();
                c.open((audioStream));
                s = new Sound(c);
                s.setVolume(0.5f);
                Resources.sounds.put("alreadyHit", s);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void initSound(){
//        try{
//            Resources.sounds.put("background", loadSound("sounds/Background.wav"));
//        }catch (UnsupportedAudioFileException | IOException| LineUnavailableException e) {
//            System.out.println(e.getMessage());
//            System.exit(-1);
//        }
//    }

    private static void initAnimations() {
        try {
            String base = "animations/explosion/explosion2_%d.png";//"expl_08_%04d.png from 0000 to 00xx"
            List<BufferedImage> temp = new ArrayList<>();
            for (int i = 1; i < 8; i++) { // 1: the size of the exlopsion picture
                String fName = String.format(base, i);
//                System.out.println(fName);
                temp.add(loadSprite(fName));
            }
            Resources.animations.put("shoot",temp);
//
        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e);
            System.exit(-3);
        }

    }

    public static void loadResources() {
        initSprites();
        initSounds();
        initAnimations();
    }

    public static BufferedImage getSprite(String key){
        if(!Resources.sprites.containsKey(key)){
            System.out.println(key+ "resource not found.");
            System.exit(-2);
        }
        return Resources.sprites.get(key);

    }
    public static Sound getSound(String key){
        if(!Resources.sounds.containsKey(key)){
            System.out.println(key+ "sound not found.");
            System.exit(-2);
        }
        return Resources.sounds.get(key);

    }


    public static List<BufferedImage> getAnimation(String key) {
        if(!Resources.animations.containsKey(key)){
            System.out.println(key+ "sound not found.");
            System.exit(-2);
        }
        return Resources.animations.get(key);

    }
}
