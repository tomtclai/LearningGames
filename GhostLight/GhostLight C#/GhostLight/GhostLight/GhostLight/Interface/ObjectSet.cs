using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GhostFinder.Interface{
    /// <summary>
    /// Manages a set of InteractableObjects
    ///  author: Michael Letter
    /// </summary>
    public class ObjectSet {
        /// <summary> Stores the current Enemy Set that will be synked by true </summary>
	    private InteractableObject[][] enemySet = null;

	    /// <summary>
	    /// Returns the current InteractableObject Grid as a 2D array with the first Dimension Representing
        /// Rows and the Second Dimension Representing columns and where the Object[0][0] is at the upper left Corner
	    /// </summary>
        /// <returns>InteractableObject[Row][Column]</returns>
	    public InteractableObject[][] getObjectGrid(){
		    return enemySet;
	    }
	    /// <summary>
        /// Will replace the current current InteractableObject Grid in use to a given grid Defined by the user
	    /// </summary>
	    /// <param name="newObjectSet">newGrid a 2D array of InteractableObjects, InteractableObject[row][column], where the first Dimension 
	    /// represents Rows and the second Dimension repents columns and where the Object[0][0] is at the upper left Corner</param>
	    public void setObjectGrid(InteractableObject[][] newObjectSet){
		    enemySet = newObjectSet;
	    }
	    /// <summary>
        /// Will return the requested InteractableObject
	    /// </summary>
        /// <param name="row">the requested row. row zero represents the top row</param>
        /// <returns>InteractableObject[collumn]</returns>
	    public InteractableObject[] getObjectRow(int row){
		    if(enemySet != null && row >= 0 && row < enemySet.Length){
			    return enemySet[row];
		    }
		    else{
			    return null;
		    }
	    }
	    /// <summary>
	    /// will use the given array to replace the be the row to be the new 
	    /// InteractableObject row at the specified row.
	    /// Note, This operation does not remove the enemies at the row being replaced. If you want 
        /// then removed call the InteractableObject.destroy() on each InteractableObject in the row being replaced prior to calling this function
	    /// </summary>
        /// <param name="newRow">the row the given InteractableObject[] will replace</param>
	    /// <param name="row">row a 1 dimension of array of InteractableObjects where the InteractableObject at index 0 is the left most InteractableObject.
        /// Note, the given row must exist be less then the total rows and be zero or greater</param>
        /// <returns>true if the replacement was successful. otherwise false</returns>
	    public bool setObjectGrid(InteractableObject[] newRow, int row) {
		    if(row >= 0){
			    if(enemySet == null || enemySet.Length <=  row){
				    setNumberOfRows(row + 1);
			    }
			    enemySet[row] = newRow;
			    return true;
		    }
		    return false;
	    }
	    /// <summary>
	    /// Will move a target Row to a given Row position. If another Row already exists at that given position
        /// It will be moved into the row position originally inhabited by the target Row.
	    /// </summary>
        /// <param name="targetRow">the Row index of the Row to me moved</param>
        /// <param name="targetPosition">The Row index of the targetRow will be moved to</param>
        /// <returns>If TRUE, the move was successful, If FALSE, the move was unsuccessful and no changes were made.</returns>
	    public bool moveRow(int targetRow, int targetPosition){
		    //is operation possible
		    if(enemySet != null && targetRow >= 0 && targetPosition >= 0 && (targetRow >= enemySet.Length || targetPosition >= enemySet.Length)){
			    //resizing if nessesary
			    if(enemySet.Length <= targetPosition){
				    setNumberOfRows(targetPosition + 1);
			    }
			    //swapping Rows
			    if(enemySet.Length > targetRow){
				    InteractableObject[] temp = enemySet[targetRow];
				    enemySet[targetRow] = enemySet[targetPosition];
				    enemySet[targetPosition] = temp;
			    }
			    //killing Row
			    else{
				    enemySet[targetPosition] = null;
			    }
			    return true;
		    }
		    return false;
	    }
	    /// <summary>
	    /// Will set the number of Rows in the current set to the requested number
	    /// If Rows currently exist in the set then rows that would exist in the new 
	    /// row set will persist at the current location. If they are greater than or 
	    /// equal to the new row count then they will be lost. Note if you want these enemies 
        /// removed then you must call InteractableObject.destroy() prior to calling this function
	    /// </summary>
        /// <param name="newRowCount">the number of rows that the enemySet will contain at the conclusion of this function</param>
        /// <returns>true if the operation was successful returns false  unsuccessful, in which case if no changes were made</returns>
	    public bool setNumberOfRows(int newRowCount){
		    //if possible length
		    if(newRowCount > 0){
			    //are the rows to salvage
			    if(enemySet != null){
				    if(enemySet.Length != newRowCount){
					    InteractableObject[][] newSet = new InteractableObject[newRowCount][];
					    int loop = 0;
					    while(loop < newSet.Length && loop < enemySet.Length){
						    newSet[loop] = enemySet[loop];
						    loop++;
					    }
					    while(loop < newSet.Length){
						    newSet[loop] = null;
					    }
					    enemySet = newSet;
				    }
			    }
			    else{
				    enemySet = new InteractableObject[newRowCount][];
				    for(int loop = 0; loop < enemySet.Length; loop++){
					    enemySet[loop] = null;
				    }
			    }
			    return true;
		    }
		    else if(newRowCount == 0){
			    enemySet = null;
			    return true;
		    }
		    else{
			    return false;
		    }
	    }
	    /// <summary>
        /// Will destroy all InteractableObjects contained in the set and will initialize the max column and row counts to zero
	    /// </summary>
	    public void reset(){
		    if(enemySet != null){
			    for(int loopRows = 0; loopRows < enemySet.Length; loopRows++){
				    if(enemySet[loopRows] != null){
					    for(int loopCollumns = 0; loopCollumns < enemySet[loopRows].Length; loopCollumns++){
						    if(enemySet[loopRows][loopCollumns] != null){
							    enemySet[loopRows][loopCollumns].destroy();
						    }
					    }
				    }
			    }
			    enemySet = null;
		    }
	    }
	    /// <summary>
        ///  Will synk the state of the given PrimitiveSet with this ObjectSets state 
	    /// </summary>
	    /// <param name="primitiveGrid"></param>
	    public void synkPrimitiveSet(PrimitiveSet primitiveGrid){
		    if(primitiveGrid != null){
			    resizePrimitiveIDs(primitiveGrid);
			    if(enemySet != null){
				    for(int loopRow = 0; loopRow < enemySet.Length; loopRow++){
					    if(enemySet[loopRow] != null){
						    for(int loopCollumn = 0; loopCollumn < enemySet[loopRow].Length; loopCollumn++){
							    primitiveGrid.getIDArray()[loopRow][loopCollumn] = -1;
							    if(enemySet[loopRow][loopCollumn] != null){
								    if(enemySet[loopRow][loopCollumn].ID <= 0){
									    enemySet[loopRow][loopCollumn].ID = primitiveGrid.getUniqueID();
								    }
								    primitiveGrid.getIDArray()[loopRow][loopCollumn] = enemySet[loopRow][loopCollumn].ID;
							    }
						    }
					    }
				    }
				    primitiveGrid.resizeArrays();
				    for(int loopRow = 0; loopRow < enemySet.Length; loopRow++){
					    if(enemySet[loopRow] != null){
						    for(int loopCollumn = 0; loopCollumn < enemySet[loopRow].Length; loopCollumn++){
							    if(enemySet[loopRow][loopCollumn] != null){
								    primitiveGrid.getTypeArray()[loopRow][loopCollumn] = (int)enemySet[loopRow][loopCollumn].getType();
								    primitiveGrid.getHealthIncrementArray()[loopRow][loopCollumn] = enemySet[loopRow][loopCollumn].getMaximumHealth();
								    //if((int)(primitiveGrid.getHealthArray()[loopRow][loopCollumn] * (float)enemySet[loopRow][loopCollumn].getMaximumHealth()) != enemySet[loopRow][loopCollumn].getHealth()){
									    primitiveGrid.getHealthArray()[loopRow][loopCollumn] = ((float)enemySet[loopRow][loopCollumn].getHealth() / (float)enemySet[loopRow][loopCollumn].getMaximumHealth());
								    //}
								    primitiveGrid.getScoreArray()[loopRow][loopCollumn] = enemySet[loopRow][loopCollumn].getScore();
								    primitiveGrid.getRevealedArray()[loopRow][loopCollumn] = enemySet[loopRow][loopCollumn].isRevealed();
							    }
						    }
					    }
				    }
			    }
		    }
	    }
	    /// <summary>
        /// Will resize the idArray in the given PrimitiveSet and will
	    /// </summary>
	    /// <param name="primitiveGrid"></param>
	    private void resizePrimitiveIDs(PrimitiveSet primitiveGrid){
		    if(primitiveGrid != null && enemySet != null){
			    //Rows
			    //Create
			    if(primitiveGrid.getIDArray() == null){
				    int[][] newIDArray = new int[enemySet.Length][];
				    for(int loopRow = 0; loopRow < enemySet.Length; loopRow++){
					    newIDArray[loopRow] = null;
				    }
				    primitiveGrid.setIDArray(newIDArray);
			    }
			    //Resize
			    else if(primitiveGrid.getIDArray().Length < enemySet.Length){
				    int[][] newIDArray = new int[enemySet.Length][];
				    int loop = 0;
				    while(loop < primitiveGrid.getIDArray().Length){
					    newIDArray[loop] = primitiveGrid.getIDArray()[loop];
					    loop++;
				    }
				    while(loop < newIDArray.Length){
					    newIDArray[loop] = null;
					    loop++;
				    }
				    primitiveGrid.setIDArray(newIDArray);
			    }
			    //Columns
			    for(int loopRow = 0; loopRow < enemySet.Length; loopRow++){
				    if(enemySet[loopRow] != null){
					    //Create
					    if(primitiveGrid.getIDArray()[loopRow] == null){
						    int[] newIDArray = new int[enemySet[loopRow].Length];
						    for(int loopCollumn = 0; loopCollumn < newIDArray.Length; loopCollumn++){
							    newIDArray[loopCollumn] = 0;
						    }
						    primitiveGrid.getIDArray()[loopRow] = newIDArray;
					    }
					    //Resize
					    else if(primitiveGrid.getIDArray()[loopRow].Length < enemySet[loopRow].Length){
						    int[] newIDArray = new int[enemySet[loopRow].Length];
						    int loopCollumn = 0;
						    while(loopCollumn < primitiveGrid.getIDArray()[loopRow].Length){
							    newIDArray[loopCollumn] = primitiveGrid.getIDArray()[loopRow][loopCollumn];
							    loopCollumn++;
						    }
						    while(loopCollumn < newIDArray.Length){
							    newIDArray[loopCollumn] = 0;
							    loopCollumn++;
						    }
						    primitiveGrid.getIDArray()[loopRow] = newIDArray;
					    }
				    }
			    }
		    }
	    }
    }
}
