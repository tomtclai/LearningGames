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
    public class PaddleSet : Set
    {
        internal List<Paddle> paddleList;

        public override void initialize()
        {
            paddleList = new List<Paddle>();
        }

        public override void add()
        {
            Paddle paddle = new Paddle();
            Paddle previous;
            paddleList.Add(paddle);

            int index = paddleList.IndexOf(paddle);

            if (index == 0)
            {
                previous = paddle;
            }
            else
            {
                if (paddleList.Count <= 3)
                {
                    previous = paddleList[0];
                }
                else
                {
                    previous = paddleList[paddleList.Count - 3];
                }

                if (index % 2 == 0)
                {
                    paddle.Center = new Vector2(previous.CenterX + (paddle.Width * 1.2f), previous.CenterY);
                }
                else
                {
                    paddle.Center = new Vector2(previous.CenterX - (paddle.Width * 1.2f), previous.CenterY);
                }
            }
        }

        public  Paddle get()
        {
            return get(0);
        }

        public  Paddle get(int position)
        {
            if (paddleList == null || position < 0 || position >= paddleList.Count)
                return null;

            return paddleList[position];
        }

        public void remove(Paddle paddle)
        {
            if (paddle != null && paddleList != null)
            {
                paddleList.Remove(paddle);
                paddle.destroy();
            }
        }

        public Paddle getLastPaddle()
        {
            Paddle paddle = null;
            if (paddleList != null)
            {
                paddle = paddleList.LastOrDefault();
            }
            return paddle;
        }

        public Paddle getFirstPaddle()
        {
            Paddle paddle = null;
            if (paddleList != null)
            {
                paddle = paddleList.FirstOrDefault();
            }
            return paddle;
        }
    }
}
