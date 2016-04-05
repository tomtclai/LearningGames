package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import Engine.Vector2;
import structures.IntVector;

public class StructureTest {

	@Test
	public void intVectorTest() {
		Vector2 v2 = new Vector2(1,1);
		IntVector iv = new IntVector(-1,-1);
		IntVector iv1 = new IntVector(-1,-1);
		assertEquals(iv.toString(), "(-1,-1)");
		assertTrue(iv.equals(iv1));
		IntVector iv2 = new IntVector(2,2);
		assertFalse(iv.equals(iv2));
		assertTrue(iv.equals(iv));
		assertFalse(iv.equals(null));
		iv.set(0,0);
		assertEquals(iv, new IntVector(0,0));
		iv.set(iv2);
		assertEquals(iv,iv2);
		iv.set(v2);
		assertTrue(iv.toVector2().getX()== v2.getX());
		assertTrue(iv.toVector2().getY()== v2.getY());
		

		IntVector low = new IntVector(-1,-1);
		IntVector high = new IntVector(2,2);
		IntVector low2 = new IntVector(0,0);
		IntVector high2 = new IntVector(1,1);
		low.forceIntoBounds(0, 0, 1, 1);
		high.forceIntoBounds(0, 0, 1, 1);
		assertEquals(low, low2);
		assertEquals(high, high2);
		
	}

}
