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
    /// Enum data type for collision results between a Primitive and the world bound 
    /// Status returned by ClampAtWorldBound() and CollideWorldBound.
    /// </summary>
    public enum BoundCollideStatus
    {
        /// <summary>
        /// Primitive has collided with the left-world boundary.
        /// </summary>
        CollideLeft, 

        /// <summary>
        /// Primitive has collided with the right-world boundary.
        /// </summary>
        CollideRight,

        /// <summary>
        /// Primitive has collided with the bottom-world boundary.
        /// </summary>
        CollideBottom,
        
        /// <summary>
        /// Primitive has collided with the top-world boundary.
        /// </summary>
        CollideTop,
        
        /// <summary>
        /// Primitive is inside the world boundary.
        /// </summary>
        InsideBound
    };

    /// <summary>
    /// Define SpriteSheet Animation
    /// </summary>
    public enum SpriteSheetAnimationMode {
        /// <summary>
        /// Animate forward in frame numbers (loops from begin to end)
        /// </summary>
        AnimateForward,

        /// <summary>
        /// Animate backward in frame numbers (loops from end to begin)
        /// </summary>
        AnimateBackward,

        /// <summary>
        /// Swing the animation in frame numbers (goes back and forth between begin and end)
        /// </summary>
        AnimateSwing,

        /// <summary>
        /// Animate forward in frame numbers then stops at the set final frame.
        /// </summary>
        AnimateForwardThenStop,

        /// <summary>
        /// Animate backward in frame numbers then stops at the set first frame.
        /// </summary>
        AnimateBackwardThenStop
    };

    /// <summary>
    /// Defines how particles with ShouldTravel==true will be displaced along their travel direction.
    /// </summary>
    public enum ParticleTravelMode {
        /// <summary>
        /// There is no displacement for the particle each tick, other than the velocity.
        /// </summary>
        Linear,
        /// <summary>
        /// The particle displaces in a sine wave normal to the velocity.
        /// </summary>
        TransverseSine,
        /// <summary>
        /// The particle displaces in a sine wave along the same direction as the velocity.
        /// </summary>
        CompressionSine,
        /// <summary>
        /// The particle displaced in a sine wave in the Y direction.
        /// </summary>
        VerticalSine,
        /// <summary>
        /// The particle is displaced in a sine wave in the X direction.
        /// </summary>
        HorizontalSine,
        /// <summary>
        /// The particle is randomly displaced.
        /// </summary>
        RandomDisplace
    }
}