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
    public class CustomCircle : XNACS1Circle
    {
        public CustomCircle()
            : base()
        {
          
        }

        public CustomCircle(Vector2 center, float radius)
            : base(center, radius)
        {

        }

        public CustomCircle(Vector2 center, float radius, string texture)
            : base(center, radius, texture)
        {

        }

        public virtual BoundCollidedStatus collideWorldBound()
        {
            if(!Visible)
            {
                return BoundCollidedStatus.InsideBound;
            }

           int status =  (int)XNACS1Base.World.CollideWorldBound(this);

           return ((BoundCollidedStatus) status);
        }

        public void clampAtWorldBounds()
        {
            XNACS1Base.World.ClampAtWorldBound(this);
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
                pushOutCircle(other);
                reflectHelper(other);
            }
        }

        public void reflect(XNACS1Rectangle other)
        {
            if (other != null)
            {
                pushOutRectangle(other);
                reflectHelper(other);
            }
        }

        protected void pushOutCircle(XNACS1Circle other)
        {
            if (other != null)
            {
                // pushout
                Vector2 dist = other.Center - Center;

                if (dist.Length() != 0)
                {
                    Vector2 direction = new Vector2(dist.X, dist.Y);
                    direction.Normalize();
                    float totalRadius = other.Radius + Radius;
                    other.CenterX += (totalRadius - dist.Length()) * direction.X;
                    other.CenterY += (totalRadius - dist.Length()) * direction.Y;
                }
            }
        }

        protected void pushOutRectangle(XNACS1Rectangle other)
        {
            if (other != null)
            {
                Vector2 otherCenter = new Vector2(CenterX, CenterY);

                float left = other.CenterX - (other.Width / 2);
                float right = other.CenterX + (other.Width / 2);
                float top = other.CenterY + (other.Height / 2);
                float bottom = other.CenterY - (other.Height / 2);
                otherCenter.X = MathHelper.Clamp(Center.X, left, right);
                otherCenter.Y = MathHelper.Clamp(Center.Y, bottom, top);

                Vector2 direction = Center - otherCenter;
                float dist = Radius - direction.Length();
                if (dist < otherCenter.Length())
                {
                    direction.Normalize();
                    other.Center -= dist * direction;
                }
            }
        }

        protected void reflectHelper(XNACS1Primitive other)
        {
            if (other != null)
            {
                // Reflect
                if (other.CenterY >= CenterY + Radius || other.CenterY <= CenterY - Radius)
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
