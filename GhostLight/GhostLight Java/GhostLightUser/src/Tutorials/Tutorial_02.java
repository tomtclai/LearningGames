package Tutorials;

import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.MouseState.MouseButton;

public class Tutorial_02 extends GhostLightInterface {

    @Override
    public void initialize() {
        gameState.setHealth(5);
        gameState.setScore(10);
        gameState.setLightPower(0.45f);

        gameState.givePrimitiveGridPriority();

        // creating a monster array starts here
        primitiveGrid.setIDRowCount(1);

        int[] idArray = new int[5]; // space for five ghosts
        primitiveGrid.setIDArray(idArray, 0);
        
        // Adding an enemy
        idArray[3] = 1;
        // creating a monster array ends here
    }

    @Override
    public void update() {
        // Swapping two array elements starts here
        if (keyboard.isButtonTapped(KeyEvent.VK_ENTER)
                || mouse.isButtonTapped(MouseButton.LEFT)) {

            int[] idArray = primitiveGrid.getIDArray(0);

            int temp = idArray[0];
            idArray[0] = idArray[3];
            idArray[3] = temp;
        }
        // Swapping two array elements ends here

        // Moving the flashlight starts here
        if (keyboard.isButtonTapped(KeyEvent.VK_A)
                || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
            light.setPosition(light.getPosition() - 1); // Decrementing position
        } else if (keyboard.isButtonTapped(KeyEvent.VK_D)
                || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
            light.setPosition(light.getPosition() + 1); // incrementing position
        }
        // Moving the flashlight ends here
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}