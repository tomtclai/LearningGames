
package GhostLight.GhostEngine;

import java.util.Vector;

import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.InteractableObject.ObjectType;
/**
 * @author Michael Letter
 */
public class Cat extends BaseEnemy{
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(ATTACKING_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
	}
	
	private static final String INTRO_FILENAME = "monsters/cat/Cat_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/cat/Cat_Idle_r.png";
	private static final String ATTACKING_FILENAME = "monsters/cat/Cat_SpecialAbility_r.png";
	private static final String DEATH_FILENAME = "monsters/cat/Cat_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/cat/Cat_IdleBlink_r.png";
	
	
	protected int targetEnemyCollumn = -1;
	protected BaseEnemy byteTarget = null;
	protected int byteTime = 0;
	protected int bytePos = 0;
	private boolean attacking = false;
	
	protected boolean isIntroPlayed = false;
	protected int introDuration = 40;
	protected int introSpeed = 3;
	protected int idleSpeed = 3;
	
	protected Vector<BaseEnemy> targtableGhosts = null;
	protected EnemySubSetNode currentTarget = null;
	
	public Cat() {
		animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
				265, 320, 40, 0, new Vector2(DEFAULT_WIDTH * 1.5f, DEFAULT_HEIGHT));
		animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
				208, 318, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		animStates.addNewState(ABILITY_STATE_NAME, ATTACKING_FILENAME,
				312, 328, 40, 0, new Vector2(DEFAULT_WIDTH * 1.5f, DEFAULT_HEIGHT));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
				334, 344, 20, 1, new Vector2(DEFAULT_DEATH_WIDTH * .7f, DEFAULT_DEATH_HEIGHT * .9f));
		animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
				208, 318, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		
		type = InteractableObject.ObjectType.CAT;
		health.setMaxSegments(InteractableObject.getDefualthealth(type));
		health.setFilledSegments(health.getMaxSegments());
		score = InteractableObject.getDefualtScore(type);
		super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
		unrevealType();
		isHelpfull = true;
	}
	protected void shiftImageState() {
		if(isTypeRevealed){
			animStates.setCycleState(IDLE_STATE_NAME,
	  				BLINK_STATE_NAME,
	  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
	  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
		
		}
		else
		{
			super.shiftImageState();
		}
	}
	 public StateChange revealType(int pauseTime){
		  storedStateChangeObject.setToDefualts();
		  super.revealType(pauseTime);
		  storedStateChangeObject.puaseForAnimation = true;
		  return storedStateChangeObject;
	    
	  }
	 public StateChange updateTurn(){
		  super.updateTurn();
		  byteTime = 0;
		  if(isTypeRevealed){
			  return setByteTarget();
		  }
		  return null;  
	  }

	 /**
	   * Will set the center of this objects position to the appropriate position in the animation and increment animPos
	   * Changes animation to move to TargetPosition then move to the set EndPosition
	   */
	  protected void attackAction(){
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
					  ByteTarget(byteTarget);
				  }
				  //From TargetEnemy to EndPos in (animTime/2)
				  else{
					  center.setX((float)(((targetEnemyPosition.getX()-animPosEnd.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosEnd.getX()));
					  center.setY((float)(((targetEnemyPosition.getY()-animPosEnd.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)bytePos) - ((Math.PI/4f)*(float)byteTime)) / ((float)byteTime/4f)))+animPosEnd.getY()));
				  }
				  bytePos++;
			  }
			  //Biting Enemy during normal Turn
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
					  ByteTarget(byteTarget);	
				  }
				  //From TargetEnemy to EndPos in (animTime/2)
				  else{
					  center.setX((float)(((targetEnemyPosition.getX()-animPosEnd.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosEnd.getX()));
					  center.setY((float)(((targetEnemyPosition.getY()-animPosEnd.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/4f)*(float)animTime)) / ((float)animTime/4f)))+animPosEnd.getY()));
				  }
			  }
		  }
		  //Normal Unrevealed Movement
		  else{
			  super.setCenterToAnimatedPosition();
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
			  //initializing targtableGhosts
			  if(targtableGhosts == null){
				  targtableGhosts = new Vector<BaseEnemy>();
			  }
			  targtableGhosts.clear();
			  if(targtableGhosts.size() < hostSet.getNumberOfCollumns(currentRow)){
				  targtableGhosts.setSize(hostSet.getNumberOfCollumns(currentRow));
			  }
			  //Finding Ghosts
			  BaseEnemy target = null;
			  int targetsFound = 0;
			  for(int loop = 0; loop < hostSet.getNumberOfCollumns(currentRow); loop++){
				  target = hostSet.getEnemyAt(currentRow, loop);
				  if(target != null && target.isTypeRevealed && (target.type == ObjectType.ANGRY || target.type == ObjectType.GHOST)){
					  targtableGhosts.set(targetsFound, target);
					  targetsFound++;
				  }
			  }
			  //Choosing Ghost to byte
			  if(targetsFound == 0){
				  targetEnemyCollumn = -1;
				  byteTarget = null;
			  }
			  //single target
			  else if(targetsFound == 1){
				  byteTarget = targtableGhosts.get(0);
				  targetEnemyCollumn = byteTarget.currentCollumn;
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
			  //Multiple targets
			  else{
				  byteTarget = targtableGhosts.remove((int)(Math.random() * ((double)targetsFound - 0.5)));
				  targetEnemyCollumn = byteTarget.currentCollumn;
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
		  }
		  return storedStateChangeObject;
	  }
	  private void ByteTarget(BaseEnemy target){
		  if(target != null && hostSet != null){
			  EnemySoundManager.requestSound(EnemySoundManager.SoundType.CAT_ATTACK);
			  if(target.type == ObjectType.ANGRY){
				  ((Ghost)target).explode();
				  removeThis();
			  }
			  else{
				  target.removeThis();
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
	
	public void update(int animationTimer) {
		 super.update(animationTimer);
		 if(attacking)
		 {
			 attackAction();
			 if(bytePos >= byteTime)
			 {
				 attacking = false;
			 }
		 }

	}
	@Override
	public void playWaitingIntroductions(int timeAllowed) 
	{
		if(justRevealed)
		{
			animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
	  				IDLE_STATE_NAME,
	  				DEFAULT_INTRO_TIME,
	  				DEFAULT_IDLE_TIME,
	  				DEFAULT_BLINK_TIME,
	  				BLINK_STATE_NAME);
			justRevealed = false;
		}
	}
	@Override
	public StateChange activateAbility(int timeAllowed) 
	{
		if(isTypeRevealed)
		{
			bytePos = 0;
			animTime = timeAllowed;
			byteTime = animTime;
			attacking = true;
			setByteTarget();
			// If there is a target, start jumping.
			//if(this.byteTarget != null)
			//{
			animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
	  				IDLE_STATE_NAME,
	  				timeAllowed,
	  				DEFAULT_IDLE_TIME,
	  				DEFAULT_BLINK_TIME,
	  				BLINK_STATE_NAME);
			//}
		}
		return null;
	}
	@Override
	protected void setDeathAnimation(int deathDuration) {
		animStates.changeState(DEATH_STATE_NAME, null);
		
	}
	
}
