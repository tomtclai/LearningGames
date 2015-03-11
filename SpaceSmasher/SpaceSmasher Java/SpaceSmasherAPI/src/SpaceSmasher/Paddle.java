
package SpaceSmasher;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.ResourceHandler;

public class Paddle extends GameObject
{
  private final static String NORMAL_PADDLE = "paddles/Paddle_Normal.png";
  private final static String ICE_PADDLE = "paddles/ice_paddle.png";
  private final static String FIRE_PADDLE = "paddles/fire_paddle.png";
  
  private final static int SPRITESHEET_FRAMES = 120;
  private final static int SPRITE_HEIGHT = 81;
  private final static int SPRITE_WIDTH = 347;
  private final static int TICKS_PER_FRAME = 1;
  
  private PaddleState state;
  private int animationCountdown;
  
  public enum PaddleState
  { NORMAL, ICE, FIRE }
  
  
  // Offsets the left and right bounderys to the inside.
  private final static float BOUNDARY_INNER_OFFSET = 2f;

  private float paddleSpeed = 1.3f;
  /**
   * will load and sounds or images required for this class to function
   * @param resources handler to be used for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(NORMAL_PADDLE);
		  resources.preloadImage(ICE_PADDLE);
		  resources.preloadImage(FIRE_PADDLE);
	  }
  }
  public Paddle()
  {
    setCenter(BaseCode.world.getWidth() * 0.5f,
        (BaseCode.world.getHeight() * 0.04f) * 2.4f);
    setSize(BaseCode.world.getWidth() * 0.14f,
        BaseCode.world.getHeight() * 0.05f);

    setImage(NORMAL_PADDLE);
    state = PaddleState.NORMAL;
  }
  
  /**
   * Retrieves the state of the paddle.
   * @return State of the paddle.
   */
  public PaddleState getState()
  {
	  return state;
  }
  

  public void reflect(Ball ball)
  {
    pushOutCircle(ball);
    ball.bounceOnPaddle(this);
  }

  /**
   * Set how much the paddle moves.
   * 
   * @param value
   *          - The distance the paddle moves when the move methods are called.
   */
  public void setMoveSpeed(float value)
  {
    paddleSpeed = value;
  }

  /**
   * Get how much the paddle moves after calling the move methods.
   * 
   * @return - The amount the paddle would move.
   */
  public float getMoveSpeed()
  {
    return paddleSpeed;
  }

  /**
   * Move the paddle to the left by the default amount. Prevents the paddle from
   * going too far off the edge of the world.
   */
  public void moveLeft()
  {
    setCenterX(getCenterX() - paddleSpeed);
    clampPaddle();
  }

  /**
   * Move the paddle to the right by the default amount. Prevents the paddle from
   * going too far off the edge of the world.
   */
  public void moveRight()
  {
    setCenterX(getCenterX() + paddleSpeed);

    //(center.getX() - (size.getX() * 0.25f)) - BaseCode.world.getWidth();

    clampPaddle();
  }
  
  /**
   * Move the paddle upwards by the default amount. Prevents the paddle from
   * going too far off the edge of the world.
   */
  public void moveUp()
  {
    setCenterY(getCenterY() + paddleSpeed);
    clampPaddle();
  }
 
  /**
   * Move the paddle downwards by the default amount. Prevents the paddle from
   * going too far off the edge of the world.
   */
  public void moveDown()
  {
    setCenterY(getCenterY() - paddleSpeed);
    clampPaddle();
  }
 
  public void clampPaddle()
  {
	  float halfWidth = getWidth() * 0.5f;
	  float halfHeight = getHeight() * 0.5f;
	  
	  // horizontal
	    float offAmountLeft = getCenterX() - halfWidth;
	    float offAmountRight = (getCenterX() + halfWidth) - BaseCode.world.getWidth();
	    
	    // Only clamp if the paddle is lower than the limit
	    if(offAmountLeft < BOUNDARY_INNER_OFFSET) {
	        setCenterX(BOUNDARY_INNER_OFFSET + halfWidth);
	    } else if(offAmountRight > -BOUNDARY_INNER_OFFSET) {
	       setCenterX((BaseCode.world.getWidth() - BOUNDARY_INNER_OFFSET) - halfWidth);
	    }
	   
	    // vertical
	    float offAmountBottom = getCenterY() - halfHeight;
	    float offAmountTop = getCenterY() + halfHeight - BaseCode.world.getHeight();
	    
	    if (offAmountBottom < BOUNDARY_INNER_OFFSET)
	    	setCenterY(BOUNDARY_INNER_OFFSET + halfHeight);
	    else if (offAmountTop > -BOUNDARY_INNER_OFFSET)
	    	setCenterY(BaseCode.world.getHeight() - BOUNDARY_INNER_OFFSET - halfHeight);
	    
  }
  
  public void update()
  {
	  super.update();
	  if(state != PaddleState.NORMAL)
	  {
		  animationCountdown -= TICKS_PER_FRAME;
		  if(animationCountdown <= 0)
		  {
			  setToNormal();
		  }
	  }
  }
  
  public void startFire()
  {
	  state = PaddleState.FIRE;
	  this.setSpriteSheet(FIRE_PADDLE,
			  SPRITE_WIDTH,
			  SPRITE_HEIGHT,
			  SPRITESHEET_FRAMES,
			  TICKS_PER_FRAME);
	  animationCountdown = TICKS_PER_FRAME * SPRITESHEET_FRAMES * 2;
	  setUsingSpriteSheet(true);
  }
  
  public void startIce()
  {
	  state = PaddleState.ICE;
	  this.setSpriteSheet(ICE_PADDLE,
			  SPRITE_WIDTH,
			  SPRITE_HEIGHT,
			  SPRITESHEET_FRAMES,
			  TICKS_PER_FRAME);
	  animationCountdown = TICKS_PER_FRAME * SPRITESHEET_FRAMES * 2;
	  setUsingSpriteSheet(true);
  }
  public void setToNormal()
  {
	  state = PaddleState.NORMAL;
	  setImage(NORMAL_PADDLE);
	  setUsingSpriteSheet(false);
  }
  
  
}