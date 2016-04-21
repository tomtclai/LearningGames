package HTLFunctionalAPI;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab3 extends HTLFunctionalAPI
{
	public void buildWorld()
	{

	}
	public void updateWorld()
	{
		// TODO: fix this so they don't look like colored boxes
		if (keyboardIsPressingRight()) 
		{
			drawSpeedyWizard(19, 5); // x = 19, y = 5
		} 
		else if (keyboardIsPressingLeft()) 
		{
			drawSpeedyWizard(0, 5); // x = 0, y = 5
		} 
		else if (keyboardIsPressingUp())
		{
			drawMedicWizard(10, 9); // x = 10, y = 9
		} 
		else if (keyboardIsPressingDown()) 
		{
			drawMedicWizard(10, 0); // x = 10, y = 0
		}
	}
}
