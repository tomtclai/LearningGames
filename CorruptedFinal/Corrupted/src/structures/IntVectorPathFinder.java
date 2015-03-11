package structures;
import java.util.ArrayList;

public class IntVectorPathFinder extends IntVector
{
	public IntVectorPathFinder previous = null;
	
	public IntVectorPathFinder(int x, int y, IntVectorPathFinder prev)
	{
		super(x,y);

		previous = prev;
	}
}
