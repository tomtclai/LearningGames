package TowerDefense;
import java.util.LinkedList;

import Engine.Vector2;

/**
 * A projectile is an object that is thrown by a Character.
 * They impact the target's health and badFeels.
 * 
 * NOTE:	May be a bit out of data, as the Hug The Line game hasn't used these for a few months.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */

public class Projectile extends GameObjectPlus
{	
	
	private static LinkedList<Projectile> projectileList = null;	
	
	private Destructible target = null;						// The target the projectile is being thrown at
	private Character creator = null;						// The Character throwing the projectile
	
	private static float speedDefault = .03f;				// The speed the projectile is moving across the screen	
	
	private boolean destroyMe = false;						// If the projectile should be destroyed
	
	private float targetHealthAdjust = 0;					// The health impact when hit by projectile	
	private float targetSpeedUpDuration = 0f;				// The amount of time the target will be sped up
	private float targetSpeedUpMultiplier = 1f;				// The multiplier to use when a target is sped up
	
	
	/**
	 * When a new projectile is created, it will automatically be placed
	 * in the list specified here.
	 * @param projectileList
	 */
	public static void setRepository(LinkedList<Projectile> projectileList)
	{
		Projectile.projectileList = projectileList;		
	}
	
	
	// may be a good idea to set this in units of something...
	/**
	 * Set the speed value of all projectiles.
	 * We never figured out the units this speed is in.
	 * Speed must be at least zero.
	 * @param speed
	 * @return		True if the speed was actually changed.
	 */
	public static boolean setSpeed(float speed)
	{
		if(speed <= 0) return false;
		
		Projectile.speedDefault = speed;
		return true;
	}
	
	/**
	 * @return	The default speed of projectiles.
	 */
	public static float getSpeed()
	{
		return Projectile.speedDefault;
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////	
	
	/**
	 * Constructor
	 * @param xLoc			X-coordinate of location.
	 * @param yLoc			Y-coordinate of location.
	 * @param width			Width in space.
	 * @param height		Height in space.
	 * @param target		The target of the projectile.
	 * @param creator		The Character throwing the projectile.
	 */
	public Projectile(float xLoc, float yLoc, float width, float height, Destructible target, Character creator)
	{
		super(xLoc, yLoc, width, height);
		draw();
		
		this.target = target;
		this.creator = creator;
		
		if(target == null)
		{
			randomizeVelocity();		
		}
		
		if(projectileList != null)
		{
			projectileList.add(this);
		}
	}
	
	///////////////////////////////
	//                           //
	//         Updates           //
	//                           //
	///////////////////////////////	
	
	
	/**
	 * Updates the projectile
	 */
	public void update()
	{	
		if(target != null && destroyMe == false)
		{
			// if target is already dead, let's get rid of projectile
			if(!target.isValidTarget())
			{
				destroyMe = true;
			}				
			// if projectile hits target, do stuff!
			else if(this.hasHitTarget())
			{				
				hitTarget();
			}
			
			updateVelocity();
		}

		super.update();
	}
	
	
	
	/**
	 * This function checks to see if the center of the target is
	 * within the bounds of the projectile's body.
	 * @return		True if the projectile is engulfing the target's center.
	 */
	private boolean hasHitTarget()
	{
		float targetX = target.getCenterX();
		float targetY = target.getCenterY();
		
		float myLeftPosition = this.getCenterX() - getWidth() / 2;
		float myRightPosition = this.getCenterX() + getWidth() / 2;
		
		float myTopPosition= this.getCenterY() + getHeight() / 2;
		float myBottomPosition = this.getCenterY() - getHeight() / 2;
		
		if(targetX < myLeftPosition || targetX > myRightPosition) return false;
		if(targetY < myBottomPosition || targetY > myTopPosition) return false;
		
		return true;
	}
	
	
	
	
	
	/**
	 * Updates the velocity of the projectile
	 */
	private void updateVelocity()
	{
		Vector2 here = this.getCenter();
		Vector2 there = target.getCenter();
		
		float yVel = there.getY() - here.getY();		
		float xVel = there.getX() - here.getX();
		
		Vector2 vector = new Vector2(xVel, yVel);
		vector.normalize();
		
		float mySpeed = speedDefault;
		/*
		if(fastMode)
		{
			mySpeed *= 10;
		}
		*/
		vector.mult(mySpeed);		
		
		this.setVelocity(vector);		
	}
	
	
	
	///////////////////////////////
	//                           //
	//    BadFeels & Health      //
	//                           //
	///////////////////////////////	
	
	
	/**
	 * Sets the amount of health that should be lost by target when projectile hits it
	 * @param val	The amount the health of the target should be adjusted when hit.
	 */
	public void setHealthAdjust(float val)
	{
		targetHealthAdjust = val;
	}
	
	
	/**
	 * If this projectile hits a target, the target's speed will be
	 * multiplied by val.
	 * @param val
	 */
	public void setSpeedUpMultiplier(float val)
	{
		targetSpeedUpMultiplier = val;
	}
	
	/**
	 * If this projectile hits a target, the target's speed will be
	 * adjusted for this amount of time.
	 * @param time
	 */
	public void setSpeedUpDuration(float time)
	{
		targetSpeedUpDuration = time;
	}
	
	
	
	//////////////////////
	//					//
	//  Impact Targets	//
	//					//
	//////////////////////
	
	
	/**
	 * Do the stuff that should happen when the target is hit.
	 * Adjust the health and badFeels appropriately.
	 */
	private void hitTarget()
	{
		// should we let shooter know we hit their target?
		
		// only Characters have feelings
		if(target instanceof Walker)
		{
			Walker tempTarget = (Walker)target;
			tempTarget.applySpeedBuff(targetSpeedUpMultiplier, targetSpeedUpDuration);
		}
		target.addHealth(targetHealthAdjust);
		
		// if the target is dead or pacified, maybe there are repercussions
		creator.reactToAffectingTarget(target);
		
		this.setVisibilityTo(false);		
		destroyMe = true;
	}
	
	
	
	//////////////////////
	//					//
	//     Movement		//
	//					//
	//////////////////////
	
	
	/**
	 * Varies the velocity of projectiles.
	 */
	private void randomizeVelocity()
	{
		float randomX = (float)(0.5f - Math.random());
		float randomY = (float)(0.5f - Math.random());			
		
		Vector2 vector = new Vector2(randomX, randomY);
		vector.normalize();
		
		float mySpeed = speedDefault;
		vector.mult(mySpeed);		
		
		this.setVelocity(vector);	
	}
	
	
	

	
	
	
	//////////////////////
	//					//
	//     Deleting		//
	//					//
	//////////////////////
	
	
	/**
	 * Checks if the projectile should be destroyed.
	 * The lists that contain Projectiles are responsible for checking this and then removing them.
	 * @return		True if the projectile has lived out its usefulness
	 */
	public boolean shouldBeDestroyed()
	{
		return destroyMe;
	}

}
