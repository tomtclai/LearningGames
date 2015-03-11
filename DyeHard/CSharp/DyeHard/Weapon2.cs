using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Weapon2 : Weapon
    {
        protected Queue<Bullet> bullets2;
        
        

        public Weapon2(Hero hero)
            : base(hero)
        {
            this.hero = hero;
            bullets2 = new Queue<Bullet>();
        }

        public override void remove()
        {
            foreach (Bullet b in bullets2)
            {
              //  b.RemoveFromAutoDrawSet();
            }
        }

        // update bullets, create explosions
        public override void update()
        {
            foreach (Bullet b in bullets2)
            {
                b.update();
            }

            while (bullets.Count > 0 && (bullets2.First().getPosition().CenterX - 
                bullets2.First().getPosition().Radius) > GameWorld.rightEdge)
            {
                //bullets.Dequeue().RemoveFromAutoDrawSet();
            }
        }

        // fire the weapon
        public override void fire()
        {
            Bullet bullet = new Bullet(hero.getPosition().Center, new Vector2(bulletSpeed, 0.0f), bulletSize, hero, enemies);
            bullet.getPosition().Color = hero.getColor();
            bullet.setEnemies(enemies);
            bullets2.Enqueue(bullet);
        }


    }
}
