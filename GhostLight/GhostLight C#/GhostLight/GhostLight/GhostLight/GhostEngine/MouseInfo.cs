using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
 
/**
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class MouseInfo {
	    internal Mouse mouse = null;
        internal float screenWidth = 0;
        internal float screenHeight = 0;

	    /// <summary>
        /// Will return whether or not the target Mouse Button is currently held down.
	    /// </summary>
        /// <param name="targetButton">the mouse button in question</param>
        /// <returns>returns true if the mouse button in question is currently held down, otherwise returns false.</returns>
	    public bool isButtonDown(MouseState.MouseButton targetButton){
		    if(mouse != null){
			    if(targetButton == MouseState.MouseButton.LEFT){
				    return mouse.leftMouseDown;
			    }
			    else if(targetButton == MouseState.MouseButton.RIGHT){
				    return mouse.rightMouseDown;
			    }
			    else if(targetButton == MouseState.MouseButton.MIDDLE){
				    return mouse.middleMouseDown;
			    }
		    }
		    return false;
	    }
	    /// <summary>
        /// Will Return the mouses X position in the window
	    /// </summary>
	    /// <returns>a float between 0 and 1 with 0 representing a position at the left edge of the screen 
	    /// and 1 representing a position at the right edge of the screen</returns>
	    public float getMouseX(){
		    if(mouse != null && screenWidth > 0){
			    //System.out.println(mouse.getPixelX()/screenWidth);
			    return (mouse.getPixelPosition().X/screenWidth) * 1.1f; // random constant is for side bar
		    }
		    return 0;
	    }
        /**
	     * Will Return the mouses X position in the window
	     * @return a float between 0 and 1 with 0 representing a position at the top edge of the screen 
	     * and 1 representing a position at the bottom edge of the screen
	     */
	    /// <summary>
        /// Will Return the mouses X position in the window
	    /// </summary>
	    /// <returns>a float between 0 and 1 with 0 representing a position at the top edge of the screen 
	    /// and 1 representing a position at the bottom edge of the screen</returns>
	    public float getMouseY(){
		    if(mouse != null && screenHeight > 0){
                return mouse.getPixelPosition().Y / screenHeight;
		    }
		    return 0;
	    }
    }
}
