package TowerDefense;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import Engine.DrawingLayer;

/**
 * The wave system is used to spawn waves of enemies in this game.
 * Rachel hates the word "spawn", but she has been really brave and accepted that it must be used.
 * It is spelled "spawn", but it is pronounced "spoon".
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class WaveSystem
{	
	private CooldownTimer waveTimer = null;		// the timer object that keeps track of wave delay
	
	private CooldownTimer spawnTimer = null;	// the timer object that keeps track of spawn delay
	
	private CooldownTimer timeUntilNextWaveTimer = null;
	
	private boolean active = false;				// whether or not the spawning has begun
	
	private Queue<Wave> waveList = new LinkedList<Wave>();	// list of all the waves for this level
	
	private Wave currentWave = null;			// the wave that we are currently spawning or will spawn next
	
	private int spawnCurrent = 0;				// the number of Walkers that have been spawned this wave	
	
	private int currentWaveNumber = 0; 			// current wave number
	
	private int totalWaves = -1;				// the total number of waves in this waveSystem; updated when spawner starts
	
	// list of paths that Walkers can be set to
	// in the current build, we have only been using a single path
	private LinkedList<Path> pathList = new LinkedList<Path>();
	
	// the DrawingLayer, if any, where newly-spawned objects should reside
	private DrawingLayer spawnDrawingLayer = null;
	
	/**
	 * Constructor.  Initializes from a file.
	 * @param filename		The file with the wave information for this level.
	 */
	public WaveSystem(String filename)
	{
		addWavesFromFile(filename);
	}
	
	
	/**
	 * Constructor.  Initializes as empty. 
	 */
	public WaveSystem()
	{
		
	}
	
	
	/**
	 * Wipes all data wave-related from the WaveSystem, making it good as new.
	 */
	public void clearWaves()
	{		
		active = false;
		spawnCurrent = 0;
		currentWaveNumber = 0;
		totalWaves = -1;
		currentWave = null;
		waveList.clear();
		waveTimer = null;
		spawnTimer = null;
		timeUntilNextWaveTimer = null;
	}
	
	
	
	/**
	 * From now on, all newly spawned Walkers will be put on the specified DrawingLayer.
	 * Enter null to use the default AutoDraw set.
	 * @param drawingLayer		The DrawingLayer.
	 */
	public void setDrawingLayer(DrawingLayer drawingLayer)
	{		
		spawnDrawingLayer = drawingLayer;
	}
	
	
	
	
	/**
	 * Adds a new wave to the end of the spawn list.
	 * @param wave		The pre-made wave.
	 */
	public void addWave(Wave wave)
	{
		waveList.add(wave);
	}
	
	
	
	/**
	 * Adds a new wave to the end of the spawn list.
	 * @param spawnDelay		Seconds between each Walker spawn.
	 * @param nextWaveDelay		Seconds between the last spawn of this wave and the first spawn of the next wave.
	 * @param walkerString		A string of consecutive characters representing the Walkers to spawn this wave. 
	 * 							b is a basic Walker, q is a quick Walker 
	 */
	public void addWave(float spawnDelay, float nextWaveDelay, String walkerString)
	{
		Wave wave = new Wave(spawnDelay, nextWaveDelay, walkerString);
		addWave(wave);
	}
	
	
	
	/**
	 * Adds waves to this Spawner from a file.
	 * Format:
	 * 		1st line:	Seconds between spawns this wave as float.
	 * 		2nd line:	Seconds until next wave after this wave's last spawn as float.
	 * 		3rd line:	String of consecutive characters representing each Walker in this wave.
	 * 					b is a Basic Walker, q is a Quick Walker; ie - bbqqqbqbq
	 * 		4th line:	empty
	 * 
	 * These 4 lines are a single wave, repeat for more waves.
	 * 
	 * @param filename		The file with the wave information the waves.
	 */
	public void addWavesFromFile(String filename)
	{
		File file = new File(filename);
		Scanner scan = null;
		
		// open the file (please be a real file, please be a real file)
		try
		{
			scan = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		// each loop reads and stores a wave
		while(scan.hasNextFloat())
		{
			Wave wave = new Wave(scan);
			// put the wave in the list
			waveList.add(wave);
		}
		scan.close();
	}

	
	/**
	 * Adds a Path to the list of Paths Walkers can travel in this level.
	 * @param path		The Path to add.
	 */
	public void addPath(Path path)
	{
		pathList.add(path);
	}
	

	/**
	 * Removes any existing paths, and sets this as the ONLY path Walkers can travel.
	 * @param path		The Path to use.
	 */
	public void setPath(Path path)
	{
		pathList.clear();
		pathList.add(path);
	}
	
	
	/**
	 * @return	True if active.
	 */
	public boolean hasStarted()
	{
		return this.active;
	}
	
	/**
	 * @return	True if this wave system has spawned all of its stuff and will do no more.
	 */
	public boolean isDone()
	{
		return this.active && waveList.isEmpty();
	}
	
	
	/**
	 * Calling this function will cause the wave system to become active, which will
	 * inevitably result in the spawning of Walkers.
	 * Note that subsequent calls of this function will have no effect.
	 */
	public void startWaves()
	{
		if(!this.active)
		{
			this.spawnTimer = new CooldownTimer(0);
			this.waveTimer = new CooldownTimer(0);
			this.timeUntilNextWaveTimer = new CooldownTimer(0);
			this.active = true;	
			this.totalWaves = waveList.size();
			this.currentWave = waveList.peek();
		}	
	}
	
	
	/**
	 * Updates the wave system's progress, perhaps even by spawning an Walker!
	 * @param walkerList		A list of all of the Walkers currently in the level.
	 */
	public void update()
	{
		// if the wave spawner is ON and not all waves have been spawned
		if(this.active && !waveList.isEmpty())
		{
			// if the cooldown between waves has elapsed
			if(waveTimer.isReady() && currentWave != null)
			{		
				// the first instant that the waveTimer is finished is when
				// the total time until the next wave needs to start counting down
				if(timeUntilNextWaveTimer.isReady())
				{
					this.currentWaveNumber++;
					timeUntilNextWaveTimer.start(currentWave.getTotalDuration());					
				}
				
				// if we haven't spawned all the piggies in this wave
				if(currentWave.getNumberOfWalkers() > 0)
				{
					// if the appropriate amount of time has passed between spawns
					if(spawnTimer.isReady())
					{
						this.doSpawnProcess();
					}
				}
			}
		}
	}
	

	/**
	 * Spawns a new Walker and updates the wave system accordingly.
	 * @param walkerList		A list of all of the Walkers currently in the level.
	 */
	private void doSpawnProcess()
	{
		// spawn sometin
		this.spawn();
		
		// record another spawned enemy
		spawnCurrent++;
		
		// if that wasn't the last Walker
		if(currentWave.getNumberOfWalkers() > 0)
		{
			// restart the spawn timer
			float delay = currentWave.getSpawnDelay();
			spawnTimer.start(delay);
		}
		// otherwise, we've spawned all our piggies in this wave
		else
		{
			prepareNextWave();
		}
	}
	
	

	
	
	/**
	 * Gets the next wave ready after spawning the last piggy in a wave
	 * Prerequisite: currentWave != null
	 */
	private void prepareNextWave()
	{
		// start the waveDelay timer
		float delay = currentWave.getNextWaveDelay();
		waveTimer.start(delay);
		
		// remove the wave from the list
		waveList.remove();
		
		// update current wave
		currentWave = waveList.peek();
		
		// reset spawns to 0
		this.spawnCurrent = 0;
	}
	
	
	/**
	 * Actually spawn the enemy along the next appropriate path.
	 * @param walkerList		A list of all of the Walkers currently in the level.
	 */
	private void spawn()
	{
		// get the path index and set the stage for the next path to be different
		int currentPathIndex = 0; //currentWave.getPathIndex();
		//currentWave.cyclePathList();
		
		// get the actual path using the index
		Path currentPath = pathList.get(currentPathIndex);
		
		// create the new Walker with that path
		Walker walker;
		
		switch(currentWave.dequeueWalker())
		{
			case QUICK:		walker = new WalkerQuick(currentPath);
							break;
						
			default:		walker = new WalkerBasic(currentPath);
		}
		
		// put Walker on specific DrawingLayer if necessary
		if(spawnDrawingLayer != null)
		{
			walker.moveToDrawingLayer(spawnDrawingLayer);
		}
	}
	
	/**
	 * @return	The number of walkers that have been spawned so far during this wave
	 */
	public int getWalkersThisWaveSpawned()
	{
		return spawnCurrent;
	}
	
	
	/**
	 * @return	The number of walkers that have yet to spawn during this wave
	 */
	public int getWalkersThisWaveRemaining()
	{
		if(currentWave == null) return 0;
		
		return currentWave.getNumberOfWalkers();
	}
	
	
	/**
	 * @return	The number of the current wave
	 */
	public int getCurrentWaveNumber()
	{
		return currentWaveNumber;
	}
	
	
	/**
	 * @return	The total number of waves this spawner has been set for
	 */
	public int getTotalWaves()
	{
		return totalWaves;
	}
	
	
	/**
	 * @return	The number of seconds until the next wave begins.  -1 means the spawner is either inactive or has no more waves.
	 */
	public long getSecondsUntilNextWave()
	{
		// if there isn't a next wave, or if the spawner isn't active, fail
		if(waveList.size() < 1 || !this.active)
		{
			return -1;
		}
		else
		{
			return timeUntilNextWaveTimer.getSecondsRemaining();
		}
	}
}
