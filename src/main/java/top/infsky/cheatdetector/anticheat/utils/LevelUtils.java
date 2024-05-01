package top.infsky.cheatdetector.anticheat.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import top.infsky.cheatdetector.anticheat.TRPlayer;

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
