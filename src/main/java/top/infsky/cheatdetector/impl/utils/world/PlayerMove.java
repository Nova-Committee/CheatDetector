package top.infsky.cheatdetector.impl.utils.world;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerMove {
    public static double getXzTickSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
            return getXZOnlyPos(currentTick).distanceTo(getXZOnlyPos(lastTick));
    }

    public static double getXzSecSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
        return getXzTickSpeed(lastTick, currentTick) * 20;  // IDK what's the fucking tps
    }

    @Contract("_ -> new")
    public static @NotNull Vec3 getXZOnlyPos(@NotNull Vec3 position) {
        return new Vec3(position.x(), 0, position.z());
    }

    public static double getJumpDistance(@NotNull AbstractClientPlayer player) {
        try {
            final int x = Objects.requireNonNull(player.getActiveEffectsMap().get(MobEffects.JUMP)).getAmplifier() + 1;
//            return -9.331952072919326 * x * x - 3.672263213983712 * x + 0.6261016701268645;  // chat gpt
          return 0.04837 * x + 0.5356 * x + 1.252;  // numpy
        } catch (NullPointerException e) {
            return 1.252203340253729;
        }
    }

    public static boolean isMove() {
        return TRPlayer.CLIENT.options.keyUp.isDown()
                || TRPlayer.CLIENT.options.keyDown.isDown()
                || TRPlayer.CLIENT.options.keyLeft.isDown()
                || TRPlayer.CLIENT.options.keyRight.isDown();
    }

    public static boolean isNoMove(@NotNull Vec3 motion) {
        return motion.x() == 0 && motion.z() == 0;
    }

    /**
     * Gets the players predicted jump motion the specified amount of ticks ahead
     *
     * @return predicted jump motion
     */
    public static double predictedMotion(final double motion, final int ticks) {
        if (ticks == 0) return motion;
        double predicted = motion;

        for (int i = 0; i < ticks; i++) {
            predicted = (predicted - 0.08) * 0.98F;
        }

        return predicted;
    }

    /**
     * Used to get the players speed
     */
    public static double speed(@NotNull Player player) {
        Vec3 motion = player.getDeltaMovement();
        return Math.hypot(motion.x(), motion.z());
    }

    /**
     * Used to get the players speed
     */
    public static double speed() {
        return speed(TRSelf.getInstance().getFabricPlayer());
    }

    /**
     * Makes the player strafe
     */
    public static void strafe() {
        strafe(speed());
    }

    /**
     * Makes the player strafe at the specified speed
     */
    public static void strafe(final double speed) {
        if (!isMove()) {
            return;
        }

        TRSelf trSelf = TRSelf.getInstance();
        final double yaw = direction(
                trSelf.fabricPlayer.input.forwardImpulse,
                trSelf.fabricPlayer.input.leftImpulse,
                trSelf.fabricPlayer.yRotO + (trSelf.fabricPlayer.getYRot() - trSelf.fabricPlayer.yRotO) * TRPlayer.CLIENT.getFrameTime()
        );

        trSelf.fabricPlayer.setDeltaMovement(getStrafeMotion(speed, yaw, trSelf.fabricPlayer.getDeltaMovement().y()));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Vec3 getStrafeMotion(final double speed, final double yaw, final double motionY) {
        return new Vec3(-Mth.sin((float) yaw) * speed, motionY, Mth.cos((float) yaw) * speed);
    }

    /**
     * Gets the players' movement yaw
     */
    public static double direction(float moveForward, float moveStrafing, float rotationYaw) {
        if (moveForward < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (moveForward < 0) {
            forward = -0.5F;
        } else if (moveForward > 0) {
            forward = 0.5F;
        }

        if (moveStrafing > 0) {
            rotationYaw -= 70 * forward;
        }

        if (moveStrafing < 0) {
            rotationYaw += 70 * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    public static boolean isInvalidMotion(@NotNull Vec3 motion) {
        return Math.abs(motion.x()) >= 3.9
                || Math.abs(motion.y()) >= 3.9
                || Math.abs(motion.z()) >= 3.9;
    }

    public static double getMaxXZDiff(@NotNull Vec3 motion1, @NotNull Vec3 motion2) {
        return Math.max(Math.abs(motion1.x() - motion2.x()), Math.abs(motion1.z() - motion2.z()));
    }

    public static @NotNull List<Vec3> getPosHistoryDiff(final @NotNull List<Vec3> posHistory) {
        List<Vec3> result = new ArrayList<>(posHistory.size() - 1);

        for (int i = 0; i < posHistory.size() - 1; i++) {
            result.add(posHistory.get(i + 1).subtract(posHistory.get(i)));
        }

        return result;
    }
}
