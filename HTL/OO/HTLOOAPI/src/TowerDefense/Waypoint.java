package TowerDefense;
import Engine.GameObject;
import Engine.Vector2;

import java.lang.Math;

/**
 * Waypoints are locations on the grid, and can be used to create a path.
 * 
 * These Waypoints are used to create a path for the Walkers to follow.
 * The path should lead them to the Queen.
 * It is possible to make branching paths.
 * 
 * NOTE:  	I don't believe the current version of the HTL game uses Waypoints,
 * 			but they're here if you want them.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Waypoint
{
	private GameObject obj = null;				// this contains the location of the Waypoint
	private Waypoint[] nextWaypoints;			// array of Waypoints to choose from when selecting next Waypoint
	
	// how close does a Walker need to be to a Waypoint to be "close enough"?
	private static float collisionThreshold = .1f;
	
	private static boolean debugDraw = false;	// debug tool that displays where the Waypoints are for fine-tuning
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////
	
	/**
	 * Constructor with 1 next Waypoint option.
	 * @param xLoc		X-position of the Waypoint.
	 * @param yLoc		Y-position of the Waypoint.
	 * @param next		The next Waypoint folks on this path should go to.
	 */
	public Waypoint(float xLoc, float yLoc, Waypoint next)
	{
		this(xLoc, yLoc, 1, next, null, null, null, null);
	}	
	/**
	 * Constructor with 2 next Waypoint options.
	 * @param xLoc		X-position of the Waypoint.
	 * @param yLoc		Y-position of the Waypoint.
	 * @param next0		One possible candidate for the next Waypoint.
	 * @param next1		One possible candidate for the next Waypoint.
	 */
	public Waypoint(float xLoc, float yLoc, Waypoint next0, Waypoint next1)
	{
		this(xLoc, yLoc, 2, next0, next1, null, null, null);
	}	
	/**
	 * Constructor with 3 next Waypoint options.
	 * @param xLoc		X-position of the Waypoint.
	 * @param yLoc		Y-position of the Waypoint.
	 * @param next0		One possible candidate for the next Waypoint.
	 * @param next1		One possible candidate for the next Waypoint.
	 * @param next2		One possible candidate for the next Waypoint.
	 */
	public Waypoint(float xLoc, float yLoc, Waypoint next0, Waypoint next1, Waypoint next2)
	{
		this(xLoc, yLoc, 3, next0, next1, next2, null, null);
	}
	/**
	 * Constructor with 4 next Waypoint options.
	 * @param xLoc		X-position of the Waypoint.
	 * @param yLoc		Y-position of the Waypoint.
	 * @param next0		One possible candidate for the next Waypoint.
	 * @param next1		One possible candidate for the next Waypoint.
	 * @param next2		One possible candidate for the next Waypoint.
	 * @param next3		One possible candidate for the next Waypoint.
	 */
	public Waypoint(float xLoc, float yLoc, Waypoint next0, Waypoint next1, Waypoint next2, Waypoint next3)
	{
		this(xLoc, yLoc, 4, next0, next1, next2, next3, null);
	}
	/**
	 * Constructor with 5 next Waypoint options.
	 * @param xLoc		X-position of the Waypoint.
	 * @param yLoc		Y-position of the Waypoint.
	 * @param next0		One possible candidate for the next Waypoint.
	 * @param next1		One possible candidate for the next Waypoint.
	 * @param next2		One possible candidate for the next Waypoint.
	 * @param next3		One possible candidate for the next Waypoint.
	 * @param next4		One possible candidate for the next Waypoint.
	 */
	public Waypoint(float xLoc, float yLoc, Waypoint next0, Waypoint next1, Waypoint next2, Waypoint next3, Waypoint next4)
	{		
		this(xLoc, yLoc, 5, next0, next1, next2, next3, next4);
	}
	
	
	/**
	 * Private constructor that all public constructors feed into.
	 * @param xLoc			X-position of the Waypoint.
	 * @param yLoc			Y-position of the Waypoint.
	 * @param numWaypoints	Actual number of next Waypoint params that are used.
	 * @param next0			One possible candidate for the next Waypoint.
	 * @param next1			One possible candidate for the next Waypoint.
	 * @param next2			One possible candidate for the next Waypoint.
	 * @param next3			One possible candidate for the next Waypoint.
	 * @param next4			One possible candidate for the next Waypoint.
	 */
	private Waypoint(float xLoc, float yLoc, int numWaypoints,
					Waypoint next0, Waypoint next1, Waypoint next2, Waypoint next3, Waypoint next4)
	{
		this.initializeNextWaypoints(numWaypoints, next0, next1, next2, next3, next4);
		
		obj = new GameObject(xLoc, yLoc, 0, 0);		// create object containing location
		
		// depending on debug settings, either draw the object or make it invisible
		if(!Waypoint.debugDraw)
		{
			obj.setToInvisible();
		}
		else
		{
			obj.setSize(1, 1);
			obj.draw();
		}
	}
	
	
	
	///////////////////////////////
	//                           //
	//       Update Towers      //
	//                           //
	///////////////////////////////
	
	
	/**
	 * From the list of possible waypoints to send units to after this one,
	 * selects one at random and returns it.
	 * @return the next waypoint the unit should head towards, or null if this is the last one
	 */
	public Waypoint getNextWaypoint()
	{
		if(nextWaypoints == null || nextWaypoints.length < 1)
		{
			return null;
		}
		
		double random = Math.random() - .0000000000001;	// random number between 0 and 0.99999999999999999
		
		int index = nextWaypoints.length;
		index = (int)(index * random);	// choose a random int between 0 and numWaypoints
		
		return nextWaypoints[index];
	}
	
	
	/**
	 * Is the object center basically overlapping with the waypoint center?
	 * @param object	The other GameObject.
	 * @return			True if they have collided.
	 */
	public boolean collidedWithWaypoint(GameObject object)
	{
		float xDif = object.getCenterX() - this.getCenterX();
		float yDif = object.getCenterY() - this.getCenterY();
		xDif = Math.abs(xDif);
		yDif = Math.abs(yDif);		
		return xDif < collisionThreshold && yDif < collisionThreshold;
	}
	
	///////////////////////////////
	//                           //
	//         Getters           //
	//                           //
	///////////////////////////////
	
	/**
	 * @return 	X-location of this Waypoint.
	 */
	public float getCenterX()
	{
		return obj.getCenterX();
	}
	
	/**
	 * @return 	Y-location of this Waypoint.
	 */
	public float getCenterY()
	{
		return obj.getCenterY();
	}
	
	/**
	 * @return 	XY-location of this Waypoint.
	 */
	public Vector2 getCenter()
	{
		return obj.getCenter();
	}
	

	///////////////////////////////
	//                           //
	//         Initialize        //
	//                           //
	///////////////////////////////
	
	
	
	/**
	 * Sets up the array of next Waypoints.
	 * @param numWaypoints	Actual number of next Waypoint params that are used.
	 * @param next0			One possible candidate for the next Waypoint.
	 * @param next1			One possible candidate for the next Waypoint.
	 * @param next2			One possible candidate for the next Waypoint.
	 * @param next3			One possible candidate for the next Waypoint.
	 * @param next4			One possible candidate for the next Waypoint.
	 */
	private void initializeNextWaypoints(int numWaypoints,
					Waypoint next0, Waypoint next1, Waypoint next2, Waypoint next3, Waypoint next4)
	{
		this.nextWaypoints = new Waypoint[numWaypoints];
		
		nextWaypoints[0] = next0;
		if(numWaypoints >= 2)
		{
			nextWaypoints[1] = next1;
		}
		if(numWaypoints >= 3)
		{
			nextWaypoints[2] = next2;
		}
		if(numWaypoints >= 4)
		{
			nextWaypoints[3] = next3;
		}
		if(numWaypoints >= 5)
		{
			nextWaypoints[4] = next4;
		}
	}
}
