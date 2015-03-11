package gridElements;

import gridElements.GridElement.ColorEnum;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import structures.ImageManip;
import Engine.BaseCode;

public class TileConnector extends GridElement{
	
	private boolean _connected;
	
	private final int mOrientation;
	/**
	 * map to link color identifiers with textures
	 * @author Brian Chau
	 */
	protected static final EnumMap<ColorEnum,BufferedImage[]> textureList;
	static 
	{
		textureList = new EnumMap<ColorEnum,BufferedImage[]>(ColorEnum.class);
		BufferedImage[] base = new BufferedImage[]
		{
			BaseCode.resources.loadImage("ConN.png"),
			BaseCode.resources.loadImage("ConNE.png"),
			BaseCode.resources.loadImage("ConE.png"),
			BaseCode.resources.loadImage("ConSE.png"),
			BaseCode.resources.loadImage("ConS.png"),
			BaseCode.resources.loadImage("ConSW.png"),
			BaseCode.resources.loadImage("ConW.png"),
			BaseCode.resources.loadImage("ConNW.png")
		};
		
		for(ColorEnum col : indexedColorList)
		{
			BufferedImage[] tinted = new BufferedImage[8];
			for(int i = 0; i < 8; i++)
			{
				tinted[i] = ImageManip.tintedImage(base[i], colorMap.get(col), .75f);
			}
			textureList.put(col,tinted);
		}
	}
	
	/**
	 * sets the texture and rotation depending on orientation
	 * @param orientation rotational direction. 0 is north, 1 is northeast (continue counterclockwise)
	 */
	private void setTexture(int orientation)
	{
		//even numbers and zero will be axis aligned

			//this.setSpriteSheet(textureList.get(mColor)[orientation], PIXEL_SIZE, PIXEL_SIZE, 1, 1);
			setImage(textureList.get(mColor)[orientation]);
			//this.setUsingSpriteSheet(true);
	}
	
	/**
	 * Constructor creates a new TileConnector
	 * @param parent Tile to derive color and position
	 * @param orientation rotational direction. 0 is north, 1 is northeast (continue counterclockwise)
	 */
	public TileConnector(Tile parent, int orientation)
	{
		super(parent.getIntCenter(), parent.mColor, parent.mGM);
		setTexture(orientation);
		mOrientation = orientation;
		reset();
		_connected = false;
		setToInvisible();
		
	}
	
	
	@Override
	public void setColorEnum(ColorEnum colorType)
	{
		super.setColorEnum(colorType);
		this.setTexture(mOrientation);
		reset();
	}
	
	/**
	 * his method is a helper for the Tile class to generate a set of tileConnectors
	 * @param parent Tile for which to generate the connectors
	 * @return an array of 8 connectors, one for each direction
	 */
	public static TileConnector[] CreateConnectorSet(Tile parent)
	{
		return new TileConnector[]
		{
				new TileConnector(parent, 0),
				new TileConnector(parent, 1),
				new TileConnector(parent, 2),
				new TileConnector(parent, 3),
				new TileConnector(parent, 4),
				new TileConnector(parent, 5),
				new TileConnector(parent, 6),
				new TileConnector(parent, 7)
		};
	}
	
	/**
	 * Activates this connector
	 * 
	 * @author Brian Chau
	 */
	public void connect()
	{
		//this.setAutoUpdateSpriteFrame(true);
		_connected = true;
		setToVisible();
	}
	
	/**
	 * Deactivates this connector
	 * 
	 * @author Brian Chau
	 */
	public void reset()
	{
		//this.setAutoUpdateSpriteFrame(false);
		//this.setLoopSpriteSheet(false);
		//this.setCurrentFrame(0);
		_connected = false;
		setToInvisible();
	}
	
	public boolean getConnectedState()
	{
		return _connected;
	}
}
