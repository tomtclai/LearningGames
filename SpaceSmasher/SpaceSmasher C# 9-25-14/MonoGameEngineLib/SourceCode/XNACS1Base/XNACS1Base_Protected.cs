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
     
        #region override and hide all the methods from XNA.Framework

        /// <summary>
        /// <b>Do not override this method!</b> This method is used by the XNACS1Lib to create
        /// and initialize the entire system.
        /// 
        /// Override InitializeWorld() and perform all your intialization there!
        /// 
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            bool fullScreen = false;
#if WINDOWS_PHONE
            // Phone
            m_GraphicsAccess.m_GraphicsManager.PreferredBackBufferWidth = m_GraphicsAccess.GraphicsDevice.DisplayMode.Width;
            m_GraphicsAccess.m_GraphicsManager.PreferredBackBufferHeight = m_GraphicsAccess.GraphicsDevice.DisplayMode.Height;
            m_GraphicsAccess.m_GraphicsManager.SupportedOrientations = DisplayOrientation.Portrait;
            m_GraphicsAccess.m_GraphicsManager.ApplyChanges();
            fullScreen = true;
#else
#if WINDOWS
            // Windows
            int w = (int) (m_GraphicsAccess.GraphicsDevice.DisplayMode.Width * kDefaultWidthCoverage);
            if (w < XNACS1_AppWindowWidth)
            {
                XNACS1_AppWindowWidth = w;
                XNACS1_AppWindowHeight = (int) (XNACS1_AppWindowWidth/kHDTVAspecRatio);
            }
#else
            // XBOX
            fullScreen = true;
#endif
#endif
            
            m_WorldBound = new XNACS1Rectangle(RequestedWorldMin, RequestedWorldWidth, RequestedWorldWidth);
            m_WorldBound.Color = Color.CornflowerBlue;

            m_GraphicsAccess.m_FontManager = new FontManager(m_GraphicsManager.GraphicsDevice, Content, XNACS1_AppWindowHeight);
            m_GraphicsAccess.CreateDrawHelper();

            // Call user's initialize method
            InitializeWorld();

            if (!mDeviceSizeSet)
                XNACS1Base.SetAppWindowPixelDimension(fullScreen, XNACS1_AppWindowWidth, XNACS1_AppWindowHeight);

        }


        //int mNumDraws = 0;

        /// <summary>
        /// <b>Do not override this method!</b> This method is used by the XNACS1Lib to setup all
        /// the internal and font drawing.
        /// 
        /// Override DrawWorld() if you are going to control the drawing of 
        /// your primitives. Otherwise, you don't need to worry about drawing.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            base.Draw(gameTime);

                m_DrawHelper.BeginDraw(BlendState.AlphaBlend);

                m_WorldBound.Draw();

                m_DrawHelper.DrawAll();

                /// if user has something specific they want to draw
                DrawWorld();

                m_FontManager.DrawFonts(m_TopStatus, m_BottomStatus, null);

                m_DrawHelper.EndDraw();
            
                
        }

        /// <summary>
        /// <b>Do not override this method!</b> This method is used by the XNACS1Lib to 
        /// maintain and update the system internal state.
        /// 
        /// Override UpdateWorld() and perform all your application specific update there!
        /// 
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            base.Update(gameTime);


            XNACS1LibInputManager.UpdateGamePadState(gameTime, Window.CurrentOrientation);

            if (!XNACS1Base.World.Paused)
                m_DrawHelper.TravelAll();



            //mNumDraws++;
            //if (gameTime.IsRunningSlowly)
            //    XNACS1Base.EchoToTopStatus("Running Slowly");
            //else
            //    XNACS1Base.EchoToTopStatus("Running Normal " + "NumD=" + mNumDraws + " TotalTime=" + gameTime.TotalGameTime.TotalMilliseconds/1000f);

            //XNACS1Base.EchoToBottomStatus(((double)mNumDraws * 1000 / gameTime.TotalGameTime.TotalMilliseconds).ToString() + " FPS");
            
            
            
            UpdateWorld();

        }
#endregion
 

        #region Methods subclass should override and should call base.methods

        /// <summary>
        ///  Subclass should override this to define the World Cooridnate origin and width, and to allocate
        /// and otherwise initialize the application state. This method is called once at the beginning of the application.
        /// </summary>
        protected virtual void InitializeWorld() { return; }

        /// <summary>
        ///  Subclass should override this method to update the world. This function is called 40 times a second.
        /// </summary>
        protected virtual void UpdateWorld() { return; }

        /// <summary>
        /// 
        /// subclass can override this to customize the redrawing of the world. This function is called 40 times a second.
        /// </summary>
        protected virtual void DrawWorld() { return; }

        #endregion

    }
}