import java.awt.event.KeyEvent;

import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject.ObjectType;

/**
 * Moving Enemies, updating turn, and setAnimationTime
 * As well as setting Default InteractableObject State
 * Uses the 2D API
 * @author Michael Letter
 */
public class APIExample4b extends GhostLightInterface {

	@Override
	public void initialize() {
		//Setting Default Status
		//This allows you to set the health and score that enemies of a given objectType start with
		//All enemies should start with 4 health
		InteractableObject.setDefualtHealth(4);
		//Uhhh... Except Angry Ghost they should only start with 2 health
		InteractableObject.setDefualtHealth(2, ObjectType.ANGRY);
		//All Pumpkins should start worth 13 points
		InteractableObject.setDefualtMaxScore(13, ObjectType.PUMPKIN);
		InteractableObject.setDefualtMinScore(13, ObjectType.PUMPKIN);
		//All Ghosts should start worth between 10 and 50 points with intervals of 10 points
		InteractableObject.setDefualtMaxScore(50, ObjectType.GHOST);
		InteractableObject.setDefualtMinScore(10, ObjectType.GHOST);
		InteractableObject.setDefualtScoreInterval(10, ObjectType.GHOST);
		
		//The number updates that will be missed when animation occures
		//increasing this number makes animation go longer
		//Decreasing this number makes animations go slower
		//setting as 0 will make it so they don't happen at all
		gameState.setAnimationTime(20);
		
		//See APIExample3b for an explanation of the code following in this function
		InteractableObject[][] iOset = new InteractableObject[5][5];
		iOset[2][0] = new InteractableObject();
		iOset[2][4] = new InteractableObject();
		iOset[2][0].setObjectType(ObjectType.PUMPKIN, true);
		iOset[2][0].setRevealStatus(true);
		iOset[2][4].setObjectType(ObjectType.GHOST, true);
		iOset[2][4].setRevealStatus(true);
		
		objectGrid.setObjectGrid(iOset);
		
	}

	@Override
	public void update() {
		//to Move an Enemy simply move it to the array position you want it to reside at
		if(keyboard.isButtonTapped(KeyEvent.VK_ENTER)){
			InteractableObject[][] iOset = objectGrid.getObjectGrid();
			 InteractableObject temp = iOset[2][0];
			 iOset[2][0] = iOset[2][4];
			 iOset[2][4] = temp;
		}
		//Updating turn
		//Some enemies evolve over time and they are allowed to do this at the passing of a turn
		//This is done by marking this update as the last update in a turn
		//Note this evolution will not occur until the conclusion of this update and the
		//effects will not be visible until the next update
		if(keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
			gameState.markTurnEnd(true);
		}
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
