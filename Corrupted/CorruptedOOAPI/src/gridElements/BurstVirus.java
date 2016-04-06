package gridElements;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;

import structures.IntVector;
import structures.TurnTimer;
import corrupted.Game;



public class BurstVirus extends Virus {
	private static final int NUM_TEX = 7;
	/**
	 * map to link color identifiers with textures
	 * @author Brian Chau
	 */
	protected static final ArrayList<EnumMap<ColorEnum,BufferedImage>> textureList;
	static{
		textureList = new ArrayList<EnumMap<ColorEnum,BufferedImage>>();
		for (int i = 0; i < NUM_TEX; i++)
		{
			textureList.add(makeLayeredTextureMap("BurstVirus"+i+".png","VirusBacking.png", .75f));
		}
	}
	
	protected TurnTimer mBurstTimer;

	/**
	 * Instantiates a new BurstVirus
	 * @author Brian Chau
	 * @param col color of the BurstVirus
	 * @param gm reference to Game
	 */
	public BurstVirus(ColorEnum col, Game gm){
		this(null, col, gm);
	}
	
	/**
	 * Instantiates a new BurstVirus at a given postition
	 * @author Brian Chau
	 * @param pos position of the BurstVirus
	 * @param col color of the BurstVirus
	 * @param gm reference to Game
	 */
	public BurstVirus(IntVector pos,ColorEnum col, Game gm){
		super(pos,col,gm);
		mBurstTimer = new TurnTimer(NUM_TEX-1);

		this.setSpriteSheet(textureList.get(mBurstTimer.howLong()).get(getColorEnum()), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 10, 1);
		this.setUsingSpriteSheet(true);
		
	}

	/**
	 * Destroys tiles in a (square) radius from the BurstVirus's current position
	 * @author Samuel Kim
	 * @param radius range of destruction (no operation if radius is negative)
	 * @return true if radius is non-negative
	 */
	private boolean Burst(int radius){
		if(radius<0) return false;
		
		
		ArrayList<IntVector> tempDelCoords = new ArrayList<IntVector>(); 
		
		int x = (int)this.getCenterX();
		int y = (int)this.getCenterY();
		
		for(int i = -radius; i<radius+1 ;i++){
			for(int j = -radius; j<radius+1 ;j++){
				
				IntVector tempIV = new IntVector( x + i, y + j );
				
				tempDelCoords.add(tempIV);
				
			}
		}
		
		mGM.tileHelper.markForDelete(tempDelCoords);
		mGM.virusHelper.markForDelete(new IntVector(x,y));
		mGM.corruptionHelper.addCorruptions(tempDelCoords);
		
		return true;
	}
	
	/**
	 * If not newly created, ticks the timer. If timer runs out, Burst() is triggered\
	 * @author Samuel Kim
	 */
	@Override
	public void TurnBasedUpdate(){
		
		if(this.newlySpreaded){
			return;
		}
		
		if(incrementBurstTimer()){
			Burst(1);
		};		
	}
	
	/**
	 * increments the timer
	 * @return true if the timer has triggered
	 */
	private boolean incrementBurstTimer()
	{
		boolean rtn = mBurstTimer.tick();
		setImage(textureList.get(mBurstTimer.howLong()).get(mColor));
		return rtn;
	}

	/**
	 * BurstVirus does not spread
	 */
	public void spread()
	{
	}
	
	
	/**
	 * Spawns a BurstVirus in a 2x2 cluster on a random tile
	 * @author Brian Chau
	 */
	@Override
	public void spawn(){
		IntVector loc = null;
		//dont let it be on the edge because we need to fit a block there
		
		//int counter = 1000; //try 1000 times and we should be confident that 
//		while ((loc == null || loc.getX() >= mGM.getWidth() - 1 || loc.getY() >= mGM.getHeight() - 1 ))
//		{//shouldnt need to loop. nudging logic should guarantee a valid position
			loc = mGM.tileHelper.getRandomElementIndex();
			if(loc == null)
			{
				return;	//if there are no tiles left, do nothing.
			}
//			//nudge it in. (it should be handled in )
//			if(loc.getX() >= mGM.getWidth() - 1)
//			{
//				loc.setX(loc.getX()-1);
//			}
//			if(loc.getY() >= mGM.getHeight() - 1)
//			{
//				loc.setY(loc.getY()-1);
//			}
			
//			counter--;
//			if (counter <= 0) {return;} //if we cant find a valid place after 10 shots, give up
//		}
		//now spawn a virus here 
		spawnAtLocation(loc);
	}

	
	/**
	 * Spawns a burstVirus at a given location
	 * @param pos positiont to spawn
	 */
	@Override
	public void spawnAtLocation(IntVector pos) {
		if(pos == null)
		{
			return;
		}
		//nudge it in
		if(pos.getX() > mGM.getWidth() - 2)
			pos.setX(mGM.getWidth() - 2);
		if(pos.getY() > mGM.getHeight() - 2)
			pos.setY(mGM.getHeight() - 2);
		
		
		ColorEnum newColor = GridElement.getRandomColorEnum();

		for(int i=0;i<2;i++){
			for(int j=0;j<2;j++){
				
				BurstVirus mBurstVirus = new BurstVirus(newColor, mGM);
				
				IntVector mlocation= new IntVector(pos.getX()+i,pos.getY()+j);
				
				mBurstVirus.moveTo(false, mlocation);
				mGM.virusHelper.putVirus(mlocation, mBurstVirus);
				mGM.tileHelper.putTile(mlocation, newColor);
				mBurstVirus.newlySpreaded = false;
				
			}
		}
	}
	
	/**
	 * moves the BurstVirus to a new location
	 * @author Brian Chau
	 * @param animate will animate the movement if true
	 * @param pos location to move
	 */
	@Override
	public void moveTo(boolean animate, IntVector pos)
	{
		super.moveTo(animate, pos);
	}

	/**
	 * draws both the BurstVirus and the timer text
	 */
	@Override public void draw()
	{
		super.draw();
	}
}
