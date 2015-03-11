package LinxLibrary;

import Engine.Vector2;
import LinxLibrary.LinxBase.Power;

class BallOnPath extends Ball {
    private Path mPath;
    private float tValue;   //0-1f
    private boolean animating = false;
    private float animateSpeed = .2f;
    
    public static float DEFAULT_SPEED = 2f;
    public float mSpeed = DEFAULT_SPEED;
    
    public BallOnPath(Path thePath, Path.PathTravelDirection dir)
    {
    	super();
        init(thePath, dir);
    }

    public BallOnPath(Path thePath, BallOnPath ballInFront, Path.PathTravelDirection dir)
    {
    	super();
        init(thePath, dir);

        // get the tValue of the ball in front
        tValue = ballInFront.tValue;
        
        // set Center and Direction
        setCenterBehind(ballInFront, dir);
    }

    public float getT()
    {
        return tValue;
    }
    public void setT(float newT)
    {
        tValue = newT;
    }
    public Vector2 getDirection()
    {
        return new Vector2(mPath.getDirection(tValue).getX(), mPath.getDirection(tValue).getY());
    }
        
    public void setAnimating(boolean isAnimating)
    {
        animating = isAnimating;
        animateSpeed = .2f; //looks and works best
    }
    public boolean isAnimating()
    {
        return animating;
    }

    public void setCenterBehind(BallOnPath refBall, Path.PathTravelDirection dir)
    {
        // check if "behind" is closer to the start or end, to position properly
        float radius, refRadius;
        if (dir == Path.PathTravelDirection.ePathTravelForward)
        {
            radius = getRadius();
            refRadius = refBall.getRadius();
            // rotate forward if not powerball
            if (!mIsPowerBall || mPower == Power.Wildcard)
                if (mSpeed > 0)
                	changeRotationBy(-mSpeed * 2);
        }
        else
        {
            radius = -getRadius();
            refRadius = -refBall.getRadius();
            // rotate backward if not powerball
            if (!mIsPowerBall || mPower == Power.Wildcard)
                if (mSpeed > 0)
                    changeRotationBy(mSpeed * 2);
        }
        // step by refRadius distance, then by radius distance to be fully outside refBall
        float resolution = .05f;
        float t = refBall.tValue;
        
        // do each step twice to be more accurate around corners
        t -= mPath.getSpeed(t) * refRadius / 2 / resolution;
        t -= mPath.getSpeed(t) * refRadius / 2 / resolution;
        t -= mPath.getSpeed(t) * radius / 2 / resolution;
        t -= mPath.getSpeed(t) * radius / 2 / resolution;

        tValue = t;

        if (checkValid())
        {
            if (animating)
            {
                Vector2 newPos = SmoothStep(new Vector2(this.getCenterX(), this.getCenterY()), mPath.getPoint(t), animateSpeed);
                animateSpeed += animateSpeed * .4f;
                animateSpeed = Clamp(animateSpeed, 0, 1);
                if (animateSpeed == 1)
                    animating = false;
                setCenter(new Engine.Vector2(newPos.getX(), newPos.getY()));
                // increase t so we would be overlapping the ball in front of us if we
                // were actually in the chain, and increase less and less as we are closer
                // to completing the animation so we end in our correct position
                tValue += (refBall.tValue - tValue) * (1f - animateSpeed);
            }
            else
                setCenter(new Engine.Vector2(mPath.getPoint(t).getX(), mPath.getPoint(t).getY()));
        }
    }

    // Everything to a good defualt value, except 
    // Center, and VelocityDirection
    private void init(Path thePath, Path.PathTravelDirection dir)
    {
        mPath = thePath;
        tValue = 0;
        setCenter(mPath.getPoint(0).getX(), mPath.getPoint(0).getY());
        Vector2 direction = mPath.getDirection(0);
        setRotation((float)Math.atan2(direction.getY(), direction.getX()));

        mSpeed = .5f;
        moveToFront();
    }
            
    private boolean checkValid()
    {
        // make sure tValue is 0-1
    	boolean valid = true;
    	
    	if (tValue > 1) {
    		valid = false;
    		destroy();
    	} else if (tValue < 0) {
    		setToInvisible();
    	} else {
    		setToVisible();
    	}
        return valid;
    	
        /*
    	if (tValue < 1)
            return true;
        else
        {
            destroy();
            return false;
        }
        */
    }
    
    public void update(Path.PathTravelDirection dir)
    {
        super.update();

        if (dir == Path.PathTravelDirection.ePathTravelForward)
        {
            // move the tValue based on current position
            tValue += mPath.getSpeed(tValue) * mSpeed;
            // rotate forward if not powerball
            if (!mIsPowerBall || mPower == Power.Wildcard)
                if (mSpeed > 0)
                    changeRotationBy(-mSpeed * 2);
        }
        else
        {
            tValue -= mPath.getSpeed(tValue) * mSpeed;
            // rotate backward if not powerball
            if (!mIsPowerBall || mPower == Power.Wildcard)
                if (mSpeed > 0)
                    changeRotationBy(mSpeed * 2);
        }

        // check if ball should be destroyed
        if (checkValid())
        {
            // actually move the ball to the new position at tValue
            if (animating)
            {
                // smoothly move to correct position, at increasing speed
                Vector2 newPos = SmoothStep(new Vector2(this.getCenterX(), this.getCenterY()), mPath.getPoint(tValue), animateSpeed);
                animateSpeed += animateSpeed * .4f;
                animateSpeed = Clamp(animateSpeed, 0, 1);
                if (animateSpeed == 1)
                    animating = false;
                // actually move
                this.setCenter(newPos.getX(), newPos.getY());
            }
            else
                this.setCenter(mPath.getPoint(tValue).getX(), mPath.getPoint(tValue).getY());
        }
    }

    public void updateFollow(Path.PathTravelDirection dir, BallOnPath refBall)
    {
        if (!refBall.isAlive())
        {
            // lead is gone, we are the lead now
            update(dir);
            return;
        }

        super.update();

        setCenterBehind(refBall, dir);

        checkValid();
    }
    
    //-------------------------------------------------------------------------
    // Code below courtesy of the MonoGame project
    // Obtained via GitHub
    //-------------------------------------------------------------------------    
    private static float Clamp(float value, float min, float max)
    {
        // First we check to see if we're greater than the max
        value = (value > max) ? max : value;


        // Then we check to see if we're less than the min.
        value = (value < min) ? min : value;


        // There's no check to see if min > max.
        return value;
    }

    private static float Hermite(float value1, float tangent1, float value2, float tangent2, float amount)
    {
        // All transformed to double not to lose precission
        // Otherwise, for high numbers of param:amount the result is NaN instead of Infinity
        double v1 = value1, v2 = value2, t1 = tangent1, t2 = tangent2, s = amount, result;
        double sCubed = s * s * s;
        double sSquared = s * s;


        if (amount == 0f)
            result = value1;
        else if (amount == 1f)
            result = value2;
        else
            result = (2 * v1 - 2 * v2 + t2 + t1) * sCubed +
                (3 * v2 - 3 * v1 - 2 * t1 - t2) * sSquared +
                t1 * s +
                v1;
        return (float)result;
    }

    private static float fSmoothStep(float value1, float value2, float amount)
    {
        // It is expected that 0 < amount < 1
        // If amount < 0, return value1
        // If amount > 1, return value2
        float result = Clamp(amount, 0f, 1f);
        result = Hermite(value1, 0f, value2, 0f, result);


        return result;
    }

    private static Vector2 SmoothStep(Vector2 value1, Vector2 value2, float amount)
    {
        return new Vector2(
            fSmoothStep(value1.getX(), value2.getX(), amount),
            fSmoothStep(value1.getY(), value2.getY(), amount));
    }

}
