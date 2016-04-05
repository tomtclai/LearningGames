package unitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import corrupted.*;
import structures.*;
import layers.*;
import gridElements.*;
import gridElements.GridElement.ColorEnum;
import runner.RunTests;
import testClasses.CorruptedDefault;

public class CorruptionTest {

	@Test
	public void addthecorruption() {
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		
		ArrayList<IntVector> indices = new ArrayList<IntVector>();
		indices.add(new IntVector(0,1));
		indices.add(new IntVector(0,2));
		indices.add(null);
		indices.add(new IntVector(-1,-1));
		
		ArrayList<Boolean> expected = new ArrayList<Boolean>();
		expected.add(true);
		expected.add(true);
		expected.add(false);
		expected.add(false);
		
		ArrayList<Boolean> results = runee.corruptionHelper.addCorruptions(indices);
		assertEquals(results, expected);
	}
	
	@Test 
	public void corruptionValidConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(0,0);
		Corruption test = new Corruption(pos, runee);
		assertEquals(test.getIntCenter(), pos);
	}	
	@Test 
	public void corruptionNullPosConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		Corruption test = new Corruption(null, runee);
		assertEquals(pos, test.getIntCenter()); // this is the default if null
	}
	@Test 
	public void corruptionNullGameConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		Corruption test = new Corruption(pos, null);
		InvalidationTest.assertAndRevertError();
	}
	

}
