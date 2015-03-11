package dyehard.Background;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.GameObject;
import dyehard.UpdateObject;

public class Background extends UpdateObject {
    private final Deque<Tile> ship;
    private final List<BufferedImage> shipTextures;
    private final String[] shipTexturePaths = {
            "Textures/Background/Dyehard_ship_tile_01.png",
            "Textures/Background/Dyehard_ship_tile_02.png",
            "Textures/Background/Dyehard_ship_tile_03.png",
            "Textures/Background/Dyehard_ship_tile_04.png", };

    private final Deque<Tile> background;
    private final List<BufferedImage> backgroundTextures;
    private final String[] backgroundTexturePaths = {
            "Textures/Background/Dyehard_starfield_01.png",
            "Textures/Background/Dyehard_starfield_02.png", };

    private final Deque<Tile> foreground;
    private final List<BufferedImage> foregroundTextures;
    private final String[] foregroundTexturePaths = {
            "Textures/Background/Dyehard_starfield_stars.png",
            "Textures/Background/Dyehard_starfield_stars.png", };

    private static Random RANDOM = new Random();

    public Background() {
        backgroundTextures = loadTextures(backgroundTexturePaths);
        background = createTiles(backgroundTextures, -0.00390625f); // 1/256

        foregroundTextures = loadTextures(foregroundTexturePaths);
        foreground = createTiles(foregroundTextures, -0.0078125f); // 1/128

        shipTextures = loadTextures(shipTexturePaths);
        ship = createTiles(shipTextures, -0.03125f); // 1/32
    }

    @Override
    public void update() {
        updateTileQueue(ship);
        updateTileQueue(background);
        updateTileQueue(foreground);
    }

    private void updateTileQueue(Deque<Tile> tiles) {
        if (tiles.peek().isOffScreen()) {
            Tile first = tiles.poll();
            Tile last = tiles.peekLast();
            first.setLeftEdgeAt(last.rightEdge() - 5f);

            tiles.add(first);
        }
    }

    private List<BufferedImage> loadTextures(String[] paths) {
        List<BufferedImage> textures = new ArrayList<BufferedImage>();
        for (String path : paths) {
            BufferedImage texture = BaseCode.resources.loadImage(path);
            textures.add(texture);
        }

        return textures;
    }

    private Deque<Tile> createTiles(List<BufferedImage> textures, float speed) {
        // Collections.shuffle(textures);

        float randomStart = RANDOM.nextFloat() * Tile.width;
        Deque<Tile> tiles = new ArrayDeque<Tile>();
        for (int i = 0; i < textures.size(); ++i) {
            Tile tile = new Tile(i * Tile.width - randomStart, speed);
            tile.texture = textures.get(i);
            tiles.add(tile);
        }
        return tiles;
    }

    private class Tile extends GameObject {
        public static final float width = 106;
        public static final float height = 60f;

        public Tile(float leftEdge, float speed) {
            float Xpos = leftEdge + (width * 0.5f);
            float Ypos = height / 2;
            Vector2 position = new Vector2(Xpos, Ypos);

            velocity = new Vector2(speed, 0f);
            center = position;

            // slightly stretch the graphic to cover the gap where the tiles
            // overlap
            size = new Vector2(width + 0.5f, height);
            color = Color.PINK;
            shouldTravel = true;
        }

        public void setLeftEdgeAt(float leftEdge) {
            center.setX(leftEdge + size.getX() / 2f);
        }

        public float rightEdge() {
            return center.getX() + size.getX() / 2f;
        }

        public boolean isOffScreen() {
            return rightEdge() < BaseCode.world.getPositionX();
        }
    }

    @Override
    public void setSpeed(float v) {
        // TODO Auto-generated method stub
    }
}