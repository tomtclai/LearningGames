using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Screen
    {
        private XNACS1Rectangle screen;

        public Screen(string label)
        {
            float width = XNACS1Base.World.WorldMax.X;
            float height = XNACS1Base.World.WorldMax.Y;
            Vector2 center = new Vector2(width/2, height/2);
            this.screen = new XNACS1Rectangle(center, width, height);

            this.screen.Texture = "StartScreen_Background";
            this.screen.LabelColor = Color.White;
            this.screen.Label = label;
        }

        public void draw()
        {
            screen.AddToAutoDrawSet();
            screen.TopOfAutoDrawSet();
        }

        public void remove()
        {
            screen.RemoveFromAutoDrawSet();
        }
    }
}
