package dyehard.Player;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.DyehardRectangle;
import dyehard.Util.DyeHardSound;

public class HeroEffect {

    private final Hero hero;

    private final DyehardRectangle repelLeft;
    private final DyehardRectangle repelRight;
    private final DyehardRectangle invin;
    private final DyehardRectangle boost;

    public HeroEffect(Hero hero) {
        this.hero = hero;

        repelLeft = new DyehardRectangle();
        repelLeft.rotate = 180f;
        repelLeft.center = hero.center.clone().add(new Vector2(-6f, 0f));
        repelLeft.size = new Vector2(11.25f, 25f);
        repelLeft.texture = BaseCode.resources
                .loadImage("Textures/Hero/fx_repel_animSheet.png");
        repelLeft.setUsingSpriteSheet(true);
        repelLeft.setSpriteSheet(repelLeft.texture, 225, 500, 7, 2);
        repelLeft.visible = false;

        repelRight = new DyehardRectangle();
        repelRight.center = hero.center.clone().add(new Vector2(6f, 0f));
        repelRight.size = new Vector2(11.25f, 25f);
        repelRight.texture = BaseCode.resources
                .loadImage("Textures/Hero/fx_repel_animSheet.png");
        repelRight.setUsingSpriteSheet(true);
        repelRight.setSpriteSheet(repelRight.texture, 225, 500, 7, 2);
        repelRight.visible = false;

        invin = new DyehardRectangle();
        invin.center = hero.center.clone();
        invin.size = new Vector2(10f, 15f);
        invin.texture = BaseCode.resources
                .loadImage("Textures/Hero/fx_invincibility_AnimSheet.png");
        invin.setUsingSpriteSheet(true);
        invin.setSpriteSheet(invin.texture, 200, 300, 10, 2);
        invin.visible = false;

        boost = new DyehardRectangle();
        boost.center = hero.center.clone().add(
                new Vector2(hero.size.getX() * -.59f, hero.size.getY() * .11f));
        boost.rotate = 0f;
        boost.size = new Vector2(2f, 2f);
        boost.texture = BaseCode.resources
                .loadImage("Textures/Hero/Dye_boost_AnimSheet.png");
        boost.setUsingSpriteSheet(true);
        boost.setSpriteSheet(boost.texture, 40, 40, 8, 2);
        boost.visible = true;
    }

    public void update() {
        if (hero.isFiring) {
            switch (hero.directionState) {
            case UP:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * -.35f, hero.size.getY()
                                * -.2f));
                boost.rotate = 90f;
                boost.visible = true;
                break;
            case UPFORWARD:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * -.2f, 0f));
                boost.rotate = 35f;
                boost.visible = true;
                break;
            default:
                boost.visible = false;
                break;
            }
        } else {
            switch (hero.directionState) {
            case NEUTRAL:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * -.59f,
                                hero.size.getY() * .11f));
                boost.rotate = 0f;
                boost.visible = true;
                break;
            case UP:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * -.33f, hero.size.getY()
                                * -.23f));
                boost.rotate = 90f;
                boost.visible = true;
                break;
            case DOWN:
                boost.visible = false;
                break;
            case BACK:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * .59f,
                                hero.size.getY() * .13f));
                boost.rotate = 180f;
                boost.visible = true;
                break;
            case FORWARD:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * -.35f,
                                hero.size.getY() * .08f));
                boost.rotate = 0f;
                boost.visible = true;
                break;
            case UPFORWARD:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * -.4f, 0f));
                boost.rotate = 35f;
                boost.visible = true;
                break;
            case UPBACK:
                boost.center = hero.center.clone().add(
                        new Vector2(hero.size.getX() * .13f, hero.size.getY()
                                * -.13f));
                boost.rotate = 180f;
                boost.visible = true;
                break;
            case DOWNFORWARD:
                boost.visible = false;
                break;
            case DOWNBACK:
                boost.visible = false;
                break;
            }
        }

        if (hero.isInvin) {
            if (!invin.visible) {
                DyeHardSound.playLoop(DyeHardSound.shieldSound);
                invin.visible = true;
            }
            invin.center = hero.center.clone();
        } else {
            if (invin.visible) {
                DyeHardSound.stopSound(DyeHardSound.shieldSound);
                invin.visible = false;
            }
        }

        if (hero.isRepel) {
            if (!repelRight.visible) {
                repelLeft.visible = true;
                repelRight.visible = true;
            }
            repelLeft.center = hero.center.clone().add(new Vector2(-6f, 0f));
            repelRight.center = hero.center.clone().add(new Vector2(6f, 0f));
        } else {
            if (repelRight.visible) {
                repelLeft.visible = false;
                repelRight.visible = false;
            }
        }
    }

    public void startFlashing() {
        boost.startFlashing();
        if (hero.isRepel) {
            repelLeft.startFlashing();
            repelRight.startFlashing();
        }
        if (hero.isInvin) {
            invin.startFlashing();
        }
    }

    public void stopFlashing() {
        boost.stopFlashing();
        repelLeft.stopFlashing();
        repelRight.stopFlashing();
        invin.stopFlashing();
    }

    public void drawOnTop() {
        invin.removeFromAutoDrawSet();
        invin.addToAutoDrawSet();
        repelLeft.removeFromAutoDrawSet();
        repelLeft.addToAutoDrawSet();
        repelRight.removeFromAutoDrawSet();
        repelRight.addToAutoDrawSet();
        boost.removeFromAutoDrawSet();
        boost.addToAutoDrawSet();
    }

}
