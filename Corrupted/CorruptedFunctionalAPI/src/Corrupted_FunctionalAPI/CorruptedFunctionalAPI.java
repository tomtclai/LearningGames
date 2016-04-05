package Corrupted_FunctionalAPI;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics;
import Engine.GameObject;
import corrupted.Game;
import corrupted.*;
import structures.*;
import layers.*;
import gridElements.*;
import java.awt.Color;
import gridElements.Tile;
import Engine.Vector2;
import ui.*;
import ui.StatusScreen.StatusScreenType;



/**
 * This is a functional API for the game Corrupted.
 * It is designed for use with indroductory computer science lessons.
 * It is intended to help teach: method structure, parameters, conditionals, and for loops.
 * It could be extended to teach arrays.
 * It does not require the use of objects by the students.
 * 
 * Note:  To allow students to personalize the tiles, have them replace the pngs in the resource folder.
 *        They will need to replace: red.png, green.png, yellow.png, blue.png, light_blue.png, purple.png.
 *        The size of these pictures in the game is 72 x 72 pixels
 * 
 * @author Rachel Horton
 * 
 */
public class CorruptedFunctionalAPI extends Game
{
    
    //TileLogic object is required to perform any methods on tiles
    TileLogic tileHelper = new TileLogic(this);
    
    //Contains all the colors available for tiles
    String[] colors = new String[6];
    
    IntVector newTilePosition;
    private RowMeter counter;
    private ExteriorLayer TilesAbove;
    private TextObject rowsRemainingText;
    private boolean pauseLine = true;
    public ArrayList<IntVector> matchedTiles; 
    
    //For the oounter (used in win/lose conditions)
    public int tilesMissed= 0;    
    public int tilesMatched= 0;
    public int tilesNeededToWin = 60;
    public int tilesInMatchSet = 3;
   
   //Win screen
    private StatusScreen mWinScreen;
    
    //Should elements be drawn on the screen
    public boolean gameWon = false;
    public boolean hasCounter = false;
    public boolean needColorMap = false;
    public boolean shouldDrawMeter = false;
    public boolean shouldDrawLaser = false;

    

    /**
     * To be overridden by the student.  This method should be used to "build the world" of the game by drawing key elements.
     * To create a full game, this method should:
     *      Place all major elements onto the screen for the game. 
     *           -Laser
     *           -Grid of tiles
     *           -Counter
     *      Start a timer for the tiles to shift across the screen.
     */
    public void buildGame()
    {
      drawLaser();
      drawBoard();  
      drawCounter();      
      startTimerForTiles();
    }

     /**
     * To be overridden by the student.  This method should be used to "update the world" of the game by checking for user input and win/lose conditions.
     * To create a full game, this method should handle:
     *      -Checking for win/lose state
     *      -Checking if player presses Up, Down, Left, or Right
     *      -Perform tasks depending on input
     *      -Update the counter
     *      -Delete missed tiles (those that have gone too far left on the screen to be playable)
     */
    public void updateGame()
    {
        
       checkGameWon();
       
        if(pressingUp())
        {
            moveLaserUp();
        }
        else if(pressingDown())
        {
            moveLaserDown();
        }
        else if (pressingRight())
        {
            drawNewTileFromLaser();
            int matches = getNumberOfMatchingTiles();
            if(matches >= 3)
            {
                deleteMatchingTiles();
            }
            setNewLaserColor();
        }
    
        if(hasCounter == true)
        {
            updateCounter();
            if(isTimeToShift() == true)
            {
                shiftTilesLeft();
            }
        }
       
        deleteMissedTiles();
        
    }
    

    /**
     * Updates the counter to show the remaining number of tiles that need to be matched to win the game.
     */
    public void updateCounter()
    {
        setCounterValue(tilesNeededToWin);
    }
    
    public void setCounterValue(int count)
    {
        counter.setValue(count);
        rowsRemainingText.setText(""+ tilesNeededToWin);
    }
    

    
   /**
    * Increments the shift timer.
    * @return true if the timer successfully incremented
    */
    protected boolean isTimeToShift()
    {
        return super.incrementShiftTimer();
    }
    

    /**
     * Draws the counter on the screen.
     */
    public void drawCounter()
    {
        shouldDrawMeter = true;
        TilesAbove = new ExteriorLayer(this, true);
        TilesAbove.addTileRows(10);
        Vector2 position = new Vector2(world.getPositionX() + world.getWidth() - (world.getHeight()*(1/10f)), world.getWorldPositionY()+world.getHeight()*(1/10f));
        Vector2  size = new Vector2(world.getHeight()*(1/5f),world.getHeight()*(1/5f));
        
        counter = new RowMeter(position, size, 40);
        counter.setValue(tilesNeededToWin);
        //rows remaining
        rowsRemainingText = new TextObject(""+tilesNeededToWin, 
                new Vector2(world.getHeight()/120f,-world.getHeight()/30f).add(position), 18, Color.WHITE, Color.BLACK);
        counter.setAutoDrawTo(false);
        counter.draw();
        rowsRemainingText.removeFromAutoDrawSet();
        hasCounter = true;
        
    }
    
    /**
     * Sets the game state to win.
     * Calling this method will trigger the win screen to appear.
     */
    public void winGame()
    {
        setGameWon(true);
    }
    
    
    
  /*
   ********************************************
   *         Methods to Draw Tiles            *
   ********************************************
  */
 
    /**
     * Draws a single tile on the screen
     */
    public void drawTile()
    {
        Random r1 = new Random();
        Random r2 = new Random();
        int rand1 = r1.nextInt(30);
        int rand2 = r2.nextInt(10);
        
        IntVector place = new IntVector(rand1,rand2);
        Tile tile = new Tile(GridElement.ColorEnum.BLUE, this);
        tileHelper.putTile(place, tile);       
        
    }
    
    /**
     * Draws a single tile on the screen
     * @param x the x coordinate position to place the tile.
     * @param y the y coordinate position to place the tile
     */ 
    public void drawTile( int x, int y)
    {
        IntVector place = new IntVector(x,y);
        Tile tile = new Tile (GridElement.ColorEnum.BLUE, this);
        tileHelper.putTile(place, tile);
    }
    
    /**
     * Draws a single tile on the screen
     * @param x the x coordinate position to place the tile.
     * @param y the y coordinate position to place the tile
     * @param color a String of the color of the new tile
     */
    public void drawTile(int x, int y, String color)
    {
        GridElement.ColorEnum c = getColor(color);
        IntVector place = new IntVector(x,y);
        Tile tile = new Tile (c, this);
        tileHelper.putTile(place, tile);
    }
    
    /**
     * Helper Method, not to be used by students.
     * Draws a single tile on the screen (using ColorEnums from old code)
     * @param x the x coordinate position to place the tile.
     * @param y the y coordinate position to place the tile
     * @param color a GridElement.ColorEnum of the color of the new tile
     */
    public void drawTile(int x, int y, GridElement.ColorEnum color)
    {
        IntVector place = new IntVector(x, y);
        Tile tile = new Tile(color, this);
        tileHelper.putTile(place, tile);        
    }
    
     /**
     * Places a new tile of the current laser color at the
     * appropriate place on the board.
     */
    public void drawNewTileFromLaser()
    {
        newTilePosition = placePlayerTile();
    }
    
    /**
     * Draws a column of randomly colored tiles.
     * @param x the x coordinate position to place the column
     */
    public void drawColumn(int x)
    {
        for(int i = 0; i < 10; i++)
        {
            drawTile(x, i, getRandomColor());   
        }        
    }
    
    
        /**
     * Creates a color map which is used to generate random tile colors, so it can draw a board.
     * Draws 26 columns of tiles on the screen to create a grid
     */
    public void drawBoard()
    {  
        for(int i = 0; i < 25; i++)
        {
            drawColumn(i+10);
        }
 
    }
    
    
      /*
   ********************************************
   *        Methods to Delete Tiles           *
   ********************************************
  */
    
     /**
     * Deletes any tiles that have shifted too far left on the screen to be playable.
     */
    public void deleteMissedTiles()
    {
        for(int x = 0; x <= 2; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                markTileForDelete(x, y);              
            }
        }
        
    }
    
    /**
     *  Helper method for deleting tiles. Students should not use this.
     *  Marks the tile at location x, y to be deleted on next update.
     *  @param x the x location of the tile.
     *  @param y the y location of the tile
     */
    public void markTileForDelete(int x, int y)
    {
        tileHelper.markForDelete(x, y);
    }
    
    /**
     * Marks the tiles in the array of matched tiles for deletion.
     * They will be deleted on the next update.
     * @param matches An ArrayList of the IntVector locations of matched tiles.
     */
    public void deleteMatchedTiles(ArrayList<IntVector> matches)
    {
        tileHelper.markForDelete(matches);
    }
    
    
    /**
     * Deletes all tiles marked for deletion
     */
     public void deleteMatchingTiles()
    {
        handlePlayerTileCollision();
        deleteAllMarked();
    }
    
    

    
   /*
   ********************************************
   *          Methods for Colors              *
   ********************************************
   */
  
     /**
     * Sets values for array of Strings of color names.
     * Used to help generate random tile colors.
     */
    public void initializeColorMap()
    {
        colors[0] = "blue";
        colors[1] = "light blue";
        colors[2] = "purple";
        colors[3] = "yellow";
        colors[4] = "red";
        colors[5] = "green";
    }

     /**
     * Generates a random color
     * @return a random color from the array of available tile colors in the game
     */
    public String getRandomColor()
    {
        if (!needColorMap)
        {
            initializeColorMap();
            needColorMap = true;
        }
        Random r = new Random();
        int random = r.nextInt(6);
        return colors[random];
    }
    
  
    
    /**
     * Translates a String of a color name to a ColorEnum so that
     * old code (on which this API is built) can understand it.
     * @param color String of a color name
     * @return ColorEnum that matches the requested color String.  If error, will return Cyan by default.
     */
    public GridElement.ColorEnum getColor(String color)
    {
        if(color.equalsIgnoreCase("red"))
        {
            return GridElement.ColorEnum.RED;   
        }
        else if(color.equalsIgnoreCase("blue"))
        {
            return GridElement.ColorEnum.BLUE;   
        }
        else if(color.equalsIgnoreCase("green"))
        {
            return GridElement.ColorEnum.GREEN;   
        }
        else if(color.equalsIgnoreCase("yellow"))
        {
            return GridElement.ColorEnum.YELLOW;   
        }
        
        else if(color.equalsIgnoreCase("purple"))
        {
            return GridElement.ColorEnum.MAGENTA;
        }
        //default color
        else 
        {
            return GridElement.ColorEnum.CYAN;   
        }
        
    }
    
   /*  
   ********************************************
   *         Methods for the Laser            *
   ********************************************
    */
   
    /**
     * Draws the player-controlled laser onto the screen.
     */
    public void drawLaser()
    {
        shouldDrawLaser = true;
        this.setPlayerVisibility(true);
    }
    
        /**
     * Moves the laser up.
     */
    public void moveLaserUp()
    {
        movePlayerUp();
    }
    
    /**
     * Moves the laser down.
     */
    public void moveLaserDown()
    {
        movePlayerDown();
    }
    

     /**
     * Sets the color of the laser to be a random color
     */
        public void setNewLaserColor()
    {
        setPlayerColorEnum(tileHelper.getRandomExistingColor());
    }
   /*
   ********************************************
   *       UI: Buttons Users Can Press        *
   ********************************************
   */
  
   /**
    * @return true if the user is pressing the up arrow
    */
    public boolean pressingUp()
    {
        return keyboard.isButtonTapped(KeyEvent.VK_UP);
    }
    
    /**
     * @return true if the user is pressing the down arrow
     */
    public boolean pressingDown()
    {
        return keyboard.isButtonTapped(KeyEvent.VK_DOWN);
    }
    
    /**
     * @return true if the user is pressing the left arrow
     */
    public boolean pressingLeft()
    {
        return keyboard.isButtonTapped(KeyEvent.VK_LEFT);
    }
    
    /**
     * @return true if the user is pressing the right arrow
     */
    public boolean pressingRight()
    {
        return keyboard.isButtonTapped(KeyEvent.VK_RIGHT);
    }
    
    /**
     * @return true if the user is pressing the space bar
     */
    public boolean pressingSpace()
    {
        return keyboard.isButtonTapped(KeyEvent.VK_SPACE);
    }
    
    
   /*
   ********************************************
   *                Gameplay                  *
   ********************************************
  */
 
    /**
     * Sets the number of tiles that have to be matched before they can be deleted.
     */
    public void setTilesInMatchSet(int num)
    {
        tilesInMatchSet = num;
    }
 
    /**
     * Sets the number of tiles that need to be matched to win.
     * @param num Number of tiles that need to be matched to win
     */
    public void setTilesToWin(int num)
    {
        tilesNeededToWin = num;
    }
 
     /**
      * Initialize the start timer for the tile shifts
      * Set to 300 by default.
      */
    public void startTimerForTiles()
    {
        shiftTimer = new TurnTimer(300);
    }
    
    /**
     * Initialize the start timer for the tile shifts.
     * @param seconds number of 1/100 seconds (approx) between shifts
     */
    public void startTimerForTiles(int seconds)
    {
        shiftTimer = new TurnTimer(seconds);
    }
    
        /**
     * Shifts all tiles left.
     */
    protected void shiftTilesLeft()
    {
        shiftGridsDown();
    }
 
        /**
     * @returns the number of matching tiles
     */
    public int getNumberOfMatchingTiles()
    {
        ArrayList<IntVector> matches = tileHelper.getContiguousTiles(newTilePosition);
        return matches.size();
    }
    
     /**
     * Sets the game won state to true or false.
     * @param a boolean of whether or not the game has been won
     */
    public void setGameWon(boolean a)
    {
        gameWon = a;
    }
 
    
            /**
     * Checks if the player has matched the required number of tiles to win.
     * If yes, displays the win screen.
     */
    public void checkGameWon()
    {
        if(tilesNeededToWin <= 0)
        {
            winGame();
        }        
    }
   /*
   ********************************************
   *             Helper Methods               *
   ********************************************
   */
    
  
    /**
     * The initialize method is overriding the initialize method in the original game.
     * In the API, it calls buildGame() which is what the students will be writing.
     */
    public void initialize()
    {
        buildGame();
    }
        
     /**
     * The update method is overriding the update method in the original game.
     * In the API, it calls updateGame() which is what the students will be writing.
     */
    public void update()
    {
        updateGame();
    }
    
           /**
     * Called when player places a tile.
     * Checks for matches and marks appropriate tiles for deletion.
     * 
     * @return the number of matches
     */
    public int handlePlayerTileCollision()
    {
        matchedTiles = getMatchedTileArray();
        if (matchedTiles.size() >= tilesInMatchSet)
        {
            deleteMatchedTiles(matchedTiles);
        }
        
        if(matchedTiles == null || matchedTiles.size() < tilesInMatchSet) return 0;
        tilesMatched += matchedTiles.size();
        tilesNeededToWin -= matchedTiles.size();
        return matchedTiles.size();
    }
    
    /**
     * @Override
     * Draws things in the correct order so they actually show up on the screen
     */
    public void draw(Graphics gfx) 
    {
        super.draw(gfx);
        if(!shouldDrawLaser)
        {
            setPlayerVisibility(false);
        }

        //meters
        if(shouldDrawMeter)
        {
            counter.draw();
            rowsRemainingText.draw();
        }
        
        if(gameWon)
        {
            mWinScreen = new StatusScreen(StatusScreenType.WIN);
            mWinScreen.setVisible(true);
            mWinScreen.draw();
        }

    }
    
        /**
     * Gets the ArrayList of IntVector locations of matched tiles.
     * @return an ArrayList of IntVector locations of matched tiles.
     */
    public ArrayList<IntVector> getMatchedTileArray()
    {
        return tileHelper.getContiguousTiles(newTilePosition);
    }
    
    /**
     * Helpelr method.
     * Places a new tile at the position the player is pointing the laser at.
     * @return the IntVector representing the position the tile was placed.
     */
    public IntVector placePlayerTile()
    {
        Tile tile = generatePlayerTile();
        IntVector pos = placePlayerTile(tile);
        return pos;
    }
    
    /**
     * Helper method.
     * Places a tile at the position the player is pointing the laser at.
     * @param the tile to be placed
     * @return the IntVector representing the position the tile was placed.
     */
    public IntVector placePlayerTile(Tile tile)
    {
        int maxColumn = tileHelper.getFurthestColumn();
        int x = (int)tile.getCenter().getX();
        int y = (int)tile.getCenter().getY();
        while(tileHelper.withinBounds(x+1, y) && 
              tileHelper.getElement(x+1,y) == null && 
              x < maxColumn)
           {
               x++;
           }
        
        IntVector location = new IntVector(x,y);
        tile.moveTo(true, location);
        tileHelper.putTile(new IntVector(x,y), tile);
        
        return location;   

    }
    
}
