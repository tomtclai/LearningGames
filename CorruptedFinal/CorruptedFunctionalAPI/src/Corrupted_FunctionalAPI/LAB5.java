package Corrupted_FunctionalAPI;
import Corrupted_FunctionalAPI.*;
/**
 * LAB 5: If/Else: Personalization
 * 
 * This is an example of a "Treasure Map" game, though students should be encouraged to make whatever
 * game they like with the skills they have.
 * 
 * Note:  To allow students to personalize the tiles, have them replace the pngs in the resource folder.
 *        They will need to replace: red.png, green.png, yellow.png, blue.png, light_blue.png, purple.png.
 *        The size of these pictures in the game is 72 x 72 pixels
 * 
 * @author Rachel Horton
 */
public class LAB5 extends CorruptedFunctionalAPI
{
    int x = 0;
    int y = 0;
    
    public void buildGame()
    {
        drawTile(0, 0, "yellow");
        drawTile(4, 4, "blue");
        drawTile(4, 5, "blue");
        drawTile(5, 4, "blue");
        drawTile(5, 5, "blue");
        
        drawTile(11, 7, "green");
        drawTile(11, 8, "green");
        drawTile(10, 7, "green");
        drawTile(10, 8, "green");
        
        drawTile(11, 1, "red");
        drawTile(11, 2, "red");
        drawTile(11, 3, "red");
        drawTile(12, 1, "red");
        drawTile(12, 2, "red");
        drawTile(12, 3, "red");
        
        drawTile(20, 8, "light blue");
        drawTile(20, 9, "light blue");
        drawTile(21, 8, "light blue");
        drawTile(21, 9, "light blue");
        drawTile(22, 8, "light blue");
        drawTile(22, 9, "light blue");
        
        drawTile(18, 3, "purple");
        drawTile(18, 4, "purple");
        drawTile(17, 3, "purple");
        drawTile(17, 4, "purple");
        
        System.out.println("Your mission: To find the TREASURE!");
        System.out.println("First you must travel AROUND Pink-a-Dink island.");
        System.out.println("Then you must travel THROUGH the orange mountains.");
        System.out.println("Next go north along the right side of the green jungle.");
        System.out.println("Be careful not to go in the jungle or the wild unicorns will eat you!");
        System.out.println("Turn left and circle around the Blue-Bottom Sea.");
        System.out.println("Then find your way to the light blue clouds where the treasure is located!");
        System.out.println("Be careful not to stray from these instructions.");
        System.out.println("If you do, the treasure will be lost to you forever!");
        
        
    }
    
    public void updateGame()
    {
        if(pressingRight())
        {
            x = x + 1;
            drawTile(x, y, "yellow");
        }
        else if(pressingLeft())
        {
            x = x - 1;
            drawTile(x, y, "yellow");
        }
        else if(pressingUp())
        {
            y = y + 1;
            drawTile(x, y, "yellow");
        }
        else if(pressingDown())
        {
            y = y - 1;
            drawTile(x, y, "yellow");
        }
    }
}
