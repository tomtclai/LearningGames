package dyehard;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Engine.BaseCode;
import Engine.Primitive;
import Engine.Vector2;

// Sprite sheet code ported from the C# engine, which
// is credited to Samuel Cook and Ron Cook for adding support for that.
public class DyehardRectangle extends Primitive {
    public boolean spriteCycleDone = false;

    public boolean reverse = false;
    public boolean overRide = false;
    public boolean stopAtEnd = false;
    private boolean drawImage = true;
    private boolean drawFilledRect = true;
    private boolean flash = false;
    private int flashCount = 0;
    private final int flashRate = 10;

    public enum SpriteSheetAnimationMode {
        ANIMATE_FORWARD, ANIMATE_BACKWARD, ANIMATE_SWING
    };

    private boolean usingSpriteSheet = false;
    private boolean panning = false;
    private boolean vertical = false;
    /**
     * Sprite tolerance is for letting spritesheets with odd widths/heights to
     * include outermost sprites. This is because the spritemapping algorithm
     * does not include any part of a spritesheet that is bigger than the sum of
     * the sprites dimension (width or height).
     */

    /**
     * frameCoords holds the lower left and upper right coordinates of each
     * frame of the sprite sheet. frameCoords[][0] = lower left x
     * frameCoords[][1] = lower left y frameCoords[][2] = upper right x
     * frameCoords[][3] = upper right y
     * 
     */
    protected int[][] frameCoords;
    protected int currentFrame;
    protected int totalFrames;
    protected int frameWidth, frameHeight;
    private int extra;

    protected int ticksPerFrame;
    protected int currentTick;

    public DyehardRectangle() {
        super();
    }

    public DyehardRectangle(DyehardRectangle other) {
        super(other);

        drawImage = other.drawImage;
        drawFilledRect = other.drawFilledRect;
    }

    public boolean usingSpriteSheet() {
        return usingSpriteSheet;
    }

    public void setUsingSpriteSheet(boolean usingSpriteSheet) {
        this.usingSpriteSheet = usingSpriteSheet;
    }

    public boolean panning() {
        return panning;
    }

    public void setPanning(boolean panning) {
        this.panning = panning;
    }

    /**
     * Sets up the spritesheet animation.
     * 
     * Will do nothing if width, height, or totalFrame are <= 0.
     * 
     * @param textureFilename
     *            Filename of the spritesheet image
     * @param width
     *            Width of individual sprite.
     * @param height
     *            Height of individual sprite.
     * @param totalFrames
     *            Total sprites in spritesheet
     * @param ticksPerFrame
     *            How long each sprite remains before the next is drawn.
     */
    public void setSpriteSheet(String textureFilename, int width, int height,
            int totalFrames, int ticksPerFrame) {
        if (totalFrames <= 0 || width <= 0 || height <= 0) {
            return;
        }
        setImage(textureFilename);

        frameCoords = new int[totalFrames][4];
        this.totalFrames = totalFrames;
        currentFrame = 0;

        currentTick = 0;
        this.ticksPerFrame = ticksPerFrame;

        int currentFrameX = 0;
        int currentFrameY = 0;

        for (int frame = 0; frame < totalFrames; frame++) {
            if (currentFrameX + width > texture.getWidth()) {
                currentFrameX = 0;
                currentFrameY += height;
            }
            if (currentFrameY + height > texture.getHeight()) {
                break;
            }
            frameCoords[frame][0] = currentFrameX + width;
            frameCoords[frame][1] = currentFrameY + height;
            frameCoords[frame][2] = currentFrameX;
            frameCoords[frame][3] = currentFrameY;

            currentFrameX += width;
        }

    }

    public void setSpriteSheet(BufferedImage texture, int width, int height,
            int totalFrames, int ticksPerFrame) {
        if (totalFrames <= 0 || width <= 0 || height <= 0) {
            return;
        }
        this.texture = texture;

        frameWidth = width;
        frameHeight = height;
        frameCoords = new int[totalFrames][4];
        this.totalFrames = totalFrames;
        currentFrame = 0;

        currentTick = 0;
        this.ticksPerFrame = ticksPerFrame;

        int currentFrameX = 0;
        int currentFrameY = 0;

        for (int frame = 0; frame < totalFrames; frame++) {
            if (currentFrameX + width > texture.getWidth()) {
                currentFrameX = 0;
                currentFrameY += height;
            }
            if (currentFrameY + height > texture.getHeight()) {
                break;
            }
            frameCoords[frame][0] = currentFrameX + width;
            frameCoords[frame][1] = currentFrameY + height;
            frameCoords[frame][2] = currentFrameX;
            frameCoords[frame][3] = currentFrameY;

            currentFrameX += width;
        }
    }

    /**
     * Updates the currently drawn sprite of the spritesheet.
     */
    private void updateSpriteSheetAnimation() {
        if ((DyeHard.state == DyeHard.State.PLAYING) || (overRide)) {
            if (currentTick < ticksPerFrame) {
                currentTick++;
                return;
            }

            currentTick = 0;
            if (reverse) {
                --currentFrame;
                if (currentFrame < 0) {
                    currentFrame = totalFrames - 1;
                    return;
                }
                if (currentFrame == 0) {
                    spriteCycleDone = true;
                } else {
                    spriteCycleDone = false;
                }
            } else {
                if (currentFrame >= totalFrames - 1) {
                    spriteCycleDone = true;
                    if (stopAtEnd) {
                        return;
                    } else {
                        currentFrame = 0;
                        return;
                    }
                } else {
                    spriteCycleDone = false;
                }
                ++currentFrame;
            }
        }
    }

    public int getNumFrames() {
        return totalFrames;
    }

    public int getCurFrame() {
        return currentFrame;
    }

    public void setCurFrame(int frame) {
        currentFrame = frame;
    }

    public void setFrameNumber(int frameNumber) {
        currentFrame = frameNumber;
        // currentFrame = Math.clamp(currentFrame, 0, totalFrames - 1)
        currentFrame = Math.min(currentFrame, totalFrames - 1);
        currentFrame = Math.max(currentFrame, 0);
    }

    // Helper getters for drawing a portion of an image(the spritesheet)
    private int getSpriteUpperX() {
        if (currentFrame >= frameCoords.length) {
            currentFrame = frameCoords.length - 1;
        }
        return frameCoords[currentFrame][0];
    }

    private int getSpriteUpperY() {
        if (currentFrame >= frameCoords.length) {
            currentFrame = frameCoords.length - 1;
        }
        return frameCoords[currentFrame][1];
    }

    private int getSpriteLowerX() {
        if (currentFrame >= frameCoords.length) {
            currentFrame = frameCoords.length - 1;
        }
        return frameCoords[currentFrame][2];
    }

    private int getSpriteLowerY() {
        if (currentFrame >= frameCoords.length) {
            currentFrame = frameCoords.length - 1;
        }
        return frameCoords[currentFrame][3];
    }

    public void setPanningSheet(BufferedImage t, int totalFrames,
            int ticksPerFrame, boolean vertical) {
        if (totalFrames <= 0) {
            return;
        }
        this.vertical = vertical;
        this.totalFrames = totalFrames;
        currentFrame = 0;

        currentTick = 0;
        this.ticksPerFrame = ticksPerFrame;

        if (texture == null) {
            texture = t;
        }

        int factor;
        if (vertical) {
            float ratio = t.getWidth() / size.getX();
            factor = ((int) ((size.getY() * ratio) / t.getHeight())) * 2;
            if (factor < 1) {
                factor = 1;
            }
            extra = (factor * t.getHeight()) - ((int) (ratio * size.getY()));
        } else {
            float ratio = t.getHeight() / size.getY();
            factor = ((int) ((size.getX() * ratio) / t.getWidth())) * 2;
            if (factor < 1) {
                factor = 1;
            }
            extra = (factor * t.getWidth()) - ((int) (ratio * size.getX()));
        }

        if (factor > 1) {
            texture = setTiling(t, factor, vertical);
        } else {
            texture = t;
        }
    }

    @Override
    public void draw() {
        if (flash) {
            if (flashCount == flashRate) {
                drawImage = !drawImage;
                flashCount = 0;
            }
            flashCount++;
        }
        if (drawImage && texture != null) {
            if (usingSpriteSheet) {
                BaseCode.resources.drawImage(texture,
                        center.getX() - (size.getX() * 0.5f), center.getY()
                                - (size.getY() * 0.5f),
                        center.getX() + (size.getX() * 0.5f), center.getY()
                                + (size.getY() * 0.5f), getSpriteLowerX(),
                        getSpriteLowerY(), getSpriteUpperX(),
                        getSpriteUpperY(), rotate);
                updateSpriteSheetAnimation();
            } else if (panning) {
                if (vertical) {
                    BaseCode.resources.drawImage(texture,
                            center.getX() - (size.getX() * 0.5f), center.getY()
                                    - (size.getY() * 0.5f), center.getX()
                                    + (size.getX() * 0.5f), center.getY()
                                    + (size.getY() * 0.5f), 0, extra
                                    / totalFrames * currentFrame,
                            texture.getWidth(), extra / totalFrames
                                    * currentFrame
                                    + (texture.getHeight() - extra), rotate);
                } else {
                    BaseCode.resources.drawImage(texture,
                            center.getX() - (size.getX() * 0.5f), center.getY()
                                    - (size.getY() * 0.5f), center.getX()
                                    + (size.getX() * 0.5f), center.getY()
                                    + (size.getY() * 0.5f), extra / totalFrames
                                    * currentFrame, 0, extra / totalFrames
                                    * currentFrame
                                    + (texture.getWidth() - extra),
                            texture.getHeight(), rotate);
                }
                updateSpriteSheetAnimation();
            } else {
                BaseCode.resources.drawImage(texture,
                        center.getX() - (size.getX() * 0.5f), center.getY()
                                - (size.getY() * 0.5f),
                        center.getX() + (size.getX() * 0.5f), center.getY()
                                + (size.getY() * 0.5f), rotate);
            }
        } else if (texture == null) {
            BaseCode.resources.setDrawingColor(color);

            if (drawFilledRect) {
                BaseCode.resources.drawFilledRectangle(center.getX(),
                        center.getY(), size.getX() * 0.5f, size.getY() * 0.5f,
                        rotate);
            } else {
                BaseCode.resources.drawOutlinedRectangle(center.getX(),
                        center.getY(), size.getX() * 0.5f, size.getY() * 0.5f,
                        rotate);
            }
        }
    }

    public void setDrawImage(boolean value) {
        drawImage = value;
    }

    public void setDrawFilledRect(boolean value) {
        drawFilledRect = value;
    }

    public boolean containsPoint(Vector2 point) {
        if (rotate != 0.0f) {
            // Rotate the point to match this object's rotation
            point = point.clone().sub(center).rotate(-rotate).add(center);
        }

        return (point.getX() >= (center.getX() - (size.getX() * 0.5f)))
                && (point.getX() < (center.getX() + (size.getX() * 0.5f)))
                && (point.getY() >= (center.getY() - (size.getY() * 0.5f)))
                && (point.getY() < (center.getY() + (size.getY() * 0.5f)));
    }

    public boolean pixelTouches(DyehardRectangle otherPrim) {
        return pixelTouches(otherPrim, null);
    }

    // From the C# code "TexturedPrimitivePixelCollide.cs"
    public boolean pixelTouches(DyehardRectangle otherPrim, Vector2 collidePoint) {
        if (texture == null || otherPrim.texture == null) {
            return false;
        }

        if (!primitivesTouches(otherPrim)) {
            return false;
        }

        Vector2 myXDir = Vector2.rotateVectorByAngle(Vector2.unitX,
                (float) Math.toRadians(rotate));
        Vector2 myYDir = Vector2.rotateVectorByAngle(Vector2.unitY,
                (float) Math.toRadians(rotate));

        Vector2 otherXDir = Vector2.rotateVectorByAngle(Vector2.unitX,
                (float) Math.toRadians(otherPrim.rotate));
        Vector2 otherYDir = Vector2.rotateVectorByAngle(Vector2.unitY,
                (float) Math.toRadians(otherPrim.rotate));

        if (collidePoint == null) {
            collidePoint = new Vector2();
        }

        int minX = usingSpriteSheet ? getSpriteLowerX() : 0;
        int maxX = usingSpriteSheet ? getSpriteUpperX() : texture.getWidth();

        int minY = usingSpriteSheet ? getSpriteLowerY() : 0;
        int maxY = usingSpriteSheet ? getSpriteUpperY() : texture.getHeight();

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                int myColor = ((texture.getRGB(x, y) >> 24) & 0xff);

                // Skip transparent pixels
                if (myColor <= 0) {
                    continue;
                }

                collidePoint.set(indexToCameraPosition(x - minX, y - minY,
                        myXDir, myYDir));
                Vector2 otherIndex = otherPrim.cameraPositionToIndex(
                        collidePoint, otherXDir, otherYDir);
                int xMin = (int) otherIndex.getX();
                int yMin = (int) otherIndex.getY();

                // TODO add check for otherPrim spriteSheet width/height
                if (xMin < 0 || xMin >= otherPrim.texture.getWidth()
                        || yMin < 0 || yMin >= otherPrim.texture.getHeight()) {
                    continue;
                }

                // overlap found!
                if (((otherPrim.texture.getRGB(xMin, yMin) >> 24) & 0xff) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private Vector2 indexToCameraPosition(int i, int j, Vector2 xDir,
            Vector2 yDir) {
        // Translate from percent across image to percent across size
        float x = i * size.getX() / (texture.getWidth() - 1);
        float y = j * size.getY() / (texture.getHeight() - 1);

        Vector2 r = center.clone()
                .add(xDir.clone().mult(x - (size.getX() * 0.5f)))
                .sub(yDir.clone().mult(y - (size.getY() * 0.5f)));

        return r;
    }

    private Vector2 cameraPositionToIndex(Vector2 p, Vector2 xDir, Vector2 yDir) {
        Vector2 delta = p.clone().sub(center);

        float xOffset = Vector2.dot(delta, xDir);
        float yOffset = Vector2.dot(delta, yDir) - 1;

        float i = texture.getWidth() * (xOffset / size.getX());
        float j = texture.getHeight() * (yOffset / size.getY());
        i += texture.getWidth() / 2;
        j = (texture.getHeight() / 2) - j;

        return new Vector2(i, j);
    }

    private boolean primitivesTouches(DyehardRectangle otherPrim) {
        final float epsilon = 0.0001f;

        if (Math.abs(Math.toRadians(rotate)) < epsilon
                && Math.abs(Math.toRadians(otherPrim.rotate)) < epsilon) {
            return ((center.getX() - (size.getX() * 0.5f)) < (otherPrim.center
                    .getX() + (otherPrim.size.getX() * 0.5f)))
                    && ((center.getX() + (size.getX() * 0.5f)) > (otherPrim.center
                            .getX() - (otherPrim.size.getX() * 0.5f)))
                    && ((center.getY() - (size.getY() * 0.5f)) < (otherPrim.center
                            .getY() + (otherPrim.size.getY() * 0.5f)))
                    && ((center.getY() + (size.getY() * 0.5f)) > (otherPrim.center
                            .getY() - (otherPrim.size.getY() * 0.5f)));
        } else {
            // From the C# version:
            // one of both are rotated ... use radius ... be conservative
            // Use the larger of the Width/Height and approx radius
            // Sqrt(1/2)*x Approx = 0.71f * x;
            float r1 = 0.71f * Math.max(size.getX(), size.getY());
            float r2 = 0.71f * Math.max(otherPrim.size.getX(),
                    otherPrim.size.getY());
            return (otherPrim.center.clone().sub(center).length() < (r1 + r2));
        }
    }

    public boolean collided(DyehardRectangle otherPrim) {
        return (visible && otherPrim.visible && primitivesTouches(otherPrim));
    }

    // Pushes the other object out of the current object
    public Vector2 pushOutCircle(DyehardRectangle other) {
        Vector2 resolved = null;

        if (other != null) {
            float topD = 0f, bottomD = 0f, leftD = 0f, rightD = 0f;

            // Flying upwards
            if (other.velocity.getY() > 0.0f) {
                // Check for bottom penetration
                topD = (other.center.getY() + (other.size.getY() * 0.5f))
                        - (center.getY() - (size.getY() * 0.5f));
            }
            // Flying downwards
            else {
                // Check for top penetration
                bottomD = (center.getY() + (size.getY() * 0.5f))
                        - (other.center.getY() - (other.size.getY() * 0.5f));
            }

            // Flying towards right
            if (other.velocity.getX() > 0) {
                // Check for left penetration
                leftD = (other.center.getX() + (other.size.getX() * 0.5f))
                        - (center.getX() - (size.getX() * 0.5f));
            }
            // Flying towards left
            else {
                // Check for right penetration
                rightD = (center.getX() + (size.getX() * 0.5f))
                        - (other.center.getX() - (other.size.getX() * 0.5f));
            }

            if (topD > 0) {
                if (leftD > 0) {
                    if (topD < leftD) {
                        // Push up from top
                        resolved = new Vector2();
                        resolved.setY((center.getY()
                                - (other.size.getY() * 0.5f) - (size.getY() * 0.5f))
                                - other.center.getY());
                        other.center
                                .setY(other.center.getY() + resolved.getY());
                    } else {
                        // Push towards left
                        resolved = new Vector2();
                        resolved.setX((center.getX()
                                - (other.size.getX() * 0.5f) - (size.getX() * 0.5f))
                                - other.center.getX());
                        other.center
                                .setX(other.center.getX() + resolved.getX());
                    }
                } else if (rightD > 0) {
                    if (topD < rightD) {
                        // Push up from top
                        resolved = new Vector2();
                        resolved.setY((center.getY()
                                - (other.size.getY() * 0.5f) - (size.getY() * 0.5f))
                                - other.center.getY());
                        other.center
                                .setY(other.center.getY() + resolved.getY());
                    } else {
                        // Push towards right
                        resolved = new Vector2();
                        resolved.setX((center.getX()
                                + (other.size.getX() * 0.5f) + (size.getX() * 0.5f))
                                - other.center.getX());
                        other.center
                                .setX(other.center.getX() + resolved.getX());
                    }
                }
            } else if (bottomD > 0) {
                if (leftD > 0) {
                    if (bottomD < leftD) {
                        // Push up from bottom
                        resolved = new Vector2();
                        resolved.setY((center.getY()
                                + (other.size.getY() * 0.5f) + (size.getY() * 0.5f))
                                - other.center.getY());
                        other.center
                                .setY(other.center.getY() + resolved.getY());
                    } else {
                        // Push towards left
                        resolved = new Vector2();
                        resolved.setX((center.getX()
                                - (other.size.getX() * 0.5f) - (size.getX() * 0.5f))
                                - other.center.getX());
                        other.center
                                .setX(other.center.getX() + resolved.getX());
                    }
                } else if (rightD > 0) {
                    if (bottomD < rightD) {
                        // Push up from bottom
                        resolved = new Vector2();
                        resolved.setY((center.getY()
                                + (other.size.getY() * 0.5f) + (size.getY() * 0.5f))
                                - other.center.getY());
                        other.center
                                .setY(other.center.getY() + resolved.getY());
                    } else {
                        // Push towards right
                        resolved = new Vector2();
                        resolved.setX((center.getX()
                                + (other.size.getX() * 0.5f) + (size.getX() * 0.5f))
                                - other.center.getX());
                        other.center
                                .setX(other.center.getX() + resolved.getX());
                    }
                }
            }

            /*
             * Vector2 otherCenter = new Vector2(other.center.x,
             * other.center.y); float left = center.x - size.x; float right =
             * center.x + size.x; float top = center.y + size.y; float bottom =
             * center.y - size.y; otherCenter.x = clamp(otherCenter.x, left,
             * right); otherCenter.y = clamp(otherCenter.y, top, bottom);
             * Vector2 direction = other.center.sub(otherCenter); float dist =
             * other.size.x - direction.length(); direction.normalize();
             * other.center.set(other.center.add(direction.mult(dist)));
             */
        }

        return resolved;
    }

    public void startFlashing() {
        flashCount = 0;
        drawImage = true;
        flash = true;
    }

    public void stopFlashing() {
        drawImage = true;
        flash = false;
    }

    private BufferedImage setTiling(BufferedImage img, int tileNum,
            boolean vertical) {
        int currentEnd = 0;
        int tileWidth = img.getWidth();
        int tileHeight = img.getHeight();

        if (vertical) {
            int width = tileWidth;
            int height = tileHeight * tileNum;

            BufferedImage newImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = newImage.createGraphics();

            while (currentEnd < height) {
                g2.drawImage(img, null, 0, currentEnd);
                currentEnd += tileHeight;
            }
            g2.dispose();
            return newImage;
        } else {
            int width = tileWidth * tileNum;
            int height = tileHeight;

            BufferedImage newImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = newImage.createGraphics();

            while (currentEnd < width) {
                g2.drawImage(img, null, currentEnd, 0);
                currentEnd += tileWidth;
            }
            g2.dispose();
            return newImage;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        texture = null;
    }

    /*
     * private float clamp(float val, float min, float max) { return (val < min
     * ? val = min : (val > max ? val = max : val)); }
     */
}
