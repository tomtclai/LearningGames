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
    public class ChangeGameStateButton : Button {
	    protected GhostFinder gameManager = null;
        protected GhostFinder.updateState targetState = GhostFinder.updateState.MAINMENUE;
	
	    public ChangeGameStateButton(){
            base.setIdleImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdle.png"));
            base.setButtonDownImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonDown.png"));
            base.setMouseOverIdleImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdleMouseOver.png"));
            base.setMouseOverSelectImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdleMouseOver.png"));
            base.setSelectImage((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/menuArt/ButtonIdle.png"));
            base.selectable = false;
	    }
	
	    public void setTargetState(GhostFinder manager, GhostFinder.updateState target){
		    gameManager = manager;
		    targetState = target;
	    }
	    public override void autoUpdateState(Mouse mouse){
            transitionState oldState = base.getSubState();
		    base.autoUpdateState(mouse);
		    if(base.getSubState() == transitionState.DOWN && base.getSubState() != oldState){
			    changeState();
		    }
	    }
	    public void changeState(){
		    if(gameManager != null){
			    gameManager.setUpdateState(targetState);
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
