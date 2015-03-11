package gridElements;
import gridElements.GridElement.ColorEnum;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;

import Engine.BaseCode;
import corrupted.ErrorHandler;
import corrupted.Game;
import structures.*;

public class ChainVirus extends Virus {
	
	/**
	 * map to link color identifiers with textures
	 * @author Brian Chau
	 */
	protected static final EnumMap<ColorEnum,BufferedImage> textureList;
	static{
		textureList = makeLayeredTextureMap("ChainVirus.png","ChainBacking.png", .5f);
	}
	
	private IntVector targetLocation;
	public boolean isHead = true;
	private boolean hasCorrupted = false;
	public TurnTimer idleTime;
	public static int idleTicks = 2;
	
	/**
	 * Instantiates a ChainVirus with a given color
	 * @author Brian Chau
	 * @param col color of the ChainVirus
	 * @param gm reference to Game
	 */
	public ChainVirus(ColorEnum col, Game gm){
		
		this(null, col, gm);
	}
	
	/**
	 * Instantiates a ChainVirus with a given color and position
	 * @author Brian Chau
	 * @param pos position of the ChainVirus
	 * @param col color of the ChainVirus
	 * @param gm reference to Game
	 */
	public ChainVirus(IntVector pos,ColorEnum col, Game gm){
		super(pos, col, gm);
		idleTime = new TurnTimer(idleTicks);
		if(mGM == null){
			targetLocation = new IntVector(0,0); //in release, null gm should force exit and this should not be hit see ErrorHandler.java
		}
		else{
		targetLocation = new IntVector(0,mGM.getHeight()/2);
		}
		this.setSpriteSheet(textureList.get(getColorEnum()), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 10, 1);
		this.setUsingSpriteSheet(true);
	}
	
	/**
	 * adds corruption on the ChanVirus's current location if it is the head
	 * @author Brian Chau
	 */
	public void TurnBasedUpdate()
	{
		//if(newlySpreaded) return;
		
		if(!isHead && !hasCorrupted){
			//ArrayList<IntVector> corruptions = new ArrayList<IntVector>();
			//corruptions.add(new IntVector(center));
			mGM.corruptionHelper.addCorruption(this.getIntCenter());
			hasCorrupted = true;
		}
	}
	
	
	/**
	 * if this ChainVirus is the head, it becomes part of the body and
	 * creates a new head closer to the center of the player's side of the grid
	 * @author Brian Chau
	 */
	@Override
	public void spread()
	{
		if(isHead)
		{
			if(!idleTime.tick())
			{
				return;
			}
			//find the direction we want to spread
			IntVector direction = new IntVector(-1,0);
			IntVector origLoc = new IntVector(getCenter());
			IntVector preDirection = origLoc.subtract(targetLocation);
			
			//if horizontal position isnt the center, use some probability to move it
			if(preDirection.getY() != 0)
			{
				int probTotal = mGM.getHeight() + mGM.getWidth();
				int probChance = mGM.getHeight();
				
				int probTry = rand.nextInt(probTotal);
				//use probability to decide if we move horizontally
				if(probTry < probChance)
				{
					//if we are moving vertically, move towards the center.
					if(preDirection.getY() > 0)
					{
						direction = new IntVector(0,-1);
					}
					else
					{
						direction = new IntVector(0,1);
					}
				}
			}
			IntVector spreadPosition = direction.add(new IntVector(getCenter()));
			//do spreading
			mGM.tileHelper.putTile(spreadPosition, mColor);
			
			ChainVirus newHead = new ChainVirus(mColor, mGM);
			
			//we are making a new virus and moving it to the new location so we do this fake moving
			newHead.moveTo(false, origLoc);
			newHead.moveTo(true, spreadPosition);
			
			mGM.virusHelper.putVirus(spreadPosition,newHead);
			

			isHead = false;
			
			newHead.consumeContiguous();
		}
	}
	/**
	 * Spawns a new ChainVirus on the right side of the grid.
	 * if there are no tiles on the rightmost column, move inward until tiles exist on the column
	 * @author Brian Chau
	 */
	@Override
	public void spawn(){
		GridElement[][] grid = mGM.getTileGrid();
		int x = mGM.tileHelper.getFurthestColumn();
		int y = rand.nextInt(mGM.getHeight());
		
		IntVector mlocation = new IntVector(x,y);
		spawnAtLocation(mlocation);
		return;
		//spawn on the farthest away column
		//if a column does not have any tiles, try the one next closer.
//		while (x >= 0)
//		{
//			for (int i = 0; i < mGM.getHeight(); i++)
//			{
//				if(grid[x][i] != null)
//				{
//					
//				}
//			}
//			//if all of this column is null, check next column
//			x--;
//		}
	}

	/**
	 * Spawns a ChainVirus at a given location
	 * @author Brian Chau
	 * @param pos position to spawn
	 */
	@Override
	public void spawnAtLocation(IntVector pos) {
		if(pos == null)
		{
			return;
		}
		
		ColorEnum newColor = GridElement.getRandomColorEnum();

		ChainVirus mChainVirus = new ChainVirus(newColor, mGM);
		
		
		mChainVirus.moveTo(false, pos);
		mGM.virusHelper.putVirus(pos, mChainVirus);
		mGM.tileHelper.putTile(pos, newColor);
		
	}
	
	/**
	 * spread the ChainVirus to all contiguous tiles of the same color as the ChainVirus's Color
	 * @author Brian Chau
	 */
	public void consumeContiguous()
	{
		IntVector lowestPoint = null;
		IntVector pos = getIntCenter();
		ArrayList<IntVector> matches = mGM.tileHelper.getContiguousTiles(pos);
		for(int i = 0; i < matches.size(); i++)
		{
			IntVector currentPos = matches.get(i);
			if(lowestPoint == null || currentPos.getX() < lowestPoint.getX())
			{
				lowestPoint = currentPos;
			}
			int x = matches.get(i).getX();
			int y = matches.get(i).getY();
			//for each contiguous Tile, 
			//if no virus, spawn chain virus and make it not head
			GridElement currentVirus = mGM.virusHelper.getElement(x,y);
			if(currentVirus == null)
			{
				ChainVirus mChainVirus = new ChainVirus(pos, mColor, mGM);
				mChainVirus.moveTo(true, currentPos);
				mChainVirus.isHead = false;
				mGM.virusHelper.putVirus(currentPos, mChainVirus);
			}
			//else if chain virus change color
			else if(currentVirus.getClass() == ChainVirus.class)
			{
				ChainVirus chainv = null;
				try
				{
					chainv = (ChainVirus)currentVirus;
				}
				catch(ClassCastException e)
				{
					ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_CHAINVIRUS);
				}
				chainv.setColorEnum(mColor);
				chainv.isHead = false;
				mGM.tileHelper.getElement(x, y).setColorEnum(mColor);
			}
		}
		if (lowestPoint != null)
		{
			GridElement lowestElement = mGM.virusHelper.getElement(lowestPoint.getX(), lowestPoint.getY());
			if(lowestElement != null && lowestElement.getClass() == ChainVirus.class)
			{
				ChainVirus lowestChain = null;
				try
				{
					lowestChain = (ChainVirus)lowestElement;
				}
				catch(ClassCastException e)
				{
					ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_CHAINVIRUS);
				}
				lowestChain.isHead = true;
			}
		}
	}
}
