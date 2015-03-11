package tests;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import linkages.AssertTest;

import org.junit.Test;

import SpaceSmasher.Ball;
import SpaceSmasher.Switch;

public class SwitchTest {

	@Test
	public void switchConstructor() throws AssertionError {
		assertNotNull(new Switch());
	}

	/**
	   * Bounce the ball off the paddle.
	   * 
	   * @param ball
	   *          - The ball to bounce.
	   */
	@Test
	public void reflect() throws AssertionError {
		Switch sw = new Switch();
		
		Ball b = new Ball();
		// set velocity up towards ball
		b.velocity.setY(2);
		
		// get bottom of switch
		int bot_of_switch = (int) (sw.center.getY() - (sw.size.getY() / 2));
		
		// move the ball to touch switch
		b.center.set(sw.center.getX(), bot_of_switch);
		
		assertTrue(b.velocity.getY() == 2);
		
		sw.reflect(b);
		
		assertTrue(b.velocity.getY() == -2);
		
	}

	/**
	   * Check if the switch is active and in visible.
	   * 
	   * @return - True if the switch is active and visible.
	   */
	@Test
	public void isActive() throws AssertionError {
		Switch sw = new Switch();
		
		sw.activate();
		assertTrue(sw.isActive());
		
		sw.deactivate();
		assertFalse(sw.isActive());
	}

	/**
	   * The switch will become active.
	   */
	@Test
	public void activate() throws AssertionError {
		Switch sw = new Switch();
		
		sw.activate();
		assertTrue(sw.isActive());
	}

	/**
	   * Will play the sound associated with the switch becoming active
	   */
	@Test
	public void playActivationSound() throws AssertionError {
		Switch sw = new Switch();
		sw.playActivationSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playActivationSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

	/**
	   * The switch is no longer active
	   */
	@Test
	public void deactivate() throws AssertionError {
		Switch sw = new Switch();
		
		sw.deactivate();
		assertFalse(sw.isActive());
	}

	/**
	   * Will play the sound associated with the switch turning off
	   */
	@Test
	public void playDeactivationSound() throws AssertionError {
		Switch sw = new Switch();
		sw.playDeactivationSound();
		
		int outcome = new AssertTest("Did you hear the sound?", "playDeactivationSound").create();
		if(outcome != JOptionPane.OK_OPTION) fail("no sound played");
	}

}
