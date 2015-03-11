#if WINDOWS_PHONE
using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Content;
using System.Text;
using System.Collections;
using System.Runtime.InteropServices;


using Microsoft.Xna.Framework.Input.Touch;
using Microsoft.Devices.Sensors;        // for accelerometer access
using System.Windows;                   // for Deployment.Dispatcher


namespace XNACS1Lib
{
    namespace WP7InputSupport
    {
        // for now:
        //
        //   Accelerameter
        //   Touch-Tap
        public class WP7Input {

            // Accelerometer support, translates accelerometer readings to a Vector3
            Accelerometer mAccMeter;
            Vector3 mAccValues;

            // Gesture support
            private const float kDRAG_UNIT = 1f / 50f; // 50 pixels is max
            private const int kTAP_SENSITIVITY = 110;  // 110 mSec between single and double tap
            bool mHasTap;
            bool mHasTapTwo;
            bool mPinchHasBegin;
            float mHDelta;       
            float mVDelta;
            DisplayOrientation mCurrentOrientation = DisplayOrientation.Portrait;

            public WP7Input()
            {
                #region Accelerometer support
                mAccValues = new Vector3();
                mAccMeter = new Accelerometer();
                mAccMeter.ReadingChanged += new EventHandler<AccelerometerReadingEventArgs>(AccMeterChange);
                // event services when accelerometer settings change
                // This function is defined in Game1_Accelerometer.
                try
                {
                    mAccMeter.Start();  // Starts the acceleromter sampling
                }
                catch (AccelerometerFailedException ex)
                {
                    System.Diagnostics.Debug.WriteLine("Accelerometer Starting failed!: " + ex.ToString());
                }
                #endregion 

                #region Gesture support
                mPinchHasBegin = false;
                TouchPanel.EnabledGestures = GestureType.Tap |
                                         GestureType.FreeDrag |
                                         GestureType.Pinch |
                                         GestureType.PinchComplete;
                #endregion 
            }

            public void UpdateWP7InputState(GameTime gameTime, DisplayOrientation currentOrientation) {

                mCurrentOrientation = currentOrientation;

                #region Gesture support
                mHasTap = false;
                mHasTapTwo = false;
                mHDelta = 0.0f;
                mVDelta = 0.0f;

                if (TouchPanel.IsGestureAvailable) {
                    GestureSample g = TouchPanel.ReadGesture();
                    switch (g.GestureType)
                    {
                        case GestureType.Tap:
                            mHasTap = true;
                            break;

                        case GestureType.Pinch:
                            if (!mPinchHasBegin)
                            {
                                mHasTapTwo = true;
                                mPinchHasBegin = true;
                            }
                            break;
                        
                        case GestureType.PinchComplete:
                            mPinchHasBegin = false;
                            break;

                        case GestureType.FreeDrag:
                            float x= (g.Delta.X) * kDRAG_UNIT;
                            x = MathHelper.Clamp(x, -1f, 1f);
                            
                            float y= -(g.Delta.Y) * kDRAG_UNIT;
                            y = MathHelper.Clamp(y, -1f, 1f);

                            mHDelta = x;
                            mVDelta = y;
                            break;
                    }
                }
                #endregion 
            }

            public Vector3 GetAcc() {
                return mAccValues;
            }
            public bool HasTap() { return mHasTap; }
            public bool HasTapTwo() { return mHasTapTwo; }
            public float HDelta() { return mHDelta; }
            public float VDelta() { return mVDelta; }

            #region Accelerometer support
            void UpdateBallAcceleration(AccelerometerReadingEventArgs e)
            {
                // by default, XNA App works in landscape mode, while
                // accelerometer assumes portrait. 
                //    XYZ ranges are betwen -2 and +2
                // Gravitatoin pull is -1
                //
                const float kAccelerationScale = 0.5f;
                float x = kAccelerationScale * (float)e.X;
                float y = kAccelerationScale * (float)e.Y;
                mAccValues.Z = kAccelerationScale * (float)e.Z;

                switch (mCurrentOrientation)
                {
                    case DisplayOrientation.Portrait:
                        mAccValues.X = x;
                        mAccValues.Y = -y;
                        break;
                    case DisplayOrientation.LandscapeLeft:
                        mAccValues.X = -y;
                        mAccValues.Y = -x;
                        break;
                    case DisplayOrientation.LandscapeRight:
                        mAccValues.X = y;
                        mAccValues.Y = x;
                        break;
                }
            }

            /// <summary>
            /// This method is called from the accelerometer service thread. We should not
            /// modify our data in this method (invoked from a different thread). Instead, 
            /// we will create a Lambda Expression, and allow our own thread to make the 
            /// function call to MoveBall.
            /// </summary>
            /// <param name="sender"></param>
            /// <param name="e"></param>
            void AccMeterChange(object sender, AccelerometerReadingEventArgs e)
            {
                // event comes from a separate thread (system thread)
                // => creates a Lambda Expression: a thread-safe way of calling MoveBall(e).
                Deployment.Current.Dispatcher.BeginInvoke(() => UpdateBallAcceleration(e));
            }
            #endregion
        }
    }
}

#endif