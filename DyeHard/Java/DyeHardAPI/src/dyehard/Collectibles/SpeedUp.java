package dyehard.Collectibles;

import java.util.ArrayList;
import java.util.List;

import Engine.BaseCode;
import dyehard.Configuration;
import dyehard.Enemies.Enemy;
import dyehard.Enemies.EnemyManager;
import dyehard.Player.Hero;
import dyehard.Player.Hero.CurPowerUp;

public class SpeedUp extends PowerUp {
    // public static PowerUpMeter meter = new PowerUpMeter(0, DyeHard.Green);

    protected float magnitude = Configuration
            .getPowerUpData(Configuration.PowerUpType.SPEEDUP).magnitude;

    public SpeedUp() {
        super();
        duration = Configuration
                .getPowerUpData(Configuration.PowerUpType.SPEEDUP).duration * 1000;
        texture = BaseCode.resources.loadImage("Textures/PowerUp_SpeedUp.png");
        enemySpeedModifier = magnitude;
        isApplied = false;
        applicationOrder = 20;
        // label.setText("Speed");
    }

    protected boolean isApplied;
    protected final float enemySpeedModifier;
    protected List<Enemy> affectedEnemies;

    @Override
    public void apply(Hero hero) {
        if (isApplied) { // only applies once
            return;
        }

        affectedEnemies = new ArrayList<Enemy>(EnemyManager.getEnemies());

        for (Enemy e : affectedEnemies) {
            e.speed *= enemySpeedModifier;
        }

        isApplied = true;
        hero.curPowerUp = CurPowerUp.SPEED;
    }

    @Override
    public void unapply(Hero hero) {
        if (affectedEnemies != null) {
            for (Enemy e : affectedEnemies) {
                e.speed /= enemySpeedModifier;
            }
        }
        hero.curPowerUp = CurPowerUp.NONE;
    }

    @Override
    public PowerUp clone() {
        return new SpeedUp();
    }

    @Override
    public String toString() {
        return "Speed Up: " + super.toString();
    }
}
