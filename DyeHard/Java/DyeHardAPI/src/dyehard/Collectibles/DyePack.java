package dyehard.Collectibles;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.Collidable;
import dyehard.Configuration;
import dyehard.Player.Hero;
import dyehard.Util.Colors;
import dyehard.Util.DyeHardSound;
import dyehard.Util.Timer;

public class DyePack extends Collidable {
    public static final float width = Configuration.dyePackWidth;
    public static final float height = Configuration.dyePackHeight;

    protected Timer t;

    public DyePack(Color color) {
        this.color = color;
        texture = BaseCode.resources.loadImage(getTexture(color));
        shouldTravel = false;
        visible = false;
        t = new Timer();
    }

    public void initialize(Vector2 center) {
        this.center = center;
        velocity = new Vector2(-Configuration.dyePackSpeed, 0f);

        size.set(width, height);
        shouldTravel = true;
        visible = true;
    }

    public void activate(Hero hero) {
        hero.setColor(color);
        visible = false;
    }

    public static String getTexture(Color color) {
        if (color == Colors.Green) {
            return "Textures/Dye_Green.png";
        }
        if (color == Colors.Blue) {
            return "Textures/Dye_Blue.png";
        }
        if (color == Colors.Yellow) {
            return "Textures/Dye_Yellow.png";
        }
        if (color == Colors.Teal) {
            return "Textures/Dye_Teal.png";
        }
        if (color == Colors.Pink) {
            return "Textures/Dye_Pink.png";
        }
        if (color == Colors.Red) {
            return "Textures/Dye_Red.png";
        }
        return "";
    }

    @Override
    public void update() {
        super.update();
        rotate += 60f * t.deltaTime();
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Hero) {
            Hero hero = (Hero) other;
            hero.collect(this);
            DyeHardSound.play(DyeHardSound.pickUpSound);
        }
    }
}
