package top.infsky.cheatdetector.impl.checks;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

import java.util.ArrayList;
import java.util.List;

public class MotionA extends Check {
    public static final List<MobEffect> IGNORED_EFFECTS = List.of(MobEffects.JUMP, MobEffects.SLOW_FALLING);
    public static final List<Double> JUMP_MOTIONS = List.of(0.41159999516010254, -0.08506399504327788, -0.08336271676487925, -0.0816954640195993, -0.08006155629742463, -0.07846032669852913, -0.07689112166107052, -0.07535330069443089, -0.07384623611779237, -0.07236931280394177, -0.07092192792819801);

    private final List<Double> motionY = new ArrayList<>();
    private boolean readyToJump = false;
    private Double jumpFromY = null;

    public MotionA(@NotNull TRPlayer player) {
        super("*MotionA*", player);
    }

    @Override
    public void _onTick() {
        if (!check()) {
            jumpFromY = null;
            readyToJump = false;
            return;
        }

        if (player.currentOnGround && !readyToJump) {
            readyToJump = true;
            motionY.clear();
        }

        if (player.jumping) {
            if (!readyToJump) return;
            if (jumpFromY == null) jumpFromY = player.lastPos.y();  // jumping = true时，玩家已经离地了
            motionY.add(player.fabricPlayer.getDeltaMovement().y());
        } else if (jumpFromY != null) {
            if (jumpFromY == player.currentPos.y()) {  // 满足判断条件
                if (motionY.size() != JUMP_MOTIONS.size()) {
                    flag("Invalid jump time.");
                } else {
                    for (int i = 0; i < JUMP_MOTIONS.size(); i++) {
                        if (Math.abs(motionY.get(i) - JUMP_MOTIONS.get(i)) > AntiCheatConfig.threshold) {
                            flag("Invalid jump motion at tick %s.".formatted(i));
                            break;
                        }
                    }
                }
            }
            jumpFromY = null;
            readyToJump = false;
        }
    }

    private boolean check() {
        if (player.fabricPlayer.getActiveEffectsMap().keySet().stream().anyMatch(IGNORED_EFFECTS::contains)) return false;
        if (player.fabricPlayer.isInWall() || player.fabricPlayer.isInWater() || player.fabricPlayer.isPassenger() || player.fabricPlayer.isAutoSpinAttack()) return false;
        return !player.fabricPlayer.getAbilities().flying;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.motionAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.motionACheck;
    }
}
