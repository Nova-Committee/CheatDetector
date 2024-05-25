package top.infsky.cheatdetector.impl.modules.danger;

import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.DangerConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRSelf;

public class SlowMotion extends Module {
    private boolean lastEnabled = false;
    public SlowMotion(@NotNull TRSelf player) {
        super("SlowMotion", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            if (lastEnabled && Advanced3Config.slowMotionStopOnDisabled) {
                player.fabricPlayer.setDeltaMovement(0, 0, 0);
            }
            lastEnabled = false;
            return;
        }

        Vec3 currentMotion = player.fabricPlayer.getDeltaMovement();
        Vec3 futureMotion = currentMotion.multiply(
                Advanced3Config.slowMotionMultiplyXZ, Advanced3Config.slowMotionMultiplyY, Advanced3Config.slowMotionMultiplyXZ
        );

        if (Advanced3Config.slowMotionLimit) {
            final double maxXz = Advanced3Config.slowMotionLimitXZ;
            final double maxY = Advanced3Config.slowMotionLimitY;
            if (Math.abs(futureMotion.x()) > maxXz || Math.abs(futureMotion.z()) > maxXz) {
                double offset = Math.max(Math.abs(futureMotion.x()), Math.abs(futureMotion.z())) - maxXz;
                futureMotion = new Vec3(addToZero(futureMotion.x(), offset), addToZero(futureMotion.y(), offset), addToZero(futureMotion.z(), offset));
            }

            if (Math.abs(futureMotion.y()) > maxY) {
                futureMotion = new Vec3(futureMotion.x(), addToZero(futureMotion.y(), Math.abs(futureMotion.y()) - maxY), futureMotion.z());
            }
        }

        if (Advanced3Config.slowMotionFastStop) {
            if (!PlayerMove.isMove()) {
                futureMotion = new Vec3(0, futureMotion.y(), 0);
            }
        }

        player.fabricPlayer.setDeltaMovement(futureMotion);

        lastEnabled = true;
    }

    private double addToZero(double num, double offset) {
        if (num < 0) {
            if (num + offset >= 0) {
                return 0;
            } else {
                return num + offset;
            }
        } else if (num > 0) {
            if (num - offset <= 0) {
                return 0;
            } else {
                return num - offset;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean isDisabled() {
        return !DangerConfig.slowMotionEnabled || !DangerConfig.aaaDangerModeEnabled
                || (Advanced3Config.slowMotionOnlyElytra && !player.fabricPlayer.getInventory().getArmor(2).is(Items.ELYTRA)
                || (Advanced3Config.slowMotionNotWhileFallFlying && player.fabricPlayer.isFallFlying()));
    }
}
