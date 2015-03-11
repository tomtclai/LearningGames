using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Background
    {
        public static float width = XNACS1Base.World.WorldMax.X;
        public static float height = XNACS1Base.World.WorldMax.Y;

        private XNACS1Rectangle world;
        private Starfield foreground;
        private Starfield background;
        private InfinityShip ship;

        public Background()
        {
            Vector2 center = new Vector2(width/2, height/2);
            this.world = new XNACS1Rectangle(center, width, height);

            this.world.Color = Color.Black;

            this.foreground = new Starfield(0.08f, 0.02f, 6f);
            this.background = new Starfield(0.04f, 0.01f, 2f);
            this.ship = new InfinityShip(0.04f);
        }

        public void update() {
            world.TopOfAutoDrawSet();
            background.update();
            foreground.update();
            ship.update();
        }
    }
}
