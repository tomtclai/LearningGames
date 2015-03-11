package gridElements;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import particles.DestroyedParticle;
import particles.MoveParticle;
import structures.ImageManip;
import structures.IntVector;
import Engine.BaseCode;
import Engine.GameObject;
import Engine.Vector2;
import corrupted.Game;
import corrupted.ErrorHandler;
public class GridElement extends GameObject {
	
	public static final int PIXEL_SIZE = 72;
	protected boolean toDelete = false;
	
	public static Random rand= new Random();
	
	/**
	 * enum listing the supported colors
	 * @author Brian Chau
	 */
	protected ColorEnum mColor;  //Color of this GridElement
	
	public Game mGM;
	
//	//TODO:minimap variables kept in case we want to put minimap back in
//	protected Vector2 minimapCenter = null;
//	protected static Vector2 minimapCenterOffset = null;
//	protected static final float minimapScaleFactor = .25f;
//	protected static final Vector2 minimapSize = new Vector2(minimapScaleFactor, minimapScaleFactor);
	
	
	public enum ColorEnum {RED,GREEN,BLUE,CYAN,MAGENTA,YELLOW};
	
	/**
	 * indexed color list to allow random picking
	 * @author Brian Chau
	 */
	protected static final ArrayList<ColorEnum> indexedColorList;
	static
	{
		indexedColorList = new ArrayList<ColorEnum>();
		indexedColorList.add(ColorEnum.RED);
		indexedColorList.add(ColorEnum.GREEN);
		indexedColorList.add(ColorEnum.BLUE);
		indexedColorList.add(ColorEnum.CYAN);
		indexedColorList.add(ColorEnum.MAGENTA);
		indexedColorList.add(ColorEnum.YELLOW);
	}
	
	/**
	 * map to link color identifiers with actual colors
	 * @author Brian Chau
	 */
	public static final EnumMap<ColorEnum,Color> colorMap;
	static 
	{
		colorMap = new EnumMap<ColorEnum,Color>(ColorEnum.class);
		colorMap.put(ColorEnum.RED, Color.RED);
		colorMap.put(ColorEnum.GREEN, Color.GREEN);
		colorMap.put(ColorEnum.BLUE, Color.BLUE);
		colorMap.put(ColorEnum.CYAN, Color.CYAN);
		colorMap.put(ColorEnum.MAGENTA, Color.MAGENTA);
		colorMap.put(ColorEnum.YELLOW, Color.YELLOW);
	}
	
	/**
	 * Helper method to generate a set of tinted layered texture maps.
	 * The foreground texture is tinted and layered above an untinted background texture
	 * @author Brian Chau
	 * @param foreground filename of the tinted foreground
	 * @param background filename of the untinted background
	 * @param tintLevel tint intensity
	 * @return a Map that links ColorEnums to their respective textures
	 */
	protected static EnumMap<ColorEnum,BufferedImage> makeLayeredTextureMap(String foreground, String background, float tintLevel)
	{
		EnumMap<ColorEnum,BufferedImage> textureList = new EnumMap<ColorEnum,BufferedImage>(ColorEnum.class);
		BufferedImage base = BaseCode.resources.loadImage(background);
		BufferedImage tint = BaseCode.resources.loadImage(foreground);
		for(Map.Entry<ColorEnum, Color> entry : colorMap.entrySet())
		{
			textureList.put(entry.getKey(), ImageManip.overlay(base, ImageManip.tintedImage(tint, entry.getValue(), tintLevel)));
		}
		
//		BufferedImage tempRed = ImageManip.overlay(base, ImageManip.tintedImage(tint, Color.red, tintLevel));
//		BufferedImage tempGreen = ImageManip.overlay(base, ImageManip.tintedImage(tint, Color.green,tintLevel));
//		BufferedImage tempBlue = ImageManip.overlay(base, ImageManip.tintedImage(tint, Color.blue, tintLevel));
//		BufferedImage tempCyan = ImageManip.overlay(base, ImageManip.tintedImage(tint, Color.cyan, tintLevel));
//		BufferedImage tempMagenta = ImageManip.overlay(base, ImageManip.tintedImage(tint, Color.magenta, tintLevel));
//		BufferedImage tempYellow = ImageManip.overlay(base, ImageManip.tintedImage(tint, Color.yellow, tintLevel));
//		
//		textureList.put(ColorEnum.RED, tempRed);
//		textureList.put(ColorEnum.GREEN, tempGreen);
//		textureList.put(ColorEnum.BLUE,tempBlue);
//		textureList.put(ColorEnum.CYAN, tempCyan);
//		textureList.put(ColorEnum.MAGENTA, tempMagenta);
//		textureList.put(ColorEnum.YELLOW, tempYellow);
		return textureList;
	}
	
	/**
	 * Helper method to generate set of tinted texture maps.
	 * The foreground texture is tinted and layered above an untinted background texture
	 * @author Brian Chau
	 * @param image filename of the tinted image
	 * @param tintLevel tint intensity
	 * @return a Map that links ColorEnums to their respective textures
	 */
	protected static EnumMap<ColorEnum,BufferedImage> makeTextureMap(String image, float tintLevel)
	{
		EnumMap<ColorEnum,BufferedImage> textureList = new EnumMap<ColorEnum,BufferedImage>(ColorEnum.class);
		BufferedImage tint = BaseCode.resources.loadImage(image);
		for(Map.Entry<ColorEnum, Color> entry : colorMap.entrySet())
		{
			textureList.put(entry.getKey(), ImageManip.tintedImage(tint, entry.getValue(), tintLevel));
		}
//		BufferedImage tempRed = ImageManip.tintedImage(tint, Color.red, tintLevel);
//		BufferedImage tempGreen = ImageManip.tintedImage(tint, Color.green,tintLevel);
//		BufferedImage tempBlue = ImageManip.tintedImage(tint, Color.blue, tintLevel);
//		BufferedImage tempCyan = ImageManip.tintedImage(tint, Color.cyan, tintLevel);
//		BufferedImage tempMagenta = ImageManip.tintedImage(tint, Color.magenta, tintLevel);
//		BufferedImage tempYellow = ImageManip.tintedImage(tint, Color.yellow, tintLevel);
//		
//		textureList.put(ColorEnum.RED, tempRed);
//		textureList.put(ColorEnum.GREEN, tempGreen);
//		textureList.put(ColorEnum.BLUE,tempBlue);
//		textureList.put(ColorEnum.CYAN, tempCyan);
//		textureList.put(ColorEnum.MAGENTA, tempMagenta);
//		textureList.put(ColorEnum.YELLOW, tempYellow);
		return textureList;
	}

	/**
	 * instantiates a GridElement with a default position of (Integer.MIN_VALUE, Integer.MIN_VALUE)
	 * and a random color.
	 * @param gm reference to Game
	 */
	public GridElement(Game gm){ 
		
		this(new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE),getRandomColorEnum(),gm);
		
	}
	
	/**
	 * construct a GridElement given a position and color
	 * @author Brian Chau
	 * @param pos position
	 * @param colorType color.
	 * @param gm reference to Game
	 */
	public GridElement(IntVector pos, ColorEnum colorType,Game gm)
	{
		if (gm == null)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.MUST_HAVE_GAME);
		}
		if (colorType == null) colorType = getRandomColorEnum();
		if (pos == null) pos = new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
		mGM = gm;		
		mColor = colorType;
//		//TODO:lazy instantiation of minimap offset
//		if(minimapCenterOffset == null)
//		{
//			minimapCenterOffset = new Vector2(mGM.gridWidth * 2f, mGM.gridHeight* .25f);
//		}
		//color = colorList.get(colorType);
		setSize(1,1);
		setCenter(pos.getX(), pos.getY());
		setAutoDrawTo(false);
		//TODO: recalculateMinimapCenter();
	}
	
//	TODO: recalculate minimap locations
//	private void recalculateMinimapCenter()
//	{
//		minimapCenter = new Vector2(center).mult(minimapScaleFactor).add(minimapCenterOffset);
//	}
	

	/**
	 * set the position for the geometry of the GridElement.
	 * if pos is null, the command is ignored
	 * 
	 * @author Brian Chau
	 * @param animate shows movement animation if true
	 * @param pos target position
	 */
	public void moveTo(boolean animate, IntVector pos)
	{
		if (pos == null) return;
		Vector2 target = pos.toVector2();
		if (mGM.Particles != null && animate && isVisible()){
			mGM.Particles.add(new MoveParticle(this, target));
		}
		setCenter(target);
		//TODO: this.recalculateMinimapCenter();
	}
	
	/**
	 * get a random supported color
	 * @author Brian Chau
	 * @return a random color
	 */
	public static ColorEnum getRandomColorEnum()
	{
		int index = rand.nextInt(indexedColorList.size());
		return indexedColorList.get(index);
	}
	
	/**
	 * Stops drawing this GridElement and if it is still in a Grid at the end of update, it is removed
	 * @author Brian Chau
	 */
	public void markForDelete()
	{
		if(!toDelete)
		{
			mGM.Particles.add(new DestroyedParticle(this, getCenter()));
		}
		toDelete = true;

		destroy();	
	}
	
	/**
	 * check if this GridElement is marked for deletion
	 * @author Brian Chau
	 * @return returns true if it is marked for deletion
	 */
	public boolean isMarkedForDelete()
	{
		return toDelete;
	}
	
	public ColorEnum getColorEnum()
	{
		return mColor;
	}
	
	/** 
	 * This method changes the color attribute of the GridElement.
	 * If 
	 * 
	 * @author Samuel Kim, Brian Chau
	 * @param col ColorEnum representing the color to change the GridElement to
	 */
	public void setColorEnum(ColorEnum col){
		if (col != null){
			mColor = col;
			setColor(colorMap.get(col));
		}
	}
	
	/**
	 * returns the geometric position of the GridElement as an IntVector 
	 * (will truncate the floats of the Vector2)
	 * @return IntVector representation of the geometricv position
	 */
	public IntVector getIntCenter(){
		return new IntVector(getCenter());
	}


	/**
	 * draws the GridElement
	 */
	@Override
	public void draw()
	{
		//dont draw if invisible
		if(!isVisible()) return;
		
		if(mGM.repairHelper.withinBounds((int)getCenterX(), (int)getCenterY())){
			super.draw();
		}
		//always draw minimap
		//drawMiniMap();
	}
	
//	TODO: draw minimap
//	public void drawMiniMap()
//	{
//	  if(texture != null)
//	  {
//
//		  //dont animate minimap
//	      BaseCode.resources.drawImage(texture,
//	      		minimapCenter.getX() - (minimapSize.getX() * 0.5f),
//	      		minimapCenter.getY() - (minimapSize.getY() * 0.5f),
//	      		minimapCenter.getX() + (minimapSize.getX() * 0.5f),
//	      		minimapCenter.getY() + (minimapSize.getY() * 0.5f),
//	            rotate);
////	    }
//	  }
//	  else
//	  {
//	    BaseCode.resources.setDrawingColor(color);
//	     BaseCode.resources.drawFilledRectangle(minimapCenter.getX(), minimapCenter.getY(),
//	    		 minimapSize.getX() * 0.5f, minimapSize.getY() * 0.5f, rotate);
//    }
//  }
}
