package layers;

import gridElements.GridElement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import structures.IntVector;
import structures.IntVectorPathFinder;
import corrupted.Game;
import corrupted.ErrorHandler;

public abstract class LogicBase
{
	protected Game mGM;

	/**
	 * initializes the layer using the size established in the Game.
	 * 
	 * @author Brian Chau
	 * @param layM Game for communication with other layers
	 */
	public LogicBase(Game layM) {
		mGM = layM;
	}
	
	
	/**
	 * this method should be overridene in the other logic helper classes to retrieve the correct grid
	 * @return the grid that this Logic class executes on.
	 */
	protected abstract GridElement[][] getTheGrid();


	/**
	 * Checks a given index in the grid and ensures that the geometry of the GridElement is set to the array index.
	 * Animations will be played if movement is required
	 */
	public void syncPosition(int x, int y)
	{
		GridElement[][] grid = getTheGrid();
		try{
		IntVector pos = grid[x][y].getIntCenter();
		IntVector index = new IntVector(x,y);
		boolean animate = true;
		if (pos.getX() == Integer.MIN_VALUE && pos.getY() == Integer.MIN_VALUE)
		{
			animate = false;
		}
		grid[x][y].moveTo(animate, index);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			//swallow it. its okay.
		}
	}
	/**
	 * Checks each index in the grid and ensures that the geometry of the GridElement is set to the array index.
	 * Animations will be played if movement is required
	 */
	public void syncPositions()
	{
		GridElement[][] grid = getTheGrid();
		try{
			for(int x = 0; x < mGM.getWidth(); x++)
			{
				for (int y = 0; y < mGM.getHeight(); y++)
				{
					if(grid[x][y] != null)
					{
						IntVector pos = grid[x][y].getIntCenter();
						IntVector index = new IntVector(x,y);
						//if it moved (geometry location does not match index), correct the geometry and animate the movement
						if (pos.getX() != x || pos.getY() != y)
						{
							boolean animate = true;
							if (pos.getX() == Integer.MIN_VALUE && pos.getY() == Integer.MIN_VALUE)
							{
								animate = false;
							}
							grid[x][y].moveTo(animate, index);
						}
						//if it needs to be deleted, remove it
						if(grid[x][y].isMarkedForDelete())
						{
							grid[x][y]=null;
						}
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
	}
	
	/**
	 * places a GridElement into the grid. If an element already exists in the
	 * target grid position, the existing object is removed and replaced with this new
	 * one. If pos is out of bounds, the command is ignored and a message is printed to the console
	 * If element is null, it sets that grid position to null.
	 * 
	 * @author Brian Chau
	 * @param pos Coordinate to put element
	 * @param element GridElement to place (position is derived from here)
	 * @return true if successful, false otherwise
	 */
	protected boolean putElement(IntVector pos, GridElement element) {
		if (pos == null) return false;
		GridElement[][] grid = getTheGrid();
		int x = pos.getX();//(int) element.getCenterX();
		int y = pos.getY();//(int) element.getCenterY();
		// check bounds
		if (!withinBounds(x, y)) {
			if(element != null){
				element.destroy();
			}
			return false;
		}
		try{
			grid[x][y] = element;
			if(element != null){
				element.moveTo(false, pos);
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
		return true;
	}
	
	
	/**
	 * Gets the grid element in a given index.
	 * Returns null if pos is null or out of bounds.
	 * 
	 * @author Brian Chau
	 * @param pos
	 * @return the element from the index
	 */
	public GridElement getElement(IntVector pos) {
		if (pos == null) return null;
		return getElement(pos.getX(), pos.getY());
	}
	/**
	 * Gets the grid element in a given index.
	 * Returns null of coordinates are out of bounds.
	 * 
	 * @author Brian Chau
	 * @param x x index component
	 * @param y y index component
	 * @return the element from the index
	 */
	public GridElement getElement(int x, int y) {
		GridElement[][] grid = getTheGrid();
		//return null if out of bounds
		if(!withinBounds(x,y)){
			return null;
		}
		try{
		return grid[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
		return null;
	}
	
	
	/**
	 * destroys a GridElement in a given index and sets the index to null
	 * returns true if something was destroyed
	 * @param x horizontal component of position to destroy
	 * @param y vertical component of position to destroy
	 */
	public boolean markForDelete(int x, int y)
	{
		GridElement[][] grid = getTheGrid();
		GridElement ge = null;
		try
		{
			ge = grid[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
		if( ge != null)
		{
			syncPosition(x,y);
			ge.markForDelete();
			//ge.deleteIfMarked();
			grid[x][y] = null;
			return true;
		}
		return false;
	}

	/**
	 * Move an element from one position to another position.
	 * If either the currentPosition or newPosition are null or out of bounds, the command is ignored
	 * if the currentPosition does not contain an element, the command is ignored.
	 * 
	 * @author Brian Chau
	 * @param currentPosition current position
	 * @param newPosition new position
	 * @return true if move was successful
	 */
	public boolean moveElement(boolean animate, IntVector currentPosition, IntVector newPosition) {
		if(currentPosition == null || newPosition == null) return false;
		int x = currentPosition.getX();
		int y = currentPosition.getY();
		int i = newPosition.getX();
		int j = newPosition.getY();
		
		//if source or destination is out of bounds do nothing
		if(!withinBounds(i,j))
		{
			System.err.println("Attempted to move GridElement outside the bounds of the grid.");
			return false;
		}
		if(!withinBounds(x,y))
		{
			return false;
		}
		
		GridElement[][] grid = getTheGrid();
		

		// checking here just to be sure, but we probably want to check before
		// to avoid creating uneccesary calls
		try{
			if (grid[x][y] == null) {
				return false;
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
			return false;
		}
		// move the geometry
		grid[x][y].moveTo(animate, newPosition);
		// move the logical representation
		try{
			
			if (grid[i][j] != null) {
				grid[i][j].destroy(); // for now, if something exists here, we
										// destroy it? as we overwrite it
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
			return false;
			
		}
		//do the move and overwrite
		grid[i][j] = grid[x][y];
		grid[x][y] = null; // make sure to remove the reference in teh previous
							// cell
		return true;
	}
	
	/**
	 * Check if a given index is within bounds.
	 * An error message is printed to console if out of grid bounds
	 * 
	 * @author Brian Chau
	 * @param pos index to check
	 * @return true if within bounds
	 */
	public boolean withinBounds(IntVector pos)
	{
		if (pos == null) return false;
		return withinBounds(pos.getX(),pos.getY());
	}

	/**
	 * Check if a given index is within bounds.
	 * An error message is printed to console if out of grid bounds
	 * 
	 * @author Brian Chau
	 * @param x x index component
	 * @param y y index component
	 * @return true if within bounds
	 */ 
	public boolean withinBounds(int x, int y) {
		boolean result = (x >= 0 && x < mGM.getWidth() && y >= 0 && y < mGM.getHeight());
		return result;
	}

	/**
	 * marks GridElements for deletion based on a list of indexes
	 * 
	 * @author Brian Chau
	 * @param indexes list of indexes to mark for delete
	 */
	public void markForDelete(ArrayList<IntVector> indexes) {
		GridElement[][] grid = getTheGrid();
		// do nothing if indexes are null or empty
		if (indexes == null || indexes.size() == 0) {
			return;
		}
		for (int i = 0; i < indexes.size(); i++) {
			markForDelete(indexes.get(i));
//			int x = indexes.get(i).getX();
//			int y = indexes.get(i).getY();
//			try{
//				if (withinBounds(x, y) && grid[x][y] != null) {
//					grid[x][y].markForDelete();
//				}
//			}
//			catch(ArrayIndexOutOfBoundsException e)
//			{
//				ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
//			}
		}
	}

	/**
	 * Marks the element in a given index for deletion at the end of the update loop.
	 * This is used because sometimes we need to maintain the reference of the element in the grid
	 * so we cannot immediately delete it. 
	 * @param index
	 */
	public void markForDelete(IntVector index) {
		GridElement[][] grid = getTheGrid();
		int x = index.getX();
		int y = index.getY();
		try{
			if (withinBounds(x, y) && grid[x][y] != null) {
				grid[x][y].markForDelete();
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
	}

	/**
	 * Deletes all GridElements that are marked for deletion
	 * 
	 * @author Brian Chau
	 */
	public void deleteAllMarked() {
		GridElement[][] grid = getTheGrid();
		try{
		for (int x = 0; x < mGM.getWidth(); x++) {
			for (int y = 0; y < mGM.getHeight(); y++) {
				if (grid[x][y] != null) {
					// if something exists, we check if its marked and delete if
					// needed
					if (grid[x][y].isMarkedForDelete()) {
						// if it was deleted, null the index
						grid[x][y] = null;
					}
				}
			}
		}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
	}

	/**
	 * returns a random index where the element is not null or marked for deletion
	 * 
	 * @author Brian Chau
	 * @return an IntVector with the grid location of a random element or null if the grid is empty
	 */
	public IntVector getRandomElementIndex() 
	{
		if(isEmpty())
		{
			//return null immediately if empty
			return null;
		}
		boolean empty = true;
		Random rand = new Random();
		int randomCounter = rand.nextInt(mGM.getWidth() * mGM.getHeight());
		// gets a random value  that could  possibly pick any index if the array were full
		while (randomCounter >= 0) {
			try{
				for (int x = 0; x < mGM.getWidth(); x++) {
					for (int y = 0; y < mGM.getHeight(); y++) {
						GridElement current = this.getElement(x, y);
						if (current != null && !current.isMarkedForDelete()) {
							empty = false;
							randomCounter--;
							if (randomCounter <= 0) {
								return new IntVector(x, y);
							}
						}
						if (empty && x == mGM.getWidth() - 1 && y == mGM.getHeight() - 1) {
							// if we get here it means we reached the end of the
							// grid without finding any elements
							return null;
						}
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
			}
		}
		return null;
	}
	
	/**
	 * returns true if its empty
	 * @author Brian Chau
	 * @return true if empty
	 */
	public boolean isEmpty()
	{
		try{
			for (int x = 0; x < mGM.getWidth(); x++) {
				for (int y = 0; y < mGM.getHeight(); y++) {
					if(getElement(x, y) != null){
						return false;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
		return true;
	}	
	/**
	 * returns number of elements in grid
	 * @author Brian Chau
	 * @return number of elements
	 */
	public int count()
	{
		int count = 0;
		try{
			for (int x = 0; x < mGM.getWidth(); x++) {
				for (int y = 0; y < mGM.getHeight(); y++) {
					if(getElement(x,y) != null){
						count++;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
		return count;
	}
	
	/**
	 * Gets the right-most column that is not empty
	 * @return column index of the right-most column that is not empty
	 */
	public int getFurthestColumn() 
	{
		int maxColumn = mGM.getWidth();
		try
		{
			for (int i = mGM.getWidth()-1; i >= 0; i--)
			{
				for(int j = 0; j < mGM.getHeight(); j++)
				{
					if (getElement(i,j) != null)
					{
						maxColumn = i;
						break;
					}
				}
				if (maxColumn != mGM.getWidth())
				{
					break;
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
		return maxColumn;
	}
	
	
	private GridElement[][] getGrid() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Gets the nearest non-null index from a given starting 2D array index.
	 * This excludes the original starting index
	 * @param pos index to start search
	 * @return nearest non-null index in the grid (excluding original position), or null if pos is null or out of bounds
	 */
	public IntVector getNearestElement(IntVector pos)
	{
		if(pos == null) return null;
		if (!withinBounds(pos.getX(), pos.getY())) return null;
		IntVector nearest = null;
		int x0 = pos.getX();
		int y0 = pos.getY();
		boolean found = false; // will be set to true when 
		int dist = 1; //square distance (diagonals are considered 1)
		int shortestlength = 999999;
		boolean allOutOfBounds = false;
		while(!allOutOfBounds && !found)
		{
			allOutOfBounds = true; // if this remains true to the end, our search has reached the edge of the map
			//make outer bound to check
			int xmin = x0-dist;
			int xmax = x0+dist;
			int ymin = y0-dist;
			int ymax = y0+dist;
			
			for (int x = xmin; x <= xmax; x++)
			{
				for (int y = ymin; y <= ymax; y++)
				{
					//only check the border, the insides were checked previously
					if((x==xmin || x==xmax || y==ymin || y==ymax) && withinBounds(x,y))
					{
						try{
							allOutOfBounds=false;
							found = found || false; //this check was within bounds, we are not done
							
							//if we found something return the coordinates
							GridElement elem = getElement(x,y);
							if(elem != null)
							{
								//if this is closer than the nearest
								int length = Math.abs(x0-x)+ Math.abs(y0-y);
								if(length < shortestlength)
								{
									found = true;
									dist = length;
									nearest = new IntVector(x,y);
								}
							}
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
						}
					} //if not border or within bounds, skip
				}
			} // we scanned all the border cells, and found nothing, increase search radius
			dist++;
		} //if we get to here, all cells we checked were out of bounds. return null
		return nearest;
	}
	
	/**
	 * removes all elements from the grid.
	 */
	public void clear(){
		GridElement[][] grid = getTheGrid();
		try{
			for(int i=0;i < this.mGM.getWidth();i++){
				for(int j=0;j<this.mGM.getHeight();j++){
					if(grid[i][j] != null){
						grid[i][j].destroy();
						grid[i][j] = null;
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
	}
}
