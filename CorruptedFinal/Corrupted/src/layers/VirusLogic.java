package layers;

import gridElements.*;
import gridElements.FuseVirus.FuseVirusTypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import structures.IntVector;
import corrupted.ErrorHandler;
import corrupted.Game;



public class VirusLogic extends LogicBase {
	private final boolean isRepair;
	private Queue<Virus> virusSpawner;
	
	/**
	 * Inatantiates a new VirusLogic
	 * @param layM
	 * @param isRepair
	 */
	public VirusLogic(Game layM, boolean isRepair)
	{
		super(layM);
		this.isRepair = isRepair;
		
		virusSpawner = new LinkedList<Virus>();
		
		if(isRepair)
			addSupportedVirus(new RepairVirus(new IntVector(-1,-1),layM));
		
		else{
			addSupportedVirus(new BurstVirus(new IntVector(-1,-1),GridElement.getRandomColorEnum(), layM));
			addSupportedVirus(new ChainVirus(new IntVector(-1,-1),GridElement.getRandomColorEnum(), layM));
			addSupportedVirus(new FuseVirus(GridElement.getRandomColorEnum(), layM,FuseVirusTypes.BOT));
		}
	}
	
	/**
	 * gets the VirusGrid or RepairGrid from the Game 
	 * depending on if this instance was instantiated as repair layer or not
	 * @author Brian Chau
	 * @return the grid that this Logic class executes on.
	 */
	@Override
	protected GridElement[][] getTheGrid()
	{
		return isRepair ? mGM.getRepairGrid() : mGM.getVirusGrid();
	}
	
	/**
	 * puts a given virus object at a desired location.
	 * 
	 * @author Brian Chau
	 * @param pos coordinate to put the virus
	 * @param virus virus to be put
	 * @return true if successful, false otherwise
	 */
	public boolean putVirus(IntVector pos, Virus virus)
	{
		return this.putElement(pos, virus);
	}
	
	/**
	 * Adds a Virus to the virus factory. Virus spawning will cycle through all the viruses in this list.
	 * @param v Virus to add to the list
	 */
	private void addSupportedVirus(Virus v)
	{
		virusSpawner.add(v);
	}
	
	/**
	 * This method spawns the next virus type from the internal cycle of viruses at a 
	 * random location.
	 * If the Tile Grid is empty, 
	 * @author Brian Chau
	 */
	public void spawnUpdate()
	{
		this.syncPositions();
		Virus toSpawn = virusSpawner.poll();
		toSpawn.spawn();
		virusSpawner.add(toSpawn);
	}

	/**
	 * This method should handle all virus spreading logic
	 * @author Brian Chau
	 */
	public void spreadUpdate()
	{
		this.syncPositions();
		//loop through all viruses and run spread logic if they have not spreaded
		for( int i=0 ; i < mGM.getWidth() ;i++){
			for( int j = mGM.getHeight()-1 ; j >= 0; j--) {
				GridElement current = getElement(i,j);
				if(current != null){
					Virus tempVirus = null;
					try
					{
						tempVirus = (Virus) current;
					}
					catch(ClassCastException e)
					{
						ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_VIRUS);
					}
					//only run spread if they have not spreaded this turn
					//this prevents a virus from being spread again after moving
					if(!tempVirus.newlySpreaded)
					{
						tempVirus.spread();
					}
					
				}
			}
		}
		
		//loop through again to mark them as not spreaded so they can spread again next update
		for( int i=0 ; i < mGM.getWidth() ;i++){
			for( int j = mGM.getHeight()-1 ; j >= 0; j--) {
				
				Virus tempV = null;
				try
				{
					tempV =(Virus)(getElement(i,j));
				}
				catch(ClassCastException e)
				{
					ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_VIRUS);
				}
				if(tempV != null)
					tempV.newlySpreaded = false;
			}
		}
	}
	
	/**
	 * Active the viruses on the layer (Do not do spreading logic here).
	 * 
	 * @author Samuel Kim, Brian Chau
	 */
	public void TurnBasedUpdate(){
		this.syncPositions();
		try{
			for( int i=0 ; i < mGM.getWidth() ;i++){
				for( int j = mGM.getHeight()-1 ; j >= 0; j--) {
					GridElement current = getElement(i,j);
					if(current != null){
						
						Virus tempVirus = null;
						try
						{
							tempVirus =  (Virus) current;
						}
						catch(ClassCastException e)
						{
							ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_VIRUS);
						}
						tempVirus.TurnBasedUpdate();
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
