package TowerDefense;

/**
 * This tower is speedy, which means that it has the ability to speed up things.
 * @author Branden Drake
 * @author Rachel Horton
 */
public class TowerSpeedy extends Tower
{	
	public static final AnimationData ANIM_IDLE = new AnimationData("art/Characters/HTL_Wizard_Speed_Idle.png", 512, 512, 64, 64, 20, 30);
	public static final AnimationData ANIM_SELECTED = new AnimationData("art/Characters/HTL_Wizard_Speed_Selected.png", 512, 512, 64, 64, 20, 30);
	public static final AnimationData ANIM_DEATH = ANIM_SELECTED; // no death animations currently
	
	public static final String SOUND_SHOOT = "audio/sfx/speedBuff.mp3";
	
	public static final AnimationData EFFECT_SPELLCAST = new AnimationData("art/Characters/Spells/HTL_Spells_Speed.png", 1024, 1024, 200, 200, 25, 30);
	
	private static float castHealthAdjust = 0;
	private static float castSpeedAdjustDuration = 1;
	private static float castSpeedAdjustMultiplier = 3;
	
	private static Type towerType = Type.SPEEDY;
	
	/**
	 * Loads images & sounds associated with this entity type.
	 */
	public static void preloadResources()
	{
		Tower.preloadResources();
		
		HTL.resources.preloadImage(ANIM_IDLE.getFilename());
		HTL.resources.preloadImage(ANIM_SELECTED.getFilename());
		HTL.resources.preloadImage(ANIM_DEATH.getFilename());
		HTL.resources.preloadImage(EFFECT_SPELLCAST.getFilename());
		
		HTL.resources.preloadSound(SOUND_SHOOT);
	}
	
	
	
	
	/**
	 * Constructor.
	 * @param tile		The tile to spawn this unit at.
	 */
	public TowerSpeedy()
	{				
		projectileAnim = "blueberry.png";	//currently not used
		
		this.effectSpellcast = new EffectVisual(EFFECT_SPELLCAST, this.getCenterX(), this.getCenterY(), 4, 4);
		if(layerEffects != null)
		{
			effectSpellcast.moveToDrawingLayer(layerEffects);
		}
	}
	
	

	/**
	 * Update the unit.
	 * @param walkerList	The list of Walkers in the game.
	 * @param projectiles	The list of Projectiles in the game.
	 */
	public void update()
	{	
		super.update();
		this.effectSpellcast.update();
	}
	
	
	
	/**
	 * Returns the AnimationData for this Tower's Idle state.
	 */
	public AnimationData getAnimationIdle()
	{
		return ANIM_IDLE;
	}

	/**
	 * Returns the AnimationData for this Tower's Selected state.
	 */
	public AnimationData getAnimationSelected()
	{
		return ANIM_SELECTED;
	}
	
	/**
	 * Returns the AnimationData for this Tower's Death state.
	 */
	public AnimationData getAnimationDeath()
	{
		return ANIM_DEATH;
	}
	
	
	
	
	
	
	
	
	/////////////////////////
	//                     //
	//        Audio        //
	//                     //
	/////////////////////////	
	

	
	/**
	 * Plays the sound associated with tower shooting.
	 */
	public void playSoundSpellcast()
	{
		HTL.resources.playSound(SOUND_SHOOT);
	}
	
	
	/**
	 * Sets how Walker health is adjusted per Tower spellcast.
	 */
	static public void setCastHealthAdjust(float value)
	{
		castHealthAdjust = value;
	}
	
	
	/**
	 * Returns the value that Walker health is adjusted per Tower spellcast.
	 */
	public float getCastHealthAdjust()
	{
		return castHealthAdjust;
	}
	
	
	/**
	 * Sets the duration in seconds that a Walker will have speed adjusted per Tower spellcast.
	 */
	static public void setCastSpeedAdjustDuration(float seconds)
	{
		castSpeedAdjustDuration = seconds;
	}
	
	
	/**
	 * Returns the duration in second that a Walker will have speed adjusted per Tower spellcast.
	 */
	public float getCastSpeedAdjustDuration()
	{
		return castSpeedAdjustDuration;
	}
	
	
	/**
	 * Sets the speed multiplier applied to Walkers by a Tower spellcast.
	 */
	static public void setCastSpeedAdjustMultiplier(float multiplier)
	{
		castSpeedAdjustMultiplier = multiplier;
	}
	
	
	/**
	 * Returns speed multiplier applied to Walkers per Tower spellcast.
	 */
	public float getCastSpeedAdjustMultiplier()
	{
		return castSpeedAdjustMultiplier;
	}
	
	
	/**
	 * Returns the type of tower this is.
	 */
	public final Type getTowerType()
	{
		return towerType;
	}
}
