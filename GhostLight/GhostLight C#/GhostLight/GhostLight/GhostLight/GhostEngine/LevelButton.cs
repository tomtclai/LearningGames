using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;
/**
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class LevelButton : Button{
	    protected GhostFinder gameManager = null;
	    internal GhostFinderInterface gameLevel = null;
	    public String winMessage = "You Win";
	    public String loseMessage = "You Lose";
	    protected int levelNumber = -1;
	
	    public LevelButton() : base(){
            base.setIdleImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdle.png"));
            base.setButtonDownImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonDown.png"));
            base.setMouseOverIdleImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdleMouseOver.png"));
            base.setMouseOverSelectImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdleMouseOver.png"));
            base.setSelectImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdle.png"));
            base.selectable = false;
	    }
	    public void setLevel(GhostFinder manager, GhostFinderInterface level){
		    gameManager = manager;
		    gameLevel = level;
		    levelNumber = -1;
		
	    }
	    public void setLevel(GhostFinder manager, GhostFinderInterface level, int LevelNum){
		    gameManager = manager;
		    gameLevel = level;
		    levelNumber = LevelNum;
		
	    }
	    public override void autoUpdateState(Mouse mouse){
            transitionState oldState = base.getSubState();
            base.autoUpdateState(mouse);
		    if(base.getSubState() == transitionState.DOWN && base.getSubState() != oldState){
			    startLevel();
		    }
	    }
	    public void startLevel(){
		    if(gameManager != null && gameLevel != null){
			    if(levelNumber >= 0){
				    gameManager.newGame(gameLevel, levelNumber);
			    }
			    else{
                    gameManager.newGame(gameLevel, base.getID());
			    }
		    }
	    }
	    public override void setState(PanelState newState, bool setSubPanels){
            base.setState(newState, setSubPanels);
		    if(newState == PanelState.INACTIVE){
			    setVisibility(false,false);
		    }
	    }
    }
}