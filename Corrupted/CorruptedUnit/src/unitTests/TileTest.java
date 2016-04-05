package unitTests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import corrupted.*;
import structures.*;
import layers.*;
import gridElements.*;
import gridElements.GridElement.ColorEnum;
import runner.RunTests;
import testClasses.CorruptedDefault;

public class TileTest {

	@Test
	public void constructorsPuttersGettersAndSetters() {
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(new IntVector(0,0), ColorEnum.RED);
		Tile t1 = new Tile(runee);
		Tile t2 = new Tile(ColorEnum.GREEN, runee);
		Tile t3 = new Tile(new IntVector(3,3), ColorEnum.BLUE, runee);
		Tile t4 = new Tile(runee);

		assertTrue(runee.tileHelper.putTile( new IntVector(0,1), t1));
		assertTrue(runee.tileHelper.putTile( new IntVector(1,0), t2));
		assertTrue(runee.tileHelper.putTile( new IntVector(1,1), t3));
		assertFalse(runee.tileHelper.putTile( new IntVector(-1,-1), t4));
		
		assertEquals(runee.tileHelper.getElement(new IntVector(0,0)).getColorEnum(), ColorEnum.RED);
		assertEquals(runee.tileHelper.getElement(0,1),t1);
		assertEquals(runee.tileHelper.getElement(1,0),t2);
		assertEquals(runee.tileHelper.getElement(1,1),t3);

		assertNull(runee.tileHelper.getElement(null));
		assertNull(runee.tileHelper.getElement(new IntVector(-1,-1)));
		assertNull(runee.tileHelper.getElement(-1,-1));
		
		Tile nullTile = null;
		ColorEnum nullColor = null;
		
		//null inputs should return false and should not be inserted
		assertFalse(runee.tileHelper.putTile(null, nullTile));
		assertFalse(runee.tileHelper.putTile(null, nullColor));
		assertFalse(runee.tileHelper.putTile(null, t4));
		assertFalse(runee.tileHelper.putTile(null, ColorEnum.RED));
		//make sure after all these operatoins,  (1,1) has not been changed.
		assertTrue(runee.tileHelper.putTile(new IntVector(1,1), nullTile));
		assertTrue(runee.tileHelper.putTile(new IntVector(1,0), nullColor));
		assertEquals(runee.tileHelper.getElement(1,1),null);	
		assertEquals(runee.tileHelper.getElement(1,0),null);	
		runee.tileHelper.clear();
		assertEquals(runee.tileHelper.count(), 0);
	}
	
	@Test
	public void contiguousTest()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(new IntVector(0,0), ColorEnum.BLUE);
		assertEquals(runee.tileHelper.getContiguousTiles(new IntVector(0,0)).size(), 1);
		runee.tileHelper.putTile(new IntVector(0,1), ColorEnum.BLUE);
		assertEquals(runee.tileHelper.getContiguousTiles(new IntVector(0,0)).size(), 2);
		runee.tileHelper.putTile(new IntVector(1,0), ColorEnum.BLUE);
		assertEquals(runee.tileHelper.getContiguousTiles(new IntVector(0,0)).size(), 3);
		runee.tileHelper.putTile(new IntVector(1,1), ColorEnum.BLUE);
		assertEquals(runee.tileHelper.getContiguousTiles(new IntVector(0,0)).size(), 4);
		runee.tileHelper.putTile(new IntVector(1,1), ColorEnum.RED);
		assertEquals(runee.tileHelper.getContiguousTiles(new IntVector(0,0)).size(), 3);
		Tile nullTile = null;
		runee.tileHelper.putTile(new IntVector(1,1), nullTile);
		assertEquals(runee.tileHelper.getContiguousTiles(new IntVector(0,0)).size(), 3);
	}
	
	@Test
	public void colorChangeTest()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector pos = new IntVector(1,1);
		runee.tileHelper.putTile(pos, ColorEnum.RED);
		assertEquals(runee.tileHelper.getElement(1, 1).getColorEnum(), ColorEnum.RED);
		//change color and check
		assertTrue(runee.tileHelper.changeTileColor(pos, ColorEnum.BLUE));
		assertEquals(runee.tileHelper.getElement(1, 1).getColorEnum(), ColorEnum.BLUE);
		//check that invalid calls do nothing
		assertFalse(runee.tileHelper.changeTileColor(null, ColorEnum.GREEN));
		assertFalse(runee.tileHelper.changeTileColor(new IntVector(-1,-1), ColorEnum.GREEN));
		assertFalse(runee.tileHelper.changeTileColor(pos, null));
		assertTrue(runee.tileHelper.changeTileColor(pos, ColorEnum.BLUE));
		//check that calling on an empty index returns false
		assertFalse(runee.tileHelper.changeTileColor(new IntVector(5,5), ColorEnum.BLUE));
		runee.tileHelper.clear();
	}
	
	@Test
	public void connectorTest()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(new IntVector(0,0), ColorEnum.RED);
		runee.tileHelper.putTile(new IntVector(1,0), ColorEnum.RED);
		runee.tileHelper.putTile(new IntVector(2,0), ColorEnum.RED);
		runee.tileHelper.putTile(new IntVector(3,0), ColorEnum.RED);
		runee.tileHelper.putTile(new IntVector(4,0), ColorEnum.RED);
		
		//this should do nothing and should not crash
		runee.tileHelper.connectTile(0,1);
		
		//this connects the second tile to the first and third
		runee.tileHelper.connectTile(1,0);
		
		Tile connectedTile0 = (Tile)runee.tileHelper.getElement(0,0);
		Tile connectedTile1 = (Tile)runee.tileHelper.getElement(1,0);
		Tile connectedTile2 = (Tile)runee.tileHelper.getElement(2,0);
		Tile connectedTile3 = (Tile)runee.tileHelper.getElement(3,0);
		Tile connectedTile4 = (Tile)runee.tileHelper.getElement(4,0);
		Tile[] tiles = new Tile[]
				{
				connectedTile0,
				connectedTile1,
				connectedTile2,
				connectedTile3,
				connectedTile4
				};
		for (int tile = 0; tile < tiles.length; tile++)
		{
			for(int direction = 0; direction < 8; direction++)
			{
				//leftmost tile should be connected to the right
				if(tile == 0 && direction == 2)
				{
					assertTrue("left tile should be connected right",tiles[tile].isConnected(direction));
				}
				//second and third  tile should be connected to the right and left
				else if((tile == 1 || tile == 2 ) && (direction == 2 || direction == 6))
				{
					assertTrue("middle tiles should be connected both sides",tiles[tile].isConnected(direction));
				}
				//all other directions should be unConnected
				else
				{
					assertFalse("tile "+tile+" should not be connected in direction:"+direction,tiles[tile].isConnected(direction));
				}
			}
		}
		
		connectedTile2.setColorEnum(ColorEnum.MAGENTA);
		runee.tileHelper.connectAllTiles();
		for (int tile = 0; tile < tiles.length; tile++)
		{
			for(int direction = 0; direction < 8; direction++)
			{
				//first and fourth should be connected right
				if((tile == 0 || tile == 3)&& direction == 2)
				{
					assertTrue("pair lefts should be connected right",tiles[tile].isConnected(direction));
				}
				//second and fifth should be connected left
				else if((tile == 1 || tile == 4) && (direction == 6))
				{
					assertTrue("pair rights should be connected left",tiles[tile].isConnected(direction));
				}
				//all other directions should be unConnected
				else
				{
					assertFalse("tile "+tile+" should not be connected in direction:"+direction,tiles[tile].isConnected(direction));
				}
			}
		}		
	}
	
	@Test
	public void randomExistingColor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(new IntVector(1,1), ColorEnum.RED);
		int redCount = 0;
		int greenCount = 0;
		int blueCount = 0;
		int loopCount = 1000;

		assertEquals(ColorEnum.RED, runee.tileHelper.getRandomExistingColor());

		
		runee.tileHelper.putTile(new IntVector(2,2), ColorEnum.GREEN);
		runee.tileHelper.putTile(new IntVector(3,3), ColorEnum.BLUE);
		for (int i = 0; i < loopCount; i++)
		{
			ColorEnum col = runee.tileHelper.getRandomExistingColor();
			if(ColorEnum.RED.equals(col))
			{
				redCount++;
			}
			else if(ColorEnum.GREEN.equals(col))
			{
				greenCount++;
			}
			else if(ColorEnum.BLUE.equals(col))
			{
				blueCount++;
			}
		}
		assertEquals(redCount+greenCount+blueCount, loopCount);
		
		//though it is possible for us to roll 1000 times and be missing one of these, but honestly it should not happen in my lifetime
		assertTrue(""+loopCount+" random picks of RGB never hit R",redCount > 1);
		assertTrue(""+loopCount+" random picks of RGB never hit G",greenCount > 1);
		assertTrue(""+loopCount+" random picks of RGB never hit B",blueCount > 1);

		runee.tileHelper.clear();
	}
	
	@Test
	public void randomExistingIndex()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector oneVec = new IntVector(runee.getWidth()-1,runee.getHeight()-1);
		IntVector twoVec = new IntVector(2,2);
		IntVector threeVec = new IntVector(3,3);
		runee.tileHelper.putTile(oneVec, ColorEnum.RED);
		int oneCount = 0;
		int twoCount = 0;
		int threeCount = 0;
		int loopCount = 1000;

		assertEquals(oneVec, runee.tileHelper.getRandomElementIndex());

		runee.tileHelper.putTile(twoVec, ColorEnum.GREEN);
		runee.tileHelper.putTile(threeVec, ColorEnum.BLUE);
		for (int i = 0; i < loopCount; i++)
		{
			IntVector pos = runee.tileHelper.getRandomElementIndex();
			if(oneVec.equals(pos))
			{
				oneCount++;
			}
			else if(twoVec.equals(pos))
			{
				twoCount++;
			}
			else if(threeVec.equals(pos))
			{
				threeCount++;
			}
		}
		assertEquals(oneCount+twoCount+threeCount, loopCount);
		
		//though it is possible for us to roll 1000 times and be missing one of these, but honestly it should not happen in my lifetime
		assertTrue(""+loopCount+" random picks of 123 never hit 1",oneCount > 1);
		assertTrue(""+loopCount+" random picks of 123 never hit 2",twoCount > 1);
		assertTrue(""+loopCount+" random picks of 123 never hit 3",threeCount > 1);

		runee.tileHelper.clear();
	}
	
	@Test
	public void verify()
	{

		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		
		Tile tile = new Tile(runee);
		IntVector center = new IntVector(5,5);
		IntVector destination = new IntVector(1,1);
		tile.setCenter(center.toVector2());
		runee.getTileGrid()[1][1] = tile;
		assertTrue(tile.getIntCenter().equals(center));
		runee.tileHelper.syncPositions();
		assertTrue(tile.getIntCenter().equals(destination));	
	}
	
	@Test
	public void move()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector center = new IntVector(5,5);
		IntVector destination = new IntVector(1,1);
		IntVector invalid = new IntVector(-1,-1);
		runee.tileHelper.putTile(center, ColorEnum.RED);
		//this just makes a connector for the movetileparticle
		runee.tileHelper.putTile(new IntVector(5,4), ColorEnum.RED);
		runee.tileHelper.connectTile(5, 4);
		//test tile is in original position and destination is empty
		assertTrue(runee.tileHelper.getElement(center.getX(), center.getY()) != null);
		assertTrue(runee.tileHelper.getElement(destination.getX(), destination.getY()) == null);
		//move and test that original position is empty and destination is now occupied
		assertTrue(runee.tileHelper.moveElement(true, center, destination));
		assertTrue(runee.tileHelper.getElement(center.getX(), center.getY()) == null);
		assertTrue(runee.tileHelper.getElement(destination.getX(), destination.getY()) != null);
		//test invalid move ops
		assertFalse(runee.tileHelper.moveElement(true, null, destination));
		assertFalse(runee.tileHelper.moveElement(true, destination, null));
		assertFalse(runee.tileHelper.moveElement(true, null, destination));
		assertFalse(runee.tileHelper.moveElement(true, destination, null));
		assertFalse(runee.tileHelper.moveElement(true, invalid, destination));
		assertFalse(runee.tileHelper.moveElement(true, destination, invalid));
		//verify that tile is still in destination position
		assertTrue(runee.tileHelper.getElement(destination.getX(), destination.getY()) != null);
		//try moving empty cell to destination and ensure noOp
		assertFalse(runee.tileHelper.moveElement(false, center, destination));
		assertTrue(runee.tileHelper.getElement(destination.getX(), destination.getY()) != null);
		runee.tileHelper.clear();
		
	}

	@Test
	public void deletion()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector center = new IntVector(5,5);
		runee.tileHelper.putTile(center, ColorEnum.RED);
		//assert that the tile is now there
		GridElement myTile = runee.tileHelper.getElement(center.getX(), center.getY());
		assertTrue(myTile != null && !myTile.isMarkedForDelete());
		//mark for delete and assert it is marked but not deleted
		runee.tileHelper.markForDelete(center);
		myTile = runee.tileHelper.getElement(center.getX(), center.getY());
		assertTrue(myTile != null && myTile.isMarkedForDelete());
		//destroyImmediately and assert is is actually gone
		runee.tileHelper.markForDelete(center.getX(), center.getY());
		myTile = runee.tileHelper.getElement(center.getX(), center.getY());
		assertTrue(myTile == null);	
		
		//new tile, markfordelete and let updateWorld pick it up
		runee.tileHelper.putTile(center, ColorEnum.RED);
		myTile = runee.tileHelper.getElement(center.getX(), center.getY());
		assertTrue(myTile != null && !myTile.isMarkedForDelete());
		runee.tileHelper.markForDelete(center);
		runee.deleteAllMarked();
		myTile = runee.tileHelper.getElement(center.getX(), center.getY());
		assertTrue(myTile == null);	
		runee.tileHelper.clear();
	}
	
	@Test
	public void emptyAndCount()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		assertTrue(runee.tileHelper.isEmpty());
		runee.tileHelper.putTile(new IntVector(1,1), ColorEnum.RED);
		assertFalse(runee.tileHelper.isEmpty());
		assertEquals(runee.tileHelper.count(), 1);
		runee.tileHelper.clear();
		assertTrue(runee.tileHelper.isEmpty());
	}
	
	@Test 
	public void furthestColumn()
	{
		int pos = 5;
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(new IntVector(pos,1), ColorEnum.RED);
		assertEquals(runee.tileHelper.getFurthestColumn(), pos);
		runee.tileHelper.putTile(new IntVector(2*pos,1), ColorEnum.RED);
		assertEquals(runee.tileHelper.getFurthestColumn(), 2*pos);
		runee.tileHelper.markForDelete(2*pos, 1);
		assertEquals(runee.tileHelper.getFurthestColumn(), pos);
	}
	
	@Test
	public void nearest()
	{
		IntVector here = new IntVector(5,5);//right on
		IntVector dist1 = new IntVector(5,6);//one away
		IntVector dist2 = new IntVector(7,5);//two away
		IntVector dist3 = new IntVector(5,2);//three away
		
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		runee.tileHelper.putTile(dist3, ColorEnum.RED);
		assertEquals(dist3, runee.tileHelper.getNearestElement(here));
		runee.tileHelper.putTile(dist1, ColorEnum.RED);
		assertEquals(dist1, runee.tileHelper.getNearestElement(here));
		runee.tileHelper.putTile(dist2, ColorEnum.RED);
		assertEquals(dist1, runee.tileHelper.getNearestElement(here));
		runee.tileHelper.putTile(here, ColorEnum.RED);
		assertEquals(dist1, runee.tileHelper.getNearestElement(here));
		runee.tileHelper.markForDelete(here.getX(), here.getY());
		runee.tileHelper.markForDelete(dist1.getX(), dist1.getY());
		assertEquals(dist2, runee.tileHelper.getNearestElement(here));
		//assert invalid cases return null
		assertNull(runee.tileHelper.getNearestElement(null));
		assertNull(runee.tileHelper.getNearestElement(new IntVector(-1,-1)));
	}
	
	@Test
	public void tileValidConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(new IntVector(0,0),ColorEnum.RED, runee);//just dont crash
	}
	@Test
	public void tileNullIntVectorConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(null,ColorEnum.RED, runee);//just dont crash
	}
	@Test
	public void tileNullColorConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(new IntVector(0,0),null, runee);//just dont crash
	}

	@Test
	public void tileNullGameConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(new IntVector(0,0),ColorEnum.RED, null);//just dont crash
		InvalidationTest.assertAndRevertError();
	}	

	@Test
	public void tile2ValidConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(ColorEnum.RED, runee);//just dont crash
	}
	@Test
	public void tile2NullColorConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(null, runee);//just dont crash
	}
	@Test
	public void tile2NullGameConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(ColorEnum.RED, null);//just dont crash
		InvalidationTest.assertAndRevertError();
	}
	
	@Test
	public void tile3ValidConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(runee);//just dont crash
	}
	@Test
	public void tile3nullConstructor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(null);//just dont crash
		InvalidationTest.assertAndRevertError();
	}
	@Test
	public void tilemoveto()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector start = new IntVector(0,0);
		IntVector stop = new IntVector(1,1);
		Tile test = new Tile(start,ColorEnum.RED, runee);
		assertEquals(test.getIntCenter(),start);
		test.moveTo(false, stop);
		assertEquals(test.getIntCenter(),stop);
		test.moveTo(false, null);
		assertEquals(test.getIntCenter(),stop);
		
	}
	
	@Test 
	public void markDelete()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(new IntVector(0,0),ColorEnum.RED, runee);
		test.markForDelete();
		assertTrue(test.isMarkedForDelete());
	}
	
	@Test
	public void setTheColor()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(new IntVector(0,0),ColorEnum.RED, runee);
		assertEquals(test.getColorEnum(), ColorEnum.RED);
		test.setColorEnum(ColorEnum.BLUE);
		assertEquals(test.getColorEnum(), ColorEnum.BLUE);
		test.setColorEnum(null);
		assertEquals(test.getColorEnum(), ColorEnum.BLUE);
	}
	
	@Test
	public void connectors()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		Tile test = new Tile(new IntVector(0,0),ColorEnum.RED, runee);
		for(int i = 0; i < 8; i++)
		{
			test.activateConnector(i);
			assertTrue(test.isConnected(i));
			test.resetConnector(i);
			assertFalse(test.isConnected(i));
		}
	}
	
	@Test
	public void shortestPathGap()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector start = new IntVector(0,0);
		IntVector middle = new IntVector(0,1);
		IntVector end = new IntVector(0,2);
		runee.tileHelper.putTile(start, ColorEnum.RED);
		runee.tileHelper.putTile(end, ColorEnum.RED);
		LinkedList<IntVector> sp = runee.tileHelper.getShortestPath(start, end);
		assertEquals(sp.poll(), start);
		assertEquals(sp.poll(), middle);
		assertEquals(sp.poll(), middle);
		assertEquals(sp.poll(), end);
	}
	
	
	@Test
	public void shortestPath()
	{
		Game runee = RunTests.ct.getRunnee();
		runee.tileHelper.clear();
		IntVector start = new IntVector(0,0);
		IntVector middle = new IntVector(0,1);
		IntVector end = new IntVector(0,2);
		runee.tileHelper.putTile(start, ColorEnum.RED);
		runee.tileHelper.putTile(middle, ColorEnum.RED);
		runee.tileHelper.putTile(end, ColorEnum.RED);
		LinkedList<IntVector> sp = runee.tileHelper.getShortestPath(start, end);
		assertEquals(sp.poll(), start);
		assertEquals(sp.poll(), middle);
		assertEquals(sp.poll(), end);
	}
}
