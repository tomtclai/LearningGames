package dyehard.Weapons;

import dyehard.Configuration;
import dyehard.Player.Hero;

public class LimitedAmmoWeapon extends Weapon {
    protected final int reloadAmount = Configuration.limitedReloadAmount;
    protected final int maxAmmo = Configuration.limitedMaxAmmo;
    protected int currentAmmo;

    public LimitedAmmoWeapon(Hero hero) {
        super(hero);
        timer.setInterval(Configuration.limitedFiringRate);
        currentAmmo = maxAmmo;
    }

    public void reload() {
        currentAmmo += reloadAmount;

        if (currentAmmo > maxAmmo) {
            currentAmmo = maxAmmo;
        }
    }

    @Override
    public void fire() {
        if (timer.isDone() && currentAmmo > 0) {
            super.fire();
            currentAmmo--;
        }
    }

    @Override
    public int currentValue() {
        return currentAmmo;
    }

    @Override
    public int totalValue() {
        return maxAmmo;
    }

    @Override
    public String toString() {
        return "Limited Ammo " + currentAmmo + "/" + maxAmmo;
    }
}
