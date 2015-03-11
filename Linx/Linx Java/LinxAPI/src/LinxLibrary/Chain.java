package LinxLibrary;

public class Chain {
	private BallChainOnPath mainChain;

    public Chain(BallChainOnPath chain)
    {
        mainChain = chain;
    }

    /// <summary>
    /// Return the number of balls in the chain
    /// </summary>
    public int getBallCount()
    {
        return mainChain.size();
    }

    /// <summary>
    /// Updates the movement of all the balls in the chain
    /// </summary>
    public void updateChainMovement()
    {
        mainChain.updateMovement();
    }

    /// <summary>
    /// Stops the chain's movement entirely
    /// </summary>
    public void stopChain()
    {
       mainChain.setChainToMovement(false);
    }

    /// <summary>
    /// Makes the chain start moving in its most recent direction
    /// </summary>
    public void moveChain()
    {
        mainChain.setChainToMovement(true);
    }

    /// <summary>
    /// Makes the chain start moving forward, toward the ending point.
    /// </summary>
    public void moveChainForward()
    {
        mainChain.setChainDirection(Path.PathTravelDirection.ePathTravelForward);
        mainChain.setChainToMovement(true);
    }

    /// <summary>
    /// Makes the chain start moving in reverse, toward the starting point.
    /// </summary>
    public void moveChainBackward()
    {
        mainChain.setChainDirection(Path.PathTravelDirection.ePathTravelReverse);
        mainChain.setChainToMovement(true);
    }

    /// <summary>
    /// Makes the chain start moving in the opposite direction of
    /// the most recent movement.
    /// </summary>
    public void reverseChainDirection()
    {
        mainChain.reverseChainDirection();
        mainChain.setChainToMovement(true);                        
    }

    /// <summary>
    /// Sets the speed of the whole chain to the given value
    /// </summary>
    /// <param name="speed"></param>
    public void setChainSpeed(float speed)
    {
        mainChain.setChainSpeed(speed);
    }

    /// <summary>
    /// Returns the current speed of the chain
    /// </summary>
    /// <returns></returns>
    public float getChainSpeed()
    {
        return mainChain.getChainSpeed();
    }

    /// <summary>
    /// Remove the ball from the chain, and destroy the ball to
    /// remove it from the game.
    /// </summary>
    /// <param name="ballPosition"></param>
    public void removeBall(int ballPosition)
    {
        if (ballPosition > -1 && ballPosition < mainChain.size())
        {
        	Ball b = mainChain.remove(ballPosition);
        	b.destroy();
        }
    }

    public BallOnPath getBall(int ballPosition)
    {
        if (ballPosition > -1 && ballPosition < mainChain.size())
            return mainChain.get(ballPosition);

        return null;
    }
}
