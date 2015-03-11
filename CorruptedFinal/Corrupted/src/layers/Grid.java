package layers;
import gridElements.GridElement;
import Engine.GameObject;
import corrupted.Game;
import corrupted.ErrorHandler;


/**
 * this class holds a 2D GridElement array for the Game and is responsible for drawing it.
 * @author Brian Chau
 *
 */
public class Grid  {
	
	private Game mGM;
	private GridElement[][] grid;
	
	/**
	 * instatiates a new Grid object
	 * @param lm reference to Game maintained to query for width and height
	 * @param g 2D array of GridElements to store into the Grid object
	 */
	public Grid(Game lm, GridElement[][] g)
	{
		mGM = lm;
		grid = g;
	}
	
	/**
	 * gets a 1D GridElement array containing a single column from the grid based on a horizontal column
	 * @param position horizontal position for the column to get
	 * @return 1D array containing the column elements from the grid
	 */
	public GridElement[] getColumn(int position)
	{
		int width = mGM.getWidth();
		if (position >= width)
		{
			System.out.println("1D array horizontal position out of bounds. Please use a value less than " + width);
			return null;
		}
		if  (position < 0)
		{
			System.out.println("1D array horizontal position out of bounds. Please use a non-negative value");
			return null;
		}
		return grid[position];
	}
	
	/**
	 * sets a column of the Grid using a user provided 1D array
	 * if the position is out of bounds of the gridwidth or 
	 * if the column length does not match the gridheight,
	 * the operation is cancelled
	 * @param column 1D array containing the GridElement column to be put into the grid
	 * @param position horizontal position to put the column
	 * @return true if operation was successful
	 */
	public boolean setColumn(GridElement[] column, int position)
	{
		if (column == null)
		{
			System.err.println("WARNING: setColumn was called using a null array.");
			return false;
		}
		int width = mGM.getWidth();
		int height = mGM.getHeight();
		if (grid == null)
		{
			grid = new GridElement[width][height];
		}
		//ignore if we did not 
		if(position < 0)
		{
			System.err.println("WARNING: 1D array horizontal position out of bounds. setColumn command ignored. Please use a non-negative value");
			return false;
		}
		if(position >= mGM.getWidth())
		{
			System.err.println("WARNING: 1D array horizontal position out of bounds. setColumn command ignored. Please use a value less than " + width);
			return false;
		}
		if(column.length != mGM.getHeight())
		{
			System.err.println("WARNING: 1D array is of wrong length. setColumn command ignored. Height of grid already set to "+ height);
			return false;
		}
		grid[position] = column;
		return true;
	}
	
	/**
	 * gets the whole grid as a 2D array
	 * @return 2D array containing the grid
	 */
	public GridElement[][] getGrid()
	{
		return grid;
	}

	/**
	 * draws all the GridElements in the grid
	 */
	// @Override
	public void draw()
	{
		try{
			for(int x = 0; x < mGM.getWidth(); x++)
			{
				for (int y = 0; y < mGM.getHeight(); y++)
				{
					if(grid[x][y] != null)
					{
						grid[x][y].draw();
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
