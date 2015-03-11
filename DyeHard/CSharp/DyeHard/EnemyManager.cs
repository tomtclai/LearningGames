using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;


namespace Dyehard
{
    class EnemyManager : GameObject
    {
        private const float enemyFrequency = 12f;
        private Hero hero;
        private List<Enemy> enemies;
        private Timer newEnemyTimer;

        public EnemyManager(Hero hero)
        {
            this.hero = hero;
            enemies = new List<Enemy>();
            newEnemyTimer = new Timer(enemyFrequency);
        }

        public override void remove()
        {
            foreach (Enemy e in enemies) {
                e.remove();
            }
        }

        public override void update()
        {
            // remove any dead enemies
            foreach (Enemy e in enemies.Where(enemy => !enemy.isAlive()))
            {
                e.remove();
            }
            enemies.RemoveAll(enemy => !enemy.isAlive());


            foreach (Enemy e in enemies)
            {
                e.update();
            }

            // prevent generating enemy
            if (GameWorld.Speed != 0)
            {
                newEnemyTimer.update();
            }
            
            //generate new enemy
            if (newEnemyTimer.isDone())
            {
                float randomY = XNACS1Base.RandomFloat(GameWorld.topEdge - 5, GameWorld.bottomEdge + 5);
                Vector2 position = new Vector2(GameWorld.rightEdge + 5, randomY);
                
                switch (XNACS1Base.RandomInt(0,3))
                {
                    case 1:
                        enemies.Add(new BrainEnemy(position, 7.5f, hero));
                        break;
                    case 2:
                        enemies.Add(new RedBeamEnemy(position, 7.5f, hero));
                        break;
                    default:
                        enemies.Add(new SpiderEnemy(position, 7.5f, hero));
                        break;
                }

                newEnemyTimer.reset();
            }
        }

        public override void draw()
        {
            foreach (Enemy b in enemies)
            {
                b.getPosition().TopOfAutoDrawSet();
            }
        }

        public void killAll()
        {
            foreach (Enemy e in enemies)
            {
                e.remove();
            }

            enemies.Clear();
        }

        public List<Enemy> getEnemies()
        {
            return enemies;
        }
   
    }
}
