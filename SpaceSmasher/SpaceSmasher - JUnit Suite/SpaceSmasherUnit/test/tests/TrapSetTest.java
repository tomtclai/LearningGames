package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.Trap;
import SpaceSmasher.TrapSet;

public class TrapSetTest {

	/**
	   * Overrides setofitem's clear to also clear the indicators.
	   */
	@Test
	public void clear() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add(5);
		
		assertTrue(set.size() > 0);
		
		set.clear();
		
		assertTrue(set.size() == 0);
	}

	@Test
	public void trapSet() throws AssertionError {
		assertNotNull(new TrapSet());
	}

	/**
	   * Add the given number of traps to the set.
	   * 
	   * @param num
	   *          - Number of traps to add.
	   */
	@Test
	public void addInt() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add(5);
		
		
		assertTrue(set.size() == 5);
	}

	/**
	   * Add a single trap to the set.
	   */
	@Test
	public void add() throws AssertionError {
		TrapSet set = new TrapSet();
		for(int i = 0; i < 5; i++)
			set.add();
		
		
		assertTrue(set.size() == 5);
	}

	/**
	   * Return the width of the traps.
	   * 
	   * @return - The width of the traps.
	   */
	@Test
	public void getWidth() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add();
		
		assertNotNull(set.getWidth());
	}

	/**
	   * Return the height of the traps.
	   * 
	   * @return - The height of the traps.
	   */
	@Test
	public void getHeight() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add();
		
		assertNotNull(set.getHeight());
	}

	/**
	   * Get the first trap that is located on the left side of the world.
	   * 
	   * @return - The first trap on the left side of the world.
	   */
	@Test
	public void left() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add();
		assertNotNull(set.left());
	}

	/**
	   * Get the first trap that is located on the right side of the world.
	   * 
	   * @return - The first trap on the right side of the world.
	   */
	@Test
	public void right() throws AssertionError {
		TrapSet set = new TrapSet();
		
		// only adding one will result in null
		set.add();
		assertNull(set.right());
		
		// add enough so that right can be found
		set.add(12);
		assertNotNull(set.right());
	}

	/**
	   * Make all of the walls look activated.
	   */
	@Test
	public void activate() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add();
		set.activate();
		
		for(int i = 0; i < set.get(0).translateTime; i++)
			set.get(0).update();
		
		assertTrue(set.get(0).isActive());
	}

	/**
	   * Make all of the walls look deactivated.
	   */
	@Test
	public void deactivate() throws AssertionError {
		TrapSet set = new TrapSet();
		set.add();
		set.deactivate();
		
		for(int i = 0; i < set.get(0).translateTime; i++)
			set.get(0).update();
		
		assertFalse(set.get(0).isActive());
	}

}
