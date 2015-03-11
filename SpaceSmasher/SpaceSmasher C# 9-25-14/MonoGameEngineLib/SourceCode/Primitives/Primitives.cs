//*******************************************
// PredefinedEmitter.cs
// Author:
// ChangeLog:
// Samuel Cook and Ron Cook - Added support for sprite sheets
//      Added normal direction for primitives
//*******************************************


using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;


namespace XNACS1Lib
{

    /// <summary>
    /// This is the base class for the behavior of Primitives (circle, rectangle, and primitive-set).
    /// </summary>
   

    public abstract class XNACS1Primitive
    {
        internal static XNACS1LibDrawHelper sm_DrawHelper = null;
        internal static void SetDrawHelper(XNACS1LibDrawHelper h) { sm_DrawHelper = h; }

        #region private primitive type for collision support
        internal enum PrimitiveType
        {
            PrimitiveCircle,
            PrimitiveRectangle,
            PrimitiveEmitter
        }
        #endregion (for private primitive type)

        #region Instance variables and constructor
        internal Color m_Color;
        internal Color m_TextureColor;

        internal Vector2 m_Velocity;
        internal bool m_ShouldTravel;

        internal Vector2 m_Center;
        internal Vector2 m_Size;

        internal float m_Rotate;
        internal Vector2 m_FrontDirection;
        internal Vector2 m_NormalDirection;

        internal PrimitiveType m_Type;
        internal String m_Texture;

        internal String m_LabelFont = null;
        internal String m_Label;
        internal Color m_LabelColor;

        internal bool m_CenterReference;

        internal bool m_Visible;

        // Blend state support: Additive vs AlphaBlend (particle systems)
        internal BlendState m_DrawBlendState = BlendState.AlphaBlend;

        //Sprite Sheet Support
        internal bool m_UseSpriteSheet;
        internal bool m_UseSpriteSheetAnimation;
        internal int m_SpriteSheetColumns;
        internal int m_SpriteSheetRows;
        internal int m_SpriteSheetPadding;
        internal int m_SpriteCurrentFrame;
        internal int m_SpriteBeginFrame;
        internal int m_SpriteEndFrame;
        internal int m_TicksPerFrame;
        internal int m_CurrentFrameTicks;
        internal int m_SpriteFrameInc;
        internal SpriteSheetAnimationMode m_SpriteAnimMode;
        internal Rectangle m_SpriteSourceRect;

        internal XNACS1Primitive(PrimitiveType p, BlendState b, Vector2 c, Vector2 s, String tex)
        {
            Init(p, b, c, s, tex);
        }

        internal XNACS1Primitive(PrimitiveType p, BlendState b)
        {
            Init(p, b, new Vector2(0, 0), new Vector2(1, 1), null);
        }
        #endregion

        #region Private utility functions
        internal void Init(PrimitiveType p, BlendState b, Vector2 c, Vector2 s, String tex) {
            m_DrawBlendState = b;

            m_Center = c;
            m_Size = s;
            m_Velocity = new Vector2(1, 0); // This cannot be zero!! Otherwise Speed will not work!!
            m_ShouldTravel = false;

            m_Type = p;
            m_Rotate = 0.0f; // rotate angle and FrontDirection are tied together in 1-1
            m_FrontDirection = new Vector2(1, 0); // initial front direction for all prtimives is in the positive X-direction
            m_NormalDirection = new Vector2(0, 1);
            m_Texture = tex;

            m_Color = Color.Aquamarine;
            m_TextureColor = Color.White;

            m_Label = null;
            m_LabelColor = Color.Black;

            m_CenterReference = true;

            m_Visible = true;

            //sprite sheet info init
            m_UseSpriteSheet = false;
            m_UseSpriteSheetAnimation = false;
            m_SpriteSheetColumns = 0;
            m_SpriteSheetRows = 0;
            m_SpriteSheetPadding = 0;
            m_SpriteCurrentFrame = -1;
            m_SpriteBeginFrame = 0;
            m_SpriteEndFrame = 0;
            m_TicksPerFrame = 1;
            m_CurrentFrameTicks = 0;
            m_SpriteFrameInc = 1;
            m_SpriteAnimMode = SpriteSheetAnimationMode.AnimateForward;
            m_SpriteSourceRect = Rectangle.Empty;

            if (null != sm_DrawHelper) {
                // null should happen only once, for the system WorldRec
                AddToAutoDrawSet();
            }
        }

        private bool CircleCircle(Vector2 c1, float r1, Vector2 c2, float r2, out Vector2 pos)
        {
            Vector2 d = c1 - c2;
            pos = Vector2.Zero;
            float len = d.Length();
            bool hit = len < (r1 + r2);
            if (hit)
                pos = c1 + 0.5f * d;  // approximated position
            return hit;
        }

        private bool RecRec(Vector2 min1, Vector2 max1, Vector2 min2, Vector2 max2, out Vector2 pos)
        {
            pos = Vector2.Zero;
            if ((min1.X > max2.X) || (min2.X > max1.X))
                return false;

            if ((min1.Y > max2.Y) || (min2.Y > max1.Y))
                return false;

            pos = (min1 + min2 + max1 + max2) * 0.25f;
            return true;
        }

        #endregion

        #region Public interface

        #region support for dimension
        /// <summary>
        /// Get/Set width/height of the primitive.
        /// </summary>
        /// <remarks>For PrimitiveCircle (diameter), PrimitiveRectangle (width/height), PrimitiveSet (undefined)
        /// </remarks>
        /// <seealso cref="SizeX"/>
        /// <seealso cref="SizeY"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        internal protected Vector2 Size
        {
            get { return m_Size; }
            set { m_Size = value; }
        }

        /// <summary>
        /// Width of the primitive. 
        /// </summary>
        /// <remarks>
        /// For 
        /// PrimitiveCircle (diameter), PrimitiveRectangle (width), PrimitiveSet (undefined).
        /// </remarks>
        /// <seealso cref="Size"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        internal protected float SizeX
        {
            get { return m_Size.X; }
            set { m_Size.X = value; }
        }
        /// <summary>
        ///  Height of the primitive. For PrimitiveCircle (diameter), PrimitiveRectangle (height), PrimitiveSet (undefined).
        /// </summary>
        /// <seealso cref="Size"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        internal protected float SizeY
        {
            get { return m_Size.Y; }
            set { m_Size.Y = value; }
        }

        /// <summary>
        /// Center of the primitive. 
        /// </summary>
        /// <remarks>
        /// For PrimitiveCircle (center), PrimitiveRectangle (center), PrimitiveSet (undefined).
        /// </remarks>
        /// <seealso cref="CenterX"/>
        /// <seealso cref="CenterY"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        public Vector2 Center
        {
            get { return m_Center; }
            set
            {
                m_CenterReference = true;
                m_Center = value;
            }
        }

        /// <summary>
        /// X coordinate of the center position of the primitive.
        /// </summary>
        /// <remarks>
        /// For PrimitiveCircle (x-position of center), PrimitiveRectangle (x-position of center), PrimitiveSet (undefined).
        /// </remarks>
        /// <seealso cref="Center"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        public float CenterX
        {
            get { return m_Center.X; }
            set
            {
                m_CenterReference = true;
                m_Center.X = value;
            }
        }

        /// <summary>
        /// Y coordinate of the center position of the primitive.
        /// </summary>
        /// <remarks>
        /// For PrimitiveCircle (y-position of center), PrimitiveRectangle (y-position of center), PrimitiveSet (undefined).
        /// </remarks>
        /// <seealso cref="Center"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        public float CenterY
        {
            get { return m_Center.Y; }
            set
            {
                m_CenterReference = true;
                m_Center.Y = value;
            }
        }

        /// <summary>
        /// Lower left corner of the Primitive.
        /// </summary>
        /// <seealso cref="MaxBound"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        public Vector2 MinBound
        {
            get { return m_Center - (m_Size * 0.5f); }
        }

        /// <summary>
        /// Upper left corner of the primitive.
        /// </summary>
        /// <seealso cref="MinBound"/>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        public Vector2 MaxBound
        {
            get { return m_Center + (m_Size * 0.5f); }
        }

        #endregion

        #region support for velocity and travel

        /// <summary>
        /// Defines if the primitive should move by its current velocity. Defaults to false.
        /// </summary>
        /// <remarks>Must be set to true for a primitive to move.</remarks>
        /// <seealso cref="Velocity"/>
        public bool ShouldTravel
        {
            get { return m_ShouldTravel; }
            set {
                m_ShouldTravel = value;
                sm_DrawHelper.UpdateTravelSet(this);
            }
        }

        /// <summary>
        /// Velocity of a primitive. This is a true velocity: with magnitude (speed) and direction for tavel.
        /// </summary>
        /// <remarks>
        /// Primitive with non-zero velocity will move only when ShouldTravel is set to true.
        /// </remarks>
        /// <seealso cref="ShouldTravel"/>
        /// <seealso cref="VelocityX"/>
        /// <seealso cref="VelocityY"/>
        /// <seealso cref="VelocityDirection"/>
        /// <seealso cref="Speed"/>
        public Vector2 Velocity
        {
            get { return m_Velocity; }
            set { m_Velocity = value; }
        }

        /// <summary>
        /// Get/Set the x-component of the primitive velocity.
        /// </summary>
        /// <remarks>
        /// Primitive with non-zero velocity will move only when ShouldTravel is set to true.
        /// </remarks>
        /// <seealso cref="Velocity"/>
        /// <seealso cref="ShouldTravel"/>
        public float VelocityX
        {
            get { return m_Velocity.X; }
            set { m_Velocity.X = value; }
        }

        /// <summary>
        /// Get/Set the y-component of the primitive velocity.
        /// </summary>
        /// <remarks>
        /// Primitive with non-zero velocity will move only when ShouldTravel is set to true.
        /// </remarks>
        /// <seealso cref="Velocity"/>
        /// <seealso cref="ShouldTravel"/>
        public float VelocityY
        {
            get { return m_Velocity.Y; }
            set { m_Velocity.Y = value; }
        }

        /// <summary>
        /// Get/Set the speed (magnitude of the Velocity vector) for the velocity.
        /// </summary>
        /// <remarks>The direction which the primitive is traveling 
        /// will not change, just how fast it is moving will be updated.
        /// Primitive with non-zero velocity will move only when ShouldTravel is set to true.
        /// </remarks>
        /// <seealso cref="Velocity"/>
        /// <seealso cref="VelocityDirection"/>
        /// <seealso cref="ShouldTravel"/>
        public float Speed
        {
            get { return m_Velocity.Length(); }
            set
            {
                m_Velocity.Normalize();
                m_Velocity *= value;
            }
        }

        /// <summary>
        /// Get/Set the direction of the velocity. 
        /// </summary>
        /// <remarks>The speed upon which the primitive travels will not change,
        /// only the direction will be updated.
        /// Primitive with non-zero velocity will move only when ShouldTravel is set to true.
        /// </remarks>
        /// <seealso cref="ShouldTravel"/>
        /// <seealso cref="Velocity"/>
        /// <seealso cref="Speed"/>
        public Vector2 VelocityDirection
        {
            get
            {
                Vector2 dir = m_Velocity;
                dir.Normalize();
                return dir;
            }
            set
            {
                float s = m_Velocity.Length();
                s = (s == 0f ? 1f : s);
                value.Normalize();
                m_Velocity = value * s;
            }
        }

        /// <summary>
        /// Retruns if the current velocity is non-zero.
        /// </summary>
        /// <remarks>When a primitive is moving very slowly (speed at 1/100,000 of world dimension), its velocity will
        /// be set to zero. This avoids final "jittering" of primitives that are moving very slowly.</remarks>
        /// <returns>True, if velocity is non-zero, otherwise, returns false.</returns>
        public bool HasNonZeroVelocity()
        {
            const float kSmallLimit = 100000f; // kSmallLimit*size should > worldSize
            Vector2 v = Velocity * kSmallLimit;
            if (Math.Abs(v.X) < XNACS1Base.World.WorldDimension.X)
                VelocityX = 0f;

            if (Math.Abs(v.Y) < XNACS1Base.World.WorldDimension.Y)
                VelocityY = 0f;
            return (VelocityX != 0f) || (VelocityY != 0f);
        }
        #endregion

        #region Public Accessors
        /// <summary>
        /// Rotate the primitive by the Angle specified (in degree). After the rotation, the FrontDirection of the primitive
        /// will be udpated accordingly. By default, when RotateAngle is 0, FrontDireciton points towards (1,0) [positive X-direction].
        /// E.g., if we set RotateAngle to 90-degrees, FrontDireciton will be updated accordinlgy to (0,1) [positive y-direciton], or
        /// rotated 90-degress in the anit-clockwise direction
        /// <seealso cref="FrontDirection"/>
        /// </summary>
        public float RotateAngle
        {
            get { return m_Rotate; }
            set
            {
                m_Rotate = value;
                double rad = m_Rotate * Math.PI / 180.0;
                m_FrontDirection.X =  (float)Math.Cos(rad);
                m_FrontDirection.Y =  (float)Math.Sin(rad);
                m_NormalDirection.X = -m_FrontDirection.Y;
                m_NormalDirection.Y = m_FrontDirection.X;
            }
        }

        /// <summary>
        /// Sets the front direction of the primitive. Setting FrontDireciton will cause the RotateAngle and NormalDirection of the primitive to be updated
        /// accordingly. y default, when RotateAngle is 0, FrontDireciton points towards (1,0) [positive X-direction].
        /// E.g., if we set RotateAngle to 90-degrees, FrontDireciton will be updated accordinlgy to (0,1) [positive y-direciton], or
        /// rotated 90-degress in the anit-clockwise direction
        /// <seealso cref="RotateAngle"/>
        /// <seealso cref="NormalDirection"/>
        /// </summary>
        public Vector2 FrontDirection
        {
            get { return m_FrontDirection; }
            set
            {
                float len = value.Length();
                if (len > 0.00001f)
                {
                    m_FrontDirection = value * (1f / len);
                    m_NormalDirection.X = -m_FrontDirection.Y;
                    m_NormalDirection.Y = m_FrontDirection.X;
                    double theta = Math.Atan2(m_FrontDirection.Y, m_FrontDirection.X);
                    m_Rotate = (float)(theta * 180f / Math.PI);
                }
            }
        }
        
        /// <summary>
        /// Get or Set the normal direction of the primitive.  Setting NormalDirection will update RotateAngle and FrontDirection accordingly.
        /// <seealso cref="RotateAngle"/>
        /// <seealso cref="FrontDirection"/>
        /// </summary>
        public Vector2 NormalDirection {
            get { return m_NormalDirection; }
            set {
                float len = value.Length();
                if (len > 0.00001f) {
                    m_NormalDirection = value * (1f / len);
                    m_FrontDirection.X = m_NormalDirection.Y;
                    m_FrontDirection.Y = -m_NormalDirection.X;
                    double theta = Math.Atan2(m_FrontDirection.Y, m_FrontDirection.X);
                    m_Rotate = (float)(theta * 180f / Math.PI);
                }
            }
        }

        /// <summary>
        /// Get or Set Font used for primitive label. If null, default font will be used
        /// </summary>
        public String LabelFont
        {
            set
            {
                m_LabelFont = value;
            }
            get { return m_LabelFont; }
        }
        #endregion Public Accessors

        #region Sprite Sheet Support

        /// <summary>
        /// Allow the primitive to draw as a sprite sheet.
        /// <seealso cref="SetTextureSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="CurrentSpriteSheetFrameX"/>
        /// <seealso cref="CurrentSpriteSheetFrameY"/>
        /// </summary>
        public bool UseSpriteSheet {
            get { return m_UseSpriteSheet; }
            set { m_UseSpriteSheet = value; }
        }

        /// <summary>
        /// Allow the primitive to follow a specified sprite sheet animation sequence.
        /// <seealso cref="SetTextureSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheet"/>
        /// <seealso cref="CurrentSpriteSheetFrameX"/>
        /// <seealso cref="CurrentSpriteSheetFrameY"/>
        /// </summary>
        public bool UseSpriteSheetAnimation {
            get { return m_UseSpriteSheetAnimation; }
            set {
                m_UseSpriteSheetAnimation = value;
                sm_DrawHelper.UpdateTravelSet(this);
            }
        }

        /// <summary>
        /// Gets or Sets the current frame for primitives sprite sheet.
        /// <seealso cref="SetTextureSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="UseSpriteSheet"/>
        /// </summary>
        private int CurrentSpriteSheetFrame {
            get { return m_SpriteCurrentFrame; }
            set {
                //update the source rect for drawing
                m_SpriteCurrentFrame = value;
                if (m_SpriteSheetColumns > 0) {
                    int col = value % m_SpriteSheetColumns;
                    int row = value / m_SpriteSheetColumns;
                    if (row <= m_SpriteSheetRows && row >= 0) {
                        m_SpriteSourceRect.X = m_SpriteSheetPadding * (col + 1) + m_SpriteSourceRect.Width* col;
                        m_SpriteSourceRect.Y = m_SpriteSheetPadding * (row + 1) + m_SpriteSourceRect.Height * row;
                    }
                }
            }
        }

        /// <summary>
        /// Gets or sets the zero-based current sprite column for sprite sheet drawing.
        /// <seealso cref="SetTextureSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="UseSpriteSheet"/>
        /// <seealso cref="CurrentSpriteSheetFrameY"/>
        /// </summary>
        public int CurrentSpriteSheetFrameX {
            get { return m_SpriteCurrentFrame % m_SpriteSheetColumns; }
            set {
                int col = value;
                int row = CurrentSpriteSheetFrame / (m_SpriteSheetColumns + 1);
                CurrentSpriteSheetFrame = row * m_SpriteSheetColumns + col;
            }
        }

        /// <summary>
        /// Gets or sets the zero-based current sprite row for sprite sheet drawing.
        /// <seealso cref="SetTextureSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="UseSpriteSheet"/>
        /// <seealso cref="CurrentSpriteSheetFrameX"/>
        /// </summary>
        public int CurrentSpriteSheetFrameY {
            get { return m_SpriteCurrentFrame / m_SpriteSheetColumns; }
            set {
                int col = CurrentSpriteSheetFrame % m_SpriteSheetColumns;
                int row = value;
                CurrentSpriteSheetFrame = row * m_SpriteSheetColumns + col;
            }
        }

        /// <summary>
        /// Defines a texture specified for a primitive to behave like a sprite sheet with the specified number of sprites.  
        /// The default sprite to be drawn is the top left sprite in the sheet.  
        /// Example: The texture for a given primitive has 16 sprites in it, arranged in a 4 by 4 pattern.  By default, all 16 sprites will be displayed, stretched to fit the primitive.  
        /// Calling this method allows you to specify the number of sprites in the texture.
        /// <param name="SpriteSheetTexture">The texture of the sprite sheet to be used.</param>
        /// <param name="numColumns">The number of columns in the sprite sheet (number of sprites in a single row). </param>
        /// <param name="numRows">Number of rows in the sprite sheet.  </param>
        /// <param name="padding">The padding between sprites and on the top and left edge.</param>
        /// <seealso cref="UseSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="CurrentSpriteSheetFrameY"/>
        /// <seealso cref="CurrentSpriteSheetFrameX"/>
        /// </summary>
        public void SetTextureSpriteSheet(string SpriteSheetTexture, int numColumns, int numRows, int padding) {
            m_Texture = SpriteSheetTexture;
            Texture2D t = sm_DrawHelper.FindTexture(m_Texture);
            if (null != t) {
                m_SpriteSheetColumns = numColumns;
                m_SpriteSheetRows = numRows;
                m_SpriteSheetPadding = padding;
                m_SpriteSourceRect.Width = t.Width / numColumns;
                m_SpriteSourceRect.Height = t.Height / numRows;
                CurrentSpriteSheetFrame = 0;
            }
        }

        /// <summary>
        /// Defines a texture specified for a primitive to behave like a sprite sheet with the specified number of sprites.  
        /// The default sprite to be drawn is the top left sprite in the sheet.  
        /// <param name="SpriteSheetTexture">The texture of the sprite sheet to be used.</param>
        /// <param name="spriteWidth">The pixel width of an individual sprite within a spritesheet.</param>
        /// <param name="spriteHeight">The pixel height of an individual sprite within a spritesheet.</param>
        /// <seealso cref="UseSpriteSheet"/>
        /// <seealso cref="SetTextureSpriteAnimationFrames"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="CurrentSpriteSheetFrameY"/>
        /// <seealso cref="CurrentSpriteSheetFrameX"/>
        /// </summary>
        public void SetTextureSpriteSheet(string SpriteSheetTexture, int spriteWidth, int spriteHeight)
        {
            m_Texture = SpriteSheetTexture;
            Texture2D t = sm_DrawHelper.FindTexture(m_Texture);
            if (null != t)
            {
                m_SpriteSourceRect.Width = spriteWidth;
                m_SpriteSourceRect.Height = spriteHeight;
                m_SpriteSheetColumns = t.Width / spriteWidth;
                m_SpriteSheetRows = t.Height / spriteHeight;
                m_SpriteSheetPadding = 0;
                CurrentSpriteSheetFrame = 0;
            }
        }

        /// <summary>
        /// Sets animation parameters for a sprite sheet primitive.
        /// <param name="beginColumn">Zero-based first frame column for the beginning sprite in the animation sequence.</param>
        /// <param name="beginRow">Zero-based first frame row for the beginning sprite in the animation sequence.</param>
        /// <param name="endColumn">Zero-based first frame column for the ending sprite in the animation sequence.</param>
        /// <param name="endRow">Zero-based first frame row for the ending sprite in the animation sequence.</param>
        /// <param name="ticksPerFrame">How many ticks the animation will pause for on each frame.</param>
        /// <param name="mode">The behavior of the animation loop.</param>
        /// <seealso cref="SetTextureSpriteSheet"/>
        /// <seealso cref="UseSpriteSheet"/>
        /// <seealso cref="UseSpriteSheetAnimation"/>
        /// <seealso cref="CurrentSpriteSheetFrameX"/>
        /// <seealso cref="CurrentSpriteSheetFrameY"/>
        /// </summary>
        public void SetTextureSpriteAnimationFrames(int beginColumn, int beginRow, int endColumn, int endRow, int ticksPerFrame, SpriteSheetAnimationMode mode) {
            if (ticksPerFrame < 1) ticksPerFrame = 1;
            m_SpriteBeginFrame = beginRow * m_SpriteSheetColumns + beginColumn;
            m_SpriteEndFrame = endRow * m_SpriteSheetColumns + endColumn;

            int tmp = m_SpriteBeginFrame;
            if (m_SpriteBeginFrame > m_SpriteEndFrame) {
                m_SpriteBeginFrame = m_SpriteEndFrame;
                m_SpriteEndFrame = tmp;
            }

            CurrentSpriteSheetFrame = m_SpriteBeginFrame;

            m_TicksPerFrame = ticksPerFrame;
            m_CurrentFrameTicks = 0;
            m_SpriteAnimMode = mode;
            m_SpriteFrameInc = 1;
            if (mode == SpriteSheetAnimationMode.AnimateBackward ||
                mode == SpriteSheetAnimationMode.AnimateBackwardThenStop)
            {
                m_SpriteFrameInc = -1;
                CurrentSpriteSheetFrame = m_SpriteEndFrame -1;
            }
                
        }

        /// <summary>
        /// Updates the animation for a primitive if UseSpriteSheetAnimation AND UseSpriteSheet.
        /// Automatically called for all primitives in draw set.
        /// </summary>
        internal void UpdateSpriteSheetAnimation() {
            if (UseSpriteSheetAnimation && UseSpriteSheet) {
                m_CurrentFrameTicks++;
                if (m_CurrentFrameTicks >= m_TicksPerFrame) {
                    m_CurrentFrameTicks = 0;
                    CurrentSpriteSheetFrame += m_SpriteFrameInc;
                }
                switch (m_SpriteAnimMode) {
                    case SpriteSheetAnimationMode.AnimateForward:
                        if (m_SpriteCurrentFrame > m_SpriteEndFrame)
                            CurrentSpriteSheetFrame = m_SpriteBeginFrame;
                        break;
                    case SpriteSheetAnimationMode.AnimateForwardThenStop:
                        if (m_SpriteCurrentFrame > m_SpriteEndFrame)
                        {
                            CurrentSpriteSheetFrame = m_SpriteEndFrame;
                            m_SpriteFrameInc = 0;
                        } 
                        break;
                    case SpriteSheetAnimationMode.AnimateBackward:
                        if (m_SpriteCurrentFrame < m_SpriteBeginFrame)
                            CurrentSpriteSheetFrame = m_SpriteEndFrame;
                        break;
                    case SpriteSheetAnimationMode.AnimateBackwardThenStop:
                        if (m_SpriteCurrentFrame < m_SpriteBeginFrame)
                        {
                            CurrentSpriteSheetFrame = m_SpriteBeginFrame;
                            m_SpriteFrameInc = 0;
                        }
                        break;
                    case SpriteSheetAnimationMode.AnimateSwing:
                        if (m_SpriteCurrentFrame > m_SpriteEndFrame) {
                            m_SpriteFrameInc = -1;
                            CurrentSpriteSheetFrame = m_SpriteEndFrame - 1;
                        }
                        else
                            if (m_SpriteCurrentFrame < m_SpriteBeginFrame) {
                                m_SpriteFrameInc = 1;
                                CurrentSpriteSheetFrame = m_SpriteBeginFrame + 1;
                            }

                        if (m_SpriteCurrentFrame < m_SpriteBeginFrame)
                            CurrentSpriteSheetFrame = m_SpriteBeginFrame;
                        if (m_SpriteCurrentFrame > m_SpriteEndFrame)
                            CurrentSpriteSheetFrame = m_SpriteEndFrame;
                        break;
                }
            }
        }


        #endregion Sprite Sheet Support

        #region support drawing (color, visibility, label, draw set manipulation)
        /// <summary>
        /// Whether the primitive is visible.
        /// </summary>
        public bool Visible
        {
            get { return m_Visible; }
            set { m_Visible = value; }
        }

        /// <summary>
        /// Texture file name (without the "." or the extension) to cover the primitive.
        /// </summary>
        /// <remarks>When texture file name is valid, Color inforamtion is ignored.</remarks>
        /// <seealso cref="Color"/>
        public String Texture
        {
            set { m_Texture = value; }
            get { return m_Texture; }
        }

        /// <summary>
        /// Annotated text label to be associated with the primitive.
        /// </summary>
        /// <seealso cref="LabelColor"/>
        public String Label
        {
            set { m_Label = value; }
            get { return m_Label; }
        }

        /// <summary>
        /// Color of the anontated text label.
        /// </summary>
        /// <seealso cref="Label"/>
        public Color LabelColor
        {
            set { m_LabelColor = value; }
            get { return m_LabelColor; }
        }

        /// <summary>
        /// Color of the primitive. (undefined for PrimitiveSet).
        /// </summary>
        /// <remarks>When texture file name is valid, Color inforamtion is ignored.</remarks>
        /// <seealso cref="Color"/>
        /// <seealso cref="Texture"/>
        public Color Color
        {
            get { return m_Color; }
            set { m_Color = value; }
        }

        /// <summary>
        /// If a primitive has a texture assigned, this will change the tint and or opacity 
        /// of the texture when drawn, Color.White is no tint or opacity.
        /// </summary>
        public Color TextureTintColor {
            get { return m_TextureColor; }
            set { m_TextureColor = value; }
        }

        /// <summary>
        /// Draws the primitive.
        /// </summary>
        protected abstract void DrawPrimitive();

        /// <summary>
        /// If visible, draws the primitive with the associated text label.
        /// </summary>
        /// <seealso cref="Label"/>
        public void Draw()
        {
            if (Visible)
            {
                sm_DrawHelper.SetBlendMode(m_DrawBlendState);
                DrawPrimitive();
                sm_DrawHelper.DrawFontAt(Center, m_Label, m_LabelColor, m_LabelFont);

                // For debugging.
                // OBB2D.Draw(this, sm_DrawHelper);
            }
        }

        /// <summary>
        /// Add this primitive to the AutoDrawSet. If this primitive already exists in the draw set,
        /// this function call is ignored. By default, all primitive are added into the AutoDrawSet.
        /// </summary>
        /// <seealso cref="TopOfAutoDrawSet"/>
        /// <seealso cref="IsInAutoDrawSet"/>
        /// <seealso cref="RemoveFromAutoDrawSet"/>
        public virtual void AddToAutoDrawSet()
        {
            sm_DrawHelper.AddToSet(this);
        }

        /// <summary>
        /// Remove this primitive from the AutoDrawSet. After this function call, this primitive will not
        /// be drawn in the application window.
        /// </summary>
        /// <seealso cref="AddToAutoDrawSet"/>
        /// <seealso cref="TopOfAutoDrawSet"/>
        /// <seealso cref="IsInAutoDrawSet"/>
        public virtual void RemoveFromAutoDrawSet()
        {
            sm_DrawHelper.RemoveFromSet(this);
        }

        /// <summary>
        /// Make sure this primitive is drawn on top of all other primitives.
        /// </summary>
        /// <seealso cref="AddToAutoDrawSet"/>
        /// <seealso cref="IsInAutoDrawSet"/>
        /// <seealso cref="RemoveFromAutoDrawSet"/>
        public virtual void TopOfAutoDrawSet()
        {
            sm_DrawHelper.TopOfDrawSet(this);
        }

        /// <summary>
        /// Determines if this primitive is in the AutoDrawSet.
        /// </summary>
        /// <returns>True: if this primitive is in the AutoDrawSet, otherwise returns false.</returns>
        /// <seealso cref="AddToAutoDrawSet"/>
        /// <seealso cref="TopOfAutoDrawSet"/>
        /// <seealso cref="RemoveFromAutoDrawSet"/>
        public virtual bool IsInAutoDrawSet()
        {
            return sm_DrawHelper.IsInDrawSet(this);
        }

        #endregion drawing support


        #region Support for Update
        /// <summary>
        /// Moves the primitive during each update cycle. This function is automacially called for all visible 
        /// primitives. Default behavior is to move the primitive if:
        ///     . Primitive is visible (invivislble primitive is not updated)
        ///     . ShouldTravel is true
        ///     . HasNonZeroVelocity() is true 
        /// <seealso cref="Visible"/>
        /// <seealso cref="ShouldTravel"/>
        /// <seealso cref="HasNonZeroVelocity"/>
        /// </summary>
        public virtual void TravelPrimitive() {
            if (Visible && ShouldTravel) {
                if (HasNonZeroVelocity()) {
                    m_Center += Velocity;
                }
            }
        }
        #endregion Support for update
        #region Support for collision

        internal Vector2 TextureToWorldPos(int texI, int texJ, int texWidth, int texHeight, Vector2 xDir, Vector2 yDir)
        {
            float xFraction = texI * SizeX / (float)(texWidth - 1);
            float yFraction = texJ * SizeY / (float)(texHeight - 1);

            return Center + ((xFraction - (SizeX * 0.5f)) * xDir) - ((yFraction - (SizeY * 0.5f)) * yDir);
        }

        internal void CameraPosToTexIndex(Vector2 cameraPos, int texWidth, int texHeight, Vector2 xDir, Vector2 yDir, out int i, out int j)
        {
            Vector2 delta = cameraPos - Center;
            float xOffset = Vector2.Dot(delta, xDir);
            float yOffset = Vector2.Dot(delta, yDir);
            i = (int) (texWidth * (xOffset / SizeX));
            j = (int) (texHeight * (yOffset / SizeY));
            i += texWidth / 2;
            j = (texHeight / 2) - j;
        }

        /// <summary>
        /// Pixel Accurate collision. This function is extremely sensitive to texture resolution.
        /// Should ALWASY have the low res primitive collide with high res primitive.
        /// e.g., primA is 60x60 and PrimB is 1000x1000
        /// primA.TextureCollided(primB) is O(60x60)
        /// primB.TextureCollided(primA) is O(1000x1000)
        /// Use with care!!
        /// </summary>
        /// <param name="otherPrimitive">Higher resolution texture map</param>
        /// <param name="collisionPt">collided position</param>
        /// <returns></returns>
        public bool TextureCollided(XNACS1Primitive otherPrimitive, out Vector2 collisionPt) {
            collisionPt = Vector2.Zero;

            if ((null == otherPrimitive.Texture) || (null == Texture))
                return false; // no texture, cannot perform pixel-accurate intersection

            if (Collided(otherPrimitive, out collisionPt)) { //texutres may be collided
                return sm_DrawHelper.TexturesCollided(this, otherPrimitive, out collisionPt);
            }

            return false;
        }



        /// <summary>
        /// Determines if this primitive collides with the otherPrimitive. If true, pos is the colliding position.
        /// </summary>
        /// <param name="otherPrimitive">the other primitive.</param>        
        /// <param name="pos">If two primitives collide, this is the "average" of all the possible colliding positions.</param>
        /// <returns>True: if the two primitives has collided. Pos is the approximated colliding position.
        /// False: if the two primitives do not collide, (Pos is undefined in this case).
        /// </returns>
        public bool Collided(XNACS1Primitive otherPrimitive, out Vector2 pos)
        {
            if ((m_Type == PrimitiveType.PrimitiveCircle) &&
                 (otherPrimitive.m_Type == PrimitiveType.PrimitiveCircle))
            {
                return (CircleCircle(m_Center, SizeX * 0.5f, otherPrimitive.Center, otherPrimitive.SizeX * 0.5f, out pos));
            }
            else
            {
                if ((m_Rotate == 0.0f) && (otherPrimitive.m_Rotate == 0.0f))
                {
                    return (RecRec(MinBound, MaxBound, otherPrimitive.MinBound, otherPrimitive.MaxBound, out pos));
                }
                else
                {
                    return (OBB2D.OBB2DIntersect(this, otherPrimitive, out pos));
                }
            }
        }

        /// <summary>
        /// Determines if this primitive collides with otherPrimitive.
        /// </summary>
        /// <param name="otherPrimitive">the other primitive.</param>        
        /// <returns>True: if the two primitives has collided.
        /// False: if the two primitives do not collide.
        /// </returns>
        public bool Collided(XNACS1Primitive otherPrimitive)
        {
            Vector2 pos = Vector2.Zero;
            return Collided(otherPrimitive, out pos);
        }

        #endregion  collision support


        #region support for relative position (above/below, etc.)

        /// <summary>
        /// Returns if the center of this primitive is "above" (in y) in relation to the center of otherPrimitive.
        /// </summary>
        /// <param name="otherPrimitive">the other primitive.</param>
        /// <returns>True: if the center's y value of this primitive is greater than the 
        /// y value of the max bound of otherPrimitive, returns
        /// false otherwise.</returns>
        public bool Above(XNACS1Primitive otherPrimitive)
        {
            return (Center.Y > otherPrimitive.MaxBound.Y);
        }

        /// <summary>
        /// Returns if the center of this primitive is "below" (in y) in relation to the center of otherPrimitive.
        /// </summary>
        /// <param name="otherPrmitive">the other primitive.</param>
        /// <returns>True: if the center's y value of this primitive is smaller than the 
        /// y value of the min bound of otherPrimitive, returns
        /// false otherwise.</returns>
        public bool Below(XNACS1Primitive otherPrmitive)
        {
            return (Center.Y < otherPrmitive.MinBound.Y);
        }

        /// <summary>
        /// Returns if the center of this primitive is "to the left" (in x) in relation to the center of otherPrimtive
        /// </summary>
        /// <param name="otherPrimitive">the other primitive</param>
        /// <returns>True: if the center's x value of this primitive is smaller than the 
        /// x value of the min bound of otherPrimitive</returns>
        public bool LeftOf(XNACS1Primitive otherPrimitive)
        {
            return (Center.X < otherPrimitive.MinBound.X);
        }

        /// <summary>
        /// Returns if the center of this primitive is "to the right" (in x) in relation to the center of otherPrimitive
        /// </summary>
        /// <param name="otherPrimitive">the other primitive</param>
        /// <returns>True: if the center's x value of this primitive is larger than the
        /// x value of the max bound of the otherPrimitive.</returns>
        public bool RightOf(XNACS1Primitive otherPrimitive)
        {
            return (Center.X > otherPrimitive.MaxBound.X);
        }
        #endregion (for public interface)

        #endregion public utilitites
    }
}