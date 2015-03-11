

import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.InteractableObject.ObjectType;

/**
 * Example Level
 * Everything but Spiders
 * @author Michael Letter
 */
public class Level4 extends UserCode{
	
	public void initialize(){
		super.initialize();
		
		super.pointsToWin = 1000;
		super.gameState.setMessage("get 1000 points to Win");
	}
	
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
					//%15 Frankenstein
					else if(percent <= 0.80){
						retVal.setObjectType(ObjectType.FRANKENSTEIN, true);
					}
					//%10 Zombie
					else if(percent <= 0.90){
						retVal.setObjectType(ObjectType.ZOMBIE, true);
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
