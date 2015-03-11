

//import Engine;
import java.awt.Color;
import java.awt.event.MouseEvent;

import LinxLibrary.Ball;
import LinxLibrary.Cursor;
import LinxLibrary.LinxBase;

/**
 * The main class that runs Linx using the LinxLibrary which calls update().
 * @author Brian
 */
public class Linx extends LinxBase {    
    
    // normal speed is 2f
    final float FAST_SPEED = 3.5f;
    final float SLOW_SPEED = .5f;
    
    final float BALL_SCORE = 1000; 
        
    protected void initialize() {    	
    	// setShowStartScreen(false);
    	preloadResources();
    	chains.setMaxBalls(43);
    }

    @Override
    protected void update()
    {
        // update turret's movement/rotation
        turret.update();

        // try to shoot the turret if left mouse is tapped
        if (mouse.isButtonTapped(MouseEvent.BUTTON1))
            turret.shootBall();

        // swap the turret's loaded ball with the next ball on right mouse click
        if (mouse.isButtonTapped(MouseEvent.BUTTON3))
            turret.swapBalls();

        // create a ball when there is room for one to fit at the start point
        Ball b = chains.spawnBall();
        if (b != null) {
        	// if a new ball is created, 1 in 10 chance there will be a power up follows
        	if (random.nextInt(100) < 10) { // 10% chance
        		chains.spawnPowerBall(Ball.getRandomPower());
        		// b.setRadius(b.getRadius() * 1.5f);
        		// b.setBallColor(BallColor.Green);
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
        
        // if we matched at least 3 (not counting a powerball)
        if (cursor.countBallsMarkedForDeletion() >= 3)
        {              
        	int score = 0; 
        	
            // check if the ball 1 past the last match (the ball we are on) is a powerball we need to activate
            if (cursor.getCurrentPower() != Power.None)
                cursor.markCurrentBallForDeletion();

            // need to move backward because we moveForward()'d past the last match
            cursor.moveBackward();
            // powerups don't count; check for match of 4+ to insert a powerball
            if (cursor.countBallsMarkedForDeletion() > 3)
            {
            	score += BALL_SCORE;
                // move to the back of the matching balls
                cursor.walkBackwardThroughMatchingBalls();
                // create a random powerup/down here because we will destroy all the matched balls
                // and we don't want the powerball to appear where the destroyed balls were
                cursor.insertPowerBall();
            }
            
            //check for powerups
            boolean hasPower = false;
            while (cursor.markedBallsHavePowerup())
            {
                hasPower = true;
                cursor.moveToPower();
                switch (cursor.getCurrentPower())
                {
                    case Bomb:
                        // Play bomb powerup sound
                        resources.playSound("Bomb.wav");
                        // delete 2 backward from bomb
                        for (int i = 0; i < 2; i++)
                        {
                            if (cursor.stillInChain())
                                cursor.markCurrentBallForDeletion();
                            cursor.moveBackward();
                        }
                        // move back to bomb
                        cursor.moveToPower();
                        // delete 2 forward from bomb
                        for (int i = 0; i < 2; i++)
                        {
                            // move forward first because we already marked the ball at cursor.moveToPower()
                            cursor.moveForward();
                            if (cursor.stillInChain())
                                cursor.markCurrentBallForDeletion();
                        }
                        // move back to bomb
                        cursor.moveToPower();
                        // set power to None so it will be deleted
                        cursor.setCurrentPower(Power.None);
                        break;
                    case Slow:
                        // Play slow powerup sound
                        //PlayACue("LINX_slow_powerup_activated");
                        resources.playSound("SlowDown.wav");
                        // slow chain for 10 seconds
                        chains.setSpeed(SLOW_SPEED, 10);
                        // set power to None so it will be deleted
                        cursor.setCurrentPower(Power.None);
                        break;
                    case Stop:
                        // play freeze sound
                        resources.playSound("Freeze.wav");
                        // stop chain for 5 seconds
                        chains.stopChains(5);
                        // set power to None so it will be deleted
                        cursor.setCurrentPower(Power.None);
                        break;
                    case Wildcard:
                        // play wildcard sound
                        resources.playSound("WildCard.wav");
                        // set power to None so it will be deleted
                        cursor.setCurrentPower(Power.None);
                        break;
                    case Speed:
                        // play speed up sound
                        resources.playSound("SpeedUp.wav");
                        // speed up chain for 3 seconds
                        chains.setSpeed(FAST_SPEED, 5);
                        // set power to None so it will be deleted
                        cursor.setCurrentPower(Power.None);
                        break;
                    case Invisiball:
                        // play invisiball sound
                        resources.playSound("Invisiball.wav");
                        // set the ball in the turret to invisible
                        turret.setBallsInvisible();
                        // set power to None so it will be deleted
                        cursor.setCurrentPower(Power.None);
                        break;
                }
                score += BALL_SCORE;
                cursor.commitChanges();
            }
            

            if (hasPower == false)
                // play balls destroyed sound if no powerup sound played
                resources.playSound("BallDisappear.wav");

            // move back to the front of the matching balls so we can split at correct position
            //cursor.walkForwardThroughMatchingBalls();
            // commit deletes/inserts before we split the chain and only control half of it
            cursor.commitChanges();
            
            score += cursor.countBallsMarkedForDeletion() * BALL_SCORE;
            increaseScoreBy(score);

            // should be a hole in the chain now, so split where we are, 
            // at the match closest to the front of the chain
            if (chains.splitChain(iChain, cursor.getIndex()))
            {
                chains.magnetizeChains(iChain, iChain + 1);
            }
            // matched at least 3
            return true;
        }
        // didn't match 3, cancel changes
        cursor.cancelChanges();
        return false;
    }
}
