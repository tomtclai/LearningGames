package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.LifeSet;

public class LifeSetTest {

	@Test
	public void lifeSetConstructor() throws AssertionError {
		assertNotNull( new LifeSet() );
	}

	/**
	 * Will increment the amount of Health Shown by 1 and will increase MaxSegments to accommodate
	 * the increased amount of health if necessary  
	 */
	@Test
	public void add() throws AssertionError {
		LifeSet set = new LifeSet();
		set.add();
		
		assertTrue(set.getCount() == 1);
	}

	/**
	 * Will increment the amount of Health Shown by the given amount will increase MaxSegments to accommodate
	 * the increased amount of health if necessary  
	 * @param amount the amount health will be incremented by
	 */
	@Test
	public void addInt() throws AssertionError {
		LifeSet set = new LifeSet();
		
		set.add(5);
		
		assertTrue(set.getCount() == 5);
	}

	/**
	 * Will Decrement the amount of health shown by 1
	 */
	@Test
	public void remove() throws AssertionError {
		LifeSet set = new LifeSet();
		set.add(3);
		
		set.remove();
		assertTrue(set.getCount() == 2);
		
		for(int i = 0; i < 7; i++)
			set.remove();
		
		assertTrue(set.getCount() == 0);
	}

	/**
	 * Will Decrement the amount of health shown by the given amount if the new Health levels 
	 * is greater then the current MaxSegments then the health level will be decreased to MaxSegments
	 * @param amount the given amount
	 */
	@Test
	public void removeInt() throws AssertionError {
		LifeSet set = new LifeSet();
		set.add(3);
		
		fail("need to add an edge case because removing more life then there is will crash");
		set.remove(12);
		
		assertTrue(set.getCount() == 0);
	}

	/**
	 * Will return the amount of health available
	 * @return amount of health available
	 */
	@Test
	public void getCount() throws AssertionError {
		LifeSet set = new LifeSet();
		set.add(20);
		
		set.remove(12);
		
		assertTrue(set.getCount() == 8);
	}

}
