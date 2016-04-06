package structures;
import Engine.Vector2;

//right now we create intvectors in order to determine grid indexes. if the grid index is null, we just instantiated this class for no reason. should we use these at all???
public class IntVector {
	private int x = 0;
	private int y = 0;
	
	/**
	 * constructs an IntVector (like a Vector2 but with ints. easier for array management)
	 * @author Brian Chau
	 * @param newX x position
	 * @param newY y position
	 */
	public IntVector(int newX, int newY)
	{
		x = newX;
		y = newY;
	}
	
	/**
	 * constructs an IntVector using a Vector2 parameter
	 * @author Brian Chau
	 * @param pos Vector2 position
	 */
	public IntVector(Vector2 pos)
	{
		// cast to int (float values are ROUNDED DOWN ALWAYS)
		x = (int)pos.getX();
		y = (int)pos.getY();
	}
	
	/**
	 * converts this IntVector to a Vector2
	 * @author Brian Chau
	 * @return Vector2 representation of this IntVector
	 */
	public Vector2 toVector2()
	{
		return new Vector2(x,y);
	}
	
	/**
	 * gets the x component
	 * @author Brian Chau
	 * @return x component
	 */
	public int getX()
	{ return x;}
	
	/**
	 * gets the y component
	 * @author Brian Chau
	 * @return y component
	 */
	public int getY()
	{ return y;}
	
	public void setX(int _x){
		x = _x;
	}
	
	public void setY(int _y){
		y = _y;
	}
	
	public void set(IntVector iv){
		x = iv.getX();
		y = iv.getY();
	}
	public void set(Vector2 v2){
		x = (int)v2.getX();
		y = (int)v2.getY();
	}
	
	public void set(int _x,int _y){
		x = _x;
		y = _y;
	}

	/**
	 * if this IntVector is outside the bounds from any direction, it is moved to the edge of the bounds (inclusive)
	 *
	 * @author Brian Chau
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public void forceIntoBounds(int minx, int miny, int maxx, int maxy)
	{
		if(x < minx) x = minx;
		if(x >= maxx) x = maxx;
		if(y < miny) y = miny;
		if(y >= maxy) y = maxy;
	}
	
	public IntVector add(IntVector that){
		
		return new IntVector(this.x + that.x, this.y + that.y);
	}
	
	public IntVector subtract(IntVector that){
		
		return new IntVector(this.x - that.x, this.y - that.y);
	}
		

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null) return false;
		if(obj.getClass().equals(IntVector.class) || obj.getClass().equals(IntVectorPathFinder.class))
		{
			IntVector that = (IntVector) obj;
			return this.x == that.x && this.y == that.y;
		}
		return false;
	}
	
	public String toString(){
		
		String out="("+x+","+y+")";
		
		return out;
		
	}
	
}
