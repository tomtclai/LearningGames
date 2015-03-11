package Tutorials;

import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject;

public class Tutorial_06 extends GhostLightInterface {

    // TODO: Do we really want this here?
    public void printArray(float[] nums) {
        for (int i = 0; i < nums.length; i++) {
            System.out.print(i
                    + ": "
                    + nums[i]
                    + " ");
        }
        System.out.println();
    }
    
    @Override
    public void initialize() {
        // Temp fix to adjust starting health values
        InteractableObject.setDefualtHealth(10);

        gameState.setHealth(5);
        gameState.setScore(10);
        gameState.setLightPower(0.45f);

        gameState.givePrimitiveGridPriority();

        // init ID array
        int[][] idArray = new int[1][];
        idArray[0] = new int[5];

        // Adding an enemy
        idArray[0][0] = 1;
        idArray[0][2] = 2;
        idArray[0][4] = 3;

        primitiveGrid.setIDArray(idArray);

        primitiveGrid.resizeArrays();

        float[][] healthArrays = new float[1][];
        float[] healthArray;
        healthArray = healthArrays[0] = new float[5];

        healthArray[0] = healthArray[2] = healthArray[4] = 0.5f; // lazy - do
                                                                 // NOT show
                                                                 // students
                                                                 // this! :)

        System.out.println();
        // printArray(healthArray);

        primitiveGrid.setHealthArray(healthArrays);
    }

    @Override
    public void update() {
        float[] healthArray = primitiveGrid.getHealthArray(0);
        int[] idArray = primitiveGrid.getIDArray(0);

        if (keyboard.isButtonTapped(KeyEvent.VK_SPACE)) {
            int locations[] = light.getTargetedEnemyColumns(primitiveGrid);

            // Activating the light returns an array that contains the enemies
            // in the objectGrid that were touched by the light
            if (locations.length > 0) { // This array exists, but may be empty
                for (int i = 0; i < locations.length; i++) {
                    int loc = locations[i];
                    healthArray[loc] = 0;
                }
            }
        }

        // Ghost bOOst starts here
        // increase all monsters' health by a bit
        if (keyboard.isButtonTapped(KeyEvent.VK_UP)) {
            int[] locs = findMonsters(idArray);

            // remember that loc.length may be 0
            for (int i = 0; i < locs.length; i++) {
                int nextMonsterLoc = locs[i];
                float health = healthArray[nextMonsterLoc];
                healthArray[nextMonsterLoc] = Math.min(health + 0.1f, 1.0f);
                System.out.print("  "
                        + nextMonsterLoc
                        + ": "
                        + healthArray[nextMonsterLoc]);
            }
            System.out.println();
        }
        // Ghost bOOst ends here

        // Ghost bUst starts here
        // decrease all monsters' health by a bit
        if (keyboard.isButtonTapped(KeyEvent.VK_DOWN)) {
            int[] locs = findMonsters(idArray);

            // remember that loc.length may be 0
            for (int i = 0; i < locs.length; i++) {
                int nextMonsterLoc = locs[i];
                float health = healthArray[nextMonsterLoc];
                healthArray[nextMonsterLoc] = Math.max(health - 0.1f, 0.0f);
                System.out.print("  "
                        + nextMonsterLoc
                        + ": "
                        + healthArray[nextMonsterLoc]);
            }
            System.out.println();
        }
        // Ghost bUst ends here

        // int numMonsters = 0;
        // numMonsters = this.numberOfMonsters(healthArray);
        //
        // System.out.print("There are " + numMonsters +
        // " monsters on screen?");
        //
        // float avgHealth = 0f;
        // avgHealth = averageMonsterHealth(healthArray);
        // System.out.println("Average health: " + avgHealth);

        if (keyboard.isButtonTapped(KeyEvent.VK_A)
                || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
            light.setPosition(light.getPosition() - 1); // Decrementing position
        } else if (keyboard.isButtonTapped(KeyEvent.VK_D)
                || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
            light.setPosition(light.getPosition() + 1); // incrementing position
        }
    }

    // method to find occupied array elements starts here
    public int[] findMonsters(int[] idArray) {
        int num = 0;
        for (int i = 0; i < idArray.length; i++) {
            if (idArray[i] >= 0)
                num++;
        }
        int[] monsterLocations = new int[num];
        if (num == 0) // unusual but legal to have zero length array
            return monsterLocations;

        int next = 0;
        for (int i = 0; i < idArray.length; i++)
            if (idArray[i] >= 0) {
                monsterLocations[next] = i;
                next++;
            }

        return monsterLocations; 
    }
    // method to find occupied array elements ends here

    public int numberOfMonsters(float[] healthArray) {

        int numMonsters = 0;
        for (int i = 0; i < healthArray.length; i++) {
            if (healthArray[i] > 0) {
                numMonsters++;
            }
        }
        return numMonsters;
    }

    public float averageMonsterHealth(float[] healthArray) {

        int numMonsters = numberOfMonsters(healthArray);

        float totalHealth = 0.0f;
        for (int i = 0; i < healthArray.length; i++) {
            if (healthArray[i] > 0) {
                totalHealth += healthArray[i];
            }
        }

        float avgHealth;
        if (numMonsters > 0)
            avgHealth = totalHealth
                    / numMonsters;
        else
            avgHealth = 0f;

        return avgHealth;
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}