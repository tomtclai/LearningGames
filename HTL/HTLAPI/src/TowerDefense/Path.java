package TowerDefense;

/**
 * Paths are a spawn location followed by a series of Waypoints
 * that ultimately lead to a final destination.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Path
{
	private float spawnLocationX;		// location where Walkers should be spawned
	private float spawnLocationY;		// location where Walkers should be spawned
	
	private Waypoint nextWaypoint;		// first waypoint in the path
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////
	
	
	/**
	 * Constructor.
	 * @param xLoc				X-position of the Waypoint.
	 * @param yLoc				Y-position of the Waypoint.
	 * @param nextWaypoint		The next Waypoint folks on this path should go to.
	 */
	public Path(float xLoc, float yLoc, Waypoint nextWaypoint)
	{
		this.spawnLocationX = xLoc;
		this.spawnLocationY = yLoc;
		this.nextWaypoint = nextWaypoint;
	}


	/**
	 * @return			The X-coordinate of the spawn location.
	 */
	public float getSpawnLocationX()
	{
		return spawnLocationX;
	}


	/**
	 * @return			The Y-coordinate of the spawn location.
	 */
	public float getSpawnLocationY()
	{
		return spawnLocationY;
	}


	/**
	 * @return			The waypoint that a Walker on this path should start moving towards.
	 */
	public final Waypoint getNextWaypoint()
	{
		return nextWaypoint;
	}	
}
