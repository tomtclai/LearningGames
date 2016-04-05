package layers;

import gridElements.GridElement;
import gridElements.Tile;

import java.util.LinkedList;

import structures.IntVector;
import corrupted.Game;

public class ExteriorLayer{// extends Primitive{
	
	private LinkedList<GridElement[]> lines;	//list of rows
	private final int offset;		//if this collection represents above the map this is getWidth(). if below, -1.
	private final int scalar;		//if this collection represents above the map, this is 1. if below, -1.
	public final boolean above;		//if this collection represents above the map, this is true. if below, false.
	private Game mGM;
	
	/**
	 * Instantiates a list of columns above or below the main grid area.
	 * These are not drawn but can be store elements that move offscreen to be retrieved later
	 * @param gm reference to Game
	 * @param _above if true, this Exterior layer represents objects on the far side (right)
	 */
	public ExteriorLayer(Game gm, boolean _above)
	{
		lines = new LinkedList<GridElement[]>();
		mGM = gm;
		above = _above;
		if (above)
		{
			offset = mGM.getWidth();
			scalar = 1;
		}
		else
		{
			offset = -1;
			scalar = -1;
		}
	}
	
	/**
	 * pushes a row of GridElements onto the ExternalLayer
	 * @param row row to push
	 */
	public void pushRow(GridElement[] row)
	{
		lines.push(row);
		//updatePositions();
	}
	
	/**
	 * pops the last row off the ExternalLayer
	 * @return row that was popped
	 */
	public GridElement[] popRow()
	{
		if(lines.isEmpty())
		{
			return null;
		}
		GridElement[] rtn = lines.pop();
		//updatePositions();
		return rtn;
	}
	
	/**
	 * gets the number of 1D GridElement arrays are stored in this ExternalLayer
	 * @return number of arrays
	 */
	public int rowCount()
	{
		return lines.size();
	}
	
	/**
	 * gets the count of the total number of GridElements in this ExternalLayer
	 * @return number of GridElements
	 */
	public int count()
	{
		int counter = 0;
		for(int i = 0; i < lines.size(); i++)
		{
			GridElement[] row = lines.get(i);
			for(int j = 0; j < row.length; j++)
			{
				if (row[j] != null)
				{
					counter++;
				}
			}
		}
		return counter;
	}
	
	/**
	 * gets if this ExternalLayer has no rows 
	 * @return true if there are no rows
	 */
	public boolean isEmpty()
	{
		return lines.isEmpty();
	}
	/**
	 * removes all 1D GridElement arrays from this ExternalLayer
	 */
	public void clear()
	{
		lines.clear();	//do I need to "deep" clear?
	}
	
	/**
	 * just a helper for initialization for corrupted this just allows multiple Tile rows to be added 
	 * without extraneous position updates
	 * 
	 * @author Brian Chau
	 * @param numRows number of rows to add
	 */
	public void addTileRows(int numRows)
	{
		for(int i = 0; i < numRows; i++)
		{
			GridElement[] newRow = new GridElement[mGM.getHeight()];
			for(int j = 0; j < mGM.getHeight(); j++)
			{
				newRow[j] = new Tile(new IntVector(mGM.getWidth(), j), GridElement.getRandomColorEnum(), mGM);
			}
			pushRow(newRow);
		}
		//updatePositions();
	}
	
	/**
	 * update geometry position of all GridElements in the ExternalLayer 
	 * to match their extended index locations relative to the main grid system
	 * this was used to draw the minimap that was thrown out
	 */
	public void updatePositions()
	{
		//dont forget that row is y and column is x
		for(int i = 0; i < lines.size(); i++)
		{
			GridElement[] row = lines.get(i);
			for(int j = 0; j < row.length; j++)
			{
				if (row[j] != null)
				{
					row[j].moveTo(false, new IntVector(i*scalar+offset,j));
				}
			}
		}
	}
	
//	@Override
//	public void draw()
//	{
//		return;
//		for(int i = 0; i < lines.size(); i++)
//		{
//			GridElement[] row = lines.get(i);
//			for(int j = 0; j < row.length; j++)
//			{
//				if (row[j] != null)
//				{
//					row[j].draw();
//				}
//			}
//		}
//	}
}
