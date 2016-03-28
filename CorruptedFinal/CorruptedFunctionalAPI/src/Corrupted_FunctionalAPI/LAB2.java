package Corrupted_FunctionalAPI;
import Corrupted_FunctionalAPI.*;
/**
 * LAB 2: Drawing Tiles with Parameters
 * 
 * @author Rachel Horton
 */
public class LAB2 extends CorruptedFunctionalAPI
{
    public void buildGame()
    {
        // Draw a few tiles at specific locations
        drawTile(3,5);
        drawTile(10,10);
        drawTile(7,1);
        
        // Determine the size of the screen
        drawTile(0, 0);
        drawTile(25, 0);
        drawTile(0,9);
        drawTile(25,9);
        
        // Experiment with colors
        
        drawTile(1, 1, "red");
        drawTile(2,2, "purple");
        drawTile(3,3, "yellow");
        drawTile(4,4, "blue");
        drawTile(5,5, "light blue");
        drawTile(6,6, "green");
    }
    
}
