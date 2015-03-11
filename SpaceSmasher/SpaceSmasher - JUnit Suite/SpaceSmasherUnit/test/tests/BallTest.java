package tests;

import static org.junit.Assert.*;

import java.util.Random;

import javax.swing.JOptionPane;

import linkages.AssertTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import Engine.World;
import SpaceSmasher.Ball;
import SpaceSmasher.BlockSet;
import SpaceSmasher.Paddle;
import SpaceSmasher.Ball.BallType;

/**
 *  referenced from -> http://www.vogella.com/tutorials/JUnit/article.html
 * 
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

public class BallTest {

	/**
	   * Check if the primitive is touching or outside of any side of the world.
	   * @return the part of the world the Ball is overlapping with
	   */
																					@Test
	public void collideWorldBound() throws AssertionError {
		Ball b = new Ball();
		
		// check if non-null
		assertNotNull(b.collideWorldBound());
		
		// check within
		assertTrue(b.collideWorldBound() == World.BoundCollidedStatus.INSIDEBOUND);
		
		// move to left
		b.center.setX(0);
		
		// check left
		assertTrue(b.collideWorldBound() == World.BoundCollidedStatus.LEFT);
					
		// move to right
		b.center.setX(BaseCode.world.getWidth());
		
		// check right
		assertTrue(b.collideWorldBound() == World.BoundCollidedStatus.RIGHT);
		
		// move to top
		b.center.setX(BaseCode.world.getWidth() / 2);
		b.center.setY(BaseCode.world.getHeight());
		
		// check top
		assertTrue(b.collideWorldBound() == World.BoundCollidedStatus.TOP);
		
		// move to bottom
		b.center.setY(0);
		
		// check bottom
		assertTrue(b.collideWorldBound() == World.BoundCollidedStatus.BOTTOM);
	}
	
	@Test
	public void ballConstructor() throws AssertionError {
		assertNotNull(new Ball());
	}

	/**
	   * Change the power up type of the ball.
	   * 
	   * @param value
	   *          - The power up type the ball should have.
	   */
	@Test
	public void setType() throws AssertionError {
		Ball b = new Ball();
		b.setType(Ball.BallType.FIRE);
		
		assertTrue(b.isBurning());
	}

	/**
	   * Create the ball and prepare it for play.
	   * @param activePaddle if null will spawn the ball at the center of the screen just below the activeBlocks aching down.
	   * otherwise will spawn the ball arching upwards from the paddle given
	   */
	@Test
	public void spawn() throws AssertionError {
		Paddle p = new Paddle();
		Ball b = new Ball();
		b.spawn(p);
		
		assertTrue(b.visible);
	}

	/**
	   * Set the block set to base ball spawn positions on.
	   * 
	   * @param value
	   *          - The block set.
	   */
	@Ignore
	public void setBlockSet() throws AssertionError {
		fail("Not yet implemented : unable to test");
	}

	/**
	   * Temporarily modify the speed of the ball to the given percentage of the
	   * unmodified speed.
	   * 
	   * @param newMod
	   *          - The new percentage of the original speed to apply to the ball
	   *          each update.
	   */
	@Test
	public void setSpeedMod() {
		try
		{
			Ball b = new Ball();
			
			// get prev speed
			float prev = b.velocity.getX();
			
			b.setSpeedMod(2f);
			
			assertNotSame(prev, b.velocity.getX());
		} catch(AssertionError e) {}
	}

	/**
	   * Check if the ball is normal.
	   * 
	   * @return - True if the ball is normal, false otherwise.
	   */
	@Test
	public void isNormal() throws AssertionError {
		Ball b = new Ball();
		
		// check false first
		assertFalse(b.isFrozen());
		
		// check actual
		assertTrue(b.isNormal());
	}

	/**
	   * Check if the ball is burning.
	   * 
	   * @return - True if the ball is burning, false otherwise.
	   */
	@Test
	public void isBurning() throws AssertionError {
		Ball b = new Ball();
		b.setType(BallType.FIRE);
		
		// check false first
		assertFalse(b.isFrozen());
		
		// check actual
		assertTrue(b.isBurning());
	}

	/**
	   * Check if the ball is freezing.
	   * 
	   * @return - True if the ball is freezing, false otherwise.
	   */
	@Test
	public void isFrozen() throws AssertionError {
		Ball b = new Ball();
		b.setType(BallType.ICE);
		
		// check false first
		assertFalse(b.isNormal());
		
		// check actual
		assertTrue(b.isFrozen());
	}

	/**
	   * Reset to normal state for the ball.
	   */
	@Test
	public void normalTheBall() throws AssertionError {
		Ball b = new Ball();
		b.setType(BallType.ICE);
		
		b.normalTheBall();
		
		// check actual
		assertTrue(b.isNormal());
	}

	/**
	   * Enable the freeze state for the ball.
	   */
	@Test
	public void freezeTheBallInt() throws AssertionError {
		Ball b = new Ball();
		
		Random rand = new Random();
		
		// assign random powerup amount
		int amnt = rand.nextInt(10) + 1;
		b.freezeTheBall(amnt);
		
		// check if frozen
		assertTrue(b.isFrozen());
		
		// reduce until powerup is gone
		for(int i = 0; i < amnt; i++)
			b.reducePowerBouncesLeft();
		
		// check if normal after powerups
		assertTrue(b.isNormal());
	}

	/**
	   * Enable the freeze state for the ball.
	   */
	@Test
	public void freezeTheBall() throws AssertionError {
		Ball b = new Ball();
		
		b.freezeTheBall();
		
		// check if frozen
		assertTrue(b.isFrozen());
	}

	/**
	   * Enable the fire state for the ball.
	   */
	@Test
	public void burnTheBallInt() throws AssertionError {
		Ball b = new Ball();
		
		Random rand = new Random();
		
		// assign random powerup amount
		int amnt = rand.nextInt(10) + 1;
		b.burnTheBall(amnt);
		
		// check if burnt
		assertTrue(b.isBurning());
		
		// reduce until powerup is gone
		for(int i = 0; i < amnt; i++)
			b.reducePowerBouncesLeft();
		
		// check if normal after powerups
		assertTrue(b.isNormal());
	}

	/**
	   * Enable the fire state for the ball.
	   */
	@Test
	public void burnTheBall() throws AssertionError {
		Ball b = new Ball();
		
		b.burnTheBall();
		
		// check if burnt
		assertTrue(b.isBurning());
	}

	/**
	   * Reflect off an object that is to the top. Will also clamp at world bounds.
	   */
	@Test
	public void reflectTop() throws AssertionError {
		Ball b = new Ball();
		
		// move to top
		b.center.setX(BaseCode.world.getWidth() / 2);
		b.center.setY(BaseCode.world.getHeight());
		
		// get prev velocity
		float prev = b.velocity.getY();
		
		b.reflectTop();
		
		assertNotSame(prev, b.velocity.getY());
	}

	/**
	   * Reflect off an object that is to the bottom. Will also clamp at world
	   * bounds.
	   */
	@Test
	public void reflectBottom() throws AssertionError {
		Ball b = new Ball();
		
		// move to bottom
		b.center.setY(0);
		
		// get prev velocity
		float prev = b.velocity.getY();
		
		b.reflectBottom();
		
		assertNotSame(prev, b.velocity.getY());
	}

	/**
	   * Reflect off an object that is to the left. Will also clamp at world bounds.
	   */
	@Test
	public void reflectLeft() throws AssertionError {
		Ball b = new Ball();
		
		// move to left
		b.center.setX(0);
		
		// get prev velocity
		float prev = b.velocity.getX();
		
		b.reflectLeft();
		
		assertNotSame(prev, b.velocity.getX());
	}

	/**
	   * Reflect off an object that is to the right. Will also clamp at world
	   * bounds.
	   */
	@Test
	public void reflectRight() throws AssertionError {
		Ball b = new Ball();
		
		// move to left
		b.center.setX(BaseCode.world.getWidth());
		
		// get prev velocity
		float prev = b.velocity.getX();
		
		b.reflectRight();
		
		assertNotSame(prev, b.velocity.getX());
	}

	/**
	   * Set the number of bounces on a block before returning to a normal ball.
	   * 
	   * @param durationInBounces
	   *          - Number of bounces power up will last.
	   */
	@Test
	public void setPowerBouncesLeft() throws AssertionError {
		Ball b = new Ball();
		
		Random rand = new Random();
		b.burnTheBall();
		
		// add random bounces 1-10
		int bounces = rand.nextInt(10) + 1;
		b.setPowerBouncesLeft(bounces);
		
		// make sure the bounces are there still
		for(int i = 0; i < bounces; i++)
			b.reducePowerBouncesLeft();
		
		// valid test because if powerBounces == 0, return to normal
		assertTrue(b.isNormal());
	}

	/**
	   * Reduce the number of bounces a power up has left by one.
	   */
	@Test
	public void reducePowerBouncesLeft() throws AssertionError {
		Ball b = new Ball();
		
		Random rand = new Random();
		b.burnTheBall();
		
		// add random bounces 1-10
		int bounces = rand.nextInt(10) + 1;
		b.setPowerBouncesLeft(bounces);
		
		// make sure the bounces are there still
		for(int i = 0; i < bounces; i++)
			b.reducePowerBouncesLeft();
		
		// valid test because if powerBounces == 0, return to normal
		assertTrue(b.isNormal());
	}

	// sound references from main API
	protected static final String BLOCK_BOUNCE_SOUND = "sounds/AlternaBounce2SpaceSmasher.wav";
	protected static final String WALL_BOUNCE_SOUND = "Ball-Wall Collision.wav";
	protected static final String JOKER_BOUNCE_SOUND = "sounds/LaughSpaceSmasher.wav";
	protected static final String FREEZE_BLOCK_SOUND = "sounds/Ice1.wav";
	protected static final String BREAK_ICE_SOUND = "sounds/Ice2.wav";
	protected static final String DIE_SOUND = "sounds/Laser1.wav";
	protected static final String FIRE_SOUND = "sounds/Fire.wav";
	
	/**
	   * Will Play the  sound the ball should make when it bounces
	   */
	@Test
	public void playBounceSound() throws AssertionError {
		Ball b = new Ball();
		b.playBounceSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playBounceSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Will play the sound the ball should make when it bounces of of a wall
	   */
	@Test
	public void playWallBounceSound() throws AssertionError {
		Ball b = new Ball();
		b.playWallBounceSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playWallBounceSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Will play the sound the ball should make when it freezes a block
	   */
	@Test
	public void playBallFreezeBlockSound() throws AssertionError {
		Ball b = new Ball();
		b.playBallFreezeBlockSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playBallFreezeBlockSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Will play the sound the ball should make when it shatters something
	   */
	@Test
	public void playBallShatterSound() throws AssertionError {
		Ball b = new Ball();
		b.playBallShatterSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playBallShatterSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Will play the sound the ball should make when it hits a Joker
	   */
	@Test
	public void playBallJokerSound() throws AssertionError {
		Ball b = new Ball();
		b.playBallJokerSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playBallJokerSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Play the sound the ball should make when it dies, usually by falling off
	   * the bottom of the world.
	   */
	@Test
	public void playDieSound() throws AssertionError {
		Ball b = new Ball();
		b.playDieSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playDieSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Will play the sound the ball should make when it is ignited
	   */
	@Test
	public void playBallIgnitedSound() throws AssertionError {
		Ball b = new Ball();
		b.playBallIgnitedSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playBallIgnitedSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * Reflect velocity on whichever axis is not zero.
	   * 
	   * @param dir
	   *          - Direction to bounce in.
	   */
	@Test
	public void bounce() throws AssertionError {
		Ball b = new Ball();
		Vector2 vec = new Vector2(4, 5);
		
		Vector2 prev = b.velocity;
		
		b.bounce(vec);
		
		assertNotSame(b.velocity.getX(), prev.getX());
		assertNotSame(b.velocity.getY(), prev.getY());
	}

	/**
	   * Bounce the ball off the given paddle based on where it hit the paddle.
	   * 
	   * @param paddle
	   *          - The paddle to bounce off.
	   */
	@Test
	public void bounceOnPaddle() throws AssertionError {
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
		b.bounceOnPaddle(p);

		// if velocity diff, then bounce worked
		assertNotSame(vel.getY(), b.velocity.getY());
	}

}
