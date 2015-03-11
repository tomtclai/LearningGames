import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import Engine.BaseCode;
import Engine.Text;
import Engine.Vector2;
import dyehard.CollisionManager;
import dyehard.DyeHard;
import dyehard.StudentObjManager;
import dyehard.UpdateManager;
import dyehard.Collectibles.DyePack;
import dyehard.Collectibles.Ghost;
import dyehard.Collectibles.Invincibility;
import dyehard.Collectibles.Magnetism;
import dyehard.Collectibles.Overload;
import dyehard.Collectibles.PowerUp;
import dyehard.Collectibles.Repel;
import dyehard.Collectibles.SlowDown;
import dyehard.Collectibles.SpeedUp;
import dyehard.Collectibles.Unarmed;
import dyehard.Player.Hero;
import dyehard.Util.Colors;

public class UserCode extends DyeHard {
    private Hero h;
    // private ClassReflector cf;
    private StudentObj hero;

    private List<Text> powerupText;
    private List<PowerUp> powerUpTypes;

    private HashSet<StudentObj> enemies;
    private Random RANDOM = new Random();

    @Override
    protected void initialize() {
        sample1Ini();
        // sample2Ini();
        sample3Ini();
        // sample4Ini();
    }

    private void sample1Ini() {
        StudentObjManager.validate();
        hero = new StudentObj();
        h = StudentObjManager.registerHero(hero);
    }

    private void sample2Ini() {
        powerupText = new ArrayList<Text>();

        powerUpTypes = new ArrayList<PowerUp>();

        powerUpTypes.add(new Ghost());
        powerUpTypes.add(new Invincibility());
        powerUpTypes.add(new Magnetism());
        powerUpTypes.add(new Overload());
        powerUpTypes.add(new SlowDown());
        powerUpTypes.add(new SpeedUp());
        powerUpTypes.add(new Unarmed());
        powerUpTypes.add(new Repel());
    }

    private void sample3Ini() {
        enemies = new HashSet<StudentObj>();
        RANDOM = new Random();
    }

    private void sample4Ini() {
        // new WormHole(hero, Colors.randomColor(), 30f, 15f, 100f, 20f);
    }

    @Override
    protected void update() {

        // sample2Update();
        sample3Update();
        // sample4Update();
        sample1Update();
    }

    private void sample1Update() {
        UpdateManager.update();
        CollisionManager.update();
        StudentObjManager.update();
        // cf.invokeMethod(hero, "moveTo", mouse.getWorldX(),
        // mouse.getWorldY());
        // hero.moveTo(mouse.getWorldX(), mouse.getWorldY());

        hero.setCenter(mouse.getWorldX(), mouse.getWorldY());

        if ((keyboard.isButtonDown(KeyEvent.VK_F)) || (mouse.isButtonDown(1))) {
            // cf.invokeMethod(hero, "fire");
            h.currentWeapon.fire();
        }
    }

    private void sample2Update() {
        updatePowerupText();

        if (keyboard.isButtonTapped(KeyEvent.VK_P)) {
            float randomY = RANDOM
                    .nextInt((int) BaseCode.world.getHeight() - 8) + 5;
            Vector2 position = new Vector2(BaseCode.world.getWidth() - 5,
                    randomY);
            PowerUp randomPowerUp = powerUpTypes.get(RANDOM
                    .nextInt(powerUpTypes.size()));
            randomPowerUp.initialize(position);
        }

        if (keyboard.isButtonTapped(KeyEvent.VK_D)) {
            float randomY = RANDOM
                    .nextInt((int) BaseCode.world.getHeight() - 8) + 5;
            Vector2 position = new Vector2(BaseCode.world.getWidth() - 5,
                    randomY);
            Color randomColor = Colors.randomColor();
            DyePack dye = new DyePack(randomColor);
            dye.initialize(position);
        }
    }

    private void sample3Update() {
        for (StudentObj temp : enemies) {
            temp.setWidth((RANDOM.nextFloat() + 0.1f) * 7f);
            temp.setHeight((RANDOM.nextFloat() + 0.1f) * 7f);
        }
        if (keyboard.isButtonTapped(KeyEvent.VK_C)) {
            for (StudentObj temp : enemies) {
                temp.setColor(Colors.randomColor());
            }
        }
        if (keyboard.isButtonTapped(KeyEvent.VK_E)) {
            float randomY = RANDOM
                    .nextInt((int) BaseCode.world.getHeight() - 8) + 5;
            Vector2 position = new Vector2(BaseCode.world.getWidth() - 20,
                    randomY);
            StudentObj e = new StudentObj(position, 5f, 5f);
            enemies.add(e);
            StudentObjManager.registerEnemy(e);
        }
    }

    private void sample4Update() {
        float randomY = RANDOM.nextInt((int) BaseCode.world.getHeight() - 8) + 5;
        Vector2 position = new Vector2(BaseCode.world.getWidth() - 5, randomY);
        if (keyboard.isButtonTapped(KeyEvent.VK_G)) {
            // new WormHole(hero, Colors.randomColor(), 60f, 15f,
            // position.getX(),
            // position.getY());
        }
    }

    private void updatePowerupText() {
        TreeSet<PowerUp> sortedPowerups = new TreeSet<PowerUp>(
                new Comparator<PowerUp>() {
                    @Override
                    public int compare(PowerUp o1, PowerUp o2) {
                        return (int) (o1.getRemainingTime() - o2
                                .getRemainingTime());
                    }
                });
        // sortedPowerups.addAll(hero.powerups);

        if (sortedPowerups.size() > powerupText.size()) {
            for (int i = powerupText.size(); i < sortedPowerups.size(); ++i) {
                powerupText.add(createTextAt(3f, BaseCode.world.getHeight() - 3
                        - i * 2));
            }
        }

        int i = 0;
        for (PowerUp p : sortedPowerups) {
            powerupText.get(i).setText(p.toString());
            i++;
        }

        for (; i < powerupText.size(); ++i) {
            powerupText.get(i).setText("");
        }
    }

    private Text createTextAt(float x, float y) {
        Text text = new Text("", x, y);
        text.setFrontColor(Color.white);
        text.setBackColor(Color.black);
        text.setFontSize(18);
        text.setFontName("Arial");
        return text;
    }

}