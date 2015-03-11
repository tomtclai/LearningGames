
package SpaceSmasher;

import java.awt.Rectangle;

import Engine.GameObject;
import Engine.SetOfGameObjects;

@SuppressWarnings("serial")
public class PaddleSet extends SetOfGameObjects<Paddle>
{
  final float paddleSpeed = 2.5f;
  public float maxX = 1.0f;

  /**
   * Add a paddle to the set.
   */
  public void add()
  {
    Paddle paddle = new Paddle();
    Paddle previous;
    add(paddle);

    int index = size() - 1;

    if(index == 0)
    {
      previous = paddle;
    }
    else
    {
      if(size() <= 3)
      {
        previous = get(0);
      }
      else
      {
        previous = get(size() - 3);
      }

      if(index % 2 == 0)
      {
        paddle.setCenter(previous.getCenterX() + (paddle.getWidth() * 1.2f), previous.getCenterY());
      }
      else
      {
        paddle.setCenter(previous.getCenterX() - (paddle.getWidth() * 1.2f), previous.getCenterY());
      }
    }
  }

  /**
   * Add the given number of paddles to the set.
   * 
   * @param num
   *          - How many paddles to add.
   */
  public void add(int num)
  {
    for(int i = 0; i < num; i++)
    {
      add();
    }
  }

  /**
   * Get the first paddle in the set.
   * 
   * @return - The first paddle.
   */
  public Paddle getPaddle()
  {
    return getPaddle(0);
  }

  /**
   * Get the given paddle based on its index.
   * 
   * @param position
   *          - The paddle index to return.
   * @return - The paddle at the given index, will be null if invalid index.
   */
  public Paddle getPaddle(int position)
  {
    if(position >= 0 && position < size())
    {
      return get(position);
    }

    return null;
  }

  /**
   * Move all paddles to the left, locking the first paddle inside the world and
   * all other worlds maintain their fixed distance to the first paddle.
   */
  public void moveLeft()
  {
    GameObject curPaddle;

    float offAmount = 0.0f;

    for(int i = 0; i < size(); i++)
    {
      curPaddle = get(i);

      curPaddle.setCenterX(curPaddle.getCenterX() - paddleSpeed);

      // Only the first paddle will get clamped and
      // other paddles will be affected by the clamp by the same amount
      if(i == 0)
      {
        offAmount = curPaddle.getCenterX() + (curPaddle.getWidth() * 0.25f);

        // Only clamp if the paddle is lower than the limit
        if(offAmount > 0.0f)
        {
          offAmount = 0.0f;
        }
      }

      // If the first paddle was clamped, then
      // clamp all paddles by the same amount
      curPaddle.setCenterX(curPaddle.getCenterX() - offAmount);
    }
  }

  /**
   * Move all paddles to the right, locking the first paddle inside the world
   * and all other worlds maintain their fixed distance to the first paddle.
   */
  public void moveRight()
  {
    GameObject curPaddle;

    float offAmount = 0.0f;

    for(int i = 0; i < size(); i++)
    {
      curPaddle = get(i);

      curPaddle.setCenterX(curPaddle.getCenterX() + paddleSpeed);

      // Only the first paddle will get clamped and
      // other paddles will be affected by the clamp by the same amount
      if(i == 0)
      {
        offAmount = curPaddle.getCenterX() - (curPaddle.getWidth() * 0.25f);
        offAmount -= maxX;

        // Only clamp if the paddle is lower than the limit
        if(offAmount < 0.0f)
        {
          offAmount = 0.0f;
        }
      }

      // If the first paddle was clamped, then
      // clamp all paddles by the same amount
      curPaddle.setCenterX(curPaddle.getCenterX() - offAmount);
    }
  }
}
