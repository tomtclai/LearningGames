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
 * The Light, How to move the light Activate it change Light get the affected Objects
 * using the 2D API
 * @author Michael Letter
 */
namespace user{
    public class APIExample5b : GhostFinderInterface {

        bool movedLeftConfirm = false;
        bool movedRightConfirm = false;
        int lightChangeComfirm = 0;
        bool lightActiveComfirm = false;

	    public override void initialize() {
		    //See APIExample3 for an explanation of the code following in this function
		    InteractableObject[][] EnemyGrid = new InteractableObject[10][];
		    for(int loopY = 0; loopY < 10; loopY++){
                EnemyGrid[loopY] = new InteractableObject[20];
			    for(int loopX = 0; loopX < EnemyGrid[loopY].Length; loopX++){
				    EnemyGrid[loopY][loopX] = new InteractableObject();
			    }
		    }
		    objectGrid.setObjectGrid(EnemyGrid);
	    }
	    public override void update() {
		    //Moving Light Left
		    if(keyboard.isKeyDown(Keys.A) || keyboard.isKeyDown(Keys.Left)) {
                if(!movedLeftConfirm){
			        light.setPosition(light.getPosition() - 1); //Decrementing position
                    movedLeftConfirm = true;
                }
		    }
            else{
                movedLeftConfirm = false;
            }
		    //Moving Light Right
		    if(keyboard.isKeyDown(Keys.D) || keyboard.isKeyDown(Keys.Right)) {
                if(!movedRightConfirm){
			        light.setPosition(light.getPosition() + 1); //incrementing position
                    movedRightConfirm = true;
                }
		    }
            else{
                movedRightConfirm = false;
            }
		    //Setting Light Type
		    //Wide
		    if(keyboard.isKeyDown(Keys.V)) {
                if(lightChangeComfirm != 1){
                    light.setLightType(LightBeam.BeamType.WIDE);
                    lightChangeComfirm = 1;
                }
		    }
		    //Laser
		    else if(keyboard.isKeyDown(Keys.B)) {
                if(lightChangeComfirm != 2){
			        light.setLightType(LightBeam.BeamType.LASER);
                    lightChangeComfirm = 2;
                }
		    }
		    //Normal
		    else if(keyboard.isKeyDown(Keys.N)) {
                if(lightChangeComfirm != 3){
                    light.setLightType(LightBeam.BeamType.REVEAL);
                    lightChangeComfirm = 3;
                }
		    }
            else{
                lightChangeComfirm = 0;
            }
		    //Activating Light
		    //Activate light and remove enemies touched by it
		    if(keyboard.isKeyDown(Keys.Space)){
                if(!lightActiveComfirm){
			        InteractableObject[] effected = light.getTargetedEnemies(objectGrid);
			        //Activating the light returns an array that contains the enemies in the objectGrid that were touched by the light
			        if(effected != null){	//This array is not garunteed to exist
				        for(int loop = 0; loop < effected.Length; loop++){
					        if(effected[loop] != null){	//nor is this array garunteed to be full
						        effected[loop].setCurrentHealth(0);
					        }
				        }
			        }
                    lightActiveComfirm = true;
                }
		    }
            else{
                lightActiveComfirm = false;
            }
	    }
	    public override void end() {
		    // TODO Auto-generated method stub
		
	    }
    }
}
