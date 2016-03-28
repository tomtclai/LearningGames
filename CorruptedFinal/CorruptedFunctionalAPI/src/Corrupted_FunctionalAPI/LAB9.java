package Corrupted_FunctionalAPI;
import Corrupted_FunctionalAPI.*;
/**
 * LAB 9: Win Conditions
 * 
 * Extension for students done early:  Create a drawBoard() method (should have the for loop in it).
 * @author Rachel Horton
 */
public class LAB9 extends CorruptedFunctionalAPI
{
    public void buildGame()
    {
        for(int currentNum = 8; currentNum < 25; currentNum = currentNum + 1)
        {
            drawColumn(currentNum);
        }
        
        drawLaser();
        drawCounter();
        setCounterValue(30); // Set number of tiles you need to match to win the game.
    }
   
    
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
        else if(pressingRight())
        {
            drawNewTileFromLaser();
            
            int matches;
            matches = getNumberOfMatchingTiles();
            if (matches >= tilesInMatchSet)
            {
                deleteMatchingTiles();
            }
            setNewLaserColor();
        }
        
        updateCounter();
    }
    
    public void checkGameWon()
    {
        if(tilesNeededToWin <= 0)
        {
            winGame();
        }        
    }
}
