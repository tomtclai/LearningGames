package dyehard;

import java.util.HashSet;
import java.util.Set;

public class UpdateManager {
    public static boolean speedUp = false;

    public interface Updateable {

        public void update();

        public void setSpeed(float factor);

        public ManagerState updateState();
    }

    static Set<Updateable> gameObjects = new HashSet<Updateable>();
    static Set<Updateable> newlyRegisteredObjects = new HashSet<Updateable>();

    public static void update() {
        for (Updateable o : gameObjects) {
            if (o != null && o.updateState() == ManagerState.ACTIVE) {
                o.update();
                if (speedUp) {
                    o.setSpeed(10f);
                } else if (!speedUp) {
                    o.setSpeed(0.1f);
                }
            }
        }

        gameObjects.addAll(newlyRegisteredObjects);
        newlyRegisteredObjects.clear();

        Set<Updateable> destroyed = new HashSet<Updateable>();
        for (Updateable o : gameObjects) {
            if (o == null || o.updateState() == ManagerState.DESTROYED) {
                destroyed.add(o);
            }
        }

        gameObjects.removeAll(destroyed);
    }

    public static void register(Updateable o) {
        if (o != null) {
            newlyRegisteredObjects.add(o);
        }
    }
}
