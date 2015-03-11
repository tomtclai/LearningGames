package dyehard.Util;

import Engine.Vector2;
import dyehard.DyehardRectangle;

public class Collision {

    /**
     * Tests for collision between two rectangles. If there is a collision, out
     * contains the shortest distance to move the first rectangle to avoid
     * collision with the second rectangle. The shortest distance is calculated
     * by finding the smallest overlap in the axis aligned extents of each
     * rectangle.
     * 
     * @param a
     *            The moving rectangle
     * @param b
     *            The stationary rectangle
     * @param out
     *            The shortest distance to move A to avoid overlapping with B
     * @return
     */
    public static boolean isOverlap(DyehardRectangle a, DyehardRectangle b,
            Vector2 out) {
        // if the overlap test fails, there is no collision
        if (!a.collided(b)) {
            out = new Vector2(0, 0);
            return false;
        }

        // Calculate x-extents of Rectangle A and B
        float aMinX = a.center.getX() - a.size.getX() / 2f;
        float aMaxX = a.center.getX() + a.size.getX() / 2f;

        float bMinX = b.center.getX() - b.size.getX() / 2f;
        float bMaxX = b.center.getX() + b.size.getX() / 2f;

        float deltaX = Float.MAX_VALUE;
        float overlap = 0f;

        // Compare x-extents and find the smallest overlap
        if (aMinX < bMaxX) {
            overlap = bMaxX - aMinX;
            deltaX = Math.abs(deltaX) < Math.abs(overlap) ? deltaX : overlap;
        }

        if (aMaxX > bMinX) {
            overlap = bMinX - aMaxX;
            deltaX = Math.abs(deltaX) < Math.abs(overlap) ? deltaX : overlap;
        }

        // Calculate y-extents of Rectangle A and B
        float aMinY = a.center.getY() - a.size.getY() / 2f;
        float aMaxY = a.center.getY() + a.size.getY() / 2f;

        float bMinY = b.center.getY() - b.size.getY() / 2f;
        float bMaxY = b.center.getY() + b.size.getY() / 2f;

        float deltaY = Float.MAX_VALUE;
        overlap = 0f;

        // Compare y-extents and find the smallest overlap
        if (aMaxY > bMinY) {
            overlap = bMinY - aMaxY;
            deltaY = Math.abs(deltaY) < Math.abs(overlap) ? deltaY : overlap;
        }

        if (aMinY < bMaxY) {
            overlap = bMaxY - aMinY;
            deltaY = Math.abs(deltaY) < Math.abs(overlap) ? deltaY : overlap;
        }

        // Move the rectangle by the shortest distance to avoid overlap
        if (Math.abs(deltaX) < Math.abs(deltaY)) {
            out.setX(deltaX);
        } else {
            out.setY(deltaY);
        }

        return true;
    }
}
