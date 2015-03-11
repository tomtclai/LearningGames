package dyehard.Background;

import Engine.BaseCode;
import dyehard.DyehardRectangle;

public class LogScreen {
    private final DyehardRectangle screen;

    public LogScreen() {
        screen = new DyehardRectangle();
        screen.size.set(40f, 11f);
        screen.center.set(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        screen.texture = BaseCode.resources
                .loadImage("Textures/UI/UI_Start.png");
        screen.alwaysOnTop = true;
        screen.visible = false;
    }

    public void showScreen(boolean show) {
        screen.visible = show;
    }

    public boolean isShown() {
        return screen.visible;
    }
}
