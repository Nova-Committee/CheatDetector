package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.multiplayer.ClientLevel;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.io.IOException;

public class LevelUtils {
    public static ClientLevel getClientLevel() {
        try (ClientLevel level = TRPlayer.CLIENT.level) {
            return level;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
