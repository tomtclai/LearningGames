package dyehard.Weapons;

import dyehard.Player.Hero;

public class BrokenWeapon extends Weapon {

    public BrokenWeapon(Hero hero) {
        super(hero);
    }

    @Override
    public int currentValue() {
        return 0;
    }

    @Override
    public int totalValue() {
        return 1;
    }

    @Override
    public void fire() {
        // Play an audio file to indicate broken weapon
        // The broken weapon does not fire
        return;
    }
}
