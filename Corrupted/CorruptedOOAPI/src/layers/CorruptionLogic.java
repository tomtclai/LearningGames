package layers;

import structures.IntVector;
import corrupted.Game;
import corrupted.Game.Sounds;
import gridElements.*;

import java.util.ArrayList;

public class CorruptionLogic extends LogicBase {
	public CorruptionLogic(Game layM)
	{
		super(layM);
	}
	
	/**
	 * gets the CorruptedGrid from the Game
	 * @return the grid that this Logic class executes on.
	 */
	@Override
	protected GridElement[][] getTheGrid()
	{
		return mGM.getCorruptedGrid();
	}
	
	/**
	 * Add Corrupted tiles on the indexed positions of the corruption layer
	 * 
	 * @author Samuel Kim, Brian Chau
	 * @param indices list of IntVectors for locations to put corruption
	 * @return An ArrayList containing a list of booleans representing success states of each put, null if pos is null
	 */
	public ArrayList<Boolean> addCorruptions(ArrayList<IntVector> indices)
	{
		if (indices == null) return null;
		ArrayList<Boolean> rtn = new ArrayList<Boolean>();
		//do nothing if indexes are null or empty
		if (indices.size() == 0)
		{
			return rtn;
		}
		for(IntVector IV : indices)
		{
			rtn.add(addCorruption(IV));
		}
		return rtn;
	}
	
	/**
	 * Add Corrupted tile on the indexed position of the corruption layer
	 * 
	 * @author Samuel Kim, Brian Chau
	 * @param index location to put the corruption
	 * @return true if successful, false otherwise
	 */
	public boolean addCorruption(IntVector index)
	{
		if (index == null) return false;
		if(!withinBounds(index.getX(), index.getY()))
		{
			return false;
		}
		mGM.triggerSound(Sounds.corruption);
		Corruption corr = new Corruption(index, mGM);
		return putElement(index,corr);
	}
}
