

//import Engine;
import java.awt.event.MouseEvent;

import LinxLibrary.Ball;
import LinxLibrary.Cursor;
import LinxLibrary.LinxBase;

/**
 * The main class that runs Linx using the LinxLibrary which calls update().
 * @author Brian
 */
public class MyLinxGame extends LinxBase {    
    
    // normal speed is 2f
    final float FAST_SPEED = 3.5f;
    final float SLOW_SPEED = .5f;
    
    final int BALL_SCORE = 1000; 
        
    protected void initialize() {
    	    	
    	// do not show initial flash screen to save time
    	setShowStartScreen(false);
    	
    	// do not preload resources, for this very simple game
    	// preloadResources();
    	
    	// Max number of balls is just 17
    	chains.setMaxBalls(17);
    }

    @Override
    protected void update()
    {
        // update turret's movement/rotation
        turret.update();

        // try to shoot the turret if left mouse is tapped
        if (mouse.isButtonTapped(MouseEvent.BUTTON1))
            if (turret.shootBall())
                

        // swap the turret's loaded ball with the next ball on right mouse click
        if (mouse.isButtonTapped(MouseEvent.BUTTON3))
            turret.swapBalls();

        // create a ball when there is room for one to fit at the start point
        Ball b = chains.spawnBall();
        if (b != null) {
        	// if a new ball is created, 1 in 10 chance there will be a power up follows
        	if (random.nextInt(100) < 10) { // 10% chance
        		chains.spawnPowerBall(Ball.getRandomPower());
        		b.setRadius(b.getRadius() * 1.5f);
        		b.setBallColor(BallColor.Green);
        	}
        }

        // try to join any chains that should become one chain
        for (int i = 0; i < chains.getCount(); i++)
        {
            for (int j = 0; j < chains.getCount(); j++)
            {
                Cursor joinCursor = chains.joinChains(i, j);
                if (joinCursor.isValid())
                {
                    matchBalls(joinCursor, i);
                }
            }
        }
        

        // shotball and chain collisions
        for (int iChain = 0; iChain < chains.getCount(); iChain++)
        {
            for (int iShotBall = 0; iShotBall < shotBalls.getCount(); iShotBall++)
            {
                Cursor cursor = chains.isShotBallCollidingWithChainBall(iShotBall, iChain);
                if (cursor.isValid()) // false if no collision
                {
                    // if there was a match, remove the shot ball too
                    if (matchBalls(cursor, iChain)) {
                        cursor.markShotBallForDeletion();
                    } else
                    {
                        // put the shot ball into the chain
                        cursor.insertShotBall();                            
                    }
                    // play ball hit sound
                    resources.playSound("BallHit.wav");
                    // commit insert/delete of shotball
                    cursor.commitChanges();
                }
            }
        }

        // make the chain update the positions of all balls it controls
        for (int i = 0; i < chains.getCount(); i++)
            chains.updateChainMovement(i);
    }

    public boolean matchBalls(Cursor cursor, int iChain)
    {	
        // check for regular match three
        // move cursor to the collision point and walk back through any matching balls
        cursor.moveToCollision();
        cursor.walkBackwardThroughMatchingBalls();

        // check one ball back for a powerball that we may need to activate
        cursor.moveBackward();
        if (cursor.getCurrentPower() != Power.None)
            cursor.markCurrentBallForDeletion();
        // move forward again regardless
        cursor.moveForward();

        // delete and move forward through any matching balls
        while (cursor.currentBallColorMatchesShotBall())
        {
            cursor.markCurrentBallForDeletion();
            cursor.moveForward();
        }
        
        cursor.moveBackward(); // one too many forward
        if (cursor.countBallsMarkedForDeletion() > 2) {
        	increaseScoreBy(cursor.countBallsMarkedForDeletion() * BALL_SCORE);
        	cursor.commitChanges();
        	// should be a hole in the chain now, so split where we are, 
            // at the match closest to the front of the chain
            if (chains.splitChain(iChain, cursor.getIndex())) {
            	// if last of first and first of second color matches, attracts the chains
                chains.magnetizeChains(iChain, iChain + 1);
            }
            
        	return true;
        }
        
        // didn't match 3, cancel changes
        cursor.cancelChanges();
        return false;
    }
}
