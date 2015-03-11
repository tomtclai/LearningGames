import java.io.IOException;
import java.util.Scanner;

import Engine.GameWindow;
import SpaceSmasher.SpaceSmasher;

@SuppressWarnings("serial")
public class Main extends GameWindow
{
  public Main()
  {
    setRunner(new UserCode());
  }

  /*
  public static void main(String[] args)
  {
	  Scanner s = new Scanner(System.in);
	  boolean quit = false;
	  do {
		  System.out.print("Enter>? ");
		  String str = s.nextLine();
		  quit = str.matches("q");
		  if (!quit) {
			  System.out.print("Another one");
			  (new Main()).startProgram();
		  }
		  	  
	  } while (!quit);
	  
	  System.out.println("Done!");
    
  } */
  
  public static void main(String[] args)
  {
	  // SpaceSmasher.SetShowFlashScreen(false);  // switch off the initial showing of Flash Screen
	  
	  (new Main()).startProgram();
  }
	
}
