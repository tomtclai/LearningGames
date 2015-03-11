package dyehard.Player;

import Engine.Vector2;
import dyehard.DHR;
import dyehard.DyehardRectangle;
import dyehard.UpdateObject;
import dyehard.Weapons.OverHeatWeapon;
import dyehard.Weapons.Weapon;

public class DyeMeter extends UpdateObject {

    public interface Progressable {
        public int currentValue();

        public int totalValue();
    }

    protected Hero hero;

    DyehardRectangle frame; // the frame containing the meter
    DyehardRectangle meter; // the meter that fills up depending on the weapon
    protected static final Vector2 OFFSET = new Vector2(-5.0f, 0f);

    public DyeMeter(Hero hero) {
        this.hero = hero;

        frame = DHR.getScaledAnimation(new Vector2(1920, 1080), new Vector2(72,
                208), 6, 2, "Textures/UI/dye_meter_frame_anim.png");

        meter = DHR.getScaledAnimation(new Vector2(1920, 1080), new Vector2(72,
                208), 20, 2, "Textures/UI/dye_meter_fill_anim.png");
    }

    static int i = 0;

    @Override
    public void update() {
        Weapon weapon = hero.currentWeapon;
        float meterPercent = 1f; // the amount of the meter shown

        if (weapon.totalValue() != 0) {
            meterPercent = (float) weapon.currentValue()
                    / (float) weapon.totalValue();
            meterPercent = Math.min(meterPercent, 1.0f);
            meterPercent = Math.max(meterPercent, 0f);
        }

        if (weapon instanceof OverHeatWeapon && weapon.currentValue() == 0) {
            frame.visible = false;
            meter.visible = false;
        } else {
            frame.visible = true;
            meter.visible = true;
        }

        frame.center = hero.center.clone();
        frame.center.offset(OFFSET);

        meter.center = hero.center.clone();
        meter.center.offset(OFFSET);

        meter.setFrameNumber((int) (meter.getNumFrames() * meterPercent));
    }

    @Override
    public void setSpeed(float v) {
        // TODO Auto-generated method stub

    }
}
