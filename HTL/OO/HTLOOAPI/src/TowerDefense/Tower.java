package TowerDefense;

import Engine.DrawingLayer;
import Engine.Vector2;

/**
 * Towers are meant to sit somewhere and shoot things in various ways.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public abstract class Tower extends Character
{	
	public static final String SOUND_SPAWN = "audio/sfx/towerSpawn.mp3";
	public static final String SOUND_MOVE = "audio/sfx/unitMove.mp3";
	public static final String SOUND_MOVE_FAIL = "audio/sfx/noAction.mp3";
	
	private static DrawingLayer layerTower = null;
	protected static DrawingLayer layerEffects = null;
	
	protected EffectVisual effectSpellcast = null;	// spellcast object is instantiated by subclass
	
	private static boolean resourcesArePreloaded = false;
	
	private Tile tile = null;						// the tile I am stationed at
	private Smoke mySmoke = null;					// the cloud of smoke that obscures movement
	
	private CooldownTimer shotCooldown = new CooldownTimer(0);
	
	protected boolean isSelected = false;
	
	protected String projectileAnim = null;
	protected float projectileSpeed = 1f;
	
	
	public enum Type
	{
		MEDIC, SPEEDY
	}	
	
	
	/**
	 * From now on, all newly spawned Towers will be put on the specified DrawingLayer.
	 * Enter null to use the default AutoDraw set.
	 * @param drawingLayer		The DrawingLayer.
	 */
	public static void setDrawingLayer(DrawingLayer drawingLayer)
	{		
		layerTower = drawingLayer;
	}
	
	/**
	 * From now on, all newly spawned Towers will have their effects on the specified DrawingLayer.
	 * Enter null to use the default AutoDraw set.
	 * @param drawingLayer		The DrawingLayer.
	 */
	public static void setDrawingLayerForEffects(DrawingLayer drawingLayer)
	{		
		layerEffects = drawingLayer;
	}
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////

	/**
	 * Constructor.
	 * 
	 */
	public Tower()
	{
		// starts the character offscreen if they aren't initialized to an appropriate Tile
		super(-50, -50, HTL.CHARACTER_WIDTH, HTL.CHARACTER_HEIGHT);
		
		this.initialize();
	}
	
	
	/**
	 * Initialize state.
	 */
	private void initialize()
	{	
		this.setTile(null);
		this.setTeam("Tower");
		
		this.mySmoke = new Smoke(-100, -100, this);
		if(layerEffects != null)
		{
			mySmoke.moveToDrawingLayer(layerEffects);
		}
		
		if(layerTower != null)
		{
			this.moveToDrawingLayer(layerTower);
		}
		
		this.setStatsVisible(false);
	}
	
	
	
	/**
	 * Destructor.
	 */
	public void destroy()
	{
		// why did we make a repository for the Walker and not one for the Towers?
		// is it because of the spawners?  Look into this when you have time.
		
		tile.removeOccupant();
		tile = null;
		
		mySmoke.destroy();
		mySmoke = null;
		
		effectSpellcast.destroy();
		effectSpellcast = null;
		super.destroy();
	}
	
	
	
	
	

	/////////////////////////
	//                     //
	//       Abstract      //
	//                     //
	/////////////////////////	
	
	// returns the type of tower this is
	abstract public Type getTowerType();
	
	// returns the idle animation data for the specific Tower type
	abstract public AnimationData getAnimationIdle();
	
	// returns the selected animation data for the specific Tower type
	abstract public AnimationData getAnimationSelected();
	
	// returns the death animation data for the specific Tower type
	abstract public AnimationData getAnimationDeath();
	
	// a non-static version of each sub-class's playSoundShoot
	abstract public void playSoundSpellcast();

	// gets the static health adjust amount for the specific Tower type
	abstract public float getCastHealthAdjust();
	
	// gets the static speed adjust duration for the specific Tower type
	abstract public float getCastSpeedAdjustDuration();
	
	// gets the static speed adjust multiplier for the specific Tower type
	abstract public float getCastSpeedAdjustMultiplier();

	
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
		if(isDestroyed())
		{
			return;
		}
		
		animUpdate();
		mySmoke.update();
		
		super.update();
	}	

	

	
	////////////////////////
	//                    //
	//       SHOOT        //
	//                    //
	////////////////////////
	
	
	/**
	 * Shoots a projectile at a Character.
	 * Likely doesn't work at the present.
	 * @param target		The Character to shoot at.
	 */
	public void shootAt(Character target)
	{
		Projectile proj = new Projectile(getCenterX(), getCenterY(),
				HTL.PROJECTILE_WIDTH, HTL.PROJECTILE_HEIGHT, target, this);
		
		// dev note: this function may not work anymore, because the animation system now
		//			takes in AnimationData objects instead of individual information.
		//			at the very least, animation is probably not working
		
		//proj.startLoopingAnim(projectileAnim, 2, 3);
		proj.setHealthAdjust(getCastHealthAdjust());
		proj.setSpeedUpDuration(getCastSpeedAdjustDuration());
		proj.setSpeedUpMultiplier(getCastSpeedAdjustMultiplier());		
	}
	
	
	
	
	
	
	////////////////////////
	//                    //
	//      MOVEMENT      //
	//                    //
	////////////////////////
	
	
	/**
	 * Returns whether this object can be moved by the user.
	 * @return	True if the user can move the unit.
	 */
	public boolean canBeMoved()
	{
		return !isDead() && !isFrozen() && !isMoving();
	}
	
	
	/**
	 * @return	Returns true if the Tower is moving.
	 */
	public boolean isMoving()
	{
		return mySmoke.isActive();
	}
	
	

	

	
	////////////////////////////
	//                        //
	//      TILE-RELATED      //
	//                        //
	////////////////////////////
	
	
	/**
	 * Moves this Tower to a specific Tile via in-game movement.
	 * @param newTile		The Tile to move the Tower to.
	 * @return				Whether or not the Tower was actually moved.
	 */
	public boolean teleportTo(Tile newTile)
	{		
		// can it be moved by the player?
		if(!this.canBeMoved()) return false;
		
		// not allowed to not have a tile
		if(newTile == null) return false;
		
		// cannot take an occupied tile
		if(newTile.hasOccupant() || newTile.isReserved()) return false;
			
		// ACTIVATE THAT FREAKIN SMOKE
		mySmoke.activate(newTile);

		return true;
	}
	
	
	
	
	/**
	 * Puts this Tower at a specific Tile, instantaneously.
	 * @param newTile		The Tile to put the Tower at.
	 * @return				Whether or not the Tower was actually moved.
	 */
	public boolean setTile(Tile newTile)
	{		
		// cannot take an occupied tile
		if(newTile != null && newTile.hasOccupant()) return false;
		
		// if we're not moving, we don't even DESERVE a reserved tile
		if(newTile != null && newTile.isReserved() && !this.isMoving()) return false;
			
		// remove tower from old tile, if any
		if(this.tile != null)
		{
			this.tile.removeOccupant();
		}
		
		// update Tower reference to Tile and Tile reference to Tower
		boolean isAbleToPutTowerAtTile = true;		
		if(newTile != null)
		{
			newTile.setReserved(false);
			isAbleToPutTowerAtTile = newTile.setOccupant(this);
		}
		
		if(isAbleToPutTowerAtTile)
		{
			this.tile = newTile;
		}
		
		this.updateLocationToTile();
		return isAbleToPutTowerAtTile;
	}
	

	/**
	 * Return the Tile this Tower is occupying.
	 * @return		The Tile the Tower is at.
	 */
	public Tile getTile()
	{
		return tile;
	}
	
	
	/**
	 * Put me at my Tile!  The one I'm supposed to be at but might not be at.
	 */
	private void updateLocationToTile()
	{
		if(this.tile != null)
		{
			Vector2 loc = this.tile.getCenter();
			this.setCenter(loc);
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
		if(!resourcesArePreloaded)
		{
			HTL.resources.preloadSound(SOUND_SPAWN);
			HTL.resources.preloadSound(SOUND_MOVE);
			HTL.resources.preloadSound(SOUND_MOVE_FAIL);
			
			Smoke.preloadResources();
			Character.preloadResources();
			resourcesArePreloaded = true;
		}
	}
	
	
	
	/////////////////////////
	//                     //
	//      Animation      //
	//                     //
	/////////////////////////
	
	
	
	/**
	 * Starts an animation if needed.
	 */
	private void animUpdate()
	{
		if(this.isDead())	// Death animation
		{
			this.forceSingleAnim(this.getAnimationDeath());			
		}
		else if(isSelected)
		{
			this.startLoopingAnim(this.getAnimationSelected());
		}
		else 
		{
			this.startLoopingAnim(this.getAnimationIdle());
		}

	}
	
	
	
	/**
	 * Plays the Spellcast effect once.
	 */
	public void playEffectSpellcast()
	{
		this.effectSpellcast.setCenter(this.getCenter());
		this.effectSpellcast.play();
	}
	
	/////////////////////////
	//                     //
	//        Audio        //
	//                     //
	/////////////////////////	
	
	
	/**
	 * Plays the sound associated with tower spawning.
	 */
	public static void playSoundSpawn()
	{
		HTL.resources.playSound(SOUND_SPAWN);
	}
	
	
	/**
	 * Plays the sound associated with tower movement.
	 */
	public static void playSoundMove()
	{
		HTL.resources.playSound(SOUND_MOVE);
	}
	
	
	/**
	 * Plays the sound associated with a tower being unable to move
	 * to an occupied space.
	 */
	public static void playSoundMoveFail()
	{
		HTL.resources.playSound(SOUND_MOVE_FAIL);
	}
	
	
	
	
	
	
	/////////////////////////
	//                     //
	//     OVERRIDES       //
	//                     //
	/////////////////////////
	
	/**
	 * If we move to front, make sure smoke then moves to front so it's on top of us.
	 */
	public void moveToFront()
	{
		super.moveToFront();
		mySmoke.moveToFront();
		feedbackBadFeelsUp.moveToFront();
		feedbackBadFeelsDown.moveToFront();
		feedbackHealthUp.moveToFront();
		feedbackHealthDown.moveToFront();		
	}
	
	
	
	/**
	 * Note: moving should invalidate targetableness
	 * @return		True if this is a valid target.
	 */
	public boolean isValidTarget()
	{
		return super.isValidTarget() && !isMoving();
	}
	
	
	
	
	
	
	/**
	 * @return		True if the attack cooldown period for this Tower is over.
	 */
	public boolean cooldownIsReady()
	{
		return this.shotCooldown.isReady();
	}
	
	/**
	 * Restarts the attack cooldown timer with the previous cooldown value.
	 * Note that the cooldown timer is not innately tied to any
	 * Tower functionality, so it's up to you to decide what to do with this.
	 */
	public void cooldownStart()
	{
		this.shotCooldown.start();
	}
	
	/**
	 * Starts the attack cooldown timer and sets it to end after the
	 * specified number of seconds.
	 * Note that the cooldown timer is not innately tied to any
	 * Tower functionality, so it's up to you to decide what to do with this.
	 * @param seconds		How long until the cooldown timer will be ready.
	 */
	public void cooldownStart(float seconds)
	{
		this.shotCooldown.start(seconds);
	}
	
	
	
	
	/**
	 * @return		True if this Tower is considered to be selected by the user.
	 */
	public boolean isSelected()
	{
		return isSelected;
	}
	
	
	/**
	 * Sets whether this Tower is considered to be selected by the user.
	 * @param selectedness		True if selected by the user.
	 */
	public void setSelectedTo(boolean selectedness)
	{
		isSelected = selectedness;
	}
	

}
