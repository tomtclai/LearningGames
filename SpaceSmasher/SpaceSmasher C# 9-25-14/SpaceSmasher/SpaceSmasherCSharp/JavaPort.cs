#region Reference to system libraries
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

using Microsoft.Xna.Framework.Storage;
#endregion
using SpaceSmasherLib;
using SpaceSmasherLib.Entities;

namespace BlockBreakerGame
{
    class JavaPort : SpaceSmasherBase
    {
        protected override void initialize()
        {

            lifeSet.add(5);
            paddleSet.add(1);
            ballSet.add(1);
            ballSet.get(0).Visible = false;
            blockSet.setBlocksPerRow(7);

            blockSet.addNormalBlock(7);
            blockSet.addFireBlock(7);
            blockSet.addFreezingBlock(7);
            blockSet.addActiveCageBlock(7);

            for (int iRow = 0; iRow < blockSet.getNumRows(); iRow++)
                for (int iCol = 0; iCol < blockSet.getNumColumns(); iCol++)
                {
                    Block block = blockSet.getBlockAt(iRow, iCol);
                    if (block != null)
                        block.revealPower();
                }

            // setDebugMode(true);
            // setAllowDebug(false);
        }

        protected override void update()
        {
            Paddle paddle = paddleSet.get(0);
            Ball ball = ballSet.get(0);

            if (blockSet.allBlocksAreDead())
            {
                gameWin();
            }
            else
            {
                // Spawn ball
                if (keyboard.isKeyTapped(Keys.Space) ||
                        (MouseDevice.MouseOnScreen() && MouseDevice.IsLeftTapped()))
                {
                    if (ball.Visible == false)
                    {
                        ball.spawn(paddle);
                    }
                }

                if (keyboard.isKeyDown(Keys.Left))
                {
                    paddle.moveLeft();
                }

                if (keyboard.isKeyDown(Keys.Right))
                {
                    paddle.moveRight();
                }
                if (MouseDevice.MouseOnScreen())
                {
                    paddle.CenterX = MouseDevice.getMouseX();
                    paddle.clampAtWorldBounds();
                }

                if (ball != null && ball.Visible)
                {
                    ballWorldCollision(ball);

                    // Paddle and ball collision
                    if (paddle.Collided(ball))
                    {
                        paddle.reflect(ball);
                        ball.playBounceSound();
                    }

                    // Checks to see ball and paddle states are synchronized.
                    if (ball.isBurning())
                    {
                        if (paddle.getState() == Paddle.PaddleState.NORMAL)
                            ball.setType(Ball.BallType.NORMAL);
                        else if (paddle.getState() == Paddle.PaddleState.ICE)
                            ball.setType(Ball.BallType.ICE);
                    }
                    if (ball.isFrozen())
                    {
                        if (paddle.getState() == Paddle.PaddleState.NORMAL)
                            ball.setType(Ball.BallType.NORMAL);
                        else if (paddle.getState() == Paddle.PaddleState.FIRE)
                            ball.setType(Ball.BallType.FIRE);
                    }


                    if (blockSet.isBallCollidingWithABlock(ball))
                    {
                        // Demonstrate 'while' loops:
                        // Walk outwards, removing blocks as we go, 
                        // until we reach a wall or missing block                  
                        Block block = blockSet.getCollidedBlock(ball);
                        block.reflect(ball);
                        //if (block.getType() == Block.BlockType.NORMAL) {
                        int row = block.getRow();
                        int col = block.getColumn();

                        //blockSet.remove(block);
                        block.revealPower();

                        int nextCol = col;
                        while (nextCol + 1 < blockSet.getNumColumns() && block != null && block.getType() == Block.BlockType.NORMAL)
                        {
                            if (block.getType() == Block.BlockType.NORMAL)
                            {
                                blockSet.remove(block);
                            }
                            //block.revealPower();
                            nextCol++;
                            block = blockSet.getBlockAt(row, nextCol);
                        }
                        nextCol = col - 1;
                        while (nextCol >= 0
                                  && block != null
                                  && block.getType() == Block.BlockType.NORMAL)
                        {
                            if (block.getType() == Block.BlockType.NORMAL)
                                // blockSet.remove(block);
                                block.revealPower();
                            nextCol--;
                            block = blockSet.getBlockAt(row, nextCol);
                        }
                        // }

                    }
                }
            }
        }

        /**
         * Check for and react to the ball colliding with the would bounds.
         *
         * @param ball
         *          - The ball to check collision with.
         */
        private void ballWorldCollision(Ball ball)
        {
            BoundCollidedStatus status = ball.collideWorldBound();
            switch (status)
            {
                case BoundCollidedStatus.CollidedTop:
                    {
                        ball.reflectTop();
                        ball.playBounceSound();

                        break;
                    }

                case BoundCollidedStatus.CollideBottom:
                    {
                        ball.Visible = false;
                        ball.playDieSound();

                        lifeSet.remove();

                        if (lifeSet.getCount() < 1)
                        {
                            gameLost();
                        }

                        break;
                    }

                case BoundCollidedStatus.CollidedLeft:
                    {
                        ball.reflectLeft();
                        ball.playBounceSound();

                        break;
                    }

                case BoundCollidedStatus.CollidedRight:
                    {
                        ball.reflectRight();
                        ball.playBounceSound();

                        break;
                    }

                // Catch the case where the ball is inside the
                // world and not hitting any bounds. A warning is
                // given if all cases are not handled.
                default:
                    break;
            }
        }
    }
}
