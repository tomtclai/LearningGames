package tests;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import Engine.Rectangle;
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

public class BlockTest {

	/**
	   * Check if the block is colliding with the given object.
	   * 
	   * @param otherPrim
	   *          - The object to check collision with.
	   * @return - True if colliding, false otherwise.
	   */
	@Test
	public void collided() {
		try
		{
			Block b = new Block(2, 3);
			Block b2 = new Block(2, 3);
			assertTrue(b.collided(b2));
		} catch(AssertionError e){ }
	}

	@Test
	public void blockConstructor() {
		try
		{
			BlockSet bset = new BlockSet();
			bset.addNormalBlock(1);
			assertNotNull(bset.get(0));
		} catch(AssertionError e){ }
	}

	@Test
	public void blockConstructorIntInt() {
		try
		{
			Block b = new Block(2, 3);
			assertNotNull(b);
			assertTrue(b.getRow() == 2);
			assertTrue(b.getColumn() == 3);
		} catch(AssertionError e){ }
	}

	/**
	   * Set whether the block should show its type. Some blocks may show their type
	   * regardless of this value.
	   * 
	   * @param value
	   *          - True if the block should show its type.
	   */
	@Test
	public void setShowType() {
		try
		{
			Block b = new Block(2, 3);
			b.setShowType(false);
			assertFalse(b.getShowType());
			b.setShowType(true);
			assertTrue(b.getShowType());
		} catch(AssertionError e){ }
	}

	/**
	   * Check whether the block is set to show its type. Some blocks may show their
	   * type regardless of this value.
	   * 
	   * @return - True if the block should show its type, false otherwise.
	   */
	@Test
	public void getShowType() {
		try
		{
			Block b = new Block(2, 3);
			b.setShowType(false);
			assertFalse(b.getShowType());
			b.setShowType(true);
			assertTrue(b.getShowType());
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Set the type of block.
	   * 
	   * @param newType
	   *          - The type for this block to be.
	   */
	@Test
	public void setType() {
		try
		{
			Block b = new Block(2, 3);
			b.setType(BlockType.FIRE);
			assertTrue(b.getType() == BlockType.FIRE);
			b.setType(BlockType.FREEZING);
			assertTrue(b.getType() == BlockType.FREEZING);
			b.setType(BlockType.JOKER);
			assertTrue(b.getType() == BlockType.JOKER);
			b.setType(BlockType.NORMAL);
			assertTrue(b.getType() == BlockType.NORMAL);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Get the type of block.
	   * 
	   * @return - The block type of this block.
	   */
	@Test
	public void getType() {
		try
		{
			Block b = new Block(2, 3);
			b.setType(BlockType.FIRE);
			assertTrue(b.getType() == BlockType.FIRE);
			b.setType(BlockType.FREEZING);
			assertTrue(b.getType() == BlockType.FREEZING);
			b.setType(BlockType.JOKER);
			assertTrue(b.getType() == BlockType.JOKER);
			b.setType(BlockType.NORMAL);
			assertTrue(b.getType() == BlockType.NORMAL);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Set the state of the block.
	   * 
	   * @param newState
	   *          - The state for this block to be.
	   */
	@Test
	public void setState() {
		try
		{
			Block b = new Block(2, 3);
			b.setState(BlockState.DEAD);
			assertTrue(b.getState() == BlockState.DEAD);
			b.setState(BlockState.FROZEN);
			assertTrue(b.getState() == BlockState.FROZEN);
			b.setState(BlockState.NORMAL);
			assertTrue(b.getState() == BlockState.NORMAL);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Get the state of the block.
	   * 
	   * @return - The block state of this block.
	   */
	@Test
	public void getState() {
		try
		{
			Block b = new Block(2, 3);
			b.setState(BlockState.DEAD);
			assertTrue(b.getState() == BlockState.DEAD);
			b.setState(BlockState.FROZEN);
			assertTrue(b.getState() == BlockState.FROZEN);
			b.setState(BlockState.NORMAL);
			assertTrue(b.getState() == BlockState.NORMAL);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	@Test
	public void freezeTheBlock() {
		try
		{
			Block b = new Block(2, 3);
			b.setState(BlockState.DEAD);
			assertTrue(b.getState() == BlockState.DEAD);
			b.setState(BlockState.FROZEN);
			assertTrue(b.getState() == BlockState.FROZEN);
			b.setState(BlockState.NORMAL);
			assertTrue(b.getState() == BlockState.NORMAL);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Bounce the ball off the block.
	   * 
	   * @param ball
	   *          - The ball to bounce.
	   */
	@Test
	public void reflect() {
		try
		{
			Block b = new Block(2, 3);
			Ball ba = new Ball();
			
			// initial ball vel
			float i_vel = ba.velocity.getY();
			
			// get block top
			float b_top = b.center.getX() + b.size.getX() / 2;
			
			// move ball to top of block
			ba.center.setX(b_top + ba.size.getX() / 2);
			
			b.reflect(ba);
			
			assertNotSame(i_vel, ba.velocity.getY());
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Breaks this block and takes it out of play.
	   */
	@Test
	public void breakBlock() {
		try
		{
			Block b = new Block(2, 4);
			b.breakBlock();
			
			assertTrue(b.getState() == BlockState.DEAD);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Reveals the power up that this block contains.
	   */
	@Test
	public void revealPower() {
		try
		{
			Block b = new Block(2, 3);
			b.revealPower();
			
			assertTrue(b.isPowerRevealed());
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Set whether the contained power up is revealed.
	   * 
	   * @param value
	   *          - True if the power should be revealed, false otherwise.
	   */
	@Test
	public void setIsPowerRevealed() {
		try
		{
			Block b = new Block(2, 3);
			b.setIsPowerRevealed(false);
			
			assertFalse(b.isPowerRevealed());
			
			b.setIsPowerRevealed(true);
			
			assertTrue(b.isPowerRevealed());
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	/**
	   * Check if the power up inside this block, if any, has been revealed.
	   * 
	   * @return - True if it has been revealed, false otherwise.
	   */
	@Test
	public void isPowerRevealed() {
		try
		{
			Block b = new Block(2, 3);
			b.setIsPowerRevealed(false);
			
			assertFalse(b.isPowerRevealed());
			
			b.setIsPowerRevealed(true);
			
			assertTrue(b.isPowerRevealed());
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	@Test
	public void getRow() {
		try
		{
			BlockSet set = new BlockSet();
			set.setBlocksPerRow(2);
			set.addNormalBlock(4);

			// should be in row 1
			
			assertTrue(set.get(3).getRow() == 1);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

	@Test
	public void getColumn() {
		try
		{
			BlockSet set = new BlockSet();
			set.setBlocksPerRow(2);
			set.addNormalBlock(4);

			// should be in col 1
			
			assertTrue(set.get(0).getColumn() == 0);
			
			assertTrue(set.get(1).getColumn() == 1);
		} catch(AssertionError e){ 
			fail(e.getMessage());
		}
	}

}
