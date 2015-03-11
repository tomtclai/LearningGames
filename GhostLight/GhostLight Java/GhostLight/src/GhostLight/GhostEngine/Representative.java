package GhostLight.GhostEngine;

import GhostLight.Interface.InteractableObject;

/**
 * Used to communicate commands beteen BaseEnemyes on the Engin side and InteractableObjects on the User side
 * Note this class must always be created as a InteractableObject object and accessed as its super class
 * Never as just itself
 * @author Michael Letter
 */
public abstract class Representative {
	/**Stores the current Column of the Representative is within the array*/
	int currentCollumn = 0;
	/** Stores the current row of the Representative is within the array */
	int currentRow = 0;
	
	/** Maintains communication with the BaseEnemy that this Representative Represents */
	BaseEnemy subject = null;
	
	/** Determines hat Type of Object this represents */
	InteractableObject.ObjectType type = InteractableObject.ObjectType.PUMPKIN;
	/** Whether or not this object is revealed */
	private boolean revealed = false;
	/** whether or not this object is partial revealed */
	private boolean partialRevealed = false;
	/** Stores the currentHealth of the object */
	private int currentHealth = 0;
	/** Stores the Maximum possible health the object can have */
	private int maxHealth = 0;
	/** Whether or not this object is infected (Can only be removed by Beam) */
	private int infectedTimer = 0;
	/** Whether or not this object will be removed if its health reaches zero */
	private boolean invulnerable = false;
	/** Whether or not this object is highlighted */
	private boolean highlighted = false;
	/** the number of points this object is worth */
	private int score = InteractableObject.getDefualtScore(type);
	/** Stored to prevent reddent news */
	private StateChange storedStateChangeObject = new StateChange();
	
	/**
	 * Will Return the BaseEnemy this enemy represents
	 * @return the Enemy this Represents
	 */
	BaseEnemy getRepresentedEnemy(){
		return subject;
	}
	//setters
	/**
	 * Will set whether or not this object is highLighted
	 * @param highlighted If TRUE, this object will have a graphic drawn over it to distinguish it from similar Objects. If FALSE, it will be drawn normally
	 */
	public void setHighlight(boolean highlighted){
		this.highlighted = highlighted;
	}
	/**
	 * If given boolean is true then this objects health will not be destroyed when its health reaches zero.
	 * If the given boolean is false then when this objects health reaches zero it will be distroyed
	 * @param invulnerable the given boolean
	 */
	public void setVulnerability(boolean invulnerable){
		this.invulnerable = invulnerable;
	}
	/**
	 * Will set the infect Timer of this object to the given amount this will mark the object as infected for that many updates
	 * @param newInfectStatus the requested number of turns this object will be marked as infected. Note, this request must be greater than or equal to zero
	 * @return will return true if the requested changes to the InfectStatus were made. otherwise returns false. In this case no changes were made
	 */
	public boolean setInfectStatus(int newInfectStatus){
		if(newInfectStatus >= 0){
			infectedTimer = newInfectStatus;
			return true;
		}
		return false;
	}
	/**
	 * Will Destroy this Object call if you are going to remove this from from the array
	 */
	public void destroy(){
		if(subject != null){
			storedStateChangeObject.add(subject.scare());
			subject = null;
		}
	}/**
	 * Will Create BaseEnemy that this Representative will represent
	 * @return
	 */
	protected boolean setType(InteractableObject.ObjectType newType){
		if(newType != null){
			type = newType;
			return false;
		}
		return true;
	}
	/**
	 * Will Reveal or unreveal this object
	 * @param if true will reveal this object if it is not alredy revealed
	 * 		  if false will unreveal this object if not currently revealed
	 */
	public void setRevealStatus(boolean newStatus){
		revealed = newStatus;
		partialRevealed = newStatus;
	}
	/**
	 * Will partially Reveal or partially unreveal this object
	 * @param if true will reveal this object if it is not alredy partially revealed
	 * 		  if false will partially unreveal this object if not currently revealed
	 */
	public void setPartialRevealStatus(boolean newStatus){
		partialRevealed = newStatus;
	}
	//getters
	/**
	 * Will return this Objects current Health
	 * @return current Health
	 */
	public int getHealth(){
		return currentHealth;
	}
	/**
	 * Wil return the largest amount of health this object can have
	 * @return MaximumHealth
	 */
	public int getMaximumHealth(){
		return maxHealth;
	}
	//Getters
	/**
	 * Will set whether or not this object is highLighted
	 * @return highlighted If TRUE, this object will have a graphic drawn over it to distinguish it from similar Objects. If FALSE, it will be drawn normally
	 */
	public boolean getHighlight(){
		return highlighted;
	}
	/**
	 * Returns Whether or not this object will destroy itself when its health reaches zero
	 * @return if True this object will not destroy itself when its health reaches zero.
	 * If false it will destroy itself when its health reaches zero
	 */
	public boolean getVulnerability(){
		return invulnerable;
	}
	/**
	 * Returns the number or points this enemy is worth
	 */
	public int getScore(){
		
		return score;
	}
	/**
	 * Will set the score of this object to the given amount
	 * @param newScore the given amount
	 */
	public void setScore(int newScore){
		score = newScore;
	}
	/**
	 * Returns whether or not the Enemy is revealed
	 * @return revealed true if the Enemy is revealed
	 */
	public boolean isRevealed(){
		return revealed;
	}
	/**
	 * Returns whether or not the Enemy is partially revealed
	 * @return revealed true if the Enemy is partially revealed
	 */
	public boolean isPartialRevealed(){
		return partialRevealed;
	}
	/**
	 * The Number of Turns the current this Enemy will Remain infected (can only be removed by beam) 
	 * -1 is a marker that the enemy is permanently infected
	 * @return infected timer
	 */
	public int getInfectedTimer(){
		return infectedTimer;
	}
	/**
	 * returns the EnemyType that represents this Enemy
	 * @return EnemyType
	 */
	public InteractableObject.ObjectType getType(){
		return type;
	}
	/**
	 * Will set the current Health of this object to the given amount. 
	 * Note the current Health must be greater than or equal to zero and less than or equal to the max health of this object.
	 * In addition, and enemies that have 0 health will be destroy between this update and the next if the persist in the array.
	 * If you remove them from the array you must call .destroy() function yourself
	 * @param newHealth the desired health of this object
	 * @return True if changes were made successfully, otherwise returns false. In which case no changes were made. 
	 */
	public boolean setCurrentHealth(int newHealth){
		if(newHealth >= 0 && newHealth <= maxHealth){
			currentHealth = newHealth;
			return true;
		}
		return false;
	}
	/**
	 * Will set this Objects maximum health to the given amount. Note the given value must be greater than zero
	 * @param newMaxHealth the desired maximum health of this object
	 * @return True if changes were made successfully, otherwise returns false. In which case no changes were made. 
	 */
	public boolean setMaxHealth(int newMaxHealth){
		if(newMaxHealth > 0){
			maxHealth = newMaxHealth;
			if(currentHealth > maxHealth){
				currentHealth = maxHealth;
			}
			return true;
		}
		return false;
	}
	/**
	 * If (type == ObjectType.GHOST || type == ObjectType.ANGRY) will set the the number of turns the Ghost will need to progress to the next to the next Stage in its evolution
	 * @param newTime must be greater then 0 and if  (type == ObjectType.GHOST) less than 4, otherwise less than 6
	 */
	public boolean setGhostTimer(short newTime){
		if(type == InteractableObject.ObjectType.GHOST || type == InteractableObject.ObjectType.ANGRY){
			if(subject == null){
				subject = createEnemy(type);
			}
			if(subject.type != type){
				if(type == InteractableObject.ObjectType.GHOST && subject.type == InteractableObject.ObjectType.ANGRY){
					((Ghost)subject).MakeNotAngry();
				}
				else if(type == InteractableObject.ObjectType.ANGRY && subject.type == InteractableObject.ObjectType.GHOST){
					((Ghost)subject).MakeAngry();
				}
				else{
					subject.removeThis();
					BaseEnemy newSubject = createEnemy(type);
					newSubject.center.set(subject.center);
					subject = newSubject;
				}
			}
			return ((Ghost)subject).setAngryTimer(newTime);
		}
		return false;
	}
	/**
	 * Will update the subject based on the settings in the representative
	 * @param hostSet the Enemy set that is in change of storing all of the BaseEnemies in use
	 * @param puasetime the amount of time the game will pause for animations to proceed
	 * @return whether or not any changes were made that actually require the game to pause
	 * This includes revealing a Enemy, and Moving an enemy
	 */
	StateChange updateSubject(EnemySet hostSet, int puasetime){
		if(hostSet != null){
			//Matching EnemyTypes and moving/Adding Enemy to appropriate array position
			if(subject == null){
				subject = createEnemy(type);
				storedStateChangeObject.puaseForAnimation = hostSet.addEnemy(subject, currentRow, currentCollumn);
			}
			else if(subject.type != type){
				if(type == InteractableObject.ObjectType.GHOST && subject.type == InteractableObject.ObjectType.ANGRY){
					((Ghost)subject).MakeNotAngry();
					storedStateChangeObject.puaseForAnimation = hostSet.moveEnemy(subject, currentRow, currentCollumn);
				}
				else if(type == InteractableObject.ObjectType.ANGRY && subject.type == InteractableObject.ObjectType.GHOST){
					((Ghost)subject).MakeAngry();
					storedStateChangeObject.puaseForAnimation = hostSet.moveEnemy(subject, currentRow, currentCollumn);
				}
				else{
					subject.removeThis();
					BaseEnemy newSubject = createEnemy(type);
					newSubject.center.set(subject.center);
					subject = newSubject;
					hostSet.addEnemy(subject, currentRow, currentCollumn);
					storedStateChangeObject.puaseForAnimation = false;
				}
			}
			else{
				storedStateChangeObject.puaseForAnimation = hostSet.moveEnemy(subject, currentRow, currentCollumn);
			}
			//Updating reveal state infect state etc..
			subject.representative = (InteractableObject)this;
			subject.setInfectStatus(infectedTimer);
			subject.getHealth().setMaxSegments(maxHealth);
			subject.getHealth().setFilledSegments(currentHealth);
			if(subject.getScore() == -1)
				subject.setScore(score);
			subject.circleEnemy(highlighted);
			//updating animation
			subject.setAnimationTarget(currentRow, currentCollumn);
			//updating Partial Reveal status
			if(subject.isPartialRevealed != partialRevealed && !revealed){
				if(partialRevealed){
					subject.partialReveal();
				}
				else{
					subject.unrevealType();
				}
			}
			//updating reveal state
			if(subject.isTypeRevealed != revealed){
				subject.setToReveal = true;
			}
			subject.setInvulnerablility(invulnerable);
		}
		return storedStateChangeObject;
	}
	void resetChanges(){
		storedStateChangeObject.setToDefualts();
	}
	/**
	 * Will create the BaseEnemy object associated with the given Enemy type
	 * @param newType The Type of BaseEnemy requested
	 * @return The new BseEnemy
	 */
	static BaseEnemy createEnemy(InteractableObject.ObjectType newType){
		BaseEnemy retVal = null;
		switch(newType){
	      case ANGRY: {
	    	retVal = new Ghost();				//Creating Ghost
    		((Ghost)retVal).MakeAngry();		//Angering Ghost
	        break;
	      }
	      case GHOST: {
	    	  retVal = (new Ghost());
	    	  break;
	      }
	      case SPIDER: {
	    	  retVal = (new Spider());
	    	  break;
	      }
	      case VAMPIRE: {
	    	  retVal = (new Vampire());
	    	  break;
	      }
	      case MUMMY: {
	    	  retVal = (new Mummy());
	    	  break;
	      }
	      case PUMPKIN: {
	    	  retVal = (new Pumpkin());
	    	  break;
	      }
	      case ZOMBIE: {
	    	  retVal = (new Zombie());
	    	  break;
	      }
	      case FRANKENSTEIN: {
	    	  retVal = (new Frankenstein());
	    	  break;
	      }  
	      case CAT: {
	    	  retVal = (new Cat());
	    	  break;
	      }
	    }
	    return retVal;
	  }
}
