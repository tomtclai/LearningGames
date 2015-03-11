using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using GhostFinder.Interface;
using GhostFinder.GhostEngine;
using CustomWindower.Driver;
using System.Windows.Forms;

/**
 *  Shows how to acheave the enemy snaking behaviour in levels 1 -> 4 
 *  using the 2D API
 * @author Michael Letter
 */
namespace user{
    public class APIExample6 : GhostFinderInterface {
	   
	    public override void initialize() {
		    //initializing objectGrid
		    base.objectGrid.setObjectGrid(new InteractableObject[8][]);
            for(int loop = 0; loop < base.objectGrid.getObjectGrid().Length; loop++){
                base.objectGrid.getObjectGrid()[loop] = new InteractableObject[18];
            }
            //Speeding up Animation
		    gameState.setAnimationTime(10);
	    }
	    public override void update() {
		    //shifing enemies when Enter is pressed
		    if(keyboard.isKeyDown(Keys.Enter)){
			    shiftEnemiesDown();
		    }
	    }
	    //shifts enemies in the curretn object grid down
	    protected void shiftEnemiesDown(){
		    InteractableObject[][] EnemyGrid = objectGrid.getObjectGrid();
		    //Shifting Enemies Down
		    InteractableObject lastEnemy = new InteractableObject();
		    bool shiftRight = true;			
    	    for(int loopRow = 0; loopRow < EnemyGrid.Length; loopRow++){
    		    lastEnemy = shiftRow(EnemyGrid, lastEnemy, loopRow, shiftRight);
    		    shiftRight = !shiftRight;
    	    }
    	    //Destroying Enemy moved out of array
    	    if(lastEnemy != null){
    		    lastEnemy.destroy();
    	    }
	    }
	    //Shifts all of the Enemies Down 1 index
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
	    public override void end() {
		
	    }
    }
}
