package dyehard.Collectibles;

import java.awt.Color;

import Engine.Text;
import Engine.Vector2;
import dyehard.Collidable;
import dyehard.Configuration;
import dyehard.Player.Hero;
import dyehard.Util.DyeHardSound;
import dyehard.Util.Timer;

public abstract class PowerUp extends Collidable implements Cloneable,
        Comparable<PowerUp> {
    public static final float width = Configuration.powerUpWidth;
    public static final float height = Configuration.powerUpHeight;

    protected int applicationOrder;
    protected float duration = 5000f;
    protected Timer timer;
    protected Text label;

    public PowerUp() {
        shouldTravel = false;
        visible = false;
        applicationOrder = 0;
        timer = new Timer(duration);

        label = new Text("", center.getX(), center.getY());
        label.setFrontColor(Color.WHITE);
        label.setBackColor(Color.BLACK);
        label.setFontSize(20);
        label.setFontName("Arial");
    }

    public PowerUp(PowerUp other) {
        texture = other.texture;
    }

    public void initialize(Vector2 center) {
        timer.setInterval(duration);

        this.center = center;
        velocity = new Vector2(-Configuration.powerUpSpeed, 0f);

        size.set(width, height);
        shouldTravel = true;
        visible = true;
    }

    @Override
    public void update() {
        super.update();
        // label.center.set(center.getX() - 2.5f, center.getY() - 0.5f);
    }

    @Override
    public void destroy() {
        label.destroy();
        super.destroy();
    }

    public float getDuration() {
        return duration;
    }

    public float getRemainingTime() {
        return timer.timeRemaining();
    }

    public boolean isDone() {
        return timer.isDone();
    }

    public abstract void apply(Hero hero);

    public abstract void unapply(Hero hero);

    public void activate(Hero hero) {
        timer.reset();
        System.out.println("Activating " + toString());
        destroy();
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Hero) {
            Hero hero = (Hero) other;
            hero.collect(this);
            DyeHardSound.play(DyeHardSound.powerUpSound);
        }
    }

    @Override
    public abstract PowerUp clone();

    @Override
    public int compareTo(PowerUp other) {
        return applicationOrder - other.applicationOrder;
    }

    @Override
    public String toString() {
        return String.format("%.0f", timer.timeRemaining() / 1000);
    }
}
