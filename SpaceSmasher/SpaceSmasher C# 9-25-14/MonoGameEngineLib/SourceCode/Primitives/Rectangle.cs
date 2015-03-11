using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;


namespace XNACS1Lib
{
    /// <summary>
    /// A Rectangle primitive.
    /// </summary>
    public class XNACS1Rectangle : XNACS1Primitive
    {
        /// <summary>
        /// Constructs a rectangle.
        /// </summary>
        /// <param name="center">center of the rectangle.</param>
        /// <param name="width">width of the rectangle.</param>
        /// <param name="height">height of the rectangle.</param>
        /// <param name="tex">texture to be used to cover the rectangle.</param>
        public XNACS1Rectangle(Vector2 center, float width, float height, String tex)
            : base(PrimitiveType.PrimitiveRectangle, BlendState.AlphaBlend, center, new Vector2(width, height), tex)
        { }

        /// <summary>
        /// Constructs a rectangle.
        /// </summary>
        /// <param name="center">center of the rectangle.</param>
        /// <param name="width">width of the rectangle.</param>
        /// <param name="height">height of the rectangle.</param>
        public XNACS1Rectangle(Vector2 center, float width, float height)
            :
            base(PrimitiveType.PrimitiveRectangle, BlendState.AlphaBlend, center, new Vector2(width, height), null)
        { }

        /// <summary>
        /// Constructs a rectangle with end points located at aPos and bPos, and width.
        /// </summary>
        /// <param name="aPos">End position of rectangle.</param>
        /// <param name="bPos">End position of rectangle.</param>
        /// <param name="width">Width of rectangle.</param>
        /// <param name="tex">texture image to be used to cover the rectangle.</param>
        public XNACS1Rectangle(Vector2 aPos, Vector2 bPos, float width, String tex)
            :
            base(PrimitiveType.PrimitiveRectangle, BlendState.AlphaBlend)
        {
            Texture = tex;
            SetEndPoints(aPos, bPos, width);
        }

        /// <summary>
        /// Constrcuts a 1x1 rectangle with center located at (0,0).
        /// </summary>
        public XNACS1Rectangle()
            : base(PrimitiveType.PrimitiveRectangle, BlendState.AlphaBlend)
        { }

        /// <summary>
        ///  Width of the rectangle.
        /// </summary>
        /// <seealso cref="Height"/>
        /// <seealso cref="SetEndPoints"/>
        /// <seealso cref="LowerLeft"/>
        public float Width {
            get { return SizeX; }
            set {
                if (!m_CenterReference)
                    m_Center.X = m_Center.X - (SizeX / 2.0f) + (value / 2.0f);
                SizeX = value;
            }
        }

        /// <summary>
        /// Height of the rectangle.
        /// </summary>
        /// <seealso cref="Width"/>
        /// <seealso cref="SetEndPoints"/>
        /// <seealso cref="LowerLeft"/>
        public float Height
        {
            get { return SizeY; }
            set {
                if (!m_CenterReference)
                    m_Center.Y = m_Center.Y - (SizeY / 2.0f) + (value / 2.0f);
                SizeY = value; 
            }
        }

        /// <summary>
        /// Lower left corner of the rectangle.
        /// </summary>
        public Vector2 LowerLeft
        {
            get { return new Vector2(Center.X - (SizeX / 2.0f), Center.Y - (SizeY / 2.0f)); }
            set
            {
                CenterX = value.X + (SizeX / 2.0f);
                CenterY = value.Y + (SizeY / 2.0f);
                m_CenterReference = false;
            }
        }

        /// <summary>
        /// aPos and bPos are two end positions on the opposite edges of the rectangle. Width is the width of the edge.
        /// </summary>
        /// <param name="aPos">Center of one end position.</param>
        /// <param name="bPos">Center of the opposite end position.</param>
        /// <param name="width">Width of the rectangle.</param>
        public void SetEndPoints(Vector2 aPos, Vector2 bPos, float width)
        {
            Vector2 delta = bPos - aPos;
            float length = delta.Length();
            Vector2 center = 0.5f * (aPos + bPos);
            Vector2 size = new Vector2(length, width);
            Init(PrimitiveType.PrimitiveRectangle, BlendState.AlphaBlend, center, size, Texture);

            if (length > 0.001f)
            {
                // DrawCircle(pa, width, 0.0f, Color.Black, Color.Black, null);
                // DrawCircle(pb, width, 0.0f, Color.Green, Color.Green, null);
                delta /= length;
                RotateAngle = (float)Math.Acos((double)delta.X);
                RotateAngle *= 180.0f / (float)Math.PI;
                if (delta.X > 0.0f)
                {
                    if (delta.Y < 0.0f)
                        RotateAngle = -RotateAngle;
                }
                else
                {
                    if (delta.Y < 0.0f)
                        RotateAngle = -RotateAngle;
                }
            }
        }

        public bool Contains(Vector2 pos)
        {
            if (pos.X < (CenterX + (SizeX * 0.5f)) &&
                pos.X > (CenterX - (SizeX * 0.5f)) &&
                pos.Y < (CenterY + (SizeY * 0.5f)) &&
                pos.Y > (CenterY - (SizeY * 0.5f)))
            {
                return true;
            }
            return false;
        }

        /// <summary>
        /// Draws the Rectangle.
        /// </summary>
        protected override void DrawPrimitive() {
            if (m_UseSpriteSheet && m_SpriteSourceRect != null)
                sm_DrawHelper.DrawRectangle(Center, SizeX, SizeY, m_SpriteSourceRect, m_Rotate, m_Texture != null ? m_TextureColor : m_Color, m_Texture);
            else
                sm_DrawHelper.DrawRectangle(Center, SizeX, SizeY, null, m_Rotate, m_Texture != null ? m_TextureColor : m_Color, m_Texture);
        }
    }
}