package GhostLight.GhostEngine;

import Engine.MouseInput;
import GhostLight.Interface.MouseState.MouseButton;
/**
 * @author Michael Letter
 */
public class MouseInfo {
	MouseInput mouse = null;
	float screenWidth = 0;
	float screenHeight = 0;

	
	/**
	 * Will return whether or not the target Mouse Button has been tapped
	 * @param targetButton the mouse button in question
	 * @return returns true if the mouse button in question has been tapped, otherwise returns false.
	 */
	public boolean isButtonTapped(MouseButton targetButton){
		if(mouse != null){
			if(targetButton == MouseButton.LEFT){
				return mouse.isButtonTapped(1);
			}
			else if(targetButton == MouseButton.RIGHT){
				return mouse.isButtonTapped(3);
			}
			else if(targetButton == MouseButton.MIDDLE){
				return mouse.isButtonTapped(2);
			}
		}
		return false;
	}
	/**
	 * Will return whether or not the target Mouse Button is currently held down.
	 * @param targetButton the mouse button in question
	 * @return returns true if the mouse button in question is currently held down, otherwise returns false.
	 */
	public boolean isButtonDown(MouseButton targetButton){
		if(mouse != null){
			if(targetButton == MouseButton.LEFT){
				return mouse.isButtonDown(1);
			}
			else if(targetButton == MouseButton.RIGHT){
				return mouse.isButtonDown(3);
			}
			else if(targetButton == MouseButton.MIDDLE){
				return mouse.isButtonDown(2);
			}
		}
		return false;
	}
	/**
	 * Will Return the mouses X position in the window
	 * @return a float between 0 and 1 with 0 representing a position at the left edge of the screen 
	 * and 1 representing a position at the right edge of the screen
	 */
	public float getMouseX(){
		if(mouse != null && screenWidth > 0){
			//System.out.println(mouse.getPixelX()/screenWidth);
			return (mouse.getPixelX()/screenWidth) * 1.1f; // random constant is for side bar
		}
		return 0;
	}
	/**
	 * Will Return the mouses X position in the window
	 * @return a float between 0 and 1 with 0 representing a position at the top edge of the screen 
	 * and 1 representing a position at the bottom edge of the screen
	 */
	public float getMouseY(){
		if(mouse != null && screenHeight > 0){
			return mouse.getPixelX()/screenHeight;
		}
		return 0;
	}
}
