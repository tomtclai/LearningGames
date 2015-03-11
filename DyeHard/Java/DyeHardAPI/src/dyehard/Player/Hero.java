package dyehard.Player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import Engine.BaseCode;
import Engine.Primitive;
import Engine.Vector2;
import Engine.World.BoundCollidedStatus;
import dyehard.Actor;
import dyehard.Collidable;
import dyehard.Configuration;
import dyehard.DyeHard;
import dyehard.Collectibles.DyePack;
import dyehard.Collectibles.Invincibility;
import dyehard.Collectibles.PowerUp;
import dyehard.Player.HeroInterfaces.HeroCollision;
import dyehard.Player.HeroInterfaces.HeroDamage;
import dyehard.Util.Colors;
import dyehard.Util.DyeHardSound;
import dyehard.Util.ImageTint;
import dyehard.Util.Timer;
import dyehard.Weapons.OverHeatWeapon;
import dyehard.Weapons.Weapon;
import dyehard.World.GameState;
import dyehard.World.WormHole.DeathGate;

public class Hero extends Actor implements HeroCollision, HeroDamage {
    public static HashMap<Color, BufferedImage> chargerIdleTextures = new HashMap<Color, BufferedImage>();
    public static HashMap<Color, BufferedImage> chargerAttackTextures = new HashMap<Color, BufferedImage>();
    public static HashMap<Color, BufferedImage> regularLeftTextures = new HashMap<Color, BufferedImage>();
    public static HashMap<Color, BufferedImage> regularRightTextures = new HashMap<Color, BufferedImage>();
    public static HashMap<Color, BufferedImage> portalEnemyTextures = new HashMap<Color, BufferedImage>();
    private static HashMap<Direction, BufferedImage> dyeTextures = new HashMap<Direction, BufferedImage>();
    private static HashMap<Direction, BufferedImage> dyeFireTextures = new HashMap<Direction, BufferedImage>();

    private final Timer timer = new Timer(7000);
    public boolean noMoreTeleport = false;
    public boolean collisionOn = true;
    public boolean damageOn = true;
    public Weapon currentWeapon;
    public float currentJetSpeed;
    public Vector2 currentGravity;
    public Set<PowerUp> powerups;
    public CurPowerUp curPowerUp;
    public boolean debugInvincibility;
    public boolean isInvin;
    public boolean isRepel;
    public boolean isFiring;
    private boolean flashing;

    public final Weapon defaultWeapon = new OverHeatWeapon(this);
    public final float defaultJetSpeed = Configuration.heroJetSpeed;
    public final Vector2 defaultGravity = new Vector2(0f, 0f);
    public Vector2 totalThrust = new Vector2();
    public HashMap<Color, BufferedImage> bulletTextures = new HashMap<Color, BufferedImage>();
    public HashMap<Color, BufferedImage> muzzleTextures = new HashMap<Color, BufferedImage>();

    private final float speedLimitX = Configuration.heroSpeedLimit;
    private static float drag = Configuration.heroDrag;

    private int collectedDyepacks;
    private int collectedPowerups;
    private final float sizeScale;

    private final float maxHeroSpeed = Configuration.heroSpeedLimit;
    private final float heroSpeedRatio = Configuration.heroJetSpeed;

    protected Direction directionState;
    protected DynamicDyePack dynamicDyepack;
    protected HeroEffect heroEffect;
    protected Vector2 previousVelocity;
    protected Vector2 currentVelocity;
    protected final static Vector2 startingLocation = new Vector2(20f, 20f);

    private final ArrayList<Weapon> weaponRack;
    private final HashMap<Integer, Integer> weaponHotkeys;

    public enum Direction {
        UP, DOWN, BACK, FORWARD, UPFORWARD, UPBACK, DOWNFORWARD, DOWNBACK, NEUTRAL
    }

    public enum CurPowerUp {
        GHOST, INVIN, MAGNET, OVERLOAD, SLOW, SPEED, UNARMED, GRAVITY, REPEL, NONE
    }

    static {
        BufferedImage idle = BaseCode.resources
                .loadImage("Textures/Enemies/Charger_AnimSheet_Idle.png");
        BufferedImage attack = BaseCode.resources
                .loadImage("Textures/Enemies/Charger_AnimSheet_Attack.png");
        BufferedImage regularLeft = BaseCode.resources
                .loadImage("Textures/Enemies/Regular_AnimSheet_Left.png");
        BufferedImage regularRight = BaseCode.resources
                .loadImage("Textures/Enemies/Regular_AnimSheet_Right.png");
        BufferedImage portalEnemy = BaseCode.resources
                .loadImage("Textures/Enemies/PortalMinion_AnimSheet_Left.png");

        // Fill the hashmap with tinted images for later use
        for (int i = 0; i < 6; i++) {
            Color temp = Colors.colorPicker(i);
            float alpha = 0.45f;
            if (temp == Colors.Blue) {
                alpha = 0.75f;
            }
            chargerIdleTextures.put(temp,
                    ImageTint.tintedImage(idle, temp, alpha));
            chargerAttackTextures.put(temp,
                    ImageTint.tintedImage(attack, temp, alpha));
            regularLeftTextures.put(temp,
                    ImageTint.tintedImage(regularLeft, temp, alpha));
            regularRightTextures.put(temp,
                    ImageTint.tintedImage(regularRight, temp, alpha));
            portalEnemyTextures.put(temp,
                    ImageTint.tintedImage(portalEnemy, temp, alpha));
        }
        chargerIdleTextures.put(Color.gray, idle);
        chargerAttackTextures.put(Color.gray, attack);
        regularLeftTextures.put(Color.gray, regularLeft);
        regularRightTextures.put(Color.gray, regularRight);
        portalEnemyTextures.put(Color.gray, portalEnemy);

        for (Direction dir : Direction.values()) {
            dyeTextures.put(
                    dir,
                    BaseCode.resources.loadImage("Textures/Hero/Dye_"
                            + dir.toString() + ".png"));
            dyeFireTextures.put(
                    dir,
                    BaseCode.resources.loadImage("Textures/Hero/Dye_"
                            + dir.toString() + "_Fire.png"));
        }
    }

    public Hero() {
        super(startingLocation.clone(), Configuration.heroWidth,
                Configuration.heroHeight); // TODO remove magic numbers

        sizeScale = size.getY() / 9f;

        curPowerUp = CurPowerUp.NONE;
        color = Colors.randomColor();
        if (!bulletTextures.containsKey(color)) {
            bulletTextures.put(color,
                    ImageTint.tintedImage(BaseCode.resources
                            .loadImage("Textures/Dye_attack_projectile.png"),
                            color, 1f));
            muzzleTextures
                    .put(color,
                            ImageTint.tintedImage(
                                    BaseCode.resources
                                            .loadImage("Textures/Dye_attack_muzzle_flash_AnimSheet.png"),
                                    color, 1f));
        }
        directionState = Direction.NEUTRAL;
        dynamicDyepack = new DynamicDyePack(this);
        heroEffect = new HeroEffect(this);
        texture = BaseCode.resources.loadImage("Textures/Hero/Dye_NEUTRAL.png");

        collectedDyepacks = 0;
        collectedPowerups = 0;

        powerups = new TreeSet<PowerUp>();

        currentJetSpeed = defaultJetSpeed;
        currentGravity = defaultGravity;

        weaponRack = new ArrayList<Weapon>();
        weaponRack.add(defaultWeapon); // add default weapon
        currentWeapon = defaultWeapon;

        // Maps number keys to weaponRack index
        weaponHotkeys = new HashMap<Integer, Integer>();
        weaponHotkeys.put(KeyEvent.VK_1, 0);
        weaponHotkeys.put(KeyEvent.VK_2, 1);
        weaponHotkeys.put(KeyEvent.VK_3, 2);
        weaponHotkeys.put(KeyEvent.VK_4, 3);

        previousVelocity = new Vector2(0f, 0f);
        currentVelocity = new Vector2(0f, 0f);

        isInvin = false;
        isRepel = false;
        isFiring = false;
    }

    public void updateMovement() {
        // Clamp the horizontal speed to speedLimit
        float velX = velocity.getX();
        velX = Math.min(speedLimitX, velX);
        velX = Math.max(-speedLimitX, velX);
        velocity.setX(velX);

        velocity.add(currentGravity);
        velocity.mult(drag);

        // Scale the velocity to the frame rate
        Vector2 frameVelocity = velocity.clone();
        // frameVelocity.mult(DyeHard.DELTA_TIME);
        center.add(frameVelocity);
    }

    @Override
    public void update() {
        super.update();
        if (noMoreTeleport) {
            if (timer.isDone()) {
                noMoreTeleport = false;
            }
        }
        applyPowerups();

        // handleInput();
        // updateDirectionState();
        // updateMovement();
        // selectWeapon();
        clampToWorldBounds();
        dynamicDyepack.update();
        heroEffect.update();
    }

    private void applyPowerups() {
        Set<PowerUp> destroyed = new TreeSet<PowerUp>();
        for (PowerUp p : powerups) {
            if (p.isDone()) {
                p.unapply(this);
                destroyed.add(p);
            }
        }

        powerups.removeAll(destroyed);

        for (PowerUp p : powerups) {
            p.apply(this);
        }
    }

    private void clampToWorldBounds() {
        // restrict the hero's movement to the boundary
        BoundCollidedStatus collisionStatus = collideWorldBound();
        if (collisionStatus != BoundCollidedStatus.INSIDEBOUND) {
            if (collisionStatus == BoundCollidedStatus.LEFT
                    || collisionStatus == BoundCollidedStatus.RIGHT) {
                velocity.setX(0);
                acceleration.setX(0);
            } else if (collisionStatus == BoundCollidedStatus.TOP
                    || collisionStatus == BoundCollidedStatus.BOTTOM) {
                velocity.setY(0);
                acceleration.setY(0);
            }
        }

        BaseCode.world.clampAtWorldBound(this);
    }

    public void moveUp() {
        // Upward speed needs to counter the effects of gravity
        totalThrust
                .add(new Vector2(0f, defaultJetSpeed - currentGravity.getY()));
    }

    public void moveDown() {
        totalThrust.add(new Vector2(0f, -defaultJetSpeed));
    }

    public void moveLeft() {
        totalThrust.add(new Vector2(-defaultJetSpeed, 0f));
    }

    public void moveRight() {
        totalThrust.add(new Vector2(defaultJetSpeed, 0));
    }

    public void moveTo(float x, float y) {
        if ((DyeHard.getState() == DyeHard.State.PLAYING) && !flashing) {
            float xOffset = x - center.getX();
            float yOffset = y - center.getY();

            float theta = (float) (180.0 / Math.PI * Math.atan2(xOffset,
                    yOffset));

            if (Math.abs(xOffset) + Math.abs(yOffset) < 0.2f) {
                directionState = Direction.NEUTRAL;
            } else if (theta > 0) {
                if (theta < 22.5) {
                    directionState = Direction.UP;
                } else if (theta < 67.5) {
                    directionState = Direction.UPFORWARD;
                } else if (theta < 112.5) {
                    directionState = Direction.FORWARD;
                } else if (theta < 157.5) {
                    directionState = Direction.DOWNFORWARD;
                } else {
                    directionState = Direction.DOWN;
                }
            } else {
                if (theta > -22.5) {
                    directionState = Direction.UP;
                } else if (theta > -67.5) {
                    directionState = Direction.UPBACK;
                } else if (theta > -112.5) {
                    directionState = Direction.BACK;
                } else if (theta > -157.5) {
                    directionState = Direction.DOWNBACK;
                } else {
                    directionState = Direction.DOWN;
                }
            }

            setTexture();

            if (xOffset > 0) {
                center.setX(center.getX()
                        + Math.min((xOffset * heroSpeedRatio), maxHeroSpeed));
            } else if (xOffset < 0) {
                center.setX(center.getX()
                        + Math.max((xOffset * heroSpeedRatio), -maxHeroSpeed));
            }

            if (yOffset > 0) {
                center.setY(center.getY()
                        + Math.min((yOffset * heroSpeedRatio), maxHeroSpeed));
            } else if (yOffset < 0) {
                center.setY(center.getY()
                        + Math.max((yOffset * heroSpeedRatio), -maxHeroSpeed));
            }
        }
    }

    private void setTexture() {
        if (isFiring) {
            switch (directionState) {
            case NEUTRAL:
                size.set(new Vector2(3.35f * sizeScale, 8.4f * sizeScale));
                break;
            case UP:
                size.set(new Vector2(5.2f * sizeScale, 6.9f * sizeScale));
                break;
            case DOWN:
                size.set(new Vector2(3.4f * sizeScale, 5.8f * sizeScale));
                break;
            case BACK:
                size.set(new Vector2(3.35f * sizeScale, 6.2f * sizeScale));
                break;
            case FORWARD:
                size.set(new Vector2(6.95f * sizeScale, 5.2f * sizeScale));
                break;
            case UPFORWARD:
                size.set(new Vector2(5.55f * sizeScale, 6.35f * sizeScale));
                break;
            case UPBACK:
                size.set(new Vector2(3.9f * sizeScale, 6.4f * sizeScale));
                break;
            case DOWNFORWARD:
                size.set(new Vector2(5.25f * sizeScale, 5.95f * sizeScale));
                break;
            case DOWNBACK:
                size.set(new Vector2(4.1f * sizeScale, 5.4f * sizeScale));
                break;
            }
            texture = dyeFireTextures.get(directionState);
        } else {
            switch (directionState) {
            case NEUTRAL:
                size.set(new Vector2(Configuration.heroWidth,
                        Configuration.heroHeight));
                break;
            case UP:
                size.set(new Vector2(4f * sizeScale, 7.25f * sizeScale));
                break;
            case DOWN:
                size.set(new Vector2(3.55f * sizeScale, 6.05f * sizeScale));
                break;
            case BACK:
                size.set(new Vector2(3.9f * sizeScale, 5.2f * sizeScale));
                break;
            case FORWARD:
                size.set(new Vector2(6.25f * sizeScale, 5.75f * sizeScale));
                break;
            case UPFORWARD:
                size.set(new Vector2(6.15f * sizeScale, 6.7f * sizeScale));
                break;
            case UPBACK:
                size.set(new Vector2(5.5f * sizeScale, 6.85f * sizeScale));
                break;
            case DOWNFORWARD:
                size.set(new Vector2(6f * sizeScale, 4.5f * sizeScale));
                break;
            case DOWNBACK:
                size.set(new Vector2(4.9f * sizeScale, 5.6f * sizeScale));
                break;
            }
            texture = dyeTextures.get(directionState);
        }
    }

    public void fire() {
        if (!flashing) {
            currentWeapon.fire();
        }
    }

    private void handleInput() {
        velocity.add(totalThrust);
        totalThrust.set(0f, 0f);
    }

    // public void updateDirectionState() {
    // previousVelocity = currentVelocity.clone();
    // currentVelocity = velocity.clone();
    // Vector2 tempVelocity = currentVelocity.clone().sub(
    // previousVelocity.clone());
    //
    // if (tempVelocity.getY() > 1f && tempVelocity.getX() < -1f) {
    // directionState = Direction.TOPLEFT;
    // } else if (tempVelocity.getY() > 1f && tempVelocity.getX() > 1f) {
    // directionState = Direction.TOPRIGHT;
    // } else if (tempVelocity.getY() < -1f && tempVelocity.getX() < -1f) {
    // directionState = Direction.BOTTOMLEFT;
    // } else if (tempVelocity.getY() < -1f && tempVelocity.getX() > 1f) {
    // directionState = Direction.BOTTOMRIGHT;
    // } else if (tempVelocity.getY() > 1f) {
    // directionState = Direction.UP;
    // } else if (tempVelocity.getY() < -1f) {
    // directionState = Direction.DOWN;
    // } else if (tempVelocity.getX() < -1f) {
    // directionState = Direction.LEFT;
    // } else if (tempVelocity.getX() > 1f) {
    // directionState = Direction.RIGHT;
    // } else {
    // directionState = Direction.NEUTRAL;
    // }
    // }

    // Select a weapon in the weapon rack based on the input
    // private void selectWeapon() {
    // for (int hotkey : weaponHotkeys.keySet()) {
    // if (DyehardKeyboard.isKeyDown(hotkey)) {
    // int weaponIndex = weaponHotkeys.get(hotkey);
    // if (weaponIndex < weaponRack.size() && weaponIndex >= 0) {
    // currentWeapon = weaponRack.get(weaponIndex);
    // }
    // }
    // }
    // }

    public void alwaysOnTop() {
        alwaysOnTop = true;
        dynamicDyepack.alwaysOnTop = true;
    }

    public void drawOnTop() {
        removeFromAutoDrawSet();
        addToAutoDrawSet();
        dynamicDyepack.removeFromAutoDrawSet();
        dynamicDyepack.addToAutoDrawSet();
        heroEffect.drawOnTop();
    }

    public void collect(DyePack dye) {
        dye.activate(this);
        collectedDyepacks += 1;
    }

    public void collect(PowerUp powerup) {
        // Only one powerup can be active at a time
        for (PowerUp p : powerups) {
            if (!(p instanceof Invincibility)) {
                p.unapply(this);
            }
        }
        powerups.clear();

        powerups.add(powerup);
        powerup.activate(this);
        collectedPowerups += 1;
    }

    public void registerWeapon(Weapon weapon) {
        weaponRack.add(weapon);
    }

    public int dyepacksCollected() {
        return collectedDyepacks;
    }

    // Powerups Functions
    public int powerupsCollected() {
        return collectedPowerups;
    }

    public Direction getDirection() {
        return directionState;
    }

    @Override
    public void kill(Primitive who) {
        if ((damageOn)
                && (who.color != color)
                || ((who instanceof DeathGate) && curPowerUp == CurPowerUp.GHOST)) {

            damageHero(this, who);
        }
    }

    @Override
    public void damageHero(Hero hero, Primitive who) {
        // Only one powerup can be active at a time
        for (PowerUp p : powerups) {
            p.unapply(this);
        }
        powerups.clear();

        if (!debugInvincibility) {
            GameState.RemainingLives--;
        }

        if (GameState.RemainingLives <= 0) {
            alive = false;
            DyeHardSound.play(DyeHardSound.loseSound);
        } else {
            powerups.add(new Invincibility());
            applyPowerups();
            DyeHardSound.play(DyeHardSound.lifeLostSound);
            hero.center.set(startingLocation.clone());
        }
    }

    @Override
    public void handleCollision(Collidable other) {
        if (collisionOn) {
            super.handleCollision(other);
        }
    }

    @Override
    public void collideWithHero(Hero hero, Collidable other) {
        super.handleCollision(other);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        if (!bulletTextures.containsKey(color)) {
            bulletTextures.put(color,
                    ImageTint.tintedImage(BaseCode.resources
                            .loadImage("Textures/Dye_attack_projectile.png"),
                            color, 1f));
            muzzleTextures
                    .put(color,
                            ImageTint.tintedImage(
                                    BaseCode.resources
                                            .loadImage("Textures/Dye_attack_muzzle_flash_AnimSheet.png"),
                                    color, 1f));
        }
    }

    @Override
    public void destroy() {
        return;
    }

    public Vector2 getStart() {
        return startingLocation.clone();
    }

    @Override
    public void startFlashing() {
        super.startFlashing();
        heroEffect.startFlashing();
        dynamicDyepack.startFlashing();
        flashing = true;
        damageOn = false;
        noMoreTeleport = true;
        timer.reset();
    }

    @Override
    public void stopFlashing() {
        super.stopFlashing();
        heroEffect.stopFlashing();
        dynamicDyepack.stopFlashing();
        flashing = false;
        damageOn = true;
    }
}
