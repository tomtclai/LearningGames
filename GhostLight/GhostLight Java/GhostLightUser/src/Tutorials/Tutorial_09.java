package Tutorials;

import java.awt.event.KeyEvent;

import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject;

public class Tutorial_09 extends GhostLightInterface {

    @Override
    public void initialize() {
        objectGrid.setObjectGrid(new InteractableObject[3][8]);
    }

    @Override
    public void update() {
        // Speeding up Animation
        gameState.setAnimationTime(10);

        // shifting enemies when Enter is pressed
        if (keyboard.isButtonDown(KeyEvent.VK_ENTER)) {
            shiftMonstersDown();
        }

        // moving monsters when player moves the light left or right starts here
        if (keyboard.isButtonTapped(KeyEvent.VK_A)
                || keyboard.isButtonTapped(KeyEvent.VK_LEFT)) {
            light.setPosition(light.getPosition() - 1); // Decrementing position
            shiftMonstersDown();
        } else if (keyboard.isButtonTapped(KeyEvent.VK_D)
                || keyboard.isButtonTapped(KeyEvent.VK_RIGHT)) {
            light.setPosition(light.getPosition() + 1); // incrementing position
            shiftMonstersDown();
        }
        // moving monsters when player moves the light left or right ends here

        if (keyboard.isButtonTapped(KeyEvent.VK_SPACE)) {
            InteractableObject[] affected = light.getTargetedEnemies(objectGrid);
            // Activating the light returns an array that contains the enemies
            // in the objectGrid that were touched by the light
            if (affected != null) { // This array is not guaranteed to exist
                for (int loop = 0; loop < affected.length; loop++) {
                    if (affected[loop] != null) { // nor is this array
                                                  // guaranteed to be full
                        affected[loop].setCurrentHealth(0);
                    }
                }
            }
            shiftMonstersDown();
        }

    }

    // moving monsters starts here
    protected void shiftMonstersDown() {
        InteractableObject[][] monsterGrid = objectGrid.getObjectGrid();
        InteractableObject monster = new InteractableObject();
        boolean shiftRight = true;
        
        for (int loopRow = 0; loopRow < monsterGrid.length; loopRow++) {
            monster = shiftRow(monsterGrid, monster, loopRow, shiftRight);
            shiftRight = !shiftRight;
        }
        // Destroying monster moved out of array
        if (monster != null) {
            monster.destroy();
        }
    }

    // Shifts all of the monsters left or right by one array element
    protected InteractableObject shiftRow(InteractableObject[][] monsterGrid, InteractableObject monster, int targetRow,
            boolean shiftRight) {
        InteractableObject lastMonster;
        
        // Shift Right
        if (shiftRight) {
            int lastIndex = monsterGrid[targetRow].length - 1;
            lastMonster = monsterGrid[targetRow][lastIndex];

            for (int loop = monsterGrid[targetRow].length - 2; loop >= 0; loop--) {
                monsterGrid[targetRow][loop+1] = monsterGrid[targetRow][loop];
            }
            
            monsterGrid[targetRow][0] = monster;
        }
        // Shift Left
        else {
            lastMonster = monsterGrid[targetRow][0];

            for (int loop = 0; loop < monsterGrid[targetRow].length -1; loop++) {
                monsterGrid[targetRow][loop] = monsterGrid[targetRow][loop+1];
            }
            
            int lastIndex = monsterGrid[targetRow].length - 1;
            monsterGrid[targetRow][lastIndex] = monster;
        }
        return lastMonster;
    }

    // moving monsters ends here

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }
}