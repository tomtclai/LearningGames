
package GhostLight.GhostEngine;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Rectangle;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.InteractableObject.ObjectType;
import MenueSystem.SegmentedBar;
import Engine.Text;
/**
 * @author Michael Letter
 */
public abstract class BaseEnemy extends Rectangle{

	private static String UNREVEALED_IDLE_FILE = "monsters/unrevealed/Unrevealed_Idle_r.png";
	private static String UNREVEALED_STATE_NAME = "unrevealed_idle";

	private static String UNREVEALED_UNFRIENDLY_FILE = "monsters/unrevealed/Unrevealed_Idle_Red_r.png";
	private static String UNREVEALED_UNFRIENDLY_STATE_NAME = "unrevealed_unfriendly";

	private static String UNREVEALED_FRIENDLY_FILE = "monsters/unrevealed/Unrevealed_Idle_Green_r.png";
	private static String UNREVEALED_FRIENDLY_STATE_NAME = "unrevealed_friendly";
	
	private static String UNREVEALED_UNFRIENDLY_BLINK_FILE = "monsters/unrevealed/Unrevealed_IdleBlink_Red_r.png";
	private static String UNREVEALED_UNFRIENDLY_BLINK_STATE_NAME = "unrevealed_unfriendly_blink";

	private static String UNREVEALED_FRIENDLY_BLINK_FILE = "monsters/unrevealed/Unrevealed_IdleBlink_Green_r.png";
	private static String UNREVEALED_FRIENDLY_BLINK_STATE_NAME = "unrevealed_friendly_blink";	
	
	protected static final String INTRO_STATE_NAME = "intro";
	protected static final String IDLE_STATE_NAME = "idle";
	protected static final String ABILITY_STATE_NAME = "ability";
	protected static final String DEATH_STATE_NAME = "death";
	protected static final String BLINK_STATE_NAME = "blink";
	
	protected static final int DEFAULT_INTRO_TIME = 40;
	protected static final int DEFAULT_IDLE_TIME = 200;
	protected static final int DEFAULT_BLINK_TIME = 40;
	protected static final int DEFAULT_ABILITY_TIME = 40;
	protected static final float DEFAULT_DEATH_WIDTH = 10f;
	protected static final float DEFAULT_DEATH_HEIGHT = 8f;
	
	
	public static void preLoadResources(ResourceHandler resources){
		resources.preloadImage(UNREVEALED_IDLE_FILE);
		resources.preloadImage(UNREVEALED_UNFRIENDLY_FILE);
		resources.preloadImage(UNREVEALED_FRIENDLY_FILE);
		resources.preloadImage(UNREVEALED_UNFRIENDLY_BLINK_FILE);
		resources.preloadImage(UNREVEALED_FRIENDLY_BLINK_FILE);
		resources.loadImage("glow.png");
		resources.loadImage("infected.png");
		
		resources.loadImage("sparcle/sparcle1.png");
		resources.loadImage("sparcle/sparcle2.png");
		resources.loadImage("sparcle/sparcle3.png");
		resources.loadImage("sparcle/sparcle4.png");
		resources.loadImage("sparcle/sparcle5.png");
		
	}
	
  public enum HiddenColor{
    WHITE, GREEN, RED
  };
  
  protected final static float DEFAULT_WIDTH = 3.5f;
  protected final static float DEFAULT_HEIGHT = 6f;
  
  protected AnimationStateHandler animStates;
  
  
  protected ObjectType type = ObjectType.PUMPKIN;
  protected boolean isTypeRevealed = false;
  protected boolean justRevealed = false; // Used decide when to play introductions.
  public boolean setToReveal = false;
  protected boolean isPartialRevealed = false;
  protected boolean isHelpfull = false;
  protected boolean isFalling = false;
  protected int score = -1;
  
  protected EnemySubSetNode hostNode = null;		//Stores the node that stores this Enemy if it is an EnemySubSet
  protected EnemySet hostSet = null;				//The current Manager of currentEnemyRow, and currentEnemyGrid
  public int currentRow = 0;						//The Row which this Enemy Resides	
  public int currentCollumn = 0;					//THe Column which this enemy Resides
  
  protected Vector2 animPosStart = new Vector2();	//The Position this animation is starting from
  protected Vector2 animPosEnd = new Vector2();		//The Position this animation is Ending at 
  protected int animPos = 0;						//THe Current animation Position: Note animPos = 0 is a marker to move enemy to its target position
  protected int animTime = -1;						//The Number updates this animation has to travel from animPosStart to animPosEnd a -1 indicates that the time is unknown 
  
  protected StateChange storedStateChangeObject = new StateChange();//Stored to prevent extra new(s) durring each update
  
  protected int animDurationTotal = 20;
  protected int animDurationCurrent = -1;
  
  protected InteractableObject representative = null;

  protected HiddenColor hiddenColor = HiddenColor.WHITE;

  protected SegmentedBar health = new SegmentedBar();
  /** If TRUE this object will remove itself when (animPos == animTime)*/
  protected boolean removeAfterAnimation = false;
  
  protected static EnemySubSet currentTargets = new EnemySubSet();
  
  private Text displayedScore = null; 
  private Rectangle selected = null;
  private Rectangle infectCloud = null;
  
  private String[] sparcleFrames = {"sparcle/sparcle1.png", "sparcle/sparcle2.png",
			"sparcle/sparcle3.png", "sparcle/sparcle4.png", "sparcle/sparcle5.png"};
  private Rectangle highLight = null;
  private int currentHighLight = 0;
  
  private int infectedTimer = 0;					//The Number of Turns the current this Enemy will Remain infected -1 is a marker that the enemy is permanently infected
  private SegmentedBar infectBar =  new SegmentedBar();
  private boolean invulnerable = false;
  
  private boolean allowMovement = true;
  
  private int deathCountDown = -1;
  private static int DEATH_DURATION = 24;
  
  
  public BaseEnemy() {
	  
	// Register States for animation
	animStates = new AnimationStateHandler(this);
	animStates.addNewState(UNREVEALED_STATE_NAME, UNREVEALED_IDLE_FILE,
			173, 306, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	animStates.addNewState(UNREVEALED_FRIENDLY_STATE_NAME, UNREVEALED_FRIENDLY_FILE,
			173, 306, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	animStates.addNewState(UNREVEALED_UNFRIENDLY_STATE_NAME, UNREVEALED_UNFRIENDLY_FILE,
			173, 306, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	animStates.addNewState(UNREVEALED_FRIENDLY_BLINK_STATE_NAME, UNREVEALED_FRIENDLY_BLINK_FILE,
			173, 306, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	animStates.addNewState(UNREVEALED_UNFRIENDLY_BLINK_STATE_NAME, UNREVEALED_UNFRIENDLY_BLINK_FILE,
			173, 306, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));  
	  
	center.setY(70f); 
    size.set(5.0f, 8.0f);
    updateImage();
    this.removeFromAutoDrawSet();
    
    selected = new Rectangle();
    selected.visible = false;
    selected.setImage("glow.png");
    selected.removeFromAutoDrawSet();
    updateSelected();
    
    currentHighLight = (int)(Math.random() * sparcleFrames.length);
    highLight = new Rectangle();
    highLight.visible = true;
    highLight.removeFromAutoDrawSet();
    updateHighLight();
    highLight.visible = false;
    
    displayedScore = new Text();
    displayedScore.setBackColor(Color.BLACK);
    displayedScore.setFrontColor(Color.WHITE);
    displayedScore.setFontName("Comic Sans MS");
    displayedScore.setFontSize(15);
    displayedScore.removeFromAutoDrawSet();
    displayedScore.visible = false;
    updateDisplayedScore();
    
    health.setMaxSegments(2);
	health.setMaxSegments(health.getMaxSegments());
	health.visible = false;
    health.size.set(size);
    health.size.setY(1.4f);
    health.color = Color.green;
    health.removeFromAutoDrawSet();
    
    infectBar.removeFromAutoDrawSet();
    infectBar.color = new Color(10, 80, 40);
    infectBar.visible = false;  
    
    infectCloud = new Rectangle();
    infectCloud.setImage("infected.png");
    infectCloud.visible = false;
    
  }
  /**
   * Updates the size and position of selected
   */
  private void updateSelected(){
	  selected.center.set(center);
	  selected.size.set(size.getX() * 1.3f, size.getY() * 1.3f);
  }
  /**
   * 
   */
  private void updateHighLight(){
	  if(highLight.visible){
		  highLight.center.set(center);
		  highLight.size.set(size);
		  highLight.size.mult(2);
		  currentHighLight++;
		  if((currentHighLight/3) >= sparcleFrames.length){
			  currentHighLight = 0;
		  }
		  highLight.setImage(sparcleFrames[currentHighLight/3]);
	  }
  }
  /**
   * Will mark this Enemy as Selected
   */
  public void select(){
	  selected.visible = true;
  }
  /**
   * Will mark this enemy as Not selected
   */
  public void deselect(){
	  selected.visible = false;
  }
  public void circleEnemy(boolean circled){
	  highLight.visible = circled;
  }
  public boolean isEnemyCircled(){
	  return highLight.visible;
  }
  /**
   * Returns whether or not this enemy will die if its health falls to zero
   * @return True if this enemy will die if its health falls to zero
   */
  public boolean isInvulnerable(){
	  return invulnerable;
  }
  /**
   * will set whether or not this enemy is invulnerable
   * @param makeInvulnerable
   */
  public void setInvulnerablility(boolean makeInvulnerable){
	  invulnerable = makeInvulnerable;
	  if(invulnerable){
		  health.visible = false;
	  }
  }
  /**
   * updates the size and position of displayedScore
   */
  private void updateDisplayedScore(){
	  displayedScore.center.set(center);
	  displayedScore.center.setX(center.getX() + (size.getX()/3));
	  displayedScore.center.setY(center.getY() + (size.getY()/3));
	  displayedScore.size.setX(1.5f);
	  displayedScore.size.setY(1.5f);
	  displayedScore.setText(""+score);
  }
  private void updateHealth(){
	  if(health != null){
		  health.center.set(center);
		  health.center.setY(health.center.getY() - size.getY()/2);
		  health.size.set(size);
		  health.size.setY(0.7f);
		  
	  }
  }
  /**
   * Will Synk this objects representative with the state of this enemy
   * @param turnTime
   */
  protected void updateRepresentative(){
	  //Checking Representative 
	  if(representative != null){
		  //Repositioning if nessesary and able
		  if(hostSet!= null){
			  if(!hostSet.moveRepresentative(representative, currentRow, currentCollumn)){
				  removeThis();
			  }
		  }
	  }
	  //Creating Representative and moving into position
	  else if(hostSet != null){
		  representative = new InteractableObject();
		  if(!hostSet.moveRepresentative(representative, currentRow, currentCollumn)){
			  removeThis();
		  }
	  }
	  //Updating Representative Type reveal status etc..
	  ((Representative)representative).subject = this;
	  ((Representative)representative).setRevealStatus(isTypeRevealed);
	  ((Representative)representative).setPartialRevealStatus(isPartialRevealed);
	  ((Representative)representative).type = type;
	  ((Representative)representative).resetChanges();
	  ((Representative)representative).setScore(score);
	  ((Representative)representative).setMaxHealth(health.getMaxSegments());
	  ((Representative)representative).setCurrentHealth(health.getfilledSegments());
	  ((Representative)representative).setVulnerability(invulnerable);
	  ((Representative)representative).setInfectStatus(infectedTimer);
	  ((Representative)representative).setHighlight(isEnemyCircled());
  }
  /**
   * will set the current position and grid the enemy think  
   * @param row The Row the Enemy thinks it is at
   * @param collumn The Column the Enemy thinks it is at
   * @param EnemyGrid The grid in which this Enemy Resides
   */
  public final void setPosition(int row, int collumn, EnemySet EnemyGrid){
	  if(EnemyGrid != null && row >= 0 && collumn >= 0){
		  hostSet = EnemyGrid;
		  currentRow = row;
		  currentCollumn = collumn;
		  if(hostNode != null){
			  hostNode.removeNode();
			  hostNode = null;
		  }
	  }
  }
  /**
   * Will set the Infect Status to the Given int
   * Giving a -1 will make this Enemy Permanently Infected
   */
  public void setInfectStatus(int newInfectStatus){
	  infectedTimer = newInfectStatus;
	  if(infectedTimer > 0){
		  infectCloud.visible = true;
		  infectBar.visible = true;
		  if(infectBar.getMaxSegments() < infectedTimer){
			  infectBar.setMaxSegments(infectedTimer);
			  infectBar.setFilledSegments(infectedTimer);
		  }
		  else{
			  infectBar.setFilledSegments(infectedTimer);
		  }
	  }
	  else{
		  infectCloud.visible = false;
		  infectBar.visible = false;
		  infectBar.setMaxSegments(1);
		  infectedTimer = 0;
	  }
  }
  /**
   * will return the current count on the infect time
   * @return the number of turns until the Enemy becomes uninfected
   * -1 indicates that it will never become uninfected
   */
  public int getInfectStatus(){
	  return infectedTimer;
  }
  /**
   * decrements infectedTimer and updates infectBar
   */
  private void updateInfectedStatus(){
	  //infect Bar
	  infectBar.size.set(size);
	  infectBar.size.setY(0.7f);
	  infectBar.center.set(center);
	  infectBar.center.setY(health.center.getY() + (health.size.getY()));
	  
	  //infectCloud
	  infectCloud.size.set(size);
	  infectCloud.center.set(center);
  }
  public void setHiddenColor(HiddenColor value) {
    hiddenColor = value;
  }

  public boolean isAlive() {
    return (visible && !isFalling);
  }

  public boolean isPowerRevealed(){
    return isTypeRevealed;
  }
  /**
   * Will reveal whether or not this Enemy is an Enemy or useful power up
   */
  public void partialReveal(){
	  isPartialRevealed = true;
	  updateImage();
  }
  public StateChange revealIfSetToo(int pauseTime){
	  storedStateChangeObject.setToDefualts();
	  if(setToReveal && !isTypeRevealed){
		  setToReveal = false;
		  return revealType(pauseTime);
	  }
	  else if (!isPartialRevealed)
	  {
		  return unrevealType();
	  }
	  setToReveal = false;
	  updateImage();
	  return storedStateChangeObject;
  }
  /**
   * Will reveal this enemy and return the changes to the game state
   * @param pauseTime, the amount of time the game will be paused for the reveal
   * @return 
   */
  public StateChange revealType(int pauseTime){
	storedStateChangeObject.setToDefualts();
    isTypeRevealed = true;
    justRevealed = true;
    isPartialRevealed = true;
    updateImage();
    displayedScore.visible = true;
    if(!invulnerable && !(isHelpfull && health.getMaxSegments() <= 1)){
    	health.visible = true;
    }
    return storedStateChangeObject;
  }
  public StateChange unrevealType(){
	  health.visible = false;
	  isTypeRevealed = false;
	  justRevealed = false;
	  isPartialRevealed = false;
	  displayedScore.visible = false;
	  updateImage();
	  return storedStateChangeObject;
  }
  /**
   * Will return the health object used by the enemy
   * @return
   */
  public SegmentedBar getHealth(){
	  return health;
  }
  /**
   * will remove this Enemy From the currentEnemyGrid
   * Note this does Reset any previous changes to the stored storedStateChangeObject
   * before making its own changes
   */
  protected StateChange removeThis(){
	  if(hostSet != null){
		  hostSet.removeEnemy(this);
	  }
	  if(hostNode != null){
		  hostNode.removeNode();
	  }
	  selected.destroy();
	  displayedScore.destroy();
	  health.destroy();
	  infectBar.destroy();
	  highLight.destroy();
	  infectCloud.destroy();
	  super.destroy();
	  storedStateChangeObject.setToDefualts();
	  storedStateChangeObject.changeInPlayerScore = score;
	  return storedStateChangeObject;
  }
  /**
   * Trigger this object to escape out of the upper edge of the screen. then remove itself
   * @return
   */
  protected StateChange scare(){
	  setAnimationPosition(0);
	  setAnimationTarget(-2, currentCollumn);
	  selected.destroy();
	  displayedScore.destroy();
	  health.destroy();
	  infectBar.destroy();
	  removeAfterAnimation = true;
	  if(hostSet != null){
		  hostSet.moveEnemytoSubSet(this);
	  }
	  storedStateChangeObject.setToDefualts();
	  storedStateChangeObject.changeInPlayerScore = score;
	  return storedStateChangeObject;
  }
  public InteractableObject.ObjectType getType() {
    return type;
  }
  public void destroy() {
    super.destroy();
  }
  //targeted Enemy Stuff
  public EnemySubSetNode targetEnemy(BaseEnemy target){
	  if(currentTargets != null){
		  return currentTargets.addEnemy(target, false);
	  }
	  return null;
  }
  public boolean isEnemyAlreadyTargeted(BaseEnemy target){
	  if(currentTargets != null){
		  if(currentTargets.findEnemy(target) != null){
			  return true;
		  }
	  }
	  return false;
  }
  /**
   * Will shift the image to another state depending on current
   * state of this object.
   */
  protected void shiftImageState() {
	  if(isPartialRevealed && !isTypeRevealed){
		  if(isHelpfull){
			  animStates.setCycleState(UNREVEALED_FRIENDLY_STATE_NAME,
					  UNREVEALED_FRIENDLY_BLINK_STATE_NAME,
					  DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
		  			  DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
		  }
		  else{
			  animStates.setCycleState(UNREVEALED_UNFRIENDLY_STATE_NAME,
					  UNREVEALED_UNFRIENDLY_BLINK_STATE_NAME,
					  DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
					  DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
		  }
	  }
	  else
	  {
		  animStates.changeState(UNREVEALED_STATE_NAME);
	  }
  }
  
 protected void updateImage() {
	shiftImageState();
  }
  
  /**
   * Will set this objects position within the animation to the given position
   * @param animationPosition	the Desired Animation position
   */
  public void setAnimationPosition(int newAnimationPosition){
	  animPos = newAnimationPosition;
  }
  //Will move towards this target through out the turn and then move to this position at the End of the turn
  public void setAnimationTarget(int row, int collumn){
	  if(hostSet != null){
		  animPosStart.set(super.center);
		  hostSet.getWorldPosition(animPosEnd, row, collumn);
	  }
  }
  /**
   * Will set the center of this objects position to the appropriate position in the animation and increment animPos
   */
  protected void setCenterToAnimatedPosition(){
	  //t = current Time
	  //T = total time to animate
	  //D = Total Distance to Travel
	  //S = Starting point
	  //P = current Position
	  //P = (D / 2)(1 + Cos((((pi / 2) * t)-(T(pi / 2)))/(T / 2))) + S;
	  if(animTime > 0){
		  center.setX((float)(((animPosEnd.getX()-animPosStart.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/2)*(float)animTime)) / ((float)animTime/2f)))+animPosStart.getX()));
		  center.setY((float)(((animPosEnd.getY()-animPosStart.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/2)*(float)animTime)) / ((float)animTime/2f)))+animPosStart.getY()));
	  }
	  else{
		  center.set(animPosEnd);
	  }
	  if(animPos >= animTime - 1) // After animation, stop movement.
	  {
		  setAllowMovement(false);
	  }

  }
  /**
   * Will Draw this Enemy 
   */
  public void draw(){
	  if(visible){
		  super.draw();
		  if(infectCloud.visible){
			  infectCloud.draw();
		  }
		  if(selected.visible){
			  selected.draw();
		  }
		  if(highLight.visible){
			  highLight.draw();
		  }
		  if(displayedScore.visible){
			  displayedScore.draw();
		  }
		  if(health.visible){
			  health.draw();
		  }
		  if(infectBar.visible){
			  infectBar.draw(); 
		  }
	  }
  }
  /**
   * Get the point value that this enemy contains.
   * 
   * @return - The score of this enemy.
   */
  public int getScore() {
    return score;
  }

  public void setScore(int value){
    score = value;
  }
  /**
   *  Called Whenever a Turn has Passed
   *  Requires the current Enemy Set and this Enemies currentPosition in the Set
   *  @returns any change in payer state (By Default returns null)
   */
  public StateChange updateTurn(){
	  //Decrementing Infect Status
	  if(infectedTimer > 0){
		  this.setInfectStatus(infectedTimer-1);
	  }
	  return null;
  }
  public void update(int animationTimer) {
	  // Allows anim manager to switch animations.
	  animStates.update();
	  
	  //Incrementing anim position
	  if(animPos < animTime && doesAllowMovement()){
		  //animating to position
		  setCenterToAnimatedPosition();
		  animPos++; 
	  }
	  else if( removeAfterAnimation){
		  setDeathCountdown();
		  deathCountDown--;
		  if(deathCountDown <= 0)
		  {
			  removeThis();
		  }
		  
	  }
	  deselect();
	  updateSelected();
	  updateHighLight();
	  updateDisplayedScore();
	  updateHealth();
	  updateInfectedStatus();
	  if(hostSet != null && !invulnerable && health.getfilledSegments() <= 0){
		  scare();
	  }
	  super.update();

  }
  private void setDeathCountdown() 
  {
	if(deathCountDown == -1)
	{
		deathCountDown = DEATH_DURATION;
		setDeathAnimation(DEATH_DURATION);
	}
  }
public boolean doesAllowMovement()
  {
	  return allowMovement;
  }
  public void setAllowMovement(boolean movement)
  {
	  allowMovement = movement;
  }
  public void allowMovement(int allowedTime) {
	  allowMovement = true;
	  animTime = allowedTime;
	  animPos = 0;
		
  }
public abstract void playWaitingIntroductions(int timeAllowed);
public abstract StateChange activateAbility(int timeAllowed);
protected abstract void setDeathAnimation(int deathDuration);

}
