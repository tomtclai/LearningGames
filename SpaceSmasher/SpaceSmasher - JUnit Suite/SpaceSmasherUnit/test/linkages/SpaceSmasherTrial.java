package linkages;
import Engine.GameWindow;
import SpaceSmasher.SpaceSmasher;

/*
 * A simple runner testing the API, a static instance of this class 
 * is used throughout the test-cases. If a new API is introduced it will
 * have to be created as such.
 * 
 * @author Chris Sullivan 2014
 */

public class SpaceSmasherTrial extends GameWindow {

	public SpaceSmasherTrial() {
		setRunner(new Runnee());
		startProgram();
	}
	
	
	public class Runnee extends SpaceSmasher {

		@Override
		protected void initialize() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void update() {
			// TODO Auto-generated method stub
			
		}
		
	}

	
	
}
