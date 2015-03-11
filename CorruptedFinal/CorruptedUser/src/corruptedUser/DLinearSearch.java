package corruptedUser;

import gridElements.GridElement;
import gridElements.Tile;

import java.awt.event.KeyEvent;

import Engine.ConsoleWindow;
import structures.IntVector;
import corrupted.Game;

public class DLinearSearch extends Game{
	
	//Enums used: ColorEnum
	//Corrupted methods: setTileGrid, setPlayerColor, getPlayerColor, movePlayerUp, movePlayerDown, getPlayerHeight
	//ConsoleWindow methods: setShowConsole, writeLine
	//Tile methods: constructor, getColorEnum, markForDelete
	//TileLogic methods: getRandomExistingColor
	
	
	//countdown timer governs how long the console window will stay open
	private int consoleCounter = 0;
	
	private int width = 26;
	private int height = 10;
	private GridElement[][] myTileGrid;
	
	/**
	 * fills up half the grid with randomly colored tiles
	 */
	@Override
	public void initialize()
	{
		myTileGrid = new GridElement[width][height];
		
		for (int x = width/2; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				myTileGrid[x][y] = new Tile(this);
			}
		}
		setTileGrid(myTileGrid);
	};
	
	/**
	 * up and down move the cannon
	 * left shifts the columns of grids toward the left and creates a new column on the far right
	 * right uses the cannon to clear all tiles in a row that match the cannon's color (a console window appears showing how many tiles were removed)
	 */
	@Override
	public void update()
	{
		//up moves the player up
		if(keyboard.isButtonTapped(KeyEvent.VK_UP))
		{
			movePlayerUp();
		}
		//down moves the player down
		else if(keyboard.isButtonTapped(KeyEvent.VK_DOWN))
		{
			movePlayerDown();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_LEFT))
		{
			//2d arrays are arrays OF ARRAYS!
			for (int x = 1; x < getWidth(); x++)
			{
				myTileGrid[x-1] = myTileGrid[x];
			}
			//populate the last column
			GridElement[] newColumn = new GridElement[getHeight()];
			for(int y = 0; y < getHeight(); y++)
			{
				newColumn[y] = new Tile(this);
			}
			 myTileGrid[getWidth()-1] = newColumn;
		}		
		//pressing right destroys any tile on the player's height that matches the color of the player
		//then sets the player's color to a color that
		else if(keyboard.isButtonTapped(KeyEvent.VK_RIGHT))
		{
			int count = 0;
			//linear searching
			GridElement.ColorEnum currentColor = getPlayerColorEnum();
			int y = this.getPlayerHeight();
			for (int x = 0; x < getWidth(); x++)
			{
				if(myTileGrid[x][y] != null)
				{
					if(myTileGrid[x][y].getColorEnum().equals(currentColor))
					{
						myTileGrid[x][y].markForDelete();
						count++;
					}
				}
			}
			
			//write the number of deleted tiles to the console window and show it briefly
			ConsoleWindow.writeLine(currentColor.toString()+" tiles cleared: "+count);
			ConsoleWindow.setShowConsole(true);
			//the console will be visible for 80 updates (2 seconds)
			consoleCounter = 80;	
			
			//change player color to random color currently on the grid
			setPlayerColorEnum(tileHelper.getRandomExistingColor());
			}
		
		//regardless of button presses the console timer decrements and when it hits 0, the console window goes away
		if(consoleCounter > 0)
		{
			consoleCounter--;
		}
		else
		{
			ConsoleWindow.setShowConsole(false);
		}
	}
}
