package unitTests;

import static org.junit.Assert.*;
import gridElements.GridElement;
import gridElements.Tile;
import gridElements.GridElement.ColorEnum;

import org.junit.Test;

import corrupted.Game;
import runner.RunTests;

public class InitializeTest {

	@Test
	//no grid or null grid generates default size empty grid
	public void defaultCorruptedInit() {

		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		assertEqualToDefault(RunTests.ct.getRunnee().getTileGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getVirusGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getCorruptedGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getRepairGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
	}
	
	@Test
	//correctly set grid generates grid as defined
	public void setGridCorruptedInit() {

		Game runee = RunTests.ct.getRunnee();
		int myWidth = 13;
		int myHeight = 5;
		int dummyTileX = 2;
		int dummyTileY = 4;
		ColorEnum dummyTileColor = ColorEnum.RED;
		GridElement[][] myTileArray = new GridElement[myWidth][myHeight];
		myTileArray[dummyTileX][dummyTileY] = new Tile(dummyTileColor, runee);
		runee.setTileGrid(myTileArray);
		
		assertEqualToPreset(RunTests.ct.getRunnee().getTileGrid(), myWidth, myHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getVirusGrid(), myWidth, myHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getCorruptedGrid(), myWidth, myHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getRepairGrid(), myWidth, myHeight);
	}
	
	@Test
	//jagged grid generatesdefault size empty grid
	public void setJaggedGridCorruptedInit()
	{
		Game runee = RunTests.ct.getRunnee();
		int myWidth = 13;
		int myHeight = 5;
		int dummyTileX = 2;
		int dummyTileY = 4;
		ColorEnum dummyTileColor = ColorEnum.RED;
		GridElement [][] myTileArray = new GridElement[myWidth][myHeight];
		myTileArray[2] = new GridElement[myHeight-2];
		myTileArray[2][2] = new Tile(dummyTileColor, runee);
		runee.setTileGrid(myTileArray);
		
		
		assertEqualToDefault(RunTests.ct.getRunnee().getTileGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getVirusGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getCorruptedGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
		assertEqualToDefault(RunTests.ct.getRunnee().getRepairGrid(),Game.defaultGridWidth, Game.defaultGridHeight);
	}
	
	private void assertEqualToPreset(GridElement[][] grid, int expectedWidth, int expectedHeight)
	{
		int dummyTileX = 2;
		int dummyTileY = 4;
		assertTrue("Preset GridWidth wrong size",grid.length == expectedWidth);
		for (int x = 0; x < grid.length; x++)
		{
			assertTrue("Preset Height on index "+x+" is wrong size",grid[x].length == expectedHeight);
			for (int y = 0; y < grid[x].length; y++)
			{
				if(x == dummyTileX && y == dummyTileY)
				{
					//for the sake of simplicity Im using a magic color here
					assertTrue("Preset Red tile missing", grid[x][y] != null && grid[x][y].getColorEnum() == ColorEnum.RED);
				}
				else{
					assertTrue("Preset index"+x+","+y+" should have been empty",grid[x][y] == null);
				}
			}
		}
	}
	
	private void assertEqualToDefault(GridElement[][] grid, int expectedWidth, int expectedHeight)
	{
		assertTrue("Default GridWidth is wrong size", grid.length == expectedWidth);
		for (int x = 0; x < grid.length; x++)
		{
			assertTrue("Default Height on index "+x+" is wrong size",grid[x].length == expectedHeight);
			for (int y = 0; y < grid[x].length; y++)
			{
				assertTrue("Default index"+x+","+y+" should have been empty",grid[x][y] == null);
			}
		}
	}
	

}
