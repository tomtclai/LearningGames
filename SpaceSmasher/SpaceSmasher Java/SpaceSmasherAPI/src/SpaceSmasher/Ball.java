
package SpaceSmasher;

import java.util.Random;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.ResourceHandler;
import Engine.Vector2;
import Engine.World;
import Engine.World.BoundCollidedStatus;

public class Ball extends GameObject
{
  protected static final String BLOCK_BOUNCE_SOUND = "sounds/AlternaBounce2SpaceSmasher.wav";
  protected static final String WALL_BOUNCE_SOUND = "Ball-Wall Collision.wav";
  protected static final String JOKER_BOUNCE_SOUND = "sounds/LaughSpaceSmasher.wav";
  protected static final String FREEZE_BLOCK_SOUND = "sounds/Ice1.wav";
  protected static final String BREAK_ICE_SOUND = "sounds/Ice2.wav";
  protected static final String DIE_SOUND = "sounds/Laser1.wav";
  protected static final String FIRE_SOUND = "sounds/Fire.wav";
  
  protected static final String NORMAL_IMAGE = "ball/Ball_Normal.png";
  protected static final String FIRE_IMAGE = "ball/Ball_Fire.png";
  protected static final String ICE_IMAGE = "ball/Ball_Ice.png";
  
  protected float upperWorldBound = 0;
  protected float lowerWorldBound = 0;
  protected float rightWorldBound = 0;
  protected float leftWorldBound = 0;
  
  private float speedMod = 1.0f;

  private BlockSet blocks = null;

  public enum BallType
  {
    NORMAL, FIRE, ICE
  };

  private BallType type = BallType.NORMAL;
  private int powerBouncesLeft = -1;
  
  private static Random rand = new Random();
  
  /**
   * will load and sounds or images required for this class to function
   * @param resources the handler to use to pre-load the image and audio files.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(NORMAL_IMAGE);
		  resources.preloadImage(FIRE_IMAGE);
		  resources.preloadImage(ICE_IMAGE);
		  resources.preloadSound(JOKER_BOUNCE_SOUND);
		  resources.preloadSound(FREEZE_BLOCK_SOUND);
		  resources.preloadSound(BLOCK_BOUNCE_SOUND);
		  resources.preloadSound(WALL_BOUNCE_SOUND);
		  resources.preloadSound(BREAK_ICE_SOUND);
		  resources.preloadSound(DIE_SOUND);
		  resources.preloadSound(FIRE_SOUND);
	  }
  }
  
  public Ball()
  {
    setToInvisible();
    setSize(BaseCode.world.getHeight() * 0.06f);
    setCenter(BaseCode.world.getWidth() * 0.5f,
        BaseCode.world.getHeight() * 0.3f);
    
    upperWorldBound = BaseCode.world.getHeight();
    rightWorldBound = BaseCode.world.getWidth();
    
  }

  /**
   * Change the power up type of the ball.
   * 
   * @param value
   *          - The power up type the ball should have.
   */
  public void setType(BallType value)
  {
    type = value;
    updateImage();
  }

  /**
   * Create the ball and prepare it for play.
   * @param activePaddle if null will spawn the ball at the center of the screen just below the activeBlocks aching down.
   * otherwise will spawn the ball arching upwards from the paddle given
   */
  public void spawn(Paddle activePaddle)
  {
    setToVisible();
    setType(BallType.NORMAL);

    //rand.setSeed(0);
    setVelocity((rand.nextFloat() * 1.4f) - 0.7f, -0.7f);
    		// direction is constrained within 45-degrees from vertical
    		//
    
    //velocity.set(1.0f, -2.5f);
    getVelocity().normalize();

    getVelocity().mult(speedMod);

    boolean setPosition = false;

    if(blocks != null) {
      Block lastBlock = blocks.getLowestVisibleBlock();

      if(lastBlock != null) {
        setCenter(BaseCode.world.getWidth() * 0.5f, lastBlock.getCenterY() -
            lastBlock.getHeight() - (BaseCode.world.getHeight() * 0.1f));

        setPosition = true;
      }
    }
    //Setting Position if nessesary
    if(activePaddle != null){
    	setVelocityY(-getVelocityY());
    	setCenter(activePaddle.getCenter());
    	setCenterY(getCenterY() + (getHeight()/2f) + (activePaddle.getHeight()/2f) + 0.2f);
    }
    else if(!setPosition) {
      setCenter(BaseCode.world.getWidth() * 0.5f,
          BaseCode.world.getHeight() * 0.5f);
    }
  }

  /**
   * Set the block set to base ball spawn positions on.
   * 
   * @param value
   *          - The block set.
   */
  public void setBlockSet(BlockSet value)
  {
    blocks = value;
  }
  
  /**
   * Temporarily modify the speed of the ball to the given percentage of the
   * unmodified speed.
   * 
   * @param newMod
   *          - The new percentage of the original speed to apply to the ball
   *          each update.
   */
  public void setSpeedMod(float newMod)
  {
    if(newMod == 0.0f)
    {
      newMod = 1.0f;
    }

    // Change from previous speed mod to the new speed mod
    speedMod = (newMod / speedMod);

    getVelocity().mult(speedMod);

    speedMod = newMod;
  }

  /**
   * Check if the ball is normal.
   * 
   * @return - True if the ball is normal, false otherwise.
   */
  public boolean isNormal()
  {
    return (type == BallType.NORMAL);
  }

  /**
   * Check if the ball is burning.
   * 
   * @return - True if the ball is burning, false otherwise.
   */
  public boolean isBurning()
  {
    return (type == BallType.FIRE);
  }

  /**
   * Check if the ball is freezing.
   * 
   * @return - True if the ball is freezing, false otherwise.
   */
  public boolean isFrozen()
  {
    return (type == BallType.ICE);
  }

  /**
   * Reset to normal state for the ball.
   */
  public void normalTheBall()
  {
    setType(BallType.NORMAL);
    setPowerBouncesLeft(0);
  }

  
  
  /**
   * Enable the freeze state for the ball.
   * @param duration number of update calls (about 40 per second)
   */
  public void freezeTheBall(int duration)
  {
    setType(BallType.ICE);
    setPowerBouncesLeft(duration);
  }

  /**
   * Enable the freeze state for the ball.
   */
  public void freezeTheBall()
  {
    freezeTheBall(7);
  }

  /**
   * Enable the fire state for the ball.
   * @param duration number of update calls (about 40 per second) 
   */
  public void burnTheBall(int duration)
  {
    setType(BallType.FIRE);
    setPowerBouncesLeft(duration);
  }

  /**
   * Enable the fire state for the ball.
   */
  public void burnTheBall()
  {
    burnTheBall(3);
  }
  /**
   * Check if the primitive is touching or outside of any side of the world.
   * @return the part of the world the Ball is overlapping with
   */
  public World.BoundCollidedStatus collideWorldBound(){
	  //Top
	  if(getCenterY() + (getHeight()/2) >= upperWorldBound){
		  return BoundCollidedStatus.TOP;
	  }
	  //Bottom
	  else if(getCenterY() - (getHeight()/2) < lowerWorldBound){
		  return BoundCollidedStatus.BOTTOM;
	  }
	  //Left
	  if(getCenterX() - (getWidth()/2) < leftWorldBound){
		  return BoundCollidedStatus.LEFT;
	  }
	  //Right
	  else if(getCenterX() + (getWidth()/2) >= rightWorldBound){
		  return BoundCollidedStatus.RIGHT;
	  }
	  return BoundCollidedStatus.INSIDEBOUND;
  }
  /**
   * Reflect off an object that is to the top. Will also clamp at world bounds.
   */
  public void reflectTop()
  {
    setVelocityY(getVelocityY() * -1.0f);
    BaseCode.world.clampAtWorldBound(this);
  }

  /**
   * Reflect off an object that is to the bottom. Will also clamp at world
   * bounds.
   */
  public void reflectBottom()
  {
    setVelocityY(getVelocityY() * -1.0f);
    BaseCode.world.clampAtWorldBound(this);
  }

  /**
   * Reflect off an object that is to the left. Will also clamp at world bounds.
   */
  public void reflectLeft()
  {
	if(getVelocityX() < 0)
		setVelocityX(getVelocityX() * -1.0f);
    BaseCode.world.clampAtWorldBound(this);
  }

  /**
   * Reflect off an object that is to the right. Will also clamp at world
   * bounds.
   */
  public void reflectRight()
  {
	if(getVelocityX() > 0)
		setVelocityX(getVelocityX() * -1.0f);
    BaseCode.world.clampAtWorldBound(this);
  }

  /**
   * Set the number of bounces on a block before returning to a normal ball.
   * 
   * @param durationInBounces
   *          - Number of bounces power up will last.
   */
  public void setPowerBouncesLeft(int durationInBounces)
  {
    if(powerBouncesLeft > 0 || durationInBounces > 0)
    {
      powerBouncesLeft = durationInBounces;

      // If there are no bounces left
      if(powerBouncesLeft == 0)
      {
        powerBouncesLeft = -1;

        normalTheBall();
      }
    }
  }

  /**
   * Reduce the number of bounces a power up has left by one.
   */
  public void reducePowerBouncesLeft()
  {
    setPowerBouncesLeft(powerBouncesLeft - 1);
  }

  /**
   * Update the ball's image to match the power up it has.
   */
  protected void updateImage()
  {
    if(type == BallType.NORMAL)
    {
      setImage(NORMAL_IMAGE);
    }
    else if(type == BallType.FIRE)
    {
      setImage(FIRE_IMAGE);
    }
    else if(type == BallType.ICE)
    {
      setImage(ICE_IMAGE);
    }
  }

  /**
   * Will Play the  sound the ball should make when it bounces
   */
  public void playBounceSound()
  {
    BaseCode.resources.playSound(BLOCK_BOUNCE_SOUND);
  }
  /**
   * Will play the sound the ball should make when it bounces of of a wall
   */
  public void playWallBounceSound(){
	  BaseCode.resources.playSound(WALL_BOUNCE_SOUND);
  }
  /**
   * Will play the sound the ball should make when it freezes a block
   */
  public void playBallFreezeBlockSound(){
	  BaseCode.resources.playSound(FREEZE_BLOCK_SOUND);
  }
  /**
   * Will play the sound the ball should make when it shatters something
   */
  public void playBallShatterSound(){
	  BaseCode.resources.playSound(BREAK_ICE_SOUND);
  }
  /**
   * Will play the sound the ball should make when it hits a Joker
   */
  public void playBallJokerSound(){
	  BaseCode.resources.playSound(JOKER_BOUNCE_SOUND);
  }
  /**
   * Play the sound the ball should make when it dies, usually by falling off
   * the bottom of the world.
   */
  public void playDieSound()
  {
    BaseCode.resources.playSound(DIE_SOUND);
  }
  /**
   * Will play the sound the ball should make when it is ignited
   */
  public void playBallIgnitedSound(){
	  BaseCode.resources.playSound(FIRE_SOUND);
  }

  /**
   * Reflect velocity on whichever axis is not zero.
   * 
   * @param dir
   *          - Direction to bounce in.
   */
  public void bounce(Vector2 dir)
  {
    if(dir != null)
    {
      if(dir.getX() != 0.0f)
      {
        setVelocityX(getVelocityX() * -1.0f);
      }

      if(dir.getY() != 0.0f)
      {
        setVelocityY(getVelocityY() * -1.0f);
      }
    }
  }

  /**
   * Bounce the ball off the given paddle based on where it hit the paddle.
   * 
   * @param paddle
   *          - The paddle to bounce off.
   */
  public void bounceOnPaddle(GameObject paddle)
  {
	  Vector2 v = getVelocity();
	  float speed = v.length();
	  
	  v.setX(getCenterX() - paddle.getCenterX());
	  v.setY(getCenterY() - paddle.getCenterY());
	  
	
	  // check velocity constraint: to avoid horizontal-major movement
	  float r = v.getX() / v.getY();
	  if (r > 1.0f) // same sign
		  v.setX(v.getY());
	  else if (r < -1.0f)
		  v.setX(-v.getY());
	  
	  v.normalize();
	  v.mult(speed);
  }
}
