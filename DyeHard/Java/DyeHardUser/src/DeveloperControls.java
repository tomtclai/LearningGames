import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import Engine.BaseCode;
import Engine.Text;
import Engine.Vector2;
import dyehard.DyehardKeyboard;
import dyehard.DyehardRectangle;
import dyehard.UpdateManager;
import dyehard.UpdateObject;
import dyehard.Collectibles.Ghost;
import dyehard.Collectibles.Gravity;
import dyehard.Collectibles.Invincibility;
import dyehard.Collectibles.Magnetism;
import dyehard.Collectibles.Overload;
import dyehard.Collectibles.PowerUp;
import dyehard.Collectibles.Repel;
import dyehard.Collectibles.SlowDown;
import dyehard.Collectibles.SpeedUp;
import dyehard.Collectibles.Unarmed;
import dyehard.Enemies.Enemy;
import dyehard.Enemies.EnemyManager;
import dyehard.Player.Hero;
import dyehard.Util.DyeHardSound;
import dyehard.Weapons.LimitedAmmoWeapon;

public class DeveloperControls extends UpdateObject {
    private final Hero hero;

    Text weaponText;
    List<Text> powerupText;

    // #TODO take out recs, to showcase fps drop only
    private final List<DyehardRectangle> recs;
    private boolean recVis = false;

    private final HashMap<Integer, PowerUp> generationHotkeys;

    public DeveloperControls(Hero hero) {
        super();
        this.hero = hero;

        generationHotkeys = new HashMap<Integer, PowerUp>();
        // generationHotkeys.put(KeyEvent.VK_0, new DyePack());
        generationHotkeys.put(KeyEvent.VK_Z, new Ghost());
        generationHotkeys.put(KeyEvent.VK_X, new Invincibility());
        generationHotkeys.put(KeyEvent.VK_C, new Overload());
        generationHotkeys.put(KeyEvent.VK_V, new SpeedUp());
        generationHotkeys.put(KeyEvent.VK_B, new SlowDown());
        generationHotkeys.put(KeyEvent.VK_N, new Unarmed());
        generationHotkeys.put(KeyEvent.VK_M, new Magnetism());
        generationHotkeys.put(KeyEvent.VK_COMMA, new Gravity());
        generationHotkeys.put(KeyEvent.VK_PERIOD, new Repel());

        weaponText = createTextAt(3f, 1f);
        powerupText = new ArrayList<Text>();

        recs = new ArrayList<DyehardRectangle>();
        float w = 20f;
        float h = 14f;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                DyehardRectangle rec = new DyehardRectangle();
                rec.texture = BaseCode.resources
                        .loadImage("Textures/Background/Warp_pink_front.png");
                rec.center = new Vector2(i * w + w / 2, j * h + h / 2);
                rec.size = new Vector2(w, h);
                rec.visible = false;

                DyehardRectangle rec2 = new DyehardRectangle();
                rec2.texture = BaseCode.resources
                        .loadImage("Textures/Background/Warp_pink_back.png");
                rec2.center = new Vector2(i * w + w / 2, j * h + h / 2);
                rec2.size = new Vector2(w, h);
                rec2.visible = false;

                recs.add(rec);
                recs.add(rec2);
            }
        }

        UpdateManager.register(this);
    }

    @Override
    public void update() {
        weaponText.setText("Weapon: " + hero.currentWeapon.toString());

        updatePowerupText();

        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_D)) {
            if (recVis) {
                for (DyehardRectangle r : recs) {
                    r.visible = false;
                }
                recVis = false;
            } else {
                for (DyehardRectangle r : recs) {
                    r.visible = true;
                }
                recVis = true;
            }
        }

        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_W)) {
            DyeHardSound.play(DyeHardSound.winSound);
        }
        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_L)) {
            DyeHardSound.play(DyeHardSound.loseSound);
        }

        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_E)) {
            EnemyGenerator.generateEnemy();
        }

        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_P)) {
            EnemyGenerator.generateEnemy(1);
        }
        // if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_9)) {
        // EnemyGenerator.generateEnemy(1);
        // }
        // if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_0)) {
        // EnemyGenerator.generateEnemy(1);
        // }

        // 'K' to kill all the enemies on screen
        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_K)) {
            for (Enemy e : EnemyManager.getEnemies()) {
                e.kill(null);
            }
        }

        for (int hotkey : generationHotkeys.keySet()) {
            if (DyehardKeyboard.isKeyTapped(hotkey)) {
                generateCollectible(generationHotkeys.get(hotkey));
            }
        }

        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_R)) {
            if (hero.currentWeapon instanceof LimitedAmmoWeapon) {
                ((LimitedAmmoWeapon) hero.currentWeapon).reload();
            }
        }

        if (DyehardKeyboard.isKeyTapped(KeyEvent.VK_I)) {
            hero.debugInvincibility = !hero.debugInvincibility;
        }

        // Debug speed up 10x
        if (DyehardKeyboard.isKeyDown(KeyEvent.VK_G)) {
            UpdateManager.speedUp = true;
        } else {
            UpdateManager.speedUp = false;
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
        sortedPowerups.addAll(hero.powerups);

        if (sortedPowerups.size() > powerupText.size()) {
            for (int i = powerupText.size(); i < sortedPowerups.size(); ++i) {
                powerupText
                        .add(createTextAt(3f, GameWorld.TOP_EDGE - 3 - i * 2));
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

    private void generateCollectible(PowerUp powerUp) {
        Vector2 position = new Vector2(60f, 35f);

        PowerUp p = powerUp.clone();
        p.initialize(position);
    }

    @Override
    public void setSpeed(float v) {
        // TODO Auto-generated method stub

    }
}
