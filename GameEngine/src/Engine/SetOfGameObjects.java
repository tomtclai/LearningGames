
package Engine;

import java.util.Vector;

@SuppressWarnings("serial")
public class SetOfGameObjects<T extends GameObject> extends Vector<T>
{
  /**
   * Update all objects contained in the set that are marked as visible.
   */
  public void update()
  {
    GameObject current;

    for(int i = 0; i < size(); i++)
    {
      current = get(i);

      if(current != null && current.isVisible())
      {
        current.update();
      }
    }
  }

  /**
   * Removes the object at the given index.
   * 
   * @param index
   *          - Index of the object to remove.
   */
  public T remove(int index)
  {
    if(index >= 0 && index < size())
    {
      T theItem = get(index);
      theItem.destroy();
      super.remove(index);
    }

    return null;
  }

  /**
   * Remove the given object in the set.
   * 
   * @param obj
   *          - The object to remove.
   */
  public void remove(GameObject obj)
  {
    remove(indexOf(obj));
  }

  /**
   * Removes all objects in the set.
   */
  public void clear()
  {
    // Remove all the objects
    for(int i = size() - 1; i >= 0; i--)
    {
      // Keep removing the first until no more are left
      remove(0);
    }
  }
}
