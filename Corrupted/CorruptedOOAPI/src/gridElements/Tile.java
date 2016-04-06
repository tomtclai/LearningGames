package gridElements;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import Engine.BaseCode;
import Engine.Vector2;
import corrupted.Game;
import corrupted.Game.Sounds;
import gridElements.GridElement.ColorEnum;
import structures.ImageManip;
import structures.IntVector;
import particles.DestroyedParticle;
import particles.DestroyedTileParticle;
import particles.MoveParticle;
import particles.MoveTileParticle;

public class Tile extends GridElement 
{
	
	public static String red = "red.png";
	public static String green = "green.png";
	public static String blue = "blue.png";
	public static String cyan = "light_blue.png";
	public static String magenta = "purple.png";
	public static String yellow = "yellow.png";
	
	
	public static void setRed(String red) {
		Tile.red = red;
	}

	public static void setGreen(String green) {
		Tile.green = green;
	}

	public static void setBlue(String blue) {
		Tile.blue = blue;
	}

	public static void setCyan(String cyan) {
		Tile.cyan = cyan;
	}

	public static void setMagenta(String magenta) {
		Tile.magenta = magenta;
	}

	public static void setYellow(String yellow) {
		Tile.yellow = yellow;
	}

	private boolean specialTexture = false;
	
	/**
	 * map to link color identifiers with textures
	 * @author Brian Chau
	 */
	protected static final EnumMap<ColorEnum,BufferedImage> textureList;
	static 
	{
		textureList = makeTextureMap(red, green, blue, cyan, magenta, yellow, 0.5f);
	}
	
	/**
	 * map to link color identifiers with special state textures
	 * @author Brian Chau
	 */
	protected static final EnumMap<ColorEnum,BufferedImage> specialTextureList;
	static
	{
		specialTextureList = makeTextureMap(red, green, blue, cyan, magenta, yellow, 0.75f);
	}
	
	/**
	 * 8 directional connectors for like-colored animation
	 */
	TileConnector[] connectors;
	
	/**
	 * initializes a new tile with a given color and position
	 * if colorType is null, a random color is chosen
	 * @author Brian Chau
	 * @param pos position for the tile
	 * @param colorType color for the tile
	 * @param gm reference to Game
	 */
	public Tile(IntVector pos, ColorEnum colorType, Game gm)
	{
		super(pos, colorType, gm);
		mColor = colorType;	
		this.setTexture();
		connectors = TileConnector.CreateConnectorSet(this);
	}
	
	/**
	 * initializes a new tile with a given color and default location
	 * @author Brian Chau
	 * @param colorType color of the tile
	 * @param gm reference to Game
	 */
	public Tile(ColorEnum colorType, Game gm)
	{
		this(new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE),colorType,gm);
	}
	
	/**
	 * initializes a new tile with a random color and default location
	 * @author Brian Chau
	 * @param gm reference to Game
	 */
	public Tile(Game gm)
	{
		this(new IntVector(Integer.MIN_VALUE,Integer.MIN_VALUE),getRandomColorEnum(),gm);
	}
	
	/**
	 * Changes the color of the tile. Also updates the texture and connector textures
	 */
	@Override
	public void setColorEnum(ColorEnum colorType){
		super.setColorEnum(colorType);
		setTexture();
		for(TileConnector tc : connectors)
		{
			tc.setColorEnum(colorType);
		}
	}
	
	/**
	 * Sets the texture type for this tile. 
	 * @param special If true, the special texture will be used.
	 */
	public void useSpecialTexture(boolean special)
	{
		specialTexture = special;
		setTexture();
	}
	
	/**
	 * Correctly sets the tinted textures based on the current color
	 * @author Brian Chau
	 */
	private void setTexture()
	{
		try
		{
			if(specialTexture)
			{
				setImage(specialTextureList.get(mColor));
			}
			else
			{
				setImage(textureList.get(mColor));
			}
		}
		catch(Exception e)
		{
			setImage("TileTint.png");
		}
	}
	
	
	
	/**
	 * set the position of the Tile
	 * @author Brian Chau
	 * @param pos new position
	 */
	@Override
	public void moveTo(boolean animate, IntVector pos)
	{
		if(pos == null) return;
		Vector2 target = pos.toVector2();
		if (mGM.Particles != null && animate && isVisible()){
			mGM.Particles.add(new MoveTileParticle(this, target));
		}
		setCenter(target);
		for(TileConnector tc : connectors)
		{
			tc.setCenter(target);
		}
	}
	
//	/**
//	 * get the tile color
//	 * @return tile color
//	 */
//	public ColorEnum getTileColor()
//	{
//		return mColor;
//	}
	
	/**
	 * activates the connector on a given side or corner
	 * @param orientation int that represents the direction of the connector to activate (0 = north, going clockwise)
	 */
	public void activateConnector(int orientation)
	{
		if(orientation >= 0 && orientation < connectors.length)
		{
			this.connectors[orientation].connect();
		}
	}
	
	/**
	 * resets the connector on a given side or corner
	 * @param orientation int that represents the direction of the connector to reset (0 = north, going clockwise)
	 */
	public void resetConnector(int orientation)
	{
		if(orientation >= 0 && orientation < connectors.length)
		{
			this.connectors[orientation].reset();
		}
	}
	
	/**
	 * gets the current connectorState of a given connector
	 * @param orientation integer that represents the direction of the connector to check state (0 = north, going clockwise)
	 * @return true if tile is connected in the querie'd direction.
	 */
	public boolean isConnected(int orientation)
	{
		if(orientation >= 0 && orientation < connectors.length)
		{
			return this.connectors[orientation].getConnectedState();
		}
		return false;
	}
//	/**
//	 * if it is marked for delete, then it is destroyed and returns true
//	 * @author Brian Chau
//	 * @return true if deleted, otherwise false
//	 */
//	@Override
//	public boolean deleteIfMarked()
//	{
//		if(toDelete)
//		{
//			destroy();
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * Stops drawing this Tile and if it is still in a Grid at the end of update, it is removed
	 * @author Brian Chau
	 */
	public void markForDelete()
	{
		toDelete = true;
		mGM.Particles.add(new DestroyedTileParticle(this, getCenter()));
		mGM.triggerSound(Sounds.clear);
		destroy();	
	}
	
	/**
	 * draws the tile
	 * @author Brian Chau
	 */
	@Override
	public void draw()
	{
		if(isVisible()){
			for(TileConnector tc : connectors)
			{
				tc.draw();
			}
			super.draw();
		}
	}

}

