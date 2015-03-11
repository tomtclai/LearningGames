package Tutorials;

import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;

public class Tutorial_03 extends GhostLightInterface {

    @Override
    public void initialize() {
        gameState.setHealth(5);
        gameState.setScore(10);
        gameState.setLightPower(0.45f);

        gameState.givePrimitiveGridPriority();

        // create several monsters starts here
        int[] idArray = new int[5]; // space for five ghosts

        primitiveGrid.setIDRowCount(1);
        primitiveGrid.setIDArray(idArray, 0);

        // Add several enemies manually:
        idArray[1] = 1;
        idArray[2] = 2;
        idArray[3] = 3;
        
        // Fill the array with monsters:
        for(int i =0 ; i < idArray.length; i++) {
            idArray[i] = i + 1;
        }
        // create several monsters ends here
    }

    @Override
    public void update() {
        // detecting where the light is shining starts here
        if (keyboard.isButtonTapped(KeyEvent.VK_SPACE)) {
            int locations[] = light.getTargetedEnemyColumns(primitiveGrid);

            if (locations != null ) {
                float[] healthArray = primitiveGrid.getHealthArray(0);

                for (int i = 0; i < locations.length; i++) {
                    int loc = locations[i];
                    healthArray[loc] = 0;
                }
            }
        }
        // detecting where the light is shining ends here

        if (keyboard.isButtonTapped(KeyEvent.VK_A)
                || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
            light.setPosition(light.getPosition() - 1); // Decrementing position
        } else if (keyboard.isButtonTapped(KeyEvent.VK_D)
                || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
            light.setPosition(light.getPosition() + 1); // incrementing position
        }
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}