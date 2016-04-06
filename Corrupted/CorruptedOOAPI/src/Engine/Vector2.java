
package Engine;

public class Vector2
{
  public static final Vector2 unitX = new Vector2(1.0f, 0.0f);
  public static final Vector2 unitY = new Vector2(0.0f, 1.0f);

  private float x = 0.0f;
  private float y = 0.0f;

  public Vector2()
  {
  }

  public Vector2(Vector2 other)
  {
    set(other);
  }

  public Vector2(float valueX, float valueY)
  {
    set(valueX, valueY);
  }

  /**
   * Get the x value.
   * 
   * @return - The x value.
   */
  public float getX()
  {
    return x;
  }

  /**
   * Set the x value.
   * 
   * @param value
   *          - The new x value.
   */
  public void setX(float value)
  {
    x = value;
  }

  /**
   * Get the y value.
   * 
   * @return - The y value.
   */
  public float getY()
  {
    return y;
  }

  /**
   * Set the y value.
   * 
   * @param value
   *          - The new y value.
   */
  public void setY(float value)
  {
    y = value;
  }

  /**
   * Set both x and y value at the same time.
   * 
   * @param valueX
   *          - The new x value.
   * @param valueY
   *          - The new y value.
   */
  public void set(float valueX, float valueY)
  {
    x = valueX;
    y = valueY;
  }

  /**
   * Set this vector to equal the given vector object.
   * 
   * @param other
   *          - The other vector to match this object to.
   */
  public void set(Vector2 other)
  {
    x = other.x;
    y = other.y;
  }

  /**
   * Offset both x and y value by the given amounts.
   * 
   * @param valueX
   *          - The amount to offset the x value.
   * @param valueY
   *          - The amount to offset the y value.
   */
  public void offset(float valueX, float valueY)
  {
    x += valueX;
    y += valueY;
  }

  /**
   * Offset x and y by given vector object.
   * 
   * @param other
   *          - The amount to offset by.
   */
  public void offset(Vector2 other)
  {
    x += other.x;
    y += other.y;
  }

  /**
   * Add the given vector to this vector.
   * 
   * @param other
   *          - The vector to add from.
   * @return - This object.
   */
  public Vector2 add(Vector2 other)
  {
    x += other.x;
    y += other.y;

    return this;
  }

  /**
   * Subtract the given vector to this vector.
   * 
   * @param other
   *          - The vector to add from.
   * @return - This object.
   */
  public Vector2 sub(Vector2 other)
  {
    x -= other.x;
    y -= other.y;

    return this;
  }

  /**
   * Multiply the given value to both x and y values.
   * 
   * @param value
   *          - The value to multiple with.
   * @return - This object.
   */
  public Vector2 mult(float value)
  {
    x *= value;
    y *= value;

    return this;
  }

  /**
   * Get the length of the vector. The calculation will not perform the
   * expensive square root calculation.
   * 
   * @return - The squared length of the vector.
   */
  public float lengthSQRD()
  {
    return (float)((x * x) + (y * y));
  }

  /**
   * Get the length of the vector.
   * 
   * @return - The length of the vector.
   */
  public float length()
  {
    return (float)Math.sqrt((x * x) + (y * y));
  }

  /**
   * Return a normalized copy of this vector.
   * 
   * @return - The normalized copy of this vector.
   */
  public Vector2 normalized()
  {
    return clone().normalize();
  }

  /**
   * Normalize this vector to have a length of one.
   * 
   * @return - This vector after applying normalization.
   */
  public Vector2 normalize()
  {
    float length = length();

    x /= length;
    y /= length;

    return this;
  }

  /**
   * Rotate this vector by the given angle in degrees.
   * 
   * @param angle
   *          - The angled in degrees.
   * @return - This vector after applying rotation.
   */
  public Vector2 rotate(float angle)
  {
    angle = (float)Math.toRadians(angle);

    float cosVal = (float)Math.cos(angle);
    float sinVal = (float)Math.sin(angle);

    float newX = (x * cosVal) - (y * sinVal);
    float newY = (x * sinVal) + (y * cosVal);

    x = newX;
    y = newY;

    return this;
  }

  public Vector2 clone()
  {
    return (new Vector2(this));
  }

  /**
   * Calculate the dot product between two vectors.
   * 
   * @param v
   *          - The first vector.
   * @param u
   *          - The second vector.
   * @return - The dot product of the two vectors.
   */
  static public float dot(Vector2 v, Vector2 u)
  {
    return (v.x * u.x) + (v.y * u.y);
  }

  /**
   * Rotate the given vector by the angle in radians.
   * 
   * @param v
   *          - The vector to rotate.
   * @param angleInRadian
   *          - The amount to rotate in radians.
   * @return - The vector after being rotated.
   */
  static public Vector2 rotateVectorByAngle(Vector2 v, float angleInRadian)
  {
    return v.rotate((float)Math.toDegrees(angleInRadian));
  }
}
