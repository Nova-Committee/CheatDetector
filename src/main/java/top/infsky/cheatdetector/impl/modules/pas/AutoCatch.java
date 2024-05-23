package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.modules.common.Rotation;
import top.infsky.cheatdetector.impl.utils.world.ContainerUtils;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.NoSuchElementException;

public class AutoCatch extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public AutoCatch(@NotNull TRSelf player) {
        super("AutoCatch", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        if (!Advanced3Config.autoCatchAlways && player.fabricPlayer.isPassenger()) return;

        doCatch();
    }

    private void doCatch() {
        try {
            AbstractClientPlayer target = LevelUtils.getClientLevel().players().stream()
                    .filter(p -> p.getGameProfile().getName().equals(Advanced3Config.autoCatchName))
                    .findFirst()
                    .orElseThrow();

            double distance = target.distanceTo(player.fabricPlayer);
            if (distance > Advanced3Config.autoCatchDistance && !Advanced3Config.autoCatchAsPossible) return;
            if (distance > 6) {
                if (Advanced3Config.autoCatchAsPossible) {
                    Vec3 targetPos = target.position().add(target.getDeltaMovement());
                    player.fabricPlayer.setDeltaMovement(getTeleportMotion(targetPos).add(target.getDeltaMovement()));
                    return;
                }
            }
            player.fabricPlayer.setDeltaMovement(0, 0, 0);

            player.fabricPlayer.getInventory().selected = ContainerUtils.findItem(player.fabricPlayer.getInventory(), AirItem.class, ContainerUtils.SlotType.HOTBAR);
            if (Advanced3Config.autoCatchRotate) {
                float yaw = PlayerRotation.getYaw(target.getEyePosition());
                float pitch = PlayerRotation.getPitch(target.getEyePosition());
                if (Advanced3Config.autoCatchSilentRotate) {
                    Rotation.silentRotate(yaw, pitch);
                } else {
                    PlayerRotation.rotate(yaw, pitch);
                }
            }

            player.fabricPlayer.setSilent(false);
            player.fabricPlayer.connection.send(ServerboundInteractPacket.createInteractionPacket(target, false, InteractionHand.MAIN_HAND, player.fabricPlayer.position()));
            player.fabricPlayer.swing(InteractionHand.MAIN_HAND);
        } catch (NoSuchElementException ignored) {
        }
    }

    private @NotNull Vec3 getTeleportMotion(@NotNull final Vec3 targetPos) {
        Vec3 current = player.fabricPlayer.position();
        final double step = Advanced3Config.autoCatchAsPossibleTeleportDistance;

        current = new Vec3(
                add(current.x(), step, targetPos.x()),
                add(current.y(), step, targetPos.y()),
                add(current.z(), step, targetPos.z())
        );
        return current.subtract(player.fabricPlayer.position());
    }

    private static double add(double current, double step, double target) {
        if (current < target) {
            return Math.min(current + step, target);
        } else {
            return Math.max(current - step, target);
        }
    }

    public void onStopRiding() {
        if (isDisabled()) return;
        if (!Advanced3Config.autoCatchFast) return;

        doCatch();
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.autoCatchEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
