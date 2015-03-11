package dyehard;

import Engine.BaseCode;

public abstract class Collidable extends GameObject {
    protected ManagerState collidableState;

    public boolean heroUp = false;
    public boolean heroDown = false;
    public boolean heroLeft = false;
    public boolean heroRight = false;

    private static final float offset = 30f;

    public Collidable() {
        collidableState = ManagerState.ACTIVE;
        CollisionManager.registerCollidable(this);
    }

    public ManagerState collideState() {
        return collidableState;
    }

    public abstract void handleCollision(Collidable other);

    @Override
    public void update() {
        super.update();
        if (!isInsideWorld(this)) {
            destroy();
        }
    }

    public void updateGate() {
        super.update();
    }

    private static boolean isInsideWorld(Collidable o) {
        // The Collidable is destroyed once it's too far from the map to the
        // left, top, or bottom portion of the map. offset is 30f for now
        if ((o.center.getX() < (BaseCode.world.getPositionX() - offset))
                || (o.center.getY() < (BaseCode.world.getWorldPositionY()))
                || (o.center.getY() > (BaseCode.world.getHeight() + offset))) {
            return false;
        }

        return true;
    }

    // public void revertCollideStatus(Hero hero) {
    // if (heroUp) {
    // heroUp = false;
    // hero.collideDown = false;
    // System.out.println(hero.collideDown);
    // }
    // if (heroDown) {
    // heroDown = false;
    // hero.collideUp = false;
    // System.out.println(hero.collideUp);
    // }
    // if (heroLeft) {
    // heroLeft = false;
    // hero.collideRight = false;
    // System.out.println(hero.collideRight);
    // }
    // if (heroRight) {
    // heroRight = false;
    // hero.collideRight = false;
    // System.out.println(hero.collideLeft);
    // }
    // }

    @Override
    public void destroy() {
        super.destroy();
        collidableState = ManagerState.DESTROYED;
    }
}
