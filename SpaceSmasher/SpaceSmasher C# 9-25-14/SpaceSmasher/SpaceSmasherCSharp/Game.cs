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
using XNACS1Lib;
using SpaceSmasherLib;
using SpaceSmasherLib.Entities;

// Currently the ball can get stuck inside the bricks or wall and this can be annoying.
namespace BlockBreakerGame
{
    public class Game : SpaceSmasherBase
    {
        protected override void initialize()
        {
            lifeSet.add(5);
            paddleSet.add(1);
            ballSet.add(1);
            ballSet.get(0).Visible = false;
            blockSet.setBlocksPerRow(7);

            blockSet.addNormalBlock(1);
            blockSet.addFireBlock(1);
            blockSet.addNormalBlock(1);
            blockSet.addFreezingBlock(1);
            blockSet.addNormalBlock(3);

            blockSet.addNormalBlock(7);

            blockSet.addNormalBlock(7);

            blockSet.addActiveCageBlock(7);

            trapSet.add(2);
            //setDebugMode(true);

            theSwitch.activate();
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
                if (keyboard.isKeyTapped(Keys.Space))
                {
                    ball.spawn();
                }

                paddleMovement(paddle);

                if (ball != null && ball.Visible)
                {
                    ballWorldCollision(ball);

                    // Paddle and ball collision
                    if (paddle.Collided(ball))
                    {
                        paddle.reflect(ball);
                        ball.playBounceSound();
                    }

                    ballTrapCollision(trapSet.getLeft(), ball);
                    ballTrapCollision(trapSet.getRight(), ball);

                    // Switch and ball collision
                    if (theSwitch.Collided(ball))
                    {
                        if (theSwitch.isActive() && !trapSet.isAnyWallActive())
                        {
                            theSwitch.deactivate();
                            trapSet.activate();
                            blockSet.deactivateUnbreakableBlocks();
                        }
                        theSwitch.reflect(ball);
                        ball.playBounceSound();
                    }

                    if (blockSet.isBallCollidingWithABlock(ball))
                    {
                        if (ball.isNormal())
                        {
                            normalBallBlockCollision(ball);
                        }
                        else if (ball.isBurning())
                        {
                            burningBallBlockCollision(ball);
                        }
                        else if (ball.isFrozen())
                        {
                            freezingBallBlockCollision(ball);
                        }
                    }
                }
            }
        }

        private void ballWorldCollision(Ball ball)
        {
            BoundCollidedStatus status = ball.collideWorldBound();

            switch (status)
            {
                case BoundCollidedStatus.CollidedTop:
                    ball.reflectTop();
                    ball.playBounceSound();
                    break;

                case BoundCollidedStatus.CollideBottom:
                    ball.Visible = false;
                    ball.playDieSound();
                    lifeSet.remove();
                    if (lifeSet.getCount() < 1)
                        gameLost();
                    break;

                case BoundCollidedStatus.CollidedLeft:
                    ball.reflectLeft();
                    ball.playBounceSound();
                    break;

                case BoundCollidedStatus.CollidedRight:
                    ball.reflectRight();
                    ball.playBounceSound();
                    break;

                default:
                    break;
            }
        }

        private void paddleMovement(Paddle paddle)
        {
            if (keyboard.isKeyDown(Keys.Left))
            {
                paddle.moveLeft();
            }
            else if (keyboard.isKeyDown(Keys.Right))
            {
                paddle.moveRight();
            }
        }

        private void ballTrapCollision(Trap trap, Ball ball)
        {
            if (trap != null && trap.Collided(ball))
            {
                if (trapSet.isAnyWallActive())
                {
                    trapSet.deactivate();
                    blockSet.activateUnbreakableBlocks();
                    theSwitch.activate();
                }
                trap.reflect(ball);
                ball.playBounceSound();
            }
        }

        private void normalBallBlockCollision(Ball ball)
        {
            Block block = blockSet.getCollidedBlock(ball);

            switch (block.getType())
            {
                case Block.BlockType.NORMAL:
                case Block.BlockType.CAGE_ACTIVE:
                    block.reflect(ball);
                    ball.playBounceSound();
                    blockSet.remove(block);
                    break;

                case Block.BlockType.FREEZING:
                    block.reflect(ball);
                    ball.playBounceSound();

                    if (block.isPowerRevealed())
                    {
                        ball.freezeTheBall();
                        blockSet.remove(block);
                    }
                    else
                    {
                        block.revealPower();
                    }

                    break;

                case Block.BlockType.FIRE:
                    block.reflect(ball);
                    ball.playBounceSound();

                    if (block.isPowerRevealed())
                    {
                        ball.burnTheBall();
                        blockSet.remove(block);
                        paddleSet.getFirstPaddle().startFire();
                    }
                    else
                    {
                        block.revealPower();
                    }

                    break;

                case Block.BlockType.JOKER:
                    //ballHitJoker(ball, block);
                    break;

                case Block.BlockType.CAGE_INACTIVE:
                    block.reflect(ball);
                    ball.playBounceSound();
                    break;

                // This is here to match the
                // Java code to catch the empty block case.
                default:
                    break;
            }
        }

        private void burningBallBlockCollision(Ball ball)
        {
            Block block = blockSet.getCollidedBlock(ball);

            switch (block.getType())
            {
                case Block.BlockType.NORMAL:
                case Block.BlockType.CAGE_ACTIVE:
                    ball.playBounceSound();
                    blockSet.remove(block);
                    ball.reducePowerBouncesLeft();
                    break;

                case Block.BlockType.FREEZING:
                    ball.playBounceSound();
                    ball.reducePowerBouncesLeft();

                    if (block.isPowerRevealed())
                    {
                        blockSet.remove(block);
                    }
                    else
                    {
                        block.reflect(ball);
                        block.revealPower();
                    }

                    break;

                case Block.BlockType.FIRE:
                    ball.playBounceSound();
                    ball.reducePowerBouncesLeft();

                    if (block.isPowerRevealed())
                    {
                        blockSet.remove(block);
                    }
                    else
                    {
                        block.reflect(ball);
                        block.revealPower();
                    }

                    break;

                case Block.BlockType.JOKER:
                    //ballHitJoker(ball, block);
                    break;

                case Block.BlockType.CAGE_INACTIVE:
                    ball.playBounceSound();
                    block.reflect(ball);
                    break;


                // This is here to match the
                // Java code to catch the empty block case.
                default:
                    break;
            }
        }

        private void freezingBallBlockCollision(Ball ball)
        {
            Block block = blockSet.getCollidedBlock(ball);

            switch (block.getType())
            {
                case Block.BlockType.NORMAL:
                case Block.BlockType.CAGE_ACTIVE:
                    block.reflect(ball);
                    ball.playBounceSound();
                    ball.reducePowerBouncesLeft();
                    if (block.getState() != Block.BlockState.Frozen)
                        block.freezeTheBlock();
                    else
                        blockSet.remove(block);
                    break;

                case Block.BlockType.FREEZING:
                    block.reflect(ball);
                    ball.playBounceSound();
                    ball.reducePowerBouncesLeft();

                    if (!block.isPowerRevealed())
                    {
                        block.revealPower();
                    }
                    else if (block.getState() != Block.BlockState.Frozen)
                    {
                        block.freezeTheBlock();
                    }
                    else
                    {
                        blockSet.remove(block);
                        ball.freezeTheBall();
                    }

                    break;

                case Block.BlockType.FIRE:
                    block.reflect(ball);
                    ball.playBounceSound();
                    ball.reducePowerBouncesLeft();

                    if (!block.isPowerRevealed())
                    {
                        block.revealPower();
                    }
                    else if (block.getState() != Block.BlockState.Frozen)
                    {
                        block.freezeTheBlock();
                    }
                    else
                    {
                        blockSet.remove(block);
                        ball.burnTheBall();
                    }

                    break;

                case Block.BlockType.JOKER:
                    //ballHitJoker(ball, block);
                    break;

                case Block.BlockType.CAGE_INACTIVE:
                    block.reflect(ball);
                    ball.playBounceSound();
                    break;


                // This is here to match the
                // Java code to catch the empty block case.
                default:
                    break;
            }
        }

        //private void ballHitJoker(Ball ball, Block jokerBlock)
        //{
        //    jokerBlock.reflect(ball);
        //    ball.playBounceSound();

        //    if (jokerBlockSwapped)
        //    {
        //        blockSet.remove(jokerBlock);
        //    }
        //    else
        //    {
        //        Block blockToReplace = blockSet.getLastCageBlock();

        //        if (blockToReplace != null)
        //        {
        //            jokerBlockSwapped = true;

        //            Vector2 temp = jokerBlock.Center;
        //            jokerBlock.Center = blockToReplace.Center;
        //            blockToReplace.Center = temp;
        //        }
        //    }
        //}
    }
}
