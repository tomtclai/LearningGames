package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.BlockSet;

public class JokerBlockTest {

	@Test
	public void jokerBlockConstructor() {
		BlockSet set = new BlockSet();
		set.addJokerBlock(2);
		
		assertNotNull(set.get(0));
	}

}
