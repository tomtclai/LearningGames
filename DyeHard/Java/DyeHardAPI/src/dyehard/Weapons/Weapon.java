package dyehard.Weapons;

import java.util.ArrayList;

import dyehard.Configuration;
import dyehard.ManagerState;
import dyehard.UpdateManager;
import dyehard.UpdateManager.Updateable;
import dyehard.Enemies.Enemy;
import dyehard.Player.DyeMeter.Progressable;
import dyehard.Player.Hero;
import dyehard.Util.DyeHardSound;
import dyehard.Util.Timer;

public class Weapon implements Updateable, Progressable {
    protected static float bulletSpeed = 1f;
    protected static float bulletSize = 1.5f;
    protected Hero hero;
    protected ArrayList<Enemy> enemies;
    protected float fireRate = Configuration.overheatFiringRate;
    protected Timer timer;

    public Weapon(Hero hero) {
        this.hero = hero;
        timer = new Timer(fireRate);
        UpdateManager.register(this);
    }

    // Fire the weapon
    public void fire() {
        if (timer.isDone()) {
            DyeHardSound.playMulti(DyeHardSound.paintSpraySound);
            new Bullet(hero);
            timer.reset();
        }
    }

    public void resetTimer() {
        timer.reset();
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    @Override
    public void update() {
        return;
    }

    @Override
    public ManagerState updateState() {
        return hero.updateState();
    }

    @Override
    public String toString() {
        return "Default";
    }

    @Override
    public int currentValue() {
        return 1;
    }

    @Override
    public int totalValue() {
        return 1;
    }

    @Override
    public void setSpeed(float factor) {
        // TODO Auto-generated method stub

    }
}
