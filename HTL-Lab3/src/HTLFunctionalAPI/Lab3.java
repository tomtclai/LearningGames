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
		if (pressingRight()) 
		{
			drawSpeedyWizard(19, 5); // x = 19, y = 5
		} 
		else if (pressingLeft()) 
		{
			drawSpeedyWizard(0, 5); // x = 0, y = 5
		} 
		else if (pressingUp())
		{
			drawMedicWizard(10, 9); // x = 10, y = 9
		} 
		else if (pressingDown()) 
		{
			drawMedicWizard(10, 0); // x = 10, y = 0
		}
	}
}
