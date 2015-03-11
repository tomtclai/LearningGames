package dyehard.Background;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import Engine.BaseCode;
import Engine.Rectangle;
import Engine.Text;
import Engine.Vector2;
import dyehard.DHR;
import dyehard.DHR.ImageID;
import dyehard.DyehardDistanceMeter;
import dyehard.ManagerState;
import dyehard.UpdateObject;
import dyehard.Player.DyeMeter;
import dyehard.Player.Hero;
import dyehard.World.GameState;

public class DyehardUI extends UpdateObject {
    protected Hero hero;
    Rectangle hud;
    DyehardDistanceMeter distanceMeter;
    Text scoreText;
    List<Rectangle> hearts = new ArrayList<Rectangle>();

    public DyehardUI(Hero hero) {
        this.hero = hero;
        new DyeMeter(hero);

        hud = DHR.getScaledRectangle(ImageID.UI_HUD);
        hud.center.setX(BaseCode.world.getWidth() / 2);
        hud.center.setY(fromTop(hud, 0f));
        hud.alwaysOnTop = true;

        Rectangle baseHeart = DHR.getScaledRectangle(ImageID.UI_HEART);
        baseHeart.alwaysOnTop = true;
        hearts = new ArrayList<Rectangle>();
        for (int i = 0; i < 4; ++i) {
            Rectangle heart = new Rectangle(baseHeart);
            float width = heart.size.getX();

            // TODO magic numbers
            heart.center = new Vector2(BaseCode.world.getWidth() - i * 1.62f
                    * width - 4f, BaseCode.world.getHeight() - width / 2 - 1.4f);
            hearts.add(heart);
        }

        baseHeart.visible = false;

        distanceMeter = new DyehardDistanceMeter(GameState.TargetDistance);

        // TODO magic numbers
        scoreText = new Text("", 4f, BaseCode.world.getHeight() - 3.25f);
        scoreText.setFrontColor(Color.white);
        scoreText.setBackColor(Color.black);
        scoreText.setFontSize(18);
        scoreText.setFontName("Arial");
    }

    protected float fromTop(Rectangle image, float padding) {
        return BaseCode.world.getHeight() - image.size.getY() / 2f - padding;
    }

    @Override
    public ManagerState updateState() {
        return ManagerState.ACTIVE;
    }

    @Override
    public void update() {
        for (Rectangle r : hearts) {
            r.visible = false;
        }

        int numHearts = hearts.size() - 1;
        for (int i = 0; i < GameState.RemainingLives; ++i) {
            hearts.get(numHearts - i).visible = true;
        }
        scoreText.setText(Integer.toString(GameState.Score));
        distanceMeter.setValue(GameState.DistanceTravelled);

        // controls texture of the progress marker in UI Bar
        switch (hero.curPowerUp) {
        case GHOST:
            distanceMeter.setProgTexture("UI/UI_Ghost");
            break;
        case INVIN:
            distanceMeter.setProgTexture("UI/UI_Invincibility");
            break;
        case MAGNET:
            distanceMeter.setProgTexture("UI/UI_Magnetism");
            break;
        case OVERLOAD:
            distanceMeter.setProgTexture("UI/UI_Overload");
            break;
        case SLOW:
            distanceMeter.setProgTexture("UI/UI_SlowDown");
            break;
        case SPEED:
            distanceMeter.setProgTexture("UI/UI_SpeedUp");
            break;
        case UNARMED:
            distanceMeter.setProgTexture("UI/UI_Unarmed");
            break;
        case REPEL:
            distanceMeter.setProgTexture("UI/UI_Repel");
            break;
        case GRAVITY:
            distanceMeter.setProgTexture("UI/Dyehard_UI_Progress_marker_empty");
            break;
        case NONE:
            distanceMeter.setProgTexture("UI/Dyehard_UI_Progress_marker_empty");
            break;
        }

    }

    public void drawFront() {
        BaseCode.resources.moveToFrontOfDrawSet(scoreText);
        distanceMeter.drawFront();
        for (Rectangle h : hearts) {
            BaseCode.resources.moveToFrontOfDrawSet(h);
        }
        BaseCode.resources.moveToFrontOfDrawSet(hud);
    }

    @Override
    public void setSpeed(float v) {
        // TODO Auto-generated method stub

    }
}
