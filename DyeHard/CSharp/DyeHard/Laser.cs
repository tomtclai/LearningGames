using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;


namespace Dyehard
{
    class Laser : GameObject
    {
        private Hero hero;
        private XNACS1Rectangle laserbeam;
        public Laser(Hero hero)
        {
            this.hero = hero;
            float height = GameWorld.topEdge - GameWorld.bottomEdge;
            float width  = 1.5f;
            float padding = 1f;
            laserbeam = new XNACS1Rectangle(new Vector2(width / 2 + padding, height / 2), width, height);
            laserbeam.Color = new Color(Color.LightSkyBlue, 100);
        }

        public override void draw()
        {
            laserbeam.TopOfAutoDrawSet();
        }

        public override void remove()
        {
            laserbeam.RemoveFromAutoDrawSet();
        }

        public override void update()
        {
            if (hero.getPosition().Collided(laserbeam))
            {
                hero.kill();
            }
        }

    }
}
