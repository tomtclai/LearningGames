package Corrupted_FunctionalAPI;
import Corrupted_FunctionalAPI.*;
/**
 * LAB 6: Lasers and Matching Tiles
 * 
 * @author Rachel Horton
 */
public class LAB6 extends CorruptedFunctionalAPI
{
    int matches = 0;
    
    public void buildGame()
    {
        drawLaser();
        
        // How many tiles need to be matched before they will delete?
        setTilesInMatchSet(3);
        
        //Build a board!  At least one column
        drawTile(7, 0, "light blue");
        drawTile(7, 1, "red");
        drawTile(7, 2, "green");
        drawTile(7, 3, "yellow");
        drawTile(7, 4, "blue");
        drawTile(7, 5, "light blue");
        drawTile(7, 6, "green");
        drawTile(7, 7, "purble");
        drawTile(7, 8, "blue");
        drawTile(7, 9, "red");
        
        drawTile(8, 0, "green");
        drawTile(8, 1, "red");
        drawTile(8, 2, "green");
        drawTile(8, 3, "blue");
        drawTile(8, 4, "yellow");
        drawTile(8, 5, "blue");
        drawTile(8, 6, "purple");
        drawTile(8, 7, "red");
        drawTile(8, 8, "green");
        drawTile(8, 9, "blue");
        
        drawTile(9, 0, "blue");
        drawTile(9, 1, "green");
        drawTile(9, 2, "red");
        drawTile(9, 3, "light blue");
        drawTile(9, 4, "green");
        drawTile(9, 5, "yellow");
        drawTile(9, 6, "purple");
        drawTile(9, 7, "light blue");
        drawTile(9, 8, "light blue");
        drawTile(9, 9, "green");
        
        drawTile(10, 0, "red");
        drawTile(10, 1, "blue");
        drawTile(10, 2, "red");
        drawTile(10, 3, "light blue");
        drawTile(10, 4, "yellow");
        drawTile(10, 5, "blue");
        drawTile(10, 6, "green");
        drawTile(10, 7, "yellow");
        drawTile(10, 8, "red");
        drawTile(10, 9, "blue");
    }
    
    public void updateGame()
    {
        //Handle player input
        if(pressingUp())
        {
            moveLaserUp();
        }
        else if(pressingDown())
        {
            moveLaserDown();
        }
        else if(pressingRight())
        {
            drawNewTileFromLaser();
            matches = getNumberOfMatchingTiles();
            if (matches >= tilesInMatchSet)
            {
                deleteMatchingTiles();
            }
            setNewLaserColor();
        }

    }
}
