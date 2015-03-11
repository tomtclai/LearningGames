import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Engine.BaseCode;
import Engine.Vector2;
import dyehard.World.PlatformSingle;

public class Platform {
    public static float height = 1.25f;
    private final float width = 6.5f;
    private static Random RANDOM = new Random();
    private final List<PlatformSingle> plats;

    public Platform(int offset, float leftEdge, boolean continuous) {
        plats = new ArrayList<PlatformSingle>();
        fillPlatform(offset, leftEdge, continuous);
    }

    private void fillPlatform(int offset, float leftEdge, boolean continuous) {
        // set up platform
        float Ypos = ((offset * 1f) / Stargate.GATE_COUNT)
                * BaseCode.world.getHeight();
        int numPlat = (int) (Stargate.WIDTH / width);
        if (continuous) {
            for (int i = 0; i < numPlat; i++) {
                float Xpos = (width * i) + leftEdge + (width / 2);
                plats.add(new PlatformSingle(new Vector2(Xpos, Ypos)));
            }
        } else {
            // randomly fill platform
            int consecutiveChance = 10;
            boolean platform = true;
            for (int i = 0; i < numPlat; i++) {
                if (consecutiveChance <= 0
                        || RANDOM.nextInt(consecutiveChance) == 0) {
                    platform = !platform;
                    consecutiveChance = 10;
                }
                if (platform) {
                    float Xpos = (width * i) + leftEdge + (width / 2);
                    plats.add(new PlatformSingle(new Vector2(Xpos, Ypos)));
                }
                consecutiveChance -= 1;
            }
        }
    }

    public void destroy() {
        for (PlatformSingle p : plats) {
            p.destroy();
        }
    }
}
