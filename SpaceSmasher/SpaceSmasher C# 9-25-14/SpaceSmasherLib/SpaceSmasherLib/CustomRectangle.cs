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

namespace SpaceSmasherLib
{
    public enum BoundCollidedStatus
    {
        CollideBottom = XNACS1Lib.BoundCollideStatus.CollideBottom,
        CollidedTop = XNACS1Lib.BoundCollideStatus.CollideTop,
        CollidedLeft = XNACS1Lib.BoundCollideStatus.CollideLeft,
        CollidedRight = XNACS1Lib.BoundCollideStatus.CollideRight,
        InsideBound = XNACS1Lib.BoundCollideStatus.InsideBound,
    }

    public class CustomRectangle : XNACS1Rectangle
    {
        public CustomRectangle()
            : base()
        {

        }

        public CustomRectangle(Vector2 center, float width, float height)
            : base(center, width, height)
        {

        }

        public CustomRectangle(Vector2 center, float width, float height, string texture)
            : base(center, width, height, texture)
        {

        }

        public void clampAtWorldBounds()
        {
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public BoundCollidedStatus collideWorldBound()
        {
            if (!Visible)
            {
                return BoundCollidedStatus.InsideBound;
            }

            int status = (int)XNACS1Base.World.CollideWorldBound(this);

            return ((BoundCollidedStatus)status);
        }

        public void reflectTop()
        {
            VelocityY *= -1f;
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public void reflectBottom()
        {
            VelocityY *= -1f;
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public void reflectLeft()
        {
            VelocityX *= -1f;
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public void reflectRight()
        {
            VelocityX *= -1f;
            XNACS1Base.World.ClampAtWorldBound(this);
        }

        public void reflect(XNACS1Circle other)
        {
            if (other != null)
            {
                reflectOutCircle(other);
                //pushOutCircle(other);
                //reflectionVelocityResolution(other);
            }
        }

        public void reflect(XNACS1Rectangle other)
        {
            if (other != null)
            {
                pushOutRectangle(other);
                reflectionVelocityResolution(other);
            }
        }

        // Pushes out a circle while reflecting it based on its velocity.
        protected void reflectOutCircle(XNACS1Circle other)
        {
            if (other != null)
            {
                float topD = 0f, bottomD = 0f, leftD = 0f, rightD = 0f;

                if (other.VelocityY > 0f)
                {   // flying upwards
                    // check for bottom penetration
                    topD = (other.CenterY + other.Radius) - MinBound.Y;
                }
                else
                {
                    // flying downwards
                    // check for top penetration
                    bottomD = MaxBound.Y - (other.CenterY - other.Radius);
                }

                if (other.VelocityX > 0)
                {   // flying towards right
                    // check for left penetration
                    leftD = (other.CenterX + other.Radius) - MinBound.X;
                }
                else
                {
                    // flying towards left
                    // check for right penetration
                    rightD = MaxBound.X - (other.CenterX - other.Radius);
                }

                if (topD > 0)
                {
                    if (leftD > 0)
                    {
                        if (topD < leftD)
                        {
                            other.CenterY = CenterY - other.Radius - (Height / 2f); // push up from top
                            other.VelocityY *= -1f;
                        }
                        else
                        {
                            other.CenterX = CenterX - other.Radius - (Width / 2f); // push towards left
                            other.VelocityX *= -1f;
                        }
                    }
                    else if (rightD > 0)
                    {
                        if (topD < rightD)
                        {
                            other.CenterY = CenterY - other.Radius - (Height / 2f); // push up from top
                            other.VelocityY *= -1f;
                        }
                        else
                        {
                            other.CenterX = CenterX + other.Radius + (Width / 2f); // push towards right
                            other.VelocityX *= -1f;
                        }
                    }
                }
                else if (bottomD > 0)
                {
                    if (leftD > 0)
                    {
                        if (bottomD < leftD)
                        {
                            other.CenterY = CenterY + other.Radius + (Height / 2f); // push up from bottom
                            other.VelocityY *= -1f;
                        }
                        else
                        {
                            other.CenterX = CenterX - other.Radius - (Width / 2f); // push towards left
                            other.VelocityX *= -1f;
                        }
                    }
                    else if (rightD > 0)
                    {
                        if (bottomD < rightD)
                        {
                            other.CenterY = CenterY + other.Radius + (Height / 2f); // push up from bottom
                            other.VelocityY *= -1f;
                        }
                        else
                        {
                            other.CenterX = CenterX + other.Radius + (Width / 2f); // push towards right
                            other.VelocityX *= -1f;
                        }
                    }
                }
            }
        }


        protected void pushOutCircle(XNACS1Circle other)
        {
            if (other != null)
            {
                float topD = 0f, bottomD = 0f, leftD = 0f, rightD = 0f;

                if (other.VelocityY > 0f)
                {   // flying upwards
                    // check for bottom penetration
                    topD = (other.CenterY + other.Radius) - MinBound.Y;
                }
                else
                {
                    // flying downwards
                    // check for top penetration
                    bottomD = MaxBound.Y - (other.CenterY - other.Radius);
                }

                if (other.VelocityX > 0)
                {   // flying towards right
                    // check for left penetration
                    leftD = (other.CenterX + other.Radius) - MinBound.X;
                }
                else
                {
                    // flying towards left
                    // check for right penetration
                    rightD = MaxBound.X - (other.CenterX - other.Radius);
                }

                if (topD > 0)
                {
                    if (leftD > 0)
                    {
                        if (topD < leftD)
                        {
                            other.CenterY = CenterY - other.Radius - (Height / 2f); // push up from top
                        }
                        else
                        {
                            other.CenterX = CenterX - other.Radius - (Width / 2f); // push towards left
                        }
                    }
                    else if (rightD > 0)
                    {
                        if (topD < rightD)
                        {
                            other.CenterY = CenterY - other.Radius - (Height / 2f); // push up from top
                        }
                        else
                        {
                            other.CenterX = CenterX + other.Radius + (Width / 2f); // push towards right
                        }
                    }
                }
                else if (bottomD > 0)
                {
                    if (leftD > 0)
                    {
                        if (bottomD < leftD)
                        {
                            other.CenterY = CenterY + other.Radius + (Height / 2f); // push up from bottom
                        }
                        else
                        {
                            other.CenterX = CenterX - other.Radius - (Width / 2f); // push towards left
                        }
                    }
                    else if (rightD > 0)
                    {
                        if (bottomD < rightD)
                        {
                            other.CenterY = CenterY + other.Radius + (Height / 2f); // push up from bottom
                        }
                        else
                        {
                            other.CenterX = CenterX + other.Radius + (Width / 2f); // push towards right
                        }
                    }
                }
             
                /*
                Vector2 otherCenter = new Vector2(other.CenterX, other.CenterY);
                float left = CenterX - (Width / 2);
                float right = CenterX + (Width / 2);
                float top = CenterY + (Width / 2);
                float bottom = CenterY - (Width / 2);
                otherCenter.X = MathHelper.Clamp(otherCenter.X, left, right);
                otherCenter.Y = MathHelper.Clamp(otherCenter.Y, top, bottom);
                Vector2 direction = other.Center - otherCenter;
                float dist = other.Radius - direction.Length();
                direction.Normalize();
                other.Center += dist * direction;
                */
            }
        }

        protected void pushOutRectangle(XNACS1Rectangle other)
        {
            // to be implemented
        }

        protected void reflectionVelocityResolution(XNACS1Primitive other)
        {
            if (other != null)
            {
                if (other.CenterY >= CenterY + Height / 2f || other.CenterY <= CenterY - Height / 2f)
                {
                    other.VelocityY *= -1;
                }
                else
                {
                    other.VelocityX *= -1;
                }
            }
        }
    }
}
