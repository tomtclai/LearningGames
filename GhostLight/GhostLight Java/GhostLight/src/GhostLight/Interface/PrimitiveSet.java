package GhostLight.Interface;

import GhostLight.Interface.InteractableObject.ObjectType;

/**
 * An alternative interface to ObjectSet into the Objects that appear on the screen.
 * @author Michael Letter
 */
public class PrimitiveSet {

	/** the Greatest ID in use if -1 no IDs are in use*/
	private int maxID = -1;
	/** Stores the next available unique not in use*/
	private int nextAvalableID = 0;
	/** stores which ID are available and which ID's are not,
	 * {Row, Column} {-1, -1}, Not Taken, {+?, +?} ID taken ID at {?, ?}, {-2, -2} ID use and position unknown 
	 *  index [?][2] is used to mark whether or not a InteractableObject is linked to an ID yet, a -1 indicates that no Interactalbe object has been linked, 1 indicates one has */
	private int[][] IDPositions = new int[50000][3];
	
	//For all -1 is used as a null character
	/** Maintains the ID's that mark where each individual object is*/
	private int[][] iDArray = null;
	/** the score of all the enemies at each position in the array */
	private int[][] scoreArray = null;
	/** the health of all the enemies at each position in the array */
	private float[][] healthArray = null;
	/** Stores the total number of health increments available to all the enemies at each position in the array */
	private int[][] healthIncrementArray = null;
	/** whether or not the enemies at each position in the array are revealed*/
	private boolean[][] revealedArray = null;
	/** the Type of each Object in the array */
	private int[][] typeArray = null;
	
	/**
	 * Initializes private data members
	 */
	public PrimitiveSet(){
		for(int loop = 0; loop < IDPositions.length; loop++){
			IDPositions[loop][0] = -1;
			IDPositions[loop][1] = -1;
			IDPositions[loop][2] = -1;
		}
	}
	//2D
	/**
	 * Returns the Data structure that handles the placement of each individual enemy
	 * each unique number greater than zero represents an enemy at that location. 
	 * A zero value means that there is no enemy at said position 
	 * @return a 2D array of unique integer ID's
	 */
	public int[][] getIDArray(){
		return iDArray;
	}
	/**
	 * Will set the curentID array to the given one.
	 * Each unique element greater than zero will be made an enemy at that location
	 * zero  duplicate values will be ignored
	 * @param newIDArray
	 */
	public void setIDArray(int[][] newIDArray){
		iDArray = newIDArray;
	}
	//1D
	/**
	 * Returns the Data structure that handles the placement of each individual enemys within a given Row
	 * each unique number greater then or equal to zero represents an enemy at that location. 
	 * A negative number means that there is no enemy at said position 
	 * @param row The Row of ID's in question 
	 * @return 1D array of unique integer ID's at the given Row. Note, if there is no row of IDs at the given row will return null
	 */
	public int[] getIDArray(int row){
		if(row >= 0 && iDArray != null && row < iDArray.length){
			return iDArray[row];
		}
		return null;
	}
	/**
	 * Will set the curentID row array to the given one. Note a row already existed at the given row location it will be replaced by the given row
	 * Each unique element greater then or equal to zero will be made an enemy at tht location
	 * negative values and duplicate values will be ignored.
	 * @param newIDArray
	 * @return if TRUE, the row was set successfully, if false no changes were made
	 */
	public boolean setIDArray(int[] newIDArray, int row){
		if(row >= 0){
			if(iDArray == null || iDArray.length <= row){
				setIDRowCount(row + 1);
			}
			iDArray[row] = newIDArray;
			return true;
		}
		return false;
	}
	/**
	 * Will move a target Row to a given Row position. If another Row already exists at that given position
	 * It will be moved into the row position originally inhabited by the target Row.
	 * @param targetRow the Row index of the Row to me moved
	 * @param targetPosition The Row index of the targetRow will be moved to
	 * @return If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.
	 */
	public boolean moveIDRow(int targetRow, int targetPosition){
		//is operation possible
		if(iDArray != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= iDArray.length || targetPosition >= iDArray.length)){
			//resizing if nessesary
			if(iDArray.length <= targetPosition){
				setIDRowCount(targetPosition + 1);
			}
			//swapping Rows
			if(iDArray.length > targetRow){
				int[] temp = iDArray[targetRow];
				iDArray[targetRow] = iDArray[targetPosition];
				iDArray[targetPosition] = temp;
			}
			//killing Row
			else{
				iDArray[targetPosition] = null;
			}
			return true;
		}
		return false;
	}
	/**
	 * Will set the number arrows available to place ID's. These Row can be accessed by there index with the first Row bing index zero 
	 * and the last row bing index (rowCount - 1). Note, if any rows existed in rows greater than (rowCount - 1) they will be lost
	 * @param rowCount the desired number arrows available to place ID's
	 * @return
	 */
	public boolean setIDRowCount(int rowCount){
		if(rowCount > 0){
			//first initialization
			if(iDArray == null){
				iDArray = new int[rowCount][];
				for(int loop = 0; loop < iDArray.length; loop++){
					iDArray[loop] = null;
				}
			}
			//resize
			else if(iDArray.length != rowCount){
				int[][] newIDArray = new int[rowCount][];
				int loop = 0; 
				while(loop < iDArray.length && loop < newIDArray.length){
					newIDArray[loop] = iDArray[loop];
					loop++;
				}
				while(loop < newIDArray.length){
					newIDArray[loop] = null;
					loop++;
				}
				iDArray = newIDArray;
			}
			return true;
		}
		//destroy all
		else if(rowCount == 0){
			iDArray = null;
			return true;
		}
		
		return false;
	}
	/**
	 * Will return the number of rows currently available to store ID's
	 * @return
	 */
	public int getIDRowCount(){
		if(iDArray != null){
			return iDArray.length;
		}
		return 0;
	}
	//2D
	/**
	 * Returns the Data structure that handles the score of all the enemies present
	 * each element represents the number of points the enemy at that location is worth. 
	 * If the element is negative then there is no information about score about the enemy at this location.
	 * In the event there is an Enemy here then default values will be used.
	 * @return 2D array of scores
	 */
	public int[][] getScoreArray(){
		return scoreArray;
	}
	/**
	 * Will set the score array to the given array. A 0 or greater element will 
	 * dictate the score of the enemy at that position if one exists there.
	 * @param newScoreArray the new ID array
	 */
	public void setScoreArray(int[][] newScoreArray){
		scoreArray = newScoreArray;
	}
	//1D
	/**
	 * Returns the Data structure that handles the score of all the enemies in a given row
	 * each element represents the number of points the enemy at that location is worth. 
	 * @param row The Row of ID's in question 
	 * @return 1D array of unique integer ID's at the given Row. Note, if there is no row of IDs at the given row will return null
	 */
	public int[] getScoreArray(int row){
		if(row >= 0 && scoreArray != null && row < scoreArray.length){
			return scoreArray[row];
		}
		return null;
	}
	/**
	 * Will set the score row array to the given array. A zero or greater element will 
	 * dictate the score of the enemy at that position if one exists there. If a negative value is placed in a position
	 * where an enemy exists. default values will be used
	 * @param newScoreArray
	 * @return if TRUE, the row was set successfully, if false no changes were made
	 */
	public boolean setScoreArray(int[] newScoreArray, int row){
		if(row >= 0){
			if(scoreArray == null || scoreArray.length <= row){
				setScoreRowCount(row + 1);
			}
			scoreArray[row] = newScoreArray;
			return true;
		}
		return false;
	}
	/**
	 * Will move a target Row to a given Row position. If another Row already exists at that given position
	 * It will be moved into the row position originally inhabited by the target Row.
	 * @param targetRow the Row index of the Row to me moved
	 * @param targetPosition The Row index of the targetRow will be moved to
	 * @return If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.
	 */
	public boolean moveScoreRow(int targetRow, int targetPosition){
		//is operation possible
		if(scoreArray != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= scoreArray.length || targetPosition >= scoreArray.length)){
			//resizing if nessesary
			if(scoreArray.length <= targetPosition){
				setScoreRowCount(targetPosition + 1);
			}
			//swapping Rows
			if(scoreArray.length > targetRow){
				int[] temp = iDArray[targetRow];
				scoreArray[targetRow] = scoreArray[targetPosition];
				scoreArray[targetPosition] = temp;
			}
			//killing Row
			else{
				scoreArray[targetPosition] = null;
			}
			return true;
		}
		return false;
	}
	/**
	 * Will set the number arrows available to place Enemy Scores. These Row can be accessed by there index with the first Row bing index zero 
	 * and the last row bing index (rowCount - 1). Note, if any rows existed in rows greater than (rowCount - 1) they will be lost
	 * @param rowCount the desired number arrows available to place Enemy Scores
	 * @return
	 */
	public boolean setScoreRowCount(int rowCount){
		if(rowCount > 0){
			//first initialization
			if(scoreArray == null){
				scoreArray = new int[rowCount][];
				for(int loop = 0; loop < scoreArray.length; loop++){
					scoreArray[loop] = null;
				}
			}
			//resize
			else if(scoreArray.length != rowCount){
				int[][] newScoreArray = new int[rowCount][];
				int loop = 0; 
				while(loop < scoreArray.length && loop < newScoreArray.length){
					newScoreArray[loop] = scoreArray[loop];
					loop++;
				}
				while(loop < newScoreArray.length){
					newScoreArray[loop] = null;
					loop++;
				}
				scoreArray = newScoreArray;
			}
			return true;
		}
		//destroy all
		else if(rowCount == 0){
			scoreArray = null;
			return true;
		}
		
		return false;
	}
	/**
	 * Will return the number of rows currently available to store Enemy Scores
	 * @return
	 */
	public int getScoreRowCount(){
		if(scoreArray != null){
			return scoreArray.length;
		}
		return 0;
	}
	//2D
	/**
	 * Returns the Data structure that handles the health of all the enemies present
	 * each element represents the number of health the enemy at that location
	 * If the element is negative then there is no information about health about the enemy at this location
	 * In the event there is an Enemy here then default values will be used
	 * @return 2D array of health ratios. 0 means the enemy is at 0% health. 1 means the enemy is at 1% health
	 */
	public float[][] getHealthArray(){
		return healthArray;
	}
	/**
	 * Will set the current Health array to the given one
	 * Each element greater than or equal to zero will dictate the percent of health the enemy at that location has
	 * if the element is negative or greater than one then default values will be used
	 * @param newHealthArray
	 */
	public void setHealthArray(float[][] newHealthArray){
		healthArray = newHealthArray;
	}
	//1D
	/**
	 * Returns the Data structure that handles the health of all the enemies present
	 * each element represents the number of health the enemy at that location
	 * If the element is negative then there is no information about health about the enemy at this location
	 * In the event there is an Enemy here then default values will be used
	 * @return 1D flort array of the ratio of health each enemy has p at the given Row. Note, if there is no row at the given row will return null
	 */
	public float[] getHealthArray(int row){
		if(row >= 0 && healthArray != null && row < healthArray.length){
			return healthArray[row];
		}
		return null;
	}
	/**
	 * Will set the current Health array to the given one
	 * Each element greater than or equal to zero will dictate the percent of health the enemy at that location has
	 * if the element is negative or greater than one then default values will be used
	 * @param newHealthArray
	 * @return if TRUE, the row was set successfully, if false no changes were made
	 */
	public boolean setHealthArray(float[] newHealthArray, int row){
		if(row >= 0){
			if(healthArray == null || healthArray.length <= row){
				setHealthRowCount(row + 1);
			}
			healthArray[row] = newHealthArray;
			return true;
		}
		return false;
	}
	/**
	 * Will move a target Row to a given Row position. If another Row already exists at that given position
	 * It will be moved into the row position originally inhabited by the target Row.
	 * @param targetRow the Row index of the Row to me moved
	 * @param targetPosition The Row index of the targetRow will be moved to
	 * @return If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.
	 */
	public boolean moveHealthRow(int targetRow, int targetPosition){
		//is operation possible
		if(healthArray != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= healthArray.length || targetPosition >= healthArray.length)){
			//resizing if nessesary
			if(healthArray.length <= targetPosition){
				setHealthRowCount(targetPosition + 1);
			}
			//swapping Rows
			if(healthArray.length > targetRow){
				float[] temp = healthArray[targetRow];
				healthArray[targetRow] = healthArray[targetPosition];
				healthArray[targetPosition] = temp;
			}
			//killing Row
			else{
				healthArray[targetPosition] = null;
			}
			return true;
		}
		return false;
	}
	/**
	 * Will set the number of rows available to store enemy Health. These Row can be accessed by there index with the first Row bing index zero 
	 * and the last row bing index (rowCount - 1). Note, if any rows existed in rows greater than (rowCount - 1) they will be lost
	 * @param rowCount the desired number arrows available to store enemy Health
	 * @return
	 */
	public boolean setHealthRowCount(int rowCount){
		if(rowCount > 0){
			//first initialization
			if(healthArray == null){
				healthArray = new float[rowCount][];
				for(int loop = 0; loop < healthArray.length; loop++){
					healthArray[loop] = null;
				}
			}
			//resize
			else if(healthArray.length != rowCount){
				float[][] newHealthArray = new float[rowCount][];
				int loop = 0; 
				while(loop < healthArray.length && loop < newHealthArray.length){
					newHealthArray[loop] = healthArray[loop];
					loop++;
				}
				while(loop < newHealthArray.length){
					newHealthArray[loop] = null;
					loop++;
				}
				healthArray = newHealthArray;
			}
			return true;
		}
		//destroy all
		else if(rowCount == 0){
			healthArray = null;
			return true;
		}
		
		return false;
	}
	/**
	 * Will return the number of rows currently available to store Enemy Health
	 * @return
	 */
	public int getHealthRowCount(){
		if(healthArray != null){
			return healthArray.length;
		}
		return 0;
	}
	//2D
	/**
	 * Returns the Data structure that handles that dictates the number of health increments available to that enemy.
	 * So if an enemy has 3 health increments it can have either 0%, 33,33...%, 66,66...% or 100% health. In the event this is not the case the enemies health will be rounded down
	 * If the element is negative then there is no information about score about the enemy at this location.
	 * In the event there is an Enemy here then default values will be used.
	 * @return 2D array of scores
	 */
	public int[][] getHealthIncrementArray(){
		return healthIncrementArray;
	}
	/**
	 * Will set the HealthIncrementArray to the given array. HealthIncrementArray dictates the number of health increments available to that enemy.
	 * So if an enemy has 3 health increments it can have either 0%, 33,33...%, 66,66...% or 100% health. In the event this is not the case the enemies health will be rounded down
	 * If the element is negative then there is no information about score about the enemy at this location.
	 * In the event there is an Enemy here then default values will be used.
	 * @param newScoreArray the new ID array
	 */
	public void setHealthIncrementArray(int[][] newIncrementArray){
		newIncrementArray = healthIncrementArray;
	}
	//1D
	/**
	 * returns a row of Health Increments, These dictate the number of health increments available to that enemy.
	 * So if an enemy has 3 health increments it can have either 0%, 33,33...%, 66,66...% or 100% health. In the event this is not the case the enemies health will be rounded down
	 * If the element is negative then there is no information about score about the enemy at this location.
	 * @param row The Row of ID's in question 
	 * @return 1D array of unique integer ID's at the given Row. Note, if there is no row of IDs at the given row will return null
	 */
	public int[] getHealthIncrementArray(int row){
		if(row >= 0 && healthIncrementArray != null && row < healthIncrementArray.length){
			return healthIncrementArray[row];
		}
		return null;
	}
	/**
	 * Will set the HealthIncrementArray row to the given array. The HealthIncrementArray dictates the number of health increments available to that enemy.
	 * So if an enemy has 3 health increments it can have either 0%, 33,33...%, 66,66...% or 100% health. In the event this is not the case the enemies health will be rounded down
	 * If the element is negative then there is no information about score about the enemy at this location.
	 * In the event there is an Enemy here then default values will be used.
	 * @param newScoreArray
	 * @return if TRUE, the row was set successfully, if false no changes were made
	 */
	public boolean setHealthIncrementArray(int[] newIncrementArray, int row){
		if(row >= 0){
			if(healthIncrementArray == null || healthIncrementArray.length <= row){
				setScoreRowCount(row + 1);
			}
			healthIncrementArray[row] = newIncrementArray;
			return true;
		}
		return false;
	}
	/**
	 * Will move a target Row to a given Row position. If another Row already exists at that given position
	 * It will be moved into the row position originally inhabited by the target Row.
	 * @param targetRow the Row index of the Row to me moved
	 * @param targetPosition The Row index of the targetRow will be moved to
	 * @return If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.
	 */
	public boolean moveHealthIncrementArray(int targetRow, int targetPosition){
		//is operation possible
		if(healthIncrementArray != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= healthIncrementArray.length || targetPosition >= healthIncrementArray.length)){
			//resizing if nessesary
			if(healthIncrementArray.length <= targetPosition){
				setScoreRowCount(targetPosition + 1);
			}
			//swapping Rows
			if(healthIncrementArray.length > targetRow){
				int[] temp = iDArray[targetRow];
				healthIncrementArray[targetRow] = healthIncrementArray[targetPosition];
				healthIncrementArray[targetPosition] = temp;
			}
			//killing Row
			else{
				healthIncrementArray[targetPosition] = null;
			}
			return true;
		}
		return false;
	}
	/**
	 * Will set the number of rows available to define. These Row can be accessed by there index with the first Row bing index zero 
	 * and the last row bing index (rowCount - 1). Note, if any rows existed in rows greater than (rowCount - 1) they will be lost
	 * @param rowCount the desired number arrows available to place Enemy Scores
	 * @return
	 */
	public boolean setHealthIncrementArray(int rowCount){
		if(rowCount > 0){
			//first initialization
			if(healthIncrementArray == null){
				healthIncrementArray = new int[rowCount][];
				for(int loop = 0; loop < healthIncrementArray.length; loop++){
					healthIncrementArray[loop] = null;
				}
			}
			//resize
			else if(healthIncrementArray.length != rowCount){
				int[][] newIncrementArray = new int[rowCount][];
				int loop = 0; 
				while(loop < healthIncrementArray.length && loop < newIncrementArray.length){
					newIncrementArray[loop] = healthIncrementArray[loop];
					loop++;
				}
				while(loop < newIncrementArray.length){
					newIncrementArray[loop] = null;
					loop++;
				}
				healthIncrementArray = newIncrementArray;
			}
			return true;
		}
		//destroy all
		else if(rowCount == 0){
			healthIncrementArray = null;
			return true;
		}
		
		return false;
	}
	/**
	 * Will return the number of rows currently available to define Health Increments
	 * If a 0 is returned the no ros currently exist
	 * @return
	 */
	public int getHealthIncrementRowCount(){
		if(healthIncrementArray != null){
			return healthIncrementArray.length;
		}
		return 0;
	}
	//2D
	/**
	 * Returns the Data structure that handles whether or not the enemy at this location is revealed.
	 * @return if an element is True the Enemy is revealed if False the Enemy is not revealed
	 */
	public boolean[][] getRevealedArray(){
		return revealedArray;
	}
	/**
	 * Will set the current RevealedArray to the given one.
	 * If an Element is true then the Enemy at that location is revealed. 
	 * If an element is False, then the enemy at that location is not revealed
	 * @param newRevealedArray
	 */
	public void setRevealedArray(boolean[][] newRevealedArray){
		revealedArray = newRevealedArray;
	}
	//1D
	/**
	 * Returns the Data structure that handles whether or not the enemy at a location within the given row is revealed.
	 * @param row The Row of Enemy Revealed statuses in question 
	 * @return 1D array of booleans at the given Row. Note, if there is no row of IDs at the given row will return null
	 */
	public boolean[] getRevealedArray(int row){
		if(row >= 0 && revealedArray != null && row < revealedArray.length){
			return revealedArray[row];
		}
		return null;
	}
	/**
	 * Will set the selected row of Enemy Revealed statuses to the given one.
	 * If an Element is true then the Enemy at that location is revealed. 
	 * If an element is False, then the enemy at that location is not revealed
	 * @param newRevealedArray
	 * @return if TRUE, the row was set successfully, if false no changes were made
	 */
	public boolean setRevealedArray(boolean[] newRevealedArray, int row){
		if(row >= 0){
			if(revealedArray == null || revealedArray.length <= row){
				setRevealedRowCount(row + 1);
			}
			revealedArray[row] = newRevealedArray;
			return true;
		}
		return false;
	}
	/**
	 * Will move a target Row to a given Row position. If another Row already exists at that given position
	 * It will be moved into the row position originally inhabited by the target Row.
	 * @param targetRow the Row index of the Row to me moved
	 * @param targetPosition The Row index of the targetRow will be moved to
	 * @return If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.
	 */
	public boolean moveRevealedhRow(int targetRow, int targetPosition){
		//is operation possible
		if(revealedArray != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= revealedArray.length || targetPosition >= revealedArray.length)){
			//resizing if nessesary
			if(revealedArray.length <= targetPosition){
				setRevealedRowCount(targetPosition + 1);
			}
			//swapping Rows
			if(revealedArray.length > targetRow){
				boolean[] temp = revealedArray[targetRow];
				revealedArray[targetRow] = revealedArray[targetPosition];
				revealedArray[targetPosition] = temp;
			}
			//killing Row
			else{
				revealedArray[targetPosition] = null;
			}
			return true;
		}
		return false;
	}
	/**
	 * Will set the number arrows available to store Enemy Reveal statuses. These Row can be accessed by there index with the first Row bing index zero 
	 * and the last row bing index (rowCount - 1). Note, if any rows existed in rows greater than (rowCount - 1) they will be lost
	 * @param rowCount the desired number arrows available to place ID's
	 * @return
	 */
	public boolean setRevealedRowCount(int rowCount){
		if(rowCount > 0){
			//first initialization
			if(revealedArray == null){
				revealedArray = new boolean[rowCount][];
				for(int loop = 0; loop < revealedArray.length; loop++){
					revealedArray[loop] = null;
				}
			}
			//resize
			else if(revealedArray.length != rowCount){
				boolean[][] newRevealedArray = new boolean[rowCount][];
				int loop = 0; 
				while(loop < revealedArray.length && loop < newRevealedArray.length){
					newRevealedArray[loop] = revealedArray[loop];
					loop++;
				}
				while(loop < newRevealedArray.length){
					newRevealedArray[loop] = null;
					loop++;
				}
				revealedArray = newRevealedArray;
			}
			return true;
		}
		//destroy all
		else if(rowCount == 0){
			revealedArray = null;
			return true;
		}
		
		return false;
	}
	/**
	 * Will return the number of rows currently available to store Enemy Reveal statuses
	 * @return
	 */
	public int getRevealedRowCount(){
		if(revealedArray != null){
			return revealedArray.length;
		}
		return 0;
	}
	//2D
	/**
	 * Returns the Data structure that handles what type enemies are.
	 * This is structured as an array of the ordinal values of corresponding ObjectType. dictating the Object type of enemies at those position
	 * Any element that is not an ordinal value of an ObjectType is either ignored or replaced
	 * by the ordinal value of ObjectType.PUMPKIN depending on if an enemy exists at that location as defined by the ID array
	 * @return a 2D array of integers
	 */
	public int[][] getTypeArray(){
		return typeArray;
	}
	/**
	 * 
	 * This will set the currentType array
	 * This is structured as an array of the ordinal values of corresponding ObjectType. dictating the Object type of enemies at those position
	 * Any element that is not an ordinal value of an ObjectType is either ignored or replaced
	 * by the ordinal value of ObjectType.PUMPKIN depending on if an enemy exists at that location as defined by the ID array
	 * @param newTypeArray
	 */
	public void setTypeArray(int[][] newTypeArray){
		typeArray = newTypeArray;
	}
	//1D
	/**
	 * Returns the Data structure that handles what type enemies are within a given row.
	 * This is structured as an array of the ordinal values of corresponding ObjectType. dictating the Object type of enemies at those position
	 * Any element that is not an ordinal value of an ObjectType is either ignored or replaced
	 * by the ordinal value of ObjectType.PUMPKIN depending on if an enemy exists at that location as defined by the ID array
	 */
	public int[] getTypeArray(int row){
		if(row >= 0 && typeArray != null && row < typeArray.length){
			return typeArray[row];
		}
		return null;
	}
	/**
	 * Will set the selected row of ObjectTypes ordinal to the given one
	 * This is structured as an array of the ordinal values of corresponding ObjectType within a given row. dictating the Object type of enemies at those position
	 * Any element that is not an ordinal value of an ObjectType is either ignored or replaced
	 * by the ordinal value of ObjectType.PUMPKIN depending on if an enemy exists at that location as defined by the ID array
	 * @param newTypeArray
	 * @return if TRUE, the row was set successfully, if false no changes were made
	 */
	public boolean setTypeArray(int[] newTypeArray, int row){
		if(row >= 0){
			if(typeArray == null || typeArray.length <= row){
				setRevealedRowCount(row + 1);
			}
			typeArray[row] = newTypeArray;
			return true;
		}
		return false;
	}
	/**
	 * Will move a target Row to a given Row position. If another Row already exists at that given position
	 * It will be moved into the row position originally inhabited by the target Row.
	 * @param targetRow the Row index of the Row to me moved
	 * @param targetPosition The Row index of the targetRow will be moved to
	 * @return If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.
	 */
	public boolean moveTypeRow(int targetRow, int targetPosition){
		//is operation possible
		if(typeArray != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= typeArray.length || targetPosition >= typeArray.length)){
			//resizing if nessesary
			if(typeArray.length <= targetPosition){
				setTypeRowCount(targetPosition + 1);
			}
			//swapping Rows
			if(revealedArray.length > targetRow){
				int[] temp = typeArray[targetRow];
				typeArray[targetRow] = typeArray[targetPosition];
				typeArray[targetPosition] = temp;
			}
			//killing Row
			else{
				typeArray[targetPosition] = null;
			}
			return true;
		}
		return false;
	}
	/**
	 * Will set the number arrows available to place Enemy Types. These Row can be accessed by there index with the first Row bing index zero 
	 * and the last row bing index (rowCount - 1). Note, if any rows existed in rows greater than (rowCount - 1) they will be lost
	 * @param rowCount the desired number arrows available to place ID's
	 * @return
	 */
	public boolean setTypeRowCount(int rowCount){
		if(rowCount > 0){
			//first initialization
			if(typeArray == null){
				typeArray = new int[rowCount][];
				for(int loop = 0; loop < typeArray.length; loop++){
					typeArray[loop] = null;
				}
			}
			//resize
			else if(typeArray.length != rowCount){
				int[][] newTypeArray = new int[rowCount][];
				int loop = 0; 
				while(loop < typeArray.length && loop < newTypeArray.length){
					newTypeArray[loop] = typeArray[loop];
					loop++;
				}
				while(loop < typeArray.length){
					typeArray[loop] = null;
					loop++;
				}
				typeArray = newTypeArray;
			}
			return true;
		}
		//destroy all
		else if(rowCount == 0){
			typeArray = null;
			return true;
		}
		
		return false;
	}
	/**
	 * Will return the number of rows currently available to store enemy Types
	 * @return
	 */
	public int getTypeRowCount(){
		if(typeArray != null){
			return typeArray.length;
		}
		return 0;
	}
	/**
	 * Will set the currentState of the the given ObjectSet to match the state of this PrimitiveSet
	 * @param ObjectGrid
	 */
	public void synkObjectSet(ObjectSet ObjectGrid){
		if(ObjectGrid != null){
			//updating sizes of primitive arrays
			updateIDPositions(ObjectGrid);
			resizeObjectSetArray(ObjectGrid);
			moveAndRemoveInteractableObjects(ObjectGrid);
			resizeArrays();
			//updating Object Health type, etc
			if(ObjectGrid.getObjectGrid() != null){
				for(int loopRow = 0; loopRow < iDArray.length; loopRow++){
					if(ObjectGrid.getObjectGrid()[loopRow] != null){
						for(int loopCollumn = 0; loopCollumn < iDArray[loopRow].length; loopCollumn++){
							if(ObjectGrid.getObjectGrid()[loopRow][loopCollumn] != null){
								if(typeArray[loopRow][loopCollumn] >= 0 && typeArray[loopRow][loopCollumn] < ObjectType.values().length){
									ObjectGrid.getObjectGrid()[loopRow][loopCollumn].setObjectType(ObjectType.values()[typeArray[loopRow][loopCollumn]], true);
								}
								ObjectGrid.getObjectGrid()[loopRow][loopCollumn].setRevealStatus(revealedArray[loopRow][loopCollumn]);
								ObjectGrid.getObjectGrid()[loopRow][loopCollumn].setScore(scoreArray[loopRow][loopCollumn]);
								ObjectGrid.getObjectGrid()[loopRow][loopCollumn].setMaxHealth(healthIncrementArray[loopRow][loopCollumn]);
								ObjectGrid.getObjectGrid()[loopRow][loopCollumn].setCurrentHealth((int)(healthArray[loopRow][loopCollumn]*(float)ObjectGrid.getObjectGrid()[loopRow][loopCollumn].getMaximumHealth()));
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Will Go through the given ObjectGrid
	 * Note, Assumes the ObjectGrid array is the same Dimensions or bigger then the current iDArray
	 * and that all taken ID positions are up to date
	 * @param ObjectGrid
	 */
	private void moveAndRemoveInteractableObjects(ObjectSet ObjectGrid){
		if(ObjectGrid != null){
			//No Enemies
			if(iDArray == null){
				//Clearing Object Grid
				if(ObjectGrid.getObjectGrid() != null){
					for(int loopRow = 0; loopRow < ObjectGrid.getObjectGrid().length; loopRow++){
						if(ObjectGrid.getObjectGrid() != null){
							for(int loopCollumn = 0; loopCollumn < ObjectGrid.getObjectGrid()[loopRow].length; loopCollumn++){
								if(ObjectGrid.getObjectGrid()[loopRow][loopCollumn] != null){
									ObjectGrid.getObjectGrid()[loopRow][loopCollumn].destroy();
									ObjectGrid.getObjectGrid()[loopRow][loopCollumn] = null;
								}
							}
						}
					}
				}
			}
			//Possibly Some Enemies
			else{
				if(ObjectGrid.getObjectGrid() != null){
					for(int loopRow = 0; loopRow < iDArray.length; loopRow++){
						if(ObjectGrid.getObjectGrid()[loopRow] != null){
							for(int loopCollumn = 0; loopCollumn < iDArray[loopRow].length; loopCollumn++){
								analizeEnemyAt(ObjectGrid, loopRow, loopCollumn);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * examin the InteractableObject at the given position and the ID position
	 * Given that information it will either, remove an enemy, add an enemy, or swap that enemy with the enemy at the position the enemy should be at
	 * If a swap hapens the enemy moved to the current position will be examined as well, and so on
	 * @param row
	 * @param collumn
	 */
	private void analizeEnemyAt(ObjectSet ObjectGrid, int row, int collumn){
		//add Eenemy
		if(ObjectGrid.getObjectGrid()[row][collumn] == null && iDArray[row][collumn] > 0 && IDPositions[iDArray[row][collumn] - 1][2] == -1){
			ObjectGrid.getObjectGrid()[row][collumn] = new InteractableObject();
			ObjectGrid.getObjectGrid()[row][collumn].ID = iDArray[row][collumn];
			IDPositions[iDArray[row][collumn] - 1][2] = 1;
		}
		else if(ObjectGrid.getObjectGrid()[row][collumn] != null && ObjectGrid.getObjectGrid()[row][collumn].ID != iDArray[row][collumn]){
			//Removing Enemy 
			if(ObjectGrid.getObjectGrid()[row][collumn].ID <= 0 || IDPositions[ObjectGrid.getObjectGrid()[row][collumn].ID - 1][0] < 0){
				IDPositions[ObjectGrid.getObjectGrid()[row][collumn].ID - 1][2] = -1;
				ObjectGrid.getObjectGrid()[row][collumn].destroy();
				ObjectGrid.getObjectGrid()[row][collumn] = null;
				//checking if ID at position is new if so adding new enemy
				if(iDArray[row][collumn] > 0 && IDPositions[iDArray[row][collumn] - 1][2] == -1){
					ObjectGrid.getObjectGrid()[row][collumn] = new InteractableObject();
					ObjectGrid.getObjectGrid()[row][collumn].ID = iDArray[row][collumn];
					IDPositions[iDArray[row][collumn] - 1][2] = 1;
				}
			}
			//Swapping current enemy with enemy at the stored position of the ID in the ID set
			else{
				//System.out.print("A" + ObjectGrid.getObjectGrid()[row][collumn].ID + "," + iDArray[row][collumn] + "\n");
				InteractableObject temp = ObjectGrid.getObjectGrid()[row][collumn];
				ObjectGrid.getObjectGrid()[row][collumn] = ObjectGrid.getObjectGrid()[IDPositions[temp.ID - 1][0]][IDPositions[temp.ID - 1][1]];
				ObjectGrid.getObjectGrid()[IDPositions[temp.ID - 1][0]][IDPositions[temp.ID - 1][1]] = temp;
				//Analyzing swapped enemy
				analizeEnemyAt(ObjectGrid, row, collumn);
			}
		}
	}
	/**
	 * Will resize the current ObjectSet set to be at least the size of the 
	 * @param ObjectGrid
	 */
	private void resizeObjectSetArray(ObjectSet ObjectGrid){
		if(ObjectGrid != null && iDArray != null){
			//Rows
			//Create
			if(ObjectGrid.getObjectGrid() == null){
				ObjectGrid.setObjectGrid(new InteractableObject[iDArray.length][]);
				for(int loop = 0; loop < ObjectGrid.getObjectGrid().length; loop++){
					ObjectGrid.getObjectGrid()[loop] = null;
				}
			}
			//Resize
			else if(ObjectGrid.getObjectGrid().length < iDArray.length){
				InteractableObject[][] newSet = new InteractableObject[iDArray.length][];
				int loop = 0;
				while(loop < ObjectGrid.getObjectGrid().length){
					newSet[loop] = ObjectGrid.getObjectGrid()[loop];
					loop++;
				}
				while(loop < newSet.length){
					newSet[loop] = null;
					loop++;
				}
				ObjectGrid.setObjectGrid(newSet);
			}
			//Collumns
			for(int loop = 0; loop < iDArray.length; loop++){
				if(iDArray[loop] != null){
					//Create
					if(ObjectGrid.getObjectGrid()[loop] == null){
						ObjectGrid.getObjectGrid()[loop] = new InteractableObject[iDArray[loop].length];
						for(int loopCollumn = 0; loopCollumn < ObjectGrid.getObjectGrid()[loop].length; loopCollumn++){
							ObjectGrid.getObjectGrid()[loop][loopCollumn] = null;
						}
					}
					//Resize
					else if(ObjectGrid.getObjectGrid()[loop].length < iDArray[loop].length){
						InteractableObject[] newSet = new InteractableObject[iDArray[loop].length];
						int loopCollumn = 0;
						while(loopCollumn < ObjectGrid.getObjectGrid()[loop].length){
							newSet[loopCollumn] = ObjectGrid.getObjectGrid()[loop][loopCollumn];
							loopCollumn++;
						}
						while(loopCollumn < newSet.length){
							newSet[loopCollumn] = null;
							loopCollumn++;
						}
						ObjectGrid.getObjectGrid()[loop] = newSet;
					}
				}
			}
		}
	}
	/**
	 * Will return a unique ID based on the IDs currently in use
	 * @return If greater than or equal to zero, the ID is unique and not in used based on the array iDArray from the last Update
	 * if 
	 */
	public int getUniqueID(){
		int retVal = nextAvalableID + 1;
		//Finding next Available ID
		for(int loop = retVal; loop < maxID; loop++){
			if(IDPositions[loop][0] == -1){
				nextAvalableID = loop;
			}
		}
		//All IDs taken
		if(retVal == nextAvalableID + 1){
			if(maxID == IDPositions.length){
				nextAvalableID = -1;
			}
			else{
				maxID++;
				nextAvalableID = maxID + 1;
			}
		}
		//System.out.print(retVal + " ");
		return retVal;
	}
	/**
	 * Will Reset this object to defualt state and initialize all arrays to null
	 */
	public void reset(){
		iDArray = null;
		scoreArray = null;
		healthArray = null;
		revealedArray = null;
		typeArray = null;
		
		for(int loop = 0; loop <= maxID; loop++){
			IDPositions[loop][0] = -1;
			IDPositions[loop][1] = -1;
			IDPositions[loop][2] = -1;
		}
	}
	/**
	 * Will search through the ID array and will resize the health, score, type, and revealed arrays to match the size of the
	 * ID array if there exists an ID in the ID array whose position is not represented in the previous arrays
	 * Note, no arrays are ever removed or shrunk by this function and Duplicate IDs are treated as active IDs by this function
	 */
	public void resizeArrays(){
		if(iDArray != null){
			//loopRows
			for(int loopRow = iDArray.length - 1; loopRow >= 0; loopRow--){
				if(iDArray[loopRow] != null){
					//loopCollumn
					for(int loopCollumn = iDArray[loopRow].length - 1; loopCollumn >= 0; loopCollumn--){
						if(iDArray[loopRow][loopCollumn] > 0){
							//Score
							
							//Updating RowSize
							if(scoreArray == null){
								scoreArray = new int[iDArray.length][];
								for(int loop = 0; loop < scoreArray.length; loop++){
									scoreArray[loopRow] = null;
								}
							}
							else if(scoreArray.length <= loopRow){
								int[][] newScoreArray = new int[iDArray.length][];
								int loop = 0;
								while(loop < scoreArray.length){
									newScoreArray[loop] = scoreArray[loop];
									loop++;
								}
								while(loop < newScoreArray.length){
									newScoreArray[loop] = null;
									loop++;
								}
								scoreArray = newScoreArray;
							}
							//Updating CollumnSize
							if(scoreArray[loopRow] == null){
								scoreArray[loopRow] = new int[iDArray[loopRow].length];
								for(int loop = 0; loop < scoreArray[loopRow].length; loop++){
									scoreArray[loopRow][loop] = -1;
								}
							}
							else if(scoreArray[loopRow].length <= loopCollumn){
								int[] newScoreArray = new int[iDArray[loopRow].length];
								int loop = 0;
								while(loop < scoreArray[loopRow].length){
									newScoreArray[loop] = scoreArray[loopRow][loop];
									loop++;
								}
								while(loop < newScoreArray.length){
									newScoreArray[loop] = -1;
									loop++;
								}
								scoreArray[loopRow] = newScoreArray;
							}
							
							//Health
							
							//Updating RowSize
							if(healthArray == null){
								healthArray = new float[iDArray.length][];
								for(int loop = 0; loop < healthArray.length; loop++){
									healthArray[loopRow] = null;
								}
							}
							else if(healthArray.length <= loopRow){
								float[][] newHealthArray = new float[iDArray.length][];
								int loop = 0;
								while(loop < healthArray.length){
									newHealthArray[loop] = healthArray[loop];
									loop++;
								}
								while(loop < newHealthArray.length){
									newHealthArray[loop] = null;
									loop++;
								}
								healthArray = newHealthArray;
							}
							//Updating CollumnSize
							if(healthArray[loopRow] == null){
								healthArray[loopRow] = new float[iDArray[loopRow].length];
								for(int loop = 0; loop < healthArray[loopRow].length; loop++){
									healthArray[loopRow][loop] = -1;
								}
							}
							else if(healthArray[loopRow].length <= loopCollumn){
								float[] newHealthArray = new float[iDArray[loopRow].length];
								int loop = 0;
								while(loop < healthArray[loopRow].length){
									newHealthArray[loop] = healthArray[loopRow][loop];
									loop++;
								}
								while(loop < newHealthArray.length){
									newHealthArray[loop] = -1;
									loop++;
								}
								healthArray[loopRow] = newHealthArray;
							}
							
							//health Increment Array
							
							//Updating RowSize
							if(healthIncrementArray == null){
								healthIncrementArray = new int[iDArray.length][];
								for(int loop = 0; loop < healthIncrementArray.length; loop++){
									healthIncrementArray[loopRow] = null;
								}
							}
							else if(healthIncrementArray.length <= loopRow){
								int[][] newIncrementArray = new int[iDArray.length][];
								int loop = 0;
								while(loop < healthIncrementArray.length){
									newIncrementArray[loop] = healthIncrementArray[loop];
									loop++;
								}
								while(loop < newIncrementArray.length){
									newIncrementArray[loop] = null;
									loop++;
								}
								healthIncrementArray = newIncrementArray;
							}
							//Updating CollumnSize
							if(healthIncrementArray[loopRow] == null){
								healthIncrementArray[loopRow] = new int[iDArray[loopRow].length];
								for(int loop = 0; loop < healthIncrementArray[loopRow].length; loop++){
									healthIncrementArray[loopRow][loop] = -1;
								}
							}
							else if(healthIncrementArray[loopRow].length <= loopCollumn){
								int[] newIncrementArray = new int[iDArray[loopRow].length];
								int loop = 0;
								while(loop < healthIncrementArray[loopRow].length){
									newIncrementArray[loop] = healthIncrementArray[loopRow][loop];
									loop++;
								}
								while(loop < newIncrementArray.length){
									newIncrementArray[loop] = -1;
									loop++;
								}
								healthIncrementArray[loopRow] = newIncrementArray;
							}
							
							//Revealed
							
							//Updating RowSize
							if(revealedArray == null){
								revealedArray = new boolean[iDArray.length][];
								for(int loop = 0; loop < revealedArray.length; loop++){
									revealedArray[loopRow] = null;
								}
							}
							else if(revealedArray.length <= loopRow){
								boolean[][] newRevealedArray = new boolean[iDArray.length][];
								int loop = 0;
								while(loop < revealedArray.length){
									newRevealedArray[loop] = revealedArray[loop];
									loop++;
								}
								while(loop < newRevealedArray.length){
									newRevealedArray[loop] = null;
									loop++;
								}
								revealedArray = newRevealedArray;
							}
							//Updating CollumnSize
							if(revealedArray[loopRow] == null){
								revealedArray[loopRow] = new boolean[loopCollumn + 1];
								for(int loop = 0; loop < revealedArray[loopRow].length; loop++){
									revealedArray[loopRow][loop] = false;
								}
							}
							else if(revealedArray[loopRow].length <= loopCollumn){
								boolean[] newRevelaedArray = new boolean[iDArray[loopRow].length];
								int loop = 0;
								while(loop < revealedArray[loopRow].length){
									newRevelaedArray[loop] = revealedArray[loopRow][loop];
									loop++;
								}
								while(loop < newRevelaedArray.length){
									newRevelaedArray[loop] = false;
									loop++;
								}
								revealedArray[loopRow] = newRevelaedArray;
							}
							
							//Type
							
							//Updating RowSize
							if(typeArray == null){
								typeArray = new int[iDArray.length][];
								for(int loop = 0; loop < typeArray.length; loop++){
									typeArray[loopRow] = null;
								}
							}
							else if(typeArray.length <= loopRow){
								int[][] newTypeArray = new int[iDArray.length][];
								int loop = 0;
								while(loop < typeArray.length){
									newTypeArray[loop] = typeArray[loop];
									loop++;
								}
								while(loop < newTypeArray.length){
									newTypeArray[loop] = null;
									loop++;
								}
								typeArray = newTypeArray;
							}
							//Updating CollumnSize
							if(typeArray[loopRow] == null){
								typeArray[loopRow] = new int[iDArray[loopRow].length];
								for(int loop = 0; loop < typeArray[loopRow].length; loop++){
									typeArray[loopRow][loop] = -1;
								}
							}
							else if(typeArray[loopRow].length <= loopCollumn){
								int[] newTypeArray = new int[iDArray[loopRow].length];
								int loop = 0;
								while(loop < typeArray[loopRow].length){
									newTypeArray[loop] = typeArray[loopRow][loop];
									loop++;
								}
								while(loop < newTypeArray.length){
									newTypeArray[loop] = -1;
									loop++;
								}
								typeArray[loopRow] = newTypeArray;
							}
							
							//breaking out of collumn loop now that greatest ID is found
							loopCollumn = -1;
						}
					}
				}
			}
		}
	}
	/**
	 * checks for duplicateIDs and removes them also unmarks any ID's that do 
	 */
	void updateIDPositions(ObjectSet objectGrid){
		//Unmarking Known ID's
		for(int loop = 0; loop <= maxID; loop++){
			IDPositions[loop][0] = -1;
			IDPositions[loop][1] = -1;
			IDPositions[loop][2] = -1;
		}
		//checking for duplicateIDs
		int currentMaxID = -1;
		int currentMinAvalableID = 0;
		//Marking Found ID's
		if(iDArray != null){
			for(int loopRow = 0; loopRow < iDArray.length; loopRow++){
				if(iDArray[loopRow] != null){
					for(int loopCollumn = 0; loopCollumn < iDArray[loopRow].length; loopCollumn++){
						//First instance of an ID is kept
						if(iDArray[loopRow][loopCollumn] > 0){
							if(IDPositions[iDArray[loopRow][loopCollumn] - 1][0] == -1){
								IDPositions[iDArray[loopRow][loopCollumn] - 1][0] = loopRow;
								IDPositions[iDArray[loopRow][loopCollumn] - 1][1] = loopCollumn;
								//updating MAX
								if(iDArray[loopRow][loopCollumn] - 1 > currentMaxID){
									currentMaxID = iDArray[loopRow][loopCollumn] - 1;
								}
								//updating MIN Available
								//assumns that the min available ID will be directly after
								if(iDArray[loopRow][loopCollumn] + 1 < IDPositions.length && IDPositions[iDArray[loopRow][loopCollumn] + 1][0] == -1	//Eligible Candidate?
										&& (iDArray[loopRow][loopCollumn] + 1 < currentMinAvalableID || IDPositions[currentMinAvalableID][0] != -1)){	//Should this replace the current Min Avalable
									currentMinAvalableID = iDArray[loopRow][loopCollumn];
								}
							}
							//Any Duplicates after that are removed
							else{
								iDArray[loopRow][loopCollumn] = 0;
							}
						}
					}
				}
			}
		}
		//setting MaxID
		maxID = currentMaxID;
		//setting next available ID
		if(IDPositions[0][0] == -1){
			currentMinAvalableID = 0;
		}
		nextAvalableID = currentMinAvalableID;
		//checking which ID's are still linked to an IntractableObject
		if(objectGrid != null && objectGrid.getObjectGrid() != null){
			for(int loopRow = 0; loopRow < objectGrid.getObjectGrid().length; loopRow++){
				if(objectGrid.getObjectGrid()[loopRow] != null){
					for(int loopCollumn = 0; loopCollumn < objectGrid.getObjectGrid()[loopRow].length; loopCollumn++){
						if(objectGrid.getObjectGrid()[loopRow][loopCollumn] != null && objectGrid.getObjectGrid()[loopRow][loopCollumn].ID > 0){
							IDPositions[objectGrid.getObjectGrid()[loopRow][loopCollumn].ID - 1][2] = 1;
						}
					}
				}
			}
		}
	}
}
