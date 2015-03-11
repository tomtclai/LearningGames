package GhostLight.GhostEngine;

import Engine.MouseInput;
import MenueSystem.Button;
import MenueSystem.Button.transitionState;

public class CreditsButton extends Button 
{
	GhostLight gamemanager;
	
	CreditsButton( GhostLight manager )
	{
		gamemanager = manager;
	}
	
	public void autoUpdateState(MouseInput mouse){
		transitionState oldState = super.getSubState();
		super.autoUpdateState(mouse);
		if(super.getSubState() == transitionState.DOWN && super.getSubState() != oldState){
			gamemanager.transitionIntoCredits();
			
		}
	}
	
}
