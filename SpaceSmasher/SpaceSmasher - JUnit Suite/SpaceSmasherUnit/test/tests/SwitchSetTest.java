package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.Switch;
import SpaceSmasher.SwitchSet;

public class SwitchSetTest {

	@Test
	public void switchSetConstructor() throws AssertionError {
		assertNotNull(new SwitchSet());
	}

	/**
	   * Show all switches.
	   */
	@Test
	public void activate() throws AssertionError {
		SwitchSet set = new SwitchSet();
		set.add(new Switch());
		
		assertFalse(set.get(0).isActive());
		
		set.activate();
		assertTrue(set.get(0).isActive());
	}

	/**
	   * Hide all switches.
	   */
	@Test
	public void deactivate() throws AssertionError {
		SwitchSet set = new SwitchSet();
		set.add(new Switch());
		
		set.activate();
		assertTrue(set.get(0).isActive());
		
		set.deactivate();
		assertFalse(set.get(0).isActive());
	}

	@Test
	public void clear() throws AssertionError {
		SwitchSet set = new SwitchSet();
		for(int i = 0; i < 10; i++)
			set.add(new Switch());
		
		assertTrue(set.size() == 10);
		
		set.clear();
	
		assertTrue(set.size() == 0);
	}

	@Test
	public void removeInt() throws AssertionError {
		SwitchSet set = new SwitchSet();
		Switch mySwitch = new Switch();
		
		set.add(mySwitch);
		
		for(int i = 0; i < 10; i++)
			set.add(new Switch());
		
		// before removal
		assertTrue(set.contains(mySwitch));
		
		// after removal - myswitch was at index 0
		set.remove(0);		
		assertFalse(set.contains(mySwitch));
	}

	@Test
	public void removeRectangle() throws AssertionError {
		SwitchSet set = new SwitchSet();
		Switch mySwitch = new Switch();
		
		set.add(mySwitch);
		
		for(int i = 0; i < 10; i++)
			set.add(new Switch());
		
		// before removal
		assertTrue(set.contains(mySwitch));
		
		// after removal
		set.remove(mySwitch);		
		assertFalse(set.contains(mySwitch));
	}

}
