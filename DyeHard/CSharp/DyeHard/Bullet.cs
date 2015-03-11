using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Bullet
    {
        private bool alive;
        protected XNACS1Circle position;
        protected List<Enemy> enemies;
        protected Hero hero;
        protected Vector2 speed;
        protected List<Explosion> explosions;

        public Bullet(Vector2 genaratePosition, Vector2 currentSpeed, float radius, Hero currentHero, List<Enemy> currentEnemy)
        {
            this.position = new XNACS1Circle(genaratePosition, radius);
            this.position.Color = Game.randomColor();
            explosions = new List<Explosion>();
            // set object into motion;

            this.alive = true;
            this.hero = currentHero;
            this.enemies = currentEnemy;
            this.speed = currentSpeed;
        }

        public  void update()
        {
            position.CenterX = position.CenterX + speed.X;
            position.CenterY = position.CenterY + speed.Y;
                position.TopOfAutoDrawSet();

                foreach (Enemy e in enemies)
                {
                    if (e.getPosition().Collided(position) && position.Visible)
                    {
                        e.gotShot(position.Color);
                        position.Visible = false;
                        explosions.Add(new Explosion(hero, e));
                    }
                }

                foreach (Explosion e in explosions)
                {
                    e.update();
                    e.interactEnemy(enemies);
                }

                explosions.RemoveAll(explosion => explosion.isDone()); 

            //if bullet is out off screen or some situation
            //bullet . alive = false
            // let bullet manager remove it
        }

        public XNACS1Circle getPosition()
        {
            return position;
        }

        public void setEnemies(List<Enemy> enemies)
        {
            this.enemies = enemies;
        }
    }
}
