package TowerDefense;

/**
 * This Walker is SO basic.  Moves at the default speed and is totally adorbs.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class WalkerBasic extends Walker
{	
	public static final AnimationData ANIM_WALK = new AnimationData("art/Characters/HTL_Walker_Turnip.png", 512, 512, 64, 64, 20, 30);
	public static final AnimationData ANIM_DEATH = new AnimationData("art/Characters/HTL_Walker_Turnip_Death.png", 512, 512, 64, 64, 20, 30);
		
	public static final float SPEED_MULTIPLIER_FROM_WALKER_TYPE = 1;
	
	public static final String SOUND_DEATH = "audio/sfx/octoleafDeath.mp3";
	
	private static Type walkerType = Type.BASIC;
	
	
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
	public WalkerBasic(Path path)
	{
		this(path.getSpawnLocationX(), path.getSpawnLocationY(), path.getNextWaypoint());
	}
	
	
	
	/**
	 * Constructor.
	 * @param xLoc			X-coordinate of location.
	 * @param yLoc			Y-coordinate of location.
	 * @param destination	Waypoint this Walker should move towards.	
	 */
	public WalkerBasic(float xLoc, float yLoc, Waypoint destination)
	{
		super(xLoc, yLoc, destination);
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
	 * Plays the sound associated with tower shooting.
	 */
	public void playSoundDeath()
	{
		HTL.resources.playSound(SOUND_DEATH);
	}
	
	

}
