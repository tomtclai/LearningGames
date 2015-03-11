package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.AllTests;

import SpaceSmasher.Ball;
import SpaceSmasher.Block;
import SpaceSmasher.Block.BlockState;
import SpaceSmasher.Block.BlockType;
import SpaceSmasher.BlockSet;

/**
 * 	Statement	Description
	fail(String)	 									Let the method fail. Might be used to check that a certain part of the code is not reached or to have a failing test before the test code is implemented. The String parameter is optional.
	assertTrue([message], boolean condition)			Checks that the boolean condition is true.
	assertFalse([message], boolean condition)			Checks that the boolean condition is false.
	assertEquals([String message], expected, actual)	 Tests that two values are the same. Note: for arrays the reference is checked not the content of the arrays.
	assertEquals([String message], expected, actual, tolerance)	 Test that float or double values match. The tolerance is the number of decimals which must be the same.
	assertNull([message], object)						Checks that the object is null.
	assertNotNull([message], object)					Checks that the object is not null.
	assertSame([String], expected, actual)	 			Checks that both variables refer to the same object.
	assertNotSame([String], expected, actual)	 		Checks that both variables refer to different objects.
 * 
 * @author Chris Sullivan 2014
 */

public class BlockSetTest {

	/**
	   * Remove a block from blockSet just replaces it with an
	   * EmptyBlock
	   * 
	   * @param obj
	   *          - The object to remove.
	   */
	@Test
	public void removeRectangle() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addEmptyBlock(1);
		
		set.remove(0);
		
		assertTrue(set.size() == 0);
	}

	@Test
	public void blockSet() throws AssertionError {
		assertNotNull(new BlockSet());
	}

	/**
	   * Returns a block at a row and column,
	   * Returns Null if outside of grid.
	   * Returns EmptyBlock if space is empty.
	   * @param row
	   * @param column
	   * @return
	   */
	@Test
	public void getBlockAt() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addEmptyBlock(5);
		
		assertNotNull(set.getBlockAt(0, 0));
		
		assertNull(set.getBlockAt(12, 12));
	}

	/**
	   * Sets a block at position, [row][column] to type
	   * 
	   * Returns a reference to created Block.
	   * Returns null if outside of grid.
	   * 
	   * @param row
	   * @param col
	   * @param type
	   * @return
	   */
	@Test
	public void setBlockAt() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addFreezingBlock(25);
		
		set.setBlockAt(0, 0, BlockType.NORMAL);
		
		// after setting block to FIRE check if its set
		assertTrue(set.getBlockAt(0, 0).getState() == BlockState.NORMAL);
	}

	@Test
	public void getNumColumns() throws AssertionError {
		BlockSet set = new BlockSet();
		
		set.setBlocksPerRow(2);
		set.addNormalBlock(4);
		
		assertTrue(set.getNumColumns() == 2);
	}

	@Test
	public void getNumRows() throws AssertionError {
		BlockSet set = new BlockSet();
		
		set.setBlocksPerRow(2);
		set.addNormalBlock(4);
		
		assertTrue(set.getNumRows() == 2);
	}

	/**
	   * Set how many blocks should be in each row of the grid.
	   * 
	   * @param num
	   *          - The number of blocks in each row of the block grid.
	   */
	@Test
	public void setBlocksPerRow() throws AssertionError {
		BlockSet set = new BlockSet();
		
		set.setBlocksPerRow(2);
		set.addNormalBlock(4);
		
		assertTrue(set.getNumRows() == 2);
	}

	/**
	   * Add the given number of normal blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addNormalBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addNormalBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Add the given number of fire blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addFireBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addNormalBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Add the given number of empty blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addEmptyBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addEmptyBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Add the given number of ice blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addFreezingBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addFreezingBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Add the given number of joker blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addJokerBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addJokerBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Add the given number of active cage blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addActiveCageBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addActiveCageBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Add the given number of inactive cage blocks to the set.
	   * 
	   * @param num
	   *          - How many blocks to add.
	   */
	@Test
	public void addInactiveCageBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addInactiveCageBlock(2);
		
		assertNotNull(set.get(0));
	}

	/**
	   * Breaks the block at the given index.
	   * 
	   * @param index
	   *          - Index of the block to break.
	   */
	@Test
	public void breakBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addInactiveCageBlock(2);
		
		// no way to test
		set.breakBlock(set.get(1));
		
		assertTrue(set.get(1).getState() == BlockState.DEAD);
	}

	/**
	   * Toggles all blocks that can be set as unbreakable should be in that state.
	   */
	@Test
	public void toggleUnbreakables() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addActiveCageBlock(1);
		
		set.toggleUnbreakables();
		set.get(0).breakBlock();
		
		assertTrue(set.get(0).getState() == BlockState.DEAD);
	}

	/**
	   * Get the last cage block found.
	   * 
	   * @return - The last cage block found. Will be null if no cage block exists.
	   */
	@Test
	public void getLastCageBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addActiveCageBlock(1);
		
		assertNotNull(set.getFirstCageBlock());
	}

	/**
	   * Get the first cage block found.
	   * 
	   * @return - The first cage block found. Will be null if no cage block exists.
	   */
	@Test
	public void getFirstCageBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addActiveCageBlock(1);
		
		assertNotNull(set.getFirstCageBlock());
	}

	/**
	   * Set whether blocks show what powerup types they contain.
	   * 
	   * @param value
	   *          - Set whether to show block types.
	   */
	@Test
	public void showTypes() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addFireBlock(7);
		
		// make burning block visible
		set.showTypes(true);
		
		// update images and states
		for(int i = 0; i < set.size(); i++)
			set.get(i).update();
		
		// test if the block is showing it's power
		assertTrue(set.get(0).isPowerRevealed());
	}

	/**
	   * Checks if all blocks are no longer in play.
	   * 
	   * @return Whether all blocks are dead (no longer in play).
	   */
	@Test
	public void allBlocksAreDead() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addNormalBlock(1);
		set.get(0).breakBlock();
		
		assertTrue(set.allBlocksAreDead());
	}

	/**
	   * Check for an return the block the given ball is colliding with, if any.
	   * 
	   * @param ball
	   *          - The ball to check collision with.
	   * @return - The first block that the ball is colliding with. Will be null if
	   *         not colliding with a block.
	   */
	@Test
	public void getCollidedBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		
		Ball b = new Ball();
		
		Block bl = new Block(2, 3);
		bl.center.set(2, 3);
		b.center.set(2, 3);
		
		set.add(bl);
		
		assertNotNull(set.getCollidedBlock(b));
	}

	@Test
	public void isBallCollidingWithABlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addNormalBlock(3);
		
		Ball b = new Ball();
		b.setBlockSet(set);
		
		Block bl = set.get(0);
		
		// get top of block
		float top_of_block = bl.center.getY() + bl.size.getY() / 2;
		
		// set ball base touching block top
		b.center.set(bl.center.getX(), top_of_block + b.size.getY() / 2 );
		
		// move ball to the right a little
		b.center.setX(b.center.getX() + 4);
		
		assertTrue(set.isBallCollidingWithABlock(b));
	}

	/**
	   * Get the block type of the block the given ball collided with.
	   * 
	   * @param ball
	   *          - The ball the block had collided with.
	   * @return - The type of the block that was collided with the ball.
	   */
	@Test
	public void getCollidedBlockType() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addFireBlock(7);
		
		Ball b = new Ball();
		b.setBlockSet(set);
		
		Block bl = set.get(0);
		
		// get top of block
		float top_of_block = bl.center.getY() + bl.size.getY() / 2;
		
		// set ball base touching block top
		b.center.set(bl.center.getX(), top_of_block + b.size.getY() / 2 );
		
		// move ball to the right a little
		b.center.setX(b.center.getX() + 4);
		
		assertTrue(set.getCollidedBlockType(b) == BlockType.FIRE);
	}

	/**
	   * Get the block state of the block the given ball collided with.
	   * 
	   * @param ball
	   *          - The ball the block had collided with.
	   * @return - The state of the block that was collided with the ball.
	   */
	@Test
	public void getCollidedBlockState() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addFreezingBlock(10);
		
		Ball b = new Ball();
		b.setBlockSet(set);
		
		Block bl = set.get(0);
		
		// get top of block
		float top_of_block = bl.center.getY() + bl.size.getY() / 2;
		
		// set ball base touching block top
		b.center.set(bl.center.getX(), top_of_block + b.size.getY() / 2 );
		
		// move ball to the right a little
		b.center.setX(b.center.getX() + 4);
		
		assertTrue(set.getCollidedBlockState(b) == BlockState.FROZEN);
	}

	/**
	   * Get the lowest block that is currently visible in the set.
	   * 
	   * @return - The lowest visible block.
	   */
	@Test
	public void getLowestVisibleBlock() throws AssertionError {
		BlockSet set = new BlockSet();
		set.addFreezingBlock(10);
		
		assertNotNull(set.getLowestVisibleBlock());
	}

}
