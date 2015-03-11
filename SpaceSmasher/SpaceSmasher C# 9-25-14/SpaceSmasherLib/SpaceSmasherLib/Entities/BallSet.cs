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
    public  class BallSet: Set
    {
       protected  List<Ball> ballList;

        public override void initialize()
        {
            ballList = new List<Ball>();
        }

        public override void add()
        {
            add(1);
        }

        public  override void  add(int num)
        {
            if (num < 0)
                return;

            for(int i = 0; i < num; i++)
                ballList.Add(new Ball());
        }

        public Ball get()
        {
            return get(0);
        }

        public Ball get(int position)
        {
            if (ballList == null || position < 0 || position >= ballList.Count)
                return null;

            return ballList[position];
        }

        public bool anyBallCollided(Paddle paddle)
        {
            if(paddle != null && ballList != null)
            {
                foreach(Ball ball in ballList)
                {
                    if(ball.Collided(paddle))
                        return true;
                }
            }
            return false;
        }

        public List<Ball> getCollidedListOfBalls(Paddle paddle)
        {
            List<Ball> list = new List<Ball>();
            if (paddle != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(paddle))
                        list.Add(ball);
                }
            }
            return list;
        }

        public  List<Ball> getCollidedListOfBallWithWalls(TrapSet WallSet)
        {
            List<Ball> list = new List<Ball>();
            if (ballList != null && WallSet.wallList != null)
            {
                foreach (Ball ball in ballList)
                {
                    foreach (Trap wall in WallSet.wallList)
                    {
                        if (ball.Collided(wall))
                            list.Add(ball);
                    }
                }
            }
            return list;
        }

        public  bool anyBallCollidedWithWalls(TrapSet WallSet)
        {
            if (ballList != null && WallSet.wallList != null)
            {
                foreach (Ball ball in ballList)
                {
                    foreach (Trap wall in WallSet.wallList)
                    {
                        if (ball.Collided(wall))
                            return true;
                    }
                }
            }
            return false;
        }

        public  List<Ball> getCollidedListOfBallsWithBlocks()
        {
            List<Ball> list = new List<Ball>();
            if (ballList != null && GameObjects.blockList != null)
            {
                foreach (Ball ball in ballList)
                {
                    foreach (Block block in GameObjects.blockList)
                    {
                        if (ball.Collided(block))
                            list.Add(ball);
                    }
                }
            }
            return list;
        }

        public  bool anyBallCollidedWithBlocks()
        {
            if (ballList != null && GameObjects.blockList!= null)
            {
                foreach (Ball ball in ballList)
                {
                    foreach (Block block in GameObjects.blockList)
                    {
                        if (ball.Collided(block))
                            return true;
                    }
                }
            }
            return false;
        }

        public  bool anyBallCollided(Trap wall)
        {
            if (wall != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(wall))
                        return true;
                }
            }
            return false;
        }

        public  List<Ball> getCollidedListOfBalls(Trap wall)
        {
            List<Ball> list = new List<Ball>();
            if (wall != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(wall))
                        list.Add(ball);
                }
            }
            return list;
        }

        public List<Ball> getCollidedListOfBalls(Switch mSwitch)
        {
            List<Ball> list = new List<Ball>();
            if (mSwitch != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(mSwitch))
                        list.Add(ball);
                }
            }
            return list;
        }

        public bool anyBallCollided(Switch mSwitch)
        {
            if (mSwitch != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(mSwitch))
                        return true;
                }
            }
            return false;
        }

        public bool anyBallCollidedWithWorld()
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.collideWorldBound() != BoundCollidedStatus.InsideBound)
                        return true;
                }
            }
            return false;
        }

        public List<Ball> getCollidedListOfBallsWithWorld()
        {
            List<Ball> list = new List<Ball>();

            if ( ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.collideWorldBound() != BoundCollidedStatus.InsideBound)
                        list.Add(ball);
                }
            }
            return list;
        }

        public Ball getCollidedBallWithWorld()
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.collideWorldBound() != BoundCollidedStatus.InsideBound)
                        return ball;
                }
            }
            return null;
        }

        public Ball getCollidedBall(Paddle paddle)
        {
            if (paddle != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(paddle))
                        return ball;
                }
            }
            return null;
        }

        public Ball getCollidedBall(Switch mSwitch)
        {
            if (mSwitch != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(mSwitch))
                        return ball;
                }
            }
            return null;
        }

        public Ball getCollidedBall(Trap wall)
        {
            if (wall != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(wall))
                        return ball;
                }
            }
            return null;
        }

        public Ball getCollidedBallWithBlocks()
        {
            if (GameObjects.blockList != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    foreach(Block block in GameObjects.blockList )
                    {
                        if (ball.Collided(block))
                            return ball;

                    }
                    
                }
            }
            return null;
        }

        public bool allBallsDead()
        {
            if(ballList != null)
            {
                foreach(Ball ball in ballList)
                {
                    if(!ball.isDead())
                        return false;
                }
            }
            return true;
        }

        public bool collided(XNACS1Primitive other)
        {
            if (other != null && ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    if (ball.Collided(other))
                        return true;
                }
            }
            return false;

        }

        public void remove(Ball ball)
        {
            if (ball != null && ballList != null)
            {
                ballList.Remove(ball);
                ball.destroy();
            }
        }

        public float getRadius()
        {
            if (ballList != null)
                return ballList[0].Radius;
            return 0;
        }

        public void setRadius(float value)
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                    ball.Radius = value;
            }
        }

        public string getTexture()
        {
            if (ballList != null)
                return ballList[0].getNormalTexture();
            return "";
        }

        public void setTexture(string value)
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                    ball.setNormalTexture(value);
            }
        }

        public Ball getLastBall()
        {
            Ball ball = null;
            if (ballList != null)
            {
                ball = ballList.LastOrDefault();
            }
            return ball;
        }

        public Ball getFirstBall()
        {
            Ball ball = null;
            if (ballList != null)
            {
                ball = ballList.FirstOrDefault();
            }
            return ball;
        }
    }
}
