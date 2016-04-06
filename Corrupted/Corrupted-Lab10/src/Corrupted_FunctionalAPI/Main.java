package Corrupted_FunctionalAPI;


import Engine.GameWindow;

/**
 * Class Main - write a description of the class here
 * 
 * @author (your name) 
 * @version (a version number)
 */
@SuppressWarnings("serial")
public class Main extends GameWindow
{
  public Main()
  {
      setRunner(new LAB10());
  }

  public static void main(String[] args)
  {
	   
	  (new Main()).startProgram();
  }
}
