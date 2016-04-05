package gridElements;

import Engine.*;
import gridElements.GridElement.ColorEnum;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import structures.*;
import corrupted.Game;

public class Player extends GridElement {
	
	private GridElement reticlePart;
	private GridElement overdraw;
	
	//if true, the targeting laser will animate until the end of the screen if unobstructed
	//otherwise it will stop at the last non-null column
	private boolean infiniteLaser = true;
	
	
	/**
	 * map to link color identifiers with textures
	 * @author Brian Chau
	 */
	protected static final EnumMap<ColorEnum,BufferedImage> textureList;
	static 
	{
		float alpha = .75f;
		textureList = makeTextureMap("LaserSprite.png", alpha);

//		BufferedImage tempReticle = BaseCode.resources.loadImage("LaserSprite.png");
//		textureList = new EnumMap<ColorEnum,BufferedImage>(ColorEnum.class);
//		textureList.put(ColorEnum.RED, ImageManip.tintedImage(tempReticle, Color.red, alpha));
//		textureList.put(ColorEnum.GREEN, ImageManip.tintedImage(tempReticle, Color.green, alpha));
//		textureList.put(ColorEnum.BLUE, ImageManip.tintedImage(tempReticle, Color.blue, alpha));
//		textureList.put(ColorEnum.CYAN, ImageManip.tintedImage(tempReticle, Color.cyan, alpha));
//		textureList.put(ColorEnum.MAGENTA, ImageManip.tintedImage(tempReticle, Color.magenta, alpha));
//		textureList.put(ColorEnum.YELLOW, ImageManip.tintedImage(tempReticle, Color.yellow, alpha));
	}
	
	/**
	 * constructs a new Player element
	 * @author Brian Chau
	 * @param pos position to place the player
	 * @param col the color of the next tile to shoot
	 */
	public Player(IntVector pos, ColorEnum col, Game gm) 
	{
		super(pos,col,gm);
		setCenter( 0, getCenterY()); // force player to left column
		setSize(229f/72f,957f/72f);
		setImage("Cannon.png");
		overdraw = new GridElement(getIntCenter(),mColor, mGM);
		overdraw.setAutoDrawTo(false);
		overdraw.setCenter(getCenter());
		overdraw.setSize(getSize());
		overdraw.setImage("Cannon2.png");
		//size = new Vector2(gm.getWidth()*2, .2f );
		
		if(gm.getWidth() > 0)
		{
			initializeReticleParts();
		}
		setColorEnum(col);
		moveTo(false, pos);
	}
	
	private void initializeReticleParts()
	{
		reticlePart = new GridElement(getIntCenter(), mColor, mGM);
		reticlePart.setSpriteSheet(textureList.get(mColor), PIXEL_SIZE, PIXEL_SIZE, 22, 1);
		reticlePart.setUsingSpriteSheet(true);
		//stretch it slightly so we can ensure seamless connection between parts
		reticlePart.setSize(1.001f,1f);
	}
	
	/**
	 * Sets the targeting laser mode (this affects drawing only).
	 * InfiniteLaserMode true: laser will draw to the end of the screen if there is a clear path.
	 * InfiniteLaserMode false: laser will stop on the last column that has tiles on it
	 * @param mode if true, InfiniteLasermode will be on.
	 */
	public void setInfiniteLaserMode(boolean mode)
	{
		infiniteLaser = mode;
	}
	
	/**
	 * This method draws the player cannon to the screen
	 */
	public void draw()
	{
		if(this.isVisible()){
			super.draw();
			IntVector intCenter = getIntCenter();
			reticlePart.setAnimationPauseStatus(true);
			int maxColumn = mGM.getWidth();
			if(!infiniteLaser)
			{
				maxColumn = mGM.tileHelper.getFurthestColumn();
			}
			for(int i = 0; i <= maxColumn; i++)
			{
				GridElement currentTile = mGM.tileHelper.getElement(i, intCenter.getY());
				if(currentTile == null)
				{
					reticlePart.setCenterX(i);
					reticlePart.draw();
				}
				else
				{
					break;				
				}
				
			}
			reticlePart.setCenterX(getCenterX());
			reticlePart.setAnimationPauseStatus(false);
			reticlePart.draw();
			overdraw.draw();
		}
	}

	public void moveTo(boolean animate, IntVector pos)
	{
		super.moveTo(animate, pos);
		//since constructor calls moveto, we have to check null and move the shipIcon later
		reticlePart.moveTo(animate, pos);
		overdraw.moveTo(animate,pos);
	}
	
	/**
	 * generates a tile with a color depending on the next color to shoot
	 * the next color to shoot is then randomized
	 * @author Brian Chau
	 * @return the tile of the color to shoot (this is centered on the player's position)
	 */
	public Tile generateTile()
	{
		
		Tile tile = new Tile(new IntVector(getCenter()), mColor, mGM);
		setColorEnum(mGM.tileHelper.getRandomExistingColor());
		return tile;
	}
	

	@Override
	public void setColorEnum(ColorEnum colorType){
	 super.setColorEnum(colorType);
	 setLaserTexture();
	}
	
	private void setLaserTexture()
	{
		BufferedImage tex;
		try
		{
			tex = textureList.get(mColor);
		}
		catch(Exception e)
		{
			tex = textureList.get(ColorEnum.RED);
		}
		if(tex == null)
		{
			tex = textureList.get(ColorEnum.RED);
		}
		reticlePart.setImage(tex);
	}
}
