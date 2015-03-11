package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.Ball;
import SpaceSmasher.Trap;

public class TrapTest {

	@Test
	public void trapConstructor() throws AssertionError {
		assertNotNull(new Trap());
	}

	/**
	   * Bounce the ball off the paddle.
	   * 
	   * @param ball
	   *          - The ball to bounce.
	   */
	@Test
	public void reflect() throws AssertionError {
		Trap t = new Trap();
		
		Ball b = new Ball();
		b.center.set(t.center.getX(), t.center.getY());
		
		float pre_vel = b.velocity.getY();
		
		t.reflect(b);
		
		assertNotSame(pre_vel, b.velocity.getY());
	}

	/**
	   * Make the wall look activated.
	   */
	@Test
	public void activate() throws AssertionError {
		Trap t = new Trap();
		
		assertFalse(t.isActive());
		
		t.activate();
		
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertTrue(t.isActive());
	}

	/**
	   * Make the wall look deactivated.
	   */
	@Test
	public void deactivate() throws AssertionError {
		Trap t = new Trap();
		
		t.activate();
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertTrue(t.isActive());
		
		t.deactivate();
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertFalse(t.isActive());
	}

	/** 
	 * returns whether the trap is active
	 */
	@Test
	public void isActive() throws AssertionError {
		Trap t = new Trap();
		
		t.activate();
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertTrue(t.isActive());
		
		t.deactivate();
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertFalse(t.isActive());
	}

	/** 
	 * returns whether the trap is inactive
	 */
	@Test
	public void isNotActive() throws AssertionError {
		Trap t = new Trap();
		
		t.activate();
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertFalse(t.isNotActive());
		
		t.deactivate();
		for(int i = 0; i < t.translateTime; i++)
			t.update();
		
		assertTrue(t.isNotActive());
	}

}
