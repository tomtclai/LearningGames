package dyehard.Obstacles;

import java.awt.Color;

import Engine.Vector2;
import dyehard.Collidable;
import dyehard.Configuration;

/**
 * Obstacles represent objects that friendly and enemy units are unable to pass.
 * Obstacles can range from moving platforms that the player is able to jump
 * onto to walls that can be combined to form a maze.
 * 
 * @author Rodelle Ladia Jr.
 * 
 */
public class Obstacle extends Collidable {

    public Obstacle() {
    }

    public Obstacle(Vector2 center, Vector2 size, Color color) {
        this.center = center;
        this.size = size;
        velocity = new Vector2(-Configuration.worldGameSpeed, 0f);
        shouldTravel = true;
        this.color = color;
    }

    @Override
    public void handleCollision(Collidable other) {
        // Obstacles by default do not handle collisions
        return;
    }
}
