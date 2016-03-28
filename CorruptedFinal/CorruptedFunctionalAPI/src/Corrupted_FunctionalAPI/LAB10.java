package Corrupted_FunctionalAPI;
import Corrupted_FunctionalAPI.*;
/**
 * LAB 10: Cleanup the Game!
 * 
 * You should end up with a complete game at the end of this lab!
 * 
 * @author Rachel Horton
 */
public class LAB10 extends CorruptedFunctionalAPI
{
     public void buildGame()
    {
      for(int currentNum = 8; currentNum < 26; currentNum = currentNum + 1)
      {
          drawColumn(currentNum);
      }
      drawLaser();  
      drawCounter(); 
      setTilesInMatchSet(3);
      startTimerForTiles(200);
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
        else if (pressingRight())
        {
            drawNewTileFromLaser();
            int matches = getNumberOfMatchingTiles();
            if(matches >= tilesInMatchSet)
            {
                deleteMatchingTiles();
            }
            setNewLaserColor();
        }
        
        if(isTimeToShift() == true)
        {
             shiftTilesLeft();
        }
       
        
        updateCounter();        
        deleteMissedTiles();
        
    }
    
}
