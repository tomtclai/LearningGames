package Engine;

public class World {
	public enum BoundCollidedStatus {
		LEFT, TOP, RIGHT, BOTTOM, INSIDEBOUND
	};

	private GameWindow window = null;

	private Vector2 worldSize = new Vector2(1.0f, 1.0f);
	private Vector2 worldOrg = new Vector2(1.0f, 1.0f);

	// Lower res HDTV: 1280x720 same aspect ratio
	// private final int AppWindowWidth = 1920;
	// private final int AppWindowHeight = 1080;

	public World(GameWindow win) {
		window = win;
	}

	/**
	 * Set the world coordinate system. World height is based on world width and
	 * the window aspec ratio.
	 * 
	 * @param x
	 *            - The number of world x coordinates that fit inside the
	 *            window.
	 */
	public void SetWorldCoordinate(float x) {
		worldSize.set(x, (window.getHeight() / (float) window.getWidth()) * x);
		// worldCoords.set(x, (AppWindowHeight / (float)AppWindowWidth) * x);
	}

	/**
	 * Set the world coordinate system.
	 * 
	 * @param x
	 *            - The number of world x coordinates that fit inside the
	 *            window.
	 * @param y
	 *            - The number of world y coordinates that fit inside the
	 *            window.
	 */
	public void SetWorldCoordinate(float x, float y) {
		worldSize.set(x, y);
	}

	/**
	 * Convert the given world x coordinate to screen pixel space.
	 * 
	 * @param pos
	 *            - The coordinate to convert to pixel space.
	 * @return - The coordinate converted to pixel space.
	 */
	public float worldToScreenX(float pos) {
		return ((pos - worldOrg.getX()) * (window.getWidth() / worldSize.getX()));
	}

	/**
	 * Convert the given world y coordinate to screen pixel space.
	 * 
	 * @param pos
	 *            - The coordinate to convert to pixel space.
	 * @return - The coordinate converted to pixel space.
	 */
	public float worldToScreenY(float pos) {
		return window.getHeight()
				- ((pos - worldOrg.getY()) * (window.getHeight() / worldSize
						.getY()));
	}

	/**
	 * Convert the given screen pixel x coordinate to world unit space.
	 * 
	 * @param pos
	 *            - The coordinate to convert to world space.
	 * @return - The coordinate converted to world space.
	 */
	public float screenToWorldX(float pos) {
		return (pos * (worldSize.getX() / window.getWidth())) + worldOrg.getX();
	}

	/**
	 * Convert the given screen pixel y coordinate to world unit space.
	 * 
	 * @param pos
	 *            - The coordinate to convert to world space.
	 * @return - The coordinate converted to world space.
	 */
  public float screenToWorldY(float pos)
  {
    return worldSize.getY() -
        (pos * (worldSize.getY() / window.getHeight())) +
        worldOrg.getY();
  }
  
  
  /**
   * Scales a Vector2 between screen units and world units
   * (Scale only, does not take into account screen/world translation)
   * @author Brian Chau
   * @param pos 
   * @return scaled Vector2
   */
  public Vector2 scaleVectorScreenToWorld(Vector2 pos)
  {
	  return new Vector2(pos.getX() * (worldSize.getX()/window.getWidth()), (pos.getY() * (worldSize.getY()/window.getHeight())));
  }
  
  /**
   * Scales a Vector2 between screen units and world units
   * (Scale only, does not take into account screen/world translation)
   * @author Brian Chau
   * @param pos 
   * @return scaled Vector2
   */
  public Vector2 scaleVectorWorldToScreen(Vector2 pos)
  {
	  return new Vector2( pos.getX() / (worldSize.getX()/window.getWidth()), pos.getY() / (worldSize.getY()/window.getHeight()));
	}

	/**
	 * Get the width of the world.
	 * 
	 * @return - The width of the world.
	 */
	public float getWidth() {
		return worldSize.getX();
	}

	/**
	 * Get the height of the world.
	 * 
	 * @return - The height of the world.
	 */
	public float getHeight() {
		return worldSize.getY();
	}

	/**
	 * Set the world to be centered around the given coordinates.
	 * 
	 * @param x
	 *            - Coordinate to center x on.
	 * @param y
	 *            - Coordinate to center y on.
	 */
	public void centerPosition(float x, float y) {
		worldOrg.set(x - (worldSize.getX() * 0.5f), y
				- (worldSize.getY() * 0.5f));
	}

	/**
	 * Set the world to be centered around the given coordinates.
	 * 
	 * @param pos
	 *            - Coordinates to center on.
	 */
	public void centerPosition(Vector2 pos) {
		worldOrg.set(pos.getX() - (worldSize.getX() * 0.5f), pos.getY()
				- (worldSize.getY() * 0.5f));
	}

	/**
	 * Set the world position based on the lower left corner.
	 * 
	 * @param x
	 *            - Position at x coordinate.
	 * @param y
	 *            - Position at y coordinate.
	 */
	public void setPosition(float x, float y) {
		worldOrg.set(x, y);
	}

	/**
	 * Offset the center of the world.
	 * 
	 * @param x
	 *            - Amount to offset in the x direction.
	 * @param y
	 *            - Amount to offset in the y direction.
	 */
	public void offsetPosition(float x, float y) {
		worldOrg.offset(x, y);
	}

	/**
	 * Get the x position of the world.
	 * 
	 * @return - The x position of the world.
	 */
	public float getPositionX() {
		return worldOrg.getX();
	}

	/**
	 * Get the y position of the world.
	 * 
	 * @return - The y position of the world.
	 */
	public float getWorldPositionY() {
		return worldOrg.getY();
	}

	/**
	 * Checks if the given object is not entirely inside the world.
	 * 
	 * @param obj
	 *            - The game object to check if it is not entirely inside the world.
	 * @return - The side of the world that the object is overlapping.
	 */
	public BoundCollidedStatus collideWorldBound(GameObject obj) {
		float halfW = obj.getWidth()*0.5f;
		float halfH = obj.getHeight()*0.5f;
		if (obj.getCenterX() < (halfW)) {
			return BoundCollidedStatus.LEFT;
		}

		if (obj.getCenterX() > (worldSize.getX() - halfW)) {
			return BoundCollidedStatus.RIGHT;
		}

		if (obj.getCenterY() < (halfH)) {
			return BoundCollidedStatus.BOTTOM;
		}

		if (obj.getCenterY() > (worldSize.getY() - halfH)) {
			return BoundCollidedStatus.TOP;
		}

		return BoundCollidedStatus.INSIDEBOUND;
	}

	/**
	 * Check if the given object is not inside of the given world bound.
	 * 
	 * @param obj
	 *            - The game object to check if it is not entirely inside the world.
	 * @param whichBound
	 *            - The world bound to check the given object against.
	 * @return - The side of the world that the object is overlapping.
	 */
	public boolean checkWorldBound(GameObject obj, BoundCollidedStatus whichBound) {
		float halfW = obj.getWidth()*0.5f;
		float halfH = obj.getHeight()*0.5f;
		if (whichBound == BoundCollidedStatus.LEFT
				&& obj.getCenterX() < (halfW)) {
			return true;
		}

		if (whichBound == BoundCollidedStatus.RIGHT
				&& obj.getCenterX() > (worldSize.getX() - halfH)) {
			return true;
		}

		if (whichBound == BoundCollidedStatus.BOTTOM
				&& obj.getCenterY() < (halfW)) {
			return true;
		}

		if (whichBound == BoundCollidedStatus.TOP
				&& obj.getCenterY() > (worldSize.getY() - halfW)) {
			return true;
		}

		return false;
	}

	/**
	 * Check if any part of the primitive is inside the world.
	 * 
	 * @param obj
	 *            - The game object to check if it is inside.
	 * @return - True if any part of the primitive is inside the world,
	 *         otherwise false.
	 */
	public boolean isInsideWorldBound(GameObject obj) {
		float halfW = obj.getWidth()*0.5f;
		float halfH = obj.getHeight()*0.5f;
		return (obj.getCenterX() >= (-halfW)
				&& obj.getCenterX() < (worldSize.getX() + (halfH))
				&& obj.getCenterY() >= (-halfH) && obj.getCenterY() < (worldSize.getY() + (halfH)));
	}

	/**
	 * Forces the given object to be inside of the world bounds.
	 * 
	 * @param obj
	 *            - The game object to clamp inside the world bounds.
	 */
	public void clampAtWorldBound(GameObject obj) {
		BoundCollidedStatus status = collideWorldBound(obj);

		switch (status) {
		case TOP: {
			obj.setCenterY(worldSize.getY()
					- (obj.getHeight() * 0.5f));

			break;
		}

		case BOTTOM: {
			obj.setCenterY(obj.getHeight() * 0.5f);

			break;
		}

		case LEFT: {
			obj.setCenterX(obj.getWidth() * 0.5f);

			break;
		}

		case RIGHT: {
			obj.setCenterX(worldSize.getX()
					- (obj.getWidth() * 0.5f));

			break;
		}

		default:
			break;
		}
	}
}
