package GhostLight.Interface;

import GhostLight.GhostEngine.LightBeam;


/**
 * Offers Access to the flashlights position, mode, and the ability to activate the light
 * The Light can operate in one of 3 modes LASER, will target enemies Directly above the Light
 * MEDIUM will Target the Enemy Directly above the and the 2 on either side of it.
 * WIDE will Target the Enemy Directly above the and the 4 on either side of it
 * @author Michael Letter
 */
public class FlashLight {
	/**
	 * The lightTypes that this light can be used in
	 * The LASER Type will target Enemies in a Row Directly Above the light
	 * the MEDIUM will Target the Enemy Directly above the and the 2 on either side of it.
	 * The WIDE will Target the Enemy Directly above the and the 4 on either side of it
	 */
	public enum lightType {
	    MEDIUM, WIDE, LASER
	};
	
	private lightType type = lightType.MEDIUM;
	private int currentPosition = 0;
	private int MaxPosition = 0;
	private boolean lightIsActive = false;
	private boolean isDieing = false;
	
	/**
	 * Will Return what light Type the lightType is currently set Too
	 * @return Return what light Type the lightType is currently set Too
	 */
	public lightType getLightType(){
		return type;
	}
	/**
	 * Will Set the current light type to given lightType
	 * @param newType light Type that could be used
	 */
	public void setLightType(LightBeam.BeamType newType){
		type = convertType(newType);
	}
	/**
	 * Will Set the current light type to given lightType
	 * @param newType light Type that could be used
	 */
	public void setLightType(lightType newType){
		type = newType;
	}
	/**
	 * Will Return the current column that the light is centered under 
	 * @return the current column that the light is centered under 
	 */
	public int getPosition(){
		return currentPosition;
	}
	/**
	 * Will Move the Camera to be centered under the the given column 
	 * provided it is greater than zero and less than the current MaxPosition
	 * @param newPosition the Desired position of the light
	 * @return True if the position was successfully changed
	 */
	public boolean setPosition(int newPosition){
		if(newPosition >= 0 && newPosition <= MaxPosition){
			currentPosition = newPosition;
			return true;
		}
		return false;
	}
	public boolean getHurtState()
	{
		return isDieing;
	}
	public void setHurtState(boolean isHurt)
	{
		isDieing = isHurt;
	}
	/**
	 * Will update the current Maxposition based on the given InteractableObject[][]
	 * @param targetSet the set of Enemies this Light will find targets in. Note if no ObjectGrid was set this set cannot target any enemies
	 * @return the final value of MaxPosition
	 */
	public int updateMaxPosition(InteractableObject[][] targetSet){
		if(targetSet != null){
			MaxPosition = 0;
			for(int loopRows = 0; loopRows < targetSet.length; loopRows++){
				if(targetSet[loopRows] != null && targetSet[loopRows].length - 1 > MaxPosition){
					MaxPosition = targetSet[loopRows].length - 1;
				}
			}
		}
		return MaxPosition;
	}
	/**
	 * Will return the current Max position of the light
	 * @return the current Max position of the light
	 */
	public int getMaxPosition(){
		return MaxPosition;
	}
	/**
	 * will find and return the Enemies that were affected by the light
	 * @param targetSet the set of Enemies this Light will find targets in. Note if no ObjectGrid was set this set cannot target any enemies
	 * @return The array of InteractableObjects that are targeted by the light if this array is null then no enemies were effected
	 */
	public InteractableObject[] getTargetedEnemies(ObjectSet targetSet){
		if(targetSet != null && targetSet.getObjectGrid() != null){
			lightIsActive = true;
			//Laser
			if(type == lightType.LASER){
				return getTargetedEnemiesLASER(targetSet.getObjectGrid(), currentPosition);
			}
			//Wide
			else if(type == lightType.WIDE){
				return getTargetedEnemiesWIDE(targetSet.getObjectGrid(), currentPosition);
			}
			//Mid
			else{
				return getTargetedEnemiesMEDIUM(targetSet.getObjectGrid(), currentPosition);
			}
		}
		return null;
	}
	/**
	 * Will find and return row and Column positions of the affected by the light
	 * @param targetSet the set of Enemies this LightWill Shine Onto. Note if no ObjectGrid was set this set cannot target any enemies
	 * @return The array is organized as a 2D array[Number of Enemies Found][2] where array[?][0] = row position and array[?][1] = Column Position
	 */
	public int[][] getTargetedEnemyPositions(ObjectSet targetSet){
		if(targetSet != null && targetSet.getObjectGrid() != null){
			//Laser
			if(type == lightType.LASER){
				return getTargetedEnemyLocations2DLASER(targetSet.getObjectGrid(), currentPosition);
			}
			//Wide
			else if(type == lightType.WIDE){
				return getTargetedEnemyLocations2DWIDE(targetSet.getObjectGrid(), currentPosition);
			}
			//Mid
			else{
				return getTargetedEnemyLocations2DMEDIUM(targetSet.getObjectGrid(), currentPosition);
			}
		}
		return null;
	}
	/**
	 * Will Find and return the column positions of the enemies affected by the light
	 * @param targetSet the set of Enemies this Light will find targets in. Note if no ObjectGrid was set this set cannot target any enemies 
	 * @return the array is organized as a 1D array[Number of Enemies Found] where array[?] = the column where an enemy was found.
	 *  Note: there is one entry per enemy found. So duplicate column entries indicate more than one enemy is targeted in the column in question
	 */
	public int[] getTargetedEnemyColumns(ObjectSet targetSet){
		if(targetSet != null && targetSet.getObjectGrid() != null){
			//Laser
			if(type == lightType.LASER){
				return getTargetedEnemyLocations1DLASER(targetSet.getObjectGrid(), currentPosition);
			}
			//Wide
			else if(type == lightType.WIDE){
				return getTargetedEnemyLocations1DWIDE(targetSet.getObjectGrid(), currentPosition);
			}
			//Mid
			else{
				return getTargetedEnemyLocations1DMEDIUM(targetSet.getObjectGrid(), currentPosition);
			}
		}
		return null;
	}
	/**
	 *Find and return the row and column positions of the enemies affected by the light
	 * @param targetSet  the set of Enemies this Light will find targets in. Note if no IDArray was set this set cannot target any enemies
	 * @return The array is organized as a 2D array[Number of Enemies Found][2] where array[?][0] = row position and array[?][1] = Column Position
	 */
	public int[][] getTargetedEnemyPositions(PrimitiveSet targetSet){
		if(targetSet != null && targetSet.getIDArray() != null){
			//Laser
			if(type == lightType.LASER){
				return getTargetedEnemyLocations2DLASER(targetSet.getIDArray(), currentPosition);
			}
			//Wide
			else if(type == lightType.WIDE){
				return getTargetedEnemyLocations2DWIDE(targetSet.getIDArray(), currentPosition);
			}
			//Mid
			else{
				return getTargetedEnemyLocations2DMEDIUM(targetSet.getIDArray(), currentPosition);
			}
		}
		return null;
	}
	/**
	 * Will Find and return the column positions of the enemies affected by the light
	 * @param targetSet the set of Enemies this Light will find targets in. Note if no IDArray was set this set cannot target any enemies 
	 * @return the array is organized as a 1D array[Number of Enemies Found] where array[?] = the column where an enemy was found.
	 *  Note: there is one entry per enemy found. So duplicate column entries indicate more than one enemy is targeted in the column in question
	 */
	public int[] getTargetedEnemyColumns(PrimitiveSet targetSet){
		if(targetSet != null && targetSet.getIDArray() != null){
			//Laser
			if(type == lightType.LASER){
				return getTargetedEnemyLocations1DLASER(targetSet.getIDArray(), currentPosition);
			}
			//Wide
			else if(type == lightType.WIDE){
				return getTargetedEnemyLocations1DWIDE(targetSet.getIDArray(), currentPosition);
			}
			//Mid
			else{
				return getTargetedEnemyLocations1DMEDIUM(targetSet.getIDArray(), currentPosition);
			}
		}
		return null;
	}
	/**
	 * Will Turn on the visual of the light active
	 */
	public void activateLight(){
		lightIsActive = true;
	}
	/**
	 * Will Turn off the visual of the light active
	 */
	public void deactivateLight(){
		lightIsActive = false;
	}
	/**
	 * Returns whether or not the light is currently active
	 * @return whether or not the light is currently active
	 */
	public boolean isLightActive(){
		return lightIsActive;
	}
	/**
	 * Will convirt a given BeamType to the apropriate lightType 
	 * @param convertTarget The given BeamType
	 * @return the converted lightType
	 */
	private lightType convertType(LightBeam.BeamType convertTarget){
		if(convertTarget == LightBeam.BeamType.LASER){
			return lightType.LASER;
		}
		else if(convertTarget == LightBeam.BeamType.WIDE){
			return lightType.WIDE;
		}
		else {
			return lightType.LASER;
		}
	}
	/**
	 * Will convirt a given lightType to the apropriate BeamType 
	 * @param convertTarget The given lightType
	 * @return the converted BeamType
	 */
	private LightBeam.BeamType convertType(lightType convertTarget){
		if(convertTarget == lightType.LASER){
			return LightBeam.BeamType.LASER;
		}
		else if(convertTarget == lightType.WIDE){
			return LightBeam.BeamType.WIDE;
		}
		else{
			return LightBeam.BeamType.REVEAL;
		}
	}
	//Laser
	//InteractableObject
	/**
	 * Will Return the affected enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @return the the array of enemies that are targeted by the light
	 */
	private InteractableObject[] getTargetedEnemiesLASER(InteractableObject[][] targetSet, int targetCollumn){
		InteractableObject[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			retVal = new InteractableObject[targetSet.length];			
			for(int loopRows = 0; loopRows < targetSet.length; loopRows++){
				if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].length && targetSet[loopRows][targetCollumn] != null){
					retVal[loopRows] = targetSet[loopRows][targetCollumn];
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the affected enemies in the 
	 * @param targetSet the the array of enemies that are targeted by the light
	 * @return the locations of the enemies in the targetSet
	 */
	private int[][] getTargetedEnemyLocations2DLASER(InteractableObject[][] targetSet, int targetCollumn){
		int[][] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			boolean[] tempArray = new boolean[targetSet.length];
			int found = 0;
			//finding targets
			for(int loopRows = 0; loopRows < targetSet.length; loopRows++){
				if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].length && targetSet[loopRows][targetCollumn] != null){
					tempArray[loopRows] = true;
					found++;
				}
				else{
					tempArray[loopRows] = false;
				}
			}
			//formatting return array and returning it
			if(found > 0){
				retVal = new int[found][2];
				int loopRows = 0;
				int loopFound = 0;
				while(loopRows < targetSet.length && loopFound < found){
					if(tempArray[loopRows]){
						retVal[loopFound][0] = loopRows;
						retVal[loopFound][1] = targetCollumn;
						loopFound++;
					}
					loopRows++;
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the affected enemies in the 
	 * @param targetSet the the array of enemies that are targeted by the light
	 * @return the locations of the enemies in the targetSet
	 */
	private int[] getTargetedEnemyLocations1DLASER(InteractableObject[][] targetSet, int targetCollumn){
		int[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			boolean[] tempArray = new boolean[targetSet.length];
			int found = 0;
			//finding targets
			for(int loopRows = 0; loopRows < targetSet.length; loopRows++){
				if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].length && targetSet[loopRows][targetCollumn] != null){
					tempArray[loopRows] = true;
					found++;
				}
				else{
					tempArray[loopRows] = false;
				}
			}
			//formatting return array and returning it
			if(found > 0){
				retVal = new int[found];
				int loopRows = 0;
				int loopFound = 0;
				while(loopRows < targetSet.length && loopFound < found){
					if(tempArray[loopRows]){
						retVal[loopFound] = targetCollumn;
						loopFound++;
					}
					loopRows++;
				}
			}
		}
		return retVal;
	}
	//PrimitiveSet
	/**
	 * Will return the locations of the affected enemies in the 
	 * @param targetSet the the array of enemies that are targeted by the light
	 * @return the locations of the enemies in the targetSet
	 */
	private int[][] getTargetedEnemyLocations2DLASER(int[][] targetSet, int targetCollumn){
		int[][] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			boolean[] tempArray = new boolean[targetSet.length];
			int found = 0;
			//finding targets
			for(int loopRows = 0; loopRows < targetSet.length; loopRows++){
				if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].length && targetSet[loopRows][targetCollumn] > 0){
					tempArray[loopRows] = true;
					found++;
				}
				else{
					tempArray[loopRows] = false;
				}
			}
			//formatting return array and returning it
			if(found > 0){
				retVal = new int[found][2];
				int loopRows = 0;
				int loopFound = 0;
				while(loopRows < targetSet.length && loopFound < found){
					if(tempArray[loopRows]){
						retVal[loopFound][0] = loopRows;
						retVal[loopFound][1] = targetCollumn;
						loopFound++;
					}
					loopRows++;
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the affected enemies in the 
	 * @param targetSet the the array of enemies that are targeted by the light
	 * @return the locations of the enemies in the targetSet
	 */
	private int[] getTargetedEnemyLocations1DLASER(int[][] targetSet, int targetCollumn){
		int[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			boolean[] tempArray = new boolean[targetSet.length];
			int found = 0;
			//finding targets
			for(int loopRows = 0; loopRows < targetSet.length; loopRows++){
				if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].length && targetSet[loopRows][targetCollumn] > 0){
					tempArray[loopRows] = true;
					found++;
				}
				else{
					tempArray[loopRows] = false;
				}
			}
			//formatting return array and returning it
			if(found > 0){
				retVal = new int[found];
				int loopRows = 0;
				int loopFound = 0;
				while(loopRows < targetSet.length && loopFound < found){
					if(tempArray[loopRows]){
						retVal[loopFound] = targetCollumn;
						loopFound++;
					}
					loopRows++;
				}
			}
		}
		return retVal;
	}
	
	//Wide
	//InteractableObject
	/**
	 * Will Return the affected enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @return the the array of enemies that are targeted by the light
	 */
	private InteractableObject[] getTargetedEnemiesWIDE(InteractableObject[][] targetSet, int targetCollumn){
		InteractableObject[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			retVal = new InteractableObject[5];
			for(int loop = targetCollumn - 2; loop <= targetCollumn + 2; loop++){
				retVal[loop - (targetCollumn - 2)] = getClosest(targetSet, loop);
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[][] getTargetedEnemyLocations2DWIDE(InteractableObject[][] targetSet, int targetCollumn){
		int[][] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[5];
			int found = 0;
			for(int loop = targetCollumn - 2; loop <= targetCollumn + 2; loop++){
				tempArray[loop - (targetCollumn - 2)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found][2];
				int loop = 0;
				int loopFound = 0;
				while(loop < 5 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound][0] = tempArray[loop];
						retVal[loopFound][1] = targetCollumn - 2 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[] getTargetedEnemyLocations1DWIDE(InteractableObject[][] targetSet, int targetCollumn){
		int[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[5];
			int found = 0;
			for(int loop = targetCollumn - 2; loop <= targetCollumn + 2; loop++){
				tempArray[loop - (targetCollumn - 2)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found];
				int loop = 0;
				int loopFound = 0;
				while(loop < 5 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound] = targetCollumn - 2 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	//PrimitiveSet
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[][] getTargetedEnemyLocations2DWIDE(int[][] targetSet, int targetCollumn){
		int[][] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[5];
			int found = 0;
			for(int loop = targetCollumn - 2; loop <= targetCollumn + 2; loop++){
				tempArray[loop - (targetCollumn - 2)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found][2];
				int loop = 0;
				int loopFound = 0;
				while(loop < 5 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound][0] = tempArray[loop];
						retVal[loopFound][1] = targetCollumn - 2 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[] getTargetedEnemyLocations1DWIDE(int[][] targetSet, int targetCollumn){
		int[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[5];
			int found = 0;
			for(int loop = targetCollumn - 2; loop <= targetCollumn + 2; loop++){
				tempArray[loop - (targetCollumn - 2)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found];
				int loop = 0;
				int loopFound = 0;
				while(loop < 5 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound] = targetCollumn - 2 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	//Medium
	//InteractableObject
	/**
	 * Will Return the affected enemies in a new array
	 * @param targetSet the set that will be targeted by the light
	 * @return the the array of enemies that are targeted by the light
	 */
	private InteractableObject[] getTargetedEnemiesMEDIUM(InteractableObject[][] targetSet, int targetCollumn){
		InteractableObject[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			retVal = new InteractableObject[3];
			for(int loop = (targetCollumn - 1); loop <= targetCollumn + 1; loop++){
				retVal[loop - (targetCollumn - 1)] = getClosest(targetSet, loop);
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[][] getTargetedEnemyLocations2DMEDIUM(InteractableObject[][] targetSet, int targetCollumn){
		int[][] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[3];
			int found = 0;
			for(int loop = targetCollumn - 1; loop <= targetCollumn + 1; loop++){
				tempArray[loop - (targetCollumn - 1)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found][2];
				int loop = 0;
				int loopFound = 0;
				while(loop < 3 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound][0] = tempArray[loop];
						retVal[loopFound][1] = targetCollumn - 1 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[] getTargetedEnemyLocations1DMEDIUM(InteractableObject[][] targetSet, int targetCollumn){
		int[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[3];
			int found = 0;
			for(int loop = targetCollumn - 1; loop <= targetCollumn + 1; loop++){
				tempArray[loop - (targetCollumn - 1)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
				
			}
			if(found > 0){
				retVal = new int[found];
				int loop = 0;
				int loopFound = 0;
				while(loop < 3 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound] = targetCollumn - 1 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	//PrimitiveSet
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[][] getTargetedEnemyLocations2DMEDIUM(int[][] targetSet, int targetCollumn){
		int[][] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[3];
			int found = 0;
			for(int loop = targetCollumn - 1; loop <= targetCollumn + 1; loop++){
				tempArray[loop - (targetCollumn - 1)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found][2];
				int loop = 0;
				int loopFound = 0;
				while(loop < 3 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound][0] = tempArray[loop];
						retVal[loopFound][1] = targetCollumn - 1 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	/**
	 * Will return the locations of the targeted enemies in a new array
	 * @param targetSet the set that will be targted by the light
	 * @param targetCollumn
	 * @return
	 */
	private int[] getTargetedEnemyLocations1DMEDIUM(int[][] targetSet, int targetCollumn){
		int[] retVal = null;
		if(targetSet != null && targetCollumn >= 0){
			int[] tempArray = new int[3];
			int found = 0;
			for(int loop = targetCollumn - 1; loop <= targetCollumn + 1; loop++){
				tempArray[loop - (targetCollumn - 1)] = getClosestIndex(targetSet, loop);
				if(tempArray[loop - (targetCollumn - 1)] >= 0){
					found++;
				}
			}
			if(found > 0){
				retVal = new int[found];
				int loop = 0;
				int loopFound = 0;
				while(loop < 3 && loopFound < found){
					if(tempArray[loop] >= 0){
						retVal[loopFound] = targetCollumn - 1 + loop;
						loopFound++;
					}
					loop++;
				}
			}
		}
		return retVal;
	}
	//PrimitiveSet
	/**
	 * Will Return the InteractableObject that has the greatest index in the target Collumn
	 * assumes targetSet exists != null
	 * @return
	 */
	private InteractableObject getClosest(InteractableObject[][] targetSet, int targetCollumn){
		if(targetCollumn >= 0){
			for(int loop = targetSet.length -1; loop >= 0; loop--){
				if( targetSet[loop] != null && targetCollumn < targetSet[loop].length && targetSet[loop][targetCollumn] != null){
					return targetSet[loop][targetCollumn];
				}
			}
		}
		return null;
	}
	/**
	 * Return the the row location of the enemy with the greatest row location in the targetCollumn
	 * @param targetSet
	 * @param targetCollumn
	 * @return
	 */
	private int getClosestIndex(InteractableObject[][] targetSet, int targetCollumn){
		if(targetCollumn >= 0){
			for(int loop = targetSet.length -1; loop >= 0; loop--){
				if( targetSet[loop] != null && targetCollumn < targetSet[loop].length && targetSet[loop][targetCollumn] != null){
					return loop;
				}
			}
		}
		return -1;
	}
	/**
	 * Return the the row location of the enemy with the greatest row location in the targetCollumnW
	 * @param targetSet
	 * @param targetCollumn
	 * @return
	 */
	private int getClosestIndex(int[][] targetSet, int targetCollumn){
		if(targetCollumn >= 0){
			for(int loop = targetSet.length -1; loop >= 0; loop--){
				if( targetSet[loop] != null && targetCollumn < targetSet[loop].length && targetSet[loop][targetCollumn] > 0){
					return loop;
				}
			}
		}
		return -1;
	}
}
