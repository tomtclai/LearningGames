package runner;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testClasses.*;
import unitTests.*;

@RunWith(Suite.class)
@SuiteClasses({ InitializeTest.class, InvalidationTest.class, TileTest.class, CorruptionTest.class, VirusTest.class, GameTest.class, ParticleTest.class, StructureTest.class})
public class RunTests {
	public static CorruptedDefault ct;
	
	@BeforeClass
	public static void setUp()
	{
		ct = new CorruptedDefault();
		
		//ct.getRunnee().setTileGrid(null);
	}
}
