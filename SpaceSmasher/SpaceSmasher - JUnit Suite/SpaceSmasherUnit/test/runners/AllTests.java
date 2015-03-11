package runners;
import java.io.File;

import linkages.SpaceSmasherTrial;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.*;

/**
 * 	AllTests acts as its own test case iterating through the linked classes and 
 * 	testing them. In other terms its the main test Suite.
 * 
 * 	Using this alone will only test the classes and not display any human-readable output
 * 
 * @author Chris Sullivan 2014
 */

@RunWith(Suite.class)
@SuiteClasses({ BallTest.class, BallSetTest.class, PaddleSetTest.class, PaddleTest.class,
	BlockTest.class, BlockSetTest.class, CageBlockTest.class, DecorationSetTest.class,
	EmptyBlockTest.class, FireBlockTest.class, FreezingBlockTest.class, JokerBlockTest.class,
	LifeSetTest.class, SwitchTest.class, SwitchSetTest.class, TrapTest.class,
	TrapSetTest.class })
public class AllTests {

	/** output selection, holder **/
	public static File fout;
	
	public static SpaceSmasherTrial tr;
	
	@BeforeClass
	public static void setUp()
	{
		tr = new SpaceSmasherTrial();
	}
	
}
