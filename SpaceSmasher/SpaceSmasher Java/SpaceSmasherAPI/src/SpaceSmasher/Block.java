
package SpaceSmasher;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.ResourceHandler;
import Engine.Vector2;

public class Block extends GameObject
{
  public enum BlockType
  {
    NORMAL, EMPTY, FIRE, FREEZING, CAGE_ACTIVE, CAGE_INACTIVE, JOKER
  };

  public enum BlockState
  {
    NORMAL, FROZEN, DEAD
  }

  private BlockType type = BlockType.NORMAL;
  private BlockState state = BlockState.NORMAL;
  private boolean showType = false;

  private boolean isPowerRevealed = false;
  
  protected int row;
  protected int column;
  
  protected static final String ICED_IMAGE = "blocks/Block_Iced.png";
  protected static final String NORMAL_BLOCK_IMAGE = "blocks/Block_Normal.png";
  

  
  /**
   * will load and sounds or images required for this class to function
   * @param resources the handler to use for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(ICED_IMAGE);
		  resources.preloadImage(NORMAL_BLOCK_IMAGE);
	  }
  }
  protected Block()
  {
    setType(BlockType.NORMAL);
	  
  }

  public Block(int currentRow, int currentColumn) 
  {
	  row = currentRow;
	  column = currentColumn;
  }
/**
   * Set whether the block should show its type. Some blocks may show their type
   * regardless of this value.
   * 
   * @param value
   *          - True if the block should show its type.
   */
  public void setShowType(boolean value)
  {
    showType = value;
    updateImage();
  }

  /**
   * Check whether the block is set to show its type. Some blocks may show their
   * type regardless of this value.
   * 
   * @return - True if the block should show its type, false otherwise.
   */
  public boolean getShowType()
  {
    return showType;
  }

  /**
   * Set the type of block.
   * 
   * @param newType
   *          - The type for this block to be.
   */
  public void setType(BlockType newType)
  {
    type = newType;
    updateImage();
  }

  /**
   * Get the type of block.
   * 
   * @return - The block type of this block.
   */
  public BlockType getType()
  {
    return type;
  }

  /**
   * Set the state of the block.
   * 
   * @param newState
   *          - The state for this block to be.
   */
  public void setState(BlockState newState)
  {
    state = newState;
    updateImage();
  }

  /**
   * Get the state of the block.
   * 
   * @return - The block state of this block.
   */
  public BlockState getState()
  {
    return state;
  }

  public void freezeTheBlock()
  {
    setState(BlockState.FROZEN);
  }

  /**
   * Check if the block is colliding with the given object.
   * 
   * @param otherObj
   *          - The object to check collision with.
   * @return - True if colliding, false otherwise.
   */
  public boolean collided(GameObject otherObj)
  {
    if(isVisible())
    {
      return super.collided(otherObj);
    }

    return false;
  }

  /**
   * Update what texture the block should have based on type and state.
   */
  protected void updateImage()
  {
    setToVisible();

    if(getState() == BlockState.FROZEN)
    {
      setImage(ICED_IMAGE);
    }
    else if(type == BlockType.NORMAL)
    {
      setImage(NORMAL_BLOCK_IMAGE);
    }

    if(state == BlockState.DEAD)
    {
      // removeFromAutoDrawSet();
      setToInvisible();
    }
  }

  /**
   * Bounce the ball off the block.
   * 
   * @param ball
   *          - The ball to bounce.
   */
  public void reflect(Ball ball)
  {
    if(ball != null)
    {
      Vector2 dir = pushOutCircle(ball);

      ball.bounce(dir);
    }
  }

  /**
   * Breaks this block and takes it out of play.
   */
  public void breakBlock()
  {
    state = BlockState.DEAD;
    updateImage();
  }

  /**
   * Reveals the power up that this block contains.
   */
  public void revealPower()
  {
    setIsPowerRevealed(true);
  }

  /**
   * Set whether the contained power up is revealed.
   * 
   * @param value
   *          - True if the power should be revealed, false otherwise.
   */
  public void setIsPowerRevealed(boolean value)
  {
    isPowerRevealed = value;
    updateImage();
  }

  /**
   * Check if the power up inside this block, if any, has been revealed.
   * 
   * @return - True if it has been revealed, false otherwise.
   */
  public boolean isPowerRevealed()
  {
    return isPowerRevealed;
  }

  /**
   * Position the block where it should be in the blockSet grid.
   * 
   * @param blocksPerRow
   *          - How many blocks are in each row.
   * @param horizontalMargin
   *          - The margin on the left and right sides of the grid.
   * @param verticalMargin
   *          - The margin on the top and bottom sides of the grid.
   * @param blockSet
   *          - The set of blocks that this block should already be a part of.
   */
  protected void autoPosition(int blocksPerRow, float horizontalMargin,
      float verticalMargin, BlockSet blockSet)
  {
    int index = blockSet.indexOf(this);

    // Check if it is the very first block
    setCenterX(horizontalMargin + ((BaseCode.world.getWidth() - 
    		(2 * horizontalMargin))/(blocksPerRow - 1)) * (index%blocksPerRow));
    setCenterY(BaseCode.world.getHeight() - verticalMargin - 
    		(getHeight() * 1.15f * (index/blocksPerRow)));
    
  }
public int getRow() 
{
	return row;
}

public int getColumn()
{
	return column;
}
  
}
