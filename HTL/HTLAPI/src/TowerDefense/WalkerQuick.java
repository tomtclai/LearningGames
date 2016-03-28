package TowerDefense;


/**
 * This Walker moves twice as fast as the Basic Walker, but has half the health.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class WalkerQuick extends Walker
{	
	public static final AnimationData ANIM_WALK = new AnimationData("art/Characters/HTL_Walker_Grub.png", 512, 512, 64, 64, 20, 30);
	public static final AnimationData ANIM_DEATH = new AnimationData("art/Characters/HTL_Walker_Grub_Death.png", 512, 512, 64, 64, 20, 30);
	
	public static final float SPEED_MULTIPLIER_FROM_WALKER_TYPE = 2;
	
	public static final String SOUND_DEATH = "audio/sfx/caterpillarDeath.mp3";
	
	private static Type walkerType = Type.QUICK;
	
	
	/**
	 * Loads images & sounds associated with this entity type.
	 */
	public static void preloadResources()
	{
		Walker.preloadResources();
		
		HTL.resources.preloadImage(ANIM_WALK.getFilename());
		HTL.resources.preloadImage(ANIM_DEATH.getFilename());
		
		HTL.resources.preloadSound(SOUND_DEATH);
	}
	
	
	
	
	
	/**
	 * Constructor.
	 * @param path			The path this Walker will follow.
	 */
	public WalkerQuick(Path path)
	{
		this(path.getSpawnLocationX(), path.getSpawnLocationY(), path.getNextWaypoint());
	}
	
	
	
	/**
	 * Constructor.
	 * @param xLoc			X-coordinate of location.
	 * @param yLoc			Y-coordinate of location.
	 * @param destination	Waypoint this Walker should move towards.	
	 */
	public WalkerQuick(float xLoc, float yLoc, Waypoint destination)
	{
		super(xLoc, yLoc, destination);
		
		// fast, but with half health		
		float oldMaxHealth = getHealthMax();
		setHealthMax(oldMaxHealth / 2);
	}
	
	
	
	
	
	
	/**
	 * Returns the type of Walker this is.
	 */
	public final Type getWalkerType()
	{
		return walkerType;
	}
	
	
	
	
	
	/**
	 * Returns the speed multiplier for this type of Walker.
	 */
	public float getWalkerTypeSpeedMultiplier()
	{
		return SPEED_MULTIPLIER_FROM_WALKER_TYPE;
	}
	
	
	/**
	 * Returns the AnimationData for this Walker's movement state.
	 */
	public AnimationData getAnimationWalk()
	{
		return ANIM_WALK;
	}

	
	/**
	 * Returns the AnimationData for this Walker's death state.
	 */
	public AnimationData getAnimationDeath()
	{
		return ANIM_DEATH;
	}
	
	
	
	/**
	 * Plays the sound associated with this Walker type dying.
	 */
	public void playSoundDeath()
	{
		HTL.resources.playSound(SOUND_DEATH);
	}
}
