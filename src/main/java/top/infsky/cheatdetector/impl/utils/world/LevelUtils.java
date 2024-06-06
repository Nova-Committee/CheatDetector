package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.utils.TRPlayer;

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

    public static @NotNull List<LivingEntity> getEntities(@NotNull ClientLevel level) {
        List<LivingEntity> result = new ArrayList<>();
        level.entitiesForRendering().forEach(entity -> {
            if (entity instanceof LivingEntity livingEntity) result.add(livingEntity);
        });
        return result;
    }

    public static @NotNull List<LivingEntity> getEntities() {
        return getEntities(getClientLevel());
    }

    /**
     * useItemOn but fix packet.
     * @param position useItemOn to this position
     * @param hand interactionHand
     */
    public static InteractionResult useItemOn(Vec3 position, InteractionHand hand) {
        if (TRPlayer.CLIENT.player != null && TRPlayer.CLIENT.gameMode != null) {
            BlockHitResult hitResult = BlockHitResult.miss(position, Direction.getNearest(position.x(), position.y(), position.z()), BlockPos.containing(position));
            return TRPlayer.CLIENT.gameMode.useItemOn(TRPlayer.CLIENT.player, hand, hitResult);
        }
        return InteractionResult.FAIL;
    }

    /**
     * placeBlock but fix packet.
     * @param position useItemOn to this position
     * @param hand interactionHand
     */
    public static InteractionResult placeBlock(BlockPos position, InteractionHand hand) {
        if (TRPlayer.CLIENT.player != null && TRPlayer.CLIENT.gameMode != null) {
            Vec3 vec3 = TRPlayer.CLIENT.player.position().subtract(position.getCenter());
            BlockHitResult hitResult = BlockHitResult.miss(position.getCenter(), Direction.getNearest(vec3.x(), vec3.y(), vec3.z()), position);
            return TRPlayer.CLIENT.gameMode.useItemOn(TRPlayer.CLIENT.player, hand, hitResult);
        }
        return InteractionResult.FAIL;
    }
}
