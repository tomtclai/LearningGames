
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class OverHeatWeapon : Weapon
    {
        private const float heatLimit  = 10.0f;
        private const float cooldownRate = 0.05f;
        private float currentHeatLevel;
        private bool overheated;
        private XNACS1Rectangle tempTracker;


        public OverHeatWeapon(Hero hero)
            : base(hero)
        {
            overheated = false;
            currentHeatLevel = 0;
            tempTracker = new XNACS1Rectangle(new Vector2(GameWorld.leftEdge + 4, GameWorld.topEdge - 4), 4, 4);
        }

        public override void remove()
        {
            tempTracker.RemoveFromAutoDrawSet();
        }

        public override void update()
        {

            if (currentHeatLevel > heatLimit)
            {
                overheated = true;
                tempTracker.Color = Color.Red;
            }

            if (currentHeatLevel >= 0)
            {
                currentHeatLevel = currentHeatLevel - cooldownRate;
            }

            if (currentHeatLevel <= 0)
            {
                tempTracker.Color = Color.Green;
                overheated = false;
            }

            base.update();   
        }

        public override void draw() {
            tempTracker.Label = currentHeatLevel.ToString("0.0");
            tempTracker.TopOfAutoDrawSet();
            base.draw();
        }

        public override void fire()
        {
            if (!overheated)
            {
                base.fire();
                currentHeatLevel += 1;
            }
        }
    }
}
