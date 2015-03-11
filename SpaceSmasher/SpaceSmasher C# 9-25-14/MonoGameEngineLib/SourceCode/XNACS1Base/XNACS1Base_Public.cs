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

        #region public utility methods

        /// <summary>
        /// For accessing the state of the XBOX 360 GamePad Controller. In the absence of a GamePad controller, the keyboard
        /// will be polled according to the default mapping.
        /// </summary>
        public static XNACS1Lib.GamePadSupport.GamePadStruct GamePad { get { return XNACS1LibInputManager.GamePad; } }

        /// <summary>
        /// Sets the top status echo font color.
        /// </summary>
        /// <param name="topEchoColor">Color of the top echo font.</param>
        public static void SetTopEchoColor(Color topEchoColor)
        {
            m_GraphicsAccess.m_FontManager.TopEchoFontColor = topEchoColor;
        }
        
        /// <summary>
        /// Echo message to the status area located at the top of the drawing window.
        /// </summary>
        /// <param name="msg">Message to be echoed.</param>
        public static void EchoToTopStatus(String msg)
        {
            m_GraphicsAccess.m_TopStatus = msg;
        }


        /// <summary>
        /// Sets the bottom status echo font color.
        /// </summary>
        /// <param name="bottomEchoColor">Color of the bottom echo font.</param>
        public static void SetBottomEchoColor(Color bottomEchoColor)
        {
            m_GraphicsAccess.m_FontManager.BottomEchoFontColor = bottomEchoColor;
        }
        
        /// <summary>
        /// Echo message to the status area located at the bottom of the drawing window.
        /// </summary>
        /// <param name="msg">Message to be echoed.</param>
        public static void EchoToBottomStatus(string msg)
        {
            //msg = "Size=" + sm_GraphicsAccess.m_DrawHelper.DrawSetSize() + " || " + msg;
            m_GraphicsAccess.m_BottomStatus = msg;
        }

        /// <summary>
        /// Play the spcified audio cue for the specified length of seconds.
        /// </summary>
        /// <param name="cueName">Name of the audio cue (wav filename without the .wav extension).</param>
        public static void PlayACue(String cueName)
        {
               m_GraphicsAccess.mAudioManager.PlaySound(cueName);
        }

        /// <summary>
        /// Reassigns the keyboard mapping for the keyboard to gamepad to a new mapping, specified by the ini file passed.
        /// Example: SetKeyboardMapFile(".\\CS1setting.ini");
        /// </summary>
        /// <param name="fileName"></param>
        public static void SetKeyboardMapFile(string fileName) {
            XNACS1LibInputManager.m_Input = new KeyboardInputManager(fileName);
        }

        /// <summary>
        /// Plays the specific audo file name continuously as the background audio.
        /// </summary>
        /// <param name="bgAudio">Name of the file to be used as background music.</param>
        /// <param name="level">The loudness of bg music (1.0 is loudest, 0.0 is silent).</param>
        /// <remarks>Pass in a null string (null) to stop the background audio.</remarks>
        public static void PlayBackgroundAudio(String bgAudio, float level)
        {
            m_GraphicsAccess.mAudioManager.PlayBackgroundAudio(bgAudio, level);
        }

        /// <summary>
        /// Sets the Size of the Appplication Window (in pixel unit).
        /// </summary>
        /// <param name="fullScreen">Run in full screen mode. If ture, the width/height parameters are ignored.</param>
        /// <param name="widthInPixel">Width of the applicaiton window (in pixel).</param>
        /// <param name="heightInPixel">Height of the application window (in pixel).</param>
        public static void SetAppWindowPixelDimension(bool fullScreen, int widthInPixel, int heightInPixel)
        {
            if (m_GraphicsAccess.mDeviceSizeSet)
            {
                EchoToBottomStatus("CANNOT set Window Pixel Size more than once!");
                return;
            }

            m_GraphicsAccess.DefaultWorldMinX = World.WorldMin.X;
            m_GraphicsAccess.DefaultWorldMinY = World.WorldMin.Y;
            m_GraphicsAccess.DefualtWorldWidth = World.WorldMax.X - World.WorldMin.X;
            m_GraphicsAccess.RequestedWorldMin = World.WorldMin;
            m_GraphicsAccess.RequestedWorldWidth = m_GraphicsAccess.DefualtWorldWidth;
            m_GraphicsAccess.SetDeviceSize(fullScreen, widthInPixel, heightInPixel);
            m_GraphicsAccess.CreateDrawHelper();
            m_GraphicsAccess.m_FontManager.ComputeFontPositions(m_GraphicsAccess.XNACS1_AppWindowHeight);
        }

        /// <summary>
        /// Meaningful only when run on a Windows Phone device. Enable/Disable changing orientation
        /// based on the phone's physical settings. Valid orientations are:
        ///    Portrait, LandscapeLeft, LandscapeRight
        /// </summary>
        /// <param name="locked">If True, orienation of app will maintained as set.</param>
        /// <param name="orientation">The orientation to set to.</param>
        public static void SetPhoneOrientation(bool locked, DisplayOrientation orientation)
        {
#if WINDOWS_PHONE
            if (locked)
            {
                int w = m_GraphicsAccess.GraphicsDevice.Viewport.Width;
                int h = m_GraphicsAccess.GraphicsDevice.Viewport.Height;

                int useW=w, useH=h;
                switch (orientation)
                {
                    case DisplayOrientation.Portrait:
                        if (w > h)
                        {
                            useW = h;
                            useH = w;
                        }
                        break;
                    case DisplayOrientation.LandscapeLeft:
                    case DisplayOrientation.LandscapeRight:
                        if (h > w)
                        {
                            useW = h;
                            useH = w;
                        }
                        break;
                }
                m_GraphicsAccess.m_GraphicsManager.PreferredBackBufferWidth = useW;
                m_GraphicsAccess.m_GraphicsManager.PreferredBackBufferHeight = useH;
                m_GraphicsAccess.m_GraphicsManager.SupportedOrientations = orientation;

                m_GraphicsAccess.Window.OrientationChanged += null;
                m_GraphicsAccess.m_GraphicsManager.ApplyChanges();
                SetAppWindowPixelDimension(true, w, h);
            }
            else
            {
                m_GraphicsAccess.m_GraphicsManager.SupportedOrientations = DisplayOrientation.Portrait |
                                                  DisplayOrientation.LandscapeRight |
                                                  DisplayOrientation.LandscapeLeft;
                m_GraphicsAccess.Window.OrientationChanged += new EventHandler<EventArgs>(PhoneOrientationChanged);
                m_GraphicsAccess.m_GraphicsManager.ApplyChanges();
            }
            
#endif
        }

#if WINDOWS_PHONE

        static private void UpdateBufferSize()
        {
            int w = m_GraphicsAccess.GraphicsDevice.Viewport.Width;
            int h = m_GraphicsAccess.GraphicsDevice.Viewport.Height;
            SetAppWindowPixelDimension(true, w, h);
        }
        static private void PhoneOrientationChanged(Object sender, EventArgs e)
        {
            // event comes from a separate thread (system thread)
            // => creates a Lambda Expression: a thread-safe way of calling 
            // to choose background image.
            Deployment.Current.Dispatcher.BeginInvoke(() => UpdateBufferSize());
        }
#endif
        #endregion

        #region Random number support
        /// <summary>
        /// Returns a random number between 0.0 to 1.0
        /// </summary>
        /// <returns>A random number between 0.0 to 1.0.</returns>
        public static float RandomFloat()
        {
            return (float)(m_Ran.NextDouble());
        }
        /// <summary>Returns a random float between 0 and max.  This function may return 0, or may return
        /// a number that's close to max, but will never return max itself
        /// </summary>
        /// <param name="max">Max number to be returned.</param>
        /// <returns>A random number between 0 and max.</returns>
        public static float RandomFloat(float max)
        {
            return (max * RandomFloat());
        }
        /// <summary>
        /// Returns a float between min and max.  This function may return min, or may return
        /// a number that's close to max, but will never return max itself
        /// </summary>
        /// <param name="min">Min number to be returned.</param>
        /// <param name="max">Max number to be returned.</param>
        /// <returns>A random number between min and max.</returns>
        public static float RandomFloat(float min, float max)
        {
            float s = max - min;
            return min + (s * RandomFloat());
        }
        /// <summary>
        /// Returns a random integer between 0 and some VERY LARGE number.
        /// </summary>
        /// <returns>A random integer between 0 and some very large number.</returns>
        public static int RandomInt()
        {
            return m_Ran.Next();
        }
        /// <summary>
        /// Returns a random integer between 0 and max.
        /// </summary>
        /// <param name="max">Max integer to be returned.</param>
        /// <returns>Returns a random integer between 0 and max.</returns>
        public static int RandomInt(int max)
        {
            return m_Ran.Next(max);
        }
        /// <summary>
        /// Returns a random integer between min and max.
        /// </summary>
        /// <param name="min">Min integer to be returned.</param>
        /// <param name="max">Max integer to be returned.</param>
        /// <returns>Returns a random integer between min and max.</returns>
        public static int RandomInt(int min, int max)
        {
            return m_Ran.Next(min, max);
        }
        #endregion
    }
}