package TowerDefense;
import Engine.Vector2;

/**
 * Smoke teleports characters and is characterized by its poofiness.
 * It is like a magical vehicle that moves Towers from one Tile to another Tile.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Smoke extends GameObjectPlus
{		
	public static final AnimationData ANIM_APPEAR = new AnimationData("smoke_appear.png", 280, 70, 70, 70, 4, 30);
	public static final AnimationData ANIM_DISAPPEAR = new AnimationData("smoke_disappear.png", 280, 70, 70, 70, 4, 30);
	
	private Tower myTower = null;
	
	public enum MovementPhase
	{
		NOT_MOVING,
		SOURCE_SMOKE_APPEARING, SOURCE_SMOKE_DISAPPEARING,
		DELAY,
		DESTINATION_SMOKE_APPEARING, DESTINATION_SMOKE_DISAPPEARING
	}
	
	private MovementPhase movementPhase = MovementPhase.NOT_MOVING;
	private Tile movementDestinationTile = null;						// which Tile are we currently moving to?
	private CooldownTimer movementDelayTimer = new CooldownTimer(0);	// delay between first smoke and second smoke
	
	
	
	/**
	* Loads images & sounds associated with this entity type.
	*/
	public static void preloadResources()
	{
		HTL.resources.preloadImage(ANIM_APPEAR.getFilename());
		HTL.resources.preloadImage(ANIM_DISAPPEAR.getFilename());
	}
	
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////

	/**
	 * Constructor.
	 * @param xLoc		X-coordinate of location.
	 * @param yLoc		Y-coordinate of location.
	 */
	public Smoke(float xLoc, float yLoc, Tower tower)
	{
		super(xLoc, yLoc, HTL.SMOKE_WIDTH, HTL.SMOKE_HEIGHT);
		this.setVisibilityTo(false);
		
		this.myTower = tower;
	}

	
	/**
	 * Destructor.
	 */
	public void destroy()
	{
		myTower = null;
		movementDestinationTile = null;
		super.destroy();
	}
	
	////////////////////////
	//                    //
	//       UPDATE       //
	//                    //
	////////////////////////
	
	
	/**
	 * Update this object.
	 */
	public void update()
	{	
		this.updateMovement();		
		super.update();
	}	
	
	
	/**
	 * Determines what phase of movement we are in, and calls the appropriate sub-function
	 */
	private void updateMovement()
	{
		if(movementPhase == MovementPhase.SOURCE_SMOKE_APPEARING &&
				!this.hasNonLoopingAnimationInProgress())
		{
			movementPhaseTransitionToSourceSmokeDisappearing();
		}
		
		else if(movementPhase == MovementPhase.SOURCE_SMOKE_DISAPPEARING &&
				!this.hasNonLoopingAnimationInProgress())
		{
			movementPhaseTransitionToDelay();
		}
		
		else if(movementPhase == MovementPhase.DELAY &&
				movementDelayTimer.isReady())
		{
			movementPhaseTransitionToDestinationSmokeAppearing();
		}
		
		else if(movementPhase == MovementPhase.DESTINATION_SMOKE_APPEARING &&
				!this.hasNonLoopingAnimationInProgress())
		{
			movementPhaseTransitionToDestinationSmokeDisappearing();
		}
		
		else if(movementPhase == MovementPhase.DESTINATION_SMOKE_DISAPPEARING &&
				!this.hasNonLoopingAnimationInProgress())
		{
			movementPhaseTransitionToNotMoving();
		}
	}




	
	////////////////////////////
	//                        //
	//     WHAT SMOKE DOES    //
	//                        //
	////////////////////////////
	

	
	/**
	 * Translates this Tower to a specific Tile.
	 * @param destination		The Tile to move the Tower to.
	 * @return					Whether or not the Tower was actually moved.
	 */
	public boolean activate(Tile destination)
	{				
		// set flag that shows we are in the process of moving
		movementPhase = MovementPhase.SOURCE_SMOKE_APPEARING;
		movementDestinationTile = destination;
		
		// reserve the destination tile
		movementDestinationTile.setReserved(true);
		
		// start playing smoke animation
		Vector2 location = myTower.getCenter();
		this.setCenter(location);
		this.setVisibilityTo(true);
		this.forceSingleAnim(ANIM_APPEAR);

		return true;
	}
	
	
	
	/**
	 * The smoke has appeared all the way; now let's disappear and reveal that THERE IS NO LONGER A DEFENDER deal with it
	 */
	private void movementPhaseTransitionToSourceSmokeDisappearing()
	{			
		// officially moving from PHASE 0 to PHASE 1
		
		// DEFENDER: invisible!
		myTower.setVisibilityTo(false);
		myTower.setCenter(-100, -100);		// relocate so that cursor leaves screen
		
		// SMOKE: set visible, move to Tower location, and start to disappear
		this.startSingleAnim(ANIM_DISAPPEAR);
		
		// Tower is no longer at the old tile, if any
		if(myTower.getTile() != null)
		{
			myTower.getTile().removeOccupant();
		}
		
		// let's consider the Tower Tile-less temporarily
		myTower.setTile(null);
		
		// time for the next phase of movement
		movementPhase = MovementPhase.SOURCE_SMOKE_DISAPPEARING;	
	}
	

	/**
	 * The smoke has disappeared, and now we shall wait until the destination smoke appears.
	 */
	private void movementPhaseTransitionToDelay()
	{
		// officially moving from PHASE 1 to PHASE 2			
		
		// SMOKE: invisible!
		this.setVisibilityTo(false);
		
		// start the timer until we can go to the next step
		movementDelayTimer.start(1);
		
		// time for the next phase of movement
		movementPhase = MovementPhase.DELAY;				
	}
	
	
	/**
	 * Huzzah!  The wait is over, and the smoke begins to appear at the destination!
	 */
	private void movementPhaseTransitionToDestinationSmokeAppearing()
	{
		// officially moving from PHASE 2 to PHASE 3
		
		// SMOKE: start to appear
		Vector2 location = movementDestinationTile.getCenter();
		this.setCenter(location);
		this.setVisibilityTo(true);
		this.startSingleAnim(ANIM_APPEAR);
		
		// time for the next phase of movement
		movementPhase = MovementPhase.DESTINATION_SMOKE_APPEARING;				
	}
	
	
	/**
	 * The smoke is there.  Let's tell it to disappear, revealing a fastidious Tower underneath.
	 */
	private void movementPhaseTransitionToDestinationSmokeDisappearing()
	{
		// officially moving from PHASE 3 to PHASE 4
		
		// DEFENDER: move to tile and set visible
		myTower.setTile(movementDestinationTile);
		myTower.setVisibilityTo(true);
		
		// SMOKE: start to disappear
		this.startSingleAnim(ANIM_DISAPPEAR);
		
		// time for the next phase of movement
		movementPhase = MovementPhase.DESTINATION_SMOKE_DISAPPEARING;				
	}
	
	
	/**
	 * The smoke has dissipated, the Tower is in place, and now it is time to close the curtains on this fascade.
	 */
	private void movementPhaseTransitionToNotMoving()
	{
		// officially ending the movement
		
		// SMOKE: invisible; back to the Disney vault!
		this.setVisibilityTo(false);
		
		// time for the next phase of movement
		movementPhase = MovementPhase.NOT_MOVING;
	}	
	
	
	
	
	
	////////////////////////
	//                    //
	//    INFO SHARING    //
	//                    //
	////////////////////////
	
	
	/**
	 * @return	Returns true if the smoke is doing work
	 */
	public boolean isActive()
	{
		return movementPhase != MovementPhase.NOT_MOVING;
	}
}
