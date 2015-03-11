using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.GhostEngine;


namespace GhostFinder.Interface{
    /// <summary>
    /// Offers access to any buttons on the screen to check if they have been clicked
    /// Michael Letter
    /// </summary>
    public class OnScreenButtons : ButtonAccess{
	    
        /// <summary> The various buttons that can be pressed on the screen </summary>
	    public enum ScreenButton {LASERBUTTON, MEDIUMBUTTON, WIDEBUTTON}

	    /// <summary>
        /// Will return true if the given ScreenButton is currently being held Down
	    /// </summary>
        /// <param name="button">button the button in question</param>
        /// <returns>true if the given ScreenButton is currently being held Down, otherwise false</returns>
	    public bool isButtonDown(ScreenButton button){
		    if(button == ScreenButton.LASERBUTTON){
			    return isLaserDown();
		    }
		    else if(button == ScreenButton.MEDIUMBUTTON){
			    return isMediumDown();
		    }
		    else if(button == ScreenButton.WIDEBUTTON){
			    return isWideDown();
		    }
		    return false;
	    }
	    /// <summary>
	    /// Will set whether or not a button is active or Inactive 
        /// If Inactive the button will be grayed out. Otherwise it will glow when the FlashLight is set the the corresponding LightType
	    /// </summary>
        /// <param name="button">button the button in question</param>
	    /// <param name="active">If True, the button will be st to Active, if false, the button will be set to inactive</param>
	    public void setButtonActivity(ScreenButton button, bool active){
		    if(button == ScreenButton.LASERBUTTON){
			    setLaserActivity(active);
		    }
		    else if(button == ScreenButton.MEDIUMBUTTON){
			    setRevealActivity(active);
		    }
		    else if(button == ScreenButton.WIDEBUTTON){
			    setWideActivity(active);
		    }
	    }
	    /// <summary>
	    /// Will return whether or not a Button is Active or Inactive
        /// If Inactive the button will be grayed out. Otherwise it will glow when the FlashLight is set the the corresponding LightType
	    /// </summary>
        /// <param name="button">the button in question</param>
        /// <returns>If TRUE, The button is Active. If FALSE, the button is Inactive</returns>
	    public bool isButtonActive(ScreenButton button){
		    if(button == ScreenButton.LASERBUTTON){
			    return getLaserActivity();
		    }
		    else if(button == ScreenButton.MEDIUMBUTTON){
			    return getRevealActivity();
		    }
		    else if(button == ScreenButton.WIDEBUTTON){
			    return getWideActivity();
		    }
		    return false;
	    }
	    /// <summary>
        /// Will highlight a given button. This will result in it appearing brighter than normal
	    /// </summary>
        /// <param name="button">the target Button</param>
	    public void highlightButton(ScreenButton button){
		    if(button == ScreenButton.LASERBUTTON){
			    base.setLaserHighLight(true);
		    }
		    else if(button == ScreenButton.MEDIUMBUTTON){
                base.setMediumHighLight(true);
		    }
		    else if(button == ScreenButton.WIDEBUTTON){
                base.setWideHighLight(true);
		    }
	    }
	    /// <summary>
        /// Will make the given button not highlighted.
	    /// </summary>
        /// <param name="button">the target Button</param>
	    public void unHighlightButton(ScreenButton button){
		    if(button == ScreenButton.LASERBUTTON){
                base.setLaserHighLight(false);
		    }
		    else if(button == ScreenButton.MEDIUMBUTTON){
                base.setMediumHighLight(false);
		    }
		    else if(button == ScreenButton.WIDEBUTTON){
                base.setWideHighLight(false);
		    }
	    }
	    /// <summary>
        /// will Return whether or not a given button is Highlighted. if it is Highlighted will appear brighter than normal
	    /// </summary>
        /// <param name="button">the button in question</param>
        /// <returns>If TRUE, the button is Highlighted. otherwise. If FALSE, the button is not</returns>
	    public bool isButtonHighlighted(ScreenButton button){
		    if(button == ScreenButton.LASERBUTTON){
                return base.isLaserHighlighted();
		    }
		    else if(button == ScreenButton.MEDIUMBUTTON){
                return base.isMediumHighlighted();
		    }
		    else if(button == ScreenButton.WIDEBUTTON){
                return base.isWideHighlighted();
		    }
		    return false;
	    }
    }
}