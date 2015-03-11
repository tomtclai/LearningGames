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
using SpaceSmasherLib.Entities;
using XNACS1Lib;

namespace SpaceSmasherLib
{
    internal static class GameObjects
    {
        internal static bool debugMode;
        internal static int score;
        internal static List<Ball> ballList;
        internal static List<Block> blockList;
        internal static List<Trap> trapList;
        internal static List<Life> lifeList;
        internal static List<Switch> switchList;
        internal static List<Paddle> paddleList;

        internal static void displayScore()
        {
            //XNACS1Lib.XNACS1Base.EchoToTopStatus("Score: " + score);
        }

        internal static bool isPlayerDead()
        {
            if (GameObjects.ballList != null)
            {
                foreach (Ball ball in GameObjects.ballList)
                {
                    if ((!ball.isDead()))
                        return false;
                }
                return true;

            }
            return false;
        }

        internal static void pause()
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                    ball.ShouldTravel = false;
            }

            if (blockList != null)
            {
                foreach (Block block in blockList)
                    block.ShouldTravel = false;
            }

            if (trapList != null)
            {
                foreach (Trap wall in trapList)
                    wall.ShouldTravel = false;
            }

            if (lifeList != null)
            {
                foreach (Life life in lifeList)
                    life.ShouldTravel = false;
            }

            if (switchList != null)
            {
                foreach (Switch mSwitch in switchList)
                    mSwitch.ShouldTravel = false;
            }

            if (paddleList != null)
            {
                foreach (Paddle paddle in paddleList)
                    paddle.ShouldTravel = false;
            }
        }

        internal static void resume()
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                    ball.ShouldTravel = true;
            }

            if (blockList != null)
            {
                foreach (Block block in blockList)
                    block.ShouldTravel = true;
            }

            if (trapList != null)
            {
                foreach (Trap wall in trapList)
                    wall.ShouldTravel = true;
            }

            if (lifeList != null)
            {
                foreach (Life life in lifeList)
                    life.ShouldTravel = true;
            }

            if (switchList != null)
            {
                foreach (Switch mSwitch in switchList)
                    mSwitch.ShouldTravel = true;
            }

            if (paddleList != null)
            {
                foreach (Paddle paddle in paddleList)
                    paddle.ShouldTravel = true;
            }
        }

        internal static void update()
        {
            if (ballList != null)
            {
                foreach (Ball ball in ballList)
                    ball.update();
            }

            if (blockList != null)
            {
                foreach (Block block in blockList)
                    block.update();
            }

            if (trapList != null)
            {
                foreach (Trap wall in trapList)
                    wall.update();
            }

            if (lifeList != null)
            {
                foreach (Life life in lifeList)
                    life.update();
            }

            if (switchList != null)
            {
                foreach (Switch mSwitch in switchList)
                    mSwitch.update();
            }

            if (paddleList != null)
            {
                foreach (Paddle paddle in paddleList)
                    paddle.update();
            }
        }

        internal static void destroy()
        {
             if (ballList != null)
            {
                foreach (Ball ball in ballList)
                {
                    ball.remove();
                }
                ballList.Clear();
            }

            if (blockList != null)
            {
                foreach (Block block in blockList)
                {
                    block.remove();
                }
                blockList.Clear();
            }

            if (trapList != null)
            {
                foreach (Trap wall in trapList)
                {
                    wall.remove();
                }
                trapList.Clear();
            }

            if (lifeList != null)
            {
                foreach (Life life in lifeList)
                {
                    life.destroy();
                }
                lifeList.Clear();
            }

            if (switchList != null)
            {
                foreach (Switch mSwitch in switchList)
                {
                    mSwitch.remove();
                }
                switchList.Clear();
            }

            if (paddleList != null)
            {
                foreach (Paddle paddle in paddleList)
                {
                    paddle.remove();
                }
                paddleList.Clear();
            }

            XNACS1Lib.XNACS1Base.World.SetBackgroundTexture("");
            //XNACS1Lib.XNACS1Base.EchoToTopStatus("");
            XNACS1Lib.XNACS1Base.PlayBackgroundAudio("", 0);
        }
    }
}

