
package GhostLight.GhostEngine;

/**
 * Spider: when revealed, the spider immediately drops-down two rows. If the space is empty a new row will 
 * be created and populated with unrevealed characters to the left of the spider’s location. If the space 
 * is occupied the spider will “eat” the character and subtract whatever its point value was from the player’s
 * score (in the case of a spider eating an unrevealed character it will subtract 10 points from the player 
 * score).
 * @author Michael Letter
 */

import java.awt.Color;

import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import Engine.Rectangle;
import GhostLight.Interface.InteractableObject;

public class Spider extends BaseEnemy{
	
	private static final String INTRO_FILENAME = "monsters/spider/Spider_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/spider/Spider_Idle_r.png";
	private static final String ABILITY_FILENAME = "monsters/spider/Spider_SpecialAbility_r.png";
	private static final String DEATH_FILENAME = "monsters/spider/Spider_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/spider/Spider_IdleBlink_r.png";
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(ABILITY_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
	}
	
  protected int totalPauseTime = 0;					//the total time that the game will be paused for reveal
  protected final int MAX_DROP_DISTANCE = 1;		//the Total Distance the Spider will try to drop
  protected int dropDistance = MAX_DROP_DISTANCE+1;	//The number of Rows Down The spider will Drop down
  protected Vector2 hangTop = null;					//The Position that Spiders Web will stretch From
  protected Vector2 dropTarget = null;				//The target that the spider is moving towards during its drop
  protected Vector2 dropStart = null;				//The starting point that the spider is doping from
  protected int dropTime = 0;						//Used to mark the time total time available to drop and down 1 row
  protected int dropPos = 0;						//The Spiders current position in the drop
  private boolean dropping = false;                 //Tells whether dropping or not ATM.
  protected Rectangle web = null;					//The Web the spider will hang from while Droping
  private int dropUntilRow = 1000;					//Set to arbitrarily long int to not add restriction.
  
  private boolean allowAbility = false;             //Enables the ability.
  
  public Spider(){
	super();
	
	
	// Setup animation states
	animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
			502, 465, 40, 0, new Vector2(DEFAULT_WIDTH * 2f,
					DEFAULT_HEIGHT * 1.3f));
	animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
			329, 307, 40, 0, new Vector2(DEFAULT_WIDTH  * 1.8f,
					DEFAULT_HEIGHT));
	animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
			329, 307, 40, 0, new Vector2(DEFAULT_WIDTH  * 1.8f,
					DEFAULT_HEIGHT));
	animStates.addNewState(ABILITY_STATE_NAME, ABILITY_FILENAME,
			496, 585, 40, 0, new Vector2(DEFAULT_WIDTH  * 2f,
					DEFAULT_HEIGHT * 1.6f));
	animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
			559, 339, 20, 1, new Vector2(DEFAULT_DEATH_WIDTH * 1.5f, DEFAULT_DEATH_HEIGHT));
	
  	type = InteractableObject.ObjectType.SPIDER;
  	unrevealType();
  	health.setMaxSegments(InteractableObject.getDefualthealth(type));
	health.setFilledSegments(health.getMaxSegments());
    score = InteractableObject.getDefualtScore(type);
    super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
    if(InteractableObject.getOption("dropUntilRow", type) != null)
    {
    	dropUntilRow = (int) InteractableObject.getOption("dropUntilRow", type);
    }
    
  }
  public StateChange updateTurn(){
	  return super.updateTurn();
  }
  /**
   * Will deduct points for each Enemy to be eaten and set first target for the spider to drop
   * and will Determine how many Rows the Spider will Drop 
   */
  public StateChange revealType(int pauseTime){
	  //checking if currentEnemyGrid and position are up to date
	  storedStateChangeObject.setToDefualts();
	  if(!isTypeRevealed){
		  super.revealType(pauseTime);
		  allowAbility = true;
	  }
	  else if(getInfectStatus() <= 0){
		  return removeThis();
	  }
	  return storedStateChangeObject;
  }
  public void update(int animationTimer)
  {
	  super.update(animationTimer);
	  if(dropping)
	  {
		  droppingAction();
		  if(dropPos >= dropTime)
		  {
			  dropping = false;
		  }
	  }
  }
  /**
   * Will set the center of this objects position to the appropriate position in the animation and increment animPos
   * Changes animation to move to drop down two rows eating the Enemies in the way when revealed
   */
  protected void droppingAction(){
	  if(dropTarget != null && dropStart != null && dropTime > 0){
		  //Moving Spider
		  dropPos++;
		  //if moved down one row successfully
		  if(dropPos >= dropTime){
			  EatVictem(super.currentRow + dropDistance, currentCollumn);
			  setMoveTarget();			  
		  }
		  //Updating Position if target was reset successfully
		  if(dropTarget != null && dropStart != null){
			  center.setX((float)(((dropTarget.getX()-dropStart.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)dropPos) - ((Math.PI/2)*(float)dropTime)) / ((float)dropTime/2f)))+dropStart.getX()));
			  center.setY((float)(((dropTarget.getY()-dropStart.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)dropPos) - ((Math.PI/2)*(float)dropTime)) / ((float)dropTime/2f)))+dropStart.getY()));
			  //Updating Web
			  //creating new web if nessesary
//			  if(web != null && hangTop != null){
//				//updating web position and length
//				  setWebPositionSize();
//			  }
		  }
		  //Moving to position and filling row
		  else{
			  hostSet.moveEnemy(this, currentRow + dropDistance, currentCollumn);
			  fillAdjacentWithEnemies(currentRow, currentCollumn);
			  this.setAnimationTarget(currentRow, currentCollumn);
		  }
	  }
	  else{
		  dropTarget = null;
		  dropStart = null;
		  if(web != null){
			  web.destroy();
		  }
		  web = null;
	  }
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
  /**
   * Will set the target of the Animation and the Time for it to take place for a Drop
   */
  protected void setMoveTarget(){
	//checking if currentEnemyGrid and position are up to date
	  if(hostSet != null){
		  //Will set spider to drop down one row
		  if(dropDistance < MAX_DROP_DISTANCE && hostSet.getNumberOfRows() > (currentRow + dropDistance + 1)){
			  dropStart = new Vector2(this.center);
			  dropDistance++;
			  BaseEnemy target = hostSet.getEnemyAt(currentRow+dropDistance, currentCollumn);
			  if(target != null && target != this){
				  dropTarget = target.center;
			  }
			  else{ 
				  dropTarget = new Vector2(this.center);
				  hostSet.getWorldPosition(dropTarget, currentRow + dropDistance, currentCollumn);
			  }
			  //setting Web hang point and initializing web
			  //setWebPositionSize();;
		  }
		  else{
			  dropTarget = null;
			  dropStart = null;
		  }
	  }
  }
  /**
   * Will eat the Victem at the Given location
   * @param row	the row the Victem the victem lies in
   * @param column the column the Victem the victem lies in
   * @return The change in score as a result of the meal
   */
  protected void EatVictem(int row, int column){
	  if(hostSet != null){
		  BaseEnemy victem = hostSet.getEnemyAt(row, column);
		  if(victem != null && victem != this){
			  victem.removeThis();
		  }
	  }
  }
  /**
   * Will Fill a given Row with a random Set of Enemies
   * @param row the given Row
   */
  protected void fillRowWithEnemies(int row){
	  if(hostSet != null){
		  for(int loop = 0; loop < hostSet.getNumberOfCollumns(row); loop++){
			  if(hostSet.getEnemyAt(row, loop) == null){
				  BaseEnemy temp = getRandomEnemy();
				  temp.center.set(this.center);
				  hostSet.addEnemy(temp, row, loop);
			  }
		  }
	  }
  }
  /**
   * Will Fill a given Row with a random Set of Enemies
   * @param row the given Row
   */
  protected void fillAdjacentWithEnemies(int row, int column){
	  if(hostSet != null){
		  if(hostSet.getNumberOfCollumns() - 1 > column &&
				  hostSet.getEnemyAt(row, column + 1) == null){
			  BaseEnemy temp = getRandomEnemy();
			  temp.center.set(this.center);
			  hostSet.addEnemy(temp, row, column + 1);
		  }
		  if(0 < column &&
				  hostSet.getEnemyAt(row, column - 1) == null){
			  BaseEnemy temp = getRandomEnemy();
			  temp.center.set(this.center);
			  hostSet.addEnemy(temp, row, column - 1);
		  }
		  
	  }
  }
  
//  protected void initializeWeb(){
//	  
//	  if(web == null){
//		  web = new Rectangle();
//		  web.color = Color.BLACK;
//		  web.visible = false;
//	  }
//	  if(hangTop == null){
//		  hangTop = new Vector2(center);
//	  }
//	  else{
//		  hangTop.set(center);
//	  }
//  }
//  protected void setWebPositionSize(){
//	  if(hostSet != null && web != null && hangTop != null){
//		  //updating web position and length
//		  web.center.set(hangTop);
//		  web.center.sub(center);
//		  web.center.mult(0.5f);
//		  web.center.add(center);
//		  web.size.setX(1f);
//		  web.size.setY(dropStart.getY() - center.getY() + (hostSet.getRowSpacing() * (dropDistance-1)));
//		  web.visible = true;
//	  }
//  }
  /**
   * Will return a random Enemy based on the Following Probabilities
   * 11% Angry Ghost
   * 11% Cat
   * 11% Frankenstien
   * 11% Ghost
   * 11% Mummy
   * 12% Pumpkin
   * 11% Spider
   * 11% Vampire
   * 11% Zombie
   * 
   * @return The Randomly Generated Enemy
   */
  protected BaseEnemy getRandomEnemy(){
	  double percent = Math.random();
	  //11% Angry Ghost
	  if(percent <= 0.11){
		  Ghost retVal = new Ghost();
		  retVal.MakeAngry();
		  return retVal;
	  }
	  //11% Cat
	  else if(percent <= 0.22){
		  return new Cat();
	  }
	  //11% Frankenstien
	  else if(percent <= 0.33){
		  return new Frankenstein();
	  }
	  //11% Ghost
	  else if(percent <= 0.44){
		  return new Ghost();
	  }
	  //11% Mummy
	  else if(percent <= 0.55){
		  return new Mummy();
	  }
	  //12% Pumpkin
	  else if(percent <= 0.67){
		  return new Pumpkin();
	  }
	  //11% Spider
	  else if(percent <= 0.78){
		  return new Spider();
	  }
	  //11% Vampire
	  else if(percent <= 0.89){
		  return new Vampire();
	  }
	  //11% Zombie
	  else {
		  return new Zombie();
	  }
  }
  @Override
  public void playWaitingIntroductions(int timeAllowed) 
  {
  	if(justRevealed)
  	{
  		this.animStates.shiftTimerStateThenCycle(INTRO_STATE_NAME,
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
	
	if(allowAbility && currentRow < dropUntilRow)
	{
		//setting targets to move Towards
		  dropDistance = 0;
		  setMoveTarget();
		  //initializeWeb();
		  //Deducting points as a result of Drop
		  if(hostSet != null){
			  if(hostSet.doesGridPositionExist(currentRow, currentRow + 1)){
				  //Jumping Down 1 row
				  BaseEnemy target = hostSet.getEnemyAt(currentRow + 1, currentCollumn);
				  if(target != null){
					  storedStateChangeObject.changeInPlayerScore -= target.getScore();
				   }
				  storedStateChangeObject.puaseForAnimation = true;
				  dropTime = timeAllowed;
			  }
			  if(dropTime == 0){
				  hostSet.moveEnemy(this, currentRow + dropDistance, currentCollumn);
				  fillAdjacentWithEnemies(currentRow + dropDistance, currentCollumn);
				  this.setAnimationTarget(currentRow, currentCollumn);
			  }
		  }
		animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
  				IDLE_STATE_NAME,
  				DEFAULT_INTRO_TIME,
  				DEFAULT_IDLE_TIME,
  				DEFAULT_BLINK_TIME,
  				BLINK_STATE_NAME);
		dropping = true;
		allowAbility = false;
	}
	return null;
}
protected void setDeathAnimation(int deathDuration) {
	animStates.changeState(DEATH_STATE_NAME, null);
	
}
}
