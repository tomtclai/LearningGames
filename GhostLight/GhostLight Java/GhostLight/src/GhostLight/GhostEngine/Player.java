
package GhostLight.GhostEngine;

import Engine.BaseCode;
import Engine.Rectangle;
import Engine.ResourceHandler;
import Engine.Vector2;

public class Player extends Rectangle
{
  private LightBeam light = null;
  private int score = 0;			//The Players current score
  protected int currentCollumn = 0;	//The index of the Column the Player Currently Occupies	
  protected int collumnNum = 10;	//The total number of columns available to the
  protected EnemySet hostSet = null;//The Enemy set the player will affecting
  protected int previousColumn = 0;
  protected boolean dieing = false;
  
  private enum PlayerAnimation // TODO: Perhaps remove this semi redundent system.
  {
	  LEFT_IDLE, RIGHT_IDLE, LEFT_HURT, RIGHT_HURT, LEFT_MOVING, RIGHT_MOVING
  }
  
  private PlayerAnimation currentAnim = PlayerAnimation.RIGHT_IDLE;
  private int animationTransitionCounter = -1;
  private int animationSpeed = 0;
  private int movingAnimationSpeed = 0;
  private final static int movingAnimationDuration = 65;
  private AnimationStateHandler animState;
  
  private final static float Y_OFFSET = 10f;
  
  private final static String IDLE_LEFT = "player/Lightbot_IdleLeft_r.png";
  private final static String IDLE_RIGHT = "player/Lightbot_IdleRight_r.png";
  private final static String MOVE_LEFT = "player/Lightbot_WalkLeft_r.png";
  private final static String MOVE_RIGHT = "player/Lightbot_WalkRight_r.png";
  private final static String HURT_LEFT = "player/Lightbot_HurtLeft_r.png";
  private final static String HURT_RIGHT = "player/Lightbot_HurtRight_r.png";

	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(IDLE_LEFT);
		resources.loadImage(IDLE_RIGHT);
		resources.loadImage(MOVE_LEFT);
		resources.loadImage(MOVE_RIGHT);
		resources.loadImage(HURT_LEFT);
		resources.loadImage(HURT_RIGHT);
	}
  
  public Player() {
    size.set(6.0f, 10.0f);
    center.setY(Y_OFFSET);
    light = new LightBeam(hostSet);
    
    animState = new AnimationStateHandler(this);
    
	animState.addNewState(IDLE_RIGHT, IDLE_RIGHT,
			114, 186, 40, 0, new Vector2(6.0f, 10.0f));
	animState.addNewState(IDLE_LEFT, IDLE_LEFT,
			114, 186, 40, 0, new Vector2(6.0f, 10.0f));
	animState.addNewState(MOVE_LEFT, MOVE_LEFT,
			154, 187, 40, movingAnimationSpeed, new Vector2(8.0f, 10.0f));	
	animState.addNewState(MOVE_RIGHT, MOVE_RIGHT,
			157, 186, 40, movingAnimationSpeed, new Vector2(8.0f, 10.0f));	
	animState.addNewState(HURT_RIGHT, HURT_RIGHT,
			150, 186, 20, animationSpeed, new Vector2(7.0f, 10.0f));	
	animState.addNewState(HURT_LEFT, HURT_LEFT,
			150, 188, 20, animationSpeed, new Vector2(7.0f, 10.0f));	
    
	animState.changeState(IDLE_RIGHT);
	
    dieing = false;
  }
  /**
   * Will Increment the Players X and Y positions by the given Increments
   * @param Xincrement The Distance and Direction moved Horizontally 
   * @param Yincrement The Distance and Direction moved Vertically 
   */
  public void incrementPosition(float Xincrement, float Yincrement){
	  super.center.setX(center.getX()+Xincrement);
	  super.center.setY(center.getY()+Yincrement);
  }
  /**
   * Will set the amount of space traveled when the Player moves left or right
   * @param newMoveSpeed the Desired Distance for the player to move
   */
  public void setEnemySet(EnemySet targetSet){
	  hostSet = targetSet;
	  movePlayer(0);
  }
  /**
   * Will set the collumn the player will occupy and the corrospoding world position
   * @param currentCollumnIndex the given column
   */
  public void setCollumn(int currentCollumnIndex){
	  previousColumn = currentCollumn;
	  currentCollumn = currentCollumnIndex;
	  
	  if(hostSet != null){
		  
		  // Begin transition Animation
		  if(currentCollumn > previousColumn)
		  {
			  animationTransitionCounter = movingAnimationDuration;
			  currentAnim = PlayerAnimation.RIGHT_MOVING;
			  animState.changeState(MOVE_RIGHT);
		  }
		  else if(currentCollumn < previousColumn)
		  {
			  animationTransitionCounter = movingAnimationDuration;
			  currentAnim = PlayerAnimation.LEFT_MOVING;
			  animState.changeState(MOVE_LEFT);
		  }
		  
		  //fixing currentCollumn
		  if(currentCollumn >= hostSet.getNumberOfCollumns()){
			  currentCollumn = hostSet.getNumberOfCollumns() - 1;
		  }
		  if(currentCollumn < 0){
			  currentCollumn = 0;
		  }
		  //Calculating knew world position
		  float currentY = Y_OFFSET;
		  hostSet.getWorldPosition(super.center, 0, currentCollumn);
		  center.setY(currentY);
	  }
	  else{
		  center.setX(BaseCode.world.getWidth()/2);
		  
	  }
  }
  /**
   * Will return the collumn the player is currently in
   * @return  the collumn index the player resides
   */
  public int getCurrentCollumn(){
	  return currentCollumn;
  }
  /**
   * Will attempt to move the player by the given amount if this amount 
   * moves the player to a collumn that is outside the bounds of the defined
   * collumn indices then the player will be moved as far as is possible while
   * remaining within the bounds 
   * @param collumnMovement	the number of indecies the player should be moved over
   */
  public void movePlayer(int collumnMovement){
	  //movement breaks lower bound
	  if(hostSet != null){
		  //Calculating ideal collumn
		  currentCollumn += collumnMovement;
		  float currentY = Y_OFFSET;
		  //fixing currentCollumn
		  if(currentCollumn >= hostSet.getNumberOfCollumns()){
			  currentCollumn = hostSet.getNumberOfCollumns() - 1;
		  }
		  if(currentCollumn < 0){
			  currentCollumn = 0;
		  }
		  //Calculating knew world position
		  hostSet.getWorldPosition(super.center, 0, currentCollumn);
		  center.setY(currentY);
	  }
	  else{
		  center.setX(BaseCode.world.getWidth()/2);
	  }
	  
  }
  public boolean isLightActive(){
	  return light.isActive();
  }
  public void turnLightOnorOff(boolean onOff){
	  if(onOff){
		  light.activate(hostSet); 
	  }
	  else{
		  light.Deactivate(hostSet);
	  }
  }
  /**
   *  Will select Enemies within the current Enemy Set that would be affected by the light
   */
  public void selectTargetableEnemies(){
	  if(hostSet != null){
		  light.selectTargetableEnemies(hostSet, currentCollumn);
	  }
  }
  public int getScore(){
	  return score;
  }
  public void setScore(int newScore){
	  if(newScore >= 0){
		  score = newScore;
	  }
  }
  public void incrementScore(int increment){
	  score += increment;
	  if(score < 0){
		  score = 0;
	  }
  }
  public LightBeam getLight(){
    return light;
  }
  private void updateLight(){
    light.update();
    light.setCenterPosition(center.getX(), center.getY() + (size.getY() * 0.5f));
  }
  private void updateAnimation() 
  {
	// First check if in transition mode
	if(animationTransitionCounter >= 0)
	{
		animationTransitionCounter--;	
		if(animationTransitionCounter <= 0)
		{
			if(dieing)
			{
				if(currentAnim == PlayerAnimation.LEFT_MOVING)
				{
					currentAnim = PlayerAnimation.LEFT_HURT;
				    animState.changeState(HURT_LEFT);
				    
				}
				else if(currentAnim == PlayerAnimation.RIGHT_MOVING)
				{
					currentAnim = PlayerAnimation.RIGHT_HURT;
					animState.changeState(HURT_RIGHT);
				    
				}
			}
			else
			{
				if(currentAnim == PlayerAnimation.LEFT_MOVING)
				{
					currentAnim = PlayerAnimation.LEFT_IDLE;
					animState.changeState(IDLE_LEFT);
				    
				}
				else if(currentAnim == PlayerAnimation.RIGHT_MOVING)
				{
					currentAnim = PlayerAnimation.RIGHT_IDLE;
					animState.changeState(IDLE_RIGHT);
				}
			}
		}
	}
  }
  public void update(){
    super.update();
    updateLight();
    updateAnimation();
    
  }
  
	public boolean getHurtState()
	{
		return dieing;
	}
	public void setHurtState(boolean isHurt)
	{
		dieing = isHurt;
	}

}
