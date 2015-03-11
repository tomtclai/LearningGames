package dyehard.Enemies;

import Engine.Vector2;
import dyehard.Configuration;
import dyehard.Configuration.EnemyType;
import dyehard.Player.Hero;

public class CollectorEnemy extends Enemy {

    public CollectorEnemy(Vector2 center, Hero currentHero) {
        super(center, 0, 0, currentHero,
                "Textures/Enemies/minion_collector.png");

        width = Configuration.getEnemyData(EnemyType.COLLECTOR_ENEMY).width;
        height = Configuration.getEnemyData(EnemyType.COLLECTOR_ENEMY).height;
        sleepTimer = Configuration.getEnemyData(EnemyType.COLLECTOR_ENEMY).sleepTimer * 1000f;
        speed = Configuration.getEnemyData(EnemyType.COLLECTOR_ENEMY).speed;
    }

    @Override
    public String toString() {
        return "Collector";
    }
}