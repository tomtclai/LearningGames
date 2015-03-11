package GhostLight.GhostEngine;

import Engine.MouseInput;
import MenueSystem.Button;
import MenueSystem.Button.transitionState;
import MenueSystem.Panel.PanelState;
/**
 * @author Michael Letter
 */
public class ButtonAccess {
	MouseInput mouse = null;
	LightButton LaserButton = null;
	LightButton MediumButton = null;
	LightButton WideButton = null;
	
	//Laser
	protected boolean isLaserTapped(){
		if(mouse != null && LaserButton != null && 
				(mouse.isButtonTapped(1) || mouse.isButtonTapped(2) || mouse.isButtonTapped(3)) &&
				LaserButton.isPointOver(mouse.getWorldX(), mouse.getWorldY())){
			return true;
			
		}
		return false;
	}
	protected boolean isLaserDown(){
		if(mouse != null && LaserButton != null && 
				(mouse.isButtonDown(1) || mouse.isButtonDown(2) || mouse.isButtonDown(3)) &&
				LaserButton.isPointOver(mouse.getWorldX(), mouse.getWorldY())){
			return true;
			
		}
		return false;
	}
	protected void setLaserActivity(boolean active){
		if(LaserButton != null){
			if(!active){
				LaserButton.setState(PanelState.INACTIVE, false);
			}
			else if(LaserButton.getState() == PanelState.INACTIVE){
				LaserButton.setState(PanelState.IDLE, false);
			}
		}
	}
	protected boolean getLaserActivity(){
		if(LaserButton != null){
			if(LaserButton.getState() != PanelState.INACTIVE){
				return true;
			}
		}
		return false;
	}
	protected void setLaserHighLight(boolean highlighted){
		if(LaserButton != null){
			LaserButton.setSparcleVisiblity(highlighted);
		}
	}
	protected boolean isLaserHighlighted(){
		if(LaserButton != null){
			LaserButton.isSparcling();
		}
		return false;
	}
	//Reveal
	protected boolean isMediumTapped(){
		if(mouse != null && MediumButton != null && 
				(mouse.isButtonTapped(1) || mouse.isButtonTapped(2) || mouse.isButtonTapped(3)) &&
				MediumButton.isPointOver(mouse.getWorldX(), mouse.getWorldY())){
			return true;
			
		}
		return false;
	}
	protected boolean isMediumDown(){
		if(mouse != null && MediumButton != null && 
				(mouse.isButtonDown(1) || mouse.isButtonDown(2) || mouse.isButtonDown(3)) &&
				MediumButton.isPointOver(mouse.getWorldX(), mouse.getWorldY())){
			return true;
			
		}
		return false;
	}
	protected void setRevealActivity(boolean active){
		if(MediumButton != null){
			if(!active){
				MediumButton.setState(PanelState.INACTIVE, false);
			}
			else if(MediumButton.getState() == PanelState.INACTIVE){
				MediumButton.setState(PanelState.IDLE, false);
			}
		}
	}
	protected boolean getRevealActivity(){
		if(MediumButton != null){
			if(MediumButton.getState() != PanelState.INACTIVE){
				return true;
			}
		}
		return false;
	}
	protected void setMediumHighLight(boolean highlighted){
		if(MediumButton != null){
			MediumButton.setSparcleVisiblity(highlighted);
		}
	}
	protected boolean isMediumHighlighted(){
		if(MediumButton != null){
			MediumButton.isSparcling();
		}
		return false;
	}
	//Wide
	protected boolean isWideTapped(){
		if(mouse != null && WideButton != null && 
				(mouse.isButtonTapped(1) || mouse.isButtonTapped(2) || mouse.isButtonTapped(3)) &&
				WideButton.isPointOver(mouse.getWorldX(), mouse.getWorldY())){
			return true;
			
		}
		return false;
	}
	protected boolean isWideDown(){
		if(mouse != null && WideButton != null && 
				(mouse.isButtonDown(1) || mouse.isButtonDown(2) || mouse.isButtonDown(3)) &&
				WideButton.isPointOver(mouse.getWorldX(), mouse.getWorldY())){
			return true;
			
		}
		return false;
	}
	protected void setWideActivity(boolean active){
		if(WideButton != null){
			if(!active){
				WideButton.setState(PanelState.INACTIVE, false);
			}
			else if(WideButton.getState() == PanelState.INACTIVE){
				WideButton.setState(PanelState.IDLE, false);
			}
		}
	}
	protected boolean getWideActivity(){
		if(WideButton != null){
			if(WideButton.getState() != PanelState.INACTIVE){
				return true;
			}
		}
		return false;
	}
	protected void setWideHighLight(boolean highlighted){
		if(WideButton != null){
			WideButton.setSparcleVisiblity(highlighted);
		}
	}
	protected boolean isWideHighlighted(){
		if(WideButton != null){
			WideButton.isSparcling();
		}
		return false;
	}
}
