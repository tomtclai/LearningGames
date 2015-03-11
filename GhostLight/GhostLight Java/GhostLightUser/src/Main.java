import Engine.GameWindow;
import GhostLight.GhostEngine.GhostLight;
import Tutorials.*;

public class Main extends GameWindow{
	private static final long serialVersionUID = 1L;
	/**
	 * In order to get your code to run in GhostLight you will need to extend GhostLight.Interface.Interface
	 * and must define "public InteractableObject[][] initialize(InteractableObject[][] EnemyGrid)" and
	 * "public InteractableObject[][] update(InteractableObject[][] EnemyGrid)" these are the functions that will be called throughout the game
	 * when that class is ready pass it into this function
	 * After words call .startProgram();
	 * @param yourCode Your class Extending GhostLight.Interface.Interface
	 */
	public static void main(String[] args){
		(new Main()).startProgram();
	}
	public Main(){
		GhostLight game = new GhostLight();
//		game.addLevel(new Tutorial_01(), "Tutorial 01");
//        game.addLevel(new Tutorial_02(), "Tutorial 02");
//        game.addLevel(new Tutorial_03(), "Tutorial 03");
//        game.addLevel(new Tutorial_04(), "Tutorial 04");
//        game.addLevel(new Tutorial_05(), "Tutorial 05");
//        game.addLevel(new Tutorial_06(), "Tutorial 06");
//        game.addLevel(new Tutorial_07(), "Tutorial 07");
//        game.addLevel(new Tutorial_08(), "Tutorial 08");
//        game.addLevel(new Tutorial_09(), "Tutorial 09");
		//game.addLevel(new Level1(), "Level 1");
		//game.addLevel(new Level2(), "Level 2");
		//game.addLevel(new Level3(), "Level 3");
		game.addLevel(new UserCode(), "Level 4");
//		game.addLevel(new APIExample1(), "API Example 1");
//		game.addLevel(new APIExample2(), "API Example 2");
//		game.addLevel(new APIExample3a(), "API Example 3a");
//		game.addLevel(new APIExample4a(), "API Example 4a");
//		game.addLevel(new APIExample3b(), "API Example 3b");
//		game.addLevel(new APIExample4b(), "API Example 4b");
//		game.addLevel(new APIExample5a(), "API Example 5a");
//		game.addLevel(new APIExample5b(), "API Example 5b");
//		game.addLevel(new APIExample6(), "API Example 6");
		//game.addLevel(new APIExample7(), "API Example 7");
		setRunner(game);		
	}
}
