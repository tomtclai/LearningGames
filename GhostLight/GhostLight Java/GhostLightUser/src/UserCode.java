
import GhostLight.Interface.*;
import GhostLight.Interface.FlashLight.lightType;
import GhostLight.Interface.GameState.EndState;
import GhostLight.Interface.InteractableObject.ObjectType;
import GhostLight.Interface.MouseState.MouseButton;
import GhostLight.Interface.OnScreenButtons.ScreenButton;

import java.util.Vector;
import java.awt.Color;
import java.awt.event.KeyEvent;
/**
 * Core Game Code
 * @author Michael Letter
 */
public class UserCode extends GhostLightInterface{
	/*public static void main(String args[]){
		GhostGame game = new GhostGame(new UserCode());
		game.startProgram();
	}*/
	protected Vector<InteractableObject> spawedEnemies = new Vector<InteractableObject>();
	
	protected float LaserCost = 0.5f;
	protected float WideCost = 0.1f;
	protected float RevealCost = 0f;
	
	protected int pointsToWin = 3000;
	
	private float lastMousePositionX = 0;
	private float lastMousePositionY = 0;
	
	private float mouseMovementX = 0;
	private float mouseMovementY = 0;
	
	private static int NUMBER_OF_ROWS = 5;
	private static int NUMBER_OF_COLUMNS = 10;
	private static int ANIMATION_WAIT_TIME = 120;
	
	private int catBonus = 50; // Bonus when cat reaches bottom of screen.
	
	private boolean firstUpdate = false;
	
	public void initialize(){
		
		//setting gameState
		InteractableObject.setDefualtHealth(2);
		InteractableObject.setDefualtHealth(3, ObjectType.ANGRY);
		InteractableObject.setDefualtHealth(1, ObjectType.PUMPKIN);
		InteractableObject.setDefualtScoreInterval(5);
		
		InteractableObject.setDefualtMinScore(50, ObjectType.ANGRY);
		InteractableObject.setDefualtMaxScore(50, ObjectType.ANGRY);
		
		InteractableObject.setDefualtMinScore(10, ObjectType.CAT);
		InteractableObject.setDefualtMaxScore(10, ObjectType.CAT);
		InteractableObject.setDefualtHealth(1, ObjectType.CAT);
		
		InteractableObject.setDefualtMinScore(30, ObjectType.GHOST);
		InteractableObject.setDefualtMaxScore(30, ObjectType.GHOST);
		
		InteractableObject.setDefualtMinScore(10, ObjectType.FRANKENSTEIN);
		InteractableObject.setDefualtMaxScore(10, ObjectType.FRANKENSTEIN);
		
		InteractableObject.setDefualtMinScore(20, ObjectType.MUMMY);
		InteractableObject.setDefualtMaxScore(20, ObjectType.MUMMY);
		
		InteractableObject.setDefualtMinScore(5, ObjectType.PUMPKIN);
		InteractableObject.setDefualtMaxScore(20, ObjectType.PUMPKIN);
		
		InteractableObject.setDefualtMinScore(10, ObjectType.SPIDER);
		InteractableObject.setDefualtMaxScore(10, ObjectType.SPIDER);
		InteractableObject.setOption("dropUntilRow", NUMBER_OF_ROWS - 2, ObjectType.SPIDER );
		
		InteractableObject.setDefualtMinScore(40, ObjectType.VAMPIRE);
		InteractableObject.setDefualtMaxScore(40, ObjectType.VAMPIRE);
		
		InteractableObject.setDefualtMinScore(40, ObjectType.ZOMBIE);
		InteractableObject.setDefualtMaxScore(40, ObjectType.ZOMBIE);
		
		spawedEnemies.clear();
		
		LaserCost = 0.5f;
		WideCost = 0.1f;
		RevealCost = 0f;
		
		lastMousePositionX = 0;
		lastMousePositionY = 0;
		
		mouseMovementX = 0;
		mouseMovementY = 0;
		
		gameState.setHealth(4);
		gameState.setScore(0);
		gameState.setLightPower(0.5f);
		gameState.setAnimationTime(ANIMATION_WAIT_TIME);
		spawedEnemies.ensureCapacity(39);
		updatePowerBarColor();
		
		super.gameState.setMessage("get " + pointsToWin + " points to Win");
		
		//setting grid
		InteractableObject[][] EnemyGrid = new InteractableObject[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
		for(int loopY = 0; loopY < 2; loopY++){
			for(int loopX = 0; loopX < EnemyGrid[loopY].length; loopX++){
				EnemyGrid[loopY][loopX] = getRandomEnemy();
			}
		}
		objectGrid.setObjectGrid(EnemyGrid);
	}
	public void update(){
		if(firstUpdate){
			if(!gameState.setLightPower(gameState.getLightPower() + 0.25f)){
				gameState.setLightPower(1f);
			}
			firstUpdate = false;
			updatePowerBarColor();
		}
		updateMouseMovement();
		
		updateLight();
		if(gameState.getScore() >= pointsToWin){
			gameState.setGameEnd(EndState.WIN);
			light.setHurtState(true);
		}
		if(gameState.getHealth() == 1)
		{
			light.setHurtState(true);
		}
		if(gameState.getHealth() <= 0){
			gameState.setGameEnd(EndState.LOSE);
		}
	}
	public void end(){
		//Meh
	}
	//endsTurn
	protected void EndTurn(){
		shiftEnemiesDown();
		firstUpdate = true;
		gameState.markTurnEnd(true);
	}
	//Update Light
	protected void updateLight(){
		// Reset light
		light.deactivateLight();
		
		//Change Light Type
		if((keyboard.isButtonTapped(KeyEvent.VK_V) || keyboard.isButtonTapped(KeyEvent.VK_2)) && clickableButtons.isButtonActive(ScreenButton.WIDEBUTTON)) {
			light.setLightType(lightType.WIDE);
			updatePowerBarColor();
		}
		else if((keyboard.isButtonTapped(KeyEvent.VK_B) || keyboard.isButtonTapped(KeyEvent.VK_3)) && clickableButtons.isButtonActive(ScreenButton.LASERBUTTON)) {
			light.setLightType(lightType.LASER);
			updatePowerBarColor();
		}
		else if((keyboard.isButtonTapped(KeyEvent.VK_N) || keyboard.isButtonTapped(KeyEvent.VK_1)) && clickableButtons.isButtonActive(ScreenButton.MEDIUMBUTTON)) {
			light.setLightType(lightType.MEDIUM);
			updatePowerBarColor();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_W) || keyboard.isButtonTapped(KeyEvent.VK_UP) || mouse.isButtonTapped(MouseButton.RIGHT)){
			cycleLightTypeUp();
			updatePowerBarColor();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_S) || keyboard.isButtonTapped(KeyEvent.VK_DOWN) || mouse.isButtonTapped(MouseButton.MIDDLE)){
			cycleLightTypeDown();
			updatePowerBarColor();
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_A) || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
			setLightPosition(light.getPosition() - 1);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_D) || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
			setLightPosition(light.getPosition() + 1);
		}
		else if(mouseMovementX > 0.005 || mouseMovementX < -0.005){
			setLightPosition((int)(lastMousePositionX * NUMBER_OF_COLUMNS));
		}
		if(clickableButtons.isButtonActive(ScreenButton.WIDEBUTTON) && clickableButtons.isButtonTapped(ScreenButton.WIDEBUTTON)) {
			light.setLightType(lightType.WIDE);
			updatePowerBarColor();
		}
		else if(clickableButtons.isButtonActive(ScreenButton.LASERBUTTON) && clickableButtons.isButtonTapped(ScreenButton.LASERBUTTON)) {
			light.setLightType(lightType.LASER);
			updatePowerBarColor();
		}
		else if(clickableButtons.isButtonActive(ScreenButton.MEDIUMBUTTON) && clickableButtons.isButtonTapped(ScreenButton.MEDIUMBUTTON)) {
			light.setLightType(lightType.MEDIUM);
			updatePowerBarColor();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_SPACE) || mouse.isButtonTapped(MouseButton.LEFT) || keyboard.isButtonTapped(KeyEvent.VK_ENTER)){
			activateLight();
		}
	}
	protected void setLightPosition(int newLightPosition){
		light.setPosition(newLightPosition);
	}
	protected void activateLight(){
		//Deducting Energy
		//Laser
		if(light.getLightType() == lightType.LASER && gameState.getLightPower() >= LaserCost){
			if(!gameState.setLightPower(gameState.getLightPower() - LaserCost)){
				gameState.setLightPower(0);
			}
			//Activating Light
			light.activateLight();
			InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			if(effected != null){
				for(int loop = 0; loop < effected.length; loop++){
					if(effected[loop] != null){
						if(effected[loop].isRevealed()){
							effected[loop].setCurrentHealth(0);
							gameState.setScore(gameState.getScore() + effected[loop].getScore());
						}
						else{
							effected[loop].setRevealStatus(true);
						}
					}
				}
			}
			EndTurn();
		}
		//Wide
		else if(light.getLightType() == lightType.WIDE && gameState.getLightPower() >= WideCost){
			if(!gameState.setLightPower(gameState.getLightPower() - WideCost)){
				gameState.setLightPower(0);
			}
			//Activating Light
			light.activateLight();
			InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			if(effected != null){
				for(int loop = 0; loop < effected.length; loop++){
					if(effected[loop] != null){
						effected[loop].setPartialRevealStatus(true);
						if(effected[loop].isRevealed() && effected[loop].getInfectedTimer() <= 0){
							effected[loop].setCurrentHealth(effected[loop].getHealth() - 1);
							if(effected[loop].getHealth() <= 0){
								gameState.setScore(gameState.getScore() + effected[loop].getScore());
							}
						}
					}
				}
			}
			EndTurn();
		}
		//Reveal
		else if(light.getLightType() == lightType.MEDIUM && gameState.getLightPower() >= RevealCost){
			if(!gameState.setLightPower(gameState.getLightPower() - RevealCost)){
				gameState.setLightPower(0);
			}
			//Activating Light
			light.activateLight();
			InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			if(effected != null){
				if(effected[0] != null){
					effected[0].setPartialRevealStatus(true);
				}
				if(effected[1] != null){
					if(effected[1].getInfectedTimer() == 0){
						if(effected[1] != null){
							if(!effected[1].isRevealed()){
								effected[1].setRevealStatus(true);
							}
							else {
								effected[1].setCurrentHealth(effected[1].getHealth() - 1);
								if(effected[1].getHealth() <= 0){
									gameState.setScore(gameState.getScore() + effected[1].getScore());
								}
							}
						}
					}
					else{
						effected[1].setPartialRevealStatus(true);
					}
				}
				if(effected[2] != null){
					effected[2].setPartialRevealStatus(true);
				}
			}
			EndTurn();
		}
	}
	//shifts enemies in the curretn object grid down
	protected void shiftEnemiesDown(){
		InteractableObject[][] EnemyGrid = objectGrid.getObjectGrid();
		//Shifting Enemies Down
		InteractableObject lastEnemy = getRandomEnemy();
		boolean shiftRight = true;			
    	for(int loopRow = 0; loopRow < EnemyGrid.length; loopRow++){
    		lastEnemy = shiftRow(EnemyGrid, lastEnemy, loopRow, shiftRight);
    		shiftRight = !shiftRight;
    	}
    	if(lastEnemy != null){
    		// Cat gets a bonus when reaches the bottom.
    		if(lastEnemy.getType() == InteractableObject.ObjectType.CAT)
    		{
    			lastEnemy.destroy();
    			gameState.setScore(gameState.getScore() + catBonus);
    		}
    		else
    		{
        		lastEnemy.destroy();
        		if(lastEnemy.getHealth() > 0){
        			gameState.setHealth(gameState.getHealth() - 1);
    	    		//Enemies have reached the bottom shifting rows up
    	    		for(int loopRow = ((int)Math.ceil(EnemyGrid.length / 2f )) - 1; loopRow >= 0; loopRow--){
    	    			for(int loopCollumn = EnemyGrid[loopRow].length - 1; loopCollumn >= 0; loopCollumn--){
    	    				if(EnemyGrid[loopRow][loopCollumn] != null){
    	    					EnemyGrid[loopRow][loopCollumn].destroy();
    	    					spawedEnemies.add(EnemyGrid[loopRow][loopCollumn]);
    	    				}
    	    				EnemyGrid[loopRow][loopCollumn] = EnemyGrid[loopRow + EnemyGrid.length/2][loopCollumn];
    	    				EnemyGrid[loopRow + EnemyGrid.length/2][loopCollumn] = null;
    	    			}
    	    		}
        		}
    		}
    	}
	}
	//Todo: Remove this function and add to user code
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
	/**
	 * updates mouse variables
	 */
	private void updateMouseMovement(){
		float tempX = mouse.getMouseX();
		float tempY = mouse.getMouseY();
		mouseMovementX = lastMousePositionX - tempX;
		mouseMovementY = lastMousePositionY - tempY;
		lastMousePositionX = tempX;
		lastMousePositionY = tempY;
	}
	/**
	 * Laser -> Medium -> Wide -> Laser -> ....
	 */
	protected void cycleLightTypeDown(){
		if(light.getLightType() == lightType.LASER){
			if(clickableButtons.isButtonActive(ScreenButton.MEDIUMBUTTON)){
				light.setLightType(lightType.MEDIUM);
			}
			else if(clickableButtons.isButtonActive(ScreenButton.WIDEBUTTON)){
				light.setLightType(lightType.WIDE);
			}
		}
		else if(light.getLightType() == lightType.MEDIUM){
			if(clickableButtons.isButtonActive(ScreenButton.WIDEBUTTON)){
				light.setLightType(lightType.WIDE);
			}
			else if(clickableButtons.isButtonActive(ScreenButton.LASERBUTTON)){
				light.setLightType(lightType.LASER);
			}
		}
		else{
			if(clickableButtons.isButtonActive(ScreenButton.LASERBUTTON)){
				light.setLightType(lightType.LASER);
			}
			else if(clickableButtons.isButtonActive(ScreenButton.MEDIUMBUTTON)){
				light.setLightType(lightType.MEDIUM);
			}
		}
	}
	/**
	 * Laser -> Wide -> Medium -> Laser -> ...
	 */
	protected void cycleLightTypeUp(){
		if(light.getLightType() == lightType.LASER){
			if(clickableButtons.isButtonActive(ScreenButton.WIDEBUTTON)){
				light.setLightType(lightType.WIDE);
			}
			else if(clickableButtons.isButtonActive(ScreenButton.LASERBUTTON)){
				light.setLightType(lightType.LASER);
			}
		} 
		else if(light.getLightType() == lightType.MEDIUM){
			if(clickableButtons.isButtonActive(ScreenButton.LASERBUTTON)){
				light.setLightType(lightType.LASER);
			}
			else if(clickableButtons.isButtonActive(ScreenButton.MEDIUMBUTTON)){
				light.setLightType(lightType.MEDIUM);
			}
		}
		else{
			if(clickableButtons.isButtonActive(ScreenButton.MEDIUMBUTTON)){
				light.setLightType(lightType.MEDIUM);
			}
			else if(clickableButtons.isButtonActive(ScreenButton.WIDEBUTTON)){
				light.setLightType(lightType.WIDE);
			}
		}
	}
	protected void updatePowerBarColor(){
		if(light.getLightType() == lightType.LASER){
			if(gameState.getLightPower() < LaserCost){
				gameState.setPowerBarColor(Color.red);
			}
			else{
				gameState.setPowerBarColor(Color.green);
			}
		}
		else if(light.getLightType() == lightType.MEDIUM){
			if(gameState.getLightPower() < RevealCost){
				gameState.setPowerBarColor(Color.red);
			}
			else{
				gameState.setPowerBarColor(Color.green);
			}
		}
		else{
			if(gameState.getLightPower() <WideCost){
				gameState.setPowerBarColor(Color.red);
			}
			else{
				gameState.setPowerBarColor(Color.green);
			}
		}
	}
	/**
	 * Will return a random Enemy based on the Following Probabilities
	 * %35 Pumpkin
	 * %15 Ghost
	 * %10 Frankenstein
	 * %10 Mummy
	 * %10 Zombie
	 * %5 Cat
	 * %5 Spider
	 * %5 Vampire
	 * @return The Randomly Generated Enemy
	 */
	protected InteractableObject getRandomEnemy(){
		//checking Vector if Old Enemies are enqueued
		if(!spawedEnemies.isEmpty()){
			return spawedEnemies.remove(0);
		}
		else{
			//otherwise creating new Enemy
			InteractableObject retVal = new InteractableObject();
			double percent = Math.random();
			//%35 Pumpkin
			if(percent <= 0.35){
				retVal.setObjectType(ObjectType.PUMPKIN, true);
			}
			//%15 Ghost
			else if(percent <= 0.55){
				retVal.setObjectType(ObjectType.GHOST, true);
			}
			//%10 Mummy
			else if(percent <= 0.65){
				retVal.setObjectType(ObjectType.MUMMY, true);
			}
			//%10 Frankenstein
			else if(percent <= 0.75){
				retVal.setObjectType(ObjectType.FRANKENSTEIN, true);
			}
			//%10 Zombie
			else if(percent <= 0.85){
				retVal.setObjectType(ObjectType.ZOMBIE, true);
			}
			//%5 Spider
			else if(percent <= 0.90){
				retVal.setObjectType(ObjectType.SPIDER, true);
			}
			//%5 Cat
			else if(percent <= 0.95){
				retVal.setObjectType(ObjectType.CAT, true);
			}
			//%5 Dracula
			else{
				retVal.setObjectType(ObjectType.VAMPIRE, true);
			}
			return retVal;
		}
	}
}

