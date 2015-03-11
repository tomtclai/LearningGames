package GhostLight.GhostEngine;

import Engine.MouseInput;
import GhostLight.Interface.GhostLightInterface;
import MenueSystem.Button;
/**
 * @author Michael Letter
 */
public class LevelButton extends Button{
	protected GhostLight gameManager = null;
	protected GhostLightInterface gameLevel = null;
	public String winMessage = "You Win";
	public String loseMessage = "You Lose";
	protected int levelNumber = -1;
	
	public LevelButton(){
		super();
		super.setIdleImage("menuArt/ButtonIdle.png");
		super.setButtonDownImage("menuArt/ButtonDown.png");
		super.setMouseOverIdleImage("menuArt/ButtonIdleMouseOver.png");
		super.setMouseOverSelectImage("menuArt/ButtonIdleMouseOver.png");
		super.setSelectImage("menuArt/ButtonIdle.png");
		super.selectable = false;
	}
	public void setLevel(GhostLight manager, GhostLightInterface level){
		gameManager = manager;
		gameLevel = level;
		levelNumber = -1;
	}
	public void setLevel(GhostLight manager, GhostLightInterface level, int LevelNum){
		gameManager = manager;
		gameLevel = level;
		levelNumber = LevelNum;
	}
	public void autoUpdateState(MouseInput mouse){
		transitionState oldState = super.getSubState();
		super.autoUpdateState(mouse);
		if(super.getSubState() == transitionState.DOWN && super.getSubState() != oldState){
			startLevel();
		}
	}
	public void startLevel(){
		if(gameManager != null && gameLevel != null){
			if(levelNumber >= 0){
				gameManager.newGame(gameLevel, levelNumber);
			}
			else{
				gameManager.newGame(gameLevel, super.getID());
			}
		}
	}
	public void setState(PanelState newState, boolean setSubPanels){
		super.setState(newState, setSubPanels);
		if(newState == PanelState.INACTIVE){
			setVisibility(false,false);
		}
	}
}
