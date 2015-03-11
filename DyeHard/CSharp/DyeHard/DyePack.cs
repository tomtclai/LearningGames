using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class DyePack : GameObject
    {
        private const float height = 3.5f;
        protected Hero hero;
        protected XNACS1Rectangle box;

        public DyePack(Hero hero, float minX, float maxX, Color color)
        {
            this.hero = hero;

            float padding = hero.getPosition().Width * 2;

            float randomX = XNACS1Base.RandomFloat(minX + padding, maxX - padding);
            float randomY = XNACS1Base.RandomFloat(GameWorld.bottomEdge + padding, GameWorld.topEdge - padding);
            box = new XNACS1Rectangle(new Vector2(randomX, randomY), 0.865f * height, height);
            box.Color = color;
            box.Texture = getTexture(color);
        }

        public override void remove()
        {
            box.RemoveFromAutoDrawSet();
        }

        public void move()
        {
            box.CenterX -= GameWorld.Speed;
        }

        public override void update()
        {
            if (box.Collided(hero.getPosition()) && box.Visible)
            {
                hero.collect(this);
            }
        }

        public void activate()
        {
            hero.setColor(box.Color);
            box.Visible = false;
        }

        public override void draw()
        {
            box.TopOfAutoDrawSet();
        }

        private static String getTexture(Color color)
        {
            if (color == Game.Green) return "Dye_Green";
            if (color == Game.Blue) return "Dye_Blue";
            if (color == Game.Yellow) return "Dye_Yellow";
            if (color == Game.Teal) return "Dye_Teal";
            if (color == Game.Pink) return "Dye_Pink";
            if (color == Game.Red) return "Dye_Red";
            return "";
        }
    }
}
