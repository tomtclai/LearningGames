package TowerDefense;

import java.util.LinkedList;

/**
 * A whole list of Walkers, and some interfaces to easily interact with them.
 * 
 * Not consistent with SetOfTowers, which should be addressed.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class SetOfWalkers
{
	// the actual storage container
	private LinkedList<Walker> walkers = new LinkedList<Walker>();
	
	
	/**
	 * Constructor
	 */
	public SetOfWalkers()
	{

	}
	
	
	/**
	 * Updates the list and all the Walkers within.
	 */
	public void update()
	{
		for(Walker walker : this.getArrayOfWalkers())
		{
			walker.update();	
		}
	}
	
	
	

	
	/**
	 * Adds a specific Walker to the set.
	 * @param walker	The Walker to add.
	 * @return			True if successfully added.
	 */
	public boolean addWalker(Walker walker)
	{
		return walkers.add(walker);
	}
	
	
	
	/**
	 * Removes a specific Walker from the set.
	 * @param walker	The Walker to remove.
	 * @return			True if successfully removed.
	 */
	public boolean removeWalker(Walker walker)
	{
		return walkers.remove(walker);
	}
	
	

	
	/**
	 * Returns an array of the Walkers in the current set.
	 * Intended to be used to iterate through the list of Walkers
	 * for the purpose of simple checks and sets.
	 * Note that this array will not be updated after this call,
	 * so keeping it around is not a good idea.
	 * @return		The current set of Walkers as an array.
	 */
	public Walker[] getArrayOfWalkers()
	{
		return walkers.toArray(new Walker[0]);
	}
	
	
	/**
	 * @return		true if the set contains no Walkers
	 */
	public boolean isEmpty()
	{
		return walkers.isEmpty();
	}
	
	
	/**
	 * Empty the list of Walkers and destroy them all.
	 */
	public void removeAll()
	{
		for(Walker walker : getArrayOfWalkers())
		{
			walker.destroy();
		}
		
		walkers.clear();
	}
}
