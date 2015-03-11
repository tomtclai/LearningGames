using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Hero : Character
    {
        private const float horizontalSpeedLimit = 0.8f;
        private static float drag = 0.96f;  // smaller number means more reduction
        private const float rightBoundaryLimit = 0.85f; // percentage of screen

        private Weapon weapon;
        private List<Obstacle> boundaries;
        private List<Weapon> weaponRack;
        private int collectedDyepacks;
        private int collectedPowerups;

        private float speedFactor;
        private bool invisible;

        public Hero()
            : base(new Vector2(GameWorld.rightEdge / 3, GameWorld.topEdge / 2), 5f, 5f)
        {
            base.setLabel("Dye");

            weaponRack = new List<Weapon>();
            createWeapons();
            weapon = weaponRack.First(); // set initial weapon to first

            collectedDyepacks = 0;
            collectedPowerups = 0;

            boundaries = new List<Obstacle>();
            setBoundaries();

            // initialize powerup variables
            speedFactor = 1.0f;
            invisible = false;
        }

        public override void remove()
        {
            foreach (Obstacle b in boundaries)
            {
                b.remove();
            }

            foreach (Weapon w in weaponRack)
            {
                w.remove();
            }

            base.remove();
        }

        public override void update()
        {
            // restrict the hero's movement to the boundary
            bool holdVisibility = invisible;
            invisible = false;
            foreach (Obstacle b in boundaries)
            {
                b.checkCollisions();
            }
            // update base character object (collisions, etc.)
            base.update();
            invisible = holdVisibility;

            // update hero's weapons
            selectWeapon();

            if (KeyboardDevice.isKeyTapped(Microsoft.Xna.Framework.Input.Keys.F))
            {
                weapon.fire();
            }

            foreach (Weapon w in weaponRack)
            {
                w.update();
            }
        }


        public void collect(DyePack dye)
        {
            dye.activate();
            collectedDyepacks += 1;

            if (weapon.GetType() == typeof(LimitedAmmoWeapon))
            {
                ((LimitedAmmoWeapon)weapon).recharge();
            }
        }

        public void collect(PowerUp powerup)
        {
            powerup.activate();
            collectedPowerups += 1;
        }

        public override void draw()
        {
            weapon.draw();
            base.draw();
        }

        public int dyepacksCollected()
        {
            return collectedDyepacks;
        }

        public int powerupsCollected()
        {
            return collectedPowerups;
        }

        public void push(Vector2 direction)
        {
            // scale direction
            direction = (direction / 12f) * speedFactor;

            // add 'jetpack' factor
            if (direction.Y > 0)
            {
                direction.Y *= 1.7f;
            }

            // update velocity
            position.Velocity = (position.Velocity + direction + (GameWorld.Gravity)) * drag;

            if (position.VelocityX < 0)
            {
                position.VelocityX = Math.Max(position.VelocityX, -1 * horizontalSpeedLimit);
            }
            else
            {
                position.VelocityX = Math.Min(position.VelocityX, horizontalSpeedLimit);
            }
        }

        public void setEnemies(List<Enemy> enemies)
        {
            foreach (Weapon w in weaponRack)
            {
                w.setEnemies(enemies);
            }
        }


        //
        // powerup functions
        //

        public void increaseSpeed()
        {
            Console.WriteLine("speed increased");
            speedFactor = 1.5f;
        }

        public void normalizeSpeed()
        {
            Console.WriteLine("speed normalized");
            speedFactor = 1.0f;
        }

        public void setInvisible()
        {
            Console.WriteLine("invisible");
            invisible = true;
        }

        public void setVisible()
        {
            Console.WriteLine("visible");
            invisible = false;
        }

        public override XNACS1Rectangle getNextPosition()
        {
            if (invisible)
            {
                XNACS1Rectangle dummy = new XNACS1Rectangle(new Vector2(50, -200), 0, 0);
                dummy.RemoveFromAutoDrawSet();
                return dummy;
            } else {
                return base.getNextPosition();
            }
        }

        private void setBoundaries()
        {
            // determine the maximum horizontal boundary for the hero
            List<Enemy> emptyList = new List<Enemy>();
            float width = (1 - rightBoundaryLimit) * GameWorld.rightEdge;
            float boundaryX = GameWorld.rightEdge - (width / 2);
            float boundaryY = GameWorld.topEdge / 2;
            Obstacle boundary = new Obstacle(this, emptyList, new Vector2(boundaryX, boundaryY), width, GameWorld.topEdge);
            boundaries.Add(boundary);

            // determine minumum vertical and horizontal, and maximum vertical boundaries for hero
            float screenCenterX = (GameWorld.rightEdge - GameWorld.leftEdge) / 2;
            float screenCenterY = (GameWorld.topEdge - GameWorld.bottomEdge) / 2;
            boundary = new Obstacle(this, emptyList, new Vector2(screenCenterX, GameWorld.bottomEdge - screenCenterY), screenCenterX * 2, screenCenterY * 2);
            boundaries.Add(boundary);
            boundary = new Obstacle(this, emptyList, new Vector2(screenCenterX, GameWorld.topEdge + screenCenterY), screenCenterX * 2, screenCenterY * 2);
            boundaries.Add(boundary);
            boundary = new Obstacle(this, emptyList, new Vector2(GameWorld.leftEdge - screenCenterX, screenCenterY), screenCenterX * 2, screenCenterY * 2);
            boundaries.Add(boundary);
        }

        private void selectWeapon()
        {
            if (KeyboardDevice.isKeyTapped(Microsoft.Xna.Framework.Input.Keys.D1))
            {
                weapon = weaponRack[0];
            }
            else if (KeyboardDevice.isKeyTapped(Microsoft.Xna.Framework.Input.Keys.D2))
            {
                weapon = weaponRack[1];
            }
            else if (KeyboardDevice.isKeyTapped(Microsoft.Xna.Framework.Input.Keys.D3))
            {
                weapon = weaponRack[2];
            }
            else if (KeyboardDevice.isKeyTapped(Microsoft.Xna.Framework.Input.Keys.D4))
            {
                weapon = weaponRack[3];
            }
            if (KeyboardDevice.isKeyTapped(Microsoft.Xna.Framework.Input.Keys.D5))
            {
                weapon = weaponRack[4];
            }
        }

        private void createWeapons()
        {
            weaponRack.Add(new Weapon(this));
            weaponRack.Add(new OverHeatWeapon(this));
            weaponRack.Add(new LimitedAmmoWeapon(this));
            weaponRack.Add(new SpreadFireWeapon(this));
            weaponRack.Add(new Weapon2(this));
        }

    }
}
