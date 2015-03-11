package SpaceSmasher;

import java.awt.Rectangle;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.ResourceHandler;
import Engine.Vector2;

public class Trap extends GameObject {
	private static final String LEFT_INDICATOR_ACTIVATED = "bumper/activated_indicator_left.png";
	private static final String RIGHT_INDICATOR_ACTIVATED = "bumper/activated_indicator_right.png";

	private static final String LEFT_INDICATOR_DEACTIVATING = "bumper/deactivating_indicator_left.png";
	private static final String RIGHT_INDICATOR_DEACTIVATING = "bumper/deactivating_indicator_right.png";

	private static final String LEFT_INDICATOR_REACTIVATING = "bumper/reactivating_indicator_left.png";
	private static final String RIGHT_INDICATOR_REACTIVATING = "bumper/reactivating_indicator_right.png";

	private static final String INDICATOR_DEACTIVATED = "bumper/SideBumperIndicator_Deactivated.png";

	private static final String LEFT_DEFAULT_ACTIVE_TEXTURE = "bumper/SideBumper_Ready_Left.png";
	private static final String RIGHT_DEFAULT_ACTIVE_TEXTURE = "bumper/SideBumper_Ready_Right.png";

	private static final String LEFT_DEFUALT_INACTIVE_TEXTURE = "bumper/SideBumper_Deactivated_Left.png";
	private static final String RIGHT_DEFUALT_INACTIVE_TEXTURE = "bumper/SideBumper_Deactivated_Right.png";

	public int translateTime = 400;
	/** the current Frame in the animation */
	public int translatePos = 0;
	/** The Starting position of the Animation */
	public Vector2 translatePosStart = new Vector2();
	/** The Ending position of the Animation */
	public Vector2 translatePosEnd = new Vector2();

	private GameObject indicator;
	private static final float INDICATOR_OFFSET = 2.5f;
	private static final int INDICATOR_SPRITE_HEIGHT = 198;
	private static final int INDICATOR_SPRITE_WIDTH = 24;

	private static final int ACTIVATED_INDICATOR_FRAMES = 39;
	private static final int ACTIVATED_INDICATOR_TICK_PER_FRAME = 1;
	private static final int DEACTIVATING_INDICATOR_FRAMES = 14;
	private static final int DEACTIVATING_INDICATOR_TICK_PER_FRAME = 5;
	private static final int REACTIVATING_INDICATOR_FRAMES = 72;
	private static final int REACTIVATING_INDICATOR_TICK_PER_FRAME = 5;

	private int deactivatingCounter;
	private int reactivatingCounter;

	/**
	 * will load and sounds or images required for this class to function
	 * 
	 * @param resources handler to be used for pre-loading.
	 */
	public static void preloadResources(ResourceHandler resources) {
		if (resources != null) {
			resources.preloadImage(LEFT_INDICATOR_ACTIVATED);
			resources.preloadImage(RIGHT_INDICATOR_ACTIVATED);
			resources.preloadImage(LEFT_INDICATOR_DEACTIVATING);
			resources.preloadImage(RIGHT_INDICATOR_DEACTIVATING);
			resources.preloadImage(LEFT_INDICATOR_REACTIVATING);
			resources.preloadImage(RIGHT_INDICATOR_REACTIVATING);
			resources.preloadImage(INDICATOR_DEACTIVATED);
			resources.preloadImage(LEFT_DEFAULT_ACTIVE_TEXTURE);
			resources.preloadImage(RIGHT_DEFAULT_ACTIVE_TEXTURE);
			resources.preloadImage(LEFT_DEFUALT_INACTIVE_TEXTURE);
			resources.preloadImage(RIGHT_DEFUALT_INACTIVE_TEXTURE);
		}
	}

	protected enum WallState {
		ACTIVE, DEACTIVATING, INACTIVE, REACTIVATING
	}

	private WallState wallState = WallState.INACTIVE;

	public Trap() {
		setSize(BaseCode.world.getWidth() * 0.04f,
				BaseCode.world.getHeight() * 0.2f);

		indicator = new GameObject();
		indicator.setSize(BaseCode.world.getWidth() * 0.01f,
				BaseCode.world.getHeight() * 0.15f);

		setToInactive();
	}

	/**
	 * Bounce the ball off the paddle.
	 * 
	 * @param ball
	 *            - The ball to bounce.
	 */
	public void reflect(Ball ball) {
		if (ball != null) {
			Vector2 dir = pushOutCircle(ball);

			ball.bounce(dir);
		}
	}

	/**
	 * Make the wall look activated.
	 */
	public void activate() {
		if (wallState == WallState.INACTIVE) {
			wallState = WallState.REACTIVATING;
			reactivatingCounter = REACTIVATING_INDICATOR_FRAMES
					* (REACTIVATING_INDICATOR_TICK_PER_FRAME - 2);
			// Right
			if ((BaseCode.world.getWidth() / 2) - getCenterX() < 0) {
				super.setImage(RIGHT_DEFUALT_INACTIVE_TEXTURE);
				indicator.setSpriteSheet(RIGHT_INDICATOR_REACTIVATING,
						INDICATOR_SPRITE_WIDTH, INDICATOR_SPRITE_HEIGHT,
						REACTIVATING_INDICATOR_FRAMES,
						REACTIVATING_INDICATOR_TICK_PER_FRAME);
			}
			// Left
			else {
				super.setImage(LEFT_DEFUALT_INACTIVE_TEXTURE);
				indicator.setSpriteSheet(LEFT_INDICATOR_REACTIVATING,
						INDICATOR_SPRITE_WIDTH, INDICATOR_SPRITE_HEIGHT,
						REACTIVATING_INDICATOR_FRAMES,
						REACTIVATING_INDICATOR_TICK_PER_FRAME);
			}
			indicator.setUsingSpriteSheet(true);
		}
	}

	/**
	 * Make the wall look deactivated.
	 */
	public void deactivate() {
		if (wallState == WallState.ACTIVE) {
			wallState = WallState.DEACTIVATING;
			deactivatingCounter = DEACTIVATING_INDICATOR_FRAMES
					* (DEACTIVATING_INDICATOR_TICK_PER_FRAME - 2);
			// Right
			if ((BaseCode.world.getWidth() / 2) - getCenterX() < 0) {
				super.setImage(RIGHT_DEFUALT_INACTIVE_TEXTURE);
				indicator.setSpriteSheet(RIGHT_INDICATOR_DEACTIVATING,
						INDICATOR_SPRITE_WIDTH, INDICATOR_SPRITE_HEIGHT,
						DEACTIVATING_INDICATOR_FRAMES,
						DEACTIVATING_INDICATOR_TICK_PER_FRAME);
			}
			// Left
			else {
				super.setImage(LEFT_DEFUALT_INACTIVE_TEXTURE);
				indicator.setSpriteSheet(LEFT_INDICATOR_DEACTIVATING,
						INDICATOR_SPRITE_WIDTH, INDICATOR_SPRITE_HEIGHT,
						DEACTIVATING_INDICATOR_FRAMES,
						DEACTIVATING_INDICATOR_TICK_PER_FRAME);
			}
			indicator.setUsingSpriteSheet(true);
		}

	}

	private void setToInactive() {
		wallState = WallState.INACTIVE;
		// Right
		if ((BaseCode.world.getWidth() / 2) - getCenterX() < 0) {
			super.setImage(RIGHT_DEFUALT_INACTIVE_TEXTURE);
			indicator.setImage(INDICATOR_DEACTIVATED);
		}
		// Left
		else {
			super.setImage(LEFT_DEFUALT_INACTIVE_TEXTURE);
			indicator.setImage(INDICATOR_DEACTIVATED);
		}
		indicator.setUsingSpriteSheet(false);
	}

	private void setToActive() {
		wallState = WallState.ACTIVE;

		// Right
		if ((BaseCode.world.getWidth() / 2) - getCenterX() < 0) {
			super.setImage(RIGHT_DEFAULT_ACTIVE_TEXTURE);
			indicator.setSpriteSheet(RIGHT_INDICATOR_ACTIVATED,
					INDICATOR_SPRITE_WIDTH, INDICATOR_SPRITE_HEIGHT,
					ACTIVATED_INDICATOR_FRAMES,
					ACTIVATED_INDICATOR_TICK_PER_FRAME);
		}
		// Left
		else {
			super.setImage(LEFT_DEFAULT_ACTIVE_TEXTURE);
			indicator.setSpriteSheet(LEFT_INDICATOR_ACTIVATED,
					INDICATOR_SPRITE_WIDTH, INDICATOR_SPRITE_HEIGHT,
					ACTIVATED_INDICATOR_FRAMES,
					ACTIVATED_INDICATOR_TICK_PER_FRAME);
		}
		indicator.setUsingSpriteSheet(true);
	}

	/*
	 Kelvin comment 11/5/2014
	 This function DOES NOT make any sense
	  1. Update of state should not happen in draw
	  2. Why will wallState be inconsistent (only set in the setToActive/setToInactive functions
	  3. Why calling indicator.draw() here?!
	 
	public void draw() {
		// INACTIVE
		if (wallState == WallState.INACTIVE) {
			// Right
			if ((BaseCode.world.getWidth() / 2) - getCenterX() < 0) {
				super.setImage(RIGHT_DEFUALT_INACTIVE_TEXTURE);
				indicator.setImage(INDICATOR_DEACTIVATED);
			}
			// Left
			else {
				super.setImage(LEFT_DEFUALT_INACTIVE_TEXTURE);
				indicator.setImage(INDICATOR_DEACTIVATED);
			}
		}
		super.draw();
		indicator.draw();
	}
	*/

	public void update() {
		translatePos++;
		translatePos = translatePos % (translateTime * 2);
		setCenterToAnimatedPosition(getCenter());
		updateIndicator();

	}

	public boolean isActive() {
		return wallState == WallState.ACTIVE;
	}

	public boolean isNotActive() {
		return wallState == WallState.INACTIVE;
	}

	private void updateIndicator() {
		setCenterToAnimatedPosition(indicator.getCenter());
		// Right
		if ((BaseCode.world.getWidth() / 2) - getCenterX() < 0) {
			indicator.setCenterX(indicator.getCenterX() + INDICATOR_OFFSET);
		}
		// Left
		else {
			indicator.setCenterX(indicator.getCenterX() - INDICATOR_OFFSET);
		}
		if (wallState == WallState.DEACTIVATING) {
			if (deactivatingCounter <= 0) {
				setToInactive();
			} else {
				deactivatingCounter--;
			}
		} else if (wallState == WallState.REACTIVATING) {
			if (reactivatingCounter <= 0) {
				setToActive();
			} else {
				reactivatingCounter--;
			}
		}

	}

	/**
	 * Will Set the the center to the current Animated Position
	 */
	protected void setCenterToAnimatedPosition(Vector2 center) {
		if (translateTime > 0) {
			// t = current Time
			// T = total time to animate
			// D = Total Distance to Travel
			// S = Starting point
			// P = current Position
			// P = (D / 2)(1 + Cos((((pi / 2) * t)-(T(pi / 2)))/(T / 2))) + S;
			center.setX((float) (((translatePosEnd.getX() - translatePosStart
					.getX()) / 2f)
					* (1f + Math
							.cos((((Math.PI / 2f) * (float) translatePos) - ((Math.PI / 2) * (float) translateTime))
									/ ((float) translateTime / 2f))) + translatePosStart
					.getX()));
			center.setY((float) (((translatePosEnd.getY() - translatePosStart
					.getY()) / 2f)
					* (1f + Math
							.cos((((Math.PI / 2f) * (float) translatePos) - ((Math.PI / 2) * (float) translateTime))
									/ ((float) translateTime / 2f))) + translatePosStart
					.getY()));
		} else {
			center.set(translatePosEnd);
		}
	}

	/**
	 * Clears away the indicator. Warning, this is a cleanup method, using it
	 * otherwise will result in null pointer execptions.
	 */
	public void clearIndicator() {
		indicator.destroy();
		indicator = null;
	}
}