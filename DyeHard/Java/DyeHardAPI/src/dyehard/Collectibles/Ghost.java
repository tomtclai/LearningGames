package dyehard.Collectibles;

import Engine.BaseCode;
import dyehard.Configuration;
import dyehard.Player.Hero;
import dyehard.Player.Hero.CurPowerUp;

public class Ghost extends PowerUp {
    // public static PowerUpMeter meter = new PowerUpMeter(1, Game.Blue);

    public Ghost() {
        super();
        duration = Configuration
                .getPowerUpData(Configuration.PowerUpType.GHOST).duration * 1000;
        texture = BaseCode.resources.loadImage("Textures/PowerUp_Ghost.png");
        applicationOrder = 90;
        // label.setText("Ghost");
    }

    @Override
    public PowerUp clone() {
        return new Ghost();
    }

    @Override
    public void apply(Hero hero) {
        hero.collisionOn = false;
        hero.damageOn = false;
        hero.curPowerUp = CurPowerUp.GHOST;
    }

    @Override
    public void unapply(Hero hero) {
        hero.collisionOn = true;
        hero.damageOn = true;
        hero.curPowerUp = CurPowerUp.NONE;
    }

    @Override
    public String toString() {
        return "Ghost: " + super.toString();
    }
}
