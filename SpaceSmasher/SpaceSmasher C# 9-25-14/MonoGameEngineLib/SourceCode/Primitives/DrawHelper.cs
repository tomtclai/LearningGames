//*******************************************
// PredefinedEmitter.cs
// Author:
// ChangeLog:
// Samuel Cook and Ron Cook - Created a set for particles/emitters, a set to track primitives
//      with shouldtravel set to true, rather than running travelprimitive() on every primitive.
//      Changed the parameters of some draw methods to take different parameters.
//      Added support for Texture based collision (currently not working)
//      Added functionality in the draw methods so that additive blending could be supported. 
//      Added support so that the circle and rectangle were created on the fly rather than requiring two texture files.
//*******************************************

using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace XNACS1Lib
{
    /// <summary>
    /// This is the base class for programming assignments.
    /// </summary>
    internal class XNACS1LibDrawHelper
    {
        private TextureTable m_TexTable;
        private FontManager m_FontManager;
        private Vector2 m_WorldMin, m_WorldMax;
        private Vector2 m_DeviceSize;

        private Matrix mW2D;

        private GraphicsDevice m_GraphicsDevice = null;
        private SpriteBatch mSpriteBatch = null;
        // internal SpriteBlendMode m_CurrentBlendMode;
        internal BlendState m_CurrentBlendState = BlendState.AlphaBlend;

        private Texture2D m_TheCircle = null;
        private Texture2D m_TheSquare = null;

        private List<XNACS1Primitive> m_DrawSet = null;
        private List<XNACS1Primitive> m_TravelSet = null;
        private List<XNACS1ParticleEmitter> m_EmitterSet = null;

        public XNACS1LibDrawHelper(GraphicsDevice graphicsDevice, ContentManager c, XNACS1Rectangle worldBound, FontManager fontManager, Vector2 deviceSize)
        {
            m_DrawSet = new List<XNACS1Primitive>();
            m_TravelSet = new List<XNACS1Primitive>();
            m_EmitterSet = new List<XNACS1ParticleEmitter>();
            
            m_GraphicsDevice = graphicsDevice;

            mSpriteBatch = new SpriteBatch(m_GraphicsDevice);

            //
            SetTransformationMatrices(worldBound, deviceSize);

            m_TexTable = new TextureTable(c);
            m_FontManager = fontManager;
            fontManager.SetDrawHelper(this);

            int CircleSize = 256;
            Texture2D Circle = new Texture2D(graphicsDevice, CircleSize, CircleSize);
            Color[] CircleData = new Color[CircleSize * CircleSize];
            for (int x = 0; x < CircleSize; x++)
                for (int y = 0; y < CircleSize; y++) {
                    if (Math.Sqrt(Math.Pow(x - CircleSize / 2, 2) + Math.Pow(y - CircleSize / 2, 2)) > CircleSize/2) {
                        int off = x + CircleSize * y;
                        CircleData[off] = Color.Black;
                        CircleData[off].A = 0x00;
                        
                    } else
                        CircleData[x + CircleSize * y] = Color.White;
                    }
            Circle.SetData<Color>(CircleData);

            Texture2D Square = new Texture2D(graphicsDevice, 1, 1);
            Color[] SquareData = new Color[1];
            SquareData[0] = Color.White;
            Square.SetData<Color>(SquareData);

            m_TheCircle = Circle;
            m_TheSquare = Square;
        }

        public void SetTransformationMatrices(XNACS1Rectangle worldBound, Vector2 deviceSize)
        {

            Vector2 wMin = worldBound.MinBound;
            Vector2 wMax = worldBound.MaxBound;

            Vector2 size = wMax - wMin;
            Matrix toOrg, toDC;
            toOrg = Matrix.CreateTranslation(-wMin.X, -wMin.Y, 0.0f);
            toDC = Matrix.CreateScale(deviceSize.X / size.X, deviceSize.Y / size.Y, 1.0f);
            mW2D = toOrg * toDC;
            
            m_WorldMin = wMin;
            m_WorldMax = wMax;
            m_DeviceSize = deviceSize;
        }


        public Texture2D FindTexture(String name)
        {
            Texture2D tex = null;
            if (null != name)
            {
                tex = m_TexTable.FindTexture(name);
                if (tex == null)
                {
                    m_TexTable.LoadTexture(name);
                    tex = m_TexTable.FindTexture(name);
                }
            }
            return tex;
        }

        public Texture2D FindTexture(XNACS1Primitive primitive) {
            Texture2D tex = null;
            if (null != primitive.Texture) {
                tex = m_TexTable.FindTexture(primitive.Texture);
                if (tex == null) {
                    m_TexTable.LoadTexture(primitive.Texture);
                    tex = m_TexTable.FindTexture(primitive.Texture);
                }
            }

            if (tex == null) {
                if (primitive.m_Type == XNACS1Primitive.PrimitiveType.PrimitiveCircle)
                    tex = m_TheCircle;
                else
                    tex = m_TheSquare;
            }

            return tex;
        }

        /// <summary>
        /// Rotate the vector-v by the the specified Radians in the clockwise direction
        /// and return the result
        /// </summary>
        /// <param name="v">Vector to be rotated. </param>
        /// <param name="angleInRadian"></param>
        /// <returns></returns>
        internal Vector2 RotateVectorByAngle(Vector2 v, float angleInRadian)
        {
            float sinTheta = (float)(Math.Sin((double)-angleInRadian));
            float cosTheta = (float)(Math.Cos((double)-angleInRadian));
            float x, y;
            x =  cosTheta * v.X + sinTheta * v.Y;
            y = -sinTheta * v.X + cosTheta * v.Y;
            return new Vector2(x, y);
        }


        internal bool TexturesCollided(XNACS1Primitive primitive1, XNACS1Primitive primitive2, out Vector2 collidePos)
        {
            collidePos = Vector2.Zero;

            Texture2D tex1 = m_TexTable.FindTexture(primitive1.Texture);
            Texture2D tex2 = m_TexTable.FindTexture(primitive2.Texture);
            if ((null == tex1) || (null == tex2))
                return false;

            Color[] tex1Color = m_TexTable.FindTextureColor(primitive1.Texture);
            Color[] tex2Color = m_TexTable.FindTextureColor(primitive2.Texture);
            

            bool pixelTouch = false;
            float r1InRadian = MathHelper.ToRadians(primitive1.RotateAngle);
            float r2InRadian = MathHelper.ToRadians(primitive2.RotateAngle);

            Vector2 p1XDir = RotateVectorByAngle(Vector2.UnitX, r1InRadian);
            Vector2 p1YDir = RotateVectorByAngle(Vector2.UnitY, r1InRadian);

            Vector2 p2XDir = RotateVectorByAngle(Vector2.UnitX, r2InRadian);
            Vector2 p2YDir = RotateVectorByAngle(Vector2.UnitY, r2InRadian);

            int tex1Width = tex1.Width;
            int tex1Height = tex1.Height;
            int tex1XOffset = 0;
            int tex1YOffset = 0;

            int tex2Width = tex2.Width;
            int tex2Height = tex2.Height;
            int tex2XOffset = 0;
            int tex2YOffset = 0;

            if (primitive1.UseSpriteSheet)
            {
                tex1Width = primitive1.m_SpriteSourceRect.Width;
                tex1Height = primitive1.m_SpriteSourceRect.Height;
                tex1XOffset = primitive1.m_SpriteSourceRect.X;
                tex1YOffset = primitive1.m_SpriteSourceRect.Y;
            }

            if (primitive2.UseSpriteSheet)
            {
                tex2Width = primitive2.m_SpriteSourceRect.Width;
                tex2Height = primitive2.m_SpriteSourceRect.Height;
                tex2XOffset = primitive2.m_SpriteSourceRect.X;
                tex2YOffset = primitive2.m_SpriteSourceRect.Y;
        }

            int i = 0;
            while ((!pixelTouch) && (i < tex1Width))
            {
                int j = 0;
                while ((!pixelTouch) && (j < tex1Height))
                {
                    collidePos = primitive1.TextureToWorldPos(i, j, tex1Width, tex1Height, p1XDir, p1YDir);
                    Color color1 = tex1Color[tex1XOffset + i + ((j+tex1YOffset) * tex1.Width)];
                    if (color1.A > 0)
                    {
                        int p2I, p2J;
                        primitive2.CameraPosToTexIndex(collidePos, tex2Width, tex2Height, p2XDir, p2YDir, out p2I, out p2J);

                        if ((p2I >= 0) && (p2I < tex2Width) &&
                            (p2J >= 0) && (p2J < tex2Height))
                        {
                            pixelTouch = (tex2Color[tex2XOffset + p2I + ((p2J+tex2YOffset) * tex2.Width)].A > 0);
                        }
                    }
                    j++;
                }
                i++;
            }

            return pixelTouch;
        }


        //private Matrix ComputeXform(Vector2 scale, float rotateInDegree, Vector2 translate, Vector2 pivot) {
        //    Matrix pm, tm, sm, rm;
        //    pm = Matrix.CreateTranslation(-pivot.X, -pivot.Y, 0.0f);
        //    tm = Matrix.CreateTranslation(translate.X + pivot.X, translate.Y + pivot.Y, 0.0f);
        //    rm = Matrix.CreateRotationZ((float)(rotateInDegree * (Math.PI / 180.0f)));
        //    sm = Matrix.CreateScale(scale.X, scale.Y, 1.0f);
        //    return pm * sm * rm * tm;
        //}

        //private void XformVertices(Matrix m, VertexPositionColorTexture[] input, VertexPositionColorTexture[] output)
        //{
        //    for (int i = 0; i < input.Length; i++)
        //    {
        //        // output[i] = input[i];
        //        output[i].Position = Vector3.Transform(input[i].Position, m);
        //        output[i].Position = Vector3.Transform(output[i].Position, mW2D);
        //        output[i].Position.Y = m_DeviceSize.Y - output[i].Position.Y;   // drawing origin is top/left
        //    }
        //}

        private Rectangle World2Device(Vector3 tl, float w, float h)
        {
            Vector3 br = new Vector3(tl.X + w, tl.Y - h, 0f);
            tl = Vector3.Transform(tl, mW2D);
            br = Vector3.Transform(br, mW2D);

            int itlX = (int)(tl.X);
            int itlY = (int)(tl.Y);
            int ibrX = (int)(br.X);
            int ibrY = (int)(br.Y);
            int iw = ibrX - itlX;
            int ih = itlY - ibrY;

            Rectangle r;

                itlY = (int)(m_DeviceSize.Y) - itlY;
                r = new Rectangle(itlX, itlY, iw, ih);

            return r;
        }

        /// <summary>
        /// Compute the top-left position given that XNA sprite rotation is always top-left.
        /// Our rotation is always on the middle of geometry:
        /// 
        /// desiredRotationCenterX + px = topLeft.X, 
        /// desiredRotationCenterY + py = topLeft.Y. 
        /// 
        /// </summary>
        private Vector3 ComputeTopLeft(Vector3 tl, float px, float py, float r)
        {
            double xS = px * Math.Sin(r);
            double xC = px * Math.Cos(r);
            double yS = py * Math.Sin(r);
            double yC = py * Math.Cos(r);
            return new Vector3(tl.X - px + (float)(xC - yS), 
                               tl.Y - py + (float)(xS + yC),
                               0f);
        }

        /// <summary>
        /// Actually draws the primitive onto screen.
        /// </summary>
        /// <param name="dcRec">The dcRec to draw</param>
        /// <param name="texName">Texture name</param>
        /// <param name="useTex">if texName is null, use this texture instead (TheCircle or TheSquare)</param>
        /// <param name="color">color to draw in in the absence of texName</param>
        /// <param name="rotation">to rotate</param>
        /// <param name="sourceRect">Specifies the portion of texture to be drawn</param>
        private void DrawDCRectangle(Rectangle dcRec, Rectangle? sourceRect, String texName, Texture2D useTex, Color color, float rotation) {
            Texture2D tex = FindTexture(texName);
            Color useColor = color;

            if (null == tex) {
                tex = useTex;
            }

            mSpriteBatch.Draw(tex, dcRec, sourceRect, useColor, -rotation, Vector2.Zero, SpriteEffects.None,0);

        }


        /// <summary>
        /// Draws a circle.
        /// </summary>
        /// <param name="center">Center of the circle.</param>
        /// <param name="radius">Radius of the circle.</param>
        /// <param name="rotation">Rotation of the circle (about the center, in degree).</param>
        /// <param name="color">Color of the circle.</param>
        /// <param name="texName">Name of texture (use null for none) for the circle.</param>
        /// <param name="sourceRect">Specifies what region of the texture to draw</param>
        public void DrawCircle(Vector2 center, float radius, Rectangle? sourceRect, float rotation, Color color, String texName) {
            Vector3 topLeft = new Vector3(center.X - radius, center.Y + radius, 0f);

            // convert rotation to radian
            rotation = MathHelper.ToRadians(rotation);

            // desiredRotationCenterX + px = topLeft.X 
            // desiredRotationCenterY + py = topLeft.Y
            topLeft = ComputeTopLeft(topLeft, -radius, radius, rotation);

            float width = 2f * radius;
            Rectangle dcRec = World2Device(topLeft, width, width);

            DrawDCRectangle(dcRec, sourceRect, texName, m_TheCircle, color, rotation);
        }

        /// <summary>
        /// Draws a rotated rectangle.
        /// </summary>
        /// <param name="center">Center of the rectangle.</param>
        /// <param name="xWidthDest">Horizontal width of the rectangle.</param>
        /// <param name="yHeightDest">Vertical height of the rectangle.</param>
        /// <param name="rot">Rotation in degrees.</param>
        /// <param name="color">Color of the rectangle.</param>
        /// <param name="texName">Name of texture (use null for none) for the rectangle.</param>
        ///<param name="sourceRect">Specifies the portion of the texture to be drawn.</param>
        public void DrawRectangle(Vector2 center, float xWidthDest, float yHeightDest, Rectangle? sourceRect, float rot, Color color, String texName) {
            Vector3 topLeft = new Vector3(center.X - (xWidthDest / 2f), center.Y + (yHeightDest / 2f), 0f);

            // convert rotation to rad
            rot = MathHelper.ToRadians(rot);

            // desiredRotationCenterX + px = topLeft.X 
            // desiredRotationCenterY + py = topLeft.Y
            topLeft = ComputeTopLeft(topLeft, -xWidthDest / 2f, yHeightDest / 2f, rot);

            Rectangle dcRec = World2Device(topLeft, xWidthDest, yHeightDest);
            DrawDCRectangle(dcRec, sourceRect, texName, m_TheSquare, color, rot);
        }

        
        public void DrawLineSegments(Vector2 a, Vector2 b)
        {
            //VertexPositionColorTexture[] v = new VertexPositionColorTexture[2];
            //VertexPositionColorTexture[] vo = new VertexPositionColorTexture[2];
            //v[0] = new VertexPositionColorTexture(
            //            new Vector3(a.X, a.Y, 0.0f), Color.White, new Vector2(0.0f, 0.0f));
            //v[1] = new VertexPositionColorTexture(
            //            new Vector3(b.X, b.Y, 0.0f), Color.White, new Vector2(1.0f, 1.0f));
            //Matrix m = ComputeXform(new Vector2(1.0f, 1.0f), 0.0f, new Vector2(0.0f, 0.0f), new Vector2(0.0f, 0.0f));
            //XformVertices(m, v, vo);
            //m_GraphicsDevice.DrawUserPrimitives(PrimitiveType.LineList, vo, 0, 1);
        }

        public void BeginDraw(BlendState blendState)
        {
            m_CurrentBlendState = blendState;
            // mSpriteBatch.Begin(blendMode, SpriteSortMode.Immediate, SaveStateMode.None);
            mSpriteBatch.Begin(SpriteSortMode.Immediate, blendState);
        }

        public void EndDraw()
        {
            mSpriteBatch.End();
        }

        public void SetBlendMode(BlendState blendState)
        {
            if (m_CurrentBlendState != blendState)
            {
                EndDraw();
                BeginDraw(blendState);
            }
        }

        private Vector2 WCToDC(Vector2 wc)
        {
            Vector2 dc = new Vector2();
            Vector2 wcSize = m_WorldMax - m_WorldMin;
            dc.X = (wc.X - m_WorldMin.X) * m_DeviceSize.X / wcSize.X;
            dc.Y = ((wc.Y - m_WorldMin.Y) * m_DeviceSize.Y / wcSize.Y);
            dc.Y = m_DeviceSize.Y - dc.Y;
            return dc;
        }

        public void DrawFontAt(Vector2 wcAt, String msg, Color c, String fontName)
        {
            m_FontManager.DrawFontsAt(WCToDC(wcAt), msg, c, fontName);
        }

        public void DrawFont(SpriteFont font, string msg, Vector2 at, Color c)
        {
            SetBlendMode(BlendState.AlphaBlend);
            mSpriteBatch.DrawString(font, msg, at, c);
        }

        #region draw set support
        public void AddToSet(XNACS1Primitive p) {
            if (null != m_DrawSet) {
                if (!m_DrawSet.Contains(p)) {
                    m_DrawSet.Add(p);
                }
                if (p.ShouldTravel || p.UseSpriteSheetAnimation)
                    if (!m_TravelSet.Contains(p))
                        m_TravelSet.Add(p);
            }
        }
        internal void UpdateTravelSet(XNACS1Primitive p) {
            if (null != m_TravelSet) {
                if (p.ShouldTravel || p.UseSpriteSheetAnimation) {
                    if (!m_TravelSet.Contains(p)) {
                        m_TravelSet.Add(p);
                    }
                }
                else {
                    m_TravelSet.Remove(p);
                }
            }
        }


        public void RemoveFromSet(XNACS1Primitive p) {
            if (null != m_DrawSet) {
                m_DrawSet.Remove(p);
                m_TravelSet.Remove(p);
            }
        }

        public void RemoveAllFromSet()
        {
            if (null != m_DrawSet) {
                m_DrawSet.Clear();
                m_TravelSet.Clear();
            }
        }

        public void TopOfDrawSet(XNACS1Primitive p)
        {
            if (null != m_DrawSet)
            {
                if (IsInDrawSet(p))
                {
                    m_DrawSet.Remove(p);
                    m_DrawSet.Add(p);
                }
            }
        }

        public void DrawAll()
        {
            if (null != m_DrawSet) {
                XNACS1Primitive p;
                for (int i = 0; i < m_DrawSet.Count; i++) {
                    p = m_DrawSet[i];
                    p.Draw();
                }
            }

        }

        public void TravelAll()
        {
            foreach(XNACS1ParticleEmitter emitter in m_EmitterSet) {
                emitter.CallUpdate();
            }

            foreach(XNACS1Primitive p in m_TravelSet) {
                p.TravelPrimitive();
                p.UpdateSpriteSheetAnimation();
            }

        }

        public bool IsInDrawSet(XNACS1Primitive p) {
            return m_DrawSet.Contains(p);
        }

        public int DrawSetSize()
        {
            return m_DrawSet.Count;
        }
        #endregion draw set support


        internal void AddEmitter(XNACS1ParticleEmitter emitter) {
            m_EmitterSet.Add(emitter);
        }
        internal void RemoveEmitter(XNACS1ParticleEmitter emitter) {
            m_EmitterSet.Remove(emitter);
        }
    }
}