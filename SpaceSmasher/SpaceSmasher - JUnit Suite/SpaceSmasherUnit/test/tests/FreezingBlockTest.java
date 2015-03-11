package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.BlockSet;

public class FreezingBlockTest {

	@Test
	public void freezingBlockConstructor() {
		BlockSet set = new BlockSet();
		set.addFreezingBlock(2);
		
		assertNotNull(set.get(0));
	}

}
