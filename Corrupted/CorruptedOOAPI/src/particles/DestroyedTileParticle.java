package particles;

import gridElements.GridElement;
import gridElements.GridElement.ColorEnum;
import Engine.BaseCode;
import Engine.Vector2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import structures.ImageManip;


public class DestroyedTileParticle extends Particle{

	private static EnumMap<ColorEnum, BufferedImage> textureMap;
	static
	{
		textureMap = new EnumMap<ColorEnum,BufferedImage>(ColorEnum.class);
		BufferedImage tint = BaseCode.resources.loadImage("TileDestroy.png");
		for(Map.Entry<ColorEnum, Color> entry : GridElement.colorMap.entrySet())
		{
			textureMap.put(entry.getKey(), ImageManip.tintedImage(tint, entry.getValue(), .75f));
		}
	}
	
	public DestroyedTileParticle(GridElement ge, Vector2 stopLoc)
	{
		super(ge, stopLoc, 10);
		ColorEnum col = GridElement.getRandomColorEnum();
		if (ge != null) 
		{
			col = ge.getColorEnum();
		}
		this.setSpriteSheet(textureMap.get(col), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 3, 2);
		
		if(getImage() != null){
			this.setUsingSpriteSheet(true);	
			this.setAnimationPauseStatus(false);
		}
	}
	
	public void update()
	{
		this.setSize(getWidth()+.1f, getHeight()+.1f);
		super.update();
	}
}
