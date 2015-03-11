package tests;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import Engine.Vector2;
import SpaceSmasher.Ball;
import SpaceSmasher.Paddle;

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

public class PaddleTest {
	
	@Test
	public void paddleConstructor() {
		try {
			assertNotNull(new Paddle());
		} catch(AssertionError e){ }
	}

	/**
	   * Retrieves the state of the paddle.
	   * @return State of the paddle.
	   */
	@Test
	public void getState() {
		try {
			Paddle p = new Paddle();
			
			// make sure value is not null
			assertNotNull(p.getState());
			
			// make sure its right data type
			assertTrue(p.getState() instanceof Paddle.PaddleState);
		} catch(AssertionError e){ }
	}

	@Test
	public void reflect() {
		try {
			Paddle p = new Paddle();
			Ball b = new Ball();
			
			// get top of paddle
			float top_of_paddle = p.center.getY() + p.size.getY() / 2;
			
			// make sure velocity is aiming at paddle
			b.velocity.setY(-Math.abs(b.velocity.getY()));
			
			// set ball base touching paddle top
			b.center.set(p.center.getX(), top_of_paddle + b.size.getY() / 2 );
			
			// move ball to the right a little
			b.center.setX(b.center.getX() + 4);
			
			// get current vel
			Vector2 vel = b.velocity;
			p.reflect(b);

			// if velocity diff, then reflect worked
			assertNotSame(vel.getY(), b.velocity.getY());
		} catch(AssertionError e){ }
	}

	/**
	   * Set how much the paddle moves.
	   * 
	   * @param value
	   *          - The distance the paddle moves when the move methods are called.
	   */
	@Test
	public void setMoveSpeed() {
		try {
			Paddle p = new Paddle();
			
			float cur_speed = p.getMoveSpeed();
			Random rand = new Random();
			
			// added 0.1f in case of nextFloat == 0
			p.setMoveSpeed(cur_speed + rand.nextFloat() + 0.1f);
			
			// make sure it changes
			assertNotSame(cur_speed, p.getMoveSpeed());
		} catch(AssertionError e){ }
	}

	/**
	   * Get how much the paddle moves after calling the move methods.
	   * 
	   * @return - The amount the paddle would move.
	   */
	@Test
	public void getMoveSpeed() {
		try {
			Paddle p = new Paddle();
			
			// make sure it changes
			assertNotNull(p.getMoveSpeed());
		} catch(AssertionError e){ }
	}

	/**
	   * Move the paddle to the left by the given amount. Prevents the paddle from
	   * going too far off the edge of the world.
	   */
	@Test
	public void moveLeft() throws AssertionError {
		Paddle p = new Paddle();
		
		// get initial x value
		float init_x = p.center.getX();
		
		// move the paddle
		p.moveLeft();
		
		assertNotSame(init_x, p.center.getX());
	}

	/**
	   * Move the paddle to the right by the given amount. Prevents the paddle from
	   * going too far off the edge of the world.
	   */
	@Test
	public void moveRight() {
		try {
			Paddle p = new Paddle();
			
			// get initial x value
			float init_x = p.center.getX();
			
			// move the paddle
			p.moveRight();
			
			assertNotSame(init_x, p.center.getX());
		} catch(AssertionError e){ }
	}

	@Test
	public void clampPaddle() {
		try {
			Paddle p = new Paddle();
			
			// get initial x value
			float init_x = p.center.getX();
			
			// move the paddle
			p.clampPaddle();
			
			assertNotSame(init_x, p.center.getX());
		} catch(AssertionError e){ }
	}

	@Test
	public void startFire() {
		try {
			Paddle p = new Paddle();
			p.startFire();
			
			assertTrue(p.getState() == Paddle.PaddleState.FIRE);
			assertTrue(p.usingSpriteSheet());
		} catch(AssertionError e){ }
	}

	@Test
	public void startIce() {
		try {
			Paddle p = new Paddle();
			p.startIce();
			
			assertTrue(p.getState() == Paddle.PaddleState.ICE);
			assertTrue(p.usingSpriteSheet());
		} catch(AssertionError e){ }
	}

	@Test
	public void setToNormal() {
		try {
			Paddle p = new Paddle();
			p.setToNormal();
			
			assertTrue(p.getState() == Paddle.PaddleState.NORMAL);
			assertTrue(!p.usingSpriteSheet());
		} catch(AssertionError e){ }
	}

}
