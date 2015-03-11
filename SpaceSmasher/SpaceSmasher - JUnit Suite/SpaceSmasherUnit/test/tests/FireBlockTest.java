package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.BlockSet;

public class FireBlockTest {

	@Test
	public void fireBlockConstructor() {
		BlockSet set = new BlockSet();
		set.addFireBlock(2);
		
		// regulare FireBlock constructor is hidden
		assertNotNull(set.get(0));
	}

}
