using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Platform : GameObject
    {
        private const int SEGMENT_COUNT = 30;
        public static float height = 1.25f;
        private const float mask = 0.1f; // overlap between platform segments
        private Hero hero;
        private List<Enemy> enemies;
        private List<Obstacle> obstacles;

        public Platform(int offset, Hero hero, List<Enemy> enemies, float leftEdge, bool continuous = false)
        {
            this.hero = hero;
            this.enemies = enemies;
            this.obstacles = new List<Obstacle>();

            fillPlatform(offset, leftEdge, continuous);
        }

        public override void remove()
        {
            foreach (Obstacle obstacle in obstacles)
            {
                obstacle.remove();
            }
        }

        public void move()
        {
            foreach (Obstacle obstacle in obstacles)
            {
                obstacle.move();
            }
        }

        public override void draw()
        {
            foreach (Obstacle obstacle in obstacles)
            {
                obstacle.draw();
            }
        }

        public override void update()
        {
            foreach (Obstacle obstacle in obstacles)
            {
                obstacle.update();
            }
        }

        private void fillPlatform(int offset, float leftEdge, bool continuous)
        {
            // set up platform
            float Ypos = ((offset * 1f) / Stargate.GATE_COUNT) * GameWorld.topEdge;

            if (continuous)
            {
                float Xpos = leftEdge + (Stargate.width / 2);
                Obstacle obstacle = new Obstacle(hero, enemies, new Vector2(Xpos, Ypos), Stargate.width + mask, height);
                obstacles.Add(obstacle);
            }
            else
            {
                // randomly fill platform
                float width = Stargate.width / SEGMENT_COUNT;
                Obstacle obstacle;
                int consecutiveChance = 10;
                bool platform = true;
                for (int i = 0; i < SEGMENT_COUNT; i++)
                {
                    if (platform)
                    {
                        float Xpos = (width * 0.5f) + leftEdge + (i * width);
                        obstacle = new Obstacle(hero, enemies, new Vector2(Xpos, Ypos), width + mask, height);
                        obstacles.Add(obstacle);
                    }
                    consecutiveChance -= 2;
                    if (XNACS1Base.RandomInt(consecutiveChance) == 0)
                    {
                        platform = !platform;
                        consecutiveChance = 10;
                    }
                }
            }
        }
    }
}
