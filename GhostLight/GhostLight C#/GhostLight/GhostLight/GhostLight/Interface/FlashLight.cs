using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using GhostFinder.GhostEngine;


namespace GhostFinder.Interface{
    /// <summary>
    /// Offers Access to the flashlights position, mode, and the ability to activate the light
    /// The Light can operate in one of 3 modes LASER, will target enemies Directly above the Light
    /// MEDIUM will Target the Enemy Directly above the and the 2 on either side of it.
    /// WIDE will Target the Enemy Directly above the and the 4 on either side of it
    /// author: Michael Letter
    /// </summary>
    public class FlashLight {
	    /// <summary>
	    /// The lightTypes that this light can be used in
	    /// The LASER Type will target Enemies in a Row Directly Above the light
	    /// the MEDIUM will Target the Enemy Directly above the and the 2 on either side of it.
        /// The WIDE will Target the Enemy Directly above the and the 4 on either side of it
	    /// </summary>
	    public enum lightType {
	        MEDIUM, WIDE, LASER
	    };
	
	    private lightType type = lightType.MEDIUM;
	    private int currentPosition = 0;
	    private int MaxPosition = 0;
	    private bool lightIsActive = false;

	    /// <summary>
        /// Will Return what light Type the lightType is currently set Too
	    /// </summary>
        /// <returns>what light Type the lightType is currently set Too</returns>
	    public lightType getLightType(){
		    return type;
	    }
	    /// <summary>
        /// Will Set the current light type to given lightType
	    /// </summary>
        /// <param name="newType">newType light Type that could be used</param>
	    public void setLightType(LightBeam.BeamType newType){
		    type = convertType(newType);
	    }
        /// <summary>
        /// Will Set the current light type to given lightType
	    /// </summary>
        /// <param name="newType">light Type that could be used</param>
	    public void setLightType(lightType newType){
		    type = newType;
	    }
	    /// <summary>
        /// Will Return the current column that the light is centered under
	    /// </summary>
        /// <returns>the current column that the light is centered under</returns>
	    public int getPosition(){
		    return currentPosition;
	    }
	    /// <summary>
	    /// Will Move the Camera to be centered under the the given column 
        /// provided it is greater than zero and less than the current MaxPosition
	    /// </summary>
        /// <param name="newPosition">the Desired position of the light</param>
        /// <returns>True if the position was successfully changed</returns>
	    public bool setPosition(int newPosition){
		    if(newPosition >= 0 && newPosition <= MaxPosition){
			    currentPosition = newPosition;
			    return true;
		    }
		    return false;
	    }
	    /// <summary>
        /// Will update the current Maxposition based on the given InteractableObject[][]
	    /// </summary>
        /// <param name="targetSet">the set of Enemies this Light will find targets in. Note if no ObjectGrid was set this set cannot target any enemies</param>
        /// <returns>the final value of MaxPosition</returns>
	    public int updateMaxPosition(InteractableObject[][] targetSet){
		    if(targetSet != null){
			    MaxPosition = 0;
			    for(int loopRows = 0; loopRows < targetSet.Length; loopRows++){
				    if(targetSet[loopRows] != null && targetSet[loopRows].Length - 1 > MaxPosition){
                        MaxPosition = targetSet[loopRows].Length - 1;
				    }
			    }
		    }
		    return MaxPosition;
	    }
	    /// <summary>
        ///  Will return the current Max position of the light
	    /// </summary>
        /// <returns>the current Max position of the light</returns>
	    public int getMaxPosition(){
		    return MaxPosition;
	    }
	    /// <summary>
        /// will find and return the Enemies that were affected by the light
	    /// </summary>
        /// <param name="targetSet">the set of Enemies this Light will find targets in. Note if no ObjectGrid was set this set cannot target any enemies</param>
        /// <returns>The array of InteractableObjects that are targeted by the light if this array is null then no enemies were effected</returns>
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
	    /// <summary>
        /// Will find and return row and Column positions of the affected by the light
	    /// </summary>
        /// <param name="targetSet">the set of Enemies this LightWill Shine Onto. Note if no ObjectGrid was set this set cannot target any enemies</param>
        /// <returns>The array is organized as a 2D array[Number of Enemies Found][2] where array[?][0] = row position and array[?][1] = Column Position</returns>
	    public int[,] getTargetedEnemyPositions(ObjectSet targetSet){
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
	    /// <summary>
        /// Will Find and return the column positions of the enemies affected by the light
	    /// </summary>
        /// <param name="targetSet">the set of Enemies this Light will find targets in. Note if no ObjectGrid was set this set cannot target any enemies </param>
	    /// <returns>the array is organized as a 1D array[Number of Enemies Found] where array[?] = the column where an enemy was found.
        ///  Note: there is one entry per enemy found. So duplicate column entries indicate more than one enemy is targeted in the column in question</returns>
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
	    /// <summary>
        /// Find and return the row and column positions of the enemies affected by the light
	    /// </summary>
        /// <param name="targetSet">the set of Enemies this Light will find targets in. Note if no IDArray was set this set cannot target any enemies</param>
        /// <returns>The array is organized as a 2D array[Number of Enemies Found][2] where array[?][0] = row position and array[?][1] = Column Position</returns>
	    public int[,] getTargetedEnemyPositions(PrimitiveSet targetSet){
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
	    /// <summary>
        /// Will Find and return the column positions of the enemies affected by the light
	    /// </summary>
        /// <param name="targetSet">the set of Enemies this Light will find targets in. Note if no IDArray was set this set cannot target any enemies </param>
	    /// <returns>the array is organized as a 1D array[Number of Enemies Found] where array[?] = the column where an enemy was found.
	    ///  Note: there is one entry per enemy found. So duplicate column entries indicate more than one enemy is targeted in the column in question</returns>
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
	    /// <summary>
        /// Will Turn on the visual of the light active
	    /// </summary>
	    public void activateLight(){
		    lightIsActive = true;
	    }
	    /// <summary>
	    /// Will Turn off the visual of the light active
	    /// </summary>
	    public void deactivateLight(){
		    lightIsActive = false;
	    }
	    /// <summary>
        /// Returns whether or not the light is currently active
	    /// </summary>
        /// <returns>whether or not the light is currently active</returns>
	    public bool isLightActive(){
		    return lightIsActive;
        }
	    /// <summary>
        /// Will convirt a given BeamType to the apropriate lightType 
	    /// </summary>
        /// <param name="convertTarget">The given BeamType</param>
        /// <returns>the converted lightType</returns>
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
	    /// <summary>
        /// Will convirt a given lightType to the apropriate BeamType 
	    /// </summary>
        /// <param name="convertTarget">The given lightType</param>
        /// <returns> the converted BeamType</returns>
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
	    /// <summary>
        /// Will Return the affected enemies in a new array
	    /// </summary>
        /// <param name="targetSet"> the the array of enemies that are targeted by the light the set that will be targted by the light</param>
        /// <param name="targetCollumn">the set that will be targted by the light</param>
	    /// <returns></returns>
	    private InteractableObject[] getTargetedEnemiesLASER(InteractableObject[][] targetSet, int targetCollumn){
		    InteractableObject[] retVal = null;
		    if(targetSet != null && targetCollumn >= 0){
                retVal = new InteractableObject[targetSet.Length];			
			    for(int loopRows = 0; loopRows < targetSet.Length; loopRows++){
				    if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].Length && targetSet[loopRows][targetCollumn] != null){
					    retVal[loopRows] = targetSet[loopRows][targetCollumn];
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        /// Will return the locations of the affected enemies in the
	    /// </summary>
        /// <param name="targetSet">the the array of enemies that are targeted by the light</param>
        /// <param name="targetCollumn">the set that will be targted by the light</param>
        /// <returns>the locations of the enemies in the targetSet</returns>
	    private int[,] getTargetedEnemyLocations2DLASER(InteractableObject[][] targetSet, int targetCollumn){
		    int[,] retVal = null;
		    if(targetSet != null && targetCollumn >= 0){
			    bool[] tempArray = new bool[targetSet.Length];
			    int found = 0;
			    //finding targets
			    for(int loopRows = 0; loopRows < targetSet.Length; loopRows++){
				    if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].Length && targetSet[loopRows][targetCollumn] != null){
					    tempArray[loopRows] = true;
					    found++;
				    }
				    else{
					    tempArray[loopRows] = false;
				    }
			    }
			    //formatting return array and returning it
			    if(found > 0){
				    retVal = new int[found,2];
				    int loopRows = 0;
				    int loopFound = 0;
				    while(loopRows < targetSet.Length && loopFound < found){
					    if(tempArray[loopRows]){
						    retVal[loopFound,0] = loopRows;
						    retVal[loopFound,1] = targetCollumn;
						    loopFound++;
					    }
					    loopRows++;
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        /// Will return the locations of the affected enemies in the
	    /// </summary>
        /// <param name="targetSet">the the array of enemies that are targeted by the light</param>
	    /// <param name="targetCollumn">Th collumn the of enemies to be targeted</param>
        /// <returns>the locations of the enemies in the targetSet</returns>
	    private int[] getTargetedEnemyLocations1DLASER(InteractableObject[][] targetSet, int targetCollumn){
		    int[] retVal = null;
		    if(targetSet != null && targetCollumn >= 0){
                bool[] tempArray = new bool[targetSet.Length];
			    int found = 0;
			    //finding targets
			    for(int loopRows = 0; loopRows < targetSet.Length; loopRows++){
				    if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].Length && targetSet[loopRows][targetCollumn] != null){
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
				    while(loopRows < targetSet.Length && loopFound < found){
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
	    /// <summary>
        /// Will return the locations of the affected enemies in the 
	    /// </summary>
        /// <param name="targetSet">the the array of enemies that are targeted by the light</param>
	    /// <param name="targetCollumn"></param>
        /// <returns>the locations of the enemies in the targetSet</returns>
	    private int[,] getTargetedEnemyLocations2DLASER(int[][] targetSet, int targetCollumn){
		    int[,] retVal = null;
		    if(targetSet != null && targetCollumn >= 0){
			    bool[] tempArray = new bool[targetSet.Length];
			    int found = 0;
			    //finding targets
			    for(int loopRows = 0; loopRows < targetSet.Length; loopRows++){
				    if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].Length && targetSet[loopRows][targetCollumn] > 0){
					    tempArray[loopRows] = true;
					    found++;
				    }
				    else{
					    tempArray[loopRows] = false;
				    }
			    }
			    //formatting return array and returning it
			    if(found > 0){
				    retVal = new int[found,2];
				    int loopRows = 0;
				    int loopFound = 0;
				    while(loopRows < targetSet.Length && loopFound < found){
					    if(tempArray[loopRows]){
						    retVal[loopFound,0] = loopRows;
						    retVal[loopFound,1] = targetCollumn;
						    loopFound++;
					    }
					    loopRows++;
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        ///  Will return the locations of the affected enemies in the 
	    /// </summary>
        /// <param name="targetSet">the the array of enemies that are targeted by the light</param>
	    /// <param name="targetCollumn"></param>
        /// <returns>the locations of the enemies in the targetSet</returns>
	    private int[] getTargetedEnemyLocations1DLASER(int[][] targetSet, int targetCollumn){
		    int[] retVal = null;
		    if(targetSet != null && targetCollumn >= 0){
                bool[] tempArray = new bool[targetSet.Length];
			    int found = 0;
			    //finding targets
			    for(int loopRows = 0; loopRows < targetSet.Length; loopRows++){
				    if(targetSet[loopRows] != null && targetCollumn < targetSet[loopRows].Length && targetSet[loopRows][targetCollumn] > 0){
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
				    while(loopRows < targetSet.Length && loopFound < found){
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
        /// <summary>
        /// Will Return the affected enemies in a new array
        /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
        /// <param name="targetCollumn"></param>
        /// <returns>the the array of enemies that are targeted by the light</returns>
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
	    /// <summary>
        ///  Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private int[,] getTargetedEnemyLocations2DWIDE(InteractableObject[][] targetSet, int targetCollumn){
		    int[,] retVal = null;
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
				    retVal = new int[found,2];
				    int loop = 0;
				    int loopFound = 0;
				    while(loop < 5 && loopFound < found){
					    if(tempArray[loop] >= 0){
						    retVal[loopFound,0] = tempArray[loop];
						    retVal[loopFound,1] = targetCollumn - 2 + loop;
						    loopFound++;
					    }
					    loop++;
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
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
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private int[,] getTargetedEnemyLocations2DWIDE(int[][] targetSet, int targetCollumn){
		    int[,] retVal = null;
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
				    retVal = new int[found,2];
				    int loop = 0;
				    int loopFound = 0;
				    while(loop < 5 && loopFound < found){
					    if(tempArray[loop] >= 0){
						    retVal[loopFound,0] = tempArray[loop];
						    retVal[loopFound,1] = targetCollumn - 2 + loop;
						    loopFound++;
					    }
					    loop++;
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
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
	    /// <summary>
        /// Will Return the affected enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targeted by the light</param>
	    /// <param name="targetCollumn"></param>
        /// <returns>the the array of enemies that are targeted by the light</returns>
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
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private int[,] getTargetedEnemyLocations2DMEDIUM(InteractableObject[][] targetSet, int targetCollumn){
		    int[,] retVal = null;
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
				    retVal = new int[found,2];
				    int loop = 0;
				    int loopFound = 0;
				    while(loop < 3 && loopFound < found){
					    if(tempArray[loop] >= 0){
						    retVal[loopFound,0] = tempArray[loop];
						    retVal[loopFound,1] = targetCollumn - 1 + loop;
						    loopFound++;
					    }
					    loop++;
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
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
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet"> the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private int[,] getTargetedEnemyLocations2DMEDIUM(int[][] targetSet, int targetCollumn){
		    int[,] retVal = null;
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
				    retVal = new int[found,2];
				    int loop = 0;
				    int loopFound = 0;
				    while(loop < 3 && loopFound < found){
					    if(tempArray[loop] >= 0){
						    retVal[loopFound,0] = tempArray[loop];
						    retVal[loopFound,1] = targetCollumn - 1 + loop;
						    loopFound++;
					    }
					    loop++;
				    }
			    }
		    }
		    return retVal;
	    }
	    /// <summary>
        /// Will return the locations of the targeted enemies in a new array
	    /// </summary>
        /// <param name="targetSet">the set that will be targted by the light</param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
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
	    /// <summary>
        /// Will Return the InteractableObject that has the greatest index in the target Collumn
        /// assumes targetSet exists != null
	    /// </summary>
	    /// <param name="targetSet"></param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private InteractableObject getClosest(InteractableObject[][] targetSet, int targetCollumn){
		    if(targetCollumn >= 0){
			    for(int loop = targetSet.Length -1; loop >= 0; loop--){
				    if( targetSet[loop] != null && targetCollumn < targetSet[loop].Length && targetSet[loop][targetCollumn] != null){
					    return targetSet[loop][targetCollumn];
				    }
			    }
		    }
		    return null;
	    }
	    /// <summary>
        /// Return the the row location of the enemy with the greatest row location in the targetCollumn
	    /// </summary>
	    /// <param name="targetSet"></param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private int getClosestIndex(InteractableObject[][] targetSet, int targetCollumn){
		    if(targetCollumn >= 0){
			    for(int loop = targetSet.Length -1; loop >= 0; loop--){
				    if( targetSet[loop] != null && targetCollumn < targetSet[loop].Length && targetSet[loop][targetCollumn] != null){
					    return loop;
				    }
			    }
		    }
		    return -1;
	    }
	    /// <summary>
        /// Return the the row location of the enemy with the greatest row location in the targetCollumnW
	    /// </summary>
	    /// <param name="targetSet"></param>
	    /// <param name="targetCollumn"></param>
	    /// <returns></returns>
	    private int getClosestIndex(int[][] targetSet, int targetCollumn){
		    if(targetCollumn >= 0){
			    for(int loop = targetSet.Length -1; loop >= 0; loop--){
				    if( targetSet[loop] != null && targetCollumn < targetSet[loop].Length && targetSet[loop][targetCollumn] > 0){
					    return loop;
				    }
			    }
		    }
		    return -1;
	    }
    }
}
