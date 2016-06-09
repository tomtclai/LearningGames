package TowerDefense;
import java.awt.Color;

import Engine.DrawingLayer;
import Engine.GameObject;

/**
 * Characters are GameObjects that represent living beings.
 *
 * In addition to the traits of Destructibles, Characters have:
 * 		- targets
 * 			When a Character wants to do something, they typically are going to do it TO something.
 * 			Targets help them keep track of what they are doing it to.
 * 		- badFeels 
 * 			People can experience negative emotions, and badFeels are Characters' representation of this.
 * 			At least at this early design phase, badFeels can represent different kinds of feelings
 * 			depending on the Character.
 * 
 * NOTE:
 * 		Long-term targets and badFeels stopped being used in the main Hug the Line game,
 * 		and hence they have not been tested in awhile.
 * 		Characters are still useful as they have a lot of methods that mobile objects
 * 		with important statistical information will want to use.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public abstract class Character extends Destructible
{	
	public static final float BAD_FEELS_MAX = 100;							// maximum possible badFeels
	public static final float BAD_FEELS_MIN = 0;							// minimum possible badFeels
	
	public static final String IMAGE_HEALTH_BORDER = "art/Characters/HTL_Walker_LifeContainer2.png";
	
	private static DrawingLayer layerHealth = null;							// DrawingLayer to draw health visuals in
	
	private float badFeels = BAD_FEELS_MIN;									// current badFeels
	
	private float badFeelsUpMultiplier = 1;									// multiplier for "bad feels" being received
	private float badFeelsDownMultiplier = 1;								// multiplier for "good feels" being received

	private float badFeelsDownCap = Float.NEGATIVE_INFINITY;				// largest allowable value for "good feels" being received
	private float badFeelsUpCap = Float.POSITIVE_INFINITY;					// largest allowable value for "bad feels" being received
	
	private Destructible target = null;										// what I want to do stuff to
	
	private CooldownTimer remainingTimeFrozen = new CooldownTimer(0);		// this will be ready when the freeze should end
	
	// this stuff should probably become its own object sometime later
	private GameObject healthVisual;							// this is the object that displays health (bar, icon, etc)
	private Direction statVisualDirection = Direction.UP;		// where should the stats be in relation to character?
	public static final float STAT_VISUAL_DISTANCE_BORDER = .6f;		// how far away should the stats UI be from the character?
	public static final float STAT_VISUAL_DISTANCE_COLOR = STAT_VISUAL_DISTANCE_BORDER + .075f;		
	
	
	protected GameObject healthVisualBorder;
	
	
	
	// This stuff isn't really used right now.  Bad Feels isn't in the game no more.
	//The pictures that rise above the character's head when there is an adjustment in stats.
	protected GameObject feedbackBadFeelsUp; 
	protected GameObject feedbackBadFeelsDown;	
	public static final String IMAGE_BAD_FEELS_UP = "faceUnhappy.png";	
	public static final String IMAGE_BAD_FEELS_DOWN = "faceHappy.png";
	protected boolean hasBadFeelsAnimations = false;
	
	
	
	
	
	
	/**
	 * From now on, all newly spawned Characters will have their health bar
	 * placed in the specified DrawingLayer.
	 * Enter null to use the default AutoDraw set.
	 * @param drawingLayer		The DrawingLayer.
	 */
	public static void setDrawingLayerForHealth(DrawingLayer drawingLayer)
	{		
		layerHealth = drawingLayer;
	}
	
	
	/**
	 * Preload resources associated with this object.
	 */
	public static void preloadResources()
	{
		HTL.resources.preloadImage(IMAGE_HEALTH_BORDER);
	}
	
	
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////
	
	
	/**
	 * Constructor.
	 * @param xLoc					X-location of this character.
	 * @param yLoc					Y-location of this character.
	 * @param width					Width of the character.
	 * @param height				Height of the character.
	 */
	 public Character(float xLoc, float yLoc, float width, float height)
	{
		super(xLoc, yLoc, width, height);
		
		
		// this stuff should probably become its own object sometime later		
		healthVisual = new GameObject(-1, -1, .25f, .25f);
		healthVisual.setColor(calculateHealthColor());
		
		// calculate size in-game from image pixel size to prevent distortion
		float hvbWidth = 25f/HTL.PIXELS_PER_GAME_UNIT;
		float hvbHeight = 35f/HTL.PIXELS_PER_GAME_UNIT;		
		healthVisualBorder = new GameObject(-1, -1, hvbWidth, hvbHeight);
		healthVisualBorder.setImage(IMAGE_HEALTH_BORDER);
		
		if(layerHealth != null)
		{
			healthVisual.moveToDrawingLayer(layerHealth);
			healthVisualBorder.moveToDrawingLayer(layerHealth);
		}
		
		
		// Floating animations when there are changes in stats (currently not in use)
		feedbackBadFeelsUp = new GameObject(-7, -7, .5f,.5f);
		feedbackBadFeelsDown = new GameObject(-7, -7, .5f,.5f);
		feedbackBadFeelsUp.setImage(IMAGE_BAD_FEELS_UP);
	    feedbackBadFeelsDown.setImage(IMAGE_BAD_FEELS_DOWN);

	}
	
	 
	 
	/**
	 * Destructor.
	 */
	public void destroy()
	{
		target = null;
		
		healthVisual.destroy();
		healthVisual = null;
		
		healthVisualBorder.destroy();
		healthVisualBorder = null;
		
		
		if(feedbackBadFeelsUp != null)
		{
			feedbackBadFeelsUp.destroy();
			feedbackBadFeelsUp = null;
		}
		
		if(feedbackBadFeelsDown != null)
		{
			feedbackBadFeelsDown.destroy();
			feedbackBadFeelsDown = null;
		}
		
		super.destroy();
	}
	
	
	///////////////////////////
	//                       //
	//  METHODS TO OVERRIDE  //
	//                       //
	///////////////////////////	
	
	
	
	/**
	 * When this Character murders something, here's what should happen.
	 * Meant to be overridden.
	 */
	public void reactToMurder()
	{
		// nothing by default
	}
	
	
	/**
	 * When this Character deters a foe (gets its badFeels all the way down), here's what should happen.
	 * Meant to be overridden.
	 */
	public void reactToDeter()
	{
		// nothing by default
	}
	
	
	/////////////////////////
	//                     //
	//       UPDATES       //
	//                     //
	/////////////////////////
	
	
	/**
	 * Updates this object.
	 */
	public void update()
	{	
		if(isDestroyed())
		{
			return;
		}
		
		updateStatVisuals();		
		updateBadFeelsUpAni();		
		updateBadFeelsDownAni();
		
		super.update();
	}
	
	
	////////////////////////
	//                    //
	//   TARGET METHODS   //
	//                    //
	////////////////////////
	
	
	/**
	 * Return reference to target.
	 * @return 			My target.
	 */
	public Destructible getTarget()
	{
		return this.target;
	}
	
	
	/**
	 * Set the target.
	 * @param target 	The unit to target.
	 */
	public void setTarget(Destructible target)
	{
		this.target = target;
	}
	
	
	/**
	 * Return true if we have a target.
	 * @return 	True if we have a target.
	 */
	public boolean hasTarget()
	{
		return this.target != null;
	}
	
	
	/**
	 * Returns true if I have a target AND it is valid.
	 * @return 	True if there is a target and it is valid.
	 */
	public boolean hasValidTarget()
	{
		return hasTarget() && target.isValidTarget();
	}
	
	
	/**
	 * Returns true if I have a target AND it is on my team.
	 * @return 	True if I have a target AND it is on my team.
	 */
	public boolean onSameTeamAsTarget()
	{
		return hasTarget() && this.onSameTeamAs(this.target);
	}
	
	
	/////////////////////////
	//                     //
	//  BAD FEELS METHODS  //
	//                     //
	/////////////////////////
	
	
	/**
	 * @return 	My badFeels value.
	 */
	public float getBadFeels()
	{
		return this.badFeels;
	}
	
	
	/**
	 * Set badFeels value.
	 * If the value is out of range, the value will be adjusted to be within the range.
	 * @param distress		The value to set badFeels to.
	 */
	public void setBadFeels(float distress)
	{
		this.badFeels = distress;
		boundBadFeels();
	}	
	
	
	/**
	 * Returns a value from 0.0 to 1.0 representing the ratio of current badFeels relative to max possible badFeels.
	 * @return 				current badFeels / max badFeels
	 */
	public float getBadFeelsProportion()
	{
		return this.badFeels/Character.BAD_FEELS_MAX;
	}
	
	
	/**
	 * Add badFeels to this object.  Negative amounts are acceptable ("good feels").
	 * The amount the object's badFeels is adjusted by is itself adjusted by several factors.
	 * @param amount	The amount of badFeels to add.
	 */
	public void addBadFeels(float amount)
	{
		if(amount > 0)
		{
			amount *= badFeelsUpMultiplier;
			
			if(amount > badFeelsUpCap)
			{
				amount = badFeelsUpCap;
				
			}
			
			if(hasBadFeelsAnimations)
			{
				displayBadFeelsUpAnimation();
			}
			
		}		
		else if(amount < 0)
		{
			amount *= badFeelsDownMultiplier;
			
			if(amount < badFeelsDownCap)
			{
				amount = badFeelsDownCap;
			}
			
			if(hasBadFeelsAnimations)
			{
				displayBadFeelsDownAnimation();
			}
		}
		
		
		badFeels += amount;
		boundBadFeels();
		
		
	}
	
	
	/**
	 * Sets the multiplier for increasing badFeels.
	 * @val		The value to set the multiplier to.
	 */
	public void setBadFeelsUpMultiplier(float val)
	{
		this.badFeelsUpMultiplier = val;
	}	
	
	/**
	 * Sets the multiplier for decreasing badFeels.
	 * @val		The value to set the multiplier to.
	 */
	public void setBadFeelsDownMultiplier(float val)
	{
		this.badFeelsDownMultiplier = val;
	}
	
	
	/**
	 * Multiplies the current multiplier for increasing badFeels.
	 * @val		The value to multiply the current multiplier by.
	 */
	public void multiplyBadFeelsUpMultiplier(float val)
	{
		this.badFeelsUpMultiplier *= val;
	}	
	
	
	/**
	 * Multiplies the current multiplier for decreasing badFeels.
	 * @val		The value to multiply the current multiplier by.
	 */
	public void multiplyBadFeelsDownMultiplier(float val)
	{
		this.badFeelsDownMultiplier *= val;
	}
	
	
	/**
	 * Sets the magnitude of the maximum allowable decrease in badFeels.
	 * @float val		The value to set the magnitude to.
	 */
	public void setBadFeelsDownCap(float val)
	{
		this.badFeelsDownCap = -Math.abs(val);
	}
	
	
	/**
	 * Sets the magnitude of the maximum allowable increase in badFeels.
	 * @float val		The value to set the magnitude to.
	 */
	public void setBadFeelsUpCap(float val)
	{
		this.badFeelsUpCap = Math.abs(val);
	}
	
	
	/**
	 * Resets the maximum allowable decrease in badFeels to its original value.
	 */
	public void resetBadFeelsDownCap()
	{
		this.badFeelsDownCap = Float.NEGATIVE_INFINITY;
	}
	
	
	/**
	 * Resets the maximum allowable increase in badFeels to its original value.
	 */
	public void resetBadFeelsUpCap()
	{
		this.badFeelsUpCap = Float.POSITIVE_INFINITY;
	}

	
	/**
	 *  Makes sure that current badFeels is never outside the bounds of badFeelsMax and badFeelsMin.
	 */
	private void boundBadFeels()
	{
		if(badFeels > BAD_FEELS_MAX)
		{
			badFeels = BAD_FEELS_MAX;
		}
		else if (badFeels < BAD_FEELS_MIN)
		{
			badFeels = BAD_FEELS_MIN;
		}
	}
	
	
	/**
	 * Simple check for maxed bad feels
	 */
	public boolean hasMaxBadFeels()
	{
		return badFeels >= BAD_FEELS_MAX;
	}
	
	
	/**
	 * Simple check for min bad feels
	 */
	public boolean hasMinBadFeels()
	{
		return badFeels <= BAD_FEELS_MIN;
	}
	
	
	
	
	
	
	
	
	
	/////////////////////////
	//                     //
	//   FREEZE METHODS    //
	//                     //
	/////////////////////////
	
	
	/**
	 * Sets this character to be frozen (can't attack or move) for a time.
	 * If a character was already frozen, the old duration is replaced by this new one.
	 * @param secondsFrozen		The number of seconds this character should be frozen.
	 */
	public void freezeMe(float secondsFrozen)
	{		
		remainingTimeFrozen.start(secondsFrozen);		
	}	


	/**
	 * Returns true if the character is currently frozen.
	 * @return 	True if frozen.
	 */
	public boolean isFrozen()
	{
		return !remainingTimeFrozen.isReady();
	}
	

	/////////////////////////
	//                     //
	//   OTHER METHODS     //
	//                     //
	/////////////////////////
	
	
	/**
	 * When a Character affects its target, this is called to check whether the
	 * target has been killed or deterred.  If so, call the appropriate reactions.
	 * Note that the target of the attack might not necessarily be the official target of this Character.
	 * @param theTarget		The target that this unit has attacked.
	 */
	public void reactToAffectingTarget(Destructible theTarget)
	{
		if(theTarget.isDead())
		{
			this.reactToMurder();
		}	
	}
	

	


		

	///////////////////////////////
	//                           //
	//     STAT HUD METHODS      //
	//                           //
	///////////////////////////////
	

	/**
	 * Sets whether the HUD that shows health and badFeels for the character is visible.
	 * @param isVisible		Whether the stats should be visible or not.
	 */
	public void setStatsVisible(boolean isVisible)
	{
		healthVisual.setVisibilityTo(isVisible);
		healthVisualBorder.setVisibilityTo(isVisible);
	}
	
	
	/**
	 * Updates the health/badFeels HUD that appears over Characters' heads.
	 * This also updates bad feels animation
	 */
	private void updateStatVisuals()
	{
		if(this.isDead())
		{
			setStatsVisible(false);	// dead things shouldn't clutter screen real estate with stats
		}
		else
		{
			// update HUD colors
			healthVisual.setColor(calculateHealthColor());
			
			// relocate HUD to the appropriate position near the character
			float borderX = this.getCenterX();
			float borderY = this.getCenterY();
			float colorX = this.getCenterX();
			float colorY = this.getCenterY();
			
			switch(statVisualDirection)
			{
				case UP:		borderY += STAT_VISUAL_DISTANCE_BORDER;
								colorY += STAT_VISUAL_DISTANCE_COLOR;
								healthVisualBorder.setRotation(0);
								break;
				case DOWN:		borderY -= STAT_VISUAL_DISTANCE_BORDER;
								colorY -= STAT_VISUAL_DISTANCE_COLOR;
								healthVisualBorder.setRotation(180);
								break;
				case LEFT:		borderX -= STAT_VISUAL_DISTANCE_BORDER;
								colorX -= STAT_VISUAL_DISTANCE_COLOR;
								healthVisualBorder.setRotation(90);
								break;
				default:		borderX += STAT_VISUAL_DISTANCE_BORDER;
								colorX += STAT_VISUAL_DISTANCE_COLOR;
								healthVisualBorder.setRotation(270);
			}
			
			healthVisual.setCenter(colorX, colorY);
			healthVisualBorder.setCenter(borderX, borderY);
		}

		healthVisual.update();
	}
	
	
	/**
	 * Set the direction in which the character's stats should appear,
	 * relative to the character.
	 * @param d		The direction!
	 */
	public void setStatVisualDirection(Direction d)
	{
		if(d != null)
		{
			statVisualDirection = d;
		}
	}
	
	
	

	


	/**
	 *  Returns the appropriate color for the badFeels HUD.
	 *  Sets the color of the badFeelsVisual, such that it goes from white (min) to dark purple (max)
	 */
	private Color calculateBadFeelsColor()
	{
		int redBlueMin = 128;
		int redGreenBlueMax = 255;
		
		float colorFraction = (BAD_FEELS_MAX - badFeels)* 1f / BAD_FEELS_MAX;
		
		int currentGreen = (int)(colorFraction * redGreenBlueMax);
		int currentRedBlue = (int)(colorFraction * (redGreenBlueMax - redBlueMin) + redBlueMin);
		
		return new Color(currentRedBlue, currentGreen, currentRedBlue);
	}
	
	
	/**
	 *  Returns the appropriate color for the health HUD.
	 *  Sets the color of the healthVisual, such that it goes from green (max) to yellow (mid) to red (min).
	 */
	private Color calculateHealthColor()
	{
		int greenMax = 255;		
		int redMax = 255;
		
		float colorFraction = this.getHealthProportion();
		
		// decide whether to go from green to yellow OR yellow to red
		if (colorFraction > .5f)
		{
			// this section is just for green to yellow
			
			// rescale colorFraction so that it is from 0.0 - 1.0, not 0.5 - 1.0
			colorFraction = (colorFraction - .5f) * 2;
			// 1.0 = no red, 0.0 = big red
			int currentRed = (int)(redMax - (colorFraction * redMax));
			
			// check to make sure floating point errors don't push us outside 0-255 range
			if(currentRed > redMax)
			{
				currentRed = redMax;
			}
			else if (currentRed < 0)
			{
				currentRed = 0;
			}
			
			return new Color(currentRed , greenMax, 0);
		}
		else
		{
			// this section is just for yellow to red
			
			// rescale colorFraction so that it is from 0.0 - 1.0, not 0.0 - 0.5
			colorFraction = colorFraction * 2;
			// 1.0 = big green, 0.0 = no green
			int currentGreen = (int)(colorFraction * greenMax);
			
			// check to make sure floating point errors don't push us outside 0-255 range
			if(currentGreen > greenMax)
			{
				currentGreen = greenMax;
			}
			else if (currentGreen < 0)
			{
				currentGreen = 0;
			}
			
			return new Color(redMax , currentGreen, 0);
		}
	}	
	
	
	
	
	
	/**
	 * Not really sure, we never used this. <3
	 */
	private void updateBadFeelsUpAni()
	{
		if(this.distanceTo(this.feedbackBadFeelsUp) > 1)
		{
			this.feedbackBadFeelsUp.setVelocityY(0f);
			this.feedbackBadFeelsUp.setToInvisible();
		}
		this.feedbackBadFeelsUp.update();
	}
	
	/**
	 * Not really sure, we never used this. <3
	 */
	private void updateBadFeelsDownAni()
	{
		if(this.distanceTo(this.feedbackBadFeelsDown) > 1)
		{
			this.feedbackBadFeelsDown.setVelocityY(0f);
			this.feedbackBadFeelsDown.setToInvisible();
		}
		this.feedbackBadFeelsDown.update();
	}
	
	/**
	 * Not really sure, we never used this. <3
	 */
	private void displayBadFeelsUpAnimation()
	{
		feedbackBadFeelsUp.setCenter(this.getCenterX(), this.getCenterY()); 
	    feedbackBadFeelsUp.setVelocityY(.05f);	
	    
	}
	
	/**
	 * Not really sure, we never used this. <3
	 */
	private void displayBadFeelsDownAnimation()
	{
		feedbackBadFeelsDown.setCenter(this.getCenterX(), this.getCenterY());	   
	    feedbackBadFeelsDown.setVelocityY(.05f);	
	}
	
	
	
	
	
}
