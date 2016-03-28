package TowerDefense;
import Engine.GameObject;



/**
 * Destructibles are GameObjects that can be destroyed.
 * 
 * This includes Barriers and even things like PEOPLE.  Our existence is so fragile.
 * 
 * Traits of destructibles are:
 * 		- health (could be considered durability if you enjoy semantics)
 * 		- a team (which lets others know whether they WANT to destroy it)
 * 
 *  @author Branden Drake
 *  @author Rachel Horton
 */
public abstract class Destructible extends GameObjectPlus
{	
	private float healthMax = 100;											// maximum possible health
	private float healthMin = 0;											// minimum possible health
	private float health = healthMax;										// current health of the object
	
	private float healthUpMultiplier = 1;					// multiplier for "healing" being received
	private float healthDownMultiplier = 1;					// multiplier for "damage" being received
	
	private float healthDownCap = Float.NEGATIVE_INFINITY;	// largest allowable value for "damage" being received
	private float healthUpCap = Float.POSITIVE_INFINITY;	// largest allowable value for "healing" being received
	
	private int deathDurationInFrames = 0;		// how many frames has this object been dead?
	
	//Pictures that will animate above their head as health changes.  (currently not used)
	protected GameObject feedbackHealthUp;
	protected GameObject feedbackHealthDown;	
	public static final String IMAGE_HEALTH_UP = "healthUp.png";	
	public static final String IMAGE_HEALTH_DOWN = "healthDown.png";
	
	protected boolean hasHealthAnimations = true;
	
	
	private String team = null;
	
	
	
	
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////
	
	/**
	 * Constructor.
	 * @param xLoc		X-coordinate of this object.
	 * @param yLoc		Y-coordinate of this object.
	 * @param width		Width of this object.
	 * @param height	Height of this object.
	 */
	public Destructible(float xLoc, float yLoc, float width, float height)
	{
		super(xLoc, yLoc, width, height);
		feedbackHealthUp = new GameObject(-7, -7, .5f, .5f);
		feedbackHealthDown = new GameObject(-7, -7, .5f, .5f);
		feedbackHealthDown.setImage(IMAGE_HEALTH_DOWN);
		feedbackHealthUp.setImage(IMAGE_HEALTH_UP);
	}
	
	
	/**
	 * Destructor.
	 */
	public void destroy()
	{
		if(feedbackHealthUp != null)
		{
			feedbackHealthUp.destroy();
			feedbackHealthUp = null;
		}
		
		if(feedbackHealthDown != null)
		{
			feedbackHealthDown.destroy();
			feedbackHealthDown = null;
		}
		
		super.destroy();
	}
	
	
	
	
	///////////////////////////
	//                       //
	//  METHODS TO OVERRIDE  //
	//                       //
	///////////////////////////	
	
	
	/**
	 * If receiving damage should result in any other logic or behavior from this Destructible,
	 * this is a place for that.
	 * Meant to be overridden.
	 */
	protected void extraEffectsFromDamage()
	{
		// nothing by default
	}
	
	
	/**
	 * If dying should result in any other logic or behavior from this Destructible,
	 * this is a place for that.
	 * Meant to be overridden.
	 */
	protected void reactToOwnDeath()
	{
		// nothing by default
	}
	
	
	////////////////////////
	//                    //
	//      UPDATE        //
	//                    //
	////////////////////////
	
	
	/**
	 * Regularly updates this object.
	 */
	public void update()
	{	
		if(isDestroyed())
		{
			return;
		}
		
		super.update();
		
		updateDeathDuration();
		
		updateHealthUpAni();		
		updateHealthDownAni();
	}
	
	
	/**
	 * Tracks frames this unit has been dead so that we know when we JUST died.
	 * Note that in exceedingly long gameplay sessions, there could be overflow issues.
	 */
	private void updateDeathDuration()
	{
		if(!isDead())
		{
			deathDurationInFrames = 0;
		}
		else
		{
			deathDurationInFrames++;
		}
	}
	
	

	
	////////////////////////
	//                    //
	//   TARGET METHODS   //
	//                    //
	////////////////////////
	
	
	/**
	 * Returns true if this Destructible is currently a valid target.
	 */
	public boolean isValidTarget()
	{
		return !isDead();
	}

	
	////////////////////////
	//                    //
	//    TEAM METHODS    //
	//                    //
	////////////////////////
	
	
	/**
	 * Set the team; team names are represented by Strings.
	 * @param team		The team name.
	 */
	public void setTeam(String team)
	{
		this.team = team;
	}
	
	/**
	 * Return the String representation of the team.
	 * @return 			The current team name.
	 */
	public String getTeam()
	{
		return team;
	}
	
	/**
	 * Returns true if this Destructible is on the same team as unit.
	 * @param unit			The other Destructible.
	 */
	public boolean onSameTeamAs(Destructible unit)
	{
		return this.team.equals(unit.getTeam());
	}
	
	
	////////////////////////
	//                    //
	//   HEALTH METHODS   //
	//                    //
	////////////////////////
	
	
	/**
	 * Return current health value.
	 * @return 	This object's current health.
	 */
	public float getHealth()
	{
		return this.health;
	}
	
	/**
	 * Set current health to a specific value.
	 * If the value is out of range, the value will be adjusted to be within the range.
	 * @param health	The health to set this object to.
	 */
	public void setHealth(float health)
	{
		this.health = health;
		boundHealth();
	}	
	
	/**
	 * Returns a value from 0.0 to 1.0 representing the ratio of current health relative to max possible health.
	 * @return 		The current fraction of health.
	 */
	public float getHealthProportion()
	{
		// because healthMin might not always be zero, we do some math.
		float healthRange = healthMax - healthMin;
		float currentHealthWithinRange = this.health - healthMin;
		return currentHealthWithinRange/healthRange;		
	}
	
	/**
	 * Add health to this object.  Negative amounts are acceptable (damage).
	 * The amount the object's health is adjusted by is itself adjusted by several factors.
	 * If there are any effects from receiving damage or dying, this method will call them.
	 * @param amount	The amount of health to add to this object.	
	 */
	public void addHealth(float amount)
	{
		if(amount > 0)
		{
			amount *= healthUpMultiplier;
			
			if(amount > healthUpCap)
			{
				amount = healthUpCap;
			}

			if(this.hasHealthAnimations)
			{
				displayHealthUpAnimation();
			}
		}		
		else if(amount < 0)
		{
			amount *= healthDownMultiplier;
			
			if(amount < healthDownCap)
			{
				amount = healthDownCap;
			}
			
			if(this.hasHealthAnimations)
			{
				displayHealthDownAnimation();
			}
			
			extraEffectsFromDamage();
		}
		
		health += amount;
		boundHealth();
		
		if(this.getHealthProportion() <= 0)
		{
			this.reactToOwnDeath();
		}
	}	
	
	
	
	
	private void displayHealthUpAnimation()
	{
		feedbackHealthUp.setCenter(this.getCenterX(), this.getCenterY());			    
		feedbackHealthUp.setVelocityY(.07f);	
	}
	
	
	private void displayHealthDownAnimation()
	{
		feedbackHealthDown.setCenter(this.getCenterX(), this.getCenterY());			    
	    feedbackHealthDown.setVelocityY(.07f);	
	}
	
	
	/**
	 * Returns true if health is at or below the minimum threshold.
	 * @return 	Returns true if the object's health is below the min.
	 */
	public boolean isDead()
	{
		return health <= healthMin;
	}	
	
	
	public boolean hasJustDied()
	{
		return deathDurationInFrames == 1;
	}
	
	
	
	
	/**
	 * Sets the multiplier for "healing".
	 * @param val	The number to multiply all healing by.
	 */
	public void setHealthUpMultiplier(float val)
	{
		this.healthUpMultiplier = val;
	}	
	
	/**
	 * Sets the multiplier for "damage".
	 * @param val	The number to multiply all damage by.
	 */
	public void setHealthDownMultiplier(float val)
	{
		this.healthDownMultiplier = val;
	}
	
	/**
	 * Multiplies the current "healing" multiplier.
	 * @param val	The number to multiply the current healing multiplier by.
	 */
	public void multiplyHealthUpMultiplier(float val)
	{
		this.healthUpMultiplier *= val;
	}
	
	/**
	 * Multiplies the current "damage" multiplier.
	 * @param val	The number to multiply the current damage multiplier by.
	 */
	public void multiplyHealthDownMultiplier(float val)
	{
		this.healthDownMultiplier *= val;
	}
	
	/**
	 * Sets the magnitude of the maximum "damage" allowable.
	 * @param val	The maximum amount of damage this object can take at one time. 
	 */
	public void setHealthDownCap(float val)
	{
		this.healthDownCap = -Math.abs(val);
	}
	
	/**
	 * Sets the magnitude of the maximum "healing" allowable.
	 * @param val	The maximum amount of healing this object can receive at one time.
	 */
	public void setHealthUpCap(float val)
	{
		this.healthUpCap = Math.abs(val);
	}
	
	/**
	 * Resets the maximum "damage" allowable.
	 */
	public void resetHealthDownCap()
	{
		this.healthDownCap = Float.NEGATIVE_INFINITY;
	}
	
	/**
	 * Resets the maximum "healing" allowable.
	 */
	public void resetHealthUpCap()
	{
		this.healthUpCap = Float.POSITIVE_INFINITY;
	}
	
	
	/**
	 *  Makes sure that current health is never outside the bounds of healthMax and healthMin.
	 */
	private void boundHealth()
	{
		if(health > healthMax)
		{
			health = healthMax;
		}
		else if (health < healthMin)
		{
			health = healthMin;
		}
	}
	
	/**
	 * @return		True if health is full.
	 */
	public boolean hasMaxHealth()
	{
		return health >= healthMax;
	}
	
	
	/**
	 * @return		True if health is empty.
	 */
	public boolean hasMinHealth()
	{
		return health <= healthMin;
	}
	
	
	/**
	 * @param max	The highest value health is allowed to reach.
	 */
	public void setHealthMax(float max)
	{
		healthMax = max;
		if(health > healthMax)
		{
			health = healthMax;
		}
	}
	
	
	/**
	 * @return		The highest value health is allowed to reach.
	 */
	public float getHealthMax()
	{
		return healthMax;
	}
	
	
	
	

	
	
	/**
	 * Old and busted method for making a health up animation.
	 */
	private void updateHealthUpAni()
	{
		if(this.distanceTo(this.feedbackHealthUp) > 1)
		{
			this.feedbackHealthUp.setVelocityY(0f);
			this.feedbackHealthUp.setToInvisible();
		}
		this.feedbackHealthUp.update();
	}
	
	/**
	 * Old and busted method for making a health down animation.
	 */
	private void updateHealthDownAni()
	{
		if(this.distanceTo(this.feedbackHealthDown) > 1)
		{
			this.feedbackHealthDown.setVelocityY(0f);
			this.feedbackHealthDown.setToInvisible();
		}
		this.feedbackHealthDown.update();
	}
}
