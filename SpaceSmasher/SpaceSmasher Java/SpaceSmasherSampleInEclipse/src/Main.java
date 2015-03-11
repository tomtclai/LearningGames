import Engine.GameWindow;
import SpaceSmasher.SpaceSmasher;

@SuppressWarnings("serial")
public class Main extends GameWindow
{
  public Main()
  {
    setRunner(new MySpaceSmasherGame());
  }

  public static void main(String[] args)
  {
	   SpaceSmasher.SetShowFlashScreen(false);  
	  		// switch off the initial showing of Flash Screen
	  		// quicker debugging cycle: advised for during debugging
	  
	  (new Main()).startProgram();
    
  }
}
