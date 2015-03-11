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
    
    /// <summary>
    /// This is the base class for your program. To work with the XNACS1Lib, your main class must subclass
    /// from this class.
    /// </summary>
    /// <remark>
    /// This is the base class for your program. To work with the XNACS1Lib, your main class must subclass
    /// from this class.
    /// </remark>
    public partial class XNACS1Base : Microsoft.Xna.Framework.Game
    {
        #region Private implementation subclasses have no need to have knowledge or to access any of these

        private float DefaultWorldMinX = 0.0f;
        private float DefaultWorldMinY = 0.0f;
        private float DefualtWorldWidth = 100.0f;
        private Vector2 RequestedWorldMin = Vector2.Zero;
        private float RequestedWorldWidth = 100.0f;

        private const int kHDTV_Width = 1920;
        private const int kHDTV_Height = 1080;
        private const float kHDTVAspecRatio = (float)kHDTV_Width / (float)kHDTV_Height;
        private int XNACS1_AppWindowWidth = 1280; // lower res HDTV: 1280x720 same aspect ratio
        private int XNACS1_AppWindowHeight = 720;
        private float kDefaultWidthCoverage = 0.85f; // by defualt how much of screen width is covered
        private float UseAspectRatio;
        private bool mDeviceSizeSet = false;

        private const float DrawViewTop = 0.0f;
        private const float DrawViewLeft = 0.0f;
        private float DrawViewWidth = 1280;
        private float DrawViewHeight = 720;

        private GraphicsDeviceManager m_GraphicsManager;

        private XNACS1Rectangle m_WorldBound;

        private String m_TopStatus;
        private String m_BottomStatus;

        // Font support
        private FontManager m_FontManager;

        // Audio Support
        private SoundManager mAudioManager;

        // Drawing support
        private XNACS1LibDrawHelper m_DrawHelper = null;

        #region static stuff

        static internal Random m_Ran = new Random();
        static internal XNACS1Base m_GraphicsAccess;

        #endregion  static stuff

        #endregion  private data of XNACS1Lib class


        /// <summary>
        /// This is the constructor of the base class for the library. You can define your own
        /// constructor to initialize your instance variables. 
        /// 
        /// You are free to _not_ implement a constructor. You will receive a defualt applicaiton
        /// window with HDTV aspect ratio.
        /// 
        /// </summary>
        public XNACS1Base()
            : base()
        {
            ConstructorInit();
        }
    }
}