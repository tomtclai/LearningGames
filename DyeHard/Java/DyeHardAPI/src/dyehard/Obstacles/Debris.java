package dyehard.Obstacles;

import java.util.Random;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.Configuration;

public class Debris extends Obstacle {
    private static Random RANDOM = new Random();
    private final float width = Configuration.debrisWidth;
    private final float height = Configuration.debrisHeight;

    public Debris(float minX, float maxX) {
        float randomX = (maxX - minX - width) * RANDOM.nextFloat() + minX
                + width / 2f;
        float randomY = (BaseCode.world.getHeight()
                - BaseCode.world.getWorldPositionY() - height)
                * RANDOM.nextFloat() + height / 2f;

        center.set(new Vector2(randomX, randomY));
        size.set(width, height);

        float speed = Configuration.debrisSpeed;
        velocity = new Vector2(-(RANDOM.nextFloat() * speed / 2 + speed), 0f);
        shouldTravel = true;

        initializeRandomTexture();
    }

    private void initializeRandomTexture() {
        switch (RANDOM.nextInt(3)) {
        case 0:
            texture = BaseCode.resources
                    .loadImage("Textures/Debris/debris_01.png");
            break;
        case 1:
            texture = BaseCode.resources
                    .loadImage("Textures/Debris/debris_02.png");
            break;
        case 2:
            texture = BaseCode.resources
                    .loadImage("Textures/Debris/debris_03.png");
            size.setX(size.getY() * 1.28f);
            break;
        }
    }
}
