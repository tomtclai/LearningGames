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
    public class TrapSet : Set
    {
        internal List<Trap> wallList;

        public override void initialize()
        {
            wallList = new List<Trap>();
        }

        public override void add()
        {
            if (wallList != null)
            {
                Trap wall = new Trap();
                wallList.Add(wall);
                autoPosition(wall);
                setupTextures(wall);
            }
        }

        protected void setupTextures(Trap wall)
        {
            int index = wallList.IndexOf(wall);

            if (index == 0)
            {
                wall.setTrapTextures(Trap.ScreenSide.Left);
            }
            else if (index == 1)
            {
                wall.setTrapTextures(Trap.ScreenSide.Right);
            }
        }

        protected void autoPosition(Trap wall)
        {
            if (wallList != null)
            {
                Vector2 leftCenter = new Vector2((getWidth() * 0.6f ), XNACS1Base.World.WorldDimension.Y * 0.6f);
                Vector2 rightCenter = new Vector2(XNACS1Base.World.WorldDimension.X - (getWidth() * 0.6f),
                    XNACS1Base.World.WorldDimension.Y * 0.6f);

                int index = wallList.IndexOf(wall);

                if (index == 0)
                {
                    wallList[index].CenterX = leftCenter.X;
                    wallList[index].CenterY = leftCenter.Y;

                    leftCenter.X += getWidth() * 1.15f;
                }
                else if (index == 1)
                {
                    wallList[index].CenterX = rightCenter.X;
                    wallList[index].CenterY = rightCenter.Y;
                    rightCenter.X += getWidth() * 1.15f;
                }
                else
                {
                    if (GameObjects.blockList != null)
                    {
                        Random random = new Random();
                        int randomNum = random.Next(0, GameObjects.blockList.Count);
                        Block block = GameObjects.blockList[randomNum];

                        wall.Center = block.Center;
                        block.destroy();
                        GameObjects.blockList.Remove(block);
                    }
                }
            }
        }

        public Trap getWall()
        {
            return getWall(0);
        }

        public Trap getWall(int position)
        {
            if (wallList == null || position < 0 || position >= wallList.Count)
                return null;

            return wallList[position];
        }

        public void activate()
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                    wall.activate();
            }
        }

        public void deactivate()
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                    wall.deActivate();
            }
        }

        public bool collided(XNACS1Primitive other)
        {
            if (other != null && wallList != null)
            {
                foreach (Trap wall in wallList)
                {
                    if (wall.Collided(other))
                        return true;
                }
            }
            return false;

        }

        public void remove(Trap trap)
        {
            if (trap != null && wallList != null)
            {
                wallList.Remove(trap);
                trap.destroy();
            }
        }

        public bool isAnyWallActive()
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                {
                    if (wall.isActive())
                        return true;
                }
            }
            return false;
        }

        public bool isAnyWallInactive()
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                {
                    if (wall.isInactive())
                        return true;
                }
            }
            return false;
        }

        public float getWidth()
        {
            if (wallList != null)
                return wallList[0].Width;
            return 0;
        }

        public void setWidth(float value)
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                    wall.Width = value;
            }
        }

        public float getHeight()
        {
            if (wallList != null)
                return wallList[0].Height;
            return 0;
        }

        public void setHeight(float value)
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                    wall.Height = value;
            }
        }

        public string getTexture()
        {
            if (wallList != null)
                return wallList[0].getNormalTexture();
            return "";
        }

        public void setTexture(string value)
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                    wall.setInativeTexture(value);
            }
        }

        public string getActiveTexture()
        {
            if (wallList != null)
                return wallList[0].getActiveTexture();
            return "";
        }

        public void setActiveTexture(string value)
        {
            if (wallList != null)
            {
                foreach (Trap wall in wallList)
                    wall.setActiveTexture(value);
            }
        }

        public Trap getLeft()
        {
            if (wallList != null)
                return wallList[0];

            return null;
        }

        public void setLeft(Trap value)
        {
            if(wallList != null)
                wallList[0] = value;
        }

        public Trap getRight()
        {
            if (wallList != null)
                return wallList[1];

            return null;
        }

        public void setRight(Trap value)
        {
            if (wallList != null)
                wallList[1] = value;
        }

        public Trap getLastTrap()
        {
            Trap trap = null;
            if (wallList != null)
            {
                trap = wallList.LastOrDefault();
            }
            return trap;
        }

        public Trap getFirstTrap()
        {
            Trap trap = null;
            if (wallList != null)
            {
                trap = wallList.FirstOrDefault();
            }
            return trap;
        }
    }
}
