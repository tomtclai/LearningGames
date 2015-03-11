
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class SpreadFireWeapon : Weapon
    {
        private XNACS1Rectangle info;
        private Queue<XNACS1Circle> angledBullets;

        public SpreadFireWeapon(Hero hero)
            : base(hero)
        {
            angledBullets = new Queue<XNACS1Circle>();

            info = new XNACS1Rectangle(new Vector2(GameWorld.leftEdge + 12, GameWorld.topEdge - 4), 4, 4);
            info.Color = Color.Blue;
        }

        public override void remove()
        {
            info.RemoveFromAutoDrawSet();

            foreach (XNACS1Circle b in angledBullets)
            {
                b.RemoveFromAutoDrawSet();
            }

            base.remove();
        }

        public override void update()
        {
            // we have to maintain our own bullet queue because Weapon class will otherwise
            // just move bullet horizontally

            for (int i = 0; i < angledBullets.Count; i++)
            {
                XNACS1Circle b = angledBullets.ElementAt(i);
                b.CenterX += bulletSpeed;
                if (i % 2 == 0)
                {
                    b.CenterY -= bulletSpeed / 2;
                }
                else
                {
                    b.CenterY += bulletSpeed / 2;

                }
            }

            while (angledBullets.Count > 0 && (angledBullets.First().CenterX - angledBullets.First().Radius) > GameWorld.rightEdge)
            {
                angledBullets.Dequeue().RemoveFromAutoDrawSet();
            }

            foreach (XNACS1Circle b in angledBullets)
            {
                foreach (Enemy e in enemies)
                {
                    if (e.getPosition().Collided(b) && b.Visible)
                    {
                        e.gotShot(b.Color);
                        b.Visible = false;
                        explosions.Add(new Explosion(hero, e));
                    }
                }
            }

            base.update();
        }

        public override void fire()
        {
            base.fire();

            XNACS1Circle bullet = new XNACS1Circle(hero.getPosition().Center, bulletSize);
            bullet.Color = hero.getColor();
            angledBullets.Enqueue(bullet);

            bullet = new XNACS1Circle(hero.getPosition().Center, bulletSize);
            bullet.Color = hero.getColor();
            angledBullets.Enqueue(bullet);
        }

        public override void draw()
        {
            info.Label = "spread";
            info.TopOfAutoDrawSet();

            foreach (XNACS1Circle b in angledBullets)
            {
                b.TopOfAutoDrawSet();
            }

            base.draw();
        }
    }
}
