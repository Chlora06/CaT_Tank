package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Health extends PowerUp{
    public Health(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    void applyPower(PowerUpEffects t) {
        t.health();
    }

}
