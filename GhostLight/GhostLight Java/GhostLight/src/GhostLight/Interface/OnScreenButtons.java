package GhostLight.Interface;

import GhostLight.GhostEngine.ButtonAccess;
import MenueSystem.Button.transitionState;

/**
 * Offers access to any buttons on the screen to check if they have been clicked
 * @author Michael Letter
 */
public class OnScreenButtons extends ButtonAccess{
	/** The various buttons that can be pressed on the screen */
	public enum ScreenButton {LASERBUTTON, MEDIUMBUTTON, WIDEBUTTON}
	
	/**
	 * Will return true if the given ScreenButton has been Clicked by the mouse
	 * @param button the button in question
	 * @return true if the given ScreenButton has been Clicked by the mouse, other wise false
	 */
	public boolean isButtonTapped(ScreenButton button){
		if(button == ScreenButton.LASERBUTTON){
			return isLaserTapped();
		}
		else if(button == ScreenButton.MEDIUMBUTTON){
			return isMediumTapped();
		}
		else if(button == ScreenButton.WIDEBUTTON){
			return isWideTapped();
		}
		return false;
	}
	/**
	 * Will return true if the given ScreenButton is currently being held Down
	 * @param button the button in question
	 * @return true if the given ScreenButton is currently being held Down, otherwise false
	 */
	public boolean isButtonDown(ScreenButton button){
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
	/**
	 * Will set whether or not a button is active or Inactive 
	 * If Inactive the button will be grayed out. Otherwise it will glow when the FlashLight is set the the corresponding LightType
	 * @param button the button in question
	 */
	public void setButtonActivity(ScreenButton button, boolean active){
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
	/**
	 * Will return whether or not a Button is Active or Inactive
	 * If Inactive the button will be grayed out. Otherwise it will glow when the FlashLight is set the the corresponding LightType
	 * @param button the button in question
	 * @return If TRUE, The button is Active. If FALSE, the button is Inactive
	 */
	public boolean isButtonActive(ScreenButton button){
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
	/**
	 * Will highlight a given button. This will result in it appearing brighter than normal
	 * @param button the target Button
	 */
	public void highlightButton(ScreenButton button){
		if(button == ScreenButton.LASERBUTTON){
			super.setLaserHighLight(true);
		}
		else if(button == ScreenButton.MEDIUMBUTTON){
			super.setMediumHighLight(true);
		}
		else if(button == ScreenButton.WIDEBUTTON){
			super.setWideHighLight(true);
		}
	}
	/**
	 * Will make the given button not highlighted.
	 * @param button the target Button
	 */
	public void unHighlightButton(ScreenButton button){
		if(button == ScreenButton.LASERBUTTON){
			super.setLaserHighLight(false);
		}
		else if(button == ScreenButton.MEDIUMBUTTON){
			super.setMediumHighLight(false);
		}
		else if(button == ScreenButton.WIDEBUTTON){
			super.setWideHighLight(false);
		}
	}
	/**
	 * will Return whether or not a given button is Highlighted. if it is Highlighted will appear brighter than normal
	 * @param button the button in question
	 * @return If TRUE, the button is Highlighted. otherwise. If FALSE, the button is not  
	 */
	public boolean isButtonHighlighted(ScreenButton button){
		if(button == ScreenButton.LASERBUTTON){
			return super.isLaserHighlighted();
		}
		else if(button == ScreenButton.MEDIUMBUTTON){
			return super.isMediumHighlighted();
		}
		else if(button == ScreenButton.WIDEBUTTON){
			return super.isWideHighlighted();
		}
		return false;
	}
}
