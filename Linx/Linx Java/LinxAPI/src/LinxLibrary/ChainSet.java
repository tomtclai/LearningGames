package LinxLibrary;

import java.awt.Color;
import java.util.List;

import Engine.Vector2;
import LinxLibrary.LinxBase.Power;

/**
 * A set of BallChainOnPath, used to control aspects of the chains as a whole.
 * Controls things like speed, spawning balls, splitting and joining chains.
 * Also creates a Cursor that is used when a shot ball collides with a chain.
 * @author Brian
 */
public class ChainSet {
    private List<BallChainOnPath> mChains;
    private BallsInFlight mShotBalls;    
    private Path mPath;
    
    private float mSpeed = BallOnPath.DEFAULT_SPEED;
    private float mSpeedTimer = 0;

    private int mBallsSpawned;
    private Vector2 mSpawnPoint;
    
    // max ball that will be spawned
    private int mMaxBalls = 40;

    /**
     * Constructor to initialize ChainSet with chains and shotBalls.
     * @param chains All the chains to be controlled by this ChainSet
     * @param shotBalls The BallsInFlight to check for collisions with chains
     */
    public ChainSet(List<BallChainOnPath> chains, BallsInFlight shotBalls)
    {
    	if (chains.size() < 1)
    		System.err.println("Chains must have at least 1 chain in the list");
        mChains = chains;
        mShotBalls = shotBalls;
        mPath = mChains.get(0).getPath();
        mSpawnPoint = mPath.getPoint(0);
    }
    
    /**
     * Sets the max number of balls that will be spawned on the path. Input parameter of less than 0 is ignored.
     * @param max The max number of balls to be spawned. 
     */
    public void setMaxBalls(int max) {
    	if (max > 0)
    		mMaxBalls = max;
    }
   
    /**
     * Sets the speed of all chains in this set
     * @param speed the new speed for all chains
     */
    public void setSpeed(float speed)
    {
        // set the speed
        mSpeed = speed;
        // set timer to 0 in case it was running
        mSpeedTimer = 0;
        for (int i = 0; i < mChains.size(); i++)
        {
            mChains.get(i).setChainSpeed(speed);
        }
    }
    /**
     * Sets the speed of all chains until time has elapsed
     * @param speed the new speed for all chains
     * @param time number of seconds for the speed change to last
     */
    public void setSpeed(float speed, float time)
    {
        if (time > 0)
        {
            mSpeed = speed;
            mSpeedTimer = time;
            for (int i = 0; i < mChains.size(); i++)
            {
                mChains.get(i).setChainSpeed(speed);
            }
        }
    }

    /**
     * Get the timer left for the current speed
     * @return seconds left
     */    
    public float getSpeedTimer()
    {
        return mSpeedTimer;
    }

    /**
     * Get the speed of all chains in this set
     * @return speed
     */
    public float getSpeed()
    {
        return mSpeed;
    }

    /**
     * Checks if "iShotBall" is colliding with "iChain", and returns a valid
     * Cursor object for "iChain" if there is a collision.
     * @param iShotBall index of turret's shot ball to check
     * @param iChain index of chain to check all chain's balls
     * @return Cursor with collision information, if there was a collision
     */
    public Cursor isShotBallCollidingWithChainBall(int iShotBall, int iChain)
    {
        if (mShotBalls.size() > 0 && mChains.size() > 0)
        {
            for (int i = 0; i < mChains.get(iChain).size(); i++)
            {
                if (mShotBalls.get(iShotBall).pixelTouches((mChains.get(iChain).get(i))))
                {                        
                    Cursor cursor = new Cursor(iShotBall, mShotBalls, i, mChains.get(iChain));
                    return cursor;
                }
            }
        }
        return new Cursor();
    }


    /**
     * Splits the chain "iChain", putting all balls behind "iCursor" onto a
     * new chain behind "iChain"
     * @param iChain the index of the chain to split
     * @param iCursor the index of the ball to split behind
     * @return true if successful
     */
    public boolean splitChain(int iChain, int iCursor)
    {
        if (iChain >= 0 && iCursor > 0) // can't be first ball
            if (iChain < mChains.size())
                if (iCursor < mChains.get(iChain).size())
                {
                    BallChainOnPath currentChain = mChains.get(iChain);
                    BallChainOnPath newChain = new BallChainOnPath(mChains.get(iChain).getPath());
                    // add all balls from iCursor and back, to the new chain
                    for (int i = iCursor; i < currentChain.size(); i++)
                    {
                        newChain.add(currentChain.get(i));
                    }
                    for (int i = 0; i < newChain.size(); i++)
                    {
                        currentChain.remove(newChain.get(i));
                    }
                    // insert the new chain directly behind the original
                    mChains.add(iChain + 1, newChain);
                    // new chain needs to move like old chain, and old chain should stop
                    newChain.setChainDirection(currentChain.getDirection());
                    // only move the new chain if it is the last chain                        
                    newChain.setChainToMovement(iChain + 1 == mChains.size() - 1);
                    // always stop the front chain
                    currentChain.setChainToMovement(false);
                    return true;
                }
        return false;
    }

    /**
     * Checks for collision between the first and last balls of "iChain1" and "iChain2",
     * and adds the rear chain to the front chain if there is a collision. Returns a
     * valid Cursor for the front chain if the chains joined.
     * @param iChain1 the front (closer to zero) chain index 
     * @param iChain2 the rear chain index
     * @return Cursor for the newly formed chain, if formed
     */
    public Cursor joinChains(int iChain1, int iChain2)
    {
        if (iChain1 < mChains.size() && iChain2 < mChains.size() && iChain1 < iChain2)
        {
            if (iChain1 >= 0 && iChain2 > 0)
            {
                // last ball in the front chain collides with first ball in the back chain
                BallChainOnPath front = mChains.get(iChain1);
                BallChainOnPath back = mChains.get(iChain2);
                if (front.size() > 0 && back.size() > 0)
                    if (!front.getLast().isRemoved() && !back.getFirst().isRemoved())
                    {
                        if (front.getLast().pixelTouches((back.getFirst())))
                        {
                            Cursor cursor = new Cursor(-1, null, front.size() - 1, front);
                            front.addAll(back);
                            front.setChainDirection(Path.PathTravelDirection.ePathTravelForward);
                            front.setChainToMovement(back.isMoving());
                            back.clear();
                            mChains.remove(iChain2);
                            return cursor;
                        }
                    }
            }
        }
        return new Cursor();
    }

    /**
     * Makes the front chain move backwards if its rear ball color matches the
     * front ball color of the back chain
     * @param iFrontChain the front chain index (closer to zero) to check
     * @param iBackChain the rear chain index to check
     * @return true if successful
     */
    public boolean magnetizeChains(int iFrontChain, int iBackChain)
    {
        if (iFrontChain < mChains.size() && iBackChain < mChains.size() && iFrontChain >= 0 && iBackChain > 0 && iFrontChain < iBackChain)
        {
            if (mChains.get(iFrontChain).size() > 0 && mChains.get(iBackChain).size() > 0)
            {
                if (mChains.get(iFrontChain).getLast().getBallColor() == mChains.get(iBackChain).getFirst().getBallColor())
                {
                	mChains.get(iFrontChain).setChainDirection(Path.PathTravelDirection.ePathTravelReverse);
                	mChains.get(iFrontChain).setChainToMovement(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the chain with the position, chainPosition.
     * @param chainPosition index of the Chain to get
     * @return the Chain if index is valid, otherwise null.
     */
    public Chain get(int chainPosition)
    {
        if (chainPosition > -1 && chainPosition < mChains.size())
            return new Chain(mChains.get(chainPosition));
        else
            return null;
    }

    /**
     * Get the number of ball chains in the game.
     * @return number of chains.
     */
    public int getCount()
    {
        return mChains.size();
    }

    /**
     * Return the number of balls in the chain at chainPosition.
     * @param chainPosition index of chain to check
     * @return number of balls in the index chain
     */
    public int getBallCount(int chainPosition)
    {
        if (chainPosition > -1 && chainPosition < mChains.size())
            return mChains.get(chainPosition).size();
        else
            return 0; // or -1 because it's an error?
    }
   
    /**
     * Updates the movement of all the balls in the chain with position, 
     * chainPosition.
     * @param chainPosition index of the chain to be updated.
     */
    public void updateChainMovement(int chainPosition)
    {
        // make sure speed is not negative
        if (mSpeed < 0)
            mSpeed = Math.abs(mSpeed);

        // check for setting speed back to old speed
        if (mSpeedTimer > 0)
            mSpeedTimer -= .025f; //40 frames/sec     
        else if (mSpeedTimer < 0)
        {
            mSpeed = BallOnPath.DEFAULT_SPEED;
            mSpeedTimer = 0f;
        }

        mChains.get(chainPosition).setChainSpeed(mSpeed);
        mChains.get(chainPosition).updateMovement();
    }

    /**
     * Stops all chains movement entirely for 'seconds' seconds
     * @param seconds number of seconds to stop.
     */
    public void stopChains(float seconds)
    {
        if (seconds > 0)
        {
            mSpeed = 0;
            mSpeedTimer = seconds;

            for (int i = 0; i < mChains.size(); i++)
            {
                mChains.get(i).setChainSpeed(0);
            }
        }
    }

    /**
     * Makes the chain start moving forward, toward the ending point.
     * @param chainPosition index of the chain to move.
     */
    public void moveChainForward(int chainPosition)
    {
        if (chainPosition > -1 && chainPosition < mChains.size())
        {
            mChains.get(chainPosition).setChainDirection(Path.PathTravelDirection.ePathTravelForward);
            mChains.get(chainPosition).setChainToMovement(true);
        }
    }

    /**
     * Makes the chain start moving in reverse, toward the starting point.
     * @param chainPosition index of the chain to move.
     */
    public void moveChainBackward(int chainPosition)
    {
        if (chainPosition > -1 && chainPosition < mChains.size())
        {
        	mChains.get(chainPosition).setChainDirection(Path.PathTravelDirection.ePathTravelReverse);
        	mChains.get(chainPosition).setChainToMovement(true);
        }
    }

    /**
     * Makes the chain start moving in the opposite direction of
     * the most recent movement.
     * @param chainPosition index of the chain to reverse the direction 
     */
    public void reverseChain(int chainPosition)
    {
        if (chainPosition > -1 && chainPosition < mChains.size())
        {
        	mChains.get(chainPosition).reverseChainDirection();
        	mChains.get(chainPosition).setChainToMovement(true);
        }
    }
    
    private BallChainOnPath findChainToAdd()
    {
    	BallChainOnPath returnChain = null;
    	
        // get the furthest back chain, or make one
        if (mChains.size() > 0)
            returnChain = mChains.get(mChains.size() - 1);
        else
        {
            // use a new chain and ADD TO CHAINLIST
            returnChain = new BallChainOnPath(mPath);
            mChains.add(returnChain );
        }
        return returnChain;
    }
    
    /**
     * Spawns balls whenever possible until maxBalls (set by the setMaxBalls() function) balls have been spawned.
     * @return Returns the ball that has just been spawned, or null if maxBall has been reached
     */
    public BallOnPath spawnBall()
    {
    	BallOnPath returnBall = null;
    	if (mBallsSpawned < mMaxBalls)
    	{
            // get the furthest back chain, or make one
            BallChainOnPath chain = findChainToAdd();
            
            // just add a new ball if the chain is empty
            if (chain.size() == 0)
            {
                    returnBall = chain.AddNewBallToEnd();
                    ++mBallsSpawned;
            }
            else
            {
                // if distance from spawn point is at least a ball, add a new ball
                float distance =  Math.abs((mSpawnPoint.clone().sub(chain.get(chain.size() - 1).getCenter()).length()));
                if (distance > (chain.get(chain.size() - 1).getRadius() * 2))
                {
                    returnBall = chain.AddNewBallToEnd();
                    ++mBallsSpawned;
                }
            }
    	}
    	return returnBall;
    }
    
    /**
     * Spawns a ball of the specific color whenever possible until maxBalls (set by the setMaxBalls() function) balls have been spawned.
     * Returns the ball that has just been spawned.
     * @param c the color of the ball to be spawned.
     * @return returns the ball that has just be spawned, or null if maxBall has been reached.
     */
    public BallOnPath spawnBall(Color c) 
    {
    	BallOnPath b = spawnBall();
    	if (b != null)
    		b.setColor(c);
    	return b;
    }
    
    /**
     * Spawns a new powerball at the end. Returns the powerball that has just been spawned. Will fail if there are already maxBalls (set by the
     * setMaxBalls() function).
     * @param power the types of power up to be spawned.
     * @return returns the powerball that has been spawned, or null if maxBall has been reached
     */
    public BallOnPath spawnPowerBall(Power power)
    {
    	BallOnPath b = null;
    	if (mBallsSpawned < mMaxBalls) {
    	// get the furthest back chain, or make one
    		BallChainOnPath chain = findChainToAdd();
    		++mBallsSpawned;
    		b = chain.AddNewBallToEnd();
    		b.setPower(power);
    	}
    	return b;
    }    
}
