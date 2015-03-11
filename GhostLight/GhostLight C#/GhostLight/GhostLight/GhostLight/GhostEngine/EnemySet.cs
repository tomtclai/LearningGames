using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
/**
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class EnemySet {
	
	    //Padding	
	    private float topPadding = 0.7f;
	    private float bottomPadding = 0.7f;
	    private float rightPadding = 0.7f;
	    private float leftPadding = 0.7f;
	
        public ResourceLibrary localLibrary {get; private set;}
        private float worldWidth = 100;
        private float worldHeight = 100;
    
	    private int totalRows = 0;							//The Number of Row Positions available in the EnemySet
	    private int maxCollumns = 0;						//The Maximum number of column positions in a current Row
	
        private BaseEnemy[][] enemySetPrime = null; 		//The Array Used to store and share the Enemies in the Game in 2 Dimensions.
        private InteractableObject[][] IObjectSet = null;	//The Array Used to store and share the Objects the user has Access too
        private EnemySubSet tempStorage = new EnemySubSet();//Used to Store BaseEnemies that are animating while removing themselvs
    
        protected StateChange storedStateChangeObject = new StateChange();//Stored to prevent extra new(s) during each update
    
        /**
         * Initializes Primary Data Structures Although No Enemies are created At this point
         */
        public EnemySet(ResourceLibrary library, float worldWidth, float worldHeight){
    	    localLibrary = library;
            this.worldWidth = worldWidth;
            this.worldHeight = worldHeight;
        }
        /**
         * Returns the Number of Columns in the set
         * @return Number of Columns in the set
         */
        public int getNumberOfCollumns(){
    	    return maxCollumns;
        }
        /**
         * Returns the Number of Columns in the selected Row. Note, a -1 will be returned when no such row exists
         * @param row selected row
         * @return number of columns in selected row
         */
        public int getNumberOfCollumns(int row){
    	    if(row >= 0 && row < totalRows && enemySetPrime[row] != null){
    		    return this.enemySetPrime[row].Length;
    	    }
    	    return -1;
        }
        /**
         * Returns the Distance Between from the right hand of the screen to the first Enemy
         * @return
         */
        public int getNumberOfRows(){
    	    return totalRows;
        }
        /**
         * Returns the amount of world space between each column
         * @return The Space Between each column
         */
        public float getCollumnSpacing(){
    	    if(maxCollumns > 0){
    		    return (worldWidth-leftPadding-rightPadding)/maxCollumns;
    	    }
    	    return 0;
        }
        /**
         * Returns the amount of world space between each Row
         * @return The Space Between each row
         */
        public float getRowSpacing(){
    	    if(this.totalRows > 0){
    		    return (worldHeight-topPadding-bottomPadding)/totalRows;
    	    }
    	    return 0;
        }
        /**
         * Will set the padding(Distance from edge of screen) that this set will place all of the sets)
         * @param top Distance between top of screen and First Row of Enemies
         * @param bottom Distance between bottom of screen and Last Row of Enemies
         * @param left Distance between left of screen and First Collumn of Enemies
         * @param right Distance between right of screen and Last Collumn of Enemies
         */
        public void setSetPadding(float top, float bottom, float left, float right){
    	    if((top + bottom) <= worldHeight && (right + left) <= worldWidth){
    		    topPadding = top;
    		    bottomPadding = bottom;
    		    rightPadding = right;
    		    leftPadding = left;
    	    }
        }
        /**
         * Will Remove All enemies in the current Enemies set
         */
        //Nuke the site from orbit its the only way to be sure
        //...
        //Game over man Game over
        public void RemoveAllEnemies(){
    	    if(enemySetPrime != null){
	    	    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
	    		    if(enemySetPrime[loopRow] != null){
		    		    for(int loopCollumn = 0;  loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
		    			    if(enemySetPrime[loopRow][loopCollumn] != null){
		    				    enemySetPrime[loopRow][loopCollumn].removeThis();
		    				    enemySetPrime[loopRow][loopCollumn] = null;
		    			    }
		    		    }
	    		    }
	    	    }
		    }
    	    tempStorage.removeAllEnemies(true);
    	    enemySetPrime = null;
    	    IObjectSet = null;
    	    totalRows = 0;
    	    maxCollumns = 0;
    	
        }
        /**
         * Will Set the size of the current set to match the current grid
         * Set the targets of existing Enemies to there new position in the grid
         * New Enemies will enter from the Screen edge
         * Enemies not present in the grid will be removed 
         * Use get Grid to get the latest version of the grid
         * A state change object will be returned to dictate the changes in player state as a result of the synk
         * Note: BaseEnemy Objects are tied to a specific interactableObjects in the Grid. So adding new InteractableObjects
         * will add new BaseEenemys and removing old Intractable objects will remove old BaseEnemies
         * @param synkSet The set that will be used to Sync
         * @return The changee in game state as a result of the synk
	     * This includes revealing a Enemy, and Moving an enemy
         */
        public StateChange synkGrid(InteractableObject[][] synkSet, int pauseTime){
    	    IObjectSet = synkSet;
    	    updateIOGridPositions();
    	    matchSizeIOGridToPrimeGrid();
    	    setsetSizes();
    	    StateChange change = updatePrimeGrid(pauseTime);
    	    return revealTrigeredEnemies(pauseTime, change);
        }
        /**
         * Will update and return the current InteractableObject[][] grid with the updated state of all of the enemies in the set
         * @return
         */
        public InteractableObject[][] getGrid(){
    	    clearAllRepresentatives();
    	    updatePrimeGridPositions();
    	    updateIOgrid();
    	    return IObjectSet;
        }
        /**
         * will Update all of the Enemies in the set. should be called after any changes were made to either the Row or Grid
         */
        public void update(int animationTime){
    	    updateTimeSlice(animationTime);
        }
        /**
         * Will return the Enemy that resides at the given Row and collumn If not enemy Exists at this location null will be returned instead
         * @param row
         * @param collumn
         * @return Enemy at location
         */
        public BaseEnemy getEnemyAt(int row, int collumn){
    	    if(doesGridPositionExist(row, collumn)){
    		    return enemySetPrime[row][collumn];
    	    }
    	    return null;
        }
        /**
         * Will Attempt to move the Given Enemy to the given position
         * Note this function requires that the Given BaseEnemy be already active in the set
         * Enemy enemy already at the target position will be removed from the set;
         * Assums that the 
         * @param moveTarget BaseEnemy you wish to move
         * @param row		Target row to move too
         * @param collumn	Target column to move too
         * @return			True if the Enemy was either moved to a different location in the set or moved into the set for the first time
         */
        public bool moveEnemy(BaseEnemy moveTarget, int row, int collumn){
    	    if(moveTarget != null && doesGridPositionExist(row, collumn)){
    		    //Moving Enemy to position
    		    if(isActiveEnemy(moveTarget)){
    			    //Enemy is not at current position
	    		    if(row != moveTarget.currentRow || collumn != moveTarget.currentCollumn){
	    			    if(enemySetPrime[row][collumn] != null){
	    				    removeEnemy(enemySetPrime[row][collumn]);
	    			    }
	    			    enemySetPrime[row][collumn] = moveTarget;
	    			    enemySetPrime[moveTarget.currentRow][moveTarget.currentCollumn] = null;
	    			    moveTarget.setPosition(row, collumn, this);
	    			    moveTarget.setAnimationTarget(row, collumn);
	    		    }
	    		    //Enemy is already at the desired position
	    		    else{
	    			    return false;
	    		    }
    		    }
    		    //adding Enemy
    		    else{
    			    if(enemySetPrime[row][collumn] != null){
        			    removeEnemy(enemySetPrime[row][collumn]);
        		    }
        		    enemySetPrime[row][collumn] = moveTarget;
        		    moveTarget.setPosition(row, collumn, this);
                    BaseCode.activeDrawSet.addToDrawSet(moveTarget);
    		    }
    		    return true;
    	    }
    	    return false;
        }
        /**
         * Will insert the given BaseEnemy at the given location if possible and will remove any enemy residing at that location already
         * @param newEnemy the given BaseEnemy
         * @param row the row the enemy will be placed in
         * @param collumn the collumn the enemy will be placed in
         * @return whether or not the enemy was successfully added
         */
        public bool addEnemy(BaseEnemy newEnemy, int row, int collumn){
    	    if(newEnemy != null && doesGridPositionExist(row, collumn) && !isActiveEnemy(newEnemy)){
    		    if(enemySetPrime[row][collumn] != null){
    			    removeEnemy(enemySetPrime[row][collumn]);
    		    }
    		    enemySetPrime[row][collumn] = newEnemy;
    		    newEnemy.setPosition(row, collumn, this);
    		    newEnemy.setAnimationTarget(row, collumn);
                BaseCode.activeDrawSet.addToDrawSet(newEnemy);
    		    return true;
    	    }
    	    return false;
        }
        /**
         * Will remove the given BaseEnemy from the set and the Interactable object will be removed the 
         * next time getGrid() is used
         * @param removeTarget the baseEnemy that will be removed
         * @return whether or not the removal was successful
         */
        public bool removeEnemy(BaseEnemy removeTarget){
    	    if(removeTarget != null && isActiveEnemy(removeTarget)){
    		    enemySetPrime[removeTarget.currentRow][removeTarget.currentCollumn] = null;
    		    return true;
    	    }
    	    return false;
        }
        /**
         * Will set the Animation position of all of the Enemies in the set to the specified animation position 
         * @param AnimationPlace the desired animation position
         */
        public void setEnemyAnimations(int AnimationPlace){
    	    if(enemySetPrime != null){
    		    for(int loopHeight = 0; loopHeight < enemySetPrime.Length; loopHeight++){
    			    if(enemySetPrime[loopHeight] != null){
    				    for(int loopWidth = 0;  loopWidth < enemySetPrime[loopHeight].Length; loopWidth++){
    					    if(enemySetPrime[loopHeight][loopWidth] != null){
    						    enemySetPrime[loopHeight][loopWidth].setAnimationPosition(AnimationPlace);
    					    }
    				    }
    			    }
    		    }
    	    }
        }
        /**
         * Will remove the given Enemy from the grid and add it to the subSet(temporary starage until the Enemy removes Itself)
         */
        public void moveEnemytoSubSet(BaseEnemy moveTarget){
    	    if(moveTarget != null){
    		    if(isActiveEnemy(moveTarget)){
    			    removeEnemy(moveTarget);
    			    moveTarget.hostSet = null;
    		    }
    		    tempStorage.addEnemy(moveTarget, true);
    	    }
        }
        /**
         * Will move a given InteractableObject to the IOset at the given position. if the 
         * given InteractableObject is not a member of the IOset then it will add it to the set
         * If this representative would replace a representative already at that location that representative will be removed
         * This does not call the .destroy() of the representative
         * @param newReprestitive The Representative to be moved/added
         * @param row 
         * @param collumn
         * @return whether or mot the move was successfull
         */
        public bool moveRepresentative(InteractableObject newReprestitive, int row, int collumn){
    	    if(newReprestitive != null && doesGridPositionExist(row, collumn)){
    		    //Moving Representative
    		    if(this.isRepresentativeActive(newReprestitive)){
    			    IObjectSet[row][collumn] = newReprestitive;
    			    IObjectSet[((Representative)newReprestitive).currentRow][((Representative)newReprestitive).currentCollumn] = null;
    			    ((Representative)newReprestitive).currentRow = row;
    			    ((Representative)newReprestitive).currentCollumn = collumn;
    		    }
    		    //adding Representative to set
    		    else{
    			    IObjectSet[row][collumn] = newReprestitive;
    			    ((Representative)newReprestitive).currentRow = row;
    			    ((Representative)newReprestitive).currentCollumn = collumn;
    		    }
    		    return true;
    	    }
    	    return false;
        }
        /**
         * 
         * @return
         */
        public bool removeRepresentative(InteractableObject removeTarget){
    	    if(removeTarget != null && IObjectSet != null && isRepresentativeActive(removeTarget)){
    		    IObjectSet[((Representative)removeTarget).currentRow][((Representative)removeTarget).currentCollumn] = null;
    		    return true;
    	    }
    	    return false;
        }
        /**
         * Will set the Given Vector to the world location of the given (row, column)
         * Note positions outside of the width and height of the grid are likely to be off screen
         * @param host The Vector that will be set to the desired world position note if the reference points to null no actions will be taken by this function
         * @param row
         * @param collumn
         */
        public PointF getWorldPosition(int row, int collumn){
            PointF retVal = new PointF();
    	    if(retVal != null){
    		    retVal.X = (leftPadding);
    		    if(maxCollumns > 1){
    			    retVal.X = (retVal.X + (((worldWidth - leftPadding - rightPadding) / (maxCollumns - 1)) * collumn));
    		    }
    		    else{
    			    retVal.X =(retVal.X + ((worldWidth - leftPadding - rightPadding) * collumn));
    		    }
                retVal.Y = topPadding;
    		    if(totalRows > 1){
    			    retVal.Y = (retVal.Y + (((worldHeight - topPadding - bottomPadding) / (totalRows - 1)) * row));
    		    }
    		    else{
    			    retVal.Y = (retVal.Y + ((worldHeight - topPadding - bottomPadding) * row));
    		    }
    	    }
    	    return retVal;
        }
        /**
         * Will call updateTurn() on all of the Enemies in the Set
         */
        public StateChange updateTurn(){
    	    storedStateChangeObject.setToDefualts();
    	    if(enemySetPrime != null){
    		    for(int loopHeight = 0; loopHeight < enemySetPrime.Length; loopHeight++){
    			    if(enemySetPrime[loopHeight] != null){
    				    for(int loopWidth = 0;  loopWidth < enemySetPrime[loopHeight].Length; loopWidth++){
    					    if(enemySetPrime[loopHeight][loopWidth] != null){
    	    				    enemySetPrime[loopHeight][loopWidth].setAnimationPosition(0);
    	    				    storedStateChangeObject.add(enemySetPrime[loopHeight][loopWidth].updateTurn());
    	    			    }
    				    }
    			    }
    		    }
    	    }
    	    return storedStateChangeObject;
        }
        /**
         * Should Be called Every TimeSlice update
         */
        public void updateTimeSlice(int animTime){
    	    if(enemySetPrime != null){
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null){
	    					    enemySetPrime[loopRow][loopCollumn].update(animTime);
	    				    }
	    			    }
    			    }
    		    }
    	    }
    	    tempStorage.updateEnemiesInSet(animTime);
        }
        /**
         * Will set all contained Enemies to move to the the given world position and reset there animation Position to zero
         * 
         * @param worldPosition the Row Enemies 
         */
        public void moveAllEnemiestoVerticalPosition(int gridRow){
    	    if(enemySetPrime != null){
    		    updatePrimeGridPositions();
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null){
	    					    enemySetPrime[loopRow][loopCollumn].setAnimationTarget(gridRow, enemySetPrime[loopRow][loopCollumn].currentCollumn);
	    					    enemySetPrime[loopRow][loopCollumn].setAnimationPosition(0);
	    				    }
	    			    }
    			    }
    		    }
    	    }
        }
        /**
         * Will Draw all of the enemies in this set
         */
        public void drawUnselected(Camera drawLocation){
    	    //Drawing Prime set
    	    if(enemySetPrime != null){
    		    updatePrimeGridPositions();
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null && !enemySetPrime[loopRow][loopCollumn].isEnemyCircled()){
	    					    enemySetPrime[loopRow][loopCollumn].paint(drawLocation);
	    				    }
	    			    }
    			    }	
    		    }
    	    }
    	    //Drawing Sub set
            tempStorage.drawAll(drawLocation);
        }
        public void drawSelected(Camera drawLocation){
    	    //Drawing Prime set
    	    if(enemySetPrime != null){
    		    updatePrimeGridPositions();
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null && enemySetPrime[loopRow][loopCollumn].isEnemyCircled()){
	    					    enemySetPrime[loopRow][loopCollumn].paint(drawLocation);
	    				    }
	    			    }
    			    }	
    		    }
    	    }
        }
        /**
         * Will update the known position of the elements of the IObjectSet
         */
        private void updateIOGridPositions(){
    	    if(IObjectSet != null){
    		    //rows
    		    for(int loopRow = 0; loopRow < IObjectSet.Length; loopRow++){
    			    //Columns
    			    if(IObjectSet[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < IObjectSet[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(IObjectSet[loopRow][loopCollumn] != null){
	    					    ((Representative)IObjectSet[loopRow][loopCollumn]).currentRow = loopRow;
	    					    ((Representative)IObjectSet[loopRow][loopCollumn]).currentCollumn = loopCollumn;
	    				    }
	    			    }
    			    }
    		    }
    	    }
        }
        /**
         * Will update the known position of the elements of the enemySetPrime
         */
        private void updatePrimeGridPositions(){
    	    if(enemySetPrime != null){
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null){
	    					    enemySetPrime[loopRow][loopCollumn].setPosition(loopRow, loopCollumn, this);
	    				    }
	    			    }
    			    }
    		    }
    	    }
        }
        /**
         * Will update the BaseEnemies in EnemySetPrime to match the state of there representatives in IObjectSet
         * @return whether or not any changes were made that actually require the game to pause
	     * This includes revealing a Enemy, and Moving an enemy
         */
        private StateChange updatePrimeGrid(int pauseTime){
    	    StateChange changesMade = null;
    	    updateIOGridPositions();
    	    if(IObjectSet != null){
    		    //rows
    		    for(int loopRow = 0; loopRow < IObjectSet.Length; loopRow++){
    			    //Columns
    			    if(IObjectSet[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < IObjectSet[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(IObjectSet[loopRow][loopCollumn] != null){
	    					    changesMade = ((Representative)IObjectSet[loopRow][loopCollumn]).updateSubject(this, pauseTime).add(changesMade);
	    				    }
	    			    }
    			    }
    		    }
    	    }
    	    return changesMade;
        }
        /**
         * Will update the representatives in IObjectSet with the state of the BaseEnemies
         */
        private void updateIOgrid(){
    	    updatePrimeGridPositions();
    	    if(enemySetPrime != null){
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null){
	    					    enemySetPrime[loopRow][loopCollumn].updateRepresentative();
	    				    }
	    			    }
    			    }
    		    }
    	    }
        }
        /**
         * Will reveal any Enemies that have been marked to reveal them selves and will add any state changes to the given StateChange object
         * @param puaseTime
         * @param change
         * @return
         */
        private StateChange revealTrigeredEnemies(int puaseTime, StateChange change){
    	    if(change == null){
    		    change = new StateChange();
    	    }
    	    if(enemySetPrime != null){
    		    //rows
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    //Columns
    			    if(enemySetPrime[loopRow] != null){
	    			    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    				    //elements
	    				    if(enemySetPrime[loopRow][loopCollumn] != null){
	    					    change.add(enemySetPrime[loopRow][loopCollumn].revealIfSetToo(puaseTime));
	    				    }
	    			    }
    			    }
    		    }
    	    }
    	    return change;
        }
        /**
         * Will Set the PrimeEnemySet set Size to match the size of the Io
         * If the Size must be changed then Enemies will migrate to the upper right hand corrner of the grid
         * and enemies are left in position that are outside of the new grid then nothing will be done of them
         * It is users responsibility to destroy() enemies they remove 
         */
        private void matchSizeIOGridToPrimeGrid(){
    	    //Salvigable
    	    if(enemySetPrime != null){
    		    //Still Salvigable
    		    if(IObjectSet != null){
    			    //Fixing Rows
    			    if(enemySetPrime.Length != IObjectSet.Length){
    				    BaseEnemy[][] newPrime = new BaseEnemy[IObjectSet.Length][];
    				    int loop = 0;
    				    //Moving over old Elements
    				    while(loop < enemySetPrime.Length && loop < IObjectSet.Length){
    					    newPrime[loop] = enemySetPrime[loop];
    					    loop++;
    				    }
    				    //Initializing Excess to null
    				    while(loop < newPrime.Length){
    					    newPrime[loop] = null;
    					    loop++;
    				    }
    				    //Removing objects moved out of set
    				    for(int loopRow = IObjectSet.Length - 1;loopRow < enemySetPrime.Length; loopRow++){
    					    if(enemySetPrime[loopRow] != null){
	    					    for(int loopCollumn = 0; loopCollumn < enemySetPrime[loopRow].Length; loopCollumn++){
	    						    if(enemySetPrime[loopRow][loopCollumn] != null){
	    							    enemySetPrime[loopRow][loopCollumn].removeThis();
	    						    }
	    					    }
    					    }
       				    }
    				    enemySetPrime = newPrime;
    			    }
    			    //Fixing Columns
    			    for(int loopRows = 0; loopRows < enemySetPrime.Length; loopRows++){
    				    enemySetPrime[loopRows] = CheckArraySizes(IObjectSet[loopRows], enemySetPrime[loopRows]);
    			    }
    		    }
    		    //There is no IObjectSet Destroy everything
    		    else{
    			    enemySetPrime = null;
    		    }
    	    }
    	    //Complete rebuild
    	    else if(IObjectSet != null){
    		    enemySetPrime = new BaseEnemy[IObjectSet.Length][];
    		    for(int loopRows = 0; loopRows < IObjectSet.Length; loopRows++){
    			    if(IObjectSet[loopRows] != null){
    				    //Creating row
    				    enemySetPrime[loopRows] = new BaseEnemy[IObjectSet[loopRows].Length];
    				    //Initializing collumns
    				    for(int loopCollumns = 0; loopCollumns < IObjectSet[loopRows].Length; loopCollumns++){
    					    enemySetPrime[loopRows][loopCollumns] = null;
    				    }
    			    }
    		    }
    	    }
        }
        /**
         * Will Set the IOset  Size to match the size of the primeSet
         * If the Size must be changed then Enemies will migrate to the upper right hand corrner of the grid
         * and enemies are left in position that are outside of the new grid then nothing will be done of them
         * It is users responsibility to destroy() enemies they remove 
         * @return returns the updated primeSet
         */
        private BaseEnemy[] CheckArraySizes(InteractableObject[] IOset, BaseEnemy[] primeSet){
    	    if(primeSet != null){
    		    //Salvage
    		    if(IOset != null){
    			    if(IOset.Length != primeSet.Length){
    				    BaseEnemy[] newPrime = new BaseEnemy[IOset.Length];	//newSet
    				    int loop = 0;
    				    //Moving over old Elements
    				    while(loop < IOset.Length && loop < primeSet.Length){
    					    newPrime[loop] = primeSet[loop];
    					    loop++;
    				    }
    				    //Initializing Excess to null
    				    while(loop < newPrime.Length){
    					    newPrime[loop] = null;
    					    loop++;
    				    }
    				    //Removing old enemies
    				    for(int loopRemove = IOset.Length - 1; loopRemove < primeSet.Length; loopRemove++){
    					    if(primeSet[loopRemove] != null){
    						    primeSet[loopRemove].removeThis();
    					    }
    				    }
    				    primeSet = newPrime;
    			    }
    		    }
    		    //Destroy all
    		    else{
    			    primeSet = null;
    		    }
    	    }
    	    //Complete Rebuild
    	    else if(IOset != null){
    		    primeSet = new BaseEnemy[IOset.Length];
    		    for(int loop = 0; loop < IOset.Length; loop++){
    			    primeSet[loop] = null;
    		    }
    	    }
    	    return primeSet;
        }
        /**
         * returns true if a given grid position exists within the primeEnemySet
         * @param row
         * @param collumn
         * @return
         */
        public bool doesGridPositionExist(int row, int collumn){
    	    if(enemySetPrime != null && row >= 0 && collumn >= 0 &&
    			    row < enemySetPrime.Length && enemySetPrime[row] != null &&
    			    collumn < enemySetPrime[row].Length){
    		    return true;
    	    }
    	    return false;
        }
        /**
         * returns true if the given Enemy is currently active within the primeEnemtSet
         * @param target
         * @return
         */
        private bool isActiveEnemy(BaseEnemy target){
    	    //information is accurate and up to date
    	    if(target != null && target.hostSet == this && enemySetPrime != null){
	    	    if(getEnemyAt(target.currentRow, target.currentCollumn) == target){
	    		    return true;
	    	    }
	    	    //searching for target
	    	    else if(doesGridPositionExist(target.currentRow, target.currentCollumn)){
	    		    for(int loopRows = 0; loopRows < getNumberOfRows(); loopRows++){
    				    for(int loopCollumns = 0; loopCollumns < getNumberOfCollumns(loopRows); loopRows++){
    					    if(enemySetPrime[loopRows][loopCollumns] == target){
    						    target.setPosition(loopRows, loopCollumns, this);
    						    return true;
    					    }
	    			    }
	    		    }
	    	    }
	    	    //Target is not in set and inactive
    	    }
    	    return false;
        }
        private bool isRepresentativeActive(InteractableObject rep){
            if(rep != null && IObjectSet != null){
        	    if(doesGridPositionExist(((Representative)rep).currentRow, ((Representative)rep).currentCollumn) && rep == IObjectSet[((Representative)rep).currentRow][((Representative)rep).currentCollumn]){
        		    return true;
        	    }
        	    else{
        		    for(int loopRow = 0; loopRow < IObjectSet.Length; loopRow++){
        			    if(IObjectSet[loopRow] != null){
        				    for(int loopCollumn = 0; loopCollumn < IObjectSet[loopRow].Length; loopCollumn++){
        					    if(IObjectSet[loopRow][loopCollumn] == rep){
        						    ((Representative)rep).currentRow = loopRow; 
        						    ((Representative)rep).currentCollumn = loopCollumn; 
        						    return true;
        					    }
        				    }
        			    }
        			
        		    }
        	    }
            }
            return false;
        }
        /**
         * Will set all elements within the IObjectSet[][] to null
         */
        //Becuase Sometimes firing all the representatives is the right thing to do
        //Let us see people truly want you all back
        private void clearAllRepresentatives(){
    	    if(IObjectSet != null){
    		    for(int loopRow = 0; loopRow < IObjectSet.Length; loopRow++){
    			    if(IObjectSet[loopRow] != null){
    				    for(int loopCollumn = 0; loopCollumn < IObjectSet[loopRow].Length; loopCollumn++){
    					    IObjectSet[loopRow][loopCollumn] = null;
    				    }
    			    }
    		    }
    	    }
        }
        /**
         * Will set maxCollumns based on the longest Column array in enemySetPrime
         * and will set totalRows bassed on the length of enemySetPrime.length
         */
        private void setsetSizes(){
    	    maxCollumns = 0;
    	    totalRows = 0;
    	    if(enemySetPrime != null){
    		    totalRows = enemySetPrime.Length;
    		    for(int loopRow = 0; loopRow < enemySetPrime.Length; loopRow++){
    			    if(enemySetPrime[loopRow] != null){
    				    if(enemySetPrime[loopRow].Length > maxCollumns){
    					    maxCollumns = enemySetPrime[loopRow].Length;
    				    }
    			    }
    		    }
    		
    	    }
        }
    }

}

