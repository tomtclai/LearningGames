package gridElements;

import java.util.ArrayList;

import corrupted.Game;
import structures.IntVector;
import gridElements.GridElement.ColorEnum;

public abstract class Virus extends GridElement {

	public boolean newlySpreaded=true;
	
	/**
	 * instantiates a Virus with a given color
	 * @param col color of the virus
	 * @param gm reference to Game
	 */
	public Virus(ColorEnum col, Game gm){
		super(null, col, gm);
	}
		
	/**
	 * Instantiates a Virus with a given color and position
	 * @param pos position to spawn
	 * @param col color of the virus
	 * @param gm reference to Game
	 */
	public Virus(IntVector pos, ColorEnum col, Game gm){
		super(pos, col, gm);
	
	}
	
	/**
	 * update method to use on the Virus action phase
	 * @author Samuel Kim
	 */
	public abstract void TurnBasedUpdate();
	
	/**
	 * update method to use on the Virus spread phase
	 * @author Brian Chau
	 */
	public abstract void spread();
	
	/**
	 * this method spawns an instance of This virus onto the grid depending on the subclassed Virus's logic
	 * @author Brian Chau
	 */
	public abstract void spawn();
	
	/**
	 * Spawn an instance of this Virus at a specific location
	 * @param pos position to spawn
	 */
	public abstract void spawnAtLocation(IntVector pos);
	
}
