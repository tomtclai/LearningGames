package dyehard;

import java.awt.Color;

import Engine.Primitive;
import Engine.Vector2;
import dyehard.Enemies.ChargerEnemy;
import dyehard.Obstacles.Obstacle;
import dyehard.Util.Collision;
import dyehard.World.PlatformSingle;

public class Actor extends Collidable {
    protected boolean alive;
    public boolean collideLeft = false;
    public boolean collideRight = false;
    public boolean collideUp = false;
    public boolean collideDown = false;

    public Actor(Vector2 position, float width, float height) {
        CollisionManager.registerActor(this);
        center = position;
        size.set(width, height);
        color = null;
        // set object into motion;
        velocity = new Vector2(0, 0);
        shouldTravel = true;
        alive = true;
    }

    public void setColor(Color color) {
        this.color = color;
        // position.TextureTintColor = color;
    }

    public Color getColor() {
        return color;
    }

    public void kill(Primitive who) {
        destroy();
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Obstacle) {
            if ((this instanceof ChargerEnemy)
                    && (!(other instanceof PlatformSingle))) {
                other.destroy();
            } else {
                collideWith(this, (Obstacle) other);
            }
        }
    }

    private static void collideWith(Actor actor, Obstacle obstacle) {
        // Check collisions with each character and push them out of the
        // Collidable. This causes the player and enemy units to glide along the
        // edges of the Collidable
        Vector2 out = new Vector2(0, 0);
        if (Collision.isOverlap(actor, obstacle, out)) {
            // Move the character so that it's no longer overlapping the
            // debris
            actor.center.add(out);

            // Stop the character from moving if they collide with the
            // Collidable
            if (Math.abs(out.getX()) > 0f) {
                if (Math.signum(out.getX()) != Math.signum(actor.velocity
                        .getX())) {
                    actor.velocity.setX(0f);
                }
            }

            if (Math.abs(out.getY()) > 0f) {
                if (Math.signum(out.getY()) != Math.signum(actor.velocity
                        .getY())) {
                    actor.velocity.setY(0f);
                }
            }
        }
    }

    @Override
    public void destroy() {
        alive = false;
        super.destroy();
    }
}
