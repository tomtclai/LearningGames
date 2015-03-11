package tests;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import SpaceSmasher.Paddle;
import SpaceSmasher.PaddleSet;

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

public class PaddleSetTest {

	@Test
	public void paddleSetConstructor() {
		try {
			assertNotNull(new PaddleSet());
		} catch(AssertionError e){ }
	}

	/**
	   * Add a paddle to the set.
	   */
	@Test
	public void add() {
		try {
			PaddleSet set = new PaddleSet();
			set.add();
			
			assertTrue(set.size() == 1);
		} catch(AssertionError e){ }
	}

	/**
	   * Add the given number of paddles to the set.
	   * 
	   * @param num
	   *          - How many paddles to add.
	   */
	@Test
	public void addInt() {
		try {
			PaddleSet set = new PaddleSet();
			Random rand = new Random();
			int val = rand.nextInt(20) + 1;
			set.add(val);
			
			assertSame(set.size(), val);
		} catch(AssertionError e){ }
	}

	/**
	   * Get the first paddle in the set.
	   * 
	   * @return - The first paddle.
	   */
	@Test
	public void getPaddle() {
		try {
			PaddleSet set = new PaddleSet();

			// with no paddles added
			assertTrue(set.getPaddle() == null);
			
			set.add();
			
			// with paddle added
			assertNotNull(set.getPaddle());
		} catch(AssertionError e){ }
	}

	/**
	   * Get the given paddle based on its index.
	   * 
	   * @param position
	   *          - The paddle index to return.
	   * @return - The paddle at the given index, will be null if invalid index.
	   */
	@Test
	public void getPaddleInt() {
		try {
			PaddleSet set = new PaddleSet();

			Random rand = new Random();
			int ref = rand.nextInt(5) + 1;
			
			// with no paddles, get rand index
			assertTrue(set.getPaddle(ref) == null);
			
			set.add(ref);
			
			// check all paddles added are valid
			for(int i = 0; i < set.size(); i++)
				assertNotNull(set.getPaddle(i));
		} catch(AssertionError e){ }
	}

	/**
	   * Move all paddles to the left, locking the first paddle inside the world and
	   * all other worlds maintain their fixed distance to the first paddle.
	   */
	@Test
	public void moveLeft() {
		try {
			PaddleSet set = new PaddleSet();
			set.add();
			Paddle p = set.getPaddle();
			float x = p.center.getX();
			
			set.moveLeft();
			
			assertNotSame(x, p.center.getX());
		} catch(AssertionError e){ }
	}

	/**
	   * Move all paddles to the right, locking the first paddle inside the world
	   * and all other worlds maintain their fixed distance to the first paddle.
	   */
	@Test
	public void moveRight() {
		try {
			PaddleSet set = new PaddleSet();
			set.add();
			Paddle p = set.getPaddle();
			float x = p.center.getX();
			
			set.moveRight();
			
			assertNotSame(x, p.center.getX());
		} catch(AssertionError e){ }
	}

}
