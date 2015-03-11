//TODO convert summaries to Java version
package LinxLibrary;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

public class BallSet {

	// Only one of these collections will be active at a time,
    // however they are all in one class to simplify things for
    // the user, so a BallSet is simply "balls"
    private List<Ball> allBalls = null;
    private BallsInFlight shotBalls = null;
    private BallChainOnPath mainChain = null;

    /// <summary>
    /// Creates an empty collection of balls.
    /// </summary>
    public BallSet() 
    { 
    	allBalls = new Vector<Ball>();
    }


    public BallSet(BallChainOnPath chain)
    {
        mainChain = chain;
    }


    public BallSet(BallsInFlight inflight)
    {
        allBalls = inflight;
    }       

    /// <summary>
    /// Adds the ball to the collection.
    /// </summary>
    /// <param name="ball"></param>
    public void add(Ball ball)
    {
        allBalls.add(ball);
    }

    /// <summary>
    /// Get the ball at ballPosition, or null for a bad ballPosition.
    /// </summary>
    /// <param name="ballPosition"></param>
    /// <returns></returns>
    public Ball get(int ballPosition)
    {
        if (shotBalls != null)
        {
            if (ballPosition > -1 && ballPosition < shotBalls.size())
                return shotBalls.get(ballPosition);
        }
        else if (mainChain != null)
        {
            if (ballPosition > -1 && ballPosition < mainChain.size())
                return mainChain.get(ballPosition);
        }
        else
        {
            if (ballPosition > -1 && ballPosition < allBalls.size())
                return allBalls.get(ballPosition);
        }

        return null;
    }
            
    /// <summary>
    /// Gets the total number of balls.
    /// </summary>
    /// <returns></returns>
    public int getCount()
    {
        if (shotBalls != null)
            return shotBalls.size();
        else if (mainChain != null)
            return mainChain.size();
        else
            return allBalls.size();
    }


    public void insertBall(int ballPosition, LinxBase.BallColor color)
    {
        if (mainChain != null)
        {
            // valid position
            if (ballPosition > -1 && ballPosition < mainChain.size())
            {
                mainChain.AddNewBallAfter(ballPosition, color);
            }
        }
    }

    /// <summary>
    /// Removes the ball at ballPosition from the game and collection
    /// </summary>
    /// <param name="ballPosition"></param>
    public void remove(int ballPosition)
    {
        // check if this is a chain ballset
        if (mainChain != null)
        {
            // valid position
            if (ballPosition > -1 && ballPosition < mainChain.size())
            {
                // destroy and remove
            	Ball b = mainChain.remove(ballPosition);
            	b.destroy();
            }
        }
        else if (shotBalls != null)
        {
            // valid position
            if (ballPosition > -1 && ballPosition < shotBalls.size())
            {
                // destroy and remove
            	Ball b = shotBalls.remove(ballPosition);
                b.destroy();
            }
        }
        else
        {
            // use the correct list of balls
            List<Ball> balls;
            if (shotBalls != null)
                balls = shotBalls;
            else
                balls = allBalls;

            if (ballPosition > -1 && ballPosition < balls.size())
            {
            	Ball b = balls.remove(ballPosition);
            	b.destroy();
            }
        }
    }

    /// <summary>
    /// Clears all balls in the collection from the game and empties the collection.
    /// </summary>
    public void clear()
    {
        if (allBalls != null) {
	    	for (int i = 0; i < allBalls.size(); i++)
	        {
	            allBalls.get(i).destroy();
	        }
	        allBalls.clear();
        }
    }


    public LinxBase.BallColor getBallColor(int ballPosition)
    {
        if (shotBalls != null)
        {
            if (ballPosition > -1 && ballPosition < shotBalls.size())
                return shotBalls.get(ballPosition).getBallColor();
        }
        else if (mainChain != null)
        {
            if (ballPosition > -1 && ballPosition < mainChain.size())
                return mainChain.get(ballPosition).getBallColor();
        }
        else
        {
            if (ballPosition > -1 && ballPosition < allBalls.size())
                return allBalls.get(ballPosition).getBallColor();
        }
        return LinxBase.BallColor.Blue;
    }

    /// <summary>
    /// Set the color code of the ball at ballPosition to color
    /// </summary>
    /// <param name="ballPosition"></param>
    /// <param name="color"></param>
    public void changeBallColor(int ballPosition, LinxBase.BallColor color)
    {
        if (shotBalls != null)
        {
            if (ballPosition > -1 && ballPosition < shotBalls.size())
                shotBalls.get(ballPosition).setBallColor(color);
        }
        else if (mainChain != null)
        {
            if (ballPosition > -1 && ballPosition < mainChain.size())
                mainChain.get(ballPosition).setBallColor(color);
        }
        else
        {
            if (ballPosition > -1 && ballPosition < allBalls.size())
                allBalls.get(ballPosition).setBallColor(color);
        }
    }
}
