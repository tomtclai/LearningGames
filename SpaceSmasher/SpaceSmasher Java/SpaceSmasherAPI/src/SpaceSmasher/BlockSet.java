
package SpaceSmasher;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.SetOfGameObjects;
import Engine.Vector2;
import SpaceSmasher.Block.BlockState;
import SpaceSmasher.Block.BlockType;

@SuppressWarnings("serial")
public class BlockSet extends SetOfGameObjects<Block>
{
  /*
  private final float BUFFER_EDGE_X = 18.0f;
  private final float BUFFER_EDGE_Y = 10.0f;
  private final float BUFFER_BETWEEN = 1.0f;

  private Random rand = new Random();

  private long randSeed = 0;
  */

  private boolean showTypes = false;
  
  //For grid based interface
  private static final float TOLERANCE = .1f;
  private Vector2 por; // Point of reference for grid.

  // A value of -1 means no blocks
  private int numBlocksPerRow = -1;
  private int numRows = 0;
  private int columnCounter = 0; // Used for calculating numRows.
  private int currentColumn = 0;// can probably be combined with columnCounter
  private int currentRow = 0; // Can probably be combined with numRows
  
  float horizontalMargin;
  float verticalMargin;
  
  // For animating movement on whole blockset.
  static final float MAX_HORIZONTAL_OFFSET = 4f;
  static final float MAX_VERTICAL_OFFSET = 1f;
  static final float DEFAULT_HORIZONTAL_VELOCITY = .01f;
  static final float DEFAULT_VERTICAL_VELOCITY = .01f;
  private Vector2 mOffsetCounter;
  private Vector2 mOffsetVelocity;
  
  private float horizontalOffsetCounter;
  private float verticalOffsetCounter;
  private float horizontalOffsetVelocity;
  private float verticalOffsetVelocity;
  
  public BlockSet()
  {
	  horizontalMargin = BaseCode.world.getWidth() * 0.22f;
	  verticalMargin = BaseCode.world.getHeight() * 0.27f;
	  
	  mOffsetCounter = new Vector2(MAX_HORIZONTAL_OFFSET / 2, MAX_VERTICAL_OFFSET / 2);
	  mOffsetVelocity = new Vector2(DEFAULT_HORIZONTAL_VELOCITY, DEFAULT_VERTICAL_VELOCITY);
	  por = new Vector2(horizontalMargin,verticalMargin + 27.6f);
  }
  
  /**
   * Remove a block from blockSet. If it doesn't exist in the
   * blockSet, does nothing.
   * 
   * @param obj
   *          - The object to remove.
   */
  public void remove(GameObject obj)
  {
	  int index = indexOf(obj);
	  if(index >= 0)
	  {
	      Block deadBlock = get(index);
	     
	      EmptyBlock insertedBlock = new EmptyBlock(deadBlock.getRow(),
	    		 deadBlock.getColumn());
	     
	      this.set(index, insertedBlock);
	      
	      deadBlock.destroy();
	  }
  }
  
  /**
   * Returns a block at a row and column,
   * Returns Null if outside of grid.
   * Returns EmptyBlock if space is empty.
   * @param row row number (top is 0, 0 to max-1)
   * @param column column number (left is 0, 0 to max-1)
   * @return reference to the block at (row, column) if defined, otherwise, return null.
   */
  public Block getBlockAt(int row, int column)
  {
	  for(Block b : this)
	  {
		  if(b.getRow() == row && b.getColumn() == column)
		  {
			  return b;
		  }
	  }
	  return null;
  }

  /**
   * Sets a block at position, [row][column] to type
   * 
   * Returns a reference to created Block.
   * Returns null if outside of grid.
   * 
   * @param row row number, top is 0
   * @param column column number, left is 0
   * @param type one of defined block types.
   * @return the reference to the newly created Block, if failed, returns null.
   */
  public Block setBlockAt(int row, int column, BlockType type)
  {
	  if(row >= numRows || column >= numBlocksPerRow || row < 0 || column < 0)
	  {
		  return null;
	  }
	  
	  Block insertBlock = blockFactory(row, column, type);
	  	  
      Block deadBlock = this.set(indexOf(getBlockAt(row,column)), insertBlock);

      insertBlock.setSize(deadBlock.getSize());
      insertBlock.setCenter(deadBlock.getCenter());
      
      deadBlock.destroy();
      
      return insertBlock;
  }
  
  private Block blockFactory(int row, int column, BlockType type) 
  {
	switch(type)
	{
	case NORMAL:
		return new Block(row, column);
	case FREEZING:
		return new FreezingBlock(row, column);
	case EMPTY:
		return new EmptyBlock(row, column);
	case FIRE:
		return new FireBlock(row, column);
	case CAGE_ACTIVE: 
		return new CageBlock(true, row, column);
	case CAGE_INACTIVE: 
		return new CageBlock(false, row, column);
	case JOKER:
		return new JokerBlock(row, column);
	}
	
	return null;
  }

/**
   * Moves the block in step with animation counters.
   * @param block
   */
  private void updateBlockPosition(Block block)
  {
	block.getCenter().add(mOffsetVelocity);
  }
  /**
   * Updates the animation counters. When the counters reach either max or zero.
   * Also updates point of reference variable.
   */
  private void updateAnimationCounters()
  {   
    por.add(mOffsetVelocity);
    mOffsetCounter.add(mOffsetVelocity);
       
    if((mOffsetCounter.getX() >= MAX_HORIZONTAL_OFFSET) ||
       (mOffsetCounter.getX() <= 0))
    {
    	mOffsetVelocity.setX( mOffsetVelocity.getX() * -1);
    }
        
    if ((mOffsetCounter.getY() >= MAX_VERTICAL_OFFSET) ||
    	(mOffsetCounter.getY() <= 0))
    {
    	mOffsetVelocity.setY( mOffsetVelocity.getY() * -1);
    }
  }
  
  public int getNumColumns()
  {
    return numBlocksPerRow;
  }
  
  public int getNumRows()
  {
	  return numRows;
  }

  /**
   * Set how many blocks should be in each row of the grid.
   * 
   * @param num
   *          - The number of blocks in each row of the block grid.
   */
  public void setBlocksPerRow(int num)
  {
	// Starts adding sequence.
    if(num < 1)
    {
      num = 1;
      columnCounter = numBlocksPerRow - 1; // So that one added block = new row.
    }

    numBlocksPerRow = num;
  }

  /**
   * Adds the given block to the block set and positions it in the block grid.
   * 
   * @param block
   *          - The block to add.
   */
  private void addBlock(Block block)
  {
    float width =
        (BaseCode.world.getWidth() - (horizontalMargin * 2.0f)) /
            numBlocksPerRow;

    float height = BaseCode.world.getHeight() * 0.07f;

    block.setSize(width, height);

    block.setShowType(showTypes);
    block.updateImage();
    add(block);
    
    block.autoPosition(numBlocksPerRow, horizontalMargin, verticalMargin, this);
    
    columnCounter++;
    currentColumn++;
    if(columnCounter >= numBlocksPerRow)
    {
    	columnCounter = 0;
    	currentColumn = 0;
    	numRows++;
    	currentRow++;
    }
    
  }

  /**
   * Add the given number of normal blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addNormalBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new Block(currentRow, currentColumn));
    }
  }

  /**
   * Add the given number of fire blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addFireBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new FireBlock(currentRow, currentColumn));
    }
  }

  /**
   * Add the given number of empty blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addEmptyBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new EmptyBlock(currentRow, currentColumn));
    }
  }

  /**
   * Add the given number of ice blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addFreezingBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new FreezingBlock(currentRow, currentColumn));
    }
  }
  /**
   * Add the given number of joker blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addJokerBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new JokerBlock(currentRow, currentColumn));
    }
  }

  /**
   * Add the given number of active cage blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addActiveCageBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new CageBlock(true, currentRow, currentColumn));
    }
  }
  /**
   * Add the given number of inactive cage blocks to the set.
   * 
   * @param num
   *          - How many blocks to add.
   */
  public void addInactiveCageBlock(int num)
  {
    for(int i = 0; i < num; i++)
    {
      addBlock(new CageBlock(false, currentRow, currentColumn));
    }
  }
  /**
   * Breaks the block at the given index.
   * 
   * @param index
   *          - Index of the block to break.
   */
  private void breakBlock(int index)
  {
    if(index >= 0 && index < size())
    {
      get(index).breakBlock();
    }
  }
  public void update(){
	  for(int loop = 0; loop < super.elementCount; loop++){
		  Block target = super.get(loop);
		  if(target != null){
			  target.update();
			  updateBlockPosition(target);
		  }
	  }
	  updateAnimationCounters();
  }
  /**
   * Breaks the given block in the set.
   * 
   * @param obj
   *          - The block to break.
   */
  public void breakBlock(Block obj)
  {
    breakBlock(indexOf(obj));
  }

  /**
   * Toggles all blocks that can be set as unbreakable should be in that state.
   */
  public void toggleUnbreakables()
  {
    Block curBlock;

    for(int i = 0; i < size(); i++)
    {
      curBlock = get(i);

      if(curBlock.getType() == BlockType.CAGE_ACTIVE)
      {
        //curBlock.setIsPowerRevealed(!curBlock.isPowerRevealed());
    	  ((CageBlock)curBlock).begineTransition(BlockType.CAGE_INACTIVE);
      }
      else if(curBlock.getType() == BlockType.CAGE_INACTIVE && curBlock.getState() != BlockState.FROZEN){
    	  ((CageBlock)curBlock).begineTransition(BlockType.CAGE_ACTIVE);
      }
    }
  }
  /**
   * Get the first cage block found after skipping the given number of cage
   * blocks.
   * 
   * @param skip
   *          - How many cage blocks to skip before returning the block.
   * @return - The first cage block found after skipping the given amount. Will
   *         be null if no cage block exists after skipping.
   */
  protected Block getCageBlock(int skip)
  {
    for(int i = 0; i < size(); i++)
    {
      if(get(i).getType() == BlockType.CAGE_ACTIVE || get(i).getType() == BlockType.CAGE_INACTIVE)
      {
        if(skip > 0)
        {
          skip -= 1;
        }
        else
        {
          return get(i);
        }
      }
    }

    return null;
  }

  public Block getLastCageBlock()
  {
    Block lastBlock = null;

    for(int i = 0; i < size(); i++)
    {
      if(get(i).getType() == BlockType.CAGE_ACTIVE || get(i).getType() == BlockType.CAGE_INACTIVE)
      {
        lastBlock = get(i);
      }
    }

    return lastBlock;
  }

  /**
   * Get the first cage block found.
   * 
   * @return - The first cage block found. Will be null if no cage block exists.
   */
  public Block getFirstCageBlock()
  {
    return getCageBlock(0);
  }

//  /**
//   * Get the block at the given grid position.
//   * 
//   * @param x
//   *          - x coordinate of the block.
//   * @param y
//   *          - y coordinate of the block.
//   * @return - The block at the given grid position. Will be null if the
//   *         coordinate is not inside the grid.
//   */
//  public Block getBlock(int x, int y)
//  {
//    int index = x + (y * numBlocksPerRow);
//
//    if(index < size())
//    {
//      return get(index);
//    }
//
//    return null;
//  }

  /**
   * Set whether blocks show what powerup types they contain.
   * 
   * @param value
   *          - Set whether to show block types.
   */
  public void showTypes(boolean value)
  {
    showTypes = value;

    Block curBlock;

    for(int i = 0; i < size(); i++)
    {
      curBlock = get(i);

      curBlock.setShowType(showTypes);
      curBlock.updateImage();
    }
  }

  /**
   * Checks if all blocks are no longer in play.
   * 
   * @return Whether all blocks are dead (no longer in play).
   */
  public boolean allBlocksAreDead()
  {
    for(int i = 0; i < size(); i++)
    {
      if(get(i).getState() != BlockState.DEAD)
      {
        return false;
      }
    }

    return (numBlocksPerRow > 0);
  }

  /**
   * Check for an return the block the given ball is colliding with, if any.
   * 
   * @param ball
   *          - The ball to check collision with.
   * @return - The first block that the ball is colliding with. Will be null if
   *         not colliding with a block.
   */
  public Block getCollidedBlock(Ball ball)
  {
    if(ball != null)
    {
      for(int i = 0; i < size(); i++)
      {
        if(get(i).collided(ball))
        {
          return get(i);
        }
      }
    }

    return null;
  }

  public boolean isBallCollidingWithABlock(Ball ball)
  {
    return (getCollidedBlock(ball) != null);
  }

  /**
   * Get the block type of the block the given ball collided with.
   * 
   * @param ball
   *          - The ball the block had collided with.
   * @return - The type of the block that was collided with the ball.
   */
  public BlockType getCollidedBlockType(Ball ball)
  {
    Block block = getCollidedBlock(ball);

    if(block != null)
    {
      return block.getType();
    }

    return null;
  }

  /**
   * Get the block state of the block the given ball collided with.
   * 
   * @param ball
   *          - The ball the block had collided with.
   * @return - The state of the block that was collided with the ball.
   */
  public BlockState getCollidedBlockState(Ball ball)
  {
    Block block = getCollidedBlock(ball);

    if(block != null)
    {
      return block.getState();
    }

    return null;
  }

  /**
   * Get the lowest block that is currently visible in the set.
   * 
   * @return - The lowest visible block.
   */
  public Block getLowestVisibleBlock()
  {
    Block lastBlock = null;
    Block current = null;

    for(int i = 0; i < size(); i++)
    {
      current = get(i);

      if(current.isVisible() &&
          (lastBlock == null || lastBlock.getCenterY() > current.getCenterY()))
      {
        lastBlock = current;
      }
    }

    return lastBlock;
  }
 
  /**
   * Removes all objects in the set, and resets adding indices.
   * (the row/column positions of added blocks)
   */
  public void clear()
  {
	  super.clear();
	  currentRow = 0;
	  currentColumn = 0;
  }
  
  /**
   * Create a row in a grid of blocks using the number of blocks in the given
   * area.
   * 
   * @param blocksPerRow
   *          - Number of blocks in each row.
   * @param posY
   *          - The y coordinate of the row.
   * @param sizeY
   *          - The height of each block.
   * @param left
   *          - Left coordinate of the grid.
   * @param top
   *          - Top coordinate of the grid.
   * @param right
   *          - Right coordinate of the grid.
   * @param bottom
   *          - Bottom coordinate of the grid, allows those blocks to be
   *          unbreakable.
   * @param bottomRow
   *          - Set to true if the row is the bottom row.
   */
  /*
  public void initBlockRow(final int blocksPerRow, final float posY,
      final float sizeY, float left, float top, float right, float bottom,
      boolean bottomRow)
  {
    final float BLOCK_SIZE_X =
        ((right - left) - (BUFFER_EDGE_X * 2.0f) - ((blocksPerRow + 1) * BUFFER_BETWEEN)) /
            blocksPerRow;

    for(int i = 0; i < blocksPerRow; i++)
    {
      int randVal = rand.nextInt(100);

      Block block = null;

      if(bottomRow)
      {
        block = new UnbreakableBlock();
      }
      else
      {
        if(randVal < 30)
        {
          // Only two values because joker is not implemented yet
          randVal = rand.nextInt(2);

          if(randVal == 0)
          {
            block = new FireBlock();
          }
          else if(randVal == 1)
          {
            block = new FreezingBlock();
          }
        }

        if(block == null)
        {
          block = new Block();
        }
      }

      block.center.set(BUFFER_EDGE_X +
          (((i % blocksPerRow) + 1) * BUFFER_BETWEEN) +
          ((i % blocksPerRow) * BLOCK_SIZE_X) + (BLOCK_SIZE_X * 0.5f), posY);
      block.size.set(BLOCK_SIZE_X, sizeY);

      block.setShowType(showTypes);
      block.updateImage();
      add(block);
    }
  }
  */

  /**
   * Create a grid of blocks using the number of blocks in the given area.
   * 
   * @param numBlocks
   *          - Number of blocks to fill the grid.
   * @param blocksPerRow
   *          - Number of blocks in each row of the grid.
   * @param left
   *          - Left coordinate of the grid.
   * @param top
   *          - Top coordinate of the grid.
   * @param right
   *          - Right coordinate of the grid.
   * @param bottom
   *          - Bottom coordinate of the grid.
   */
  /*
  public void initBlocks(final int numBlocks, final int blocksPerRow,
      float left, float top, float right, float bottom)
  {
    numBlocksPerRow = blocksPerRow;

    final int NUM_ROWS = (int)Math.ceil((float)numBlocks / blocksPerRow);
    final float BLOCK_SIZE_Y =
        ((top - bottom) - (BUFFER_EDGE_Y * 2.0f) - ((NUM_ROWS + 1) * BUFFER_BETWEEN)) /
            NUM_ROWS;

    float curY;

    rand.setSeed(randSeed);

    for(int i = 0; i < NUM_ROWS; i++)
    {
      curY =
          top -
              (BUFFER_EDGE_Y + ((i + 1) * BUFFER_BETWEEN) + (i * BLOCK_SIZE_Y) + (BLOCK_SIZE_Y * 0.5f));

      if(i == (NUM_ROWS - 1))
      {
        int num = numBlocks % blocksPerRow;

        if(num != 0)
        {
          initBlockRow(numBlocks % blocksPerRow, curY, BLOCK_SIZE_Y, left, top,
              right, bottom, i == (NUM_ROWS - 1));

          continue;
        }
      }

      initBlockRow(blocksPerRow, curY, BLOCK_SIZE_Y, left, top, right, bottom,
          i == (NUM_ROWS - 1));
    }
  }
  */
}
