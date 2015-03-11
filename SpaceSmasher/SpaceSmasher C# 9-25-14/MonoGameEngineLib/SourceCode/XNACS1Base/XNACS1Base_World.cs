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
        #region Provided a public class to gain access to the "Global World" functions
        /// <summary>
        /// This this the class for setting, inquiring, and changing information corresponding
        /// to the "World", including:
        /// Coordinate System, Background color/texture, Clamping/colliding with the boundary, and removing
        /// all primitives from the AutoDraw set.
        /// </summary>
        public static class World
        {

            /// <summary>
            /// Gets or sets the number of ticks/frames in one second.
            /// </summary>
            public static int TicksInASecond {
                get { return m_TicksInASecond; }
                set {
                    m_TicksInASecond = value;
                    m_GraphicsAccess.TargetElapsedTime = new TimeSpan(0, 0, 0, 0, 1000/value);
                }
            }
            private static int m_TicksInASecond = 40;

            private static bool mPaused = false;
    
            /// <summary>
            /// Sets the background color.
            /// </summary>
            /// <param name="bgColor">The specified color for the entire background.</param>
            public static void SetBackgroundColor(Color bgColor)
            {
                m_GraphicsAccess.m_WorldBound.Color = bgColor;
            }

            /// <summary>
            /// Defines the world coordinate system to draw in.
            /// </summary>
            /// <param name="min">Coordinate position of the lower left corner of the drawing window.</param>
            /// <param name="width">Width represented by the drawing window.</param>
            public static void SetWorldCoordinate(Vector2 min, float width)
            {
                m_GraphicsAccess.RequestedWorldMin = min;
                m_GraphicsAccess.RequestedWorldWidth = width;
                m_GraphicsAccess.CreateDrawHelper();
            }

            /// <summary>
            /// Set the background texture. The texture image will cover the entire background
            /// such that the background color will not be visible.
            /// </summary>
            /// <param name="tex">name of the background texture image.</param>
            public static void SetBackgroundTexture(String tex)
            {
                m_GraphicsAccess.m_WorldBound.Texture = tex;
            }

            /// <summary>
            /// Pausing the World Update (ShouldTravel primitive will stop moving).
            /// </summary>
            public static bool Paused
            {
                set { mPaused = value; }
                get { return mPaused; }
            }

            /// <summary>
            /// Get the coodinate position that corresponds to the lower-left corner of the world.
            /// </summary>  
            /// <seealso cref="WorldMax"/>
            /// <seealso cref="WorldDimension"/>
            public static Vector2 WorldMin
            {
                get { return m_GraphicsAccess.m_WorldBound.MinBound; }
            }

            /// <summary>
            /// Get the coordinate position that corresponds to the upper-right corner of the world.
            /// </summary>
            /// <seealso cref="WorldMin"/>
            /// <seealso cref="WorldDimension"/>
            public static Vector2 WorldMax
            {
                get { return m_GraphicsAccess.m_WorldBound.MaxBound; }
            }

            /// <summary>
            /// Get the dimension (width/height) of the world. 
            /// </summary>
            /// <seealso cref="WorldMin"/>
            /// <seealso cref="WorldMax"/>
            public static Vector2 WorldDimension
            {
                get { return m_GraphicsAccess.m_WorldBound.m_Size; }
            }

            /// <summary>
            /// Returns true if the inputPrimitive is outside of the worldBound. 
            /// </summary>
            /// <param name="inputPrimitive">the primitive to check</param>
            /// <returns>True if inputPrimitive is outside the world bound, otherwise returns false.</returns>
            public static bool OutsideWorldBound(XNACS1Primitive inputPrimitive)
            {
                Vector2 min = m_GraphicsAccess.m_WorldBound.MinBound;
                Vector2 max = m_GraphicsAccess.m_WorldBound.MaxBound;

                return (inputPrimitive.MinBound.X > max.X) ||
                       (inputPrimitive.MaxBound.X < min.X) ||
                       (inputPrimitive.MinBound.Y > max.Y) ||
                       (inputPrimitive.MaxBound.Y < min.Y);
            }

            /// <summary>
            /// Collide the inputPrimitive with the worldBound and return which of the worldBound (left/right/top/bottom or inside)
            /// did this primitive intersect with.
            /// </summary>
            /// <param name="inputPrimitive">Primitive to be tested</param>
            /// <returns>The side upon which the primitive has collided with (or if the primitive is inside the world bound).</returns>
            public static BoundCollideStatus CollideWorldBound(XNACS1Primitive inputPrimitive)
            {
                Vector2 min = m_GraphicsAccess.m_WorldBound.MinBound;
                Vector2 max = m_GraphicsAccess.m_WorldBound.MaxBound;

                if (inputPrimitive.MinBound.X < min.X)
                    return BoundCollideStatus.CollideLeft;
                if (inputPrimitive.MaxBound.X > max.X)
                    return BoundCollideStatus.CollideRight;
                if (inputPrimitive.MinBound.Y < min.Y)
                    return BoundCollideStatus.CollideBottom;
                if (inputPrimitive.MaxBound.Y > max.Y)
                    return BoundCollideStatus.CollideTop;

                return BoundCollideStatus.InsideBound;
            }

            /// <summary>
            /// Ensures that the inputPrimitive is kept within bounds of the world. If inputPrimitive position is outside
            /// of the world bound, This function will determine the distances to the closes bound and force the inputPrimitive to
            /// be insde that world bound.
            /// </summary>
            /// <param name="inputPrimitive">The primtive to be tested</param>
            /// <returns>Which of the world bound that the inputPrimitive has moved out of.</returns>
            public static BoundCollideStatus ClampAtWorldBound(XNACS1Primitive inputPrimitive)
            {
                BoundCollideStatus status = CollideWorldBound(inputPrimitive);
                if (status != BoundCollideStatus.InsideBound)
                {
                    Vector2 size = (inputPrimitive.MaxBound - inputPrimitive.MinBound) * 0.5f;
                    Vector2 max = new Vector2(WorldMax.X - size.X, WorldMax.Y - size.Y);
                    Vector2 min = new Vector2(WorldMin.X + size.X, WorldMin.Y + size.Y);
                    Vector2 pos = new Vector2(inputPrimitive.Center.X, inputPrimitive.Center.Y);
                    if (pos.X > max.X) pos.X = max.X;
                    if (pos.Y > max.Y) pos.Y = max.Y;
                    if (pos.X < min.X) pos.X = min.X;
                    if (pos.Y < min.Y) pos.Y = min.Y;
                    inputPrimitive.Center = pos;
                }
                return status;
            }

            /// <summary>
            /// Remove all Primitives from the default draw set. After this funciton, you will observe a blank screen.
            /// </summary>
            public static void RemoveAllFromDrawSet()
            {
                m_GraphicsAccess.m_DrawHelper.RemoveAllFromSet();
            }

        };

        #endregion
    }
}