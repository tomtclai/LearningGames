package dyehard.Weapons;

import java.awt.Color;

import Engine.Vector2;
import dyehard.Collidable;
import dyehard.DyehardRectangle;
import dyehard.Enemies.Enemy;
import dyehard.Player.Hero;

public class Bullet extends Collidable {
    public Color dyeColor;

    private final DyehardRectangle muzzle;

    Hero hero;
    boolean firing = true;

    public Bullet(Hero hero) {
        this.hero = hero;

        size = new Vector2(6.25f, 2.25f);
        dyeColor = hero.getColor();
        texture = hero.bulletTextures.get(dyeColor);
        setPosition(this);
        shouldTravel = true;
        velocity = new Vector2(3f, 0f);

        muzzle = new DyehardRectangle();
        muzzle.size = new Vector2(2.5f, 3.5f);
        muzzle.texture = hero.muzzleTextures.get(dyeColor);
        muzzle.setUsingSpriteSheet(true);
        muzzle.setSpriteSheet(muzzle.texture, 50, 70, 3, 3);

        hero.isFiring = true;
    }

    @Override
    public void update() {
        setPosition(muzzle);
        if ((muzzle.spriteCycleDone) && (firing)) {
            hero.isFiring = false;
            muzzle.destroy();
            firing = false;
        } else if (!muzzle.spriteCycleDone) {
            hero.isFiring = true;
        }
        super.update();
    }

    private void setPosition(DyehardRectangle c) {
        switch (hero.getDirection()) {
        case NEUTRAL:
            c.center = hero.center.clone()
                    .add(new Vector2(hero.size.getX() * .6f,
                            hero.size.getY() * .23f));
            break;
        case UP:
            c.center = hero.center.clone().add(
                    new Vector2(hero.size.getX() * .58f,
                            hero.size.getY() * .18f));
            break;
        case DOWN:
            c.center = hero.center.clone().add(
                    new Vector2(hero.size.getX() * .69f,
                            hero.size.getY() * .15f));
            break;
        case BACK:
            c.center = hero.center.clone().add(
                    new Vector2(hero.size.getX() * .74f,
                            hero.size.getY() * .16f));
            break;
        case FORWARD:
            c.center = hero.center.clone()
                    .add(new Vector2(hero.size.getX() * .6f,
                            hero.size.getY() * .14f));
            break;
        case UPFORWARD:
            c.center = hero.center.clone().add(
                    new Vector2(hero.size.getX() * .63f,
                            hero.size.getY() * .26f));
            break;
        case UPBACK:
            c.center = hero.center.clone()
                    .add(new Vector2(hero.size.getX() * .6f,
                            hero.size.getY() * .16f));
            break;
        case DOWNFORWARD:
            c.center = hero.center.clone().add(
                    new Vector2(hero.size.getX() * .64f,
                            hero.size.getY() * .19f));
            break;
        case DOWNBACK:
            c.center = hero.center.clone().add(
                    new Vector2(hero.size.getX() * .68f,
                            hero.size.getY() * .13f));
            break;
        }
    }

    @Override
    public void handleCollision(Collidable other) {
        if ((other instanceof Enemy) && (other.color != dyeColor)) {
            // int frame = getCurFrame();
            // float offset = 0;
            // if (frame == 3) {
            // offset = 0.15f;
            // } else if (frame == 4) {
            // offset = 0.3728f;
            // } else if (frame == 5) {
            // offset = 0.74f;
            // } else if (frame == 6) {
            // offset = 1f;
            // }
            //
            // float xMax = center.getX() + (offset * size.getX())
            // + (other.size.getX() * 0.5f);
            // float xMin = center.getX() - (size.getX() * 0.5f)
            // - (0.5f * other.size.getX());
            //
            // float yMax = center.getY() + (0.15f * size.getY())
            // + (other.size.getY() * 0.4f);
            // float yMin = center.getY() - (0.3f * size.getY())
            // - (other.size.getY() * 0.4f);
            //
            // if ((other.center.getX() < xMax) && (other.center.getX() > xMin)
            // && (other.center.getY() < yMax)
            // && (other.center.getY() > yMin)) {
            // Enemy enemy = (Enemy) other;
            // enemy.setColor(dyeColor);
            // enemy.beenHit = true;
            // collidableState = ManagerState.DESTROYED;
            // }
            Enemy enemy = (Enemy) other;
            enemy.setColor(dyeColor);
            enemy.beenHit = true;
            enemy.setHarmless();
        }
    }
}
