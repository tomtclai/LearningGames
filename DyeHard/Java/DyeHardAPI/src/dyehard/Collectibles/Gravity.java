package dyehard.Collectibles;

import Engine.BaseCode;
import dyehard.Configuration;
import dyehard.Player.Hero;
import dyehard.Player.Hero.CurPowerUp;

public class Gravity extends PowerUp {

    protected float magnitude = Configuration
            .getPowerUpData(Configuration.PowerUpType.GRAVITY).magnitude;

    public Gravity() {
        duration = Configuration
                .getPowerUpData(Configuration.PowerUpType.GRAVITY).duration * 1000;
        texture = BaseCode.resources.loadImage("Textures/PowerUp_Gravity.png");
        applicationOrder = 2;
        // label.setText("Gravity");
    }

    @Override
    public void apply(Hero hero) {
        hero.currentGravity.set(0f, -magnitude);
        hero.curPowerUp = CurPowerUp.GRAVITY;
    }

    @Override
    public void unapply(Hero hero) {
        hero.currentGravity.set(0f, 0f);
        hero.curPowerUp = CurPowerUp.NONE;
    }

    @Override
    public PowerUp clone() {
        return new Gravity();
    }

    @Override
    public String toString() {
        return "Gravity: " + super.toString();
    }

}
