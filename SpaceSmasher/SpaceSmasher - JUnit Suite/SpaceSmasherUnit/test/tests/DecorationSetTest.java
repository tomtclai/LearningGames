package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Engine.Rectangle;
import SpaceSmasher.DecorationSet;

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

public class DecorationSetTest {

	@Test
	public void decorationSetConstructor() {
		assertNotNull(new DecorationSet());
	}

	@Test
	public void clear() {
		DecorationSet set = new DecorationSet();
		
		for(int i = 0; i < 10; i++)
			set.add(new Rectangle());
		
		assertFalse(set.size() == 0);
		
		set.clear();
		
		assertTrue(set.size() == 0);
	}

	@Test
	public void removeInt() {
		DecorationSet set = new DecorationSet();
		
		Rectangle myRec = new Rectangle();
		for(int i = 0; i < 10; i++)
			set.add(new Rectangle());
		
		set.add(myRec);
		
		// remove myRec @ index 10
		set.remove(10);
		
		assertFalse(set.contains(myRec));
	}

	@Test
	public void removeRectangle() {
DecorationSet set = new DecorationSet();
		
		Rectangle myRec = new Rectangle();
		for(int i = 0; i < 10; i++)
			set.add(new Rectangle());
		
		set.add(myRec);
		
		// remove myRec
		set.remove(myRec);
		
		assertFalse(set.contains(myRec));
	}

}
