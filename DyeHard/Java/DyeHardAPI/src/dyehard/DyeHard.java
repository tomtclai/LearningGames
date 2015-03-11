package dyehard;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import Engine.LibraryCode;
import dyehard.Background.Background;

public abstract class DyeHard extends LibraryCode {
    public enum State {
        BEGIN, PAUSED, PLAYING, GAMEOVER, QUIT, MENU, RESTART
    }

    // Game state
    protected static State state;

    protected abstract void initialize();

    @Override
    public void initializeWorld() {
        super.initializeWorld();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("resources/DyeHard_Cursor.png");
        Dimension cursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(
                image.getHeight(window), image.getHeight(window));
        Point point = new Point((int) (cursorSize.getWidth() / 2.0),
                (int) (cursorSize.getHeight() / 2.0)); // hot point to middle
        Cursor cursor = toolkit.createCustomCursor(image, point, "cursor");
        window.setCursor(cursor);
        window.addMouseListener(mouse);

        resources.setClassInJar(this);

        state = State.PLAYING;

        // preload the gate path images
        dyehard.World.WormHole.setGatePathImages();

        new Background();

        initialize(); // call user code Initialize()
    }

    @Override
    public void updateWorld() {
        update(); // call user code update()
    }

    public static State getState() {
        return state;
    }

    protected abstract void update();

}
