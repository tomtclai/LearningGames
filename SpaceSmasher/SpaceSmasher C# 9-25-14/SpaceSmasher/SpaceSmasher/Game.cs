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

// Currently the ball can get stuck inside the bricks or wall and this can be annoying.
namespace BlockBreakerGame
{
    public class Game : SpaceSmasherBase
    {
        private bool jokerBlockSwapped = false;

        protected override void initialize()
        {
            jokerBlockSwapped = false;
            lifeSet.add(5);
            paddleSet.add(1);
            ballSet.add(1);
            ballSet.get(0).Visible = false;
            blockSet.setBlocksPerRow(7);

            blockSet.addNormalBlock(1);
            blockSet.addFireBlock(1);
            blockSet.addNormalBlock(1);
            blockSet.addFreezingBlock(1);
            blockSet.addNormalBlock(1);
            blockSet.addCageBlock(1);
            blockSet.addNormalBlock(1);

            blockSet.addNormalBlock(4);
            blockSet.addCageBlock(1);
            blockSet.addNormalBlock(1);
            blockSet.addCageBlock(1);

            blockSet.addJokerBlock(1);
            blockSet.addNormalBlock(4);
            blockSet.addCageBlock(1);
            blockSet.addNormalBlock(1);

            blockSet.addUnbreakableBlock(7);

            trapSet.add(2);
            //setDebugMode(true);
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
                    if (theSwitch.isActive() && theSwitch.Collided(ball))
                    {
                        theSwitch.reflect(ball);
                        theSwitch.deactivate();
                        trapSet.deactivate();
                        blockSet.deactivateUnbreakableBlocks();
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
                trap.reflect(ball);
                ball.playBounceSound();
                trapSet.activate();
                blockSet.activateUnbreakableBlocks();
                theSwitch.activate();
            }
        }

        private void normalBallBlockCollision(Ball ball)
        {
            Block block = blockSet.getCollidedBlock(ball);

            switch (block.getType())
            {
                case Block.Type.Normal:
                    block.reflect(ball);
                    ball.playBounceSound();
                    blockSet.remove(block);
                    break;

                case Block.Type.Frozen:
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

                case Block.Type.Fire:
                    block.reflect(ball);
                    ball.playBounceSound();

                    if (block.isPowerRevealed())
                    {
                        ball.burnTheBall();
                        blockSet.remove(block);
                    }
                    else
                    {
                        block.revealPower();
                    }

                    break;

                case Block.Type.Joker:
                    ballHitJoker(ball, block);
                    break;

                case Block.Type.Unbreakable:
                    block.reflect(ball);
                    ball.playBounceSound();
                    if (!block.isPowerRevealed())
                    {
                        blockSet.remove(block);
                    }
                    break;

                case Block.Type.Cage:
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
                case Block.Type.Normal:
                    ball.playBounceSound();
                    blockSet.remove(block);
                    ball.reducePowerBouncesLeft();
                    break;

                case Block.Type.Frozen:
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

                case Block.Type.Fire:
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

                case Block.Type.Joker:
                    ballHitJoker(ball, block);
                    break;

                case Block.Type.Unbreakable:
                    ball.playBounceSound();

                    if (!block.isPowerRevealed())
                    {
                        blockSet.remove(block);
                        ball.reducePowerBouncesLeft();
                    }
                    else
                    {
                        block.reflect(ball);
                    }
                    break;

                case Block.Type.Cage:
                    block.reflect(ball);
                    ball.playBounceSound();
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
                case Block.Type.Normal:
                    block.reflect(ball);
                    ball.playBounceSound();
                    ball.reducePowerBouncesLeft();
                    if (block.getState() != Block.BlockState.Frozen)
                        block.freezeTheBlock();
                    else
                        blockSet.remove(block);
                    break;

                case Block.Type.Frozen:
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

                case Block.Type.Fire:
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

                case Block.Type.Joker:
                    ballHitJoker(ball, block);
                    break;

                case Block.Type.Unbreakable:
                    block.reflect(ball);
                    ball.playBounceSound();
                    if (block.getState() == Block.BlockState.Normal)
                    {
                        ball.reducePowerBouncesLeft();
                        block.freezeTheBlock();
                    }
                    else if (block.getState() == Block.BlockState.Frozen)
                    {
                        ball.reducePowerBouncesLeft();
                        blockSet.remove(block);
                    }
                    break;

                case Block.Type.Cage:
                    block.reflect(ball);
                    ball.playBounceSound();
                    break;

                // This is here to match the
                // Java code to catch the empty block case.
                default:
                    break;
            }
        }

        private void ballHitJoker(Ball ball, Block jokerBlock)
        {
            jokerBlock.reflect(ball);
            ball.playBounceSound();

            if (jokerBlockSwapped)
            {
                blockSet.remove(jokerBlock);
            }
            else
            {
                Block blockToReplace = blockSet.getLastCageBlock();

                if (blockToReplace != null)
                {
                    jokerBlockSwapped = true;

                    Vector2 temp = jokerBlock.Center;
                    jokerBlock.Center = blockToReplace.Center;
                    blockToReplace.Center = temp;
                }
            }
        }
    }
}
