
package Engine;

public class World
{
  public enum BoundCollidedStatus
  {
    LEFT, TOP, RIGHT, BOTTOM, INSIDEBOUND
  };

  private GameWindow window = null;

  private Vector2 worldCoords = new Vector2(1.0f, 1.0f);
  private Vector2 worldPosition = new Vector2(1.0f, 1.0f);

  // Lower res HDTV: 1280x720 same aspect ratio
  //private final int AppWindowWidth = 1920;
  //private final int AppWindowHeight = 1080;

  public World(GameWindow win)
  {
    window = win;
  }

  /**
   * Set the world coordinate system. World height is based on world width and
   * the window aspec ratio.
   * 
   * @param x
   *          - The number of world x coordinates that fit inside the window.
   */
  public void SetWorldCoordinate(float x)
  {
    worldCoords.set(x, (window.getHeight() / (float)window.getWidth()) * x);
    //worldCoords.set(x, (AppWindowHeight / (float)AppWindowWidth) * x);
  }

  /**
   * Set the world coordinate system.
   * 
   * @param x
   *          - The number of world x coordinates that fit inside the window.
   * @param y
   *          - The number of world y coordinates that fit inside the window.
   */
  public void SetWorldCoordinate(float x, float y)
  {
	worldCoords.set(x, y);
  }
  
  /**
   * Convert the given world x coordinate to screen pixel space.
   * 
   * @param pos
   *          - The coordinate to convert to pixel space.
   * @return - The coordinate converted to pixel space.
   */
  public float worldToScreenX(float pos)
  {
    return ((pos - worldPosition.getX()) * (window.getWidth() / worldCoords
        .getX()));
  }

  /**
   * Convert the given world y coordinate to screen pixel space.
   * 
   * @param pos
   *          - The coordinate to convert to pixel space.
   * @return - The coordinate converted to pixel space.
   */
  public float worldToScreenY(float pos)
  {
    return window.getHeight() -
        ((pos - worldPosition.getY()) * (window.getHeight() / worldCoords
            .getY()));
  }

  /**
   * Convert the given screen pixel x coordinate to world unit space.
   * 
   * @param pos
   *          - The coordinate to convert to world space.
   * @return - The coordinate converted to world space.
   */
  public float screenToWorldX(float pos)
  {
    return (pos * (worldCoords.getX() / window.getWidth())) +
        worldPosition.getX();
  }

  /**
   * Convert the given screen pixel y coordinate to world unit space.
   * 
   * @param pos
   *          - The coordinate to convert to world space.
   * @return - The coordinate converted to world space.
   */
  public float screenToWorldY(float pos)
  {
    return worldCoords.getY() -
        (pos * (worldCoords.getY() / window.getHeight())) +
        worldPosition.getY();
  }

  /**
   * Get the width of the world.
   * 
   * @return - The width of the world.
   */
  public float getWidth()
  {
    return worldCoords.getX();
  }

  /**
   * Get the height of the world.
   * 
   * @return - The height of the world.
   */
  public float getHeight()
  {
    return worldCoords.getY();
  }

  /**
   * Set the world to be centered around the given coordinates.
   * 
   * @param x
   *          - Coordinate to center x on.
   * @param y
   *          - Coordinate to center y on.
   */
  public void centerPosition(float x, float y)
  {
    worldPosition.set(x - (worldCoords.getX() * 0.5f), y -
        (worldCoords.getY() * 0.5f));
  }

  /**
   * Set the world to be centered around the given coordinates.
   * 
   * @param pos
   *          - Coordinates to center on.
   */
  public void centerPosition(Vector2 pos)
  {
    worldPosition.set(pos.getX() - (worldCoords.getX() * 0.5f), pos.getY() -
        (worldCoords.getY() * 0.5f));
  }

  /**
   * Set the world position based on the lower left corner.
   * 
   * @param x
   *          - Position at x coordinate.
   * @param y
   *          - Position at y coordinate.
   */
  public void setPosition(float x, float y)
  {
    worldPosition.set(x, y);
  }

  /**
   * Offset the center of the world.
   * 
   * @param x
   *          - Amount to offset in the x direction.
   * @param y
   *          - Amount to offset in the y direction.
   */
  public void offsetPosition(float x, float y)
  {
    worldPosition.offset(x, y);
  }

  /**
   * Get the x position of the world.
   * 
   * @return - The x position of the world.
   */
  public float getPositionX()
  {
    return worldPosition.getX();
  }

  /**
   * Get the y position of the world.
   * 
   * @return - The y position of the world.
   */
  public float getWorldPositionY()
  {
    return worldPosition.getY();
  }

  /**
   * Checks if the given object is not entirely inside the world.
   * 
   * @param primitive
   *          - The object to check if it is not entirely inside the world.
   * @return - The side of the world that the object is overlapping.
   */
  public BoundCollidedStatus collideWorldBound(Primitive primitive)
  {
    if(primitive.center.getX() < (primitive.size.getX() * 0.5f))
    {
      return BoundCollidedStatus.LEFT;
    }

    if(primitive.center.getX() > (worldCoords.getX() - (primitive.size.getX() * 0.5f)))
    {
      return BoundCollidedStatus.RIGHT;
    }

    if(primitive.center.getY() < (primitive.size.getY() * 0.5f))
    {
      return BoundCollidedStatus.BOTTOM;
    }

    if(primitive.center.getY() > (worldCoords.getY() - (primitive.size.getY() * 0.5f)))
    {
      return BoundCollidedStatus.TOP;
    }

    return BoundCollidedStatus.INSIDEBOUND;
  }

  /**
   * Check if the given object is not inside of the given world bound.
   * 
   * @param primitive
   *          - The object to check if it is not entirely inside the world.
   * @param whichBound
   *          - The world bound to check the given object against.
   * @return - The side of the world that the object is overlapping.
   */
  public boolean checkWorldBound(Primitive primitive,
      BoundCollidedStatus whichBound)
  {
    if(whichBound == BoundCollidedStatus.LEFT &&
        primitive.center.getX() < (primitive.size.getX() * 0.5f))
    {
      return true;
    }

    if(whichBound == BoundCollidedStatus.RIGHT &&
        primitive.center.getX() > (worldCoords.getX() - (primitive.size.getX() * 0.5f)))
    {
      return true;
    }

    if(whichBound == BoundCollidedStatus.BOTTOM &&
        primitive.center.getY() < (primitive.size.getY() * 0.5f))
    {
      return true;
    }

    if(whichBound == BoundCollidedStatus.TOP &&
        primitive.center.getY() > (worldCoords.getY() - (primitive.size.getY() * 0.5f)))
    {
      return true;
    }

    return false;
  }

  /**
   * Check if any part of the primitive is inside the world.
   * 
   * @param primitive
   *          - The primitive to check if it is inside.
   * @return - True if any part of the primitive is inside the world, otherwise
   *         false.
   */
  public boolean isInsideWorldBound(Primitive primitive)
  {
    return (primitive.center.getX() >= (-primitive.size.getX() * 0.5f) &&
        primitive.center.getX() < (worldCoords.getX() + (primitive.size.getX() * 0.5f)) &&
        primitive.center.getY() >= (-primitive.size.getY() * 0.5f) && primitive.center
        .getY() < (worldCoords.getY() + (primitive.size.getY() * 0.5f)));
  }

  /**
   * Forces the given object to be inside of the world bounds.
   * 
   * @param primitive
   *          - The object to clamp inside the world bounds.
   */
  public void clampAtWorldBound(Primitive primitive)
  {
    BoundCollidedStatus status = collideWorldBound(primitive);

    switch(status)
    {
      case TOP:
      {
        primitive.center.setY(worldCoords.getY() -
            (primitive.size.getY() * 0.5f));

        break;
      }

      case BOTTOM:
      {
        primitive.center.setY(primitive.size.getY() * 0.5f);

        break;
      }

      case LEFT:
      {
        primitive.center.setX(primitive.size.getX() * 0.5f);

        break;
      }

      case RIGHT:
      {
        primitive.center.setX(worldCoords.getX() -
            (primitive.size.getX() * 0.5f));

        break;
      }

      default:
        break;
    }
  }
}
