package layers;

import gridElements.GridElement;
import gridElements.Tile;

import java.util.ArrayList;
import java.util.LinkedList;

import structures.IntVector;
import structures.IntVectorPathFinder;
import corrupted.Game;
import corrupted.ErrorHandler;

public class TileLogic extends LogicBase{

	/**
	 * initializes the layer with a size
	 * @author Brian Chau
	 * @param layM Game for communication with other layers
	 */
	public TileLogic(Game layM)
	{
		super(layM);
	}
	
	/**
	 * gets the TileGrid from the Game
	 * @return the grid that this Logic class executes on.
	 */
	@Override
	protected GridElement[][] getTheGrid()
	{
		return mGM.getTileGrid();
	}
	/**
	 * Puts a new tile onto the grid at the position of pos.
	 * If pos is null, the command is ignored.
	 * If col is null, the position in the grid is set to null.
	 * 
	 * @author Brian Chau
	 * @param pos coordinates to put the tile
	 * @param col color of the tile
	 * @return true if successful, false otherwise
	 */
	public boolean putTile(IntVector pos, GridElement.ColorEnum col)
	{
		if (pos == null) return false;
		
		Tile tile = null;
		if  (col != null){
			tile = new Tile(pos, col, mGM);
		}
		return putElement(pos,tile);
	}
	/**
	 * Puts a new tile onto the grid at the position of pos.
	 * If tile is null, the designated position will be set to null
	 * 
	 * @author Brian Chau
	 * @param pos position to put the tile
	 * @param tile tile to be put on the grid
	 * @return true if successful, false otherwise
	 */
	public boolean putTile(IntVector pos, Tile tile)
	{
		if (pos == null) return false;
		return putElement(pos,tile);
	}	
	
	/**
	 * Given an IntVector, returns a list contiguously colored tiles on the grid
	 * based on the tile at the position of the IntVector.
	 * if pos is null or out of bounds, an empty list is returned
	 * @author Brian Chau
	 * @param pos position of the tile shot by player
	 * @return list of IntVectors representing position of each color-contiguous tile. Empty if pos is invalid
	 */
	public ArrayList<IntVector> getContiguousTiles(IntVector pos)
	{
		ArrayList<IntVector> matches = new ArrayList<IntVector>();
		if (pos == null || !withinBounds(pos.getX(),pos.getY())) return matches;
		getMatches(pos, matches, null);
		
		return matches;
	}
	
	/**
	 * getMatches is a recursive method that gets all the 
	 * contiguous color-matching tiles (including the current) and stores them in the matches argument
	 * @author Brian Chau
	 * @param pos position of current tile to check.
	 * @param matches list of tiles already matched. By the end, this contains the list of all contiguous color matching tiles.
	 * @param col color we are matching.
	 */
	private void getMatches(IntVector pos, ArrayList<IntVector> matches, GridElement.ColorEnum col)
	{
		if (pos == null) return;
		if (matches == null)
		{
			matches = new ArrayList<IntVector>();
		}
		int x = pos.getX();
		int y = pos.getY();
		//check dimensions
		if (!withinBounds(x,y))
		{
			return;
		}
		Tile current = (Tile)this.getElement(x, y);
		if(current == null)
		{
			return;
		}
		if (col == null)
		{
			col = current.getColorEnum();
		}
		if(!current.getColorEnum().equals(col))
		{
			return;
		}
		if(matches.contains(pos))
		{
			return;
		}
		matches.add(pos);
		//get adjacent elements (including diagonals)
		for(int i = x-1; i <= x+1; i++)
		{
			for (int j = y-1; j <= y+1; j++)
			{
				getMatches(new IntVector(i,j),matches,col);
			}
		}
	}

	
	/**
	 * Change the Color of the tile located on IntVector.
	 * If pos is null or out of bounds, or if there is no tile at the position, the operation is ignored
	 * of the ColorEnum is null, the operation is also ignored
	 * @author Samuel Kim, Brian Chau
	 * @param pos the position to change the tile color
	 * @return true if the tile at the position changed its color
	 */
	public boolean changeTileColor(IntVector pos, GridElement.ColorEnum col){
		if (pos == null || !withinBounds(pos) || col == null) return false;
		
		Tile tempTile = (Tile) getElement(pos.getX(),pos.getY());
		
		if(tempTile == null) return false;
		
		tempTile.setColorEnum(col);	
		return true;
	}
	
	/**
	 * updates connector status for all tiles
	 * @author Brian Chau
	 */
	public void connectAllTiles()
	{
		try{
			for(int i=0;i < this.mGM.getWidth();i++){
				for(int j=0;j<this.mGM.getHeight();j++){
					if(getElement(i,j) != null){
						connectTileOneWay(i,j);
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
	}
	
	/**
	 * Update connectors for a tile at a given position as well as all adjacent tiles.
	 * This allows updating a single tile without hitting the whole grid.
	 * @author Brian Chau
	 * @param x x coordinate of Tile for connector update
	 * @param y y coordinate of Tile for connector update
	 */
	public void connectTile(int x,int y)
	{
		Tile centerTile = (Tile) this.getElement(x, y);
		if (centerTile == null) return;
		connectTileOneWay(x,y);

		IntVector[] orientations = new IntVector[]
		{
				new IntVector(x,y+1),
				new IntVector(x+1,y+1),
				new IntVector(x+1,y),
				new IntVector(x+1,y-1),
				new IntVector(x,y-1),
				new IntVector(x-1,y-1),
				new IntVector(x-1,y),
				new IntVector(x-1,y+1)
		};
		for(int i = 0; i < orientations.length; i++)
		{
			Tile orientedTile = (Tile) this.getElement(orientations[i].getX(), orientations[i].getY());
			if (orientedTile != null && orientedTile.getColorEnum() == centerTile.getColorEnum() && orientedTile.isVisible() && centerTile.isVisible())
			{
				connectTileOneWay(orientations[i].getX(), orientations[i].getY());
			}
		}
	}
	
	/**
	 * update connectors for a single Tile only
	 * @param x x coordinate of Tile for connector update
	 * @param y y coordinate of Tile for connector update
	 */
	private void connectTileOneWay(int x, int y)
	{
		Tile centerTile = (Tile) this.getElement(x, y);
		if(centerTile == null) return;
		IntVector[] orientations = new IntVector[]
		{
				new IntVector(x,y+1),
				new IntVector(x+1,y+1),
				new IntVector(x+1,y),
				new IntVector(x+1,y-1),
				new IntVector(x,y-1),
				new IntVector(x-1,y-1),
				new IntVector(x-1,y),
				new IntVector(x-1,y+1)
		};
		for(int i = 0; i < orientations.length; i++)
		{
			Tile orientedTile = null;
			try
			{
				orientedTile = (Tile) this.getElement(orientations[i].getX(), orientations[i].getY());
			}
			catch(ClassCastException e)
			{
				ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_TILE);
			}
			if (orientedTile != null && orientedTile.getColorEnum() == centerTile.getColorEnum())
			{
				if(orientedTile.isVisible() && centerTile.isVisible()){
					centerTile.activateConnector(i);
				}
			}
			else
			{
				if(centerTile != null){
					centerTile.resetConnector(i);
				}
			}
		}
	}
	
	/**
	 * get a ColorEnum of a Tile that is currently on the tileGrid
	 * @author Brian Chau
	 * @return color of an existing tile, random if the grid is empty
	 */
	public GridElement.ColorEnum getRandomExistingColor()
	{
		IntVector coord = this.getRandomElementIndex();
		if(coord == null)
		{
			return GridElement.getRandomColorEnum();
		}
		Tile randTile = (Tile)getElement(coord.getX(), coord.getY());
		return randTile.getColorEnum();
	}
	
	/**
	 * Finds the shortest path between two indexes on the following rules:
	 * travelling across an occupied space takes 1 move
	 * travelling across an unoccupied space (null) takes 2 moves
	 * 
	 * @param start index to start
	 * @param end index to finish
	 * @return Linked List of IntVectors with the shortest path taken. 
	 * The first IntVector is the start location. The last IntVector is the end location.
	 * null indexes are listed twice since it takes 2 turns to travel through them
	 */
	public LinkedList<IntVector> getShortestPath(IntVector start, IntVector end)
	{
		//initialize visited indexes. each index has a value representing the number of turns it takes to get through that index.
		// occupied tiles take one turn, gaps cost 2.
		// once a 
		int[][] visitedIndexes = new int[mGM.getWidth()][mGM.getHeight()];
		for (int a = 0; a < mGM.getWidth(); a++)
		{
			for (int b = 0; b < mGM.getHeight(); b++)
			{
				if(getElement(a,b) == null){
					visitedIndexes[a][b] = 2;
					continue;
				}
				visitedIndexes[a][b] = 1;
			}
		}

		LinkedList<IntVector> rtn = new LinkedList<IntVector>(); 
		//this will contain the path from start to finish
		//fill a grid outwards marking the distance from the start position
		LinkedList<IntVectorPathFinder> BFSQueue = new LinkedList<IntVectorPathFinder>();

		IntVectorPathFinder endPath = new IntVectorPathFinder(end.getX(), end.getY(), null);
		BFSQueue.add(new IntVectorPathFinder(start.getX(), start.getY(), null));
		visitedIndexes[start.getX()][start.getY()] = 0;
		//radiate out from the start
		IntVectorPathFinder current = null;
		while (!BFSQueue.isEmpty())
		{
			//dequeue the front
			current = BFSQueue.poll();
			//if it is what we are looking for, break
			if (current.equals(endPath))
			{
				break;
			}
			int x = current.getX();
			int y = current.getY();
			//if the current location requires another turn to pass, push a copy of myself back in
			if (visitedIndexes[x][y] > 0)
			{
				IntVectorPathFinder next = new IntVectorPathFinder(x,y,current);
				BFSQueue.add(next);
				visitedIndexes[x][y]--;
				continue; // we waited a turn here so we dont get to check adjacent tiles;
			}
			//otherwise add all adjacent cells to the queue 
			
			int[] i = new int[]{x,x-1,x+1};
			for(int j = y-1; j <= y+1; j++)
			{
				for(int iProxy = 0; iProxy < 3; iProxy++)
				{
					if(this.withinBounds(i[iProxy], j) && visitedIndexes[i[iProxy]][j] > 0)//&& this.getElement(i, j[jProxy]) != null)
					{
						IntVectorPathFinder next = new IntVectorPathFinder(i[iProxy],j,current);
						if(!BFSQueue.contains(next))
						{
							BFSQueue.add(next);
							visitedIndexes[i[iProxy]][j]--;
						}
					}
				}
			}
		}
		//by now if we could path to the end point, we would have done so.
		if(!current.equals(endPath))
		{
			//if we didnt find the end point, return null
			return null;
		}
		//otherwise build the path
		while(current != null){
			rtn.addFirst(current);
			current = current.previous;
		}
		return rtn;
	}
}
