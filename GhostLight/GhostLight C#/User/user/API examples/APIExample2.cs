using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using GhostFinder.Interface;
using GhostFinder.GhostEngine;
using CustomWindower.Driver;
using System.Windows.Forms;

/**
 * Keyboard and mouse input
 * @author Michael Letter
 */
namespace user{
    public class APIExample2 : GhostFinderInterface {

	    public override void initialize() {
		
	    }
	    public override void update() {
		    //isButtonDown == is button being pressed at this very moment
		    //isButtonTapped == has the button been presed and released lately
		
		    //Keybord input
		    //to ask about specific keys use KeyEvent enums
		    if(keyboard.isKeyDown(Keys.A)){
			    Console.Out.WriteLine('a');
		    }
		    if(keyboard.isKeyDown(Keys.Enter)){
			    Console.Out.WriteLine('\n');
		    }
		    //Mouse Input
		    if(mouse.isButtonDown(MouseState.MouseButton.LEFT)){
			    Console.Out.WriteLine("Left");
		    }
		    if(mouse.isButtonDown(MouseState.MouseButton.MIDDLE)){
			    Console.Out.WriteLine("Middle,");
		    }
		    if(mouse.isButtonDown(MouseState.MouseButton.RIGHT)){
			    Console.Out.WriteLine("Right");
		    }
		    //Button Input
		    if(clickableButtons.isButtonDown(OnScreenButtons.ScreenButton.LASERBUTTON)){
			    Console.Out.WriteLine("LAZER!!");
		    }
		    if(clickableButtons.isButtonDown(OnScreenButtons.ScreenButton.MEDIUMBUTTON)){
			    Console.Out.WriteLine("Medium Button");
		    }
		    if(clickableButtons.isButtonDown(OnScreenButtons.ScreenButton.WIDEBUTTON)){
                Console.Out.WriteLine("Wide Button");
		    }
		
	    }
	    public override void end() {
		    // TODO Auto-generated method stub
		
	    }
    }
}
