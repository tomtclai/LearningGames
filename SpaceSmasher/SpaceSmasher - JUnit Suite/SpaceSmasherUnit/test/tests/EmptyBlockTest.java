package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import SpaceSmasher.BlockSet;
import SpaceSmasher.EmptyBlock;

public class EmptyBlockTest {

	@Test
	public void emptyBlockConstructor() {
		BlockSet set = new BlockSet();
		set.addEmptyBlock(2);
		assertNotNull(set.get(0));
	}

}
