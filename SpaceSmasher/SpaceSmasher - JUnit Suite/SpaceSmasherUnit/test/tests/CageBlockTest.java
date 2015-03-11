package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.Block.BlockType;
import SpaceSmasher.CageBlock;

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

public class CageBlockTest {

	@Test
	public void cageBlockConstructor() throws AssertionError {
		assertNotNull(new CageBlock(false, 2, 4));
	}

	@Test
	public void begineTransition() throws AssertionError {
		CageBlock cg = new CageBlock(false, 2, 3);
		cg.begineTransition(BlockType.CAGE_ACTIVE);
		
		// need to cycle through animation first
		for(int i = 0; i < CageBlock.CLOSE_ANIMATION_FRAME_COUNT; i++)
			cg.update();
		
		// check if transition worked
		assertTrue(cg.getType() == BlockType.CAGE_ACTIVE);
	}

}
