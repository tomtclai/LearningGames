package TowerDefense;

import Engine.DrawingLayer;
import Engine.GameObject;
import Engine.MouseInput;

/**
 * The Grid is a 2-dimensional grid of Tiles.  Tiles are like little squares of land.
 * Towers can be placed on it, and a Path can be created on it.
 * 
 * This class is not as robust as it could be, but the foundation is solid. :)
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Grid
{	
	private float width;				// width of Grid
	private float height;				// height of Grid
	
	private float xCenter;				// x-coordinate of the Grid's center
	private float yCenter;				// y-coordinate of the Grid's center
	
	private float tileWidth;			// width of the Tiles for this Grid
	private float tileHeight;			// height of the Tiles for this Grid
	
	
	private Tile[][] tiles;				// matrix containing all of the Tiles
	
	private int columns, rows;			// dimensions of the matrix

	private Path path = null;			// the path Walkers will take
	private int tilesInPath = 0;
	
	
	private Tile clickedTile = null;	// the tile, if any, that was clicked this game loop
	
	
	private GameObject background = null;		// can be used, but not used by default

	private boolean pathTilesAreVisible = true;
	
	private DrawingLayer tileDrawingLayer = null;
	
	
	
	///////////////////////////////
	//                           //
	//        Constructor        //
	//                           //
	///////////////////////////////
	
	
	
	/**
	 * Constructor.
	 * @param xCenter		X-coordinate of grid center
	 * @param yCenter		Y-coordinate of grid center
	 * @param width			Width of grid.
	 * @param height		Height of grid.
	 */
	public Grid(float xCenter, float yCenter, float width, float height)
	{
		if(width <= 0) width = 1;
		if(height <= 0) height = 1;
		
		this.width = width;
		this.height = height;
		
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		
		background = new GameObject(xCenter, yCenter, width, height);
		
		// the background image is currently not JUST the size of the play area
		// so an external background is being used
		background.setVisibilityTo(false);
		
		setDimensions(20, 10);			// default dimensions, we expect user to change them
	}
	
	
	
	/**
	 * Deconstructor.
	 * Destroy this Grid and all of its Tiles.
	 * Please stop using this Grid after you call this method.
	 */
	public void destroy()
	{
		clickedTile = null;
		
		if(background != null)
		{
			background.destroy();
			background = null;
		}	
		
		deleteOldGrid();
	}
	
	

	
	
	
	
	///////////////////////////////
	//                           //
	//    Initialize Grid        //
	//                           //
	///////////////////////////////
	
	
	
	
	
	
	
	/**
	 * Sets the dimensions of the game grid.
	 * Note that this will overwrite any existing game grid,
	 * including destroying all of the things on it.
	 * @param columns
	 * @param rows
	 */
	public boolean setDimensions(int columns, int rows)
	{
		if(columns < 1 || rows < 1) return false;
		
		this.deleteOldGrid();
		
		this.rows = rows;
		this.columns = columns;
		
		this.calculateTileSize(width, height);		
		
		this.createAllTiles();
		
		this.positionAndSizeBackground();
		
		return true;
	}
	
	
	
	
	/**
	 * Removes the old Tiles so that new ones can be created.
	 */
	private void deleteOldGrid()
	{
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < columns; x++)
			{	
				tiles[x][y].destroy();
				tiles[x][y] = null;
			}
		}
	}
	
	
	
	
	/**
	 * Based on the grid dimensions and the rows/columns requested,
	 * calculates the dimensions of Tiles on the screen so that
	 * they fit on the screen.
	 * @param mapWidth			Width set aside for the grid.
	 * @param mapHeight			Height set aside for the grid.
	 */
	private void calculateTileSize(float mapWidth, float mapHeight)
	{
		// There are two ways to scale a grid onto a screen
		// while maximizing use of screen real estate:
		// Base the tile width on grid width, or
		// base the tile height on grid height.
		// In almost all cases, one of these choices will result in
		// tiles going off of the screen, past the boundaries set
		// aside for the grid.  Because of this, we choose the
		// other one - the one that fits everything on the screen.
		
		float heightMultiplier = Tile.getHeightMultiplierFromWidth();
		
		tileWidth = mapWidth / columns;
		tileHeight = tileWidth * heightMultiplier;
		
		// if the tiles don't fit on the screen
		if(tileHeight * rows > mapHeight)
		{
			// let's recalculate and base everything on tileHeight instead
			tileHeight = mapHeight / rows;			
			tileWidth = tileHeight / heightMultiplier;			
		}
	}
	
	
	
	
	/**
	 * Instantiates Tile objects and initializes locations,
	 * as well as linking adjacent tiles together and
	 * making them accessible via the tiles matrix.
	 */
	private void createAllTiles()
	{	
		float lowerLeftCornerX = xCenter - width / 2;
		float lowerLeftCornerY = yCenter - height / 2;
		
		tiles = new Tile[columns][rows];
		
		// center of first tile
		float lowerLeftTileCenterX = lowerLeftCornerX + tileWidth/2f;
		float lowerLeftTileCenterY = lowerLeftCornerY + tileHeight/2f;
		
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < columns; x++)
			{	
				// calculate location of this next Tile
				float xLocation = lowerLeftTileCenterX + tileWidth*x;
				float yLocation = lowerLeftTileCenterY + tileHeight*y;
				
				// make the tiles slightly bigger so there are no gaps
				float adjustedTileWidth = tileWidth * 1.05f;
				float adjustedTileHeight = tileHeight * 1.05f;
				
				tiles[x][y] = new Tile(xLocation, yLocation, adjustedTileWidth, adjustedTileHeight, x, y);
				
				// Tiles without Paths shouldn't be drawn, and they don't have Paths yet
				tiles[x][y].setVisibilityTo(false);
				
				// put Tile on specific DrawingLayer if necessary
				if(tileDrawingLayer != null)
				{
					tiles[x][y].moveToDrawingLayer(tileDrawingLayer);
				}
				
				// connect new Tile to the Tile left of it
				if(x > 0)
				{
					tiles[x][y].connectWithLeft(tiles[x-1][y]);
				}
				
				// connect new Tile to the Tile below it
				if(y > 0)
				{
					tiles[x][y].connectWithDown(tiles[x][y-1]);
				}
			}
		}
	}
	
	
	
	
	
	
	/**
	 * Prerequisite: calculateTileSize has been called for current width and height
	 */
	private void positionAndSizeBackground()
	{		
		float backgroundWidth = tileWidth * columns;
		float backgroundHeight = tileHeight * rows;
		background.setSize(backgroundWidth, backgroundHeight);
		
		float backgroundCenterX = xCenter;
		float backgroundCenterY = yCenter;
		background.setCenter(backgroundCenterX, backgroundCenterY);
	}
	
	
	

		
	
	
	
	

	
	
	
	
	///////////////////////////////
	//                           //
	//     RELOCATE / RESIZE     //
	//                           //
	///////////////////////////////	
	
	
	
	
	
	
	/**
	 * @param xCenter
	 * @param yCenter
	 */
	public void setCenter(float xCenter, float yCenter)
	{
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		this.alignTilesWithMap();
		this.positionAndSizeBackground();
	}
	
	
	
	/**
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height)
	{
		this.width = width;
		this.height = height;
		this.calculateTileSize(width, height);
		this.resizeTiles();
		this.positionAndSizeBackground();		
	}
	
	
	
	/**
	 * This method moves all of the Tiles so that they are in the
	 * appropriate positions relative to the Grid object.
	 */
	private void alignTilesWithMap()
	{
		float lowerLeftCornerX = xCenter - width / 2;
		float lowerLeftCornerY = yCenter - height / 2;
		
		// center of first tile
		float lowerLeftTileCenterX = lowerLeftCornerX + tileWidth/2f;
		float lowerLeftTileCenterY = lowerLeftCornerY + tileHeight/2f;
		
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < columns; x++)
			{	
				// calculate location of this next Tile
				float xLocation = lowerLeftTileCenterX + tileWidth*x;
				float yLocation = lowerLeftTileCenterY + tileHeight*y;	
				
				tiles[x][y].setCenter(xLocation, yLocation);			
			}
		}
	}
	
	
	
	/**
	 * Resizes all the Tiles to the current tile size
	 */
	private void resizeTiles()
	{
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < columns; x++)
			{	
				tiles[x][y].setSize(tileWidth, tileHeight);			
			}
		}
	}
	
	
	
	
	///////////////////////////////
	//                           //
	//        VISIBILITY         //
	//                           //
	///////////////////////////////
		
	
	
	
	
	
	/**
	 * Set a background image to appear on the grid.
	 * Note that by default the background is invisible.
	 * @param filename		Name of the image file.
	 */
	public void setBackgroundImage(String filename)
	{
		background.setImage(filename);
	}
	
	
	/**
	 * Set whether the grid's background is visible.
	 * @param isVisible		True if background should be visible
	 */
	public void setBackgroundVisibilityTo(boolean isVisible)
	{
		background.setVisibilityTo(isVisible);
	}
	
	
	/**
	 * Set whether Tiles with paths in the grid are visible.
	 * Note that Tiles without paths are never drawn,
	 * because it ends up being really expensive.
	 * @param isVisible		True if Path Tiles should be visible.
	 */
	public void setPathTileVisibilityTo(boolean isVisible)
	{
		pathTilesAreVisible = isVisible;
		
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < columns; x++)
			{	
				if(tiles[x][y].hasPath())
				{
					tiles[x][y].setVisibilityTo(isVisible);
				}
			}
		}
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//         LAYERS            //
	//                           //
	///////////////////////////////
	
	
	/**
	 * Puts all existing and future Tiles on the specified DrawingLayer.
	 * @param drawingLayer		The DrawingLayer to draw Tiles and/or the background on.
	 */
	public void setDrawingLayer(DrawingLayer drawingLayer)
	{
		if(drawingLayer != tileDrawingLayer)
		{		
			for(int y = 0; y < rows; y++)
			{
				for(int x = 0; x < columns; x++)
				{	
					tiles[x][y].moveToDrawingLayer(drawingLayer);
				}			
			}
		}
		
		tileDrawingLayer = drawingLayer;
	}
	
	
	
	
	///////////////////////////////
	//                           //
	//         UPDATE            //
	//                           //
	///////////////////////////////
	
	
	
	
	/**
	 * For now this just updates each tile,
	 * including finding out if any Tiles have been clicked.
	 */
	public void update(MouseInput mouse)
	{
		clickedTile = null;
		
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < columns; x++)
			{	
				updateTile(tiles[x][y], mouse);
			}			
		}
	}
	
	
	
	
	
	/**
	 * Updates an individual tile, and records that tile as the Grid's clickedTile if needed.
	 * Something to be aware of: if for some reason there are multiple tiles that are
	 * somehow being clicked in a single game loop, only the last Tile tile that is both updated
	 * and clicked will be recorded as THE clickedTile.
	 * @param tile		Tile on the Grid's grid to update.
	 * @param mouse		The game's mouse input.
	 */
	private void updateTile(Tile tile, MouseInput mouse)
	{
		tile.update(mouse);
		
		if(tile.wasClicked())
		{
			clickedTile = tile;
		}
	}
	
	
	
	
	
	public void coordinatesOfClickedTile() {
		
	}
	
	
	
	

	
	
	
	///////////////////////////////
	//                           //
	//     Path Construction     //
	//                           //
	///////////////////////////////	
	
	
	
	/**
	 * Constructs a Path for this Grid object.
	 * The Path is constructed by starting at the specified end Tile and
	 * connecting to an adjacent Tile that has been marked as part of the path, then
	 * connecting to the next, etc, until the start Tile has been reached.
	 * 
	 * If the start and end Tiles can't be connected, or if the start or end Tiles could
	 * connect to multiple Tiles, the building of the Path will fail.
	 * 
	 * @param startColumn		The column of the Tile where the Path begins.
	 * @param startRow			The row of the Tile where the Path begins.
	 * @param endColumn			The column of the Tile where the Path ends.
	 * @param endRow			The row of the Tile where the Path ends.
	 * @return					True if the Path was successfully created.
	 */
	public boolean constructPath(int startColumn, int startRow, int endColumn, int endRow)
	{		
		// wow, this method is really long!  Could probably use some refactoring...
		
		if(!tileHasPath(startColumn, startRow) || !tileHasPath(endColumn, endRow))
		{
			return false;
		}
	
		Tile startTile = tiles[startColumn][startRow];
		Tile endTile = tiles[endColumn][endRow];
		
		Tile currentTile = endTile;		// start at the end, traverse to beginning
		Tile previousTile = null;		// the Tile we added to the path before this one
		
		Waypoint currentWaypoint = null;	
		
		boolean done = false; 		// are we done making a path?
		
		int tempTilesInPath = 0;
		while(!done)
		{		
			tempTilesInPath++;
			
			// if we're the end Tile, we need to set up the final waypoint
			// that way the Walker will walk off this Tile (presumably off the screen)
			// instead of just standing here forever
			if (currentTile == endTile)
			{
				Direction exitDirection = getSingleBranchWithoutConnection(currentTile);				
				
				float x = currentTile.getCenterX();
				float y = currentTile.getCenterY();
				
				switch(exitDirection)
				{
					case UP:	y += tileHeight;	break;
					case DOWN:	y -= tileHeight;	break;
					case LEFT:	x -= tileWidth;		break;
					case RIGHT:	x += tileWidth;		break;
					default:	return false;
				}	
				
				// if we made it here, let's set the final waypoint (which is off this Tile)				
				currentWaypoint = new Waypoint(x, y, null);			
			}


			// create a Waypoint that corresponds to this Tile,
			// and set it as the Waypoint before the previous Waypoint			
			float currentX = currentTile.getCenterX();
			float currentY = currentTile.getCenterY();
			currentWaypoint = new Waypoint(currentX, currentY, currentWaypoint);
			
			
			// if we aren't the startTile, let's go find the next Tile
			if(currentTile != startTile)
			{
				Tile oldPreviousTile = previousTile;
				
				// now let's move on to the next tile
				Direction direction;
				for(int i = 0; i < 4; i++)
				{
					// every loop, gets a new direction
					direction = Direction.getOrderedDirection(i);
					
					// if the Tile can connect to another Tile in this direction
					// AND this direction is not the direction of the previous Tile in the Path
					if(currentTile.pathConnectsTo(direction)
					   && previousTile != currentTile.getTileTo(direction))
					{
						previousTile = currentTile;
						currentTile = currentTile.getTileTo(direction);
						break;
					}	
				}
				
				// if we didn't find a new direction to go in, something went wrong
				if(oldPreviousTile == previousTile)
				{
					return false;
				}
			}
			// otherwise, we are the startTile, and we need to set up the spawn point
			else
			{	
				
				Direction enterDirection = getSingleBranchWithoutConnection(currentTile);
				// to find the spawn direction, we check every direction for branches and connections
				
				switch(enterDirection)
				{
					case UP:	currentY += tileHeight;		break;
					case DOWN:	currentY -= tileHeight;		break;
					case LEFT:	currentX -= tileWidth;		break;
					case RIGHT:	currentX += tileWidth;		break;
					default:	return false;
				}
				
				// at this point, we know where to put the spawn point
				// so let's finalize it by making the Path!
				
				path = new Path(currentX, currentY, currentWaypoint);
				done = true;
			}
		}
		
		tilesInPath = tempTilesInPath;
		return true;
	}
	
	
	
	
	/**
	 * If this Tile is a Path segment, and exactly one of the direction the
	 * segment branches in doesn't connect to another segment, return
	 * the direction that doesn't connect.
	 * Used to find the direction that Walkers should flow towards the start Tile
	 * from, and to find the direction that Walkers should leave the end Tile from.
	 *  
	 * @param tile		The specific Tile in the (Path being created).
	 * @return			The Direction this tile exits in, or null if invalid.
	 */
	private Direction getSingleBranchWithoutConnection(Tile tile)
	{
		// For this next chunk of code, we want to find out which branch is the exit
		Direction testDirection;
		Direction exitDirection = null;
		
		// to find the exit, we check every direction for branches and connections
		for(int i = 0; i < 4; i++)
		{
			// every loop, gets a new direction
			testDirection = Direction.getOrderedDirection(i);			
			
			// if the Tile branches in this direction BUT
			// there is either no Tile in that direction OR the Tile there doesn't branch in our direction
			if(tile.pathBranchesWithoutConnectingTo(testDirection))
			{
				// end Tile can't have two exits without connections
				if(exitDirection != null)
				{
					return null;
				}
				
				exitDirection = testDirection;				
			}
		}
		
		return exitDirection;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @return		A reference to this Grid's Path object.
	 */
	public final Path getPath()
	{
		return path;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	/**
	 * @param column		Column to check.
	 * @param row			Row to check.
	 * @return				True if such a Tile exists on the Grid
	 */
	public boolean containsTile(int column, int row)
	{
		return (column >= 0 && column < columns) && (row >= 0 && row < rows);
	}
	
	
	
	
	
	/**
	 * Marks the corresponding Tile as being a part of the Path,
	 * with the ability to connect to Tiles to the top and bottom.
	 * This means that when the Path is created using constructPath(),
	 * this Tile can be included in the Path.
	 * 
	 * @param column	Column of the Tile to include in the Path
	 * @param row		Row of the Tile to include in the Path
	 * @return			True if Tile was successfully marked
	 */
	public boolean addPathUpDown(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return false;
		}

		tiles[column][row].setVisibilityTo(pathTilesAreVisible);
		
		return tiles[column][row].addPathUpDown();		
	}
	
	/**
	 * Marks the corresponding Tile as being a part of the Path,
	 * with the ability to connect to Tiles to the left and right.
	 * This means that when the Path is created using constructPath(),
	 * this Tile can be included in the Path.
	 * 
	 * @param column	Column of the Tile to include in the Path
	 * @param row		Row of the Tile to include in the Path
	 * @return			True if Tile was successfully marked
	 */
	public boolean addPathLeftRight(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return false;
		}
		
		tiles[column][row].setVisibilityTo(pathTilesAreVisible);
		
		return tiles[column][row].addPathLeftRight();		
	}
	
	/**
	 * Marks the corresponding Tile as being a part of the Path,
	 * with the ability to connect to Tiles to the left and top.
	 * This means that when the Path is created using constructPath(),
	 * this Tile can be included in the Path.
	 * 
	 * @param column	Column of the Tile to include in the Path
	 * @param row		Row of the Tile to include in the Path
	 * @return			True if Tile was successfully marked
	 */
	public boolean addPathUpLeft(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return false;
		}
		
		tiles[column][row].setVisibilityTo(pathTilesAreVisible);
		
		return tiles[column][row].addPathUpLeft();		
	}
	
	/**
	 * Marks the corresponding Tile as being a part of the Path,
	 * with the ability to connect to Tiles to the right and top.
	 * This means that when the Path is created using constructPath(),
	 * this Tile can be included in the Path.
	 * 
	 * @param column	Column of the Tile to include in the Path
	 * @param row		Row of the Tile to include in the Path
	 * @return			True if Tile was successfully marked
	 */
	public boolean addPathUpRight(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return false;
		}

		tiles[column][row].setVisibilityTo(pathTilesAreVisible);
		
		return tiles[column][row].addPathUpRight();		
	}	

	/**
	 * Marks the corresponding Tile as being a part of the Path,
	 * with the ability to connect to Tiles to the left and bottom.
	 * This means that when the Path is created using constructPath(),
	 * this Tile can be included in the Path.
	 * 
	 * @param column	Column of the Tile to include in the Path
	 * @param row		Row of the Tile to include in the Path
	 * @return			True if Tile was successfully marked
	 */
	public boolean addPathDownLeft(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return false;
		}

		tiles[column][row].setVisibilityTo(pathTilesAreVisible);
		
		return tiles[column][row].addPathDownLeft();		
	}
	
	/**
	 * Marks the corresponding Tile as being a part of the Path,
	 * with the ability to connect to Tiles to the right and bottom.
	 * This means that when the Path is created using constructPath(),
	 * this Tile can be included in the Path.
	 * 
	 * @param column	Column of the Tile to include in the Path
	 * @param row		Row of the Tile to include in the Path
	 * @return			True if Tile was successfully marked
	 */
	public boolean addPathDownRight(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return false;
		}

		tiles[column][row].setVisibilityTo(pathTilesAreVisible);
		
		return tiles[column][row].addPathDownRight();		
	}
	
	
	
	
	
	/**
	 * Removes a path, if any, from a Tile..
	 * @param column	Column of the Tile on the Grid's grid.
	 * @param row		Row of the Tile on the Grid's grid.
	 */
	public void removePath(int column, int row)
	{
		if(!containsTile(column, row))
		{
			return;
		}

		tiles[column][row].setVisibilityTo(false);
		
		tiles[column][row].removePath();		
	}
	



	
	/**
	 * @param tile		The Tile to check
	 * @return			True if the Grid's Path includes this Tile
	 */
	private boolean	tileHasPath(Tile tile)
	{
		return tile != null && tile.hasPath();
	}
	

	
	/**
	 * @param column	The column of the Tile to check
	 * @param row		The row of the Tile to check
	 * @return			True if the Grid's Path includes this Tile
	 */
	private boolean tileHasPath(int column, int row)
	{
		return containsTile(column, row) && tileHasPath(tiles[column][row]);
	}
	
	
	
	/**
	 * Sets whether Towers are allowed to move to the specified Tile.
	 * Does not have any effect on Towers that are already
	 * occupying the Tile.
	 * @param column		The column of the Tile
	 * @param row			The row of the Tile
	 * @param isBlocked		True if the Tile is blocked.
	 */
	public void setTileBlockedTo(int column, int row, boolean isBlocked)
	{
		Tile tile = getTile(column, row);		
		if(tile == null)
		{
			return;
		}
		
		tile.setBlockedTo(isBlocked);		
		//tile.setVisibilityTo(isBlocked);  // debug
	}
	
	
	/**
	 * Returns whether or not Towers are allowed to move to
	 * the Tile.  Note that this does not take into
	 * account whether the Tile is already occupied.
	 * @param column		The column of the Tile
	 * @param row			The row of the Tile
	 * @return				True if the Tile is blocked.
	 */
	public boolean tileIsBlocked(int column, int row)
	{
		Tile tile = getTile(column, row);
		if(tile == null)
		{
			return false;
		}
		
		return tile.isBlocked();
	}

	
	
	
	
	///////////////////////////////
	//                           //
	//         Getters           //
	//                           //
	///////////////////////////////
	
	
	
	/**
	 * @return	The number of rows in the current Grid
	 */
	public int getNumberOfRows()
	{
		return rows;
	}
	
	
	
	/**
	 * @return	The number of columns in the current grid
	 */
	public int getNumberOfColumns()
	{
		return columns;
	}
	
	
	
	/**
	 * If a Tile was clicked by the user since the last update,
	 * returns that Tile.  Otherwise, null.
	 * @return		The clicked Tile, or null.
	 */
	public Tile getClickedTile()
	{
		return clickedTile;
	}
	
	
	
	/**
	 * @return	Current width of each Tile
	 */
	public float getTileWidth()
	{
		return tileWidth;
	}
	
	
	/**
	 * @return	Current height of each Tile
	 */
	public float getTileHeight()
	{
		return tileHeight;
	}
	
	
	
	/**
	 * @return	Number of tiles in this Grid's Path.
	 */
	public int getNumberOfTilesInPath()
	{
		return tilesInPath;
	}
	
	
	
	/**
	 * @param column	Column of the tile (zero-indexed)
	 * @param row		Row of the tile (zero-indexed)
	 * @return			The Tile at that location, or null.
	 */
	public Tile getTile(int column, int row)
	{
		if(!containsTile(column, row)) return null;
		
		return tiles[column][row];
	}	
	
	
	
	
	
	
	///////////////////////////////
	//                           //
	//    Path multiple tiles    //
	//                           //
	///////////////////////////////
	
	// NONE OF THIS CURRENTLY WORKS, BUT FIXING IT SHOULD BE EASY
	
	/*
	
	public boolean addVerticalPathSegment(int column, int row, int numTiles)
	{
		return false && addPathSegment(column, row, true, numTiles);
	}
	
	public boolean addHorizontalPathSegment(int column, int row, int numTiles)
	{
		return false && addPathSegment(column, row, false, numTiles);
	}
	
	
	
	
	private boolean addPathSegment(int column, int row, boolean vertical, int numTiles)
	{
		// can't have a path segment without at least 2 tiles
		if(numTiles < 2) return false;
		
		if(!containsTile(column, row)) return false;
		
		int endColumn = column;
		int endRow = row;
		int columnDelta = vertical ? 0 : 1;		// if vertical, columnDelta = 0, else 1
		int rowDelta = vertical ? 1 : 0; 		// if vertical, rowDelta = 1, else 0
		
		///////boolean success = createPathSegment(column, row, numTiles, columnDelta, rowDelta);
		
		////if(!success) 	return false		
		return true;
	}
	
	*/
	
	
	
	
}
