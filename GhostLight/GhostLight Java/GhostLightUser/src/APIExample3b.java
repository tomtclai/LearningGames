import java.awt.event.KeyEvent;

import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject.ObjectType;

/**
 * Object Grid how to initialize and navigate
 * IntractableObjects How to Add them, Remove them and change there state through the objectGrid
 * Uses the 2D API
 * @author Michael Letter
 */
public class APIExample3b extends GhostLightInterface {

	@Override
	public void initialize() {
		//ObjectSet
		//used to gain access to the InteractableObject[Row][Colluumn]
		//Grid orientation
		//(0,0),(0,1) -> ....
		//(1,0),(1,1).
		//  |
		//	V
		//initializeing objectGrid to gave 5 rows and 6 collumns
		objectGrid.setObjectGrid(new InteractableObject[5][7]);
		//Note objectGrid objectGrid.setNumberOfRows(newRowCount) and objectGrid.setObjectGrid(InteractableObject[collumns], row) can be used to the same effect
		//However any Rows you do not set will remain untouched
		
		//IntractableObject
		InteractableObject newby = new InteractableObject();
		//set State
		newby.setObjectType(ObjectType.FRANKENSTEIN, true);	//Setting Type(Ghost, Zombie, etc...)
		newby.setMaxHealth(5);								//Setting Max Health 
		newby.setCurrentHealth(2);							//Setting current Health
		newby.setScore(9001); 								//Setting the number of points the enemy is worth
		
		//Revealed means that the enemies true for is shown and when revealed the 
		//		enemy will take whatever action it does when it is revealed
		//Partial revealed means that its "Friendliness is hinted at to the player"
		//		Pumpkins turn green everything else turns red
		newby.setRevealStatus(true); 
		
		//Add an enemy to the set at (Row 3, Collunn 4)
		InteractableObject[][] iOset =  objectGrid.getObjectGrid();
		iOset[2][3] = newby;
	}

	@Override
	public void update() {
		//Removing an Enemy 2 Ways
		
		//Set Health to zero
		//THis will result in the enemy being removed from the set and destroyed between this update and the next
		//you must leave the enemy in the array. if you want to remove the enemy from the array use the other method
		if(super.keyboard.isButtonTapped(KeyEvent.VK_1)){
			InteractableObject[][] iOset = objectGrid.getObjectGrid();
			iOset[2][3].setCurrentHealth(0);
		}
		//OR
		//Call .Destroy() and remove from array
		//Note you must remove the enemy from the array if you do not the enemy will simply revert to Default state
		if(super.keyboard.isButtonTapped(KeyEvent.VK_2)){
			InteractableObject[][] iOset = objectGrid.getObjectGrid();
			iOset[2][3].destroy();
			iOset[2][3] = null;
		}
		//Whatever you do do not simply remove the enemy from the array or bad things will happen
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
