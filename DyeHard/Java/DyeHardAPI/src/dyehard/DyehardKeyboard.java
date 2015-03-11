package dyehard;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import Engine.KeyboardInput;

public class DyehardKeyboard extends KeyboardInput {

    static DyehardKeyboard instance = null;

    public static boolean isKeyTapped(int key) {
        return instance != null && instance.isButtonTapped(key);
    }

    public static boolean isKeyDown(int key) {
        return instance != null && instance.isButtonDown(key);
    }

    Set<KeyEvent> keyPresses = new HashSet<KeyEvent>();
    Set<KeyEvent> keyReleases = new HashSet<KeyEvent>();
    protected String lastKeyPress;

    public DyehardKeyboard() {
        assert (instance == null);

        instance = this;
    }

    @Override
    public String getLastKey() {
        return lastKeyPress;
    }

    @Override
    public void keyPressed(KeyEvent key) {
        keyPresses.add(key);
        lastKeyPress = KeyEvent.getKeyText(key.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent key) {
        keyReleases.add(key);
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // do nothing
    }

    @Override
    public void update() {
        super.update();

        for (KeyEvent key : keyPresses) {
            pressButton(key.getKeyCode());
        }
        keyPresses.clear();

        for (KeyEvent key : keyReleases) {
            releaseButton(key.getKeyCode());
        }
        keyReleases.clear();
    }

}
