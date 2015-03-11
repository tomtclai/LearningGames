package dyehard;

import Engine.BaseCode;
import Engine.Vector2;

public class DyehardEndMenu {
    private final MenuSelect menuSelect;
    private final DyehardRectangle menuHud;

    // positions for selection texture
    private final Vector2 restartSelect = new Vector2(40.85f, 29.4f);
    private final Vector2 quitSelect = new Vector2(40.85f, 20.755f);

    public DyehardEndMenu() {
        menuSelect = new MenuSelect(quitSelect);

        menuHud = new DyehardRectangle();
        menuHud.size = new Vector2(34f, 26.9f);
        menuHud.center = new Vector2(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        menuHud.texture = BaseCode.resources
                .loadImage("Textures/UI/Win_Menu.png");
        menuHud.alwaysOnTop = true;
        menuHud.visible = false;
    }

    public void active(boolean active) {
        if (active) {
            BaseCode.resources.moveToFrontOfDrawSet(menuHud);
        }
        menuHud.visible = active;
        menuSelect.active(active);
    }

    public void select(float x, float y, boolean click) {
        // x value line up of buttons in game world
        if ((x > 38f) && (x < 61.875f)) {
            // hit button 1, restart
            if ((y > 29.75f) && (y < 34.125f)) {
                menuSelect.move(restartSelect);
                if (click) {
                    DyeHard.state = DyeHard.State.RESTART;
                }
            }
            // hit button 2, quit
            else if ((y > 21.125f) && (y < 25.5f)) {
                menuSelect.move(quitSelect);
                if (click) {
                    DyeHard.state = DyeHard.State.QUIT;
                }
            }
        }
    }

    public void setMenu(boolean win) {
        if (win) {
            menuHud.texture = BaseCode.resources
                    .loadImage("Textures/UI/Win_Menu.png");
        } else {
            menuHud.texture = BaseCode.resources
                    .loadImage("Textures/UI/Lose_Menu.png");
        }
    }

    private class MenuSelect {
        private final DyehardRectangle sel;

        private MenuSelect(Vector2 pos) {
            sel = new DyehardRectangle();
            sel.size = new Vector2(2.8f, 2.8f);
            sel.center = new Vector2(pos);
            sel.texture = BaseCode.resources
                    .loadImage("Textures/UI/UI_Menu_Select.png");
            sel.alwaysOnTop = true;
            sel.visible = false;
        }

        private void active(boolean active) {
            if (active) {
                BaseCode.resources.moveToFrontOfDrawSet(sel);
            }
            sel.visible = active;
        }

        private void move(Vector2 pos) {
            if (pos != sel.center) {
                sel.center = pos;
                active(true);
            }
        }
    }
}
