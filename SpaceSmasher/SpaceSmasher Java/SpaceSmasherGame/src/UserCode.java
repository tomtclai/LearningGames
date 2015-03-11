import java.awt.event.KeyEvent;
import Engine.Vector2;
import Engine.World.BoundCollidedStatus;
import Engine.GameObject;
import SpaceSmasher.Ball;
import SpaceSmasher.Block;
import SpaceSmasher.Paddle;
import SpaceSmasher.SpaceSmasher;
import SpaceSmasher.Trap;
import SpaceSmasher.Block.BlockState;
import SpaceSmasher.Block.BlockType;

public class UserCode extends SpaceSmasher
{
  private boolean jokerBlockSwapped = false;
  private GameObject leftGenerator;
  private GameObject rightGenerator;
  private int DEFAULT_SCORE_PER_BLOCK = 100;
    
  protected void initialize()
  {
	  preloadResources();
	  
	  // Still deciding where this should go....
      leftGenerator = new GameObject(5f,3f, 6f,5f);
      leftGenerator.setImage("tinyTransparent.png");
      
      rightGenerator = new GameObject(95f, 3f, 6f,5f);
      rightGenerator.setImage("tinyTransparent.png");


    jokerBlockSwapped = false;
    lifeSet.add(5);
    paddleSet.add(1);
    ballSet.add(1);
    ballSet.get(0).setToInvisible();
    blockSet.setBlocksPerRow(7);

    for(int loop = 0; loop < blockSet.getNumColumns() * 5; loop++){
        double currentPercent = Math.random();
        //30% Normal
        if(currentPercent <= 0.3){
            blockSet.addNormalBlock(1);
        }
        //25% Active cage
        else if(currentPercent <= 0.55){
            blockSet.addActiveCageBlock(1);
        }
        //35% Inactive Cage
        else if(currentPercent <= 0.9){
            blockSet.addInactiveCageBlock(1);
        }
        //4% ice
        else if(currentPercent <= 0.94){
            blockSet.addFreezingBlock(1);
        }
        //4% fire
        else if(currentPercent <= 0.98){
            blockSet.addFireBlock(1);
        }
        //2% Joker
        else{
            blockSet.addJokerBlock(1);
        }
        
    }

    /*blockSet.addNormalBlock(1);
    blockSet.addFireBlock(1);
    blockSet.addNormalBlock(1);
    blockSet.addFreezingBlock(1);
    blockSet.addNormalBlock(1);
    blockSet.addCageBlock(1);
    blockSet.addNormalBlock(1);

    blockSet.addNormalBlock(4);
    blockSet.addCageBlock(1);
    blockSet.addNormalBlock(1);
    blockSet.addCageBlock(1);

    blockSet.addJokerBlock(1);
    blockSet.addNormalBlock(4);
    blockSet.addCageBlock(1);
    blockSet.addNormalBlock(1);*/

    //blockSet.addUnbreakableBlock(7);

    trapSet.add(2);

    //setDebugMode(true);
    //setAllowDebug(false);
    
  }

  protected void update()
  {
    Paddle paddle = paddleSet.get(0);
    Ball ball = ballSet.get(0);

    if(blockSet.allBlocksAreDead())
    {
      gameWin();
    }
    else
    {
      // Spawn ball
      if(keyboard.isButtonTapped(KeyEvent.VK_SPACE) || 
    		  (mouse.MouseOnScreen() && mouse.isButtonTapped(1)))
      {
    	  if(!ball.isVisible())
    	  {
    		  ball.spawn(paddle);
    	  }
      }
      
      if(keyboard.isButtonTapped(KeyEvent.VK_I))
      {
    	  blockSet.setBlockAt(1, 2, BlockType.JOKER); 
      }

      paddleMovement(paddle);

      if(ball != null && ball.isVisible())
      {
        ballWorldCollision(ball);

        // Paddle and ball collision
        if(paddle.collided(ball))
        {
          paddle.reflect(ball);
          ball.playBounceSound();
        }

        ballTrapCollision(trapSet.left(), ball);
        ballTrapCollision(trapSet.right(), ball);

        // Switch and ball collision
        if(theSwitch.isActive() && theSwitch.collided(ball))
        {
          if(this.trapSet.left().isActive())
          {
              theSwitch.deactivate();
              theSwitch.playDeactivationSound();
              trapSet.deactivate();
              blockSet.toggleUnbreakables();
          }
          else
          {
              ball.playBounceSound();
          }
          theSwitch.reflect(ball);

        }

        // Checks to see ball and paddle states are synchronized.
        if(ball.isBurning())
        {
            if(paddle.getState() == Paddle.PaddleState.NORMAL)
                ball.setType(Ball.BallType.NORMAL);
            else if(paddle.getState() == Paddle.PaddleState.ICE)
                ball.setType(Ball.BallType.ICE);
        }
        if(ball.isFrozen())
        {
            if(paddle.getState() == Paddle.PaddleState.NORMAL)
                ball.setType(Ball.BallType.NORMAL);
            else if(paddle.getState() == Paddle.PaddleState.FIRE)
                ball.setType(Ball.BallType.FIRE);
        }

        if(blockSet.isBallCollidingWithABlock(ball))
        {
          if(ball.isNormal())
          {
            normalBallBlockCollision(ball);
          }
          else if(ball.isBurning())
          {
            burningBallBlockCollision(ball);
          }
          else if(ball.isFrozen())
          {
            freezingBallBlockCollision(ball);
          }
        }
      }
    }
  }

  /**
   * Check for and react to the ball colliding with the would bounds.
   *
   * @param ball
   *          - The ball to check collision with.
   */
  private void ballWorldCollision(Ball ball)
  {
    BoundCollidedStatus status = ball.collideWorldBound();
    switch(status)
    {
      case TOP:
      {
        ball.reflectTop();
        ball.playBounceSound();

        break;
      }

      case BOTTOM:
      {
        ball.setToInvisible();
        ball.playDieSound();

        lifeSet.remove();

        if(lifeSet.getCount() < 1)
        {
          gameLost();
        }

        break;
      }

      case LEFT:
      {
        ball.reflectLeft();
        ball.playBounceSound();

        break;
      }

      case RIGHT:
      {
        ball.reflectRight();
        ball.playBounceSound();

        break;
      }

      // Catch the case where the ball is inside the
      // world and not hitting any bounds. A warning is
      // given if all cases are not handled.
      default:
        break;
    }
  }

  /**
   * Move the given paddle based on user input.
   *
   * @param paddle
   *          - The paddle to move.
   */
  private void paddleMovement(Paddle paddle)
  {
    if(keyboard.isButtonDown(KeyEvent.VK_LEFT))
    {
      paddle.moveLeft();
    }

    if(keyboard.isButtonDown(KeyEvent.VK_RIGHT))
    {
      paddle.moveRight();
    }
    
    /*
    if(keyboard.isButtonDown(KeyEvent.VK_UP))
      paddle.moveUp();
    
    if(keyboard.isButtonDown(KeyEvent.VK_DOWN))
        paddle.moveDown();
      */
     
    if(mouse.MouseOnScreen())
    {
    	paddle.setCenterX(mouse.getWorldX());
    	// paddle.setCenterY(mouse.getWorldY());
    	paddle.clampPaddle();
    }
  }

  /**
   * Checks for and reacts to the ball colliding with a trap.
   *
   * @param ball
   *          - The ball to check collision with.
   */
  private void ballTrapCollision(Trap trap, Ball ball)
  {
    if(trap != null && trap.collided(ball)){
      trap.reflect(ball);
      if(trap.isNotActive()){
          trapSet.activate();
          blockSet.toggleUnbreakables();
          theSwitch.activate();
          theSwitch.playActivationSound();
      }
      else
      {
          ball.playBounceSound();
      }
    }
  }

  /**
   * Collision between a normal ball and a block.
   *
   * @param ball
   *          - The ball to check collision with.
   * @param blockType
   *          - The type of block the ball collided with.
   */
  private void normalBallBlockCollision(Ball ball)
  {
    Block block = blockSet.getCollidedBlock(ball);

    switch(block.getType())
    {
      case NORMAL:
      {
        playSoundBasedOnState(block.getState(), ball);

        block.reflect(ball);
        blockSet.remove(block);

        increaseScore(DEFAULT_SCORE_PER_BLOCK);
        break;
      }

      case FREEZING:
      {
        block.reflect(ball);
        ball.playBounceSound();

        if(block.isPowerRevealed())
        {
          ball.freezeTheBall();
          blockSet.remove(block);
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
          paddleSet.get(0).startIce();
        }
        else
        {
          block.revealPower();
        }

        break;
      }

      case FIRE:
      {
        block.reflect(ball);
        if(block.isPowerRevealed())
        {
          ball.burnTheBall();
          blockSet.remove(block);
          ball.playBallIgnitedSound();
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
          paddleSet.get(0).startFire();
        }
        else
        {
          block.revealPower();
          ball.playBounceSound();
        }

        break;
      }

      case JOKER:
      {
        ballHitJoker(ball, block);
        ball.playBallJokerSound();

        break;
      }

      case CAGE_ACTIVE:
      {
        block.reflect(ball);
        ball.playBounceSound();

        break;
      }
      case CAGE_INACTIVE:
      {
        block.reflect(ball);
        playSoundBasedOnState(block.getState(), ball);
        blockSet.remove(block);
        increaseScore(DEFAULT_SCORE_PER_BLOCK);

        break;
      }

      default:
        break;
    }
  }

  /**
   * Collision between a fire ball and a block.
   *
   * @param ball
   *          - The ball to check collision with.
   * @param blockType
   *          - The type of block the ball collided with.
   */
  private void burningBallBlockCollision(Ball ball)
  {
    Block block = blockSet.getCollidedBlock(ball);

    switch(block.getType())
    {
      case NORMAL:
      {
          playSoundBasedOnState(block.getState(), ball);
        blockSet.remove(block);
        increaseScore(DEFAULT_SCORE_PER_BLOCK);
        break;
      }

      case FREEZING:
      {
        ball.playBounceSound();

        if(block.isPowerRevealed())
        {
          blockSet.remove(block);
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
        }
        else
        {
          block.reflect(ball);
          block.revealPower();
        }

        break;
      }

      case FIRE:
      {
        if(block.isPowerRevealed())
        {
          blockSet.remove(block);
          ball.playBallIgnitedSound();
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
        }
        else
        {
          block.reflect(ball);
          ball.playBounceSound();
          block.revealPower();
        }

        break;
      }

      case JOKER:
      {
        ballHitJoker(ball, block);
        ball.playBallJokerSound();

        break;
      }

      case CAGE_ACTIVE:
      {
        block.reflect(ball);
        ball.playBounceSound();

        break;
      }
      case CAGE_INACTIVE:
      {
          playSoundBasedOnState(block.getState(), ball);
          blockSet.remove(block);
          increaseScore(DEFAULT_SCORE_PER_BLOCK);

          break;
      }

      default:
        break;
    }
  }

  /**
   * Collision between a freezing ball and a block.
   *
   * @param ball
   *          - The ball to check collision with.
   * @param blockType
   *          - The type of block the ball collided with.
   */
  private void freezingBallBlockCollision(Ball ball)
  {
    Block block = blockSet.getCollidedBlock(ball);

    switch(block.getType())
    {
      case NORMAL:
      {
        block.reflect(ball);
        playSoundBasedOnState(block.getState(), ball);

        if(block.getState() != BlockState.FROZEN)
        {
          block.freezeTheBlock();
        }
        else
        {
          blockSet.remove(block);
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
        }

        break;
      }

      case FREEZING:
      {
        block.reflect(ball);

        if(!block.isPowerRevealed())
        {
          block.revealPower();
          ball.playBounceSound();
        }
        else if(block.getState() != BlockState.FROZEN)
        {
          block.freezeTheBlock();
          ball.playBallFreezeBlockSound();
        }
        else
        {
          blockSet.remove(block);
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
          ball.freezeTheBall();
          paddleSet.get(0).startIce();
        }

        break;
      }

      case FIRE:
      {
        block.reflect(ball);
        ball.playBounceSound();

        if(!block.isPowerRevealed())
        {
          block.revealPower();
        }
        else if(block.getState() != BlockState.FROZEN)
        {
          block.freezeTheBlock();
          ball.playBallFreezeBlockSound();
          paddleSet.get(0).startIce();
        }
        else
        {
          blockSet.remove(block);
          increaseScore(DEFAULT_SCORE_PER_BLOCK);
          ball.burnTheBall();
          ball.playBallIgnitedSound();
          paddleSet.get(0).startFire();
        }

        break;
      }

      case JOKER:
      {
        ballHitJoker(ball, block);
        ball.playBallJokerSound();
        break;
      }

      case CAGE_ACTIVE:
      {
        block.reflect(ball);
        ball.playBounceSound();

        break;
      }

      case CAGE_INACTIVE:
      {
          block.reflect(ball);
          playSoundBasedOnState(block.getState(), ball);

          if(block.getState() != BlockState.FROZEN)
          {
            block.freezeTheBlock();
          }
          else
          {
            blockSet.remove(block);
            increaseScore(DEFAULT_SCORE_PER_BLOCK);
          }

          break;
      }

      default:
        break;
    }
  }

  /**
   * Collision between a ball and a joker block.
   *
   * @param ball
   *          - The ball to check collision with.
   * @param jokerBlock
   *          - The joker block that a collision occurred with.
   */
  private void ballHitJoker(Ball ball, Block jokerBlock)
  {
    jokerBlock.reflect(ball);
    ball.playBounceSound();

    if(jokerBlockSwapped)
    {
      blockSet.remove(jokerBlock);
      increaseScore(DEFAULT_SCORE_PER_BLOCK);
    }
    else
    {
      // Swap with a cage block
      Block blockToReplace = blockSet.getLastCageBlock();

      if(blockToReplace != null)
      {
        jokerBlockSwapped = true;

        Vector2 temp = jokerBlock.getCenter();
        jokerBlock.setCenter(blockToReplace.getCenter());
        blockToReplace.setCenter(temp);
      }
    }
  }
 
  /**
   * Plays the expected sound a ball should make when colliding with
   * a block depending on the states of the ball and block.
   * @param state
   * @param b
   */
  private void playSoundBasedOnState(BlockState state, Ball b)
  {
      if(state == BlockState.FROZEN){
        b.playBallShatterSound();
    }
    else if(b.isFrozen())
    {
        b.playBallFreezeBlockSound();
    }
    else{
        b.playBounceSound();
    }
  }
}
