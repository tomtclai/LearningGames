package gridElements;
import structures.*;
import Engine.BaseCode;
import corrupted.Game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Corruption extends GridElement{
	
	protected BufferedImage corruptedSpriteSheet = BaseCode.resources.loadImage("CorruptedSpriteSheet.png");
	private static Random rand = new Random();
	public Corruption(IntVector pos, Game gm)
	{
		super(pos, ColorEnum.RED, gm); //corrupted is actually colorless

		this.setSpriteSheet(corruptedSpriteSheet, PIXEL_SIZE, PIXEL_SIZE, 22, 1);
		this.setUsingSpriteSheet(true);
		
	}
}
