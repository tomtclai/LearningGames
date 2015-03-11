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
 * Explores code to get and set different pieces of the gameState GameState
 * @author Michael Letter
 */
namespace user{
    public class APIExample1 : GhostFinderInterface {

	    public override void initialize() {
		    //Health
		    //Displayed as the number of hearts in the upper right corner of the screen
		    //Represented by an int greater than zero
		    //Note, at this point if the int gets too larger (around 20) the number of hearts will extend off screan
		    gameState.setHealth(5);						//Set Health
		    Console.Out.WriteLine(gameState.getHealth());	//Get Health
		
		    //Score
		    //Displayed as a number in the upper left corner of the screen
		    //Represented by an int greater than zero
		    gameState.setScore(9001);					//Set Score
            Console.Out.WriteLine(gameState.getScore());	//Get Score
		
		    //Power
		    //Displayed as a green bar in the lower left corner of the screen
		    //Represented as a float between 0 and 1. 1 results in the bar being filled completely and zero results in the bar bing completely empty
		    gameState.setLightPower(0.45f);					//Set Power
		    Console.Out.WriteLine(gameState.getLightPower());	//Get Power	
	    }
	    public override void update() {
		
	    }
	    public override void end() {
		    // TODO Auto-generated method stub
		
	    }
    }
}