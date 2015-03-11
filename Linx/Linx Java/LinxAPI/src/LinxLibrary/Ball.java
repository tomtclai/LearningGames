package LinxLibrary;

import java.awt.Color;

import Engine.GameObject;
import LinxLibrary.LinxBase.Power;

/**
 * The base class that contains the basic methods and variables for all balls.
 * It has a color and a power that can be changed, a timer and booleans used
 * for the balls explosion or removal, and can be marked by a Cursor. This
 * class is generally only created by the turret because it does not follow
 * a path.
 * @author Brian
 */
public class Ball extends GameObject
{
    static public float sRadius = 1.8f;
	
    protected float mRemoveTimer = -1;
    protected boolean mIsRemoved = false;
    protected boolean mIsAlive = true;
    protected boolean mIsPowerBall = false;
    protected Power mPower = Power.None;
    protected boolean mMarked = false;
    protected LinxBase.BallColor mBallColor = LinxBase.BallColor.Blue;
	
    /**
     * Basic default constructor to initialize and set to a random color.
     */
    public Ball()
    {
        super();
        initialize();
        setRandomColor();
    }
    
    /**
     * Constructor to specify the balls initial color.
     * @param color the color to create the ball with
     */
    public Ball(LinxBase.BallColor color)
    {
        super();
        initialize();
        setBallColor(color);
    }
	
    /**
     * Makes sure the ball will be on top of drawset and sets ball radius.
     */
    public void initialize()
    {
    	this.moveToFront();
        this.setRadius(sRadius);
    }
	
    /**
     * Updates the ball to destroy it when offscreen or the timer is set.
     */
    public void update()
    {
        super.update();
        if (!Engine.BaseCode.world.isInsideWorldBound(this))
            destroy();
        if (mRemoveTimer != -1)
        {
            if (mRemoveTimer >= 0)
            {
                mRemoveTimer -= .025f;
            }
            else
            {
                destroy();
            }
        }
    }

    /**
     * Returns true if the ball has any power, good or bad.
     * @return false for Power.None, true for all others
     */
    public boolean isPowerBall()
    {
        return mIsPowerBall;
    }
	
    /**
     * Returns this balls current power.
     * @return the Power of this ball
     * @see Power
     */
    public Power getPower()
    {
        return mPower;
    }

    /**
     * Sets this balls Power, and updates the image and power boolean.
     * Can also be used with Power.None to set a power ball back to normal.
     * @param power the Power to set this ball to
     * @see Power
     */
    public void setPower(Power power)
    {
        mPower = power;
        if (power != Power.None)
        {
            mIsPowerBall = true;
            switch (power)
            {
                case Bomb:
                    setSpriteSheet("Orb_Special_Bomb.png", 122, 120, 37, 4);
                    setUsingSpriteSheet(true);
                    break;
                case Slow:
                    setSpriteSheet("Orb_Special_Slow.png", 121, 120, 37, 4);
                    setUsingSpriteSheet(true);
                    break;
                case Stop:
                    setSpriteSheet("Orb_Special_Freeze.png", 120, 120, 37, 4);
                    setUsingSpriteSheet(true);
                    break;
                case Wildcard:
                    setImage("Orb_Special_Wildcard.png");
                    setUsingSpriteSheet(false);
                    break;
                case Invisiball:
                    setSpriteSheet("Orb_Special_Invisiball.png", 119, 120, 37, 4);
                    setUsingSpriteSheet(true);
                    break;
                case Speed:
                    setSpriteSheet("Orb_Special_Speed.png", 123, 121, 37, 4);
                    setUsingSpriteSheet(true);
                    break;
            }
        }
        else
        {
            setBallColor(mBallColor);           
        }
    }

    /**
     * Removes the ball from the game and being drawn to the screen.
     */
    public void destroy()
    {
        mIsAlive = false;
        super.destroy();
    }

    /**
     * Sets the ball to be destroyed after an explosion and increasing score.
     */
    public void remove()
    {           
        // Setup removal timer and explosion
        mIsRemoved = true;
        mRemoveTimer = .5f;
        this.setSpriteSheet("Explosion.png", 60, 59, 5, 1);
        this.setUsingSpriteSheet(true);
    }

    /**
     * Returns whether or not the ball has exploded,  from the game.
     * @return boolean true if removed
     */
    public boolean isRemoved() { return mIsRemoved; }

    /**
     * Returns whether or not the ball has been destroyed completely.
     * @return true if the ball is alive
     */
    public boolean isAlive() { return mIsAlive; }

    /**
     * Returns whether or not the ball has been marked by a Cursor.
     * @return true if the ball has been marked
     * @see Cursor
     */
    public boolean isMarked() { return mMarked; }

    /**
     * Mark the ball, generally for deletion by a Cursor.
     * @param mark true to mark, false to unmark the ball
     */
    public void setMarked(boolean mark) { mMarked = mark; }

    /**
     * Returns the balls current color.
     * @return the Color of the ball
     */
    public LinxBase.BallColor getBallColor() { return mBallColor; }

    /**
     * Sets the new color for this ball and sets Power to None.
     * @param c the Color to set the ball to.
     */
    public void setBallColor(LinxBase.BallColor c)
    {
    	
        if (c== LinxBase.BallColor.Blue) {
            setImage("Orb_Blue.png");
            setColor(Color.blue);
        }
        else if (c== LinxBase.BallColor.Magenta) {
            setImage("Orb_Purple.png");
            setColor(Color.magenta);
        }
        else if (c== LinxBase.BallColor.Green) {
            setImage("Orb_Green.png");
            setColor(Color.green);
        }
        else if (c== LinxBase.BallColor.Red) {
            setImage("Orb_Red.png");
            setColor(Color.red);
        }
        else if (c== LinxBase.BallColor.Yellow) {
            setImage("Orb_Yellow.png");
            setColor(Color.yellow);
        }
        mBallColor = c;
        // no more powerup
        mPower = Power.None;
        mIsPowerBall = false;
        setUsingSpriteSheet(false);
    }

    /**
     * Sets this ball to an evenly random color.
     */
    public void setRandomColor()
    {
        int n = Engine.BaseCode.random.nextInt(5);
        switch (n)
        {
            case 0: 
                setBallColor(LinxBase.BallColor.Red);
                break;
            case 1: 
            	setBallColor(LinxBase.BallColor.Magenta);
                break;
            case 2: 
            	setBallColor(LinxBase.BallColor.Blue);
                break;
            case 3: 
            	setBallColor(LinxBase.BallColor.Yellow);
                break;
            case 4: 
            	setBallColor(LinxBase.BallColor.Green);
                break;
        }
    }

    /**
     * Returns a random possible ball color.
     * @return the random Color
     */
    public static LinxBase.BallColor getRandomColor()
    {
        int n = Engine.BaseCode.random.nextInt(5);
        switch (n)
        {
            case 1:
                return LinxBase.BallColor.Red;
            case 2:
                return LinxBase.BallColor.Magenta;                    
            case 3:
                return LinxBase.BallColor.Blue;
            case 4:
                return LinxBase.BallColor.Yellow;
            default:
                return LinxBase.BallColor.Green;
        }
    }

    /**
     * Returns a random Power from all possible powers.
     * @return the randomly chosen Power
     */
    public static Power getRandomPower()
    {
        int n = Engine.BaseCode.random.nextInt(6);
        switch (n)
        {
            case 1:
                return Power.Bomb;
            case 2:
                return Power.Slow;
            case 3:
                return Power.Stop;
            case 4:
                return Power.Wildcard;
            case 5:
                return Power.Speed;
            default:
                return Power.Invisiball;
        }
    }
    
    /**
     * Returns the ball's radius.
     * @return the float radius
     */
    public float getRadius()
    {
    	return this.getHeight()/2f; 
    }
        
    /**
     * Sets the balls radius.
     * @param f the float size for the radius
     */
    public void setRadius(float f)
    {
    	setSize(f*2.0f);
    }
}
