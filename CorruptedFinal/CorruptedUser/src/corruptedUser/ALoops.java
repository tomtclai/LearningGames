package corruptedUser;

import gridElements.*;
import gridElements.GridElement.ColorEnum;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import structures.IntVector;
import corrupted.Game;


public class ALoops extends Game {
	
	//Enums used: ColorEnum
	//Corrupted methods: setTileColumn, setPlayerHeight
	//GridElement methods: GetRandomColor
	//Tile methods: constructor, getColorEnum
	
	GridElement[] myTileArray; //bracket use 1

	/**
	 * create a 1D array of yellow tiles and put it into the api at horizontal position 10
	 */
	@Override
	public void initialize()
	{
		myTileArray = new GridElement[10]; //bracket use 2
		for (int i = 0; i < myTileArray.length; i++) //.length
		{
			myTileArray[i] = new Tile(ColorEnum.YELLOW, this);//bracket use 3
		}
		int horizontalPosition  = 10; //1D arrays are rendered as vertical columns. when injecting them into the API, you must provide a horizontal position
		
		//this method sets the tile grid with a default width (25) and puts the vertical 1D array into the 10th position
		setTileColumn(myTileArray, horizontalPosition); //arrays as parameters
	}

	/**
	 * The cannon follows the mouse.
	 * left click turns all tiles below the cannon to turn player's color
	 * right click turns all tiles above the cannon to turn the player's color
	 * practices 1D array looping
	 */
	@Override
	public void update()
	{
		super.update();
		
		//get the mouse position (this gets automatically bounded to the world coordinates)
		IntVector mousePosition = getMouseCenter();
		int myHeight = mousePosition.getY();
		
		//move the player cannon to the mouse's height
		this.setPlayerHeight(myHeight);
		
		//on left click all tiles below the mouse height is turned the color of the player's cannon
		if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
			int i = 0;
			while (i <= myHeight)
			{
				GridElement currentTile = myTileArray[i];
				currentTile.setColorEnum(getPlayerColorEnum());
				i++;
			}
			this.setPlayerColorEnum(GridElement.getRandomColorEnum());
		}
		
		//on right click all tiles above the mouse height is turned the color of the player's cannon
		//then player is set to random color
		else if(mouse.isButtonTapped(MouseEvent.BUTTON3)){
			int i = getHeight()-1;
			while (i >= myHeight)
			{
				GridElement currentTile = myTileArray[i];
				currentTile.setColorEnum(getPlayerColorEnum());
				i--;
			}
			this.setPlayerColorEnum(GridElement.getRandomColorEnum());
		}
	}
}
