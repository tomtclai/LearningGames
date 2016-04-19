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
       if(pressingRight())
       {
	   drawMedicWizard(20,5);
       }
       else if(pressingLeft())
       {
	   drawMedicWizard(1,5);
       }
       else if (pressingUp())
       {
	   drawMedicWizard(8,8);
       }
       else if (pressingDown())
       {
	   drawMedicWizard(1,1);
       }
	}
}
