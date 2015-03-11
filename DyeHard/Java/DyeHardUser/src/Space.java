import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.Configuration;
import dyehard.Collectibles.DyePack;
import dyehard.Collectibles.Ghost;
import dyehard.Collectibles.Invincibility;
import dyehard.Collectibles.Magnetism;
import dyehard.Collectibles.Overload;
import dyehard.Collectibles.PowerUp;
import dyehard.Collectibles.Repel;
import dyehard.Collectibles.SlowDown;
import dyehard.Collectibles.SpeedUp;
import dyehard.Collectibles.Unarmed;
import dyehard.Obstacles.Debris;
import dyehard.Player.Hero;
import dyehard.Util.Colors;
import dyehard.World.GameWorldRegion;

public class Space extends GameWorldRegion {
    public static float WIDTH = BaseCode.world.getWidth() * 3f;

    private static int powerUpCount = Configuration.worldPowerUpCount;
    private static int dyePackCount = Configuration.worldDyePackCount;
    private static int debrisCount = Configuration.worldDebrisCount;

    private static Random RANDOM = new Random();

    // The list of powerups that can be randomly generated
    private static List<PowerUp> powerUpTypes;
    private static List<PowerUp> userPowerUps;
    private List<PowerUp> powerUpList;

    // The list of dyes that can be randomly generated
    private List<DyePack> dyeList;
    private static List<DyePack> userDyePacks;

    // The list of debris that can be randomly generated
    private List<Debris> debrisList;

    public Space(Hero hero) {
        width = WIDTH;
        speed = -Configuration.worldGameSpeed;
    }

    static {
        powerUpTypes = new ArrayList<PowerUp>();
        userPowerUps = new ArrayList<PowerUp>();
        userDyePacks = new ArrayList<DyePack>();

        powerUpTypes.add(new Ghost());
        powerUpTypes.add(new Invincibility());
        powerUpTypes.add(new Magnetism());
        powerUpTypes.add(new Overload());
        powerUpTypes.add(new SlowDown());
        powerUpTypes.add(new SpeedUp());
        powerUpTypes.add(new Unarmed());
        // powerUpTypes.add(new Gravity());
        powerUpTypes.add(new Repel());
    }

    @Override
    public void initialize(float leftEdge) {
        debrisList = new ArrayList<Debris>();
        position = leftEdge + width * 0.5f;
        generateCollectibles(leftEdge);
    }

    private void generateCollectibles(float leftEdge) {
        generateDyePacks();
        initializeDyePacks(dyeList);

        generatePowerUps(powerUpTypes, powerUpCount);
        initializePowerUps(powerUpList);

        // offset the region to pad the space before the next element
        // this makes the region slightly smaller than it actually should be
        // otherwise
        int offset = 1;
        float region = (rightEdge() - leftEdge()) / (debrisCount + offset);
        for (int i = 0; i < debrisCount; i++) {
            float regionLeft = leftEdge + (i * region);
            float regionRight = regionLeft + region;
            debrisList.add(new Debris(regionLeft, regionRight));
        }
    }

    private void generatePowerUps(List<PowerUp> powerUpTypes, int count) {
        powerUpList = new ArrayList<PowerUp>();
        if (powerUpTypes == null || powerUpTypes.size() == 0) {
            return;
        }

        for (int i = 0; i < count; i++) {
            PowerUp randomPowerUp = powerUpTypes.get(RANDOM
                    .nextInt(powerUpTypes.size()));

            PowerUp generatedPowerUp = randomPowerUp.clone();
            powerUpList.add(generatedPowerUp);
        }
    }

    private void initializePowerUps(List<PowerUp> powerups) {
        assert powerups != null;

        // Powerups are distributed within uniformly distributed regions
        float regionWidth = width / powerups.size();
        float regionStart = leftEdge();
        float regionHeight = BaseCode.world.getHeight()
                - BaseCode.world.getWorldPositionY();
        float posX, posY;

        for (int i = 0; i < powerups.size(); i++) {
            posX = regionStart + (i * regionWidth);
            posX += RANDOM.nextFloat() * regionWidth;

            posY = (regionHeight - PowerUp.height) * RANDOM.nextFloat()
                    + PowerUp.height / 2f;

            Vector2 position = new Vector2(posX, posY);

            powerups.get(i).initialize(position);
        }

        for (int i = 0; i < userPowerUps.size(); i++) {
            Vector2 pos = userPowerUps.get(i).center.clone();
            pos.offset(leftEdge(), 0f);

            PowerUp p = userPowerUps.get(i).clone();

            p.initialize(pos);
        }
    }

    private void initializeDyePacks(List<DyePack> dyes) {
        assert dyes != null;

        // Dyepacks are distributed within uniformly distributed regions
        float regionWidth = width / dyeList.size();
        float regionStart = leftEdge();
        float regionHeight = BaseCode.world.getHeight()
                - BaseCode.world.getWorldPositionY();

        float posX, posY;

        for (int i = 0; i < dyes.size(); i++) {
            posX = regionStart + (i * regionWidth);
            posX += RANDOM.nextFloat() * regionWidth;

            posY = (regionHeight - DyePack.height) * RANDOM.nextFloat()
                    + DyePack.height / 2f;

            Vector2 position = new Vector2(posX, posY);

            dyes.get(i).initialize(position);
        }

        for (int i = 0; i < userDyePacks.size(); i++) {
            Vector2 pos = userDyePacks.get(i).center.clone();
            pos.offset(leftEdge(), 0f);

            DyePack d = new DyePack(userDyePacks.get(i).color);

            d.initialize(pos);
        }
    }

    public void generateDyePacks() {
        dyeList = new ArrayList<DyePack>();

        for (int i = 0; i < dyePackCount; ++i) {
            Color randomColor = Colors.randomColor();
            DyePack dye = new DyePack(randomColor);
            dyeList.add(dye);
        }
    }

    public static void numberOfDefaultPowerUps(int count) {
        if (count >= 0) {
            powerUpCount = count;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void registerPowerUp(PowerUp p) {
        PowerUp userPowerUp = p.clone();
        userPowerUp.center.set(p.center.clone());

        userPowerUps.add(userPowerUp);

        // Immediately places the user's power up
        userPowerUp.clone().initialize(p.center.clone());
    }

    public static void numberOfDefaultDyePacks(int count) {
        if (count >= 0) {
            dyePackCount = count;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void registerDyePack(DyePack d) {
        DyePack userDyePack = new DyePack(d.color);
        userDyePack.center.set(d.center.clone());

        userDyePacks.add(userDyePack);

        DyePack initialPack = new DyePack(d.color);

        // Immediately places the user's dye pack
        initialPack.initialize(d.center.clone());
    }

    public static void numberOfDefaultDebris(int count) {
        if (count >= 0) {
            debrisCount = count;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void destroy() {
        for (DyePack d : dyeList) {
            d.destroy();
        }
        for (PowerUp p : powerUpList) {
            p.destroy();
        }
        for (Debris deb : debrisList) {
            deb.destroy();
        }
    }
}
