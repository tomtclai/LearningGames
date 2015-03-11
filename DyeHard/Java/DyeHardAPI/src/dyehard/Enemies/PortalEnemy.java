package dyehard.Enemies;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.Configuration;
import dyehard.Configuration.EnemyType;
import dyehard.Player.Hero;
import dyehard.Util.Timer;

public class PortalEnemy extends Enemy {
    protected Timer timer;
    protected float portalSpawnInterval;

    public PortalEnemy(Vector2 center, Hero currentHero) {
        super(center, 0, 0, currentHero,
                "Textures/Enemies/PortalMinion_AnimSheet_Left.png");
        setUsingSpriteSheet(true);
        setSpriteSheet(texture, 140, 140, 12, 2);

        portalSpawnInterval = Float.parseFloat(parseNodeList(
                EnemyType.PORTAL_ENEMY, "portalSpawnInterval")) * 1000;

        timer = new Timer(portalSpawnInterval);

        width = Configuration.getEnemyData(EnemyType.PORTAL_ENEMY).width;
        height = Configuration.getEnemyData(EnemyType.PORTAL_ENEMY).height;
        sleepTimer = Configuration.getEnemyData(EnemyType.PORTAL_ENEMY).sleepTimer * 1000f;
        speed = Configuration.getEnemyData(EnemyType.PORTAL_ENEMY).speed;
    }

    @Override
    public void update() {
        if (harmlessTimer.isDone()) {
            harmlessTimer.setActive(false);
            color = null;
            setSpriteSheet(Hero.portalEnemyTextures.get(Color.gray), 140, 140,
                    12, 2);
        }
        if (timer.isDone()) {
            new Portal(center.clone(), hero);
            timer.reset();
        }
        if (center.getX() < BaseCode.world.getPositionX() - 5f) {
            destroy();
        }
    }

    @Override
    public void setColor(Color color) {
        if (this.color != color) {
            this.color = color;
            int temp = getCurFrame();
            setSpriteSheet(Hero.portalEnemyTextures.get(color), 140, 140, 12, 2);
            setCurFrame(temp);
        }
    }

    @Override
    public String toString() {
        return "Portal";
    }
}