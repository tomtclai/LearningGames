package gridElements;

import java.util.ArrayList;

import structures.IntVector;
import corrupted.Game.Sounds;
import corrupted.ErrorHandler;
import corrupted.Game;

public class RepairVirus extends Virus
{
	
	
	/**
	 * Instantiates a default RepairVirus
	 * @param gm reference to Game
	 */
	public RepairVirus(Game gm){
		super(ColorEnum.RED, gm); // color doesnt matter for RepairProtocol
		this.setSpriteSheet("Heal.png", PIXEL_SIZE, PIXEL_SIZE, 10, 1);
		this.setUsingSpriteSheet(true);
		
	}
	
	/**
	 * Instantiates a RepairVirus with a predetermined position
	 * @param _position position of the RepairVirus
	 * @param gm reference to Game
	 */
	public RepairVirus(IntVector _position,Game gm){
		this(gm);
		this.moveTo(false, _position);
	}

	/**
	 * if not newly spreaded this turn, clear corruption and contiguous viruses
	 */
	public void TurnBasedUpdate(){
		
		if(this.newlySpreaded){
			return;
		}
		cleanThings();
	}

	/**
	 * moves towards the nearest corruption or virus and cleans corruptionn while it moves
	 * @author Brian Chau
	 */
	@Override
	public void spread()
	{
		//find nearest corrupted
		IntVector myPosition = new IntVector(getCenter());
		IntVector nearestHostile = mGM.corruptionHelper.getNearestElement(myPosition);
		//if there are no corrupted, look for viruses
		if(nearestHostile == null)
		{
			nearestHostile = mGM.virusHelper.getNearestElement(myPosition);
		}
		//if no viruses either, do nothing
		if(nearestHostile == null) return;
		
		//get a "unit vector" of the distance
		int x = 0;
		int y = 0;
		IntVector distance = nearestHostile.subtract(myPosition);
		if (distance.getX() > 0) x = 1;
		else if (distance.getX() < 0) x = -1;
		if (distance.getY() > 0) y = 1;
		else if (distance.getY() < 0) y = -1;
		distance = new IntVector(x, y);		
		IntVector destination = myPosition.add(distance);
		//clear for good measure
		
		mGM.repairHelper.moveElement(true,  myPosition, destination);
		newlySpreaded = true;
		cleanThings();
	}
	
	/**
	 * Using the repair protocol's current position, clear out existing corruption.
	 * If there is also a virus, clear all contiguous virus and destroy self.
	 * @author Brian Chau
	 */
	private void cleanThings()
	{
		boolean somethingWasCleared = false;
		int posx = (int)getCenterX();
		int posy = (int)getCenterY();
		IntVector posVec = new IntVector(posx, posy);
		Corruption corruption = (Corruption)mGM.corruptionHelper.getElement(posx, posy);
		Virus virus=null;
		try
		{
			virus = (Virus)mGM.virusHelper.getElement(posx, posy);
		}
		catch(ClassCastException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_VIRUS);
		}
		//cure corruption if exists and spawn a new tile. (if there is also a virus, do virus logic instead)
		if (corruption != null && virus == null)
		{
			corruption.markForDelete();
			mGM.tileHelper.putTile(posVec, getRandomColorEnum());
			somethingWasCleared=true;
		}
		
		//if there is a virus on this tile, get all contiguous virus indexes and clear virus and corruption
		if(virus != null)// && !virus.getType().equals("RepairVirus"))
		{
			//destory a virus and repopulate with random tiles
			ArrayList<IntVector> matches = mGM.tileHelper.getContiguousTiles(posVec);
			if(matches != null){
				mGM.virusHelper.markForDelete(matches);
				mGM.corruptionHelper.markForDelete(matches);
				for(int i = 0; i < matches.size(); i++)
				{
					mGM.tileHelper.putTile(matches.get(i), getRandomColorEnum());	
				}
			}
			//if we clear a virus, mark this repair protocol for delete
			markForDelete();
			somethingWasCleared = true;
		}		
		if(somethingWasCleared)
		{
			mGM.triggerSound(Sounds.repairheal);
		}
	}
	
	/**
	 * Spawn a repair  protocol within 2 spaces from a corruption or virus tile
	 * @author Brian Chau
	 */
	@Override
	public void spawn(){
		IntVector loc = mGM.corruptionHelper.getRandomElementIndex();
		//if corrupted layer is empty try finding a virus
		if(loc == null) 
		{
			loc = mGM.virusHelper.getRandomElementIndex();
		}
		//if viru layer also null, do nothing.
		if(loc == null)
		{
			return;
		}
		//get random offset
		int newx = rand.nextInt(5)-2;
		int newy = rand.nextInt(5)-2;
		loc = loc.add(new IntVector(newx,newy));
		//if out of bounds, scoot it back into bounds
		//make sure it spawns past the firewall
		loc.forceIntoBounds(4, 0, mGM.getWidth()-1, mGM.getHeight()-1);
		
		//now spawn a virus here 
		spawnAtLocation(loc);

	}

	/**
	 * Spawn a repair protocol at a specific location
	 * @param pos specific location to spawn
	 */
	@Override
	public void spawnAtLocation(IntVector pos) {
		//we dont care of the color		
		if(pos == null)
		{
			return;
		}
		
		ColorEnum newColor = ColorEnum.RED;
				
		RepairVirus mRepairVirus = new RepairVirus(mGM);
		mRepairVirus.newlySpreaded = false;
		mRepairVirus.moveTo(false,pos);
		mGM.repairHelper.putVirus(pos, mRepairVirus);
				
		mRepairVirus.cleanThings();
	}
}
