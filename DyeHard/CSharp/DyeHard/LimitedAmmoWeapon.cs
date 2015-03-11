
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class LimitedAmmoWeapon : Weapon
    {
        private int reloadAmount = 10;
        private int ammo;
        private XNACS1Rectangle ammoTracker;

        public LimitedAmmoWeapon(Hero hero)
            : base(hero)
        {
            ammo = 10;
            ammoTracker = new XNACS1Rectangle(new Vector2(GameWorld.leftEdge + 8, GameWorld.topEdge - 4), 4, 4);
        }

        public override void remove()
        {
            ammoTracker.RemoveFromAutoDrawSet();
        }

        public void recharge()
        {
            ammo = reloadAmount;
        }

        public override void update()
        {
            if (ammo == 0)
            {
                ammoTracker.Color = Color.Red;
            }
            else
            {
                ammoTracker.Color = Color.Green;

            }

            base.update();
        }

        public override void fire()
        {
            if (ammo > 0)
            {
                base.fire();
                ammo--;
            }
        }

        public override void draw()
        {
            ammoTracker.Label = ammo.ToString();
            ammoTracker.TopOfAutoDrawSet();
            base.draw();
        }
    }
}
