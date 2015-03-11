package GhostLight.GhostEngine;

import Engine.MouseInput;
import GhostLight.GhostEngine.GhostLight.updateState;
import MenueSystem.Button;
/**
 * @author Michael Letter
 */
public class ChangeGameStateButton extends Button {
	protected GhostLight gameManager = null;
	protected updateState targetState = updateState.MAINMENUE;
	
	public ChangeGameStateButton(){
		super();
		super.setIdleImage("menuArt/ButtonIdle.png");
		super.setButtonDownImage("menuArt/ButtonIdle.png");
		super.setMouseOverIdleImage("menuArt/ButtonIdleMouseOver.png");
		super.setMouseOverSelectImage("menuArt/ButtonIdleMouseOver.png");
		super.setSelectImage("menuArt/ButtonIdle.png");
		super.selectable = false;
	}
	
	public void setTargetState(GhostLight manager, updateState target){
		gameManager = manager;
		targetState = target;
	}
	public void autoUpdateState(MouseInput mouse){
		transitionState oldState = super.getSubState();
		super.autoUpdateState(mouse);
		if(super.getSubState() == transitionState.DOWN && super.getSubState() != oldState){
			changeState();
		}
	}
	public void changeState(){
		if(gameManager != null){
			gameManager.setUpdateState(targetState);
		}
	}
	public void setState(PanelState newState, boolean setSubPanels){
		super.setState(newState, setSubPanels);
		if(newState == PanelState.INACTIVE){
			setVisibility(false,false);
		}
	}
}
