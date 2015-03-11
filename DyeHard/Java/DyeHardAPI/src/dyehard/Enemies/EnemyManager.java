package dyehard.Enemies;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {
    public static List<Enemy> enemies;

    static {
        enemies = new ArrayList<Enemy>();
    }

    public static void registerEnemy(Enemy e) {
        e.initialize();
        enemies.add(e);
    }

    public static void clear() {
        for (Enemy e : enemies) {
            e.destroy();
        }
        enemies.clear();
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }
}
