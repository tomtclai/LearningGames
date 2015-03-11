using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Space : GameWorldRegion
    {
        public static float width = GameWorld.rightEdge * 3f;
        public static int powerupCount = 5;
        public static int dyepackCount = 11;
        public static int debrisCount = 10;

        private XNACS1Rectangle space;
        private Hero hero;
        private List<PowerUp> powerups;
        private List<DyePack> dyepacks;
        private List<Debris> debris;
        private Trail trail;

        public Space(Hero hero, List<Enemy> enemies, float leftEdge) : base()
        {
            this.hero = hero;

            powerups = new List<PowerUp>();
            dyepacks = new List<DyePack>();
            debris = new List<Debris>();
            trail = new Trail(hero);

            float height = GameWorld.topEdge;
            float position = (width * 0.5f) + leftEdge;

            space = new XNACS1Rectangle(new Vector2(position, height/2), width, height);
            space.Visible = false;

            // add dyepacks to space region
            List<Color> colors = Game.randomColorSet(Game.colorCount);
            float rightEdge = space.CenterX + space.Width / 2;
            float region = (rightEdge - leftEdge) / dyepackCount;
            for (int i = 0; i < dyepackCount; i++)
            {
                float regionLeft = leftEdge + (i * region);
                float regionRight = regionLeft + region;
                dyepacks.Add(new DyePack(hero, regionLeft, regionRight, colors[i % Game.colorCount]));
            }

            region = (rightEdge - leftEdge) / powerupCount;
            for (int i = 0; i < powerupCount; i++)
            {
                float regionLeft = leftEdge + (i * region);
                float regionRight = regionLeft + region;
                powerups.Add(PowerUp.randomPowerUp(hero, regionLeft, regionRight));
            }

            // offset the region to pad the space before the next element
            // this makes the region slightly smaller than it actually should be otherwise
            int offset = 1;
            region = (rightEdge - leftEdge) / (debrisCount + offset);
            for (int i = 0; i < debrisCount; i++) {
                float regionLeft = leftEdge + (i * region);
                float regionRight = regionLeft + region;
                debris.Add(new Debris(hero, enemies, regionLeft, regionRight));
            }
        }


        public override void remove() {
            space.RemoveFromAutoDrawSet();
            trail.remove();

            foreach (PowerUp p in powerups)
            {
                p.remove();
            }

            foreach (DyePack p in dyepacks)
            {
                p.remove();
            }

            foreach (Debris d in debris)
            {
                d.remove();
            }
        }


        public override void move()
        {
            space.CenterX -= GameWorld.Speed;

            trail.move();

            foreach (DyePack p in dyepacks)
            {
                p.move();
            }

            foreach (PowerUp p in powerups)
            {
                p.move();
            }

            foreach (Debris d in debris)
            {
                d.move();
            }
        }

        public override void update()
        {
            foreach (DyePack p in dyepacks)
            {
                p.update();
            }

            foreach (PowerUp p in powerups)
            {
                p.update();
            }

            foreach (Debris d in debris)
            {
                d.update();
            }

            if (contains(hero.getPosition()))
            {
                trail.update();
            }
        }

        public override void draw()
        {
            space.TopOfAutoDrawSet();

            if (contains(hero.getPosition()))
            {
                trail.draw();
            }

            foreach (Debris d in debris)
            {
                d.draw();
            }

            foreach (DyePack p in dyepacks)
            {
                p.draw();
            }

            foreach (PowerUp p in powerups)
            {
                p.draw();
            }
        }

        private bool contains(XNACS1Rectangle other)
        {
            float leftEdge = space.LowerLeft.X;
            float rightEdge = leftEdge + space.Width;

            float otherLeftEdge = other.MinBound.X;
            float otherRightEdge = otherLeftEdge + other.Width;

            return otherLeftEdge > leftEdge && otherRightEdge < rightEdge;
        }

        public override bool isOffScreen()
        {
            return space.CenterX + space.Width / 2 <= 0;
        }

        public override float rightEdge()
        {
            return space.CenterX + space.Width / 2;
        }

        private float leftEdge()
        {
            return space.CenterX + space.Width / 2;
        }
    }
}
