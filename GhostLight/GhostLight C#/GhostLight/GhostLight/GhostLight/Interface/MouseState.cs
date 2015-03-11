using CustomWindower.CoreEngine;
using GhostFinder.GhostEngine;

/**
 * Used to access the mouses current state. 
 * This includes position and which buttons have been tapped or pressed
 * @author Michael Letter
 */
namespace GhostFinder.Interface{
    /// <summary>
    ///  Used to access the mouses current state. 
    /// This includes position and which buttons have been tapped or pressed
    /// author: Michael Letter
    /// </summary>
    public class MouseState : MouseInfo{
        /// <summary>Identifies a mouse button </summary>
	    public enum MouseButton {LEFT, RIGHT, MIDDLE}
	
    }
}
