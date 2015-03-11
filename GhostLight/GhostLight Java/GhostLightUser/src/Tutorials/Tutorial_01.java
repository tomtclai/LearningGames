package Tutorials;

import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.MouseState.MouseButton;
import GhostLight.Interface.OnScreenButtons.ScreenButton;

public class Tutorial_01 extends GhostLightInterface {

    // initialize method starts here
    @Override
    public void initialize() {
        // Score
        // Displayed as a number in the upper left corner of the screen
        // Represented by an int greater than zero
        gameState.setScore(9001); // Set Score
        System.out.println(gameState.getScore()); // Get Score

        // Health
        // Displayed as the number of hearts in the upper right corner of the
        // screen
        // Represented by an int greater than zero
        // Note, at this point if the int gets too larger (around 20) the number
        // of hearts will extend off screan
        gameState.setHealth(5); // Set Health
        System.out.println(gameState.getHealth()); // Get Health

        // Power
        // Displayed as a green bar in the lower left corner of the screen
        // Represented as a float between 0 and 1. 1 results in the bar being
        // filled completely and zero results in the bar bing completely empty
        gameState.setLightPower(0.45f); // Set Power
        System.out.println(gameState.getLightPower()); // Get Power
    }

    @Override
    // update method starts here
    public void update() {
        // keyboard input starts here
        
        // isButtonTapped == button is being pressed now 
        //                          BUT not during prior update
        // isButtonDown == button is being pressed now
        //                          regardless of prior updates
        
        if (keyboard.isButtonTapped(KeyEvent.VK_ENTER)) {
            System.out.print('\n');
        }
        if (keyboard.isButtonDown(KeyEvent.VK_A) || 
            keyboard.isButtonDown(KeyEvent.VK_Q)) {
            System.out.print('a');
        }
        if(keyboard.isButtonTapped(KeyEvent.VK_UP)) {
            int currentHealth = gameState.getHealth();
            currentHealth++;
            gameState.setHealth(currentHealth);                        
        }
        if(keyboard.isButtonTapped(KeyEvent.VK_DOWN)) {           
            gameState.setHealth(gameState.getHealth() - 1);        
        }
        // keyboard input ends here
        
        //Mouse input starts here
        if (mouse.isButtonTapped(MouseButton.LEFT)) {
            System.out.println("Left");
        }
        if (mouse.isButtonDown(MouseButton.MIDDLE)) {
            System.out.print("Middle");
        }
        if (mouse.isButtonTapped(MouseButton.RIGHT)) {
            System.out.println("Right");
        }
        if( mouse.isButtonDown(MouseButton.LEFT)) {
            gameState.setScore(gameState.getScore()+1);
        }
        if( mouse.isButtonDown(MouseButton.RIGHT)) {
            gameState.setScore(gameState.getScore()-1);
        }        
        //Mouse input ends here

        // On screen button input starts here
        if (clickableButtons.isButtonTapped(ScreenButton.LASERBUTTON)) {
            System.out.println("LAZER!!");
        }
        if (clickableButtons.isButtonDown(ScreenButton.MEDIUMBUTTON)) {
            System.out.println("Medium Button");
        }
        if (clickableButtons.isButtonTapped(ScreenButton.WIDEBUTTON)) {
            System.out.println("Wide Button");
        }
        if (clickableButtons.isButtonDown(ScreenButton.WIDEBUTTON)) {
            gameState.setLightPower( gameState.getLightPower() + 0.01f);
        }
        if (clickableButtons.isButtonDown(ScreenButton.LASERBUTTON)) {
            gameState.setLightPower( gameState.getLightPower() - 0.01f);
        }
        // On screen button input ends here
    }
    // update method ends shere

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}