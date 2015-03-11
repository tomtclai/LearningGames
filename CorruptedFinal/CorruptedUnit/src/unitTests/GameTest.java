package unitTests;

import static org.junit.Assert.*;
import gridElements.*;
import gridElements.GridElement.ColorEnum;

import org.junit.Test;

import Engine.Vector2;
import runner.RunTests;
import structures.IntVector;
import ui.CorruptionBarMeter;
import ui.RepairMeter;
import corrupted.Game;

public class GameTest {

	@Test
	public void test() {
		Game runee = RunTests.ct.getRunnee();
		
		String[] strings = new String[]{"1","2","3","4","5"};
		runee.setConsole(strings);
		runee.showConsole();
		
	}
	
	@Test 
	public void setGridGetCheck()
	{
		Game runee = RunTests.ct.getRunnee();
		GridElement[][] grid = new GridElement[25][10];
		runee.setTileGrid(grid);
		assertTrue(grid == runee.getTileGrid()); //check same instance
	}
	
	@Test 
	public void setColumnGetCheck()
	{
		Game runee = RunTests.ct.getRunnee();
		GridElement[] column = new GridElement[10];
		runee.setTileColumn(column, 5);
		assertTrue(column == runee.getTileColumn(5));// check same instance
		assertFalse(column == runee.getTileColumn(4));
		assertFalse(column == runee.getTileColumn(6));
		assertFalse(column == runee.getTileColumn(2));
	}	
	@Test 
	public void setColumnWrongSizeCheck()
	{
		Game runee = RunTests.ct.getRunnee();
		GridElement[] column = new GridElement[5];
		runee.setTileColumn(column, 5);
		assertFalse(column == runee.getTileColumn(4));
		
	}
	
	@Test
	public void syncPositions()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector defpos = new IntVector(-1,-1);
		Tile tile = new Tile(defpos, ColorEnum.RED, runee);
		runee.getTileGrid()[5][5] = tile;
		assertEquals(runee.tileHelper.getElement(5, 5),tile);
		assertEquals(runee.tileHelper.getElement(5, 5).getIntCenter(), defpos);
		runee.syncPositions();
		assertEquals(runee.tileHelper.getElement(5, 5),tile);
		assertEquals(runee.tileHelper.getElement(5, 5).getIntCenter(), new IntVector(5,5));

		runee.tileHelper.clear();
	}
	@Test 
	public void playerTile()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector target = new IntVector(9,9);
		ColorEnum playerColor = runee.getPlayerColorEnum();
		Tile tile = runee.generatePlayerTile();
		runee.getTileGrid()[9][9] = tile;
		assertEquals(runee.tileHelper.getElement(9,9), tile);
		assertEquals(tile.getColorEnum(),playerColor);	
		runee.tileHelper.clear();
	}
	
	@Test 
	public void setgetPlayercolor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.setPlayerColorEnum(ColorEnum.GREEN);
		assertEquals(ColorEnum.GREEN,runee.getPlayerColorEnum());
		runee.setPlayerColorEnum(ColorEnum.RED);
		assertEquals(ColorEnum.RED,runee.getPlayerColorEnum());
	}
	
	@Test
	public void setgetPlayerPosition()
	{
		Game runee = RunTests.ct.getRunnee();

		IntVector toobig = new IntVector(0,runee.getHeight()*2);
		IntVector toosmall = new IntVector(0,-5);
		IntVector justright = new IntVector(0,runee.getHeight()/2);
				
		runee.setPlayerHeight(toobig.getY());
		assertEquals(runee.getPlayerHeight(), runee.getHeight()-1);
		assertEquals(runee.getPlayerPosition(), new IntVector(0,runee.getHeight()-1));
		
		runee.setPlayerHeight(toosmall.getY());
		assertEquals(runee.getPlayerHeight(), 0);
		assertEquals(runee.getPlayerPosition(), new IntVector(0,0));
		
		runee.setPlayerHeight(justright.getY());
		assertEquals(runee.getPlayerHeight(), justright.getY());
		assertEquals(runee.getPlayerPosition(), justright);
	}
	
	@Test
	public void playerUp()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.setPlayerHeight(runee.getHeight()/2);
		int prevpos = runee.getPlayerHeight();
		do{
			runee.movePlayerUp();
			assertTrue(runee.getPlayerHeight() == prevpos+1 || runee.getPlayerHeight() == runee.getHeight()-1);
		}while(runee.getPlayerHeight() == ++prevpos);
		assertTrue(runee.getPlayerHeight() == runee.getHeight()-1);
	}

	@Test
	public void playerDown()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.setPlayerHeight(runee.getHeight()/2);
		int prevpos = runee.getPlayerHeight();
		do{
			runee.movePlayerDown();
			assertTrue(runee.getPlayerHeight() == prevpos-1 || runee.getPlayerHeight() == 0);
		}while(runee.getPlayerHeight() == --prevpos);
		assertTrue(runee.getPlayerHeight() == 0);
	}
	
	@Test
	public void shift()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(new IntVector(0,0), ColorEnum.MAGENTA);
		runee.shiftGridsDown();
		assertEquals(runee.tileHelper.count(), 0);
	}

	@Test
	public void mouse()
	{
		Game runee = RunTests.ct.getRunnee();
		IntVector ms = runee.getMouseCenter();
		assertTrue(runee.tileHelper.withinBounds(ms));
	}
	
	@Test
	public void lolStuffHappens()
	{
		//stuff is happening here with a degree of randomness. just dont crash

		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.virusHelper.clear();
		runee.corruptionHelper.clear();
		runee.repairHelper.clear();
		
		runee.tileHelper.putTile(new IntVector(5,5), ColorEnum.RED);
		BurstVirus bv = new BurstVirus(ColorEnum.RED, runee);
		bv.spawn();
		assertTrue(runee.virusHelper.getElement(5,5) != null);
		for(int i = 0; i < 6; i++){
			runee.runLayerUpdateSequence();
		}
		assertTrue(runee.corruptionHelper.getElement(5,5) != null);
		runee.tileHelper.clear();
		runee.virusHelper.clear();
		runee.corruptionHelper.clear();
		
		runee.tileHelper.putTile(new IntVector(5,5), ColorEnum.RED);
		ChainVirus cv = new ChainVirus(ColorEnum.RED, runee);
		cv.spawn();
		assertTrue(runee.virusHelper.count() == 1);
		for(int i = 0; i < 10; i++){
			runee.runLayerUpdateSequence();
		}
		assertTrue(runee.corruptionHelper.count() > 0);
		runee.tileHelper.clear();
		runee.virusHelper.clear();
		runee.corruptionHelper.clear();
		
		runee.tileHelper.putTile(new IntVector(5,5), ColorEnum.RED);
		FuseVirus fv = new FuseVirus(ColorEnum.RED, runee, null);
		fv.spawn();
		assertTrue(runee.virusHelper.count() == 2);
		for(int i = 0; i < 10; i++){
			runee.runLayerUpdateSequence();
		}
		assertTrue(runee.corruptionHelper.count() > 0);
		
		RepairVirus rv = new RepairVirus(runee);
		rv.spawn();
		assertTrue(runee.repairHelper.count() == 1);
		for(int i = 0; i < 10; i++){
			runee.runLayerUpdateSequence();
		}
	}
	
	@Test
	public void animmetertest()
	{
		RepairMeter rm = new RepairMeter(new Vector2(0,0), new Vector2(1,1), 10);
		assertTrue(rm.getValue()==0);
		assertTrue(rm.getMaxValue()==10);
		assertFalse(rm.isFull());
		rm.incrementValue(20);
		assertTrue(rm.getValue()==10);
		assertTrue(rm.isFull());
		rm.reset();
		assertTrue(rm.getValue()==0);
		rm.setValue(-10);
		assertTrue(rm.getValue()==0);;
		rm.setValue(100);
		assertTrue(rm.getValue()==10);
	}
	
	@Test
	public void barmetertest()
	{
		CorruptionBarMeter bm = new CorruptionBarMeter(new Vector2(0,0), new Vector2(1,1), 10, .5f);
		assertTrue(bm.getValue()==0);
		assertTrue(bm.getMaxValue()==10);
		assertFalse(bm.isFull());
		bm.incrementValue(20);
		assertTrue(bm.getValue()==10);
		assertTrue(bm.isFull());
		bm.reset();
		assertTrue(bm.getValue()==0);
		bm.setValue(-10);
		assertTrue(bm.getValue()==0);;
		bm.setValue(100);
		assertTrue(bm.getValue()==10);
	}
}

