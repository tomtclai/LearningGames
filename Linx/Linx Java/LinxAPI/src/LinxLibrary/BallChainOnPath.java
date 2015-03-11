package LinxLibrary;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

import LinxLibrary.LinxBase.Power;

/**
 * Class contains a list of BallOnPath that are traveling on the same path.
 * They act as a single chain of balls, with a single speed and direction.
 * Has methods to act on the entire chain at once, add or remove balls, and
 * update the balls movement.
 * @author Brian
 */
class BallChainOnPath extends Vector<BallOnPath>{

    //-------------------------------------------------------------------------
    // Main Substance of BallChainOnPath
    //-------------------------------------------------------------------------
    Path mPath;
    Path.PathTravelDirection mMoveDirection;
    boolean mStopped = false;
    float mSpeed = BallOnPath.DEFAULT_SPEED;
        
    /**
     * Constructor to initialize a BallChainOnPath with a specific Path.
     * @param thePath the Path all balls in the chain will move along
     */
    public BallChainOnPath(Path thePath)
    {
    	mPath = thePath;
        mMoveDirection = Path.PathTravelDirection.ePathTravelForward;
    }
    
    public void clearMarks()
    {
        for (int i = 0; i < size(); i++)
            get(i).setMarked(false);
    }
    
    public Path getPath()
    {
        return mPath;
    }

    public boolean isMoving()
    {
        return !mStopped;
    }

    public Path.PathTravelDirection getDirection()
    {
        return mMoveDirection;
    }

    /**
     * Updates the movement of all balls based on chain speed and direction.
     * Also removes any balls that are have Alive set to false.
     */
    public void updateMovement()
    {          
        // if stopped, set speed to 0
        if (mStopped)
            mSpeed = 0;

        for (int i = size()-1; i >=0; i--)
        {
            // set balls speed to chain speed
            get(i).mSpeed = mSpeed;
            // remove dead balls
            if (!get(i).isAlive())
            {
            	Ball b = remove(i);
                BallsInFlight.addToRemoveBalls(b);
            }
        }

        if (size() == 0)
        {
            mMoveDirection = Path.PathTravelDirection.ePathTravelForward;
            return;
        }
        else if (mMoveDirection == Path.PathTravelDirection.ePathTravelForward)
        {
            get(0).update(mMoveDirection);
            UpdatePosFromIndexToEnd(1);
        }
        else
        {
            int firstIndex = size() - 1;
            get(firstIndex).update(mMoveDirection);
            UpdatePositionFromIndexToBegin(firstIndex - 1);
        }
    }

    public void toggleChainMovement()
    {
        mStopped = !mStopped;
    }
    public void setChainToMovement(boolean shouldMove)
    {
        mStopped = !shouldMove;
    }
    
    /**
     * Reverses the direction of all balls in the chain.
     */
    public void reverseChainDirection()
    {
        if (mMoveDirection == Path.PathTravelDirection.ePathTravelReverse)
            mMoveDirection = Path.PathTravelDirection.ePathTravelForward;
        else
            mMoveDirection = Path.PathTravelDirection.ePathTravelReverse;
    }
    /**
     * Sets the direction of all balls in the chain to the specified direction.
     * @param dir the Path.PathTravelDirection for the balls to move
     */
    public void setChainDirection(Path.PathTravelDirection dir)
    {
        if (mMoveDirection != dir)
            reverseChainDirection();
    }

    /**
     * Sets this chains speed, all its balls will move at this speed.
     * @param speed the desired speed value
     */
    public void setChainSpeed(float speed)
    {
        mSpeed = speed;
    }
    
    /**
     * Returns the speed of this chain and all of its balls.
     * @return the speed at which this chain is set
     */
    public float getChainSpeed()
    {
        // return the chain's speed value
        return mSpeed;
    }
    
    //-------------------------------------------------------------------------
    // AddBalls portion of BallChainOnPath
    //-------------------------------------------------------------------------
    /**
     * Adds a new BallOnPath to the last position in the chain.
     * @return the newly created BallOnPath
     */
    public BallOnPath AddNewBallToEnd()
    {
        BallOnPath newBall = null;
        if (size() == 0)
        {
            newBall = new BallOnPath(mPath, mMoveDirection);
            add(newBall);
        }
        else
        {
            newBall = AddNewBallAfter(size() - 1);
        }
        return newBall;
    }

    /**
     * Adds a new BallOnPath to the end of this chain with the specified color.
     * @param color the color to create the new ball with
     * @return the newly created BallOnPath with the specified color
     */
    public BallOnPath AddNewBallToEnd(LinxBase.BallColor color)
    {
        BallOnPath b = AddNewBallToEnd();
        b.setBallColor(color);
        return b;
    }

    /**
     * Adds a new BallOnPath behind the specified index, with the given color.
     * @param index the index of the ball to place a new ball behind
     * @param color the desired color for the new BallOnPath
     * @return the newly created BallOnPath
     */
    public BallOnPath AddNewBallAfter(int index, LinxBase.BallColor color)
    {
        BallOnPath b = AddNewBallAfter(index);
        b.setBallColor(color);
        return b;
    }

    /**
     * Adds a new BallOnPath behind the index, with the given Color and Power.
     * @param index the index of the ball to place a new ball behind
     * @param color the desired Color for the new BallOnPath
     * @param power the desired Power for the new BallOnPath
     * @return the newly created BallOnPath
     */
    public BallOnPath AddNewBallAfter(int index, LinxBase.BallColor color, Power power)
    {
        BallOnPath b = AddNewBallAfter(index, color);
        b.setPower(power);
        return b;
    }

    /**
     * Adds a new ball to the chain behind the ball with the specified index.
     * @param index the index of the ball to place the new ball behind
     * @return the newly created BallOnPath
     */
    public BallOnPath AddNewBallAfter(int index)
    {
        if (index >= 0 && index < size())
        {
            BallOnPath refBall = get(index);
            BallOnPath b = new BallOnPath(mPath, refBall, mMoveDirection);
            if (b.isAlive())
                add(index + 1, b);
            return b;
        }
        else // inserting before the first ball, so index is -1
        {
            BallOnPath b = new BallOnPath(mPath, mMoveDirection);
            // set the center behind the front ball, but in reverse dir (so essentially a setCenterInfront())
            if (mMoveDirection == Path.PathTravelDirection.ePathTravelForward)
                b.setCenterBehind(get(0), Path.PathTravelDirection.ePathTravelReverse);
            else
                b.setCenterBehind(get(0), Path.PathTravelDirection.ePathTravelForward);
            add(0, b);
            // don't need to update positions because ball is just going in front
            return b;
        }
    }
    
    //-------------------------------------------------------------------------
    // Utils portion of BallChainOnPath
    //-------------------------------------------------------------------------
    public BallOnPath getFirst()
    {
        if (size() == 0)
            return null;
        return get(0);
    }
    public BallOnPath getLast()
    {
        if (size() == 0)
            return null;
        return get(size() - 1);
    }

    public void setFirst(BallOnPath ball)
    {
        if (size() == 0)
            add(ball);
        else
            set(0, ball);
    }
    public void setLast(BallOnPath ball)
    {
        if (size() == 0)
            add(ball);
        else
            set(size() - 1, ball);
    }

    public void destroyBallChain()
    {
    	for (int i=size()-1; i>=0; i--) {
    		BallOnPath b = remove(i);
    		b.destroy();
    	} 
    	super.clear();
    }
    private void UpdatePosFromIndexToEnd(int index)
    {
        for (int i = index; i < size(); i++)
            get(i).updateFollow(mMoveDirection, get(i - 1));
    }

    private void UpdatePositionFromIndexToBegin(int index)
    {
        for (int i = index; i >= 0; i--)
        	get(i).updateFollow(mMoveDirection, get(i + 1));
    }

    private void CheckChainAt(int index)
    {
        if (mMoveDirection == Path.PathTravelDirection.ePathTravelForward)
        {
            index--;
            if (index <= 0)
                index = 1;
            UpdatePosFromIndexToEnd(index);
        }
        else
        {
            index += 2;
            if (index > size() - 2)
                index = size() - 2;
            UpdatePositionFromIndexToBegin(index);
        }
    }    
}
