package dyehard;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.Background.CreditScreen;
import dyehard.Util.DyeHardSound;

public class DyehardMenuUI {
    private final MenuSelect menuSelect;
    private final MenuSelect soundTog;
    private final MenuSelect musicTog;
    private final DyehardRectangle menuHud;
    private final CreditScreen credit;

    private boolean music = true;

    // positions for selection texture
    private final Vector2 soundOn = new Vector2(46.328f, 36.111f);
    private final Vector2 soundOff = new Vector2(56.614f, 36.111f);
    private final Vector2 musicOn = new Vector2(46.328f, 27.345f);
    private final Vector2 musicOff = new Vector2(56.614f, 27.345f);
    private final Vector2 restartSelect = new Vector2(38.383f, 42.506f);
    private final Vector2 soundSelect = new Vector2(38.383f, 36.111f);
    private final Vector2 musicSelect = new Vector2(38.383f, 27.345f);
    private final Vector2 creditSelect = new Vector2(38.383f, 18.95f);
    private final Vector2 quitSelect = new Vector2(38.383f, 12.722f);

    public DyehardMenuUI() {
        menuSelect = new MenuSelect(restartSelect);
        soundTog = new MenuSelect(soundOn);
        musicTog = new MenuSelect(musicOn);

        menuHud = new DyehardRectangle();
        menuHud.size = new Vector2(40f, 40f);
        menuHud.center = new Vector2(BaseCode.world.getWidth() / 2,
                BaseCode.world.getHeight() / 2);
        menuHud.texture = BaseCode.resources
                .loadImage("Textures/UI/UI_Menu.png");
        menuHud.alwaysOnTop = true;
        menuHud.visible = false;

        credit = new CreditScreen();
    }

    public void active(boolean active) {
        if (active) {
            BaseCode.resources.moveToFrontOfDrawSet(menuHud);
        }
        menuHud.visible = active;
        soundTog.active(active);
        musicTog.active(active);
        menuSelect.active(active);
    }

    public void select(float x, float y, boolean click) {
        // x value line up of buttons in game world
        if ((x > 35.45f) && (x < 59.68f)) {
            // hit button 1, restart
            if ((y > 42.75f) && (y < 47.125f)) {
                menuSelect.move(restartSelect);
                if (click) {
                    DyeHard.state = DyeHard.State.RESTART;
                }
            }
            // hit button 2, sound toggle
            else if ((y > 36.125f) && (y < 40.75f)) {
                menuSelect.move(soundSelect);
                if (click) {
                    DyeHardSound.setSound(!DyeHardSound.getSound());
                    if (DyeHardSound.getSound()) {
                        soundTog.move(soundOn);
                    } else {
                        soundTog.move(soundOff);
                    }
                }
            }
            // hit button 3, music toggle
            else if ((y > 27.625f) && (y < 32.125f)) {
                menuSelect.move(musicSelect);
                if (click) {
                    music = !music;
                    DyeHardSound.setMusic(music);
                    if (music) {
                        DyeHardSound.playBgMusic();
                        musicTog.move(musicOn);
                    } else {
                        DyeHardSound.stopBgMusic();
                        musicTog.move(musicOff);
                    }
                }
            }
            // hit button 4, credits
            else if ((y > 19.25f) && (y < 23.75f)) {
                menuSelect.move(creditSelect);
                if (click) {
                    DyeHard.state = DyeHard.State.PAUSED;
                    credit.showScreen(true);
                }
            }
            // hit button 5, quit
            else if ((y > 13.125f) && (y < 17.5f)) {
                menuSelect.move(quitSelect);
                if (click) {
                    DyeHard.state = DyeHard.State.QUIT;
                }
            } else {
                // menuSelect.active(false);
            }
        }
        // sound on/off
        if ((y > 33.375) && (y < 35.625)) {
            // sound on
            if ((x > 49.25) && (x < 54.25)) {
                menuSelect.move(soundSelect);
                if (click) {
                    DyeHardSound.setSound(true);
                    soundTog.move(soundOn);
                }
            }
            // sound off
            else if ((x > 59.5) && (x < 65.625)) {
                menuSelect.move(soundSelect);
                if (click) {
                    DyeHardSound.setSound(false);
                    soundTog.move(soundOff);
                }
            }
        }
        // music on/off
        else if ((y > 24.75) && (y < 26.875)) {
            // music on
            if ((x > 49.25) && (x < 54.25)) {
                menuSelect.move(musicSelect);
                if (click) {
                    if (!music) {
                        DyeHardSound.setMusic(true);
                        DyeHardSound.playBgMusic();
                        musicTog.move(musicOn);
                        music = true;
                    }
                }
            }
            // music off
            else if ((x > 59.5) && (x < 65.625)) {
                menuSelect.move(musicSelect);
                if (click) {
                    if (music) {
                        DyeHardSound.setMusic(false);
                        DyeHardSound.stopBgMusic();
                        musicTog.move(musicOff);
                        music = false;
                    }
                }
            }
        }
    }

    public void CreditOff() {
        if (credit.isShown()) {
            credit.showScreen(false);
        }
    }

    public boolean getCredit() {
        return credit.isShown();
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
            BaseCode.resources.moveToFrontOfDrawSet(sel);
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
