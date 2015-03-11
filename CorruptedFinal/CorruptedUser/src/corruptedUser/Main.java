package corruptedUser;
import Engine.GameWindow;
import corrupted.*;
import gridElements.*;
import structures.*;
@SuppressWarnings("serial")
public class Main extends GameWindow
{
	public Main()
	{
		setRunner(new UserCodeCorruptedFull());
		//setRunner(new UserCodeOneRowMatchThree());
		//setRunner(new UserCodeSwapper());
		//setRunner(new UserCodeLowResPaint());
		//setRunner(new ALoops());
		//setRunner(new BDirectionalTraversal());
		//setRunner(new CPatternedAccess());
		//setRunner(new DLinearSearch());
		//setRunner(new E2DConditionalCountRemoval());
		//setRunner(new ());
		//setRunner(new ());
		//setRunner(new ());
		//setRunner(new ());
	}
	
	public static void main(String[] args)
	{
		(new Main()).startProgram();
	}
}
