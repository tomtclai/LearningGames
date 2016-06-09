package TowerDefense;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * A Wave is a series of Walkers that appears at a set rate during a level.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Wave
{	
	// list of which walker to spawn at this point
	private LinkedList<Walker.Type> walkerList = new LinkedList<Walker.Type>();
	private float spawnDelay = 1;			// number of seconds between Walker spawns this wave			
	private float nextWaveDelay = 1;		// number of seconds to wait after this wave before the next wave
	
	/**
	 * Constructor.  Initializes from a file being read by a scanner.
	 * @param scan		The Scanner object that is ready to read a Wave object from something (like a file).
	 */
	public Wave(Scanner scan)
	{		
		this.readWaveFromFile(scan);
	}
	
	
	
	/**
	 * Constructor.
	 * @param spawnDelay		Seconds between each Walker spawn.
	 * @param nextWaveDelay		Seconds between the last spawn of this wave and the first spawn of the next wave.
	 * @param walkerString		A string of consecutive characters representing the Walkers to spawn this wave. 
	 * 							b is a basic Walker, q is a quick Walker 
	 */
	public Wave(float spawnDelay, float nextWaveDelay, String walkerString)
	{
		this.spawnDelay = spawnDelay;
		this.nextWaveDelay = nextWaveDelay;
		
		constructWalkerList(walkerString);
	}
	
	
	
	/**
	 * Initializes this object from a Scanner object.
	 * @param scan		The Scanner object that is ready to read a Wave object from something (like a file).
	 */
	private void readWaveFromFile(Scanner scan)
	{		
		// read in spawn delay
		this.spawnDelay = scanNextFloat(scan);
		
		// read in wave delay
		this.nextWaveDelay = scanNextFloat(scan);
		
		// read in the walker string
		String walkerString = scan.nextLine();
		walkerString.toLowerCase();
		
		// parse string and convert to list
		constructWalkerList(walkerString);
	}
	
	
	
	/**
	 * Turns a string into Walkers to be spawned by the spawner.
	 * @param walkerString		A string of consecutive characters representing the Walkers to spawn this wave. 
	 * 							b is a basic Walker, q is a quick Walker 		
	 */
	private void constructWalkerList(String walkerString)
	{
		while(!walkerString.isEmpty())
		{
			// get the first character
			char currentChar = walkerString.charAt(0);
			
			// cut off the first character from the rest of the string
			walkerString = walkerString.substring(1);
			
			switch(currentChar)
			{
				case 'q':
				case 'Q':	walkerList.addLast(Walker.Type.QUICK);
							break;
				default:	walkerList.addLast(Walker.Type.BASIC);
			}
		}
	}
	
	
	
	/**
	 * Reads the next float and moves to the next line.  Tower for Scanner-reading.
	 * @param scan		The Scanner object that is ready to read a Wave object from something (like a file).
	 * @return			The float that was read.
	 */
	private float scanNextFloat(Scanner scan)
	{
		float val = scan.nextFloat();
		scan.nextLine();
		return val;
	}
	
	/**
	 * Reads the next int and moves to the next line.  Tower for Scanner-reading.
	 * @param scan		The Scanner object that is ready to read a Wave object from something (like a file).
	 * @return			The int that was read.
	 */
	private int scanNextInt(Scanner scan)
	{
		int val = scan.nextInt();
		scan.nextLine();
		return val;
	}
	
	
	
	/**
	 * @return		The number of walkers in this wave.
	 */
	public int getNumberOfWalkers()
	{
		return walkerList.size();
	}


	/**
	 * Removes the next type of walker from the list and returns it.
	 * @return		The next type of Walker to spawn.
	 */
	public Walker.Type dequeueWalker()
	{
		if(walkerList.isEmpty()) return null;
		
		return walkerList.removeFirst();
	}
	
	
	
	
	/**
	 * @return			The number of seconds between spawns.
	 */
	public float getSpawnDelay()
	{
		return spawnDelay;
	}

	
	/**
	 * @param spawnDelay		The number of seconds between spawns.
	 */
	public void setSpawnDelay(float spawnDelay)
	{
		this.spawnDelay = spawnDelay;
	}

	
	/**
	 * @return			The number of seconds between waves.
	 */
	public float getNextWaveDelay()
	{
		return nextWaveDelay;
	}


	/**
	 * @param waveDelay		The number of seconds between waves.
	 */
	public void setNextWaveDelay(float waveDelay)
	{
		this.nextWaveDelay = waveDelay;
	}
	
	
	/**
	 * Returns the total amount of time from the moment this wave begins until
	 * the next wave (if any) should begin.
	 */
	public float getTotalDuration()
	{
		float duration = (walkerList.size() - 1) * spawnDelay;
		duration += nextWaveDelay;
		
		return duration;
	}
}
