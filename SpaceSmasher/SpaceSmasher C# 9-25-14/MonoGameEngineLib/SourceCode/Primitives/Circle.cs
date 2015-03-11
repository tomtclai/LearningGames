using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace XNACS1Lib
{
    /// <summary>
    /// A circle primitive.
    /// </summary>
    public class XNACS1Circle : XNACS1Primitive
    {

        /// <summary>
        /// Contstructs a circle. 
        /// </summary>
        /// <param name="center">center of the circle.</param>
        /// <param name="radius">radius of the circle.</param>
        /// <param name="tex">texture to be used for the circle.</param>
        public XNACS1Circle(Vector2 center, float radius, String tex)
            :
            base(PrimitiveType.PrimitiveCircle, BlendState.AlphaBlend, center, new Vector2(2.0f * radius, 2.0f * radius), tex)
        {
        }

        /// <summary>
        /// Constructs a circle.
        /// </summary>
        /// <param name="center">center of the circle.</param>
        /// <param name="radius">radius of the circle.</param>
        public XNACS1Circle(Vector2 center, float radius)
            :
            base(PrimitiveType.PrimitiveCircle, BlendState.AlphaBlend, center, new Vector2(2.0f * radius, 2.0f * radius), null)
        {
        }

        /// <summary>
        /// Constructs a circle, with center located at (0,0), and radius of 1.0.
        /// </summary>
        public XNACS1Circle()
            : base(PrimitiveType.PrimitiveCircle, BlendState.AlphaBlend)
        { }

        internal XNACS1Circle(PrimitiveType type) : base(type, BlendState.AlphaBlend) { }

        /// <summary>
        /// Radius of the circle.
        /// </summary>
        public float Radius {
            get { return SizeX * 0.5f; }
            set { SizeX = 2.0f* value; SizeY = 2.0f*value; }
        }
    
        /// <summary>
        /// Draws the circle.
        /// </summary>
        protected override void DrawPrimitive() {
            if (m_UseSpriteSheet && m_SpriteSourceRect != null)
                sm_DrawHelper.DrawCircle(Center, Radius, m_SpriteSourceRect, m_Rotate, m_Texture != null ? m_TextureColor : m_Color, m_Texture);
            else
                sm_DrawHelper.DrawCircle(Center, Radius, null, m_Rotate, m_Texture != null ? m_TextureColor : m_Color, m_Texture);
        }
    }
}