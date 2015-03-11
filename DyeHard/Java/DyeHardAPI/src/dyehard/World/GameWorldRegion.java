package dyehard.World;

public abstract class GameWorldRegion {
    protected float width;
    protected float position;
    protected float speed;

    public float leftEdge() {
        return position - width / 2;
    }

    public float rightEdge() {
        return position + width / 2;
    }

    public float getWidth() {
        return width;
    }

    public void moveLeft() {
        position += speed;
    }

    // for debugging speedup purpose
    public void moveLeft(float factor) {
        // #TODO maybe take out for final version
        position += speed * factor;
    }

    // Instructs the region to construct its components using the leftEdge as
    // its starting location
    public abstract void initialize(float leftEdge);

    public abstract void destroy();
}
