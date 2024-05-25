package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelUtils {
    public static ClientLevel getClientLevel() {
        try (ClientLevel level = CheatDetector.CLIENT.level) {
            return Objects.requireNonNull(level);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull List<LivingEntity> getEntities() {
        List<LivingEntity> result = new ArrayList<>();
        getClientLevel().entitiesForRendering().forEach(entity -> {
            if (entity instanceof LivingEntity livingEntity) result.add(livingEntity);
        });
        return result;
    }
}
