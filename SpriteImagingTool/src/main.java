import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class main 
{
	public static void main(String[] args)
	{
		// Create Display
		final SpriteDisplayer displayInterface = new SpriteDisplayer();
		DisplayWindow display = new DisplayWindow(displayInterface);
		display.startProgram();
		
		
		// Create and run controls.
		try
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					SpriteControls controls = new SpriteControls(displayInterface);
				}
			});
		}
		catch(Exception e) {};
		
	}
}
