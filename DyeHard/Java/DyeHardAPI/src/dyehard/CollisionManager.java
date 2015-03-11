package dyehard;

import java.util.HashSet;
import java.util.Set;

/**
 * Collision Manager provides a single class to handle collisions between
 * objects.
 * 
 * Registering an Collidable with CollidableManager indicates that it is ready
 * to start colliding with registered Actors and vice versa.
 * 
 * Calling CollidableManager.update() will call update on the registered
 * Collidables and then perform the collision checks handling the necessary
 * interactions between actors and Collidables.
 * 
 * @author Rodelle Ladia Jr.
 * 
 */
public class CollisionManager {
    static Set<Collidable> collidables = new HashSet<Collidable>();
    static Set<Collidable> newCollidables = new HashSet<Collidable>();

    /**
     * Indicates that Actors are ready to start colliding with other registered
     * actors and collidables.
     */
    public static void registerActor(Collidable c) {
        if (c != null) {
            newCollidables.add(c);
        }
    }

    /**
     * Registers an object that can collide with registered actors. Registered
     * collidables do not collide with other registered collidables.
     */
    public static void registerCollidable(Collidable c) {
        if (c != null) {
            newCollidables.add(c);
        }
    }

    public static void update() {

        for (Collidable c1 : collidables) {
            if (c1.collideState() != ManagerState.ACTIVE) {
                continue;
            } else {
                for (Collidable c2 : collidables) {
                    if (c2.collideState() != ManagerState.ACTIVE) {
                        continue;
                    }

                    else if (c1 != c2 && c1.collided(c2)) {
                        c1.handleCollision(c2);
                        c2.handleCollision(c1);
                    }
                }
            }
        }

        collidables.addAll(newCollidables);
        newCollidables.clear();
        removeInactiveObjects(collidables);
    }

    private static void removeInactiveObjects(Set<Collidable> set) {
        Set<Collidable> destroyed = new HashSet<Collidable>();
        for (Collidable o : set) {
            if (o == null || o.collideState() == ManagerState.DESTROYED) {
                destroyed.add(o);
            }
        }

        set.removeAll(destroyed);
    }

    public static final Set<Collidable> getCollidables() {
        return collidables;
    }
}
