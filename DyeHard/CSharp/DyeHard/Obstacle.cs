using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;


namespace Dyehard
{
    class Obstacle : GameObject
    {
        private Hero hero;
        private List<Enemy> enemies;
        private XNACS1Rectangle box;

        public Obstacle(Hero hero, List<Enemy> enemies, Vector2 center, float width, float height)
        {
            this.hero = hero;
            this.enemies = enemies;
            this.box = new XNACS1Rectangle(center, width, height);
            this.box.Color = Color.SlateGray;
        }

        public override void remove()
        {
            box.RemoveFromAutoDrawSet();
        }

        public void move()
        {
            box.CenterX -= GameWorld.Speed;
            checkCollisions();
        }

        public void checkCollisions()
        {
            // let the hero know if it is about to collide with the platform
            if (box.Collided(hero.getNextPosition()))
            {
                hero.addCollision(box);
            }

            foreach (Enemy e in enemies)
            {
                if (box.Collided(e.getNextPosition()))
                {
                    e.addCollision(box);
                }
            }
        }

        public override void draw()
        {
            box.TopOfAutoDrawSet();
        }

        public override void update()
        {
            // do nothing
        }
    }
}
