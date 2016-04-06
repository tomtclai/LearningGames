package corruptedUser;

import java.awt.event.KeyEvent;

import Engine.Vector2;
import particles.DestroyedParticle;
import structures.IntVector;
import gridElements.GridElement;
import gridElements.Tile;
import corrupted.Game;

public class E2DConditionalCountRemoval extends Game{
	
	//Enums used: ColorEnum
	//Corrupted methods: setTileGrid, setPlayerColor
	//Tile methods: constructor, markForDelete
	
	//hey look, 1D array practice here
	GridElement.ColorEnum[] colorArray = new GridElement.ColorEnum[] 
	{
		GridElement.ColorEnum.RED, 
		GridElement.ColorEnum.GREEN, 
		GridElement.ColorEnum.BLUE,
		GridElement.ColorEnum.CYAN,
		GridElement.ColorEnum.MAGENTA,
		GridElement.ColorEnum.YELLOW,
	};
	
	private static final int minXValue = 3;
	
	private int colorIndex = 0;
	private int width = 26;
	private int height = 10;
	GridElement[][] myTileGrid;
	
	
	public void initialize()
	{
		myTileGrid = new GridElement[width][height];
		init();
		setTileGrid(myTileGrid);
		this.setPlayerColorEnum(colorArray[colorIndex]);
	}
	
	@Override
	public void update()
	{
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
		

		else if(keyboard.isButtonTapped(KeyEvent.VK_RIGHT))
		{
			//count each column and build an int array of column counts
			int[] colorCount = new int[width];
			int maxCountValue = 0;
			
			for(int i = minXValue; i < width; i++)
			{
				int columnColorCount = 0;
				for(int j = 0; j < height; j++)
				{
					if(myTileGrid[i][j] != null && myTileGrid[i][j].getColorEnum().equals(getPlayerColorEnum()))
					{
						columnColorCount++;
					}
				}
				colorCount[i] = columnColorCount;
				//track the highest count
				if(columnColorCount > maxCountValue)
				{
					maxCountValue = columnColorCount;
				}
			}
			
			//loop through again and destroy rows that have the highest count
			for(int x = minXValue; x < width; x++){
				if(colorCount[x] == maxCountValue){
					//destroy rows
					for (int y = 0; y < height; y++){
						if(myTileGrid[x][y]!=null){
							myTileGrid[x][y].markForDelete();
						}
					}
				}
			}
		}
		
		else if(keyboard.isButtonTapped(KeyEvent.VK_LEFT))
		{
			init();
		}
	}
	
	private void init()
	{
		for (int i = minXValue; i < width; i++){
			for (int j=0; j < height; j++){
				myTileGrid[i][j] = new Tile(this);
			}
		}
	}
	
	public void changeColorPattern(int x)
	{
		int counter = 0;
		for (int i = minXValue; i < width; i++)
		{
			for (int j=0; j < height; j++)
			{
				if(myTileGrid[i][j] != null && counter % x == 0) //patterned access
				{
					myTileGrid[i][j].setColorEnum(this.getPlayerColorEnum());
				}
				counter++;
			}
		}
		//TL.connectAllTiles();
	}
}
