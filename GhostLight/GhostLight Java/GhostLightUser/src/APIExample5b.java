import java.awt.event.KeyEvent;

import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.FlashLight.lightType;

/**
 * The Light, How to move the light Activate it change Light get the affected Objects
 * using the 2D API
 * @author Michael Letter
 */
public class APIExample5b extends GhostLightInterface {

	@Override
	public void initialize() {
		//See APIExample3 for an explanation of the code following in this function
		InteractableObject[][] EnemyGrid = new InteractableObject[10][20];
		for(int loopY = 0; loopY < 10; loopY++){
			for(int loopX = 0; loopX < EnemyGrid[loopY].length; loopX++){
				EnemyGrid[loopY][loopX] = new InteractableObject();
			}
		}
		objectGrid.setObjectGrid(EnemyGrid);
	}

	@Override
	public void update() {
		//Moving Light Left
		if(keyboard.isButtonTapped(KeyEvent.VK_A) || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
			light.setPosition(light.getPosition() - 1); //Decrementing position
		}
		//Moving Light Right
		else if(keyboard.isButtonTapped(KeyEvent.VK_D) || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
			light.setPosition(light.getPosition() + 1); //incrementing position
		}
		//Setting Light Type
		//Wide
		if(keyboard.isButtonTapped(KeyEvent.VK_V)) {
			light.setLightType(lightType.WIDE);
		}
		//Laser
		else if(keyboard.isButtonTapped(KeyEvent.VK_B)) {
			light.setLightType(lightType.LASER);
		}
		//Normal
		else if(keyboard.isButtonTapped(KeyEvent.VK_N)) {
			light.setLightType(lightType.MEDIUM);
		}
		//Activating Light
		//Activate light and remove enemies touched by it
		if(keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
			InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			//Activating the light returns an array that contains the enemies in the objectGrid that were touched by the light
			if(effected != null){	//This array is not garunteed to exist
				for(int loop = 0; loop < effected.length; loop++){
					if(effected[loop] != null){	//nor is this array garunteed to be full
						effected[loop].setCurrentHealth(0);
					}
				}
			}
		}
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
