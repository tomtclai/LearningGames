import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject;

/**
 *  Shows how to acheave the enemy snaking behaviour in levels 1 -> 4 
 *  using the 2D API
 * @author Michael Letter
 */
public class APIExample6 extends GhostLightInterface {

	@Override
	public void initialize() {
		//initializing objectGrid
		super.objectGrid.setObjectGrid(new InteractableObject[8][18]);
	}

	@Override
	public void update() {
		//shifing enemies when Enter is pressed
		if(keyboard.isButtonDown(KeyEvent.VK_ENTER)){
			shiftEnemiesDown();
		}
		//Speeding up Animation
		gameState.setAnimationTime(10);
	}
	//shifts enemies in the curretn object grid down
	protected void shiftEnemiesDown(){
		InteractableObject[][] EnemyGrid = objectGrid.getObjectGrid();
		//Shifting Enemies Down
		InteractableObject lastEnemy = new InteractableObject();
		boolean shiftRight = true;			
    	for(int loopRow = 0; loopRow < EnemyGrid.length; loopRow++){
    		lastEnemy = shiftRow(EnemyGrid, lastEnemy, loopRow, shiftRight);
    		shiftRight = !shiftRight;
    	}
    	//Destroying Enemy moved out of array
    	if(lastEnemy != null){
    		lastEnemy.destroy();
    	}
	}
	//Shifts all of the Enemies Down 1 index
	protected InteractableObject shiftRow(InteractableObject[][] EnemyGrid, InteractableObject newEnemy, int targetRow, boolean shiftRight) {
		InteractableObject lastEnemy = newEnemy;
    	//Shift Right
    	if(shiftRight){
	    	for(int loop = 0; loop < EnemyGrid[targetRow].length ; loop++){
	    		InteractableObject temp = EnemyGrid[targetRow][loop];
	    		EnemyGrid[targetRow][loop] = lastEnemy;
	    		lastEnemy = temp;
	    	}
    	}
    	//Shift Left
    	else{
    		for(int loop = EnemyGrid[targetRow].length - 1; loop >= 0; loop--){
    			InteractableObject temp = EnemyGrid[targetRow][loop];
	    		EnemyGrid[targetRow][loop] = lastEnemy;
	    		lastEnemy = temp;
	    	}
    	}
    	return lastEnemy;
    }
	
	@Override
	public void end() {
		
		
	}

}
