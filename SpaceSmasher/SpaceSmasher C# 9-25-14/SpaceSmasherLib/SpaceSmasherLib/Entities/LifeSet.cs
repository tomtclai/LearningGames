using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using XNACS1Lib;
using XNACS1Lib.XNACS1Lib;

namespace SpaceSmasherLib.Entities
{
    public class LifeSet : Set
    {
        internal List<Life> lifeList;
        private int numberOfLives = 0;

        public override void initialize()
        {
            lifeList = new List<Life>();
        }

        public override void add()
        {
            add(1);
        }

        public override void add(int num)
        {
            if (num < 0 || lifeList == null)
                return;

            float height;
            float width;
            float x;
            float y;
            if (lifeList.Any())
            {
                Life life = lifeList[lifeList.Count - 1];
                height = life.Height;
                width = life.Width;
                x = life.CenterX  - (width * 1.5f);
                y = life.CenterY;
            }
            else
            {
                width = XNACS1Base.World.WorldDimension.X * 0.02f;
                height = width * 1.5f;
                x = XNACS1Base.World.WorldDimension.X - width * 2.9f;
                y = XNACS1Base.World.WorldDimension.Y - width * 1.55f;
            }
            for (int i = 0; i < num; i++)
            {
                lifeList.Add(new Life(x, y, width, height));
                x -= width * 1.5f;
            }
            numberOfLives += num;
        }

        public int getCount()
        {
            return numberOfLives;
        }

        public Life getLife()
        {
            return getLife(numberOfLives - 1);
        }

        public Life getLife(int index)
        {
            Life life = null;

            if (lifeList != null && index < numberOfLives && index >= 0)
                life = lifeList[index];

            return life;
        }

        public void remove()
        {
            if ( lifeList != null && numberOfLives > 0)
            {
                Life life = getLife();
                life.remove();
                numberOfLives--;
            }
            
        }

        public void Remove(Life life)
        {
            if (life != null && numberOfLives > 0)
            {
                life.remove();
            }
            numberOfLives--;
        }

        public Life getLastLife()
        {
            Life life = null;
            if (lifeList != null)
            {
                life = lifeList.LastOrDefault();
            }
            return life;
        }

        public Life getFirstLife()
        {
            Life life = null;
            if (lifeList != null)
            {
                life = lifeList.FirstOrDefault();
            }
            return life;
        }
    }
}
