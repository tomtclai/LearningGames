package TowerDefense;
import Engine.Vector2;

import java.util.LinkedList;

/**
 * Walkers are the characters that move along the path.
 * Are they friends?  Are they foes?  That's for the developers to decide.
 * 
 * NOTE: Walkers used to be able to attack things.  This functionality has not
 * been used or updated in quite awhile.  Most of the functionality regarding
 * attacking has been commented out, and what hasn't won't affect the game.
 * Theoretically, someone could get it working again if they want to.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
abstract public class Walker extends Character
{		
	public static final String SOUND_SURVIVAL = "audio/sfx/survived.mp3";	// lived until the finish line
	
	private static boolean resourcesHaveBeenPreloaded = false;
	
	private static SetOfWalkers repository = null;	// static list of all Walkers	
	
	public static boolean fastMode = false;			// this is a debug setting to make Walkers move faster (note: may no longer work)

	private Waypoint destination = null;			// the waypoint this unit will be moving towards

	
	
	private static float speedDefault = 0.01f;		// the distance basic Walkers move per frame (default speed)	
	private float speedCurrent = 0;					// this individual Walker's current speed
	
	
	// Speed multiplier from an external buff, and the amount of time it until it wears off.
	// Current implementation only supports a single buff at a time.
	private float buffSpeedMultiplier = 1f;
	private CooldownTimer buffSpeedMultiplierTimer = new CooldownTimer(0);


	// the amount of damage that a Walker should take per second (can be negative to heal)
	private static float damageTakenPerSecond = 0;
	private CooldownTimer damageTakenPerSecondTimer = new CooldownTimer(1);
	
	private Direction facing = Direction.RIGHT;			// which direction the character is facing/moving
	
	
	private static boolean hideAllStatVisuals = false;  // should we hide health for ALL Walkers?
	private boolean hideMyStatVisuals = false;			// should we hide health for THIS Walker?		
	
	private static float secondsUntilDeadDisappear = 10f;
	private CooldownTimer disappearAfterDeathTimer = null;
	
	
	public enum Type
	{
		BASIC, QUICK
	}
	
	
	
	
	///////// most Attack-related functionality is commented out ////////////
	
	private boolean attacking = false;					// whether or not the Walker is currently in an attack state
	
	//private static float attackHealthAdjust = -1;		// how is the target's health affected each attack?
	//private static float timeBetweenAttacks = 100;		// how many seconds between attacks?
	//private CooldownTimer attackCooldown = new CooldownTimer(timeBetweenAttacks);  // related timer
	
	// for the purposes of attacking objects, Walkers have an invisible rectangle for collision checks.
	//private GameObject collision = null;
	
	
	
	///////////////////////////////
	//                           //
	// Universal Static Settings //
	//                           //
	///////////////////////////////	
	
	
	
	/**
	 * Sets the amount of damage all Walkers will take every second.
	 * Note that this will override any health being gained per second.
	 * @param damage	The amount of damage to take every second.
	 */
	public static void setDamageTakenPerSecond(float damage)
	{
		damageTakenPerSecond = damage;
	}
	
	
	/**
	 * Sets the amount of health all Walkers will gain every second.
	 * Note that this will override any damage being taken per second.
	 * @param health	The amount of health to gain per second.
	 */
	public static void setHealthGainedPerSecond(float health)
	{
		setDamageTakenPerSecond(-health);
	}
	
	
	/**
	 * Note: negative values mean that Walkers are being healed every second.
	 * @return	The amount of damage Walkers currently take every second.
	 */
	public static float getDamageTakenPerSecond()
	{
		return damageTakenPerSecond;
	}
	
	
	/**
	 * Note: negative values mean that Walkers are being damaged every second.
	 * @return	The amount of health Walkers gain every second.
	 */
	public static float getHealthGainedPerSecond()
	{
		return getDamageTakenPerSecond();
	}
	
	
	/**
	 * Sets the default, unmodified speed of Walkers.  The value must be positive.
	 * Note that while some Walkers with modified movement speeds may not move
	 * at this exact speed, all Walkers will be affected by this change.
	 * @param speed		The speed of an unmodified, basic Walker.
	 * @return			True if the new speed was applied.
	 */
	public static boolean setDefaultSpeed(float speed)
	{
		if(speed < 0) return false;
		
		// set the new default speed
		speedDefault = speed;		
		
		return true;
	}
	
	
	/**
	 * Returns the unmodified default speed of Walkers.
	 * Note that while some Walkers with modified movement speeds may not move
	 * at this exact speed, all Walkers will be affected by this change.
	 * @return			The speed.
	 */
	public static float getDefaultSpeed()
	{
		return speedDefault;
	}
	
	
	/**
	 * When a Walker dies, it will disappear from the screen after
	 * this many seconds.
	 * 
	 * Negative numbers will result in Walkers never disappearing.
	 * @param seconds		How long until dead Walkers disappear.
	 */
	public static void setTimeUntilDeadDisappear(float seconds)
	{
		secondsUntilDeadDisappear = seconds;
	}
	
	
	/**
	 * Sets the repository that newly-created Walkers will be added to.
	 * The repository is a set of Walkers, and setting the repository
	 * allows this list to automatically be updated each time a Walker
	 * is created.
	 * A null value will result in no automatic repository being used.
	 * @param walkerSet		The storage container for Walkers.
	 */
	public static void setRepository(SetOfWalkers walkerSet)
	{
		Walker.repository = walkerSet;		
	}
	
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////	
	
	
	
	/**
	 * Constructor.
	 * @param path		The path that this Walker will follow.
	 */
	public Walker(Path path)
	{
		this(path.getSpawnLocationX(), path.getSpawnLocationY(), path.getNextWaypoint());
	}
	
	
	
	/**
	 * Constructor.
	 * @param xLoc			X-coordinate of location.
	 * @param yLoc			Y-coordinate of location.
	 * @param destination	Waypoint this Walker should move towards.	
	 */
	public Walker(float xLoc, float yLoc, Waypoint destination)
	{
		super(xLoc, yLoc, HTL.CHARACTER_WIDTH, HTL.CHARACTER_HEIGHT);
		this.destination = destination;

		this.setTeam("Walker");
		
		this.updateAnim();
		
		this.updateStatVisuals();
		
		//So that we don't see all the animations above Walkers' heads.  Can turn back on if we want.
		this.hasHealthAnimations = false;
		this.hasBadFeelsAnimations = false;
		
		//this.initializeCollision();
		
		if(Walker.repository != null)
		{
			repository.addWalker(this);
		}
	}

	
	
	
	/**
	 * Destructor.
	 * Remove this Walker from the game...as much as we can in Java.
	 */
	public void destroy()
	{
		if(Walker.repository != null)
		{
			Walker.repository.removeWalker(this);
		}
		//hideMyStatVisuals = true;
		//this.updateStatVisuals();
		super.destroy();	// this kind of just hides it, and we pretend it doesn't exist
	}
	
	
	////////////////////////
	//                    //
	//  Abstract Methods  //
	//                    //
	////////////////////////
	
	// returns the type of Walker
	abstract public Type getWalkerType();
	
	// returns the walk animation data for the specific Walker type
	abstract public AnimationData getAnimationWalk();
	
	// returns the death animation data for the specific Walker type
	abstract public AnimationData getAnimationDeath();
	
	// each type of walker has a different speed multiplier
	abstract public float getWalkerTypeSpeedMultiplier();
	
	// each type of walker has a few distinct sounds
	abstract public void playSoundDeath();
	
	
	
	
	
	
	
	
	
	////////////////////////
	//                    //
	//      Updates       //
	//                    //
	////////////////////////
	
	
	/**
	 * Update this object.
	 */
	public void update()
	{
		this.update(null, null);
	}
	
	
	/**
	 * Update this object.
	 * @param towers		The list of towers in the current game.
	 * @param barricades	The list of barricades in the current game.
	 */
	public void update(LinkedList<Tower> towers, LinkedList<Barricade> barricades)
	{			
		if(isDestroyed())
		{
			return;
		}
		
		updateAnim();
		
		updateMovement();

		updateDamagePerSecond();
		
		updateStatVisuals();
		
		updateDeathDisappearance();
		
		// attack-related functionality commented out
		//updateCollisionLocation();
		//updateTarget(towers, barricades);		
		//updateAttack();
		
		super.update();
	}
	

	
	
	/////////////////////////
	//                     //
	//  Speed & Movement   //
	//                     //
	/////////////////////////
	
	
	
	/**
	 * Update stuff that has to do with movement.
	 */
	private void updateMovement()
	{	
		this.updateWaypoint();
		
		// if stationary, don't move
		if(destination == null || this.isDead() || this.attacking || this.isFrozen())
		{
			this.setVelocity(0,0);
		}
		// otherwise, set the Walker to move towards its current waypoint
		else
		{
			this.setVelocityTowardsDestination();
		}
	}
	
	
	
	/**
	 * If the Walker has reached its current Waypoint, set its sights on the next Waypoint.
	 */
	private void updateWaypoint()
	{
		// if you're at your destination, change your destination to the next waypoint
		if(destination != null && destination.collidedWithWaypoint(this))
		{			
			destination = destination.getNextWaypoint();
		}
	}
	
	
	/**
	 * @return		True if the Walker has reached the end of its path.
	 */
	public boolean isAtPathEnd()
	{
		return destination == null;
	}
	
	
	/**
	 * Let's get the Walker moving towards its destination.
	 */
	private void setVelocityTowardsDestination()
	{	
		Vector2 here = this.getCenter();
		Vector2 there = destination.getCenter();
		
		float yDistance = there.getY() - here.getY();		
		float xDistance = there.getX() - here.getX();
		
		// this gives us a unit vector (magnitude 1) pointing in the right direction
		Vector2 vector = new Vector2(xDistance, yDistance);		
		vector.normalize();	
		
		// update the current speed and then multiply our unit vector by it
		this.updateCurrentSpeed();
		vector.mult(speedCurrent);
		
		// actually adjust the velocity with the vector we made
		this.setVelocity(vector);
		
		// this makes the animation sprite face the right way
		updateFacing();					
	}
	
	/**
	 * this makes sure that the current movement speed is
	 * what it should be, in accordance with default movement speed
	 * AND whether there is a speed buff 
	 */
	private void updateCurrentSpeed()
	{
		speedCurrent = speedDefault;
		
		// adjust speed for type of Walker
		speedCurrent *= getWalkerTypeSpeedMultiplier();
		
		// adjust speed for buffs
		if(!buffSpeedMultiplierTimer.isReady())
		{
			speedCurrent *= buffSpeedMultiplier;
		}
	}
	
	
	/**
	 * Multiplies this Walker's speed by a value for a set duration.
	 * Note that this will override any existing speed buffs.
	 * @param multiplier	The value to multiply speed by.
	 * @param duration		The number of seconds for the buff to last.
	 */
	public void applySpeedBuff(float multiplier, float duration)
	{
		buffSpeedMultiplierTimer.start(duration);
		buffSpeedMultiplier = multiplier;
	}
	
	
	/** 
	 * Rotates the Walker image so that the unit appears to be looking in the direction it is moving.
	 * Note that this only currently supports four cardinal directions; no diagonals!
	 */
	private void updateFacing()
	{
		//default image is facing upwards		
		float x = this.getVelocityX();
		float y = this.getVelocityY();
		
		// if we're moving left or right
		if(Math.abs(x) > Math.abs(y))
		{
			if(x > 0)	// right
			{
				facing = Direction.RIGHT;
				this.setRotation(270);
			}
			else		// left
			{
				facing = Direction.LEFT;
				this.setRotation(90);
			}
		}
		// otherwise we're moving up or down
		else
		{
			if(y > 0)	// up
			{
				facing = Direction.UP;
				this.setRotation(0);
			}
			else		// down
			{
				facing = Direction.DOWN;
				this.setRotation(180);
			}
		}
	}
	
	
		
	///////////////////////////////
	//                           //
	//    Resource Preloading    //
	//                           //
	///////////////////////////////
	
	
	
	/**
	 * Loads images & sounds associated with this entity type.
	 */
	public static void preloadResources()
	{
		if(!resourcesHaveBeenPreloaded)
		{
			HTL.resources.preloadSound(SOUND_SURVIVAL);
			Character.preloadResources();
			
			resourcesHaveBeenPreloaded = true;
		}
	}
	
	
	
	///////////////////////////////
	//                           //
	//         Animation         //
	//                           //
	///////////////////////////////
	
	
	
	
	
	/**
	 * Start a new animation if appropriate.
	 */
	private void updateAnim()
	{		
		if(this.hasJustDied())
		{
			this.forceSingleAnim(getAnimationDeath());
		}
		// attack-related functionality commented out
		//else if (attacking)
		//{
		//	this.startLoopingAnim(getAnimationWalk()); // currently no attack anims
		//}
		else if(!this.isDead())
		{
			this.startLoopingAnim(getAnimationWalk());
		}
	}
	

	
	
	///////////////////////////////
	//                           //
	//          Audio            //
	//                           //
	///////////////////////////////
	
	
	
	
	/**
	 * Plays the sound indicating that a Walker made it
	 * to the end of the path and survived.
	 */
	public static void playSoundSurvival()
	{
		HTL.resources.playSound(SOUND_SURVIVAL);
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//         Utility           //
	//                           //
	///////////////////////////////
	


	
	/**
	 * If the unit is alive, set the HUD to be either displayed or hidden in accordance with variable state.
	 * Also set the direction in which the visual appears from the character.
	 */
	private void updateStatVisuals()
	{
		boolean showStatVisuals = !hideAllStatVisuals && !hideMyStatVisuals;
		
		if(!isDead())
		{
			setStatsVisible(showStatVisuals);
		}
		
		switch(facing)
		{
			case UP:		setStatVisualDirection(Direction.LEFT);
							break;
			case DOWN:		setStatVisualDirection(Direction.RIGHT);
							break;
			case LEFT:		setStatVisualDirection(Direction.DOWN);
							break;
			default:		setStatVisualDirection(Direction.UP);
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * If any damage per second or healing per second needs to happen,
	 * make it so.
	 */
	private void updateDamagePerSecond()
	{
		if(isDead()) return;
		
		if(damageTakenPerSecondTimer.isReady())
		{
			// makes them "pulse" when they take damage
			//this.setSize(0.9f);
			
			damageTakenPerSecondTimer.start();
			this.addHealth(-damageTakenPerSecond);
		}
		else
		{
			// makes them "pulse" when they take damage
			//this.setSize(1);
		}
	}
	
	
	
	/**
	 * Controls whether this Walker should disappear a certain time after its death
	 */
	private void updateDeathDisappearance()
	{
		if(isDead())
		{
			if(disappearAfterDeathTimer == null)
			{
				disappearAfterDeathTimer = new CooldownTimer(secondsUntilDeadDisappear);
			}
			else
			{
				if(disappearAfterDeathTimer.isReady())
				{
					//this.setVisibilityTo(false);
					this.destroy();
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/////////////////////////
	//                     //
	//       Combat        //
	//                     //
	/////////////////////////
	
	// NOTE!  Combat for Walkers was removed from the "official" HTL game.
	// Because of that, these functions haven't been updated.
	// They might still work?  If you're feeling adventurous, take a chance!
	
	
	
	
	
	
	
	
	
	/**
	 * Validate current target if any, and get a new target if we need one.
	 * @param towerList		The list of Towers in the current game.
	 * @param barricadeList		The list of Barricades in the current game.
	 */
	/*
	private void updateTarget(LinkedList<Tower> towerList, LinkedList<Barricade> barricadeList)
	{		
		// if our old target is invalid 
		if(this.hasTarget())
		{
			if( !this.hasValidTarget() || !this.collision.collided(this.getTarget()) )
			{
				this.setTarget(null);
			}
		}
		
		// get a new target
		if( !this.hasTarget() )
		{
			// first check Barricades
			for(Barricade barricade : barricadeList)
			{
				if(barricade.isValidTarget() && this.collision.collided(barricade))
				{
					this.setTarget(barricade);
					return;
				}
			}
			
			// if no barricades, let's get us a Tower to munch on (not literally)
			//for(Tower tower : towerList)
			//{
			//	if(tower.isValidTarget() && this.collision.collided(tower))
			//	{
			//		this.setTarget(tower);
			//		return;
			//	}
			//}
		}
	}
	*/
	
	
	/**
	 * Attack if need be, and maybe even do some damage!
	 */
	/*
	private void updateAttack()
	{
		attacking = false;
		// so many reasons not to attack
		if(this.isDead() || !this.hasTarget() || this.isFrozen())
		{
			return;
		}
		
		// we are attacking; this is a state we enter, and it affects animation
		attacking = true;

		// if the cooldown timer is ready, let's actually deal some damage due to attacking
		if(attackCooldown.isReady())
		{
			attackCooldown.start();
			
			float multiplier = 1;
			
			float dmg = attackHealthAdjust * multiplier;
			
			// debug
			//float targetHealth = getTarget().getHealth();
			//System.out.println("Target's health is " + targetHealth + ".  Target then receives " + dmg + " damage!");
			
			getTarget().addHealth(dmg);
		}		
	}
	*/
	
	
	/**
	 * Creates the collision object and makes it transparent
	 */
	/*
	void initializeCollision()
	{
		// dividing by speedMultiplier for size so that faster enemies have smaller collision boxes
		this.collision = new GameObject(xLoc, yLoc, HTL.characterWidth, HTL.characterHeight);
		// we use a transparent image, because setting visibility to off turns off ability to collide as well
		this.collision.setImage("transparent.png");	
	}
	*/
	
	
	/**
	 * Moves the collision to where the rest of the object is.
	 */
	/*
	private void updateCollisionLocation()
	{
		Vector2 myCenter = this.getCenter();
		this.collision.setCenter(myCenter);
	}
	*/
	
}
