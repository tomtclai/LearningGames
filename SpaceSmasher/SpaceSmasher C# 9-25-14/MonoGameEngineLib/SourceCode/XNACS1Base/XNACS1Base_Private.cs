//*******************************************
// PredefinedEmitter.cs
// Author:
// ChangeLog
// Samuel Cook and Ron Cook - Added enums for particle travel mode and sprite sheet animation
//  Added public "TicksInASecond" so users could define their own frame rate.
//*******************************************

using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

#if WINDOWS_PHONE
using System.Windows; // for Deployment
#endif


namespace XNACS1Lib
{

    public partial class XNACS1Base : Microsoft.Xna.Framework.Game
    {       
        #region internal utility functions: ConstructorInit() CreateDrawHelper() SetDeviceSize(fullScreen, W, H)

        internal void ConstructorInit()
        {
            mAudioManager = new SoundManager(Content);

            m_TopStatus = null;
            m_BottomStatus = null;

            this.IsFixedTimeStep = true;
            this.TargetElapsedTime = new TimeSpan(0, 0, 0, 0, 25);

            m_GraphicsAccess = this;
            m_GraphicsManager = new GraphicsDeviceManager(this);

        }


        internal void CreateDrawHelper()
        {
            // World to NDC transform
            float height;
            height = (DrawViewHeight / DrawViewWidth) * RequestedWorldWidth;

            Vector2 min, max;
            min = new Vector2(RequestedWorldMin.X, RequestedWorldMin.Y);
            max = new Vector2((min.X + RequestedWorldWidth), (min.Y + height));
            Vector2 size = max - min;
            Vector2 center = min + (size * 0.5f);
            m_WorldBound.Center = center;
            m_WorldBound.Width = size.X;
            m_WorldBound.Height = size.Y;
            if (null == m_DrawHelper)
            {
                m_DrawHelper = new XNACS1LibDrawHelper(m_GraphicsManager.GraphicsDevice, Content, m_WorldBound, m_FontManager, new Vector2(XNACS1_AppWindowWidth, XNACS1_AppWindowHeight));
                XNACS1Primitive.SetDrawHelper(m_DrawHelper);
            }
            else
            {
                // update the transformation matrices
                m_DrawHelper.SetTransformationMatrices(m_WorldBound, new Vector2(XNACS1_AppWindowWidth, XNACS1_AppWindowHeight));
            }
        }

        private void SetDeviceSize(bool fullScreen, int widthInPixel, int heightInPixel)
        {
            mDeviceSizeSet = true;

            XNACS1_AppWindowWidth = widthInPixel;
            XNACS1_AppWindowHeight = heightInPixel;

            m_GraphicsManager.IsFullScreen = fullScreen;

#if WINDOWS_PHONE
            XNACS1_AppWindowWidth = GraphicsDevice.Viewport.Width;
            XNACS1_AppWindowHeight = GraphicsDevice.Viewport.Height;
#else

            if (fullScreen)
            {
                // XBOX and PC
                XNACS1_AppWindowWidth = GraphicsDevice.DisplayMode.Width;
                XNACS1_AppWindowHeight = GraphicsDevice.DisplayMode.Height;

                m_GraphicsManager.PreferredBackBufferWidth = XNACS1_AppWindowWidth;
                m_GraphicsManager.PreferredBackBufferHeight = XNACS1_AppWindowHeight;
            }
            else
            {
                //float maxWidth = MathHelper.Min(widthInPixel, GraphicsDevice.DisplayMode.Width);
#if WINDOWS
                    // PC
                //maxWidth = maxWidth * 0.95f;
#endif
                //XNACS1_AppWindowWidth = (int)maxWidth;
                //XNACS1_AppWindowHeight = (int)((maxWidth / kHDTVAspecRatio) + 0.5f);

                Rectangle bounds = new Rectangle(0, 0, XNACS1_AppWindowWidth, XNACS1_AppWindowHeight);
                m_GraphicsManager.PreferredBackBufferWidth = XNACS1_AppWindowWidth;
                m_GraphicsManager.PreferredBackBufferHeight = XNACS1_AppWindowHeight;
                m_GraphicsManager.GraphicsDevice.PresentationParameters.BackBufferWidth = m_GraphicsManager.PreferredBackBufferWidth;
                m_GraphicsManager.GraphicsDevice.PresentationParameters.BackBufferHeight = m_GraphicsManager.PreferredBackBufferHeight;
                m_GraphicsManager.GraphicsDevice.Viewport = new Viewport(bounds);

            }
#endif
            m_GraphicsManager.ApplyChanges();

            UseAspectRatio = (float)XNACS1_AppWindowWidth / (float)XNACS1_AppWindowHeight;

            RequestedWorldMin = new Vector2(DefaultWorldMinX, DefaultWorldMinY);
            RequestedWorldWidth = DefualtWorldWidth;

            DrawViewHeight = XNACS1_AppWindowHeight;
            DrawViewWidth = XNACS1_AppWindowWidth;
        }
        #endregion
    }
}