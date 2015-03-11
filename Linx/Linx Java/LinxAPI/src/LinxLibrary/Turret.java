package LinxLibrary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import Engine.GameObject;
import Engine.LibraryCode;
import Engine.MouseInput;
import Engine.Vector2;
import LinxLibrary.LinxBase.Power;

public class Turret extends GameObject {

    private enum ShootingState
    {
        Ready, NotReady
    }

    private float mShootingDelay;
    private float mInitialShootingDelay;
    private float mBulletSpeed;
    private ShootingState mShootingState;
    private final String TEXTURE = "Turret_BodyPlacement.png";
    private final float NEXT_BALL3_RADIUS = 1;
    private final float NEXT_BALL2_RADIUS = 1.3f;
    private final float NEXT_BALL_RADIUS = 1.7f;
    private Ball mCurrentBall;
    private Ball mNextBall;
    private Ball mNextBall2;
    private Ball mNextBall3;
    private BallsInFlight inFlight;
    private GameObject mColorBand;
    private int mInvisible; // number of invisiball balls left    
    private boolean mDebug;
    private List<BallChainOnPath> mChains;
    private MouseInput mouse;
    
    private Vector2 mFrontDirection;

    public Turret(String filePath, BallsInFlight inFlightBalls, MouseInput m, List<BallChainOnPath> chains) throws IOException
    {
    	super();
        initialize();
        inFlight = inFlightBalls;
        mChains = chains;
        mouse = m;
        
        String[] xy;
        // Get turret x and y, first line of Path.txt file
        try {
        	BufferedReader in = new BufferedReader(new FileReader(filePath));
        	String line = in.readLine();        
            in.close();
            xy = line.split(",");
        } catch(FileNotFoundException e) { 
            // Use the hard coded values if file not found
            xy = new String[] {"52" , "36"};                                                
        }
        
        Vector2 pos = new Vector2(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
        setCenter((Engine.BaseCode.world.getWidth() * pos.getX() / 100f), Engine.BaseCode.world.getHeight() * pos.getY() / 100f);
    }

    private void initialize()
    {
        setSize(8, 4.5f);
        setImage(TEXTURE);
        mShootingState = ShootingState.NotReady;
        mBulletSpeed = 1f;
        mInitialShootingDelay = .4f;
        mShootingDelay = 0f;
        mColorBand = new GameObject();
        mColorBand.setCenter(getCenter());
        mColorBand.setSize(getSize());
        mNextBall2 = new Ball();
        mNextBall3 = new Ball();
    }

    /// <summary>
    /// Update the turret's movement/rotation based on the mouse position
    /// </summary>
    public void update()
    {
        rotateTurret();
        positionCurrentBall();
        positionNextBall();
        positionColorBand();
        shootActivitity();
        
        if (mDebug)
        {
            mCurrentBall.setCenter(mouse.getWorldX(), mouse.getWorldY());
        }
        mCurrentBall.moveToFront();
        mColorBand.moveToFront();
        moveToFront();
    }

    /// <summary>
    /// Swaps the turret's currently loaded ball with the next ball
    /// </summary>
    public void swapBalls()
    {
        // swap the balls
        Ball temp = mCurrentBall;
        setCurrentBall(mNextBall);
        setNextBall(temp);
    }

    private void rotateTurret()
    {
        // rotate the turret by aiming at mouse position
        float mouseX = mouse.getWorldX();
        float mouseY = mouse.getWorldY();
        
        // subtract to get distance
        float deltaX = mouseX - getCenterX();
        float deltaY = mouseY - getCenterY();
        
        // rotate to face mouse position
        double r = Math.atan2(deltaY, deltaX);
        rotateFront(r);
        
        r = r * 180 / Math.PI;
        setRotation((float)r);
    }        

    private void setCurrentBall()
    {
        // try 5 times to get a ball with a color in the chain
        Ball ball = new Ball();
        for (int t = 0; t < 5; t++)
        {
            for (BallChainOnPath mChain : mChains) {
                for (BallOnPath bop : mChain) {
                    if (bop.getBallColor() == ball.getBallColor()) {
                        setCurrentBall(ball);
                        return;
                    }
                }
            }
            // don't check the same color again...
            LinxBase.BallColor current = ball.getBallColor();
            do
            {
                ball.setRandomColor();
            } while (ball.getBallColor() == current);
        }
        // didn't find that color in chain after 5 trys, oh well
        setCurrentBall(ball);
    }
    private void setCurrentBall(Ball current)
    {
        mCurrentBall = current;
        //mCurrentBall.alwaysOnTop = true;
        mCurrentBall.setRadius(Ball.sRadius);
        positionCurrentBall();
    }

    private void setNextBall()
    {
        // try 5 times to get a ball with a color in the chain
        Ball ball = new Ball();
        for (int t = 0; t < 5; t++)
        {
            // check if it is a color in the chain
            for (BallChainOnPath mChain : mChains) {
                for (BallOnPath bop : mChain) {
                    if (bop.getBallColor() == ball.getBallColor()) {
                        setNextBall(ball);
                        return;
                    }
                }
            }
            // don't check the same color again...
            LinxBase.BallColor current = ball.getBallColor();
            do
            {
                ball.setRandomColor();
            } while (ball.getBallColor() == current);
        }
        // didn't find that color in chain after 5 trys, oh well
        setNextBall(ball);
    }
    private void setNextBall(Ball next)
    {
        mNextBall = mNextBall2;
        mNextBall2 = mNextBall3;
        mNextBall3 = next;
        mNextBall.setRadius(NEXT_BALL_RADIUS);
        mNextBall2.setRadius(NEXT_BALL2_RADIUS);
        mNextBall3.setRadius(NEXT_BALL3_RADIUS);
        positionNextBall();
    }
    
    private void positionColorBand()
    {
        if (mColorBand != null)
        {
            mColorBand.setCenter(getCenter());
            mColorBand.setRotation(getRotation());
            if (mCurrentBall != null)
            {
                String texture = "Turret_ColorIndicator_";
                // Check for invisiball powerup used
                if (mInvisible > 0)
                {
                    texture += "Neutral.png";                        
                }
                else
                {
                	LinxBase.BallColor c = mCurrentBall.getBallColor();
                    if (c == LinxBase.BallColor.Blue)
                        texture += "Blue.png";
                    else if (c == LinxBase.BallColor.Green)
                        texture += "Green.png";
                    else if (c == LinxBase.BallColor.Magenta)
                        texture += "Purple.png";
                    else if (c == LinxBase.BallColor.Red)
                        texture += "Red.png";
                    else
                        texture += "Yellow.png";
                }
                mColorBand.setImage(texture);
            }

        }
    }

    private void positionCurrentBall()
    {
        if (mCurrentBall != null)
        {
            mCurrentBall.setCenter(getCenter().clone().add(mFrontDirection.clone().mult(0.32f * getWidth()))); //TODO check if this works
            mCurrentBall.setRotation(getRotation());
        }
    }

    private void positionNextBall()
    {
        if (mNextBall != null)
        {
            mNextBall.setCenter(57.5f, 19.5f);
            mNextBall2.setCenter(56, 17);
            mNextBall3.setCenter(53.5f, 16.5f);
        }
    }


    private void shootActivitity()
    {
        if (mShootingState == ShootingState.NotReady)
        {
            mShootingDelay -= .025f;
            if (mShootingDelay < 0)
            {
                mShootingDelay = mInitialShootingDelay;
                mShootingState = ShootingState.Ready;
                // activate the balls 
                if (mCurrentBall == null)
                {
                    setCurrentBall();
                    setNextBall();
                }
            }
        }            
    }

    /// <summary>
    /// Shoot a ball from the turret
    /// </summary>
    public boolean shootBall()
    {
        if (mShootingState == ShootingState.Ready)
        {
            // if the ball was invisible
            if (mInvisible > 0)
                mInvisible -= 1; // going to shoot an invisible ball
            // set current ball ready for flight
            mCurrentBall.setRotation(getRotation());
            mCurrentBall.setVelocity(mFrontDirection.clone().mult(mBulletSpeed));
// mCurrentBall.shouldTravel = true;
            // make sure the ball comes back from going invisible            
            mCurrentBall.setBallColor(mCurrentBall.getBallColor());
            // add to in flight balls
            inFlight.add(mCurrentBall);
            // move the next ball to current
            setCurrentBall(mNextBall);
            // set new next ball
            setNextBall();
            mShootingState = ShootingState.NotReady;
            LibraryCode.resources.playSound("BallShoot.wav"); // play sound if turret did shoot
            return true;
        }
        return false;
    }

    public void setBallsInvisible()
    {
    	mCurrentBall.setImage("Orb_Neutral.png");
        mNextBall.setImage("Orb_Neutral.png");
        mInvisible = 2; // next 2 balls need to have neutral color band as well
    }

    public void setCurrentBallPower(Power power)
    {
        mCurrentBall.setPower(power);
    }

    public void setCurrentBallColor(LinxBase.BallColor color)
    {
        mCurrentBall.setBallColor(color);
    }

    public void insertPowerBall()
    {
        mNextBall.setPower(Ball.getRandomPower());
    }
    
    private void rotateFront(double r)
    {    	
    	mFrontDirection = new Vector2(1, 0);
    	mFrontDirection = Vector2.rotateVectorByAngle(mFrontDirection, (float) r);
    	mFrontDirection = mFrontDirection.normalize();
    }
    
    public void debugMode(boolean mode)
    {
        mDebug = mode;
    }
}
