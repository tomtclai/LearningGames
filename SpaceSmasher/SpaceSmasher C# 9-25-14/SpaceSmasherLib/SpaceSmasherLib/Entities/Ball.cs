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
    public class Ball : CustomCircle
    {
        protected const string defaultTexture = "Balls/Ball_Normal";
        protected const string defaultFrozenTexture = "Balls/Ball_Ice";
        protected const string defaultFireTexture = "Balls/Ball_Fire";


        protected const string defaultBlockBounceSound = "Ball/AlternaBounce2SpaceSmasher";
        protected const string defaultWallBounceSound = "Ball/Ball-Wall Collision";
        protected const string defaultJokerBounceSound = "Ball/LaughSpaceSmasher";
        protected const string defaultFreezeBounceSound = "Ball/Ice1";
        protected const string defaultBreakIceBounceSound = "Ball/IceBreakin2SpaceSmasher";
        protected const string defaultFireBounceSound = "Ball/Fire";

        protected const string defaultDieSound = "Ball/Laser1";
        public int powerBouncesLeft;
        protected KeyboardState previousKeyboard;
        protected bool ballCannotMove;
        
        public enum BallType
        {
            NORMAL, ICE, FIRE, DEAD
        }

        protected BallType state;
        protected float speed;
        protected string frozenTexture;
        protected string normalTexture;
        protected string fireTexture;
        protected Vector2 spawnPoint;
        protected Boolean controlWithMouseInDebug = false;

        // Overriden world bounds for balls.
        protected float upperBound = XNACS1Base.World.WorldDimension.Y * .87f;
        protected float lowerBound = 0;
        protected float leftBound = 2f;
        protected float rightBound = XNACS1Base.World.WorldDimension.X - 2f;

        public Ball()
            : base(new Vector2(XNACS1Base.World.WorldDimension.X * 0.5f, XNACS1Base.World.WorldDimension.Y * 0.35f),
            XNACS1Base.World.WorldDimension.Y * 0.020f, defaultTexture)
        {
            initialize();
        }

        public Ball(float x, float y)
            : base(new Vector2(x, y), XNACS1Base.World.WorldDimension.Y * 0.35f, defaultTexture)
        {
            initialize();
        }

        public Ball(float x, float y, float radius)
            : base(new Vector2(x, y), radius, defaultTexture)
        {
            initialize();
        }

        private void initialize()
        {
            ballCannotMove = true;
            powerBouncesLeft = 0;
            ShouldTravel = true;
            Velocity = VelocityDirection = Vector2.Zero;
            speed = 1f;
            Texture = normalTexture = defaultTexture;
            frozenTexture = defaultFrozenTexture;
            fireTexture = defaultFireTexture;
            spawnPoint = new Vector2(XNACS1Base.World.WorldDimension.X * 0.5f, XNACS1Base.World.WorldDimension.Y * 0.35f);

            if (GameObjects.ballList == null)
                GameObjects.ballList = new List<Ball>();

            GameObjects.ballList.Add(this);
        }

        public void setControlWithMouseInDebug(bool value)
        {
            controlWithMouseInDebug = value;
        }

        public void update()
        {
            KeyboardState currentKeyboard = Keyboard.GetState();

            if (GameObjects.debugMode)
            {
                if(ballCannotMove)
                    Velocity = Vector2.Zero;

                if (currentKeyboard.IsKeyDown(Keys.Enter) && previousKeyboard.IsKeyUp(Keys.Enter))
                    ballCannotMove = !ballCannotMove;

                if (controlWithMouseInDebug)
                {
                    // Need to convert to world coordinates inside of MouseDevice
                    CenterX = MouseDevice.getMouseX();
                    CenterY = MouseDevice.getMouseY();
                }
                else
                {
                    if (currentKeyboard.IsKeyDown(Keys.W))
                    {
                        VelocityY = speed;
                      //  CenterY +=  speed;
                    }
                    else if (currentKeyboard.IsKeyDown(Keys.S))
                    {
                        VelocityY = -speed;
                        //CenterY -=  speed;
                    }
                    else if (currentKeyboard.IsKeyDown(Keys.A))
                    {
                        VelocityX = -speed;
                    }
                    else if (currentKeyboard.IsKeyDown(Keys.D))
                    {
                        VelocityX = speed;
                    }

                    XNACS1Base.World.ClampAtWorldBound(this);
                }
            }

            previousKeyboard =  currentKeyboard;
        }

        public void setPowerBouncesLeft(int durationInBounces)
        {
            if (powerBouncesLeft > 0 || durationInBounces > 0)
            {
                powerBouncesLeft = durationInBounces;

                if (powerBouncesLeft == 0)
                {
                    powerBouncesLeft = -1;

                    normalTheBall();
                }
            }
        }

        public void reducePowerBouncesLeft()
        {
            setPowerBouncesLeft(powerBouncesLeft - 1);
        }

        public void remove()
        {
            state = BallType.DEAD;
            Visible = false;
            RemoveFromAutoDrawSet();
            Velocity = Vector2.Zero;
        }

        internal void destroy()
        {
            remove();
            if (GameObjects.ballList != null)
                GameObjects.ballList.Remove(this);
        }

        public void playBounceSound()
        {
            XNACS1Base.PlayACue(defaultBlockBounceSound);
        }

        public void playDieSound()
        {
            XNACS1Base.PlayACue(defaultDieSound);
        }
        /**
         * Will play the sound the ball should make when it bounces of of a wall
         */
        public void playWallBounceSound()
        {
            XNACS1Base.PlayACue(defaultWallBounceSound);
        }
        /**
         * Will play the sound the ball should make when it freezes a block
         */
        public void playBallFreezeBlockSound()
        {
            XNACS1Base.PlayACue(defaultFreezeBounceSound);
        }
        /**
         * Will play the sound the ball should make when it shatters something
         */
        public void playBallShatterSound()
        {
            XNACS1Base.PlayACue(defaultBreakIceBounceSound);
        }
        /**
         * Will play the sound the ball should make when it hits a Joker
         */
        public void playBallJokerSound()
        {
            XNACS1Base.PlayACue(defaultJokerBounceSound);
        }
        /**
         * Will play the sound the ball should make when it is ignited
         */
        public void playBallIgnitedSound()
        {
            XNACS1Base.PlayACue(defaultFireBounceSound);
        }


        /// <summary>
        /// Spawns a normal ball at the ball's current position
        /// </summary>
        public void spawn()
        {
            if (GameObjects.blockList != null && GameObjects.blockList.Count > 0)
            {
                Block lowest = GameObjects.blockList[0];

                foreach (Block block in GameObjects.blockList)
                {
                    if (block.getType() != Block.BlockType.EMPTY && block.CenterY < lowest.CenterY)
                        lowest = block;
                }

                spawnPoint = new Vector2(XNACS1Base.World.WorldDimension.X * 0.5f, lowest.CenterY - lowest.Height - (XNACS1Base.World.WorldDimension.Y * 0.10f));
            }
            else
            {
                spawnPoint = new Vector2(XNACS1Base.World.WorldDimension.X * 0.5f, XNACS1Base.World.WorldDimension.Y * 0.5f);
            }

            spawn(spawnPoint);
        }

        public void spawn(Paddle paddle)
        {
            spawn(paddle.CenterX, paddle.CenterY + 2f);
        }

        /// <summary>
        /// Spawns a normal ball at the given position
        /// </summary>
        /// <param name="centerX"></param>
        /// <param name="centerY"></param>
        public void spawn(float centerX, float centerY)
        {
            spawn(new Vector2(centerX, centerY));
        }

        /// <summary>
        /// Spawns a normal ball at the given position
        /// </summary>
        /// <param name="postion"></param>
        public void spawn(Vector2 position)
        {
            Vector2 direction = new Vector2(XNACS1Base.RandomFloat(-0.8f, 0.8f), -0.7f);

            if (direction != Vector2.Zero)
                direction.Normalize();
            spawn(position, direction);
        }

        /// <summary>
        /// Spawns a normal ball at a given position with a given direction
        /// </summary>
        /// <param name="centerX"></param>
        /// <param name="centerY"></param>
        /// <param name="initialVelocity"></param>
        public void spawn(float centerX, float centerY, Vector2 direction)
        {
            spawn(new Vector2(centerX, centerY), direction);
        }

        /// <summary>
        /// Spawns a normal ball at a given position with a given direction
        /// </summary>
        /// <param name="position"></param>
        /// <param name="direction"></param>
        public void spawn(Vector2 position, Vector2 direction)
        {
            if (position == null)
                position = Vector2.Zero;

            if (direction == null)
                direction = Vector2.Zero;

            if (direction != Vector2.Zero)
                direction.Normalize();

            CenterX = position.X;
            CenterY = position.Y;
            Velocity = direction * speed;
            Texture = normalTexture;
            Visible = true;
            AddToAutoDrawSet();
            state = BallType.NORMAL;
        }

        public void normalTheBall()
        {
            state = BallType.NORMAL;
            Texture = normalTexture;
            setPowerBouncesLeft(0);
        }

        public void freezeTheBall()
        {
            state = BallType.ICE;
            Texture = frozenTexture;
            setPowerBouncesLeft(7);
        }

        public void burnTheBall()
        {
            state = BallType.FIRE;
            Texture = fireTexture;
            setPowerBouncesLeft(3);
        }

        public void setType(BallType type)
        {
            state = type;
            switch (type)
            { 
                case BallType.NORMAL:
                    Texture = normalTexture;
                    break;
                case BallType.ICE:
                    Texture = frozenTexture;
                    break;
                case BallType.FIRE:
                    Texture = fireTexture;
                    break;
            }
        }

        public void setToDeadState()
        {
            state = BallType.DEAD;
        }

        public void setToNormalState()
        {
            state = BallType.NORMAL;
        }

        public void setToFireState()
        {
            state = BallType.FIRE;
        }

        public void setToFrozenState()
        {
            state = BallType.ICE;
        }

        public bool isDead()
        {
            return state == BallType.DEAD;
        }

        public bool isNormal()
        {
            return state == BallType.NORMAL;
        }

        public bool isFrozen()
        {
            return state == BallType.ICE;
        }

        public bool isBurning()
        {
            return state == BallType.FIRE;
        }

        public float getSpeed()
        {
            return speed;
        }

        public void setSpeed(float value)
        {
            speed = Speed;
        }

        public string getFrozenTexture()
        {
            return frozenTexture;
        }

        public void setFrozenTexture(string value)
        {
            frozenTexture = value;

            if (state == BallType.ICE)
                Texture = frozenTexture;
        }

        public string getNormalTexture()
        {
            return normalTexture;
        }

        public void setNormalTexture(string value)
        {
            normalTexture = value;

            if (state == BallType.NORMAL)
                Texture = normalTexture;
        }

        public string getFireTexture()
        {
            return fireTexture;
        }

        public void setFireTexture(string value)
        {
            fireTexture = value;

            if (state == BallType.FIRE)
                Texture = fireTexture;
        }

        public Vector2 getSpawnPoint()
        {
            return spawnPoint;
        }

        public void setSpawnPoint(Vector2 value)
        {
            spawnPoint = value;
        }

        public override BoundCollidedStatus collideWorldBound()
        {
            if (MinBound.X < leftBound)
            {
                return BoundCollidedStatus.CollidedLeft;
            }
            if (MinBound.Y < lowerBound)
            {
                return BoundCollidedStatus.CollideBottom;
            }
            if (MaxBound.Y > upperBound)
            {
                return BoundCollidedStatus.CollidedTop;
            }
            if (CenterX + (SizeX / 2) > rightBound)
            {
                return BoundCollidedStatus.CollidedRight;
            }

            return BoundCollidedStatus.InsideBound;
        }
    }
}
