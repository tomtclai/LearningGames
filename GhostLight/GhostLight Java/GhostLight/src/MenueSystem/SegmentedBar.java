package MenueSystem;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Rectangle;

/**
 * Maintains a segmented horizontal Segment bar
 * @author Michael Letter
 */
public class SegmentedBar extends Rectangle{
	private int maxSegments = 0;
	/** stores the Segment the bar will be representing must be less than SegmentBarDividers.length */
	private int filledSegments = 0;
	/** Stores the image used on a bar Segment if it is active */
	private String activeImage = "";
	/** Stores the image used on a bar Segment if it is not active */
	private String inactiveImage = "";
	/** if True the Bar will be Drawn Vertically. Otherwise it will be drawn horizontal */
	public boolean vertical = false;
	/** If True, when drawn will be drawn with the given textures rather than this rectangles color. If False, the bar will be drawn with the current color*/
	public boolean useImages = false;
	/**
	 * if "useImages" is True will set the texture drawn for the active segments of bar
	 * @param activeImageLocation the location of the target image
	 */
	public void setActiveImage(String activeImageLocation){
		if(activeImageLocation != null){
			activeImage = activeImageLocation;
		}
	}
	/**
	 * if "useImages" is True will set the texture drawn for the inactive segments of bar
	 * @param activeImageLocation the location of the target image
	 */
	public void setInactiveImage(String inactiveImageLocation){
		if(inactiveImageLocation != null){
			inactiveImage = inactiveImageLocation;
		}
	}
	/**
	 * will set the max Segment of the bar
	 * @param newmaxSegments
	 * @return whether or not changes were made
	 */
	public boolean setMaxSegments(int newmaxSegments){
		if(newmaxSegments >= 0){
			maxSegments = newmaxSegments;
			if(filledSegments > maxSegments){
				filledSegments = maxSegments;
			}
			return true;
		}
		return false;
	}
	/**
	 * Does do you think this does
	 * @return
	 */
	public int getMaxSegments(){
		return maxSegments;
	}
	/**
	 * Will set the Segment displayed by the bar
	 * @param newfilledSegments
	 * @return whether or not changes were made
	 */
	public boolean setFilledSegments(int newfilledSegments){
		if(newfilledSegments >= 0){
			filledSegments = newfilledSegments;
			if(filledSegments > maxSegments){
				filledSegments = maxSegments;
			}
			return true;
		}
		return false;
	}
	/**
	 * Does do you think this does
	 * @return
	 */
	public int getfilledSegments(){
		return filledSegments;
	}
	/**
	 * Will add as much of increment to the filledSegments as possible 
	 * @param increment
	 */
	public void incrementSegmentsFilled(int increment){
		filledSegments += increment;
		if(filledSegments < 0){
			filledSegments = 0;
		}
		else if(filledSegments > maxSegments){
			maxSegments = filledSegments;
		}
	}
	/** Will Draw the given Segment bar */
	public void draw(){
		//super.draw();
		if(BaseCode.resources != null && visible && maxSegments > 0){
			//Vertical
			if(vertical){
				//Images
				if(useImages){
					float segmentWidth = super.size.getY() / (float)maxSegments;
					float firstCorner = center.getY() - (size.getY()/2);
				
					//Empty Segments
					int loop = 0;
					super.setImage(inactiveImage);
					while(loop < (maxSegments - filledSegments) && texture != null){
						if(texture != null){
							BaseCode.resources.drawImage(texture, center.getX() - (size.getX()/2), firstCorner + (segmentWidth * loop), center.getX() + (size.getX()/2), firstCorner + (segmentWidth * (loop + 1)), rotate);
						}
						loop++;
					}
					//Filled Segments
					super.setImage(activeImage);
					while(loop < maxSegments){
						if(texture != null){
							BaseCode.resources.drawImage(texture, center.getX() - (size.getX()/2), firstCorner + (segmentWidth * loop), center.getX() + (size.getX()/2), firstCorner + (segmentWidth * (loop + 1)), rotate);
						}
						loop++;
					}
				}
				//Color
				else{
					float segmentHeight = super.size.getY() / (float)maxSegments;
					float firstCenter = super.center.getY() - (super.size.getY()/2f) + (segmentHeight/2);
					
					BaseCode.resources.setDrawingColor(color);
					if(filledSegments > 0){
						BaseCode.resources.drawFilledRectangle(center.getX(), firstCenter + ((segmentHeight * (filledSegments - 1))/2), size.getX()/2, (segmentHeight/2) * filledSegments, 0);
					}
					BaseCode.resources.setDrawingColor(Color.black);
					for(int loop = 0; loop < maxSegments; loop++){
						BaseCode.resources.drawOutlinedRectangle(center.getX(), firstCenter + (segmentHeight * loop), size.getX()/2, (segmentHeight/2), 0);
					}
				}
			}
			//Horizontal
			else{
				//Images
				if(useImages){
					float segmentWidth = super.size.getX() / (float)maxSegments;
					float firstCorner = center.getX() - (size.getX()/2);
					//Empty Segments
					int loop = 0;
					super.setImage(inactiveImage);
					while(loop < (maxSegments - filledSegments)){
						if(texture != null){
							BaseCode.resources.drawImage(texture, firstCorner + (segmentWidth * loop), center.getY() - (size.getY()/2), firstCorner + (segmentWidth * (loop + 1)), center.getY() + (size.getY()/2), rotate);
						}
						loop++;
					}
					//Filled Segments
					super.setImage(activeImage);
					while(loop < maxSegments){
						if(texture != null){
							BaseCode.resources.drawImage(texture, firstCorner + (segmentWidth * loop), center.getY() - (size.getY()/2), firstCorner + (segmentWidth * (loop + 1)), center.getY() + (size.getY()/2), rotate);
						}
						loop++;
					}
				}
				//Color
				else{
					float segmentWidth = super.size.getX() / (float)maxSegments;
					float firstCenter = super.center.getX() - (super.size.getX()/2f) + (segmentWidth/2);
					
					BaseCode.resources.setDrawingColor(color);
					if(filledSegments > 0){
						BaseCode.resources.drawFilledRectangle(firstCenter + ((segmentWidth * (filledSegments - 1))/2), center.getY(), (segmentWidth/2) * filledSegments, size.getY()/2, rotate);
					}
					BaseCode.resources.setDrawingColor(Color.black);
					for(int loop = 0; loop < maxSegments; loop++){
						BaseCode.resources.drawOutlinedRectangle(firstCenter + (segmentWidth * loop), center.getY(), (segmentWidth/2), size.getY()/2, rotate);
					}
				}
			}
		}
	}

	
}
