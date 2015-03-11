using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Window
    {
        private XNACS1Rectangle window;
        public Window(string label)
        {
            float width = XNACS1Base.World.WorldMax.X;
            float height = XNACS1Base.World.WorldMax.Y;
            Vector2 center = new Vector2(width/2, height/2);
            this.window = new XNACS1Rectangle(center, width/3, height/3);

            this.window.Color = new Color(Color.Gray, 50);
            this.window.LabelColor = Color.White;
            this.window.Label = label;
        }

        public void draw()
        {
            window.AddToAutoDrawSet();
            window.TopOfAutoDrawSet();
        }

        public void remove()
        {
            window.RemoveFromAutoDrawSet();
        }
    }
}
