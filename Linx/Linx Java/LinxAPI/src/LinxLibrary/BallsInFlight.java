package LinxLibrary;

import java.util.List;
import java.util.Vector;

class BallsInFlight extends Vector<Ball> {
	
	private static Vector<Ball> sRemovedBalls = new Vector<Ball>();
    public static void addToRemoveBalls(Ball b) {
    	sRemovedBalls.add(b);
    }
    public static void cleanRemovedBalls() {
    	for (int i = sRemovedBalls.size()-1; i >=0; i--)
        {
            sRemovedBalls.get(i).update();
            if (!sRemovedBalls.get(i).isAlive())
            {
                // remove it; count is lower
                Ball b = sRemovedBalls.remove(i);
                b.destroy();
            }
        }
    }

	public BallsInFlight() { }
	
	public void update()
	{
		for (int i = size() - 1; i >= 0; i--)
		{
			if (!get(i).isAlive())
			{
				Ball b = remove(i);
				b.destroy();
			}
		}
		
		for (int i = 0; i < size(); i++)
		{
			get(i).update();
		}
	}
	
	public void clear()
	{

		for (int i = size() - 1; i >= 0; i--)
		{
			Ball b = remove(i);
			b.destroy();
		}
		super.clear();

	}
}
