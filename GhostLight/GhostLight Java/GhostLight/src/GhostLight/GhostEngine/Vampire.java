
package GhostLight.GhostEngine;

/**
 * Dracula: When revealed, Dracula will immediately jump to a random space somewhere in the
 * current chain (Dracula cannot jump to a space on the game board further than the leading 
 * occupied spot, although he can jump to an empty space within the current chain), bite 
 * whatever is in that space (revealed or unrevealed) and turn it into an angry ghost, then 
 * return to his original location. If the space is occupied by an unrevealed character there 
 * is no point loss; if the character is revealed, the player loses however many points that 
 * character was worth. If the space is empty, Dracula will remain in it after jumping. If 
 * the space is filled with an Angry Ghost, Dracula will explode that ghost upon biting it 
 * and the player will lose a life. Dracula repeats this behavior every other turn until he 
 * is removed. Dracula can only be removed by the charge beam. Dracula is worth 50 points.
 * @author Michael Letter
 */
import java.lang.Math;

import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;

public class Vampire extends BaseEnemy{
	
	private static final String INTRO_FILENAME = "monsters/vampire/Dracula_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/vampire/Dracula_Idle_r.png";
	private static final String ABILITY_FILENAME = "monsters/vampire/Dracula_SpecialAbility_r.png";
	private static final String DEATH_FILENAME = "monsters/vampire/Dracula_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/vampire/Dracula_IdleBlink_r.png";
		
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(ABILITY_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
	}
	protected int targetEnemyCollumn = -1;
	protected BaseEnemy byteTarget = null;
	protected int byteTime = 0;
	protected int bytePos = 0;
	private boolean biting = false;
	
	protected EnemySubSetNode currentTarget = null;
	
  public Vampire() {
	  super();
	  
		// Setup animation states
		animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
				321, 310, 40, 0, new Vector2(DEFAULT_WIDTH * 1.5f, DEFAULT_HEIGHT));
		animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
				187, 289, 40, 0, new Vector2(DEFAULT_WIDTH * 1.2f, DEFAULT_HEIGHT));
		animStates.addNewState(ABILITY_STATE_NAME, ABILITY_FILENAME,
				242, 288, 40, 0, new Vector2(DEFAULT_WIDTH * 1.5f, DEFAULT_HEIGHT));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
				559, 339, 20, 1, new Vector2(DEFAULT_DEATH_WIDTH * 1.2f, DEFAULT_DEATH_HEIGHT));
		animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
				187, 289, 40, 0, new Vector2(DEFAULT_WIDTH * 1.2f, DEFAULT_HEIGHT));

	  type = InteractableObject.ObjectType.VAMPIRE;
	  unrevealType();
	  health.setMaxSegments(InteractableObject.getDefualthealth(type));
	  health.setFilledSegments(health.getMaxSegments());
	  score = InteractableObject.getDefualtScore(type);
	  super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
  }

  protected void shiftImageState() {
    if(isTypeRevealed){
      	animStates.changeState(IDLE_STATE_NAME);
    }
    else
    {
    	super.shiftImageState();
    }
    
  }
  public StateChange updateTurn(){
	  super.updateTurn();
	  byteTime = 0;
	  if(isTypeRevealed){
		  return setByteTarget();
	  }
	  return null;  
  }
  public StateChange revealType(int pauseTime){
	  storedStateChangeObject.setToDefualts();
	  super.revealType(pauseTime);
	  EnemySoundManager.requestSound(EnemySoundManager.SoundType.DRACULA_INTRO);
	  setByteTarget();
	  if(pauseTime == 0){
		  BiteTarget(byteTarget);
	  }
	  byteTime = pauseTime;
	  bytePos = 0;
	  storedStateChangeObject.puaseForAnimation = true;
	  return storedStateChangeObject;
    
  }
  public void update(int animationTime)
  {
	  super.update(animationTime);
	  if(biting)
	  {
		  biteAction();
		  if(bytePos >= byteTime)
		  {
			  biting = false;
		  }
	  }
  }
  /**
   * Will set the center of this objects position to the appropriate position in the animation and increment animPos
   * Changes animation to move to TargetPosition then move to the set EndPosition
   */
  protected void biteAction(){
	  //From StartPos to TargetEnemy in (animTime/2)
	  if(isTypeRevealed && byteTarget != null && !removeAfterAnimation){
		  Vector2 targetEnemyPosition = byteTarget.center;
		  //IfBiting at Reveal vs during normal Update
		  if(byteTime > 0){
			//Moving Two Enemy
			  if(bytePos < (byteTime/2)){
				  center.setX((float)(((targetEnemyPosition.getX()-animPosStart.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosStart.getX()));
				  center.setY((float)(((targetEnemyPosition.getY()-animPosStart.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosStart.getY()));
			  }
			  //Reached Enemy
			  //From TargetEnemy to EndPos in (animTime/2)
			  else if(bytePos == (byteTime/2)){
				  center.setX((float)(((targetEnemyPosition.getX()-animPosEnd.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosEnd.getX()));
				  center.setY((float)(((targetEnemyPosition.getY()-animPosEnd.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosEnd.getY()));
				  BiteTarget(byteTarget);
			  }
			  //From TargetEnemy to EndPos in (animTime/2)
			  else{
				  center.setX((float)(((targetEnemyPosition.getX()-animPosEnd.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosEnd.getX()));
				  center.setY((float)(((targetEnemyPosition.getY()-animPosEnd.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosEnd.getY()));
			  }
			  bytePos++;
		  }
		  //Biting Enemy durring normal Turn
		  else if(byteTime <= 0){
			  //Moving Two Enemy
			  if(animPos < (animTime/2)){
				  center.setX((float)(((targetEnemyPosition.getX()-animPosStart.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosStart.getX()));
				  center.setY((float)(((targetEnemyPosition.getY()-animPosStart.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosStart.getY()));
			  }
			  //Reached Enemy
			  //From TargetEnemy to EndPos in (animTime/2)
			  else if(animPos == (animTime/2)){
				  center.setX((float)(((targetEnemyPosition.getX()-animPosEnd.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosEnd.getX()));
				  center.setY((float)(((targetEnemyPosition.getY()-animPosEnd.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosEnd.getY()));
				  BiteTarget(byteTarget);	
			  }
			  //From TargetEnemy to EndPos in (animTime/2)
			  else{
				  center.setX((float)(((targetEnemyPosition.getX()-animPosEnd.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosEnd.getX()));
				  center.setY((float)(((targetEnemyPosition.getY()-animPosEnd.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosEnd.getY()));
			  }
		  }
	  }
  }
  /**
   * will remove this Enemy From the currentEnemyGrid
   * Note this does Reset any previous changes to the stored storedStateChangeObject
   * before making its own changes
   */
  protected StateChange removeThis(){
	  if(currentTarget != null){
		  currentTarget.removeNode();
		  currentTarget = null;
	  }
	  byteTarget = null;
	  this.targetEnemyCollumn = -1;
	  return super.removeThis();
  }
  /**
   * Will set the Target of the Byte and will Deduct any points that will need to be Deducted as a result of the byte
   * Will Also Set the Animation to move towards the Byte target within the Fist half of the animation ime
   * @return The super.storedStateChangeObject as the change game state
   */
  private StateChange setByteTarget(){
	  storedStateChangeObject.setToDefualts();
	  bytePos = 0;
	  if(currentTarget != null){
		  currentTarget.removeNode();
	  }
	  if(hostSet != null){
		  //choosing Target Enemy
		  targetEnemyCollumn = getRandomCollomn();
		  byteTarget = hostSet.getEnemyAt(currentRow, targetEnemyCollumn);
		  //Deducting Points
		  if(byteTarget != this){
			  //Byte
			  if(byteTarget != null && byteTarget.type != InteractableObject.ObjectType.CAT){
				  if(byteTarget.isTypeRevealed){
					  storedStateChangeObject.changeInPlayerScore =- byteTarget.getScore();
				  }
				  if(byteTarget.type == InteractableObject.ObjectType.ANGRY){
					  storedStateChangeObject.add(((Ghost)byteTarget).getExplodeEffects());
				  }
			  }
			  //Move
			  else if(byteTarget == null){
				  hostSet.moveEnemy(this, currentRow, targetEnemyCollumn);
				  super.setAnimationTarget(currentRow, targetEnemyCollumn);
			  }
			  //Idle
			  else{
				  targetEnemyCollumn = -1;
				  byteTarget = null;
			  }
			  //is enemy already targeted
			  if(isEnemyAlreadyTargeted(byteTarget)){
				  targetEnemyCollumn = -1;
				  byteTarget = null;
			  }
			  //targeting enemy
			  else{
				  currentTarget = targetEnemy(byteTarget);
			  }
		  }
		  else{
			  byteTarget = null; 
		  }  
	  }
	  return storedStateChangeObject;
  }
  /**
   * Will perform a bite effect on a target
   * @return the Score Decrement as a result of the Bite
   */
  private void BiteTarget(BaseEnemy target){
	  if(hostSet != null && target != null){
		  EnemySoundManager.requestSound(EnemySoundManager.SoundType.DRACKULA_ATTACK);
	    	//Target is Ghost
	    	//Making Angry
	    	if(target.type == InteractableObject.ObjectType.GHOST){
	    		//if not revealed
	    		if(!target.isTypeRevealed){
	    			target.revealType(0);
	    		}
	    		((Ghost)target).MakeAngry();
	    		((Ghost)target).setCanGetAngry(true);
	    	}
	    	//Target is already an angry Ghost
	    	else if(target.type == InteractableObject.ObjectType.ANGRY){
	    		//if not revealed
	    		if(!target.isTypeRevealed){
	    			target.revealType(0);
	    		}
	    		((Ghost)target).explode();
	    		removeThis();
	    	}
	    	//Target is not an Angry Ghost
	    	//Removing Target and Making it an angry Ghost
	    	else if(target != this && target.type != InteractableObject.ObjectType.CAT){
	    		target.removeThis();
	    		Ghost angryGhost = new Ghost();
	    		angryGhost.revealType(0);
	    		hostSet.addEnemy(angryGhost, currentRow, targetEnemyCollumn);
	    		angryGhost.center.set(center);
	    		angryGhost.setAnimationTarget(currentRow, targetEnemyCollumn);
	    	}
	  }
	  if(currentTarget != null){
		 currentTarget.removeNode();
		 currentTarget = null;
	  }
	  targetEnemyCollumn = -1;
  }
  //will return a random column in the current Enemy Grid
  private int getRandomCollomn(){
	if(hostSet != null && hostSet.getNumberOfCollumns(currentRow) > 0){	
		int MaxAttempts = 5;
		int retVal = (int)(Math.random() * (hostSet.getNumberOfCollumns(currentRow) - 0.2));
		while(retVal == currentCollumn && MaxAttempts >= 0){
			retVal = (int)(Math.random() * (hostSet.getNumberOfCollumns(currentRow) - 0.2));
			MaxAttempts--;
		}
		return retVal;
	}
	return 0;
  }

@Override
public void playWaitingIntroductions(int timeAllowed) 
{
	if(justRevealed)
	{
		animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
  				IDLE_STATE_NAME,
  				DEFAULT_INTRO_TIME,
  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME),
  				BLINK_STATE_NAME);
		justRevealed = false;
	}
}

@Override
public StateChange activateAbility(int timeAllowed) {
	if(isTypeRevealed)
	{
		bytePos = 0;
		animTime = timeAllowed;
		byteTime = animTime;
		biting = true;
		setByteTarget();
		// If there is a target, start jumping.
		if(this.byteTarget != null)
		{
			animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
	  				IDLE_STATE_NAME,
	  				timeAllowed,
	  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
	  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME),
	  				BLINK_STATE_NAME);
		}
	}
    return null;
}
protected void setDeathAnimation(int deathDuration) {
	animStates.changeState(DEATH_STATE_NAME, null);
	
}
}
