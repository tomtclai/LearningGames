package TowerDefense;
import java.util.LinkedList;

import Engine.GameObject;
import Engine.MouseInput;
import Engine.Vector2;

/** 
 * A tile is a rectangular area representing terrain on a 2-dimensional grid.  This grid is the Grid. 
 * As you might expect, the Tile class and Grid class are quite coupled.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Tile extends GameObject
{		
	public static final String IMAGE_PATH_UP_LEFT = "art/Background/Path-Background Pieces/Path4.png";
	public static final String IMAGE_PATH_UP_RIGHT = "art/Background/Path-Background Pieces/Path5.png";
	public static final String IMAGE_PATH_DOWN_LEFT = "art/Background/Path-Background Pieces/Path2.png";
	public static final String IMAGE_PATH_DOWN_RIGHT = "art/Background/Path-Background Pieces/Path3.png";
	
	public static final String IMAGE_PATH_LEFT_RIGHT = "art/Background/Path-Background Pieces/Path6.png";
	public static final String IMAGE_PATH_UP_DOWN = "art/Background/Path-Background Pieces/Path1.png";
	
	public static final String IMAGE_TILE_BASIC = "tempTile.png"; // not really used, good for debug
	
	
	private static float heightMultiplierFromWidth = 1;	
											// width * heightMultiplierFromWidth = height
	
	
	private boolean isReserved = false;		// if a Tower is scheduled to move here, we don't let others in
	
	private Tower occupant = null;			// which Tower, if any, is currently in the Tile
	
	private boolean isBlocked = false;		// blocked tiles cannot be moved to
	
	private Tile left, up, down, right;		// what Tile is in that direction?
	
	private Direction pathBranches[] = new Direction[]{null, null};	// the directions this Tile's path branches in
	
	private boolean clicked = false; 		// whether or not the object was clicked at last update
	
	private int gridColumn, gridRow;			// this Tile's position in the Grid
	
	
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////	
	
	/**
	 * Constructor.
	 * @param xLoc					X-location of this tile.
	 * @param yLoc					Y-location of this tile.
	 * @param width					Width of the tile.
	 * @param height				Height of the tile.
	 * @param gridColumn			Column this tile occupies in associated grid.		
	 * @param gridRow				Row this tile occupies in the associated grid.
	 */
	public Tile(float xLoc, float yLoc, float width, float height, int gridColumn, int gridRow)
	{
		super(xLoc, yLoc, width, height);
		
		this.gridColumn = gridColumn;
		this.gridRow = gridRow;
		
		this.setImage(IMAGE_TILE_BASIC);		
	}
	
	
	/**
	 * Destructor.
	 */
	public void destroy()
	{
		occupant = null;
		left = null;
		right = null;
		up = null;
		down = null;
		
		super.destroy();
	}
	
	
	///////////////////////////////
	//                           //
	//      Initialization       //
	//                           //
	///////////////////////////////
	
	
	/**
	 * Connects this Tile to another Tile; the other Tile will be on this Tile's left,
	 * and this Tile will be on the other Tile's right.
	 * 
	 * @param leftTile	The other Tile to connect this Tile to.
	 * @return			Returns true if the connection was successful.
	 */
	public boolean connectWithLeft(Tile leftTile)
	{
		if(this.left != null || leftTile.right != null)
		{
			return false;
		}
		
		this.left = leftTile;
		leftTile.right = this;		
		
		return true;
	}
	
	
	
	/**
	 * Connects this Tile to another Tile; the other Tile will be below this Tile,
	 * and this Tile will be above the other Tile.
	 * @param downTile	The other Tile to connect this Tile to.
	 * @return			Returns true if the connection was successful.
	 */
	public boolean connectWithDown(Tile downTile)
	{
		if(this.down != null || downTile.up != null)
		{
			return false;
		}
		
		this.down = downTile;
		downTile.up = this;		
		
		return true;
	}
	
	
	
	///////////////////////////////
	//                           //
	//  Neighboring Tile Stuff   //
	//                           //
	///////////////////////////////
	
	/**
	 * @return	The tile above this one.
	 */
	public Tile getUp()
	{
		return up;
	}
	
	/**
	 * @return	The tile below this one.
	 */
	public Tile getDown()
	{
		return down;
	}
	
	/**
	 * @return	The tile left of this one.
	 */
	public Tile getLeft()
	{
		return left;
	}
	
	/**
	 * @return	The tile right of this one.
	 */
	public Tile getRight()
	{
		return right;
	}
	
	/**
	 * Returns the tile in the corresponding direction of this one.
	 * @param direction		The direction
	 * @return				The tile
	 */
	public Tile getTileTo(Direction direction)
	{
		switch(direction)
		{
			case UP:		return up;
			case DOWN:		return down;
			case LEFT:		return left;
			case RIGHT:		return right;
			default:		return null;
		}
	}
	
	
	
	
	///////////////////////////////
	//                           //
	//    Grid Location Stuff     //
	//                           //
	///////////////////////////////
	
	/**
	 * @return		The row this tile occupies in its grid.
	 */
	public int getGridRow()
	{
		return gridRow;
	}
	
	/**
	 * @return		The column this tile occupies in its grid.
	 */
	public int getGridColumn()
	{
		return gridColumn;
	}
	

	///////////////////////////////
	//                           //
	//        Path Stuff         //
	//                           //
	///////////////////////////////
	
	
	/**
	 * Remove any path on this tile.
	 */
	public void removePath()
	{
		pathBranches[0] = null;
		pathBranches[1] = null;
		this.setImage(IMAGE_TILE_BASIC);
		this.setVisibilityTo(false);
	}
	
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles in two specified directions.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	private boolean addPath(Direction exit0, Direction exit1, String pathImage)
	{
		if(this.hasPath()) return false;
		
		pathBranches[0] = exit0;
		pathBranches[1] = exit1;
		
		this.setImage(pathImage);
		
		return true;
	}
	
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles to the top and bottom.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	public boolean addPathUpDown()
	{
		return addPath(Direction.UP, Direction.DOWN, IMAGE_PATH_UP_DOWN);
	}
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles to the left and right.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	public boolean addPathLeftRight()
	{
		return addPath(Direction.LEFT, Direction.RIGHT, IMAGE_PATH_LEFT_RIGHT);
	}
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles to the top and left.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	public boolean addPathUpLeft()
	{
		return addPath(Direction.UP, Direction.LEFT, IMAGE_PATH_UP_LEFT);
	}
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles to the top and right.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	public boolean addPathUpRight()
	{
		return addPath(Direction.UP, Direction.RIGHT, IMAGE_PATH_UP_RIGHT);
	}
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles to the bottom and left.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	public boolean addPathDownLeft()
	{
		return addPath(Direction.DOWN, Direction.LEFT, IMAGE_PATH_DOWN_LEFT);
	}
	
	/**
	 * Marks this Tile as being a part of the grid's Path,
	 * with the ability to connect to Tiles to the bottom and right.
	 * This means that when the Path is created using grid.constructPath(),
	 * this Tile can be included in the Path.
	 * @param exit0			One direction that the path exits from this tile.
	 * @param exit1			The other direction that the path exits from this tile.
	 * @param pathImage		The Path image to use if drawing this tile.
	 * @return				True if the path was created.
	 */
	public boolean addPathDownRight()
	{
		return addPath(Direction.DOWN, Direction.RIGHT, IMAGE_PATH_DOWN_RIGHT);
	}
	
	
	///////////////////////////////
	//                           //
	//       Path Getters        //
	//                           //
	///////////////////////////////
	
	
	/**
	 * @return		True if this Tile is marked with a path.
	 */
	public boolean hasPath()
	{
		return pathBranches[0] != null && pathBranches[1] != null;
	}
	

	
	/**
	 * If this Tile is setup to be part of the Path,
	 * returns true if one of the directions it
	 * branches in is the chosen direction.
	 * @param direction		The direction to test against.
	 * @return				True if input direction is one of this Tile's exits.
	 */
	public boolean pathBranchesTo(Direction direction)
	{
		return pathBranches[0] == direction || pathBranches[1] == direction;
	}	
	
	/**
	 * @return		True if the Tile is marked with a path that exits up.
	 */
	public boolean pathBranchesUp()
	{
		return pathBranchesTo(Direction.UP);
	}
	
	/**
	 * @return		True if the Tile is marked with a path that exits down.
	 */
	public boolean pathBranchesDown()
	{
		return pathBranchesTo(Direction.DOWN);
	}
	
	/**
	 * @return		True if the Tile is marked with a path that exits left.
	 */
	public boolean pathBranchesLeft()
	{
		return pathBranchesTo(Direction.LEFT);
	}
	
	/**
	 * @return		True if the Tile is marked with a path that exits right.
	 */
	public boolean pathBranchesRight()
	{
		return pathBranchesTo(Direction.RIGHT);
	}
	

	
	
	/**
	 * Returns true if this Tile is capable of connecting to form part
	 * of a Path with the Tile in the specified direction.
	 * Specifically, if this Tile branches in the specified direction,
	 * and the Tile in that direction branches in the opposite direction,
	 * returns true.
	 * @param direction		The direction to check.
	 * @return				True if this Tile and the Tile in the specified direction are ready to form a Path.
	 */
	public boolean pathConnectsTo(Direction direction)
	{
		if (!pathBranchesTo(direction)) 	return false;
		
		Tile other = getTileTo(direction);		
		if(other == null)		return false;
		
		Direction reverseDirection = Direction.oppositeOf(direction);
		
		return other.pathBranchesTo(reverseDirection);
	}	
	
	/**
	 * @return	True if this Tile is and the Tile above it are marked with paths that exit towards each other.
	 */
	public boolean pathConnectsUp()
	{
		return pathConnectsTo(Direction.UP);
	}
	
	/**
	 * @return	True if this Tile is and the Tile below it are marked with paths that exit towards each other.
	 */
	public boolean pathConnectsDown()
	{
		return pathConnectsTo(Direction.DOWN);
	}
	
	/**
	 * @return	True if this Tile is and the Tile left of it are marked with paths that exit towards each other.
	 */
	public boolean pathConnectsLeft()
	{
		return pathConnectsTo(Direction.LEFT);
	}
	
	/**
	 * @return	True if this Tile is and the Tile right of it are marked with paths that exit towards each other.
	 */
	public boolean pathConnectsRight()
	{
		return pathConnectsTo(Direction.RIGHT);
	}
	
	
	
	/**
	 * Returns true if this Tile branches in a direction,
	 * but the Tile in that direction (if any) doesn't
	 * branch in our direction.
	 * This means that the two Tiles cannot connect as
	 * part of a Path.
	 * @param direction		The direction to check in.
	 * @return				True if the two Tiles branch in opposite directions.
	 */
	public boolean pathBranchesWithoutConnectingTo(Direction direction)
	{
		return this.pathBranchesTo(direction) && !this.pathConnectsTo(direction);
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//    Mouse Clicky Stuff     //
	//                           //
	///////////////////////////////	
	
	
	/**
	 * @return		True if this tile was clicked within the last frame.
	 */
	public boolean wasClicked()
	{
		return clicked;
	}
	
	
	/**
	 * Updates whether this tile has been clicked.
	 * @param mouse		The mouse doing the clicking
	 */
	private void checkIfClicked(MouseInput mouse)
	{
		this.clicked = false;
		
		if(mouse == null) return;
		
		if(mouse.MouseOnScreen() && mouse.isButtonTapped(1))
		{
			Vector2 mousePosition = new Vector2(mouse.getWorldX(), mouse.getWorldY());
			
			if(this.containsPoint(mousePosition))
			{
				this.clicked = true;
			}
		}

	}
	
	
	/**
	 * @return		True if there is room for a Tower to move here.
	 */
	public boolean isAvailable()
	{
		return !hasOccupant() && !isBlocked();
	}
	
	
	/**
	 * Sets whether Towers are allowed to move to this Tile.
	 * Does not have any effect on Towers that are already
	 * occupying the Tile.
	 * @param isBlocked		True if the Tile is blocked.
	 */
	public void setBlockedTo(boolean isBlocked)
	{
		this.isBlocked = isBlocked;
	}
	
	/**
	 * Returns whether or not Towers are allowed to move to
	 * this Tile.  Note that this does not take into
	 * account whether the Tile is already occupied.
	 * @return				True if the Tile is blocked.
	 */
	public boolean isBlocked()
	{
		return isBlocked;
	}
	
	
	
	///////////////////////////////
	//                           //
	//           Update          //
	//                           //
	///////////////////////////////	
	
	/**
	 *  Updates the tile.
	 */
	public void update(MouseInput mouse)
	{				
		super.update();
		checkIfClicked(mouse);
	}
	
	
	///////////////////////////////
	//                           //
	//        Occupants          //
	//                           //
	///////////////////////////////	
	
	
	/**
	 * Creates a new Tower that is a Medic on this tile.
	 * Probably not the best way to create a Tower, but why not?
	 * @return		True if it was created.
	 */
	public boolean addTower()
	{
		if(this.hasOccupant() || isBlocked())
		{
			return false;
		}
		
		occupant = new TowerMedic();
		occupant.setTile(this);
		
		return true;
	}
	
	
	
	
	
	/**
	 * Put a Tower in this tile.
	 * @param tower			The Tower you want to put on the tile
	 * @return				True if the Tower was placed on the tile.
	 */
	public boolean setOccupant(Tower tower)
	{
		// not allowed to setOccupant if already occupied
		if(this.hasOccupant() || isBlocked)
		{
			return false;
		}		
		else
		{
			occupant = tower;
			return true;
		}		
	}
	
	
	/**
	 * Removes the Tower from the tile.
	 */
	public void removeOccupant()
	{
		this.occupant = null;	
	}
	
	
	/**
	 * 
	 * @return 	The Tower that is currently occupying the tile
	 */
	
	public Tower getOccupant()
	{
		return occupant;
	}
	
	
	/**
	 * @return 	True if there is a Tower currently on the tile
	 */
	public boolean hasOccupant()
	{
		return occupant != null;
	}
	

	
	



	
	/////////////////////////
	//                     //
	// Reservation System  //
	//                     //
	/////////////////////////
	

	/**
	 * @return	True if the Tile is reserved by a Tower.
	 */
	public boolean isReserved()
	{
		return isReserved;
	}


	/**
	 * Sets the Tile to have the desired reservation status.
	 * @param isReserved
	 */
	public void setReserved(boolean isReserved) 
	{
		this.isReserved = isReserved;
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//    WORDS WORDS WORDS      //
	//                           //
	///////////////////////////////
	
	/**
	 * For getting proportions.
	 * @return	The number you have to multiply height by to get width.
	 */
	public static float getHeightMultiplierFromWidth()
	{
		return Tile.heightMultiplierFromWidth;
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//    Walkers Be On Me       //
	//                           //
	///////////////////////////////	
	
	
	/**
	 * commented out now because it doesn't work and the current game doesn't need it to work
	 * @return		True if any Walker is colliding with this tile's collision
	 */
	/*
	public boolean isCollidingWithWalker()
	{
		LinkedList<Walker> walkerList = GameplayArea.getWalkerList(); // <-- this is why it doesn't work
		
		for(Walker walker : walkerList)
		{
			if(!walker.isDead() && walker.collided(this))
			{
				return true;
			}
		}
		return false;
	}
	*/
	
	
}
