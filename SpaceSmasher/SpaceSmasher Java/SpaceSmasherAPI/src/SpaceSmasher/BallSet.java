
package SpaceSmasher;

import java.util.Vector;

import Engine.GameObject;
import Engine.SetOfGameObjects;
import SpaceSmasher.Block.BlockType;

@SuppressWarnings("serial")
public class BallSet extends SetOfGameObjects<Ball>
{
  private float speedMod = 1.0f;

  private BlockSet blocks = null;
  /** If Positive is communicated to the ball to use as its upper bound instead of the World Upper Bound */
  float upperBound = -1;
  /** If Positive is communicated to the ball to use as its upper bound instead of the World Lower Bound */
  float lowerBound = -1;
  /** If Positive is communicated to the ball to use as its upper bound instead of the World left Bound */
  float rightBound = -1;
  /** If Positive is communicated to the ball to use as its upper bound instead of the World right Bound */
  float leftBound = -1;
  
  /**
   * Set the visibility of all balls.
   * 
   * @param value
   *          - True for visible, false for not visible.
   */
  public void setVisible(boolean value)
  {
    for(int i = 0; i < size(); i++)
    {
    	get(i).setVisibilityTo(value);
    }
  }

  /**
   * Check if there are no more balls in play.
   * 
   * @return True if no balls are alive, false otherwise.
   */
  public boolean allBallsDead()
  {
    for(int i = 0; i < size(); i++)
    {
      if(get(i).isVisible())
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Set the modifier for all ball velocities to the given amount.
   * 
   * @param newMod The modifier value for the balls' velocity.
   */
  public void setSpeedMod(float newMod)
  {
    if(newMod == 0.0f)
    {
      newMod = 1.0f;
    }

    for(int i = 0; i < size(); i++)
    {
      get(i).setSpeedMod(newMod);
    }

    speedMod = newMod;
  }

  /**
   * Checks if any ball is colliding with the given object.
   * 
   * @param obj
   *          - The object to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  private boolean anyBallCollided(GameObject obj)
  {
    if(obj != null)
    {
      for(int i = 0; i < size(); i++)
      {
        if(get(i).collided(obj))
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if any ball is colliding with any object in the other set.
   * 
   * @param theSet
   *          - The set of objects to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  private <T extends GameObject> boolean anyBallCollided(SetOfGameObjects<T> theSet)
  {
    if(theSet != null)
    {
      for(int i = 0; i < theSet.size(); i++)
      {
        if(anyBallCollided(theSet.get(i)))
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Check if any ball is colliding with the given paddle.
   * 
   * @param paddle
   *          - The paddle to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  public boolean anyBallCollidedWithPaddle(Paddle paddle)
  {
    return anyBallCollided(paddle);
  }

  /**
   * Checks if any ball is colliding with any wall in the given set.
   * 
   * @param theSet
   *          - The set of walls to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  public boolean anyBallCollidedWithWalls(TrapSet theSet)
  {
    return anyBallCollided(theSet);
  }

  /**
   * Checks if any ball is colliding with any block in the given set.
   * 
   * @param theSet
   *          - The set of blocks to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  public boolean anyBallCollidedWithBlocks(BlockSet theSet)
  {
    return anyBallCollided(theSet);
  }

  //
  //
  //

  /**
   * Checks if any ball is colliding with any block of the given type in the
   * given set.
   * 
   * @param theSet
   *          - The set of walls to check collision with.
   * @param type
   *          - The type of block to check for.
   * @return - True if a ball is colliding, false otherwise.
   */
  private boolean anyBallCollided(BlockSet theSet, BlockType type)
  {
    if(theSet != null)
    {
      Block curBlock;

      for(int i = 0; i < theSet.size(); i++)
      {
        curBlock = theSet.get(i);

        if(curBlock.getType() == type && curBlock.isVisible() &&
            anyBallCollided(curBlock))
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if any ball is colliding with any normal block in the given set.
   * 
   * @param theSet
   *          - The set of walls to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  public boolean anyBallCollidedWithNormalBlock(BlockSet theSet)
  {
    return anyBallCollided(theSet, BlockType.NORMAL);
  }

  /**
   * Checks if any ball is colliding with any fire block in the given set.
   * 
   * @param theSet
   *          - The set of walls to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  public boolean anyBallCollidedWithFireBlock(BlockSet theSet)
  {
    return anyBallCollided(theSet, BlockType.FIRE);
  }

  /**
   * Checks if any ball is colliding with any freezing block block in the given
   * set.
   * 
   * @param theSet
   *          - The set of walls to check collision with.
   * @return - True if a ball is colliding, false otherwise.
   */
  public boolean anyBallCollidedWithFreezingBlock(BlockSet theSet)
  {
    return anyBallCollided(theSet, BlockType.FREEZING);
  }

  //
  //
  //

  /**
   * Get which balls collide with the given items.
   * 
   * @param obj
   *          - The given item to check collision with.
   * @return - The list of balls that collide with the given object.
   */
  private Vector<Ball> getCollidedList(GameObject obj)
  {
    Vector<Ball> list = new Vector<Ball>();

    if(obj != null)
    {
      for(int i = 0; i < size(); i++)
      {
        if(get(i).collided(obj))
        {
          list.add(get(i));
        }
      }
    }

    return list;
  }

  /**
   * Get which balls collide with the given set of items.
   * 
   * @param theSet
   *          - The set of items to check for collision with.
   * @return - The list of balls that collide with the other set.
   */
  private <T extends GameObject> Vector<Ball> getCollidedList(
      SetOfGameObjects<T> theSet)
  {
    Vector<Ball> list = new Vector<Ball>();

    if(theSet != null)
    {
      for(int i = 0; i < theSet.size(); i++)
      {
        list.addAll(getCollidedList(theSet.get(i)));
      }
    }

    return list;
  }

  /**
   * Get a list of balls that are colliding with the given paddle.
   * 
   * @param paddle
   *          - The paddle to check collision with.
   * @return - List of balls that collide with the paddle.
   */
  public Vector<Ball> getCollidedListOfBalls(Paddle paddle)
  {
    return getCollidedList(paddle);
  }

  /**
   * Get a list of balls that are colliding with the given trap set.
   * 
   * @param theSet
   *          - The set of traps to check collision with.
   * @return - List of balls that collide with the traps.
   */
  public Vector<Ball> getCollidedListOfBallsWithWalls(TrapSet theSet)
  {
    return getCollidedList(theSet);
  }

  /**
   * Get a list of balls that are colliding with the given block set.
   * 
   * @param theSet
   *          - The set of blocks to check collision with.
   * @return - List of balls that collide with the bricks.
   */
  public Vector<Ball> getCollidedListOfBallsWithBlocks(BlockSet theSet)
  {
    return getCollidedList(theSet);
  }

  //
  //
  //

  /*
  private Vector<Ball> getCollidedListOfBallWithBlock(BlockSet theSet,
      BlockType type)
  {
    Vector<Ball> list = new Vector<Ball>();

    if(theSet != null)
    {
      Block curBlock;

      for(int i = 0; i < theSet.size(); i++)
      {
        curBlock = theSet.get(i);

        if(curBlock.type == type && curBlock.getIsSolid())
        {
          list.addAll(getCollidedList(curBlock));
        }
      }
    }

    return list;
  }

  private Vector<Ball> getCollidedListOfBallWithBlock(BlockSet theSet,
      BlockState state)
  {
    Vector<Ball> list = new Vector<Ball>();

    if(theSet != null)
    {
      Block curBlock;

      for(int i = 0; i < theSet.size(); i++)
      {
        curBlock = theSet.get(i);

        if(curBlock.state == state && curBlock.getIsSolid())
        {
          list.addAll(getCollidedList(curBlock));
        }
      }
    }

    return list;
  }

  private Vector<Ball> getCollidedListOfBallWithNormalBlock(BlockSet theSet)
  {
    return getCollidedListOfBallWithBlock(theSet, BlockType.NORMAL);
  }

  private Vector<Ball> getCollidedListOfBallWithFireBlock(BlockSet theSet)
  {
    return getCollidedListOfBallWithBlock(theSet, BlockType.FIRE);
  }

  private Vector<Ball> getCollidedListOfBallWithFreezingBlock(BlockSet theSet)
  {
    return getCollidedListOfBallWithBlock(theSet, BlockType.FROZEN);
  }

  private Vector<Ball>
      getCollidedListOfBallWithUnbreakableBlock(BlockSet theSet)
  {
    return getCollidedListOfBallWithBlock(theSet, BlockState.UNBREAKABLE);
  }
  */

  /**
   * Set the block set to base ball spawn positions on.
   * 
   * @param value
   *          - The block set.
   */
  public void setBlockSet(BlockSet value)
  {
    blocks = value;

    for(int i = 0; i < size(); i++)
    {
      get(i).setBlockSet(blocks);
    }
  }

  /**
   * Create a ball in the world.
   */
  public void add()
  {
    Ball ball = new Ball();
    if(this.upperBound >= 0 ){
    	ball.upperWorldBound = upperBound;
    }
    if(this.lowerBound >= 0 ){
    	ball.lowerWorldBound = lowerBound;
    }
    if(this.leftBound >= 0 ){
    	ball.leftWorldBound = leftBound;
    }
    if(this.rightBound >= 0 ){
    	ball.rightWorldBound = rightBound;
    }
    ball.setSpeedMod(speedMod);
    ball.setBlockSet(blocks);
    add(ball);
  }

  /**
   * Create multiple balls in the world.
   * @param num number of new balls to create into the world.
   */
  public void add(int num)
  {
    for(int i = 0; i < num; i++)
    {
      add();
    }
  }
}
