package dyehard.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextureTile {
    // tileNum = number of tiles wanted, img = one tile in bufferedImage
    // this allows user to make a large buffered image using smaller tiles
    public BufferedImage setTiling(BufferedImage img, int tileNum,
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
}
