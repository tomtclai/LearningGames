package Corrupted_FunctionalAPI;

import java.awt.*;
import javax.swing.*;
import Engine.GameWindow;
import corrupted.Game;

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
      /*
       * This is the only code that will be modified in this file; 
       * comment and uncomment TODOs as you work on them, leaving only one active at a time
       */
	  
      //setRunner(new LAB1());
      //setRunner(new LAB2());
      //setRunner(new LAB3());
      //setRunner(new LAB4());
      //setRunner(new LAB5());
      //setRunner(new LAB6());
      //setRunner(new LAB7());
      //setRunner(new LAB8());
      //setRunner(new LAB9());
      setRunner(new LAB10());
      
	  //setRunner(new MyGame());
	  //setRunner(new MethodsToOverride());

  }

  public static void main(String[] args)
  {
	   
	  (new Main()).startProgram();
  }
}
