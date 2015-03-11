
package GhostLight.GhostEngine;

import java.awt.Color;
import Engine.Rectangle;
import Engine.Vector2;

public class PowerBar extends Rectangle
{
  private Rectangle backgroundBar = null;
  private Rectangle[] segments = null;
  private float percent = 1.0f;
  private float barOffset = 1.8f; // The offset between the segments.
  private float staticXSize = 5f;
  private float staticYSize = 2.5f;
  private float animXSize = 6.5f;
  private float animYSize = 4f;
  private float xCenterOffset = 0f;
  private float yCenterOffset = -9f;
  private int animCounter = 0;
  private int ANIMATION_TIME = 30;
  

  private float maxScore = 10.0f;
  
  private static final int TOTAL_SEGMENTS = 12;
  
  private static final String EMPTYBAR = "powerbar/HealthBar_Empty_Static.png";
  private static final String STATIC_BAR_1 = "powerbar/Health12Static.png";
  private static final String STATIC_BAR_2 = "powerbar/Health11Static.png";
  private static final String STATIC_BAR_3 = "powerbar/Health10Static.png";
  private static final String STATIC_BAR_4 = "powerbar/Health9Static.png";
  private static final String STATIC_BAR_5 = "powerbar/Health678Static.png";
  private static final String STATIC_BAR_6 = STATIC_BAR_5;
  private static final String STATIC_BAR_7 = STATIC_BAR_6;
  private static final String STATIC_BAR_8 = "powerbar/Health5Static.png";
  private static final String STATIC_BAR_9 = "powerbar/Health4Static.png";
  private static final String STATIC_BAR_10 = "powerbar/Health3Static.png";
  private static final String STATIC_BAR_11 = "powerbar/Health2Static.png";
  private static final String STATIC_BAR_12 = "powerbar/Health1Static.png";
  
  

  public PowerBar() {

    backgroundBar = new Rectangle();
    backgroundBar.setImage(EMPTYBAR);
    backgroundBar.size = this.size;
    backgroundBar.center = this.center;
    
    segments = new Rectangle[TOTAL_SEGMENTS];
    
    for(int i = 0; i < TOTAL_SEGMENTS; i++)
    {
    	segments[i] = new Rectangle();	
    	segments[i].size.set(staticXSize, staticYSize);
    	segments[i].visible = false;
    	segments[i].setAnimationMode(0, 14,
    			Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP);
    }
    
    segments[0].setImage(STATIC_BAR_1);
    segments[1].setImage(STATIC_BAR_2);
    segments[2].setImage(STATIC_BAR_3);
    segments[3].setImage(STATIC_BAR_4);
    segments[4].setImage(STATIC_BAR_5);
    segments[5].setImage(STATIC_BAR_6);
    segments[6].setImage(STATIC_BAR_7);
    segments[7].setImage(STATIC_BAR_8);
    segments[8].setImage(STATIC_BAR_9);
    segments[9].setImage(STATIC_BAR_10);
    segments[10].setImage(STATIC_BAR_11);
    segments[11].setImage(STATIC_BAR_12);
    
    
    setPercent(1.0f);
  }
  
  public Rectangle getSegmentRectangle(int i)
  {
	  return null;
  }

  public void destroy() {
	  super.destroy();

	  for(int i = 0; i < TOTAL_SEGMENTS; i++)
	  {
		  segments[i].destroy();
		  segments[i] = null;
	  }
  }
  public void setVisibility(boolean visible){
	  backgroundBar.visible = visible;
	  for(int i = 0; i < TOTAL_SEGMENTS; i++)
	  {
		  segments[i].visible = visible;
	  }
	  super.visible = visible;
  }
  public void updateBar() {
	  
	  for(int i = 0; i < TOTAL_SEGMENTS; i++)
	  {
		  if(segments[i].usingSpriteSheet() == true)
		  {
			  segments[i].size.set(animXSize, animYSize);
			  segments[i].center.set(this.center.getX() + xCenterOffset,
					  (this.center.getY() + yCenterOffset) + barOffset * i );	
		  }
		  else
		  {
			  segments[i].size.set(staticXSize, staticYSize);
			  segments[i].center.set(this.center.getX() - this.size.getX() / 10,
				  (this.center.getY() + (this.size.getY() / 6.5f) - (this.size.getY() / 2)) + barOffset * i );
		  }
	  }

	  setPercent(percent);
  }

  public void setMaxScore(float value) {
    maxScore = value;
  }

  public void setToVertical() {
    updateBar();
  }

  public void setToHorizontal() {
    updateBar();
  }

  public float getScore() {
    return (getPercent() * maxScore);
  }

  public float getPercent() {
    return percent;
  }

  public void increaseScore(float value) {
    increasePercent(value / maxScore);
  }

  public void setScore(float value) {
    setPercent(value / maxScore);
  }

  public void increasePercent(float value) {
    setPercent(percent + value);
  }

  public void setPercent(float value) {
    percent = value;
    
    // 0 - 13 things
    int segmentsActive = (int)(percent * (TOTAL_SEGMENTS + 1));
    if(animCounter <= 0)
    {
	    if(segmentsActive > 0)
	    {
	    	for(int i = 0; i < segmentsActive - 1; i++)
	    	{
	    		segments[i].visible = true;
	    	}
	    	for(int i = segmentsActive - 1; i < TOTAL_SEGMENTS; i++)
	    	{
	    		if(segments[i].visible)
	    		{
		    		//segments[i].visible = false; // TODO: DEACTIVE HERE
		    		segments[i].setSpriteSheet(getSegmentImage(i, true), 58, 28, 15, 3);
		    		segments[i].setUsingSpriteSheet(true);
					  segments[i].size.set(animXSize, animYSize);
					  segments[i].center.set(this.center.getX() + xCenterOffset,
							  (this.center.getY() + yCenterOffset) + barOffset * i );	
		    		animCounter = ANIMATION_TIME;
	    		}
	    	}
	    }
	    else
	    {
	    	for(int i = 0; i < TOTAL_SEGMENTS; i++)
	    	{
	    		if(segments[i].visible)
	    		{
		    		//segments[i].visible = false; // TODO: DEACTIVE HERE
		    		segments[i].setSpriteSheet(getSegmentImage(i, true), 58, 28, 15, 3);
		    		segments[i].setUsingSpriteSheet(true);
					  segments[i].size.set(animXSize, animYSize);
					  segments[i].center.set(this.center.getX() + xCenterOffset,
							  (this.center.getY() + yCenterOffset) + barOffset * i );	
		    		animCounter = ANIMATION_TIME;
	    		}
	    	}
	    }
    }
    else
    {
    	animCounter--;
    	if(animCounter <= 0)
    	{
	    	for(int i = 0; i < TOTAL_SEGMENTS; i++)
	    	{
	    		if(segments[i].usingSpriteSheet())
	    		{
		    		segments[i].setImage(getSegmentImage(i, false));
		    		segments[i].setUsingSpriteSheet(false);
		    		segments[i].visible = false;
					  segments[i].size.set(staticXSize, staticYSize);
					  segments[i].center.set(this.center.getX() - this.size.getX() / 10,
						  (this.center.getY() + (this.size.getY() / 6.5f) - (this.size.getY() / 2)) + barOffset * i );
	    		}
	    	}
    	}
    }
    

  }
  public void draw(){
	  backgroundBar.draw();
  }
  
  private String getSegmentImage(int num, boolean anim)
  {
	  int seg = TOTAL_SEGMENTS - 1 - num;
	  
	  if(seg >= 5 && seg <= 7) // Subtract one to bounderies
	  {
		  if(anim)
		  {
			  return "powerbar/Health678.png";
		  }
		  return "powerbar/Health678Static.png";
	  }
	  if(anim)
	  {
		  return "powerbar/Health" + (seg + 1) + ".png";
	  }
	  return "powerbar/Health" + (seg + 1) + "Static.png";
  }
}
