using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;


namespace Dyehard
{
    class Debris : GameObject
    {
        private static float size = 6f;
        private Obstacle obstacle;
        private XNACS1Rectangle texture;

        public Debris(Hero hero, List<Enemy> enemies, float minX, float maxX)
        {
            float padding = size;

            float randomX = XNACS1Base.RandomFloat(minX + padding, maxX - padding);
            float randomY = XNACS1Base.RandomFloat(GameWorld.bottomEdge + padding, GameWorld.topEdge - padding);
            texture = new XNACS1Rectangle(new Vector2(randomX, randomY), size, size);

            switch (XNACS1Base.RandomInt(3)) {
                case 0:
                    texture.Texture = "Beak";
                    break;

                case 1:
                    texture.Texture = "Window";
                    break;

                case 2:
                    texture.Texture = "Wing2";
                    texture.Width = texture.Height * 1.8f;
                    break;
            }

            obstacle = new Obstacle(hero, enemies, texture.Center, texture.Width * .9f, texture.Height * .9f);
        }

        public override void remove()
        {
            obstacle.remove();
            texture.RemoveFromAutoDrawSet();
        }

        public void move()
        {
            texture.CenterX -= GameWorld.Speed;
            obstacle.move();
        }

        public override void draw()
        {
            texture.TopOfAutoDrawSet();
        }

        public override void update()
        {
            obstacle.update();
        }   
    }
}
