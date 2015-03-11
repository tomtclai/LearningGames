package corruptedUser;

import java.awt.event.KeyEvent;

import Engine.Vector2;
import particles.DestroyedParticle;
import structures.IntVector;
import gridElements.GridElement;
import gridElements.Tile;
import corrupted.Game;

public class CPatternedAccess extends Game{
	
	//Enums used: ColorEnum
	//Corrupted methods: setTileGrid, setPlayerColor
	//Tile methods: constructor setColorEnum
	
	//hey look, 1D array practice here
	//this array contains all suppported colors in the corrupted game. 
	//this allows students to cycle through the colors in order
	GridElement.ColorEnum[] colorArray = new GridElement.ColorEnum[] 
	{
		GridElement.ColorEnum.RED, 
		GridElement.ColorEnum.GREEN, 
		GridElement.ColorEnum.BLUE,
		GridElement.ColorEnum.CYAN,
		GridElement.ColorEnum.MAGENTA,
		GridElement.ColorEnum.YELLOW,
	};
	
	//minimum x value set so the tiles dont cover the cannon
	private static final int minXValue = 3;
	
	private int colorIndex = 0;
	private int width = 26;
	private int height = 10;
	GridElement[][] myTileGrid;
	
	/**
	 * populates the grid (leaving 3 columns to the left empty)
	 * sets the player color to the first color in the list
	 */
	@Override
	public void initialize()
	{
		myTileGrid = new GridElement[width][height];
		for (int i = minXValue; i < width; i++)		{
			for (int j=0; j < height; j++){
				myTileGrid[i][j] = new Tile(GridElement.ColorEnum.GREEN, this);
			}
		}
		setTileGrid(myTileGrid);
		this.setPlayerColorEnum(colorArray[colorIndex]);
	}
	
	/**
	 * up and down keys change the active color.
	 * the number keys triggers loop across the 2d array changing each nth object to the color of the array
	 * e.g. [1] changes every tile, [2] changes every other tile
	 */
	@Override
	public void update()
	{
		super.update();
		//up and down cycle colors (modulo index practice)
		if(keyboard.isButtonTapped(KeyEvent.VK_UP))
		{
			colorIndex--;
			if (colorIndex < 0) //pseudo-modulo by hand
			{
				colorIndex += colorArray.length;
			}
			this.setPlayerColorEnum(colorArray[colorIndex]);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_DOWN))
		{
			colorIndex = (colorIndex+1) % colorArray.length; //modulo operation
			this.setPlayerColorEnum(colorArray[colorIndex]);
		}
		
		//number keys execute patterned color changer
		else if(keyboard.isButtonTapped(KeyEvent.VK_1))
		{
			changeColorPattern(1);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_2))
		{
			changeColorPattern(2);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_3))
		{
			changeColorPattern(3);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_4))
		{
			changeColorPattern(4);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_5))
		{
			changeColorPattern(5);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_6))
		{
			changeColorPattern(6);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_7))
		{
			changeColorPattern(7);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_8))
		{
			changeColorPattern(8);
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_9))
		{
			changeColorPattern(9);
		}
	}
	
	/**
	 * change every nth tile to the player's color.
	 * @param n
	 */
	public void changeColorPattern(int n)
	{
		int counter = 0;
		for (int i = minXValue; i < width; i++)
		{
			for (int j=0; j < height; j++)
			{
				if(myTileGrid[i][j] != null && counter % n == 0) //patterned access
				{
					myTileGrid[i][j].setColorEnum(this.getPlayerColorEnum());
				}
				counter++;
			}
		}
		//TL.connectAllTiles();
	}
}
