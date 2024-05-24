package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.multiplayer.ClientLevel;
import top.infsky.cheatdetector.CheatDetector;

import java.util.Objects;

public class LevelUtils {
    public static ClientLevel getClientLevel() {
        try (ClientLevel level = CheatDetector.CLIENT.level) {
            return Objects.requireNonNull(level);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
