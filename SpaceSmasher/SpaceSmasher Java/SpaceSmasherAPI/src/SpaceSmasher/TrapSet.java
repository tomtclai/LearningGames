
package SpaceSmasher;

import Engine.BaseCode;
import Engine.SetOfGameObjects;
import Engine.Vector2;

@SuppressWarnings("serial")
public class TrapSet extends SetOfGameObjects<Trap>
{
  final float WALL_WIDTH = 4.0f;

  public Trap left = null;
  public Trap right = null;

  /**
   * Add the given number of traps to the set.
   * 
   * @param num
   *          - Number of traps to add.
   */
  public void add(int num)
  {
    for(int i = 0; i < num; i++)
    {
      add();
    }
  }

  /**
   * Add a single trap to the set.
   */
  public void add()
  {
    Trap wall = new Trap();
    add(wall);
    autoPosition(wall);
    deactivate();
  }

  /**
   * Automatically position a trap based on its index in the set.
   * 
   * @param wall
   *          - The wall that should be positioned. It should already be a part
   *          of the set.
   */
  protected void autoPosition(Trap wall)
  {
	Vector2 leftCenter =
        new Vector2(BaseCode.world.getWidth() * 0.034f, BaseCode.world.getHeight() * 0.6f);
    Vector2 rightCenter =
        new Vector2(BaseCode.world.getWidth() * 0.966f,
            leftCenter.getY());
    
    int index = indexOf(wall) % 2;

    if(index == 0)
    {
      wall.translatePosStart.set(leftCenter);
      wall.translatePosStart.setY(BaseCode.world.getHeight() * 0.4f);
      wall.translatePosEnd.set(leftCenter);
      wall.translatePosEnd.setY(BaseCode.world.getHeight() * 0.7f);
      wall.translatePos = (int)(Math.random() * wall.translateTime);
    }
    else if(index == 1)
    {
      wall.translatePosStart.set(rightCenter);
      wall.translatePosStart.setY(BaseCode.world.getHeight() * 0.4f);
      wall.translatePosEnd.set(rightCenter);
      wall.translatePosEnd.setY(BaseCode.world.getHeight() * 0.7f);
      wall.translatePos = (int)(Math.random() * wall.translateTime);
    }
  }

  /**
   * Return the width of the traps.
   * 
   * @return - The width of the traps.
   */
  public float getWidth()
  {
    if(size() > 0)
    {
      return get(0).getWidth();
    }

    return 0.0f;
  }

  /**
   * Return the height of the traps.
   * 
   * @return - The height of the traps.
   */
  public float getHeight()
  {
    if(size() > 0)
    {
      return get(0).getHeight();
    }

    return 0.0f;
  }
  public void update(){
	  if(super.size() >= 2){
		  super.get(0).update();
		  super.get(1).update();
	  }
  }
  /**
   * Get the first trap that is located on the left side of the world.
   * 
   * @return - The first trap on the left side of the world.
   */
  public Trap left()
  {
    if(size() > 0)
    {
      return get(0);
    }

    return null;
  }

  /**
   * Get the first trap that is located on the right side of the world.
   * 
   * @return - The first trap on the right side of the world.
   */
  public Trap right()
  {
    if(size() > 1)
    {
      return get(1);
    }

    return null;
  }

  /**
   * Make all of the walls look activated.
   */
  public void activate()
  {
    for(int i = 0; i < size(); i++)
    {
      get(i).activate();
    }
  }

  /**
   * Make all of the walls look deactivated.
   */
  public void deactivate()
  {
    for(int i = 0; i < size(); i++)
    {
      get(i).deactivate();
    }
  }
  /**
   * Overrides setofitem's clear to also clear the indicators.
   */
  public void clear()
  {
	  for(int i = 0; i < this.size(); i++)
	  {
		  get(i).clearIndicator();
	  }
	  super.clear();
  }
}
