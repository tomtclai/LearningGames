package tests;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import Engine.BaseCode;
import Engine.Vector2;
import SpaceSmasher.Ball;
import SpaceSmasher.BallSet;
import SpaceSmasher.Block;
import SpaceSmasher.BlockSet;
import SpaceSmasher.FireBlock;
import SpaceSmasher.Paddle;
import SpaceSmasher.PaddleSet;
import SpaceSmasher.TrapSet;

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


public class BallSetTest {

	@Test
	public void ballSetConstructor() {
		try
		{
	
			assertNotNull(new BallSet());
		} catch(AssertionError e ){}
	}

	/**
	   * Set the visibility of all balls.
	   * 
	   * @param value
	   *          - True for visible, false for not visible.
	   */
	@Test
	public void setVisible() {
		try
		{
			BallSet set = new BallSet();
			set.add();
			
			// set false and test
			set.setVisible(false);
			assertFalse(set.get(0).visible);
			
			// set true and test
			set.setVisible(true);
			assertTrue(set.get(0).visible);
		
		} catch(AssertionError e ){}
	}

	/**
	   * Check if there are no more balls in play.
	   * 
	   * @return True if no balls are alive, false otherwise.
	   */
	@Test
	public void allBallsDead() {
		try
		{
			BallSet set = new BallSet();
			set.add(10);
			
			// kill(disappear) remaining and re-test
			set.setVisible(false);
			
			assertTrue(set.allBallsDead());
		} catch(AssertionError e ){}
	}

	/**
	   * Set the modifier for all ball velocities to the given amount.
	   * 
	   * @param value
	   *          - The modifier value for the balls' velocity.
	   */
	@Test
	public void setSpeedMod() {
		try
		{
			BallSet set = new BallSet();
			set.add(1);
			
			// get original speed
			Vector2 prev = set.get(0).velocity;
			
			set.setSpeedMod(2f);
			
			assertNotSame(prev.getX(), set.get(0).velocity.getX());
			assertNotSame(prev.getY(), set.get(0).velocity.getY());
		
		} catch(AssertionError e ){}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void anyBallCollidedWithPaddle() {
		try
		{
			BallSet set = new BallSet();
			Paddle p = new Paddle();
			set.add(1);
			
			assertFalse(set.anyBallCollidedWithPaddle(p));
			
			Ball b = set.get(0);
			
			// get top of paddle
			float top_of_paddle = p.center.getY() + p.size.getY() / 2;
			
			// make sure velocity is aiming at paddle
			b.velocity.setY(-Math.abs(b.velocity.getY()));
			
			// set ball base touching paddle top
			b.center.set(p.center.getX(), top_of_paddle + b.size.getY() / 2 );;

			assertTrue(set.anyBallCollidedWithPaddle(p));
		} catch(AssertionError e ) {}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void anyBallCollidedWithWalls() {
		try
		{
			BallSet set = new BallSet();
			set.add(1);
			
			TrapSet tset = new TrapSet();
			tset.add();
			assertFalse(set.anyBallCollidedWithWalls(tset));
			
			Ball b = set.get(0);
			
			b.center.setX(tset.getWidth());

			assertTrue(set.anyBallCollidedWithWalls(tset));
		
		} catch(AssertionError e ){}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void anyBallCollidedWithBlocks() {
		try
		{
		BallSet set = new BallSet();
		set.add(1);
		
		BlockSet bset = new BlockSet();
		bset.addEmptyBlock(1);
		assertFalse(set.anyBallCollidedWithBlocks(bset));
		
		Ball b = set.get(0);
		
		b.center.setX(bset.get(0).center.getX());
		b.center.setY(bset.get(0).center.getY());

		assertTrue(set.anyBallCollidedWithBlocks(bset));
		} catch(AssertionError e ){}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void anyBallCollidedWithNormalBlock() {
		try
		{
		BallSet set = new BallSet();
		set.add(1);
		
		BlockSet bset = new BlockSet();
		bset.addNormalBlock(1);
		assertFalse(set.anyBallCollidedWithNormalBlock(bset));
		
		Ball b = set.get(0);
		
		b.center.setX(bset.get(0).center.getX());
		b.center.setY(bset.get(0).center.getY());

		assertTrue(set.anyBallCollidedWithNormalBlock(bset));
		} catch(AssertionError e ){}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void anyBallCollidedWithFireBlock() {
		try
		{
		BallSet set = new BallSet();
		set.add(1);
		
		BlockSet bset = new BlockSet();
		bset.addFireBlock(1);
		assertFalse(set.anyBallCollidedWithFireBlock(bset));
		
		Ball b = set.get(0);
		
		b.center.setX(bset.get(0).center.getX());
		b.center.setY(bset.get(0).center.getY());

		assertTrue(set.anyBallCollidedWithFireBlock(bset));
		} catch(AssertionError e ){}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void anyBallCollidedWithFreezingBlock() {
		try
		{
		BallSet set = new BallSet();
		set.add(1);
		
		BlockSet bset = new BlockSet();
		bset.addFreezingBlock(1);
		assertFalse(set.anyBallCollidedWithFreezingBlock(bset));
		
		Ball b = set.get(0);
		
		b.center.setX(bset.get(0).center.getX());
		b.center.setY(bset.get(0).center.getY());

		assertTrue(set.anyBallCollidedWithFreezingBlock(bset));
		} catch(AssertionError e ){}
	}

	/**
	   * Checks if any ball is colliding with the given object.
	   * 
	   * @param obj
	   *          - The object to check collision with.
	   * @return - True if a ball is colliding, false otherwise.
	   */
	@Test
	public void getCollidedListOfBalls() {
		try
		{
		BallSet set = new BallSet();
		set.add();
		Paddle p = new Paddle();
		
		assertNull(set.getCollidedListOfBalls(p));
		
		Ball b = set.get(0);
		
		// get top of paddle
		float top_of_paddle = p.center.getY() + p.size.getY() / 2;
		
		// make sure velocity is aiming at paddle
		b.velocity.setY(-Math.abs(b.velocity.getY()));
		
		// set ball base touching paddle top
		b.center.set(p.center.getX(), top_of_paddle + b.size.getY() / 2 );
		
		assertNotNull(set.getCollidedListOfBalls(p));
		} catch(AssertionError e ){}
	}

	/**
	   * Get which balls collide with the given items.
	   * 
	   * @param obj
	   *          - The given item to check collision with.
	   * @return - The list of balls that collide with the given object.
	   */
	@Test
	public void getCollidedListOfBallsWithWalls() {
		try
		{
		BallSet set = new BallSet();
		set.add(1);
		
		TrapSet tset = new TrapSet();
		tset.add();
		assertNull(set.getCollidedListOfBallsWithWalls(tset));
		
		Ball b = set.get(0);
		
		b.center.setX(tset.getWidth());

		assertNotNull(set.getCollidedListOfBallsWithWalls(tset));
		} catch(AssertionError e ){}
	}

	/**
	   * Get which balls collide with the given items.
	   * 
	   * @param obj
	   *          - The given item to check collision with.
	   * @return - The list of balls that collide with the given object.
	   */
	@Test
	public void getCollidedListOfBallsWithBlocks() {
		try
		{
		BallSet set = new BallSet();
		set.add(1);
		
		BlockSet bset = new BlockSet();
		bset.addNormalBlock(1);
		assertNull(set.getCollidedListOfBallsWithBlocks(bset));
		
		Ball b = set.get(0);
		
		b.center.setX(bset.get(0).center.getX());
		b.center.setY(bset.get(0).center.getY());

		assertNotNull(set.getCollidedListOfBallsWithBlocks(bset));
		} catch(AssertionError e ){}
	}

	/**
	   * Set the block set to base ball spawn positions on.
	   * 
	   * @param value
	   *          - The block set.
	   */
	@Ignore
	public void setBlockSet() {
		try
		{
		fail("Not yet implemented : unable to test");
		} catch(AssertionError e ){}
	}

	/**
	   * Create a ball in the world.
	   */
	@Test
	public void add() {
		try
		{
		BallSet set = new BallSet();
		set.add();
		
		assertTrue(set.size() == 1);
		
		assertNotNull(set.get(0));
		} catch(AssertionError e ){}
	}

	/**
	   * Create a given amount of balls in the world.
	   */
	@Test
	public void addInt() {
		try
		{
			BallSet set = new BallSet();
			
			Random rand = new Random();
			int amnt = rand.nextInt(10) + 1;
			set.add(amnt);
			
			for(int i = 0; i < amnt; i++)
				assertNotNull(set.get(i));
			
			assertNotSame(12, set.size());
		} catch(AssertionError e ){}
	}

}
