package dyehard.Background;

import Engine.BaseCode;
import dyehard.DyehardRectangle;

public class StartScreen {
    private final DyehardRectangle screen;

    public StartScreen() {
        screen = new DyehardRectangle();
        screen.size.set(BaseCode.world.getWidth(), BaseCode.world.getHeight());
        screen.center.set(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        screen.texture = BaseCode.resources
                .getImage("Textures/UI/DyeHard_StartScreen.png");
        screen.alwaysOnTop = true;
        screen.visible = true;
    }

    public void showScreen(boolean show) {
        if (show) {
            BaseCode.resources.moveToFrontOfDrawSet(screen);
        } else {
            screen.texture = null;
        }
        screen.visible = show;
    }

    public boolean isShown() {
        return screen.visible;
    }
}
