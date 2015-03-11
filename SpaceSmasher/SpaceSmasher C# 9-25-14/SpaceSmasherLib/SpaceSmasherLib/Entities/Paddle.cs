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

namespace SpaceSmasherLib.Entities
{
    public class Paddle : CustomRectangle
    {
        public enum PaddleState
        { NORMAL, ICE, FIRE }

        private const string defaultNormalTexture = "Paddles/Paddle_Normal";
        private const string defaultFireSpritesheet = "Paddles/fire_paddle";
        private const string defaultFrozenSpritesheet = "Paddles/ice_paddle";
        private float speed;

        private const int SPRITESHEET_FRAMES = 120;
        private const int SPRITE_HEIGHT = 81;
        private const int SPRITE_WIDTH = 347;
        private const int TICKS_PER_FRAME = 1;
        private const int SPRITE_FINAL_FRAME_COL = 0;
        private const int SPRITE_FINAL_FRAME_ROW = 17;
  
        private PaddleState state;
  
        // Offsets the left and right bounderys to the inside.
        private const float BOUNDARY_INNER_OFFSET = 2f;

        public Paddle()
            : base(new Vector2(XNACS1Base.World.WorldDimension.X * 0.5f, (XNACS1Base.World.WorldDimension.Y * 0.04f) * 2.6f),
            XNACS1Base.World.WorldDimension.X * 0.14f, XNACS1Base.World.WorldDimension.Y * 0.04f, defaultNormalTexture)
        {
            initialize();
        }

        public Paddle(float x, float y)
            : base(new Vector2(x, y), XNACS1Base.World.WorldDimension.X * 0.14f, XNACS1Base.World.WorldDimension.Y * 0.04f, defaultNormalTexture)
        {
            initialize();
        }

        public Paddle(float x, float y, float width, float height)
            : base(new Vector2(x, y), width, height, defaultNormalTexture)
        {
            initialize();
        }

        private void initialize()
        {
            ShouldTravel = true;
            Velocity = VelocityDirection = Vector2.Zero;
            speed = 1.3f;

            if (GameObjects.paddleList == null)
                GameObjects.paddleList = new List<Paddle>();

            GameObjects.paddleList.Add(this);
            state = PaddleState.NORMAL;
        }

        public void update()
        {
            if (state != PaddleState.NORMAL)
            {
                if (CurrentSpriteSheetFrameX == SPRITE_FINAL_FRAME_COL &&
                    CurrentSpriteSheetFrameY == SPRITE_FINAL_FRAME_ROW)
                {
                    setToNormal();
                }
            }
        }

        public void remove()
        {
            Visible = false;
            RemoveFromAutoDrawSet();
            Velocity = Vector2.Zero;
        }

        internal void destroy()
        {
            remove();

            if (GameObjects.paddleList != null)
                GameObjects.paddleList.Remove(this);
        }

        public void reflect(Ball ball)
        {
            if (ball != null)
            {
                pushOutCircle(ball);

                Vector2 offset = ball.Center - Center;

                offset.X = (offset.X + (Size.X * 0.5f));
                offset.X = (offset.X / Size.X);
                offset.X = (offset.X * 5.0f);

                offset.X = (int)offset.X;

                if (offset.X < 0.0f)
                {
                    offset.X = 0.0f;
                }

                if (offset.X > 4.0f)
                {
                    offset.X = 4.0f;
                }

                float angle = 180.0f;

                angle -= (30.0f * (offset.X + 1.0f));
                angle = MathHelper.ToRadians(angle);

                ball.VelocityX = (float)Math.Cos(angle);
                ball.VelocityY = (float)Math.Sin(angle);

                /*
                float regionSize = Width / 5f;
                float endAngle = (float)(Math.PI * 2 / 3);
                float middleAngle = (float)((Math.PI / 2) + endAngle) / 2f;
                Vector2 ballDirection = ball.VelocityDirection;

                if (ball.CenterX + ball.Radius < CenterX - regionSize * 1.5f)
                    ballDirection.X = (float)Math.Cos(endAngle);

                else if (ball.CenterX + ball.Radius < CenterX - regionSize * 0.5f)
                    ballDirection.X = (float)Math.Cos(middleAngle);

                else if (ball.CenterX - ball.Radius > CenterX + regionSize * 0.5f)
                    ballDirection.X = -(float)Math.Cos(middleAngle);

                else if (ball.CenterX - ball.Radius > CenterX + regionSize * 1.5f)
                    ballDirection.X = -(float)Math.Cos(endAngle);

                ballDirection.Y = Math.Abs(ball.VelocityDirection.Y);
                ball.Velocity = ballDirection * ball.Speed;
                ball.VelocityX += VelocityX * 0.1f;
                */
            }

        }

        public PaddleState getState()
        {
            return state;
        }

        public void moveLeft()
        {
            CenterX -= speed;
            clampPaddle();
        }

        public void moveRight()
        {
            CenterX += speed;
            clampPaddle();
        }

        public void moveUp()
        {
            CenterY += speed;
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public void moveDown()
        {
            CenterY -= speed;
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public float getSpeed()
        {
            return speed;
        }

        public void setSpeed(float value)
        {
            speed = value;
        }

        private void clampPaddle()
        {
            float offAmountLeft = CenterX - (Width * 0.5f);
            float offAmountRight =
                    (CenterX + (Width * 0.5f)) - XNACS1Base.World.WorldDimension.X;

            // Only clamp if the paddle is lower than the limit
            if (offAmountLeft < BOUNDARY_INNER_OFFSET)
            {
                CenterX = BOUNDARY_INNER_OFFSET + (Width * 0.5f);
            }
            if (offAmountRight > -BOUNDARY_INNER_OFFSET)
            {
                CenterX = (XNACS1Base.World.WorldDimension.X - BOUNDARY_INNER_OFFSET) - (Width * 0.5f);
            }
        }
        /// <summary>
        /// Starts the red paddle animation and countdown. State will be set to
        /// fire until the animation completes.
        /// </summary>
        public void startFire()
        {
            state = PaddleState.FIRE;
            SetTextureSpriteSheet(defaultFireSpritesheet, SPRITE_WIDTH, SPRITE_HEIGHT);
            SetTextureSpriteAnimationFrames(0, 0,
                SPRITE_FINAL_FRAME_COL, SPRITE_FINAL_FRAME_ROW,
                TICKS_PER_FRAME, SpriteSheetAnimationMode.AnimateForwardThenStop);
            UseSpriteSheet = true;
            UseSpriteSheetAnimation = true;
        }
        /// <summary>
        /// Starts the blue paddle animation and countdown. State will be set to
        /// ice until the animation completes.
        /// </summary>
        public void startIce()
        {
            state = PaddleState.ICE;
            SetTextureSpriteSheet(defaultFrozenSpritesheet, SPRITE_WIDTH, SPRITE_HEIGHT);
            SetTextureSpriteAnimationFrames(0, 0,
                SPRITE_FINAL_FRAME_COL, SPRITE_FINAL_FRAME_ROW,
                TICKS_PER_FRAME,
                SpriteSheetAnimationMode.AnimateForwardThenStop);
            UseSpriteSheet = true;
            UseSpriteSheetAnimation = true;
        }
        /// <summary>
        /// Sets the paddle to the normal texture.
        /// </summary>
        public void setToNormal()
        {
            state = PaddleState.NORMAL;
            Texture = defaultNormalTexture;
            UseSpriteSheet = false;
            UseSpriteSheetAnimation = false;
        }
    }
}
