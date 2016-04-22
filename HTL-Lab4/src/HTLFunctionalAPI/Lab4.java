package HTLFunctionalAPI;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab4 extends HTLFunctionalAPI
{
	
    //Declare variables for the start position of the "player tile"
    int x = 0;
    int y = 0;
    
	public void buildWorld()
	{
        // Player tile start
		drawSpeedyWizard(0,0);
    
        // Build the maze board!
        drawMedicWizard(1,1);
        drawMedicWizard(2, 1);
        drawMedicWizard(3, 1);
        drawMedicWizard(4, 1);
        drawMedicWizard(5, 1);
        drawMedicWizard(6, 1);
        drawMedicWizard(7, 1);
        drawMedicWizard(8, 1);
        drawMedicWizard(18, 1);
        drawMedicWizard(19, 1);
        drawMedicWizard(20, 1);
        
        drawMedicWizard(1, 2);
        drawMedicWizard(2, 2);
        drawMedicWizard(3, 2);
        drawMedicWizard(4, 2);
        drawMedicWizard(5, 2);
        drawMedicWizard(6, 2);
        drawMedicWizard(7, 2);
        drawMedicWizard(8, 2);
        drawMedicWizard(10, 2);
        drawMedicWizard(11, 2);
        drawMedicWizard(12, 2);
        drawMedicWizard(13, 2);
        drawMedicWizard(14, 2);
        drawMedicWizard(15, 2);
        drawMedicWizard(16, 2);

        
        drawMedicWizard(5, 3);
        drawMedicWizard(8, 3);
        drawMedicWizard(10, 3);
        drawMedicWizard(11, 3);
        drawMedicWizard(12, 3);
        drawMedicWizard(13, 3);
        drawMedicWizard(14, 3);
        drawMedicWizard(15, 3);
        drawMedicWizard(16, 3);
        drawMedicWizard(18, 3);
        drawMedicWizard(19, 3);
        drawMedicWizard(20, 3);

        drawMedicWizard(0, 4);
        drawMedicWizard(2, 4);
        drawMedicWizard(3, 4);
        drawMedicWizard(5, 4);
        drawMedicWizard(6, 4);
        drawMedicWizard(8, 4);
        drawMedicWizard(10, 4);
        drawMedicWizard(11, 4);
        drawMedicWizard(12, 4);
        drawMedicWizard(13, 4);
        drawMedicWizard(14, 4);
        drawMedicWizard(15, 4);
        drawMedicWizard(16, 4);
        drawMedicWizard(18, 4);
        drawMedicWizard(19, 4);
        drawMedicWizard(20, 4);
        
        drawMedicWizard(0, 5);
        drawMedicWizard(2, 5);
        drawMedicWizard(3, 5);
        drawMedicWizard(5, 5);
        drawMedicWizard(6, 5);
        drawMedicWizard(8, 5);
        drawMedicWizard(16, 5);
        drawMedicWizard(18, 5);
        drawMedicWizard(19, 5);
        drawMedicWizard(20, 5);
        
        drawMedicWizard(0, 6);
        drawMedicWizard(5, 6);
        drawMedicWizard(6, 6);
        drawMedicWizard(8, 6);
        drawMedicWizard(9, 6);
        drawMedicWizard(10, 6);
        drawMedicWizard(11, 6);
        drawMedicWizard(12, 6);
        drawMedicWizard(13, 6);
        drawMedicWizard(14, 6);
        drawMedicWizard(16, 6);
        drawMedicWizard(18, 6);
        drawMedicWizard(19, 6);
        drawMedicWizard(20, 6);
        
        drawMedicWizard(0, 7);
        drawMedicWizard(1, 7);
        drawMedicWizard(2, 7);
        drawMedicWizard(3, 7);
        drawMedicWizard(5, 7);
        drawMedicWizard(6, 7);
        drawMedicWizard(8, 7);
        drawMedicWizard(9, 7);
        drawMedicWizard(10, 7);
        drawMedicWizard(11, 7);
        drawMedicWizard(12, 7);
        drawMedicWizard(13, 7);
        drawMedicWizard(14, 7);
        drawMedicWizard(16, 7);
        drawMedicWizard(18, 7);
        drawMedicWizard(19, 7);
        drawMedicWizard(20, 7);
       
        drawMedicWizard(0, 8);
        drawMedicWizard(1, 8);
        drawMedicWizard(2, 8);
        drawMedicWizard(3, 8);
        drawMedicWizard(16, 8);
        drawMedicWizard(18, 8);
        drawMedicWizard(19, 8);
        drawMedicWizard(20, 8);
        
        drawMedicWizard(0, 9);
        drawMedicWizard(1, 9);
        drawMedicWizard(2, 9);
        drawMedicWizard(3, 9);
        drawMedicWizard(5, 9);
        drawMedicWizard(6, 9);
        drawMedicWizard(7, 9);
        drawMedicWizard(8, 9);
        drawMedicWizard(9, 9);
        drawMedicWizard(10, 9);
        drawMedicWizard(11, 9);
        drawMedicWizard(12, 9);
        drawMedicWizard(13, 9);
        drawMedicWizard(14, 9);
        drawMedicWizard(15, 9);
        drawMedicWizard(16, 9);


	}
	
	public void updateWorld()
	{
		super.updateWorld();
        if(keyboardIsPressingRight())
        {
            x = x + 1;
            drawSpeedyWizard(x, y);
        }
        else if(keyboardIsPressingLeft())
        {
            x = x - 1;
            drawSpeedyWizard(x, y);
        }
        else if(keyboardIsPressingUp())
        {
            y = y + 1;
            drawSpeedyWizard(x, y);
        }
        else if(keyboardIsPressingDown())
        {
            y = y - 1;
            drawSpeedyWizard(x, y);
        }
	}
}
