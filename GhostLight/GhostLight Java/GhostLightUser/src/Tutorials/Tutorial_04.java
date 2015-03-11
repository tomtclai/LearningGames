package Tutorials;

import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.InteractableObject.ObjectType;

public class Tutorial_04 extends GhostLightInterface {

    @Override
    public void initialize() {
        gameState.setHealth(5);
        gameState.setScore(10);
        gameState.setLightPower(0.45f);

        gameState.givePrimitiveGridPriority();

        // Creating monsters with different amounts of health starts here
        int[] idArray = new int[5]; // space for five ghosts

        primitiveGrid.setIDRowCount(1);
        primitiveGrid.setIDArray(idArray, 0);

        // Adding several enemies
        idArray[1] = 1;
        idArray[2] = 2;
        idArray[3] = 3;

//      InteractableObject.setDefaultHealth(10);
        primitiveGrid.resizeArrays();
        
        float[] healthArray = primitiveGrid.getHealthArray(0);
        healthArray[1] = 0.75f;
        healthArray[3] = 0.5f;
        // Creating monsters with different amounts of health starts here
    }

    @Override
    public void update() {
        if (keyboard.isButtonTapped(KeyEvent.VK_SPACE)) {
            int locations[] = light.getTargetedEnemyColumns(primitiveGrid);
            if (locations != null) { 
                float[] healthArray = primitiveGrid.getHealthArray(0);

                for (int i = 0; i < locations.length; i++) {
                    int loc = locations[i];
                    healthArray[loc] = 0;
                }
            }
        }

        if (keyboard.isButtonTapped(KeyEvent.VK_A)
                || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
            light.setPosition(light.getPosition() - 1); // Decrementing position
        } else if (keyboard.isButtonTapped(KeyEvent.VK_D)
                || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
            light.setPosition(light.getPosition() + 1); // incrementing position
        }

        // traversing the array starts here
        float[] healthArray = primitiveGrid.getHealthArray(0);
        
        int numMonsters = 0;
        float totalHealth = 0f;
        for (int i = 0; i < healthArray.length; i++) {
            if (healthArray[i] > 0) {
                numMonsters++;
                totalHealth += healthArray[i];
            }
        }

        System.out.print( numMonsters + " monsters, health scores:");

        for (int i = 0; i < healthArray.length; i++) {
            if (healthArray[i] > 0) {
                System.out.print( " @" + i + ": " + healthArray[i]);
            }
        }
        
        
        float avgHealth = 0f;
        if (numMonsters > 0)
            avgHealth = totalHealth
                    / numMonsters;
        else
            avgHealth = 0f;
        System.out.println("Average health: "
                + avgHealth);
        // traversing the array ends here
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}