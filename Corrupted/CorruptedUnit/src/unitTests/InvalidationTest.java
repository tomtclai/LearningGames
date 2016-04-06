package unitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import gridElements.*;
import gridElements.GridElement.ColorEnum;

import org.junit.Test;

import corrupted.Game;
import corrupted.ErrorHandler;
import runner.RunTests;
import structures.IntVector;

public class InvalidationTest {

	@Test
	public void setJaggedArray() {
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		//save orignal column2 and replace it with one that is too short
		GridElement[][] grid = runee.getTileGrid();
		GridElement[] originalColumn = grid[2];
		
		grid[2] = new GridElement[2];
		
		//testing getFurthestColumn first while bad column is empty
		//if there are tiles in there, it breaks on first tile found and behaves normall (is this ok?)
		//if there are no tiles yet, it will keep looping off the tiny array in search of tiles
		runee.tileHelper.getFurthestColumn();
		assertAndRevertError();
		
		//test adding tile to something within normal bounds but invalidated by the previous column swap
		runee.tileHelper.putTile(new IntVector(2,2), ColorEnum.BLUE);
		assertAndRevertError();
		//now do it again where it is within the invalid size (this is wont cause break. but should it?)
		runee.tileHelper.putTile(new IntVector(2,0), ColorEnum.BLUE);
		assertFalse("Putting Tile in short row should be fine", ErrorHandler.exitOnNextUpdate);
		grid[2][1] = new Tile(ColorEnum.RED, runee);
		runee.tileHelper.connectAllTiles();
		assertAndRevertError();
		runee.tileHelper.syncPositions();
		assertAndRevertError();
		runee.tileHelper.getElement(2, 2);
		assertAndRevertError();
		runee.tileHelper.markForDelete(2, 2);
		assertAndRevertError();
		runee.tileHelper.markForDelete(new IntVector(2,2));
		assertAndRevertError();
		runee.tileHelper.deleteAllMarked();
		assertAndRevertError();
		runee.tileHelper.getRandomElementIndex() ;
		assertAndRevertError();
		runee.tileHelper.count();
		assertAndRevertError();
//		runee.TL.moveElement(true, IntVector currentPosition, IntVector newPosition);
//		assertAndRevertError();
		runee.tileHelper.clear();
		assertAndRevertError();
		
		//checking corruptedGrid  invalidations
		grid = runee.getCorruptedGrid();
		GridElement[] originalCorruptedColumn = grid[2];
		grid[2] = new GridElement[2];
		runee.corruptionHelper.addCorruption(new IntVector(2,2));
		assertAndRevertError();
		//if still within bounds its okay
		runee.corruptionHelper.addCorruption(new IntVector(2,0));
		assertFalse("Putting Corrupted in short row should be fine", ErrorHandler.exitOnNextUpdate);
		
		//fix the array so we dont die later
		runee.setTileGrid(null);
	}
	
	public static void assertAndRevertError()
	{
		assertTrue("Expected Error catch did not occur", ErrorHandler.exitOnNextUpdate);
		ErrorHandler.exitOnNextUpdate = false;
	}

}
