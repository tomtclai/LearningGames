package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import corrupted.*;
import structures.*;
import layers.*;
import gridElements.*;
import gridElements.FuseVirus.FuseVirusTypes;
import gridElements.GridElement.ColorEnum;
import runner.RunTests;
import testClasses.CorruptedDefault;

public class VirusTest {

	@Test
	public void burstSpawns() {
		Game runee = RunTests.ct.getRunnee();
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		
		BurstVirus burstSpawner = new BurstVirus(new IntVector(-1,-1),GridElement.getRandomColorEnum(), runee);

		//assure that a burstVirus is where you spawn it
		runee.tileHelper.putTile(new IntVector(0,0), ColorEnum.RED);
		burstSpawner.spawn();
		assertTrue(runee.virusHelper.getElement(0,0).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(0,1).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(1,0).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(1,1).getClass().equals(burstSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		
		runee.tileHelper.putTile(new IntVector(0,runee.getHeight()-1), ColorEnum.RED);
		burstSpawner.spawn();
		assertTrue(runee.virusHelper.getElement(0,runee.getHeight()-1).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(0,runee.getHeight()-2).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(1,runee.getHeight()-1).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(1,runee.getHeight()-2).getClass().equals(burstSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		
		runee.tileHelper.putTile(new IntVector(runee.getWidth()-1,0), ColorEnum.RED);
		burstSpawner.spawn();
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-1,0).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-1,1).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-2,0).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-2,1).getClass().equals(burstSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		
		runee.tileHelper.putTile(new IntVector(runee.getWidth()-1,runee.getHeight()-1), ColorEnum.RED);
		burstSpawner.spawn();
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-1,runee.getHeight()-1).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-1,runee.getHeight()-2).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-2,runee.getHeight()-1).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(runee.getWidth()-2,runee.getHeight()-2).getClass().equals(burstSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
	}
	
	@Test
	public void burstTargetedSpawns()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		BurstVirus burstSpawner = new BurstVirus(new IntVector(-1,-1),GridElement.getRandomColorEnum(), runee);
		
		burstSpawner.spawnAtLocation(new IntVector(5,5));
		assertTrue(runee.virusHelper.getElement(5,5).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(5,6).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(6,5).getClass().equals(burstSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(6,6).getClass().equals(burstSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		
	}
	
	@Test
	public void chainSpawns()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		ChainVirus chainSpawner = new ChainVirus(new IntVector(-1,-1),GridElement.getRandomColorEnum(), runee);
		
		runee.tileHelper.putTile(new IntVector(5,5), ColorEnum.RED);
		
		chainSpawner.spawn();
		//we dont know the vertical position but there should be only one virus and it should be horizontally 5
		IntVector pos =runee.virusHelper.getRandomElementIndex();
		GridElement v = runee.virusHelper.getElement(pos);
		assertEquals(pos.getX(),5);
		assertEquals(v.getClass(),chainSpawner.getClass());
	}
	
	@Test	
	public void chainTargetedSpawns()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		ChainVirus chainSpawner = new ChainVirus(new IntVector(-1,-1),GridElement.getRandomColorEnum(), runee);
		
		chainSpawner.spawnAtLocation(new IntVector(5,5));
		assertTrue(runee.virusHelper.getElement(5,5).getClass().equals(chainSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
	}
	
	@Test
	public void fuseTargetedSpawns()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.virusHelper.clear();
		runee.tileHelper.clear();
		FuseVirus fuseSpawner = new FuseVirus(GridElement.getRandomColorEnum(), runee,FuseVirusTypes.BOT);
		
		fuseSpawner.spawnAtLocation(new IntVector(5,5));
		assertTrue(runee.virusHelper.getElement(5,runee.getHeight()-1).getClass().equals(fuseSpawner.getClass()));
		assertTrue(runee.virusHelper.getElement(5,0).getClass().equals(fuseSpawner.getClass()));
		runee.virusHelper.clear();
		runee.tileHelper.clear();
	}
	
	@Test
	public void burstValidConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(1,1);
		ColorEnum col = ColorEnum.RED;
		BurstVirus test = new BurstVirus(pos, col, runee);
		assertEquals(test.getIntCenter(), pos);
		assertEquals(test.getColorEnum(), col);
	}
	@Test
	public void burstNullPosConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		ColorEnum col = ColorEnum.RED;
		BurstVirus test = new BurstVirus(null, col, runee);
		assertEquals(test.getIntCenter(), pos);
		assertEquals(test.getColorEnum(), col);
	}
	@Test
	public void burstNullColorConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(1,1);
		BurstVirus test = new BurstVirus(pos, null, runee); //color will be random just dont crash
		assertEquals(test.getIntCenter(), pos);
	}
	@Test
	public void burstnullGameConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(1,1);
		ColorEnum col = ColorEnum.RED;
		BurstVirus test = new BurstVirus(pos, col, null);
		InvalidationTest.assertAndRevertError();
	}
	@Test
	public void burst2ValidConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		ColorEnum col = ColorEnum.RED;
		BurstVirus test = new BurstVirus(col, runee);
		assertEquals(test.getIntCenter(), pos);
		assertEquals(test.getColorEnum(), col);
	}
	@Test
	public void burst2nullColConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		BurstVirus test = new BurstVirus(null, runee);
		assertEquals(test.getIntCenter(), pos);
	}
	@Test
	public void burst2nullgameConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		ColorEnum col = ColorEnum.RED;
		BurstVirus test = new BurstVirus(col, null);
		InvalidationTest.assertAndRevertError();
	}
	
	@Test
	public void chainValidConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(1,1);
		ColorEnum col = ColorEnum.RED;
		ChainVirus test = new ChainVirus(pos, col, runee);
		assertEquals(test.getIntCenter(), pos);
		assertEquals(test.getColorEnum(), col);
	}
	@Test
	public void chainNullPosConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		ColorEnum col = ColorEnum.RED;
		ChainVirus test = new ChainVirus(null, col, runee);
		assertEquals(test.getIntCenter(), pos);
		assertEquals(test.getColorEnum(), col);
	}
	@Test
	public void chainNullColorConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(1,1);
		ChainVirus test = new ChainVirus(pos, null, runee); //color will be random just dont crash
		assertEquals(test.getIntCenter(), pos);
	}
	@Test
	public void chainnullGameConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(1,1);
		ColorEnum col = ColorEnum.RED;
		ChainVirus test = new ChainVirus(pos, col, null);
		InvalidationTest.assertAndRevertError();
	}
	@Test
	public void chain2ValidConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		ColorEnum col = ColorEnum.RED;
		ChainVirus test = new ChainVirus(col, runee);
		assertEquals(test.getIntCenter(), pos);
		assertEquals(test.getColorEnum(), col);
	}
	@Test
	public void chain2nullColConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		IntVector pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		ChainVirus test = new ChainVirus(null, runee);
		assertEquals(test.getIntCenter(), pos);
	}
	@Test
	public void chain2nullgameConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		ColorEnum col = ColorEnum.RED;
		ChainVirus test = new ChainVirus(col, null);
		InvalidationTest.assertAndRevertError();
	}
	
	@Test
	public void fuseValidConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		ColorEnum col = ColorEnum.RED;
	    FuseVirus test = new FuseVirus(col, runee,FuseVirusTypes.BOT);
		assertEquals(test.getColorEnum(), col);
		assertTrue(test.isBot());
	}
	
	@Test
	public void fusenullColConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
	    FuseVirus test = new FuseVirus(null, runee,FuseVirusTypes.TOP);
		assertFalse(test.isBot());
	}
	
	@Test
	public void fusenullRuneeConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		ColorEnum col = ColorEnum.RED;
	    FuseVirus test = new FuseVirus(col, null,FuseVirusTypes.BOT);
		InvalidationTest.assertAndRevertError();
	}
	
	@Test
	public void fuse2ValidConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		ColorEnum col = ColorEnum.RED;
	    FuseVirus test = new FuseVirus(5, col, runee,FuseVirusTypes.BOT);
		assertEquals(test.getColorEnum(), col);
		assertTrue(test.isBot());
		assertEquals(test.getIntCenter().getX(), 5);
		
	}
	@Test
	public void fuse2nullColConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
	    FuseVirus test = new FuseVirus(5,null, runee,FuseVirusTypes.TOP);
		assertFalse(test.isBot());
		assertEquals(test.getIntCenter().getX(), 5);
	}
	
	@Test
	public void fuse2nullRuneeConstructorTest(){
		Game runee = RunTests.ct.getRunnee();
		ColorEnum col = ColorEnum.RED;
	    FuseVirus test = new FuseVirus(5,col, null,FuseVirusTypes.BOT);
		InvalidationTest.assertAndRevertError();
	}
	
}
