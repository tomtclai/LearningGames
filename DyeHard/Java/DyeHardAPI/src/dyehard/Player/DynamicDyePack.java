package dyehard.Player;

import java.util.HashMap;
import java.util.Map;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.DyehardRectangle;
import dyehard.Collectibles.DyePack;
import dyehard.Player.Hero.Direction;

public class DynamicDyePack extends DyehardRectangle {
    Hero hero;
    private final float height = 2f;
    private final float width = 1.75f;

    public class Offset {
        public Offset(float rotation, float x, float y) {
            this.rotation = rotation;
            transform = new Vector2(x, y);
        }

        public float rotation;
        public Vector2 transform;
    }

    protected Map<Direction, Offset> offsets;

    public DynamicDyePack(Hero hero) {
        this.hero = hero;
        color = hero.color;
        size.set(width, height);
        shouldTravel = false;
        visible = true;
        texture = BaseCode.resources.loadImage(DyePack.getTexture(color));

        // Offset equal to direction of travel
        // offsets = new HashMap<Direction, Offset>();
        // offsets.put(Direction.TOPLEFT, new Offset(30, -4, 4));
        // offsets.put(Direction.TOPRIGHT, new Offset(-30, 4, 4));
        // offsets.put(Direction.BOTTOMLEFT, new Offset(30, -4, -4));
        // offsets.put(Direction.BOTTOMRIGHT, new Offset(-30, 4, -4));
        // offsets.put(Direction.UP, new Offset(0, 0, 4));
        // offsets.put(Direction.DOWN, new Offset(0, 0, -4));
        // offsets.put(Direction.LEFT, new Offset(0, -4, 0));
        // offsets.put(Direction.RIGHT, new Offset(0, 4, 0));
        // offsets.put(Direction.NEUTRAL, new Offset(0, 0, 0));

        // Constant offset for all directions
        offsets = new HashMap<Direction, Offset>();
        offsets.put(Direction.UP, new Offset(30, -0.8f, 0.2f));
        offsets.put(Direction.DOWN, new Offset(30, -1, 0f));
        offsets.put(Direction.BACK, new Offset(30, -1, 0.2f));
        offsets.put(Direction.FORWARD, new Offset(30, 0f, 0f));
        offsets.put(Direction.NEUTRAL, new Offset(30, -1, 1f));
        offsets.put(Direction.UPFORWARD, new Offset(30, -1, 0f));
        offsets.put(Direction.UPBACK, new Offset(30, -1, 0.2f));
        offsets.put(Direction.DOWNFORWARD, new Offset(30, 0f, 0f));
        offsets.put(Direction.DOWNBACK, new Offset(30, -1, 0f));
    }

    @Override
    public void update() {
        updateColor();
        updatePosition();
    }

    @Override
    public void draw() {
        // removeFromAutoDrawSet();
        // addToAutoDrawSet();
        super.draw();
    }

    protected void updateColor() {
        if (hero.color != color) {
            color = hero.color;
            texture = BaseCode.resources.loadImage(DyePack.getTexture(color));
        }
    }

    public void updatePosition() {
        // if (hero.direction == Direction.TOPLEFT) {
        // rotate = 30f;
        // center.set(hero.center.getX() - 4, hero.center.getY() + 4);
        // } else if (hero.direction == Direction.TOPRIGHT) {
        // rotate = -30f;
        // center.set(hero.center.getX() + 4, hero.center.getY() + 4);
        // } else if (hero.direction == Direction.BOTTOMLEFT) {
        // rotate = 30f;
        // center.set(hero.center.getX() - 4, hero.center.getY() - 4);
        // } else if (hero.direction == Direction.BOTTOMRIGHT) {
        // rotate = -30f;
        // center.set(hero.center.getX() + 4, hero.center.getY() - 4);
        // } else if (hero.direction == Direction.UP) {
        // rotate = 0f;
        // center.set(hero.center.getX(), hero.center.getY() + 4);
        // } else if (hero.direction == Direction.DOWN) {
        // rotate = 0f;
        // center.set(hero.center.getX(), hero.center.getY() - 4);
        // } else if (hero.direction == Direction.LEFT) {
        // rotate = 0f;
        // center.set(hero.center.getX() - 4, hero.center.getY());
        // } else if (hero.direction == Direction.RIGHT) {
        // rotate = 0f;
        // center.set(hero.center.getX() + 4, hero.center.getY());
        // } else if (hero.direction == Direction.NEUTRAL) {
        // rotate = 0f;
        // center.set(hero.center.getX(), hero.center.getY());
        // }

        Offset offset = offsets.get(hero.directionState);
        rotate = offset.rotation;
        center = hero.center.clone().add(offset.transform);
    }
}