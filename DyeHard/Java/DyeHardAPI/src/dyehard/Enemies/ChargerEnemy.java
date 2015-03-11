package dyehard.Enemies;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.Configuration;
import dyehard.Configuration.EnemyType;
import dyehard.Player.Hero;
import dyehard.Util.DyeHardSound;
import dyehard.Util.Timer;

public class ChargerEnemy extends Enemy {
    private boolean chasing;
    private boolean setImage;
    private boolean charge;
    private final float xOffset;
    private final float yOffset;
    private final Timer t;

    public ChargerEnemy(Vector2 center, Hero currentHero) {
        super(center, 0, 0, currentHero,
                "Textures/Enemies/Charger_AnimSheet_Idle.png");

        width = Configuration.getEnemyData(EnemyType.CHARGER_ENEMY).width;
        height = Configuration.getEnemyData(EnemyType.CHARGER_ENEMY).height;
        sleepTimer = Configuration.getEnemyData(EnemyType.CHARGER_ENEMY).sleepTimer * 1000f;
        speed = Configuration.getEnemyData(EnemyType.CHARGER_ENEMY).speed;

        chasing = false;
        setImage = false;
        charge = false;
        xOffset = 45f;
        yOffset = 7f;

        setUsingSpriteSheet(true);
        setSpriteSheet(texture, 340, 140, 13, 2);

        t = new Timer(2000);
    }

    @Override
    public void update() {
        float deltaT = t.deltaTime();

        if (harmlessTimer.isDone()) {
            harmlessTimer.setActive(false);
            color = null;
            if (chasing) {
                setSpriteSheet(Hero.chargerAttackTextures.get(Color.gray), 340,
                        140, 11, 2);
            } else {
                setSpriteSheet(Hero.chargerIdleTextures.get(Color.gray), 340,
                        140, 11, 2);
            }
        }

        if (chasing) {
            DyeHardSound.playLoop(DyeHardSound.enemySpaceship1);
            soundOn = true;
            if (!setImage) {
                if (color == null) {
                    setSpriteSheet(Hero.chargerAttackTextures.get(Color.gray),
                            340, 140, 11, 2);
                } else {
                    setSpriteSheet(Hero.chargerAttackTextures.get(color), 340,
                            140, 11, 2);
                }
                setImage = true;
                t.reset();
            }
            if (t.isDone() && (!charge)) {
                charge = true;
            }
            if (charge) {
                center = center.clone().add(
                        new Vector2(-speed * (deltaT * 500), 0f));
            }
        } else {
            center = center.clone().add(
                    new Vector2(-speed * (deltaT * 100), 0f));
            float xDif = Math.abs(hero.center.getX() - center.getX());
            float yDif = center.getY() - hero.center.getY();
            if ((xDif < xOffset) && (yDif < yOffset)
                    && (center.getX() < (BaseCode.world.getWidth()) - 5)) {
                chasing = true;
            }
        }

        if (center.getX() < BaseCode.world.getPositionX()) {
            destroy();
        }
    }

    @Override
    public void setColor(Color color) {
        if (this.color != color) {
            this.color = color;
            int temp = getCurFrame();
            if (chasing) {
                setSpriteSheet(Hero.chargerAttackTextures.get(color), 340, 140,
                        11, 8);
            } else {
                setSpriteSheet(Hero.chargerIdleTextures.get(color), 340, 140,
                        11, 8);
            }
            setCurFrame(temp);
        }
    }

    @Override
    public String toString() {
        return "Charger";
    }

    @Override
    public void destroy() {
        if (soundOn) {
            DyeHardSound.stopSound(DyeHardSound.enemySpaceship1);
        }
        super.destroy();
    }
}