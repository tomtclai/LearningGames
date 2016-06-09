package TowerDefense;

import java.util.LinkedList;

/**
 * This creates an interface for user interactions with a list of Towers.
 * 
 * Not consistent with SetOfWalkers, which should be addressed.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class SetOfTowers
{

	private LinkedList<Tower> towers = new LinkedList<Tower>();
	
	/**
	 * Constructor
	 */
	public SetOfTowers()
	{

	}
	
	
	
	/**
	 * Updates the list and all the Towers within.
	 */
	public void update()
	{
		for(Tower theTower : this.getArrayOfTowers())
		{
			theTower.update();
		}
	}
	
	
	/**
	 * Creates a new tower (a Medic) at the corresponding tile
	 * and stores it in this set.
	 * @param tile		The tile at which to create the Tower
	 * @return			True if the tower was created
	 */
	public boolean addTowerAt(Tile tile)
	{
		return addTowerAt(tile, true);
	}
	
	
	/**
	 * Creates a new tower at the corresponding tile
	 * and stores it in this set.
	 * @param tile				The tile at which to create the Tower
	 * @param towerIsMedic		True if the Tower should be a Medic, false if it should be Speedy.
	 * @return					True if the tower was created.
	 */
	public boolean addTowerAt(Tile tile, boolean towerIsMedic)
	{		
		if(tile == null)  return false;
		if(tile.hasOccupant()) 	return false;

		Tower def;
		if(!towerIsMedic)
		{
			def = new TowerSpeedy();
		}
		else
		{
			def = new TowerMedic();
		}
		
		def.setTile(tile);
		towers.add(def);
		
		return true;
	}
	
	

	
	
	/**
	 * Returns an array of the Towers in the current set.
	 * Intended to be used to iterate through the list of Towers
	 * for the purpose of simple checks and sets.
	 * Note that this array will not be updated after this call,
	 * so keeping it around is not a good idea.
	 * @return		The current set of Towers as an array.
	 */
	public Tower[] getArrayOfTowers()
	{
		return towers.toArray(new Tower[0]);
	}
	
	
	/**
	 * @return		true if the set contains no Towers
	 */
	public boolean isEmpty()
	{
		return towers.isEmpty();
	}
	
	
	/**
	 * Empty the list of Towers and destroy them all.
	 */
	public void removeAll()
	{
		for(Tower tower : getArrayOfTowers())
		{
			tower.destroy();
		}
		
		towers.clear();
	}
}
