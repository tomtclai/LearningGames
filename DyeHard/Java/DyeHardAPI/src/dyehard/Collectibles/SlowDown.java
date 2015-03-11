package dyehard.Collectibles;

import java.util.ArrayList;
import java.util.List;

import Engine.BaseCode;
import dyehard.Configuration;
import dyehard.Enemies.Enemy;
import dyehard.Enemies.EnemyManager;
import dyehard.Player.Hero;
import dyehard.Player.Hero.CurPowerUp;

public class SlowDown extends PowerUp {

    protected float magnitude = Configuration
            .getPowerUpData(Configuration.PowerUpType.SLOWDOWN).magnitude;

    public SlowDown() {
        super();
        duration = Configuration
                .getPowerUpData(Configuration.PowerUpType.SLOWDOWN).duration * 1000;
        texture = BaseCode.resources.loadImage("Textures/PowerUp_SlowDown.png");
        enemySpeedModifier = magnitude;
        isApplied = false;
        applicationOrder = 10;
        // label.setText("Slow");
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
        hero.curPowerUp = CurPowerUp.SLOW;
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
        return new SlowDown();
    }

    @Override
    public String toString() {
        return "Slow Down: " + super.toString();
    }

}
