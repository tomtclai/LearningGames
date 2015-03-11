import java.util.LinkedList;

import Engine.BaseCode;
import dyehard.Configuration;
import dyehard.DyehardEndMenu;
import dyehard.DyehardMenuUI;
import dyehard.ManagerState;
import dyehard.UpdateManager;
import dyehard.UpdateManager.Updateable;
import dyehard.Background.DyehardUI;
import dyehard.Background.LogScreen;
import dyehard.Background.StartScreen;
import dyehard.Obstacles.Laser;
import dyehard.Player.Hero;
import dyehard.World.GameState;
import dyehard.World.GameWorldRegion;

public class GameWorld implements Updateable {
    public DyehardMenuUI menu;
    public DyehardEndMenu endMenu;
    public LogScreen start;
    public StartScreen startScreen;
    public Laser deathEdge;
    public DyehardUI ui;

    private static EnemyGenerator enemyGenerator;

    private static float factor = 1f;
    // private final float StartSpeed = 0.2f;
    public static final float LEFT_EDGE = BaseCode.world.getPositionX();
    public static final float RIGHT_EDGE = BaseCode.world.getWidth();
    public static final float TOP_EDGE = BaseCode.world.getHeight();
    public static final float BOTTOM_EDGE = BaseCode.world.getWorldPositionY();
    public static float Speed = Configuration.worldGameSpeed;

    float distance = 0f;
    protected Hero hero;
    private final LinkedList<GameWorldRegion> gameRegions;

    public GameWorld() {
        gameRegions = new LinkedList<GameWorldRegion>();
    }

    public void initialize(Hero hero) {
        enemyGenerator = new EnemyGenerator(hero);
        this.hero = hero;

        // Draw the hero on top of the background
        hero.drawOnTop();

        ui = new DyehardUI(hero);
        menu = new DyehardMenuUI();
        endMenu = new DyehardEndMenu();
        start = new LogScreen();
        startScreen = new StartScreen();

        deathEdge = new Laser(hero);

        // hero.alwaysOnTop();

        UpdateManager.register(this);

        addRegion(new Space(hero));
    }

    // Adds a region to the queue of upcoming regions
    // The region is initalized with the position of the last region currently
    // in the queue.
    public void addRegion(GameWorldRegion region) {
        float startLocation = 0f;
        if (!gameRegions.isEmpty()) {
            startLocation = gameRegions.getLast().rightEdge();
            System.out.println(startLocation);
        }
        region.initialize(startLocation);
        gameRegions.add(region);
    }

    // clear game regions and create new region. true for space false for
    // stargate.
    public void restartWorld(boolean space) {
        while (!gameRegions.isEmpty()) {
            gameRegions.pop().destroy();
            System.out.println("destroy");
        }
        enemyGenerator.clearEnemy();
        if (space) {
            addRegion(new Space(hero));
        } else {
            addRegion(new Stargate(hero));
        }
        System.gc();
        distance = 0f;
        GameState.DistanceTravelled = 0;
        GameState.Score = 0;
        hero.center = hero.getStart();
    }

    public boolean gameOver() {
        if (hero == null) {
            return false;
        }

        if (GameState.DistanceTravelled == GameState.TargetDistance) {
            return true;
        }

        return GameState.RemainingLives <= 0;
    }

    @Override
    public void update() {
        if (hero == null) {
            System.err.println("The GameWorld was not initialized!");
        }

        for (GameWorldRegion e : gameRegions) {
            if (factor > 1) {
                e.moveLeft(factor);
            } else {
                e.moveLeft();
            }
            if (e instanceof Stargate) {
                ((Stargate) e).blockHero();
                if ((e.leftEdge() > RIGHT_EDGE + 10f)
                        || (e.leftEdge() < LEFT_EDGE + 30f)) {
                    enemyGenerator.update();
                }
            } else if (gameRegions.size() == 1) {
                enemyGenerator.update();
            }
            distance += Speed;
            GameState.DistanceTravelled = (int) distance;
            if ((int) distance % 50 == 0) {
                GameState.Score = (int) distance;
            }
        }
        updateSequence();
    }

    @Override
    public ManagerState updateState() {
        return ManagerState.ACTIVE;
    }

    private void updateSequence() {
        // generate new game regions if the current one is about to end
        while (gameRegions.getLast().rightEdge() <= GameWorld.RIGHT_EDGE + 100f) {
            generateNewRegion();
        }

        // remove game regions that have moved off screen
        if (gameRegions.peek().rightEdge() <= GameWorld.LEFT_EDGE) {
            gameRegions.pop();
        }
    }

    // Generates regions that alternate between Stargate and Space
    protected void generateNewRegion() {
        GameWorldRegion newRegion;
        if (gameRegions.getLast() instanceof Space) {
            newRegion = new Stargate(hero);
        } else {
            newRegion = new Space(hero);
        }

        // the new region will start where the last region ends
        float startLocation = gameRegions.getLast().rightEdge();
        newRegion.initialize(startLocation);
        gameRegions.add(newRegion);
    }

    public boolean nextRegionIsSpace() {
        if (gameRegions.getLast().rightEdge() <= GameWorld.RIGHT_EDGE + 101f
                && gameRegions.getLast() instanceof Stargate) {
            return true;
        }
        return false;
    }

    public boolean nextRegionIsStargate() {
        if (gameRegions.getLast().rightEdge() <= GameWorld.RIGHT_EDGE + 101f
                && gameRegions.getLast() instanceof Space) {
            return true;
        }
        return false;
    }

    @Override
    public void setSpeed(float factor) {
        // TODO Auto-generated method stub
        GameWorld.factor = factor;
    }
}
