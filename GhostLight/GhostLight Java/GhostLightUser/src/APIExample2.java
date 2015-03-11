import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.MouseState.MouseButton;
import GhostLight.Interface.OnScreenButtons.ScreenButton;

/**
 * Keyboard and mouse input
 * @author Michael Letter
 */
public class APIExample2 extends GhostLightInterface {

	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		//isButtonDown == is button being pressed at this very moment
		//isButtonTapped == has the button been presed and released lately
		
		//Keybord input
		//to ask about specific keys use KeyEvent enums
		if(keyboard.isButtonDown(KeyEvent.VK_A)){
			System.out.print('a');
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_ENTER)){
			System.out.print('\n');
		}
		//Mouse Input
		if(mouse.isButtonTapped(MouseButton.LEFT)){
			System.out.println("Left");
		}
		if(mouse.isButtonDown(MouseButton.MIDDLE)){
			System.out.print("Middle,");
		}
		if(mouse.isButtonTapped(MouseButton.RIGHT)){
			System.out.println("Right");
		}
		//Button Input
		if(clickableButtons.isButtonTapped(ScreenButton.LASERBUTTON)){
			System.out.println("LAZER!!");
		}
		if(clickableButtons.isButtonDown(ScreenButton.MEDIUMBUTTON)){
			System.out.println("Medium Button");
		}
		if(clickableButtons.isButtonTapped(ScreenButton.WIDEBUTTON)){
			System.out.println("Wide Button");
		}
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
