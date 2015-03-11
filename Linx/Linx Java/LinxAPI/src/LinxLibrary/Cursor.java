package LinxLibrary;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

import Engine.Vector2;
import LinxLibrary.LinxBase.Power;

public class Cursor {
    private BallChainOnPath mChain;
    private int mCollisionIndex = -1;

    private BallsInFlight mShotBalls;
    private int mShotBallIndex = -1;
    private Ball matchBall;

    private int mIndex = -1;
    private int mPowerups = 0;
    private boolean mIsValid = false;
    private boolean mInsertShotBall = false;
    private boolean mInsertPowerBall = false;
    private boolean mInsertFront = false;

    // arrays to hold an action before commit
    private List<Integer> markedColorChangeIndex;
    private List<LinxBase.BallColor> markedColorChangeColor;
    private int markedPowerBallInsert;

    public Cursor()
    {
        mIsValid = false;
    }
    
    public Cursor(int iShotBall, BallsInFlight shotBalls, int iChainBall, BallChainOnPath chain)
    {
        mShotBallIndex = iShotBall;
        mShotBalls = shotBalls;
        mCollisionIndex = iChainBall;
        mChain = chain;
        mIndex = 0;
        mIsValid = true;
        if (shotBalls == null)
            matchBall = chain.get(mCollisionIndex);
        else
        {
            matchBall = mShotBalls.get(mShotBallIndex);
            
            // use the dot product of chain balls direction with difference of chain and shot ball's center
            // to decide if the shotball should be in front or behind the chain ball it hit.
            Vector2 dist = chain.get(iChainBall).getCenter().clone().sub(mShotBalls.get(iShotBall).getCenter());
            dist = dist.normalize();
            
            float dot = Vector2.dot(chain.get(iChainBall).getDirection(), dist);
            if (dot < 0)
                mInsertFront = true;
        }

        markedColorChangeIndex = new Vector<Integer>();
        markedColorChangeColor = new Vector<LinxBase.BallColor>();
    }

    /**
     * Cancels all changes made to the cursor - colors, inserting and removing
     * balls.
     */
    public void cancelChanges()
    {
        // cancel colors
        markedColorChangeColor.clear();
        markedColorChangeIndex.clear();

        // cancel inserting
        mInsertPowerBall = false;
        mInsertShotBall = false;

        // remove marks
        if (mShotBalls != null)
        {
            for (int i = 0; i < mShotBalls.size(); i++)
                mShotBalls.get(i).setMarked(false);
        }

        for (int i = 0; i < mChain.size(); i++)
            mChain.get(i).setMarked(false);
    }

    /**
     * Commits all changes made to ball colors, inserting, or removing balls.
     * All changes to balls/chains have completed after this method completes.
     */
    public void commitChanges()
    {
        if (checkValid())
        {
            // change ball colors
            for (int i = 0; i < markedColorChangeIndex.size(); i++)
            {
                mChain.get(markedColorChangeIndex.get(i)).setBallColor(markedColorChangeColor.get(i));
                if (mChain.get(markedColorChangeIndex.get(i)).getPower() != Power.None)
                {
                    mPowerups -= 1;
                    mChain.get(markedColorChangeIndex.get(i)).setPower(Power.None);
                }
            }
            markedColorChangeColor.clear();
            markedColorChangeIndex.clear();

            // check for inserting balls
            if (mInsertShotBall)
                insertShotBallCode();
            if (mInsertPowerBall)
                insertPowerBallCode();
            mInsertShotBall = mInsertPowerBall = false;

            // delete marked shot balls
            if (mShotBalls != null)
            {
                for (int i = 0; i < mShotBalls.size(); i++)
                {
                    if (mShotBalls.get(i).isMarked())
                    {
                        // stop and blow up the shotball
                    	Ball b = mShotBalls.remove(i);
                    	b.setVelocity(0f, 0f);
                        b.remove();
                        BallsInFlight.addToRemoveBalls(b);
                        // move back because list shrunk
                        --i;
                    }
                }
            }

            // remove marked balls
            for (int i = 0; i < mChain.size(); i++)
            {
                if (mChain.get(i).isMarked() && !mChain.get(i).isPowerBall())
                {
                    // sub the index if necessary because of list shrink
                    if (i < mIndex)
                        --mIndex;
                    Ball b = mChain.remove(i);
                    b.remove();
                    BallsInFlight.addToRemoveBalls(b);
                    // move back because list shrunk
                    --i;
                }
            }

            // fix index for out of bounds because cursor index can still be used
            if (mChain.size() <= mIndex)
                mIndex = mChain.size() - 1;
            if (mIndex < 0)
                mIndex = 0;
        }
    }
    
    /**
     * Checks if the cursor is actually valid and usable.
     * @return true if cursor is usable.
     */
    public boolean isValid()
    {
        return mIsValid;
    }


    /**
     * Returns true if any balls that have been marked since cancel/commit are
     * powerballs.
     * @return true if any balls that have been marked since cancel/commit are powerballs
     */
    public boolean markedBallsHavePowerup()
    {
        if (mPowerups > 0)
            return true;
        else
            return false;
    }

    /**
     * Returns the index of the Cursor in the chain of balls.
     * @return index of the cursor position. 
     */
    public int getIndex()
    {
        return mIndex;
    }

    /**
     * Returns the power of the ball at the current index. Can be Power.None.
     * @return power of the ball at the current index. 
     */
    public Power getCurrentPower()
    {
        if (checkValid())
            if (stillInChain())
                return mChain.get(mIndex).getPower();
        return Power.None;
    }
    
    /**
     * Sets the Power of the ball at the current index to the specified Power.
     * @param power the Power to set the current ball to
     */
    public void setCurrentPower(Power power)
    {
        if (checkValid())
            if (stillInChain())
            {                    
                if (power == Power.None && mChain.get(mIndex).isPowerBall())
                    mPowerups -= 1;
                mChain.get(mIndex).setPower(power);
            }
    }

    /**
     * Returns the color of the current ball
     * @return the color of the current ball
     */
    public Color getCurrentColor()
    {
        if (stillInChain())
            return mChain.get(mIndex).getColor();
        else
            return Color.white; // error
    }

    /**
     * Moves the index of the Cursor further back in the chain
     */
    public void moveBackward()
    {
        if (checkValid())
            ++mIndex;
    }

    /**
     * Moves the index of the Cursor further forward in the chain
     */
    public void moveForward()
    {
        if (checkValid())
            --mIndex;
    }

    /**
     * Moves the index of the Cursor to the ball of the collision
     */
    public void moveToCollision()
    {
        if (checkValid())
        {
            mIndex = mCollisionIndex;
        }
    }

    /**
     * Moves the index to the very front of the chain
     */
    public void moveToStartOfChain()
    {
        if (checkValid())
        {
            // assuming chain ball count is > 0
            mIndex = 0;
        }
    }

    /**
     * Moves the index to the first powerball that has been marked for deletion.
     */
    public void moveToPower()
    {
        if (checkValid() && mPowerups > 0)
        {
            int i = 0;
            while (i < mChain.size() && (!mChain.get(i).isPowerBall() || !mChain.get(i).isMarked()))
                i++;
            mIndex = i;
        }
    }
    
    /**
     * Inserts the shot ball in the chain
     */
    public void insertShotBall()
    {
        if (checkValid() && mShotBalls != null)
        {
            mInsertShotBall = true;
        }
    }
    private void insertShotBallCode()
    {
        if (checkValid() && mShotBalls != null && mShotBallIndex >= 0 && mShotBallIndex < mShotBalls.size())
        {
            if (mShotBalls.get(mShotBallIndex).isPowerBall() == false || mShotBalls.get(mShotBallIndex).getPower() == Power.Wildcard)
            {
                // check adding in front or behind collision ball
                int index;
                if (mInsertFront)
                    index = mCollisionIndex - 1;
                else
                    index = mCollisionIndex;
                // add the ball to the correct spot
                mChain.AddNewBallAfter(index, mShotBalls.get(mShotBallIndex).getBallColor(), mShotBalls.get(mShotBallIndex).getPower());
                // make sure we don't go out of bounds somehow
                if (index + 1 < mChain.size())
                {
                    mChain.get(index + 1).setCenter(mShotBalls.get(mShotBallIndex).getCenter());
                    if (index > -1) // T is set already when adding in front of the front (-1 index)
                        mChain.get(index + 1).setT(mChain.get(index).getT());
                    mChain.get(index + 1).setAnimating(true);
                }
                // remove the shot ball
                mShotBalls.get(mShotBallIndex).destroy();
                mShotBalls.remove(mShotBallIndex);
            }
        }
    }

    /**
     * Inserts a new random power at the current Cursor index
     */
    public void insertPowerBall()
    {
        if (checkValid() && stillInChain())
        {
            mInsertPowerBall = true;
            markedPowerBallInsert = mIndex;
        }
    }
    private void insertPowerBallCode()
    {
        if (checkValid() && stillInChain())
        {
            mChain.AddNewBallAfter(markedPowerBallInsert, LinxBase.BallColor.PowerUp);
            if(markedPowerBallInsert + 1 == mChain.size())
            	markedPowerBallInsert--;
            mChain.get(markedPowerBallInsert + 1).setPower(Ball.getRandomPower());                
        }
    }

    /**
     * Copies the color of the turret's shot ball to the current chain ball
     */
    public void copyShotBallColorToCurrentChainBall()
    {
        if (checkValid())
        {
            if (stillInChain())
            {
                markedColorChangeIndex.add(mIndex);
                markedColorChangeColor.add(matchBall.getBallColor());
            }
        }
    }

    /**
     * Sets the shot ball to a random color
     */
    public void setShotBallToRandomColor()
    {
        if (checkValid())
        {
            if (mShotBalls != null && mShotBallIndex >= 0 && mShotBallIndex < mShotBalls.size())
            {
                mShotBalls.get(mShotBallIndex).setRandomColor();
                mShotBalls.get(mShotBallIndex).setPower(Power.None);
            }
        }
    }

    /**
     * Sets the shot ball to the Color color
     * @param color color to be set 
     */
    public void setShotBallColor(LinxBase.BallColor color)
    {
        if (checkValid())
            if (mShotBalls != null && mShotBallIndex >= 0 && mShotBallIndex < mShotBalls.size())
            {
                mShotBalls.get(mShotBallIndex).setBallColor(color);
                mShotBalls.get(mShotBallIndex).setPower(Power.None);
            }
    }                    

    /**
     * Sets the current chain ball to the Color color
     * @param color color to be set.
     */
    public void setCurrentChainBallColor(LinxBase.BallColor color)
    {
        if (checkValid())
        {
            if (stillInChain())
            {
                markedColorChangeIndex.add(mIndex);
                markedColorChangeColor.add(color);
            }
        }
    }

    /**
     * Sets the current chain ball to a random color
     */
    public void setCurrentChainBallToRandomColor()
    {
        if (checkValid())
        {
            if (stillInChain())
            {
                markedColorChangeIndex.add(mIndex);
                markedColorChangeColor.add(Ball.getRandomColor());
            }
        }
    }

    /**
     * Returns true if the current ball's color matches the shot ball's color
     * @return true if the current ball's color matches the shot ball's color
     */
    public boolean currentBallColorMatchesShotBall()
    {
        if (checkValid())
        {
            if (stillInChain())
            {
                if (mChain.get(mIndex).getBallColor() == matchBall.getBallColor() || mChain.get(mIndex).getPower() == Power.Wildcard)
                    return true;
            }
        }
        return false;
    }

    /**
     * Moves the index forward continually until a non-matching ball is reached.
     */
    public void walkForwardThroughMatchingBalls()
    {
        if (checkValid())
        {
            
        	if(!stillInChain())
        		return;
        	// check for first ball matching then move
        	List<BallOnPath> l = mChain;
        	BallOnPath b = l.get(mIndex);
        	LinxBase.BallColor c = b.getBallColor();
        	Power p = b.getPower();
            if (c != matchBall.getBallColor() || p == Power.Wildcard)
                // if shotball would go behind collision ball
                if (!mInsertFront)
                {
                    // move the index backward so user can check the back side of collision ball
                    mIndex += 1;
                    return;
                }
                else // move forward and continue because there could be a match in front of the collision ball
                    mIndex -= 1;

            while (stillInChain() && (mChain.get(mIndex).getBallColor() == matchBall.getBallColor() || mChain.get(mIndex).getPower() == Power.Wildcard))
            {
                // keep moving forward
                mIndex -= 1;
            }
            // go back to last matching
            mIndex += 1;
        }
    }

    /**
     * Moves the index backward until a non-matching ball is reached.
     */
    public void walkBackwardThroughMatchingBalls()
    {
        if (checkValid() && stillInChain())
        {                
            // check for first ball matching then move
            if (mChain.get(mIndex).getBallColor() != matchBall.getBallColor() || mChain.get(mIndex).getPower() == Power.Wildcard)
            {
                // if shotball would go in front of collision ball
                if (mInsertFront)
                {
                    // move the index forward so user can check the front side of collision ball
                    mIndex -= 1;
                    return;
                }
                else // move back and continue because there could be a match behind the collision ball
                    mIndex += 1;
            }

            while (stillInChain() && (mChain.get(mIndex).getBallColor() == matchBall.getBallColor() || mChain.get(mIndex).getPower() == Power.Wildcard))
            {
                // keep moving backward
                mIndex += 1;
            }
            // go back to last matching
            mIndex -= 1;
        }

    }

    /**
     * Marks the shot ball to be deleted when changes are committed.
     */
    public void markShotBallForDeletion()
    {
        if (checkValid())
            if (mShotBalls != null && mShotBallIndex > -1 && mShotBallIndex < mShotBalls.size())
                mShotBalls.get(mShotBallIndex).setMarked(true);
    }
    /**
     * Marks the current ball for deletion when changes are committed.
     */
    public void markCurrentBallForDeletion()
    {
        if (checkValid())
            if (stillInChain())
            {
                // only mark it once
                if (!mChain.get(mIndex).isMarked())
                {
                    mChain.get(mIndex).setMarked(true);
                    if (mChain.get(mIndex).isPowerBall())
                        mPowerups += 1;
                }
            }
    }

    /**
     * Returns the number of balls that have been marked for deletion.
     * @return the number of balls that have been marked for deletion.
     */
    public int countBallsMarkedForDeletion()
    {
        if (checkValid())
        {
            int count = 0;
            if (mShotBalls == null)
                count = 0;
            else
                count = 1;
            for (int i = 0; i < mChain.size(); i++)
            {
                if (mChain.get(i).isMarked() && (!mChain.get(i).isPowerBall() || mChain.get(i).getPower() == Power.Wildcard)) //fix for wildcard
                    count += 1;
            }
            return count;
        }
        return -1;
    }
    
    /**
     * Returns true if the Cursor index is still within the chain
     * @return true if the Cursor index is still within the chain
     */
    public boolean stillInChain()
    {
        if (checkValid())
        {
            if (mIndex >= 0 && mIndex < mChain.size())
                return true;                
        }
        
        return false;
    }

    private boolean checkValid()
    {
        if (mIsValid && mChain != null)
            return true;
        else
            return false;
    }
}
