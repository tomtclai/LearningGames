using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

// A starfield is a collection of stars moving to the left at a given speed.
// Layers of starfields can be used to create a parallax effect.

namespace Dyehard
{
    class Starfield
    {
        private Queue<XNACS1Circle> stars;
        private float speed;
        private float size;

        public Starfield(float size, float speed, float spacing)
        {
            this.size = size;
            this.speed = speed;
            this.stars = new Queue<XNACS1Circle>();

            // fill background
            for (float i = 0; i < Background.width; i += spacing)
            {
                stars.Enqueue(starAt(i));
            }
        }

        public void update()
        {
            // move stars, and redraw
            foreach (XNACS1Circle star in stars)
            {
                star.CenterX -= speed;
                star.TopOfAutoDrawSet();
            }

            // recycle stars to right edge of screen
            if (stars.First().CenterX <= GameWorld.leftEdge)
            {
                XNACS1Circle star = stars.Dequeue();
                star.CenterX = GameWorld.rightEdge;
                star.CenterY = randomPosition();
                stars.Enqueue(star);
            }
        }

        private float randomPosition()
        {
            return XNACS1Base.RandomFloat(Background.height);
        }

        private static Color randomStarColor()
        {
            int r = XNACS1Base.RandomInt(200, 256);
            int g = XNACS1Base.RandomInt(200, 256);
            int b = XNACS1Base.RandomInt(200, 256);
            return new Color(r, g, b);
        }

        private XNACS1Circle starAt(float xPos)
        {
            Vector2 position = new Vector2(xPos, randomPosition());
            XNACS1Circle star = new XNACS1Circle(position, size);
            star.Color = randomStarColor();
            return star;
        }
    }
}
