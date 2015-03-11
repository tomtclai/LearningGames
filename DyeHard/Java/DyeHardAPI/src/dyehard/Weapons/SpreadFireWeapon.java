package dyehard.Weapons;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Rectangle;
import Engine.Vector2;
import dyehard.Player.Hero;

public class SpreadFireWeapon extends Weapon {
    private final Rectangle info;

    public SpreadFireWeapon(Hero hero) {
        super(hero);

        info = new Rectangle();
        info.center = new Vector2(BaseCode.world.getPositionX() + 12,
                BaseCode.world.getHeight() - 4);
        info.size.set(4, 4);
        info.color = Color.BLUE;
    }

    @Override
    public void fire() {
        if (timer.isDone()) {
            Bullet bullet = new Bullet(hero);
            bullet.center = new Vector2(hero.center);
            bullet.size.set(bulletSize, bulletSize);
            bullet.velocity = new Vector2(bulletSpeed, bulletSpeed / 2);
            bullet.color = hero.getColor();

            bullet = new Bullet(hero);
            bullet.center = new Vector2(hero.center);
            bullet.size.set(bulletSize, bulletSize);
            bullet.velocity = new Vector2(bulletSpeed, -bulletSpeed / 2);
            bullet.color = hero.getColor();

            bullet = new Bullet(hero);
            bullet.center = new Vector2(hero.center);
            bullet.size.set(bulletSize, bulletSize);
            bullet.velocity = new Vector2(bulletSpeed, 0f);
            bullet.color = hero.getColor();
        }
        super.fire();
    }

    @Override
    public String toString() {
        return "Spread fire";
    }
}
