using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

/**
 * Core Game Code
 * @author Michael Letter
 */
namespace user{
    public class UserCode : GhostFinderInterface{
	    /*public static void main(String args[]){
		    GhostGame game = new GhostGame(new UserCode());
		    game.startProgram();
	    }*/
        protected Queue<InteractableObject> spawedEnemies = new Queue<InteractableObject>();
	
	    protected float LaserCost = 0.5f;
	    protected float WideCost = 0.1f;
	    protected float RevealCost = 0f;

        public bool selectLazerTapped { get; private set; }
        public bool selectLazerDown { get; private set; }

        public bool selectWideTapped { get; private set; }
        public bool selectWideDown { get; private set; }

        public bool selectMediumTapped { get; private set; }
        public bool selectMediumDown { get; private set; }

        public bool cycleLightUpTapped { get; private set; }
        public bool cycleLightUpDown { get; private set; }

        public bool cycleLightDownTapped { get; private set; }
        public bool cycleLightDownDown { get; private set; }

        public bool activateLightTapped { get; private set; }
        public bool activateLightDown { get; private set; }

        public bool moveLeftTapped { get; private set; }
        public bool moveLeftDown { get; private set; }

        public bool moveRightTapped { get; private set; }
        public bool moveRightDown { get; private set; }

	    protected int pointsToWin = 3000;
	
	    private float lastMousePositionX = 0;
	    private float lastMousePositionY = 0;
	
	    private float mouseMovementX = 0;
	    private float mouseMovementY = 0;
	
	    private bool firstUpdate = false;

        protected Random rand = new Random();
	
	    public override void initialize(){
		
		    //setting gameState
		    InteractableObject.setDefualtHealth(2);
		    InteractableObject.setDefualtHealth(3, InteractableObject.ObjectType.ANGRY);
		    InteractableObject.setDefualtHealth(1, InteractableObject.ObjectType.PUMPKIN);
		    InteractableObject.setDefualtScoreInterval(5);
		
		    InteractableObject.setDefualtMinScore(50, InteractableObject.ObjectType.ANGRY);
		    InteractableObject.setDefualtMaxScore(50, InteractableObject.ObjectType.ANGRY);
		
		    InteractableObject.setDefualtMinScore(10, InteractableObject.ObjectType.CAT);
		    InteractableObject.setDefualtMaxScore(10, InteractableObject.ObjectType.CAT);
		    InteractableObject.setDefualtHealth(1, InteractableObject.ObjectType.CAT);
		
		    InteractableObject.setDefualtMinScore(30, InteractableObject.ObjectType.GHOST);
		    InteractableObject.setDefualtMaxScore(30, InteractableObject.ObjectType.GHOST);
		
		    InteractableObject.setDefualtMinScore(10, InteractableObject.ObjectType.FRANKENSTEIN);
		    InteractableObject.setDefualtMaxScore(10, InteractableObject.ObjectType.FRANKENSTEIN);
		
		    InteractableObject.setDefualtMinScore(20, InteractableObject.ObjectType.MUMMY);
		    InteractableObject.setDefualtMaxScore(20, InteractableObject.ObjectType.MUMMY);
		
		    InteractableObject.setDefualtMinScore(5, InteractableObject.ObjectType.PUMPKIN);
		    InteractableObject.setDefualtMaxScore(25, InteractableObject.ObjectType.PUMPKIN);
		
		    InteractableObject.setDefualtMinScore(10, InteractableObject.ObjectType.SPIDER);
		    InteractableObject.setDefualtMaxScore(10, InteractableObject.ObjectType.SPIDER);
		
		    InteractableObject.setDefualtMinScore(40, InteractableObject.ObjectType.VAMPIRE);
		    InteractableObject.setDefualtMaxScore(40, InteractableObject.ObjectType.VAMPIRE);
		
		    InteractableObject.setDefualtMinScore(40, InteractableObject.ObjectType.ZOMBIE);
		    InteractableObject.setDefualtMaxScore(40, InteractableObject.ObjectType.ZOMBIE);
		
		    spawedEnemies.Clear();
		
		    LaserCost = 0.5f;
		    WideCost = 0.1f;
		    RevealCost = 0f;

		    lastMousePositionX = 0;
		    lastMousePositionY = 0;
		
		    mouseMovementX = 0;
		    mouseMovementY = 0;
		
		    gameState.setHealth(5);
		    gameState.setScore(0);
		    gameState.setLightPower(0.5f);
		    gameState.setAnimationTime(30);
		    updatePowerBarColor();
		
		    base.gameState.setMessage("get 3000 points to Win");
		
		    //setting grid
		    InteractableObject[][] EnemyGrid = new InteractableObject[6][];
		    for(int loopY = 0; loopY < 6; loopY++){
                EnemyGrid[loopY] = new InteractableObject[13];
                if(loopY < 2){
			        for(int loopX = 0; loopX < EnemyGrid[loopY].Length; loopX++){
				        EnemyGrid[loopY][loopX] = getRandomEnemy();
			        }
                }
		    }
		    objectGrid.setObjectGrid(EnemyGrid);
	    }
	    public override void update(){
            updateKeyInputStatuses();
		    if(firstUpdate){
			    if(!gameState.setLightPower(gameState.getLightPower() + 0.25f)){
				    gameState.setLightPower(1f);
			    }
			    firstUpdate = false;
			    updatePowerBarColor();
			    light.deactivateLight();
		    }
		    updateMouseMovement();
		    updateLight();
		    if(gameState.getScore() >= pointsToWin){
			    gameState.setGameEnd(GameState.EndState.WIN);
		    }
		    if(gameState.getHealth() <= 0){
                gameState.setGameEnd(GameState.EndState.LOSE);
		    }
	    }
	    public override void end(){
		    //Meh
	    }
	    //endsTurn
	    protected virtual void EndTurn(){
		    shiftEnemiesDown();
		    firstUpdate = true;
		    gameState.markTurnEnd(true);
	    }
	    //Update Light
	    protected virtual void updateLight(){
		    //Change Light Type
		    if(selectWideTapped && clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.WIDEBUTTON)){
                light.setLightType(FlashLight.lightType.WIDE);
			    updatePowerBarColor();
		    }
		    else if(selectLazerTapped && clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.LASERBUTTON)) {
                light.setLightType(FlashLight.lightType.LASER);
			    updatePowerBarColor();
		    }
		    else if(selectMediumTapped && clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.MEDIUMBUTTON)) {
			    light.setLightType(FlashLight.lightType.MEDIUM);
			    updatePowerBarColor();
		    }
		    else if(cycleLightUpTapped){
			    cycleLightTypeUp();
			    updatePowerBarColor();
		    }
		    else if(cycleLightDownTapped){
			    cycleLightTypeDown();
			    updatePowerBarColor();
		    }
		    if(moveLeftTapped) {
			    setLightPosition(light.getPosition() - 1);
		    }
		    else if(moveRightTapped) {
			    setLightPosition(light.getPosition() + 1);
		    }
		    else if(mouseMovementX > 0.005 || mouseMovementX < -0.005){
			    setLightPosition((int)(lastMousePositionX*13));
		    }
		    else if(activateLightTapped){
			    activateLight();
		    }
	    }
        /// <summary>
        /// Updates the button Tapped and button Down Buttons
        /// </summary>
        public void updateKeyInputStatuses(){
            //Select Lazer
            if(base.keyboard.isKeyDown(Keys.D3) || base.clickableButtons.isButtonDown(OnScreenButtons.ScreenButton.LASERBUTTON)){
                if(!selectLazerDown){
                    selectLazerDown = true;
                    selectLazerTapped = true;
                }
                else{
                    selectLazerTapped = false;
                }
            }
            else{
                selectLazerDown = false;
                selectLazerTapped = false;
            }
            //Select Wide
            if (base.keyboard.isKeyDown(Keys.D2) || base.clickableButtons.isButtonDown(OnScreenButtons.ScreenButton.WIDEBUTTON)) {
                if(!selectWideDown){
                    selectWideDown = true;
                    selectWideTapped = true;
                }
                else{
                    selectWideTapped = false;
                }
            }
            else{
                selectWideDown = false;
                selectWideTapped = false;
            }
            //Select Medium
            if(base.keyboard.isKeyDown(Keys.D1) || base.clickableButtons.isButtonDown(OnScreenButtons.ScreenButton.MEDIUMBUTTON)){
                if(!selectMediumDown){
                    selectMediumDown = true;
                    selectMediumTapped = true;
                }
                else{
                    selectMediumTapped = false;
                }
            }
            else{
                selectMediumDown = false;
                selectMediumTapped = false;
            }
            //Cycle Lights Up
            if(base.keyboard.isKeyDown(Keys.W) || base.keyboard.isKeyDown(Keys.Up) || base.mouse.isButtonDown(MouseState.MouseButton.MIDDLE)){
                if(!cycleLightUpDown){
                    cycleLightUpDown = true;
                    cycleLightUpTapped = true;
                }
                else{
                    cycleLightUpTapped = false;
                }
            }
            else{
                cycleLightUpDown = false;
                cycleLightUpTapped = false;
            }
            //Cycle Lights Down
            if(base.keyboard.isKeyDown(Keys.S) || base.keyboard.isKeyDown(Keys.Down) || base.mouse.isButtonDown(MouseState.MouseButton.RIGHT)){
                if(!cycleLightDownDown){
                    cycleLightDownDown = true;
                    cycleLightDownTapped = true;
                }
                else{
                    cycleLightDownTapped = false;
                }
            }
            else{
                cycleLightDownDown = false;
                cycleLightDownTapped = false;
            }
            //Move Left
            if(base.keyboard.isKeyDown(Keys.A) || base.keyboard.isKeyDown(Keys.Left)){
                if(!moveLeftDown){
                    moveLeftDown = true;
                    moveLeftTapped = true;
                }
                else{
                    moveLeftTapped = false;
                }
            }
            else{
                moveLeftDown = false;
                moveLeftTapped = false;
            }
            //Move Right
            if(base.keyboard.isKeyDown(Keys.D) || base.keyboard.isKeyDown(Keys.Right)){
                if(!moveRightDown){
                    moveRightDown = true;
                    moveRightTapped = true;
                }
                else{
                    moveRightTapped = false;
                }
            }
            else{
                moveRightDown = false;
                moveRightTapped = false;
            }
            //Activate Light
            if(!selectLazerDown && !selectWideDown && !selectMediumDown && (base.keyboard.isKeyDown(Keys.Space) || base.keyboard.isKeyDown(Keys.Enter) || mouse.isButtonDown(MouseState.MouseButton.LEFT))){
                if(!activateLightDown){
                    activateLightDown = true;
                    activateLightTapped = true;
                }
                else{
                    activateLightTapped = false;
                }
            }
            else{
                activateLightDown = false;
                activateLightTapped = false;
            }
        }
	    protected void setLightPosition(int newLightPosition){
		    light.setPosition(newLightPosition);
	    }
	    protected void activateLight(){
		    //Deducting Energy
		    //Laser
		    if(light.getLightType() == FlashLight.lightType.LASER && gameState.getLightPower() >= LaserCost){
			    if(!gameState.setLightPower(gameState.getLightPower() - LaserCost)){
				    gameState.setLightPower(0);
			    }
			    //Activating Light
			    light.activateLight();
			    InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			    if(effected != null){
				    for(int loop = 0; loop < effected.Length; loop++){
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
            else if (light.getLightType() == FlashLight.lightType.WIDE && gameState.getLightPower() >= WideCost)
            {
			    if(!gameState.setLightPower(gameState.getLightPower() - WideCost)){
				    gameState.setLightPower(0);
			    }
			    //Activating Light
			    light.activateLight();
			    InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			    if(effected != null){
				    for(int loop = 0; loop < effected.Length; loop++){
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
		    else if(light.getLightType() == FlashLight.lightType.MEDIUM && gameState.getLightPower() >= RevealCost){
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
		    bool shiftRight = true;			
    	    for(int loopRow = 0; loopRow < EnemyGrid.Length; loopRow++){
    		    lastEnemy = shiftRow(EnemyGrid, lastEnemy, loopRow, shiftRight);
    		    shiftRight = !shiftRight;
    	    }
    	    if(lastEnemy != null){
    		    lastEnemy.destroy();
    		    if(lastEnemy.getHealth() > 0){
    			    gameState.setHealth(gameState.getHealth() - 1);
	    		    //Enemies have reached the bottom shifting rows up
	    		    for(int loopRow = EnemyGrid.Length/2 - 1; loopRow >= 0; loopRow--){
	    			    for(int loopCollumn = EnemyGrid[loopRow].Length - 1; loopCollumn >= 0; loopCollumn--){
	    				    if(EnemyGrid[loopRow][loopCollumn] != null){
	    					    EnemyGrid[loopRow][loopCollumn].destroy();
	    					    spawedEnemies.Enqueue(EnemyGrid[loopRow][loopCollumn]);
	    				    }
                            EnemyGrid[loopRow][loopCollumn] = EnemyGrid[loopRow + EnemyGrid.Length / 2][loopCollumn];
                            EnemyGrid[loopRow + EnemyGrid.Length / 2][loopCollumn] = null;
	    			    }
	    		    }
    		    }
    	    }
	    }
	    //Todo: Remove this function and add to user code
	    protected InteractableObject shiftRow(InteractableObject[][] EnemyGrid, InteractableObject newEnemy, int targetRow, bool shiftRight) {
		    InteractableObject lastEnemy = newEnemy;
    	    //Shift Right
    	    if(shiftRight){
	    	    for(int loop = 0; loop < EnemyGrid[targetRow].Length ; loop++){
	    		    InteractableObject temp = EnemyGrid[targetRow][loop];
	    		    EnemyGrid[targetRow][loop] = lastEnemy;
	    		    lastEnemy = temp;
	    	    }
    	    }
    	    //Shift Left
    	    else{
    		    for(int loop = EnemyGrid[targetRow].Length - 1; loop >= 0; loop--){
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
		    if(light.getLightType() == FlashLight.lightType.LASER){
			    if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.MEDIUMBUTTON)){
                    light.setLightType(FlashLight.lightType.MEDIUM);
			    }
                else if (clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.WIDEBUTTON))
                {
                    light.setLightType(FlashLight.lightType.WIDE);
			    }
		    }
		    else if(light.getLightType() == FlashLight.lightType.MEDIUM){
			    if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.WIDEBUTTON)){
                    light.setLightType(FlashLight.lightType.WIDE);
			    }
			    else if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.LASERBUTTON)){
                    light.setLightType(FlashLight.lightType.LASER);
			    }
		    }
		    else{
			    if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.LASERBUTTON)){
                    light.setLightType(FlashLight.lightType.LASER);
			    }
			    else if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.MEDIUMBUTTON)){
                    light.setLightType(FlashLight.lightType.MEDIUM);
			    }
		    }
	    }
	    /**
	     * Laser -> Wide -> Medium -> Laser -> ...
	     */
	    protected void cycleLightTypeUp(){
		    if(light.getLightType() == FlashLight.lightType.LASER){
			    if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.WIDEBUTTON)){
                    light.setLightType(FlashLight.lightType.WIDE);
			    }
			    else if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.LASERBUTTON)){
                    light.setLightType(FlashLight.lightType.LASER);
			    }
		    } 
		    else if(light.getLightType() == FlashLight.lightType.MEDIUM){
			    if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.LASERBUTTON)){
                    light.setLightType(FlashLight.lightType.LASER);
			    }
			    else if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.MEDIUMBUTTON)){
                    light.setLightType(FlashLight.lightType.MEDIUM);
			    }
		    }
		    else{
			    if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.MEDIUMBUTTON)){
                    light.setLightType(FlashLight.lightType.MEDIUM);
			    }
			    else if(clickableButtons.isButtonActive(OnScreenButtons.ScreenButton.WIDEBUTTON)){
                    light.setLightType(FlashLight.lightType.WIDE);
			    }
		    }
	    }
	    protected void updatePowerBarColor(){
		    if(light.getLightType() == FlashLight.lightType.LASER){
			    if(gameState.getLightPower() < LaserCost){
				    gameState.setPowerBarColor(Color.Red);
			    }
			    else{
				    gameState.setPowerBarColor(Color.Green);
			    }
		    }
		    else if(light.getLightType() == FlashLight.lightType.MEDIUM){
			    if(gameState.getLightPower() < RevealCost){
				    gameState.setPowerBarColor(Color.Red);
			    }
			    else{
				    gameState.setPowerBarColor(Color.Green);
			    }
		    }
		    else{
			    if(gameState.getLightPower() <WideCost){
				    gameState.setPowerBarColor(Color.Red);
			    }
			    else{
				    gameState.setPowerBarColor(Color.Green);
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
	    protected virtual InteractableObject getRandomEnemy(){
		    //checking Vector if Old Enemies are enqueued
		    if(spawedEnemies.Count() > 0){
			    return spawedEnemies.Dequeue();
		    }
		    else{
			    //otherwise creating new Enemy
			    InteractableObject retVal = new InteractableObject();
                double percent = rand.NextDouble(); ;
			    //%35 Pumpkin
			    if(percent <= 0.35){
                    retVal.setObjectType(InteractableObject.ObjectType.PUMPKIN, true);
			    }
			    //%15 Ghost
			    else if(percent <= 0.55){
                    retVal.setObjectType(InteractableObject.ObjectType.GHOST, true);
			    }
			    //%10 Mummy
			    else if(percent <= 0.65){
                    retVal.setObjectType(InteractableObject.ObjectType.MUMMY, true);
			    }
			    //%10 Frankenstein
			    else if(percent <= 0.75){
                    retVal.setObjectType(InteractableObject.ObjectType.FRANKENSTEIN, true);
			    }
			    //%10 Zombie
			    else if(percent <= 0.85){
                    retVal.setObjectType(InteractableObject.ObjectType.ZOMBIE, true);
			    }
			    //%5 Spider
			    else if(percent <= 0.90){
                    retVal.setObjectType(InteractableObject.ObjectType.SPIDER, true);
			    }
			    //%5 Cat
			    else if(percent <= 0.95){
                    retVal.setObjectType(InteractableObject.ObjectType.CAT, true);
			    }
			    //%5 Dracula
			    else{
                    retVal.setObjectType(InteractableObject.ObjectType.VAMPIRE, true);
			    }
			    return retVal;
		    }
	    }
    }
}
