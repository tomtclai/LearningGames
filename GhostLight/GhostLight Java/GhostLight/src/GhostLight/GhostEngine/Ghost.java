
package GhostLight.GhostEngine;

import java.awt.Color;





import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;
import MenueSystem.SegmentedBar;
/**
 * Ghost: when revealed, the ghost must be removed within 3 turns or it will turn into an angry ghost. The ghost is worth 30 points.
 * 	The ghost can only be removed by the charge beam.
 * Angry Ghost: when a regular ghost becomes an angry ghost, if it is not removed in 5 turns it explodes and takes one of the player’s 
 * 	lives. The angry ghost can only be removed by the charge beam. The angry ghost is worth 50 points.
 * import GhostLight.Interface.InteractableObject.ObjectType;
 * @author Michael Letter
 */


public class Ghost extends BaseEnemy {

	private static final String INTRO_FILENAME = "monsters/ghost/Ghost_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/ghost/Ghost_Idle_r.png";
	private static final String ANGRY_IDLE_FILENAME = "monsters/ghost/GhostAngry_Idle_r.png";
	private static final String DEATH_FILENAME = "monsters/ghost/Ghost_Death_r.png";
	private static final String ANGRY_DEATH_FILENAME = "monsters/ghost/GhostAngry_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/ghost/Ghost_IdleBlink_r.png";
	private static final String ANGRY_BLINK_FILENAME = "monsters/ghost/GhostAngry_IdleBlink_r.png";
		
	private static final String ANGRY_IDLE_STATE_NAME = "angry_idle";
	private static final String ANGRY_DEATH_STATE_NAME = "angry_death";
	private static final String ANGRY_BLINK_STATE_NAME = "angry_blink";
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
		
		resources.loadImage(ANGRY_IDLE_FILENAME);
		resources.loadImage(ANGRY_DEATH_FILENAME);
		resources.loadImage(ANGRY_BLINK_FILENAME);
	}
	
	protected final static int EXPLODING_TIMER_START_VAL = 11;	//the number of turns the explotion animation takes to complete
	protected short AngryTimerStartVal = 3;						//The Number of turns it takes for a normal ghost to turn into an angry Ghost
	protected short ExplodeTimerStertVal = 5;					//The Number of turns it requires to Angry Ghost to Explode
	protected boolean angry;									//Marks whether the Ghost is angry
	private short angryTimer;									//Marks the number of turns until the Ghost transforms from Ghost -> AngryGhost: Transforms when == 0
	protected boolean vibrate = false;
	protected SegmentedBar explodeTimer = new SegmentedBar();
	private boolean angrynessTriggered = false;
	
	//Defialt Constructor
	//Sets image to new
	public Ghost() {
		super();
		
		//Setup animation states
		animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
				242, 329, 40, 0, new Vector2(DEFAULT_WIDTH * 1.4f, DEFAULT_HEIGHT * 1.2f));
		animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
				170, 304, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
				170, 304, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
				559, 339, 20, 1, new Vector2(DEFAULT_DEATH_WIDTH * 1.5f, DEFAULT_DEATH_HEIGHT));
		
		
		animStates.addNewState(ANGRY_IDLE_STATE_NAME, ANGRY_IDLE_FILENAME,
				171, 307, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		animStates.addNewState(ANGRY_DEATH_STATE_NAME, ANGRY_DEATH_FILENAME,
				559, 339, 20, 0, new Vector2(DEFAULT_DEATH_WIDTH * 1.5f, DEFAULT_DEATH_HEIGHT));
		animStates.addNewState(ANGRY_BLINK_STATE_NAME, ANGRY_BLINK_FILENAME,
				170, 307, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		
		type = InteractableObject.ObjectType.GHOST;
		MakeNotAngry();
		unrevealType();
		health.setMaxSegments(InteractableObject.getDefualthealth(type));
		health.setFilledSegments(health.getMaxSegments());
	    score = InteractableObject.getDefualtScore(type);
	    super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
	    
	    health.size.setY(0.35f);
	    
	    explodeTimer.size.setY(0.7f);
	    explodeTimer.removeFromAutoDrawSet();
	    explodeTimer.visible = false;
	    explodeTimer.center.set(center);
		explodeTimer.center.setY(health.center.getY() - (size.getY()/2)  + 0.35f);
		explodeTimer.size.set(size);
		explodeTimer.color = Color.yellow;
		explodeTimer.setMaxSegments(AngryTimerStartVal + ExplodeTimerStertVal);
		explodeTimer.setFilledSegments(explodeTimer.getMaxSegments());
	}
	//returns the texture associated with the texture 
	protected void shiftImageState(){
	    if(isTypeRevealed) {
	    	if(explodeTimer.getfilledSegments() > 0){
	    		if(angry){
					animStates.setCycleState(ANGRY_IDLE_STATE_NAME,
							ANGRY_BLINK_STATE_NAME,
							DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
			  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
	    		}
	    		else{
					animStates.setCycleState(IDLE_STATE_NAME,
							BLINK_STATE_NAME,
							DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
			  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
	    		}
	    	}
	    }
	    else
	    {
	    	super.shiftImageState();
	    }
	}
	/**
	 * Will Make this Ghost Angry
	 */
	public void MakeAngry(){
		angry = true;
		type = InteractableObject.ObjectType.ANGRY;
		angryTimer = ExplodeTimerStertVal;
		if(isTypeRevealed){
			animStates.setCycleState(ANGRY_IDLE_STATE_NAME,
					ANGRY_BLINK_STATE_NAME,
					DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
	  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
		}
		explodeTimer.setFilledSegments(angryTimer);
		health.setMaxSegments(InteractableObject.getDefualthealth(type));
		health.setFilledSegments(health.getMaxSegments());
	    score = InteractableObject.getDefualtScore(type);
	    super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
		vibrate = false;
	}
	/**
	 * Will Make this Ghost Not Angry
	 */
	public void MakeNotAngry(){
		angry = false;
		type = InteractableObject.ObjectType.GHOST;
		angryTimer = AngryTimerStartVal;
		if(isTypeRevealed){
			animStates.setCycleState(IDLE_STATE_NAME,
					BLINK_STATE_NAME,
					DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
	  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
		}
		explodeTimer.setFilledSegments(ExplodeTimerStertVal + angryTimer);
		health.setMaxSegments(InteractableObject.getDefualthealth(type));
		health.setFilledSegments(health.getMaxSegments());
	    score = InteractableObject.getDefualtScore(type);
	    super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
		vibrate = false;
	}
	/**
	 * Will Make the Ghost Explode and return the changes to the player state
	 * @return any changes to player state as a result of the explotion
	 */
	public StateChange explode(){
		EnemySoundManager.requestSound(EnemySoundManager.SoundType.GHOST_EXPLODE);
		
		if(!isTypeRevealed){
			revealType(0);
		}
		if(!angry){
			MakeAngry();
		}
		super.getHealth().visible = false;
		explodeTimer.visible = false;
		angryTimer = (EXPLODING_TIMER_START_VAL * 2);
		explodeTimer.setFilledSegments(0);
		killEnemy(currentRow - 1, currentCollumn + 1);
		killEnemy(currentRow - 1, currentCollumn - 1);
		killEnemy(currentRow - 1, currentCollumn);
		killEnemy(currentRow + 1, currentCollumn + 1);
		killEnemy(currentRow + 1, currentCollumn - 1);
		killEnemy(currentRow + 1, currentCollumn);
		killEnemy(currentRow, currentCollumn + 1);
		killEnemy(currentRow, currentCollumn - 1);
		
		if(hostSet != null){
			size.setX(hostSet.getCollumnSpacing() * 1);
			size.setY(hostSet.getRowSpacing() * 1);
			hostSet.moveEnemytoSubSet(this);
		}
		else{
			//size.mult(5f); TODO: Commented out here.
		}
		vibrate = false;
		return getExplodeEffects(); 
	}
	/**
	 * Will return the effects of this Ghost Exploding if it were to explode
	 */
	public StateChange getExplodeEffects(){
		storedStateChangeObject.setToDefualts();
		storedStateChangeObject.changeInPlayerHealth = -1;
		return storedStateChangeObject; 
	}
	/**
	 * Will set the number of turns this ghost take to evolve
	 * @param target
	 * @return
	 */
	public boolean setAngryTimer(short target){
		if(target > 0){
			angryTimer = target;
			updateAngryness();
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @return Returns True if the Ghost is Angry. Returns False if the Ghost is not
	 */
	public boolean isAngry(){
		return angry;
	}
	//Overides updateTurn
	/**
	 * Call Each time a Turn Concludes 
	 */
	public StateChange updateTurn(){
		super.updateTurn();
		return updateAngryness();
	}
	/**
	 * Overrides normal updated to update explode timer
	 */
	public void update(int animationTimer) {
		super.update(animationTimer);
		if(isTypeRevealed && explodeTimer.getfilledSegments() == 0){
			//incrementing animation
			angryTimer--;
			//setting animation frame
			if((angryTimer/2) > 0 && (angryTimer/2) <= EXPLODING_TIMER_START_VAL){
				animStates.changeState(ANGRY_DEATH_STATE_NAME, null);
				//super.setImage("explode/explode" + (EXPLODING_TIMER_START_VAL - (angryTimer/2) + 1) +".png");
			}
			else{
				removeThis();
			}
		}
		
		explodeTimer.size.set(size);
		explodeTimer.size.setY(0.7f);
		explodeTimer.center.set(center);
		explodeTimer.center.setY(health.center.getY() - (health.size.getY()));
		
	}
	public StateChange revealType(int pauseTime){
		EnemySoundManager.requestSound(EnemySoundManager.SoundType.GHOST_INTRO);
		explodeTimer.visible = true;
		return super.revealType(pauseTime);
	}
	public StateChange unrevealType(){
		if(explodeTimer.getfilledSegments() > 0){
			explodeTimer.visible = false;
		}
		return super.unrevealType();
	}
	public void draw(){
		if(explodeTimer.visible){
			explodeTimer.draw();
		}
		super.draw();
	}
	protected StateChange removeThis(){
		explodeTimer.destroy();
		return super.removeThis();
	}
	/**
	 * Called once per turn updates the angry state of the ghost and the image
	 */
	protected void setCenterToAnimatedPosition(){
		super.setCenterToAnimatedPosition();
		if(isTypeRevealed && vibrate){
			center.setX((float)(center.getX()+Math.random()));
			center.setY((float)(center.getY()+Math.random()));
		}
	}
	/**
	 * Will attempt to Remove an enemy at the given location 
	 * Note Does not check if hostSet is set
	 * @param targetRow
	 * @param targetCollumn
	 */
	private void killEnemy(int targetRow, int targetCollumn){
		if(hostSet != null){
			BaseEnemy target = hostSet.getEnemyAt(targetRow, targetCollumn);
			if(target != null){
				target.scare();
			}
		}
	}
	private StateChange updateAngryness(){
		super.storedStateChangeObject.setToDefualts();
		
		if(health.getfilledSegments() != health.getMaxSegments())
		{
			angrynessTriggered = true;
		}
		
		if( isTypeRevealed && angrynessTriggered && explodeTimer.getfilledSegments() > 0){
			angryTimer--; 
			if(!angry){
				if(angryTimer == 0){
					MakeAngry();
				}
				else{
					if(angryTimer > AngryTimerStartVal){
						angryTimer = AngryTimerStartVal;
					}
					explodeTimer.setFilledSegments(ExplodeTimerStertVal + angryTimer);
					animStates.setCycleState(
							IDLE_STATE_NAME,
							BLINK_STATE_NAME,
							DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
			  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
					//super.setImage("ghost" + (AngryTimerStartVal - angryTimer + 1) + ".png");
				}
			}
			else{
				if(angryTimer == 0){
					return explode();
				}
				else{
					if(angryTimer > ExplodeTimerStertVal){
						angryTimer = ExplodeTimerStertVal;
					}
					explodeTimer.setFilledSegments(angryTimer);
					animStates.setCycleState(ANGRY_IDLE_STATE_NAME,
							ANGRY_BLINK_STATE_NAME,
							DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
			  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
					//super.setImage("angry-ghost" + (ExplodeTimerStertVal - angryTimer + 1) + ".png");
					if(angryTimer == 1){
						vibrate = true;
					}
				}
			}
		}
		return storedStateChangeObject;
	}
	public void setCanGetAngry(boolean angryable)
	{
		angrynessTriggered = angryable;
	}
	@Override
	public void playWaitingIntroductions(int timeAllowed) 
	{
		if(justRevealed)
		{
			animStates.shiftTimerStateThenCycle(INTRO_STATE_NAME,
	  				IDLE_STATE_NAME,
	  				timeAllowed,
	  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
	  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME),
	  				BLINK_STATE_NAME);
			justRevealed = false;
		}
	}

	protected void setDeathAnimation(int deathDuration) {
		animStates.changeState(DEATH_STATE_NAME, null);
		explodeTimer.visible = false;
		
	}
	@Override
	public StateChange activateAbility(int timeAllowed) {
		// TODO What is this???
		return null;
	}
}
