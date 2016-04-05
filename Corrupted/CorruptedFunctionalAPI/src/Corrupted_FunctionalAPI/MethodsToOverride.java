package Corrupted_FunctionalAPI;
import Corrupted_FunctionalAPI.*;

/**
 * Methods that can be overwritten.
 * This includes the method and what it should look like after it is overwritten.
 */
public class MethodsToOverride extends CorruptedFunctionalAPI
{
    
    public void updateCounter()
    {
        setCounterValue(tilesNeededToWin);
    }
    
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

    public void checkGameWon()
    {
        if(tilesNeededToWin <= 0)
        {
            winGame();
        }        
    }
    
    // Can also be overwritten without a for loop.  In that case, the student
    // could specify what color they want at each position.
    public void drawColumn(int x)
    {
        for(int i = 0; i < 10; i++)
        {
            drawTile(x, i, getRandomColor());   
        }        
    }
    
    // This can be overwritten using drawTile(), drawTile(int x, int y), 
    // drawTile(String color) or drawColumn(int xPosition).
    public void drawBoard()
    {  
        for(int i = 0; i < 25; i++)
        {
            drawColumn(i+10);
        }
 
    }
    
    public void setTilesToWin(int num)
    {
        tilesNeededToWin = num;
    }
    
    public void setGameWon(boolean a)
    {
        gameWon = a;
    }
    

    
    // This method uses objects and arrays, so I probably won't use it with the 
    // middle school students.
    // Might be useful for college classes though.
    public int handlePlayerTileCollision()
    {
        matchedTiles = getMatchedTileArray();
        if (matchedTiles.size() >= 3)
        {
            deleteMatchedTiles(matchedTiles);
        }
        
        if(matchedTiles == null || matchedTiles.size() <3) return 0;
        tilesMatched += matchedTiles.size();
        tilesNeededToWin -= matchedTiles.size();
        return matchedTiles.size();
    }
}
