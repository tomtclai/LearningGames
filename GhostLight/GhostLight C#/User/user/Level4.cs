using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

/**
 * Example Level
 * Everything but Spiders
 * @author Michael Letter
 */
namespace user {
    public class Level4 : UserCode{
	
	    public override void initialize(){
		    base.initialize();

            base.pointsToWin = 1000;
            base.gameState.setMessage("get 1000 points to Win");
	    }
	
	    protected override InteractableObject getRandomEnemy(){
		    //checking Vector if Old Enemies are enqueued
			if(spawedEnemies.Count > 0){
				return spawedEnemies.Dequeue();
			}
			else{
				//otherwise creating new Enemy
				InteractableObject retVal = new InteractableObject();
				double percent = base.rand.NextDouble();
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
				//%15 Frankenstein
				else if(percent <= 0.80){
                    retVal.setObjectType(InteractableObject.ObjectType.FRANKENSTEIN, true);
				}
				//%10 Zombie
				else if(percent <= 0.90){
                    retVal.setObjectType(InteractableObject.ObjectType.ZOMBIE, true);
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
