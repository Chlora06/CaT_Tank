package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Damage extends PowerUp{
    public Damage(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    void applyPower(PowerUpEffects t) {
        t.damage();
    }
}
