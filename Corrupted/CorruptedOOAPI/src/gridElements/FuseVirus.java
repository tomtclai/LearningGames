package gridElements;

import gridElements.GridElement.ColorEnum;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;

import Engine.BaseCode;
import corrupted.ErrorHandler;
import corrupted.Game;
import structures.*;


public class FuseVirus extends Virus {

	private boolean mIsBot=true;
	
	private FuseVirus other;
	
	private TurnTimer mTurnTimer = new TurnTimer(4);
	
	private FuseVirusTypes mType;
	
	public FuseVirusStates state = FuseVirusStates.Active;
	
	private static final EnumMap<ColorEnum,BufferedImage> topTextureList;
	static{
		topTextureList = makeLayeredTextureMap("FuseRight.png","VirusBacking.png", .5f);
	}
	private static final EnumMap<ColorEnum,BufferedImage> botTextureList;
	static{
		botTextureList = makeLayeredTextureMap("FuseLeft.png","VirusBacking.png", .5f);
	}
	private static final EnumMap<ColorEnum,BufferedImage> topFusedTextureList;
	static{
		topFusedTextureList = makeLayeredTextureMap("FusedRight.png","VirusBacking.png", .5f);
	}
	private static final EnumMap<ColorEnum,BufferedImage> botFusedTextureList;
	static{
		botFusedTextureList = makeLayeredTextureMap("FusedLeft.png","VirusBacking.png", .5f);
	}
	
	
	
	public enum FuseVirusTypes{
		BOT,
		TOP,
		BOTFUSED,
		TOPFUSED
	}
	
	public enum FuseVirusStates{
		Active,
		Deactive,
		Fused,
	}
	
	/**
	 * instantiates a FuseVirus
	 * @author Samuel Kim
	 * @param col color of the virus
	 * @param gm reference to Game
	 * @param fuseType whether this is the bottom or top side
	 */
	public FuseVirus(gridElements.GridElement.ColorEnum col , Game gm, FuseVirusTypes fuseType){
		super(col, gm);
		if (fuseType == null) fuseType = FuseVirusTypes.BOT;
		setType(fuseType);
				
	}
	/**
	 * instantiates a FuseVirus with a given horizontal position
	 * @author Samuel Kim
	 * @param line horizontal position to create the FuseVirus
	 * @param col color of the virus
	 * @param gm reference to Game
	 * @param fuseType whether this is the bottom or top side
	 */
	public FuseVirus(int line, gridElements.GridElement.ColorEnum col , Game gm, FuseVirusTypes fuseType){
		this(col, gm, fuseType);
		
		this.setCenterX(line);
	}
	
	/** 
	 * returns true if this is the bottom FuseVirus of a pair
	 * @author Samuel Kim
	 * @return true if this is the bottom
	 */
	public boolean isBot(){
		return (mType == FuseVirusTypes.BOT);
	}
	
	/**
	 * sets the type of the fuseVirus (this determines behavior and texture)
	 * @param _Type the type to be set
	 */
	public void setType(FuseVirusTypes _Type){
		mType = _Type;
		setTexture();
	}
	
	/**
	 * sets the correct texture based on type and color.
	 */
	private void setTexture()
	{
		switch (mType){
		case BOT:
			this.setSpriteSheet(botTextureList.get(mColor), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 10, 1);
			break;
		case TOP:
			this.setSpriteSheet(topTextureList.get(mColor), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 10, 1);
			break;
		case BOTFUSED:
			this.setSpriteSheet(botFusedTextureList.get(mColor), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 10, 1);
			break;
		case TOPFUSED:
			this.setSpriteSheet(topFusedTextureList.get(mColor), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 10, 1);
			break;
		default:
			break;
			
		}
		this.setUsingSpriteSheet(true);
	}
	
	/**
	 * This method will return true if this virus can advance.
	 * if it's an deactivated virus, after default(5) turns, 
	 * it will become activated. 
	 * @author Samuel Kim
	 * @return Whether it can advance or not
	 */
	public boolean Advance(){
		
		if(state == FuseVirusStates.Deactive){
			if(mTurnTimer.tick()){
				state = FuseVirusStates.Active;
			}
			return false;
		}else if(state == FuseVirusStates.Active){
			return true;
		}else
			return false;
	}
	
	@Override

	/**
	 * initiates FuseVirus movement
	 * if this FuseVirus is the BOT type, it moves upwards towards the TOP counterpart along existing tiles
	 * if this FuseVirus is the TOP type, it moves downwards towards the BOT counterpart along existing tiles
	 * if there is a gap in the tiles between the pair, a new tile of the FuseViruses Color may be spawned in lieu of movement
	 * if the TOP and BOT counterparts collide, trigger blowup()
	 * if this FuseViris is neither BOT nor TOP, do nothing
	 * @author Samuel Kim
	 */
	public void spread()
	{
		// if the virus cell is not eligible for spreading, quits.
		if(!Advance() || other == null) return;
		
		//non-heads do not update
		if(mType != FuseVirusTypes.BOT && mType != FuseVirusTypes.TOP ) return;
		//-----------------------------------
		
		//System.out.print(this.mType.toString() + " FuseVirus Spread()\n");
		
		//get the best path to the other side
		LinkedList<IntVector> path = mGM.tileHelper.getShortestPath(this.getIntCenter(), other.getIntCenter());
		
		//pop off the current (we check multiple times because the algorithm can return the same place multiple times based on gaps)
		while(path.peek().equals(this.getIntCenter()))
		{
			path.poll();
		}
		//this should be the first position that is not the starting position
		IntVector newPos = path.poll(); //this.getIntCenter();
		//newPos.setX(newPos.getX()+xDiff);
		
		//if the target position contains another fuse virus, we blow up
		GridElement otherChecker = mGM.virusHelper.getElement(newPos.getX(), newPos.getY());
		if(otherChecker != null && otherChecker.getClass().equals(this.getClass()))
		{
			FuseVirus tempFV = null;
			try
			{
				tempFV = (FuseVirus) otherChecker;
			}
			catch(ClassCastException e)
			{
				ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_VIRUS);
			}
			this.state = FuseVirusStates.Deactive;
			tempFV.state = FuseVirusStates.Deactive;
			
			blowup();
			return;
		}
		//if the target position does not have a tile, we spawn a tile instead of move
		GridElement targetUnderTile = mGM.tileHelper.getElement(newPos.getX(), newPos.getY());
		if(targetUnderTile == null)
		{
			mGM.tileHelper.putTile(newPos, mColor);
			return;
		}
		
		//if we didnt have to spawn a bridge tile, we spread instead.
		FuseVirus newFuseVirus;
		
		switch(mType){
		case BOT:
			newFuseVirus=new FuseVirus(this.mColor,this.mGM,FuseVirusTypes.BOTFUSED);
			break;
		case TOP:
			newFuseVirus=new FuseVirus(this.mColor,this.mGM,FuseVirusTypes.TOPFUSED);
			break;
			
		default:
			//we should never get here
			newFuseVirus=new FuseVirus(this.mColor,this.mGM,FuseVirusTypes.TOP);
			break;
		}
		mGM.tileHelper.putTile(newPos, mColor);

		IntVector newFuseCenter =this.getIntCenter();
		newFuseVirus.moveTo(false, newFuseCenter);
		this.moveTo(true, newPos);
		this.newlySpreaded = true;
		mGM.virusHelper.putVirus(newPos, this);
		
		newFuseVirus.newlySpreaded = true;
		newFuseVirus.state = FuseVirusStates.Fused;
		mGM.virusHelper.putVirus(newFuseCenter, newFuseVirus);

		
	}
	
	
	/**
	 * This method will remove both right and left head and fused tiles,
	 * destroy all FuseViruses on screen, delete tiles under them, and add corruption
	 * as well as put corruption in their place
	 * 
	 * @author Samuel Kim
	 */
	public void blowup(){
		ArrayList<IntVector> deleteList = new ArrayList<IntVector>();
		
		for( int i=0 ; i < mGM.getWidth() ;i++){
			for( int j = mGM.getHeight()-1 ; j >= 0; j--) {
				Virus tempV = null;
				try{
					tempV =(Virus)(mGM.virusHelper.getElement(i, j));
				}
				catch(ClassCastException e)
				{
					ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_VIRUS);
				}
				if(tempV != null ){
					if(tempV.getClass().equals(FuseVirus.class)){
						deleteList.add(new IntVector(i,j));
					}
				}
			}
		}
		
		mGM.virusHelper.markForDelete(deleteList);
		mGM.tileHelper.markForDelete(deleteList);
		mGM.corruptionHelper.addCorruptions(deleteList);
		
		
	}
	

	/**
	 * nothing happens in the FuseVirus turnBasedUpdate
	 * everything happens in spread
	 */
	@Override
	public void TurnBasedUpdate() {

	}		
	
	/**
	 * Deactivate (but do not mark for delete) this FuseVirus
	 * if the counterpart is also deactivated, mark for delete both BOT and TOP
	 * as well as any contiguous FUSED types
	 * @author Samuel Kim
	 */
	@Override
	public void markForDelete(){
		
		switch(this.mType){
		case BOT:
		case TOP:
			if(other!=null){
				if(other.state == FuseVirusStates.Deactive){
					this.toDelete = true;
					other.toDelete = true;
					//make sure that you set the other to null as well in the grid
				}else{ 
					this.state = FuseVirusStates.Deactive;
					mGM.tileHelper.putTile(this.getIntCenter(), this.mColor);
				}
			}else{
				this.toDelete = true;
			}
			break;
		default:
			this.toDelete = true;
		}
	}
	
	/**
	 * This method picks a horizontal position based on a random tile and spawns
	 * a FuseVirus at the top and bottom of the grid at that horizontal position
	 * the command is ignored if there are no tiles 
	 * @author Brian Chau
	 */
	@Override
	public void spawn(){
		
		IntVector loc = null;
		loc = mGM.tileHelper.getRandomElementIndex();
		if (loc == null) return;
		spawnAtLocation(loc);
		
	}
	
	/**
	 * Spawn a pair of fuseViruses at a horizontal position based on a given position
	 * the BOT side spawns at the bottom of the grid and the TOP side spawns at the top
	 * @author Samuel Kim
	 * @param loc location to extract horizontal position for spawning
	 */
	@Override
	public void spawnAtLocation(IntVector loc) {

		if(loc == null)
		{
			return;
		}
		int pos=0;
		pos = loc.getX();
		
		//now spawn a virus here 
		ColorEnum newColor = GridElement.getRandomColorEnum();
		
		FuseVirus newTopFuse = new FuseVirus(pos, newColor, mGM,FuseVirusTypes.TOP);
		FuseVirus newBotFuse = new FuseVirus(pos, newColor, mGM,FuseVirusTypes.BOT);
		newTopFuse.other = newBotFuse;
		newBotFuse.other = newTopFuse;
		
		IntVector botPos = new IntVector(pos,0);
		IntVector topPos = new IntVector(pos, mGM.getHeight()-1);
		
		newBotFuse.moveTo(false, botPos);
		newTopFuse.moveTo(false, topPos);
		
		newBotFuse.setColorEnum(newColor);
		newTopFuse.setColorEnum(newColor);
		
		mGM.virusHelper.putVirus(botPos,newBotFuse);
		mGM.virusHelper.putVirus(topPos,newTopFuse);
		

		mGM.tileHelper.putTile(newBotFuse.getIntCenter(), newColor);
		mGM.tileHelper.putTile(newTopFuse.getIntCenter(), newColor);		
	}
}
