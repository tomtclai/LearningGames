package Tutorials;

import java.awt.event.KeyEvent;
import GhostLight.Interface.GhostLightInterface;

public class Tutorial_05 extends GhostLightInterface {

    @Override
    public void initialize() {
        gameState.setHealth(5);
        gameState.setScore(10);
        gameState.setLightPower(0.45f);

        gameState.givePrimitiveGridPriority();

        int[] idArray = new int[5]; // space for five ghosts

        primitiveGrid.setIDRowCount(1);

        // Adding several enemies
        idArray[1] = 1;
        idArray[2] = 2;
        idArray[3] = 3;

        primitiveGrid.setIDArray(idArray, 0);

        primitiveGrid.resizeArrays();
        float[] healthArray = primitiveGrid.getHealthArray(0);
        healthArray[1] = 0.75f;
    }

    @Override
    public void update() {
        // isButtonDown == is button being pressed at this very moment
        // isButtonTapped == has the button been pressed and released lately

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

        // using the numberOfMonsters method starts here
        float[] healthArray = primitiveGrid.getHealthArray(0);
        int numMonsters = 0;
        numMonsters = this.numberOfMonsters(healthArray);

        System.out.print("There are "
                + numMonsters
                + " monsters on screen");
        // using the numberOfMonsters method ends here

        // using the averageMonsterHealth method starts here
        float avgHealth = 0f;
        avgHealth = averageMonsterHealth(healthArray);
        System.out.println("Average health: "
                + avgHealth);
        // using the averageMonsterHealth method ends here

        if (keyboard.isButtonTapped(KeyEvent.VK_A)
                || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
            light.setPosition(light.getPosition() - 1); // Decrementing position
        } else if (keyboard.isButtonTapped(KeyEvent.VK_D)
                || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
            light.setPosition(light.getPosition() + 1); // incrementing position
        }
    }

    // method to sum array elements starts here
    public int numberOfMonsters(float[] healthArray) {
        int numMonsters = 0;
        for (int i = 0; i < healthArray.length; i++) {
            if (healthArray[i] > 0) {
                numMonsters++;
            }
        }
        return numMonsters;
    }
    // method to sum array elements ends here

    // method to average array elements starts here
    public float averageMonsterHealth(float[] healthArray) {
        float avgHealth;

        int numMonsters = numberOfMonsters(healthArray);
        if (numMonsters <= 0)
            avgHealth = 0f;
        else
        {
            float totalHealth = 0.0f;
            for (int i = 0; i < healthArray.length; i++) {
                if (healthArray[i] > 0) {
                    totalHealth += healthArray[i];
                }
            }
            avgHealth = totalHealth / numMonsters;
        }

        return avgHealth;
    }
    // method to average array elements ends here

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}