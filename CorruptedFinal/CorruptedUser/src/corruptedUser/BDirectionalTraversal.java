package corruptedUser;

import gridElements.*;
import gridElements.GridElement.ColorEnum;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import structures.IntVector;
import corrupted.Game;

public class BDirectionalTraversal extends Game {
	
	//Enums used: ColorEnum
	//Corrupted methods: setTileGrid, getMouseCenter
	//IntVector methods: getX, getY
	//Tile methods: constructor,  markForDelete
	//TileLogic methods: getRandomExistingColor
	
	private int width = 52;
	private int height = 20;
	GridElement[][] myTileGrid;

	/**
	 * initialize a grid full of random tiles
	 */
	@Override
	public void initialize()
	{
		this.setPlayerVisibility(false);
		myTileGrid = new GridElement[width][height];
		setTileGrid(myTileGrid);
		init();

	}
	
	/**
	 * Initializes the grid with red tiles
	 */
	public void init()
	{
		for (int i = 0; i < width; i++)
		{
			for (int j=0; j < height; j++){
				myTileGrid[i][j] = new Tile(this); //this constructor picks a random color
			}
		}
	}
	
	/**
	 * this method is checks that coordinates are within bounds of the defined array
	 * @param x y coordinate
	 * @param y y coordinate
	 * @return true if within bounds
	 */
	private boolean withinBounds (int x, int  y)
	{
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	/**
	 * Checks an index for a tile. if it is not null, it calls the markForDelete method,
	 * which
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	private void safeDelete(int x, int y)
	{
		if(myTileGrid[x][y] != null)
		{
			//this method will destroy the Tile (triggers destruction particle and stops drawing it).
			//the Tile is also marked for deletion and is automatically removed from the array at the end of
			//the game update. Alternatively, just setting the index of the array to null will remove the tile without the cool animations 
		    myTileGrid[x][y].markForDelete();
			myTileGrid[x][y] = null;
		}
	}
	
	/**
	 * use mouse buttons to destroy tiles in different orientations depending on mouse buttons
	 * spacebar resets grid
	 * practices diretional access of elements (horizontal, vertical, diagonal)
	 */
	@Override
	public void update()
	{
		super.update();
		
		//shift left click clears diagonally (slope 1)
		if(keyboard.isButtonDown(KeyEvent.VK_SHIFT) && mouse.isButtonTapped(MouseEvent.BUTTON1)){
			//diagonal traversal 1
			int x = getMouseCenter().getX();
			int y = getMouseCenter().getY();
			while (withinBounds(x,y))
			{
				safeDelete(x,y);
				x++;
				y++;
			}
			//diagonal traversal 3
			x = getMouseCenter().getX();
			y = getMouseCenter().getY();
			while (withinBounds(x,y))
			{
				safeDelete(x,y);
				x--;
				y--;
			}
		}
		//shift left click clears diagonally (slope = -1)
		else if(keyboard.isButtonDown(KeyEvent.VK_SHIFT) && mouse.isButtonTapped(MouseEvent.BUTTON3)){
			//diagonal traversal 2
			int x = getMouseCenter().getX();
			int y = getMouseCenter().getY();
			while (withinBounds(x,y))
			{
				safeDelete(x,y);
				x++;
				y--;
			}
			//diagonal traversal 4
			x = getMouseCenter().getX();
			y = getMouseCenter().getY();
			while (withinBounds(x,y))
			{
				safeDelete(x,y);
				x--;
				y++;
			}
		}
		//left click clears vertical
		else if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
			//column traversal
			int x = getMouseCenter().getX();
			for (int y = 0; y < height; y++)
			{
				safeDelete(x,y);
			}
		}
		//right click clears horizontal
		else if(mouse.isButtonTapped(MouseEvent.BUTTON3)){
			//row traversal
			int y = getMouseCenter().getY();
			for(int x = 0; x < width; x++)
			{
				safeDelete(x,y);
			}
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_SPACE))
		{
			init();
		}
	}
}
