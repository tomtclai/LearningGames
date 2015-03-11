using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using GhostFinder.Interface;
using GhostFinder.GhostEngine;
using CustomWindower.Driver;

/**
 * Example Main
 * @author Michael Letter
 */
namespace user{
    public class Program {
	    /**
	     * In order to get your code to run in GhostFinder you will need to extend GhostFinder.Interface.Interface
	     * and must define "public InteractableObject[][] initialize(InteractableObject[][] EnemyGrid)" and
	     * "public InteractableObject[][] update(InteractableObject[][] EnemyGrid)" these are the functions that will be called throughout the game
	     * when that class is ready pass it into this function
	     * After words call .startProgram();
	     * @param yourCode Your class Extending GhostFinder.Interface.Interface
	     */
	    public static void Main(String[] args){
            GhostFinder.GhostEngine.GhostFinder game = new GhostFinder.GhostEngine.GhostFinder();
            game.addLevel(new Level4(), "Level 4");
            game.addLevel(new UserCode(), "Level 5");

            game.addLevel(new APIExample1(), "API Example 1");
            game.addLevel(new APIExample2(), "API Example 2");
            game.addLevel(new APIExample3a(), "API Example 3a");
            game.addLevel(new APIExample4a(), "API Example 4a");
            game.addLevel(new APIExample3b(), "API Example 3b");
            game.addLevel(new APIExample4b(), "API Example 4b");
            game.addLevel(new APIExample5a(), "API Example 5a");
            game.addLevel(new APIExample5b(), "API Example 5b");
            game.addLevel(new APIExample6(), "API Example 6");
            game.addLevel(new APIExample7a(), "API Example 7a");
            game.addLevel(new APIExample7b(), "API Example 7b");
            
            game.start();
	    }
    }
}