package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import java.util.List;
import java.util.Set;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class VelocityA extends Check {
    public boolean isHurt = false;
    public boolean shouldCheck = false;
    public int hasDelayed = 0;  // 单位毫秒
    public Vec3 hurtPos = Vec3.ZERO;

    public VelocityA(@NotNull TRPlayer player) {
        super("VelocityA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.hurtTime > 0 && !shouldBypass()) {  // 客户端无法知道伤害来源，因此我们需要判断。
            if (!isHurt) {  // onHurt
                hurtPos = player.currentPos;  // 如果之后不动 = velocity
                isHurt = true;
                shouldCheck = true;
                return;  // 第一个tick检查是没有必要的
            }
        } else {
            isHurt = false;
        }

        if (isHurt && shouldCheck) {
            if (hasDelayed > player.latency + CONFIG().getAdvanced().getVelocityAExtraDelayedMs()) {
                if (player.currentPos == hurtPos)
                    flag("latency: %d  hasDelayed: %d".formatted(player.latency, hasDelayed));
                shouldCheck = false;
                hasDelayed = 0;
                hurtPos = Vec3.ZERO;
            } else {  // wait for internet latency
                hasDelayed += 50;
            }
        }
    }

    private final List<MobEffect> hurtEffects = List.of(
            MobEffects.WITHER,
            MobEffects.POISON
    );
    private boolean shouldBypass() {
        final Set<MobEffect> hasEffects = player.fabricPlayer.getActiveEffectsMap().keySet();

        // fire
        if ((player.fabricPlayer.isOnFire() || player.fabricPlayer.isInLava()) && !hasEffects.contains(MobEffects.FIRE_RESISTANCE))
            return true;

        // asphyxia
        if (player.fabricPlayer.isInWater() && !hasEffects.contains(MobEffects.WATER_BREATHING))
            return true;

        // hunger
        if (CheatDetector.CLIENT.level != null
                && player.fabricPlayer.getFoodData().getFoodLevel() == 0 && CheatDetector.CLIENT.level.getDifficulty() == Difficulty.HARD)
            return true;

        // MagmaBlock
        if (CheatDetector.CLIENT.level != null
                && CheatDetector.CLIENT.level.getBlockState(player.fabricPlayer.blockPosition().below()).getBlock() instanceof MagmaBlock)
            return true;

        // effect
        for (MobEffect effect : hurtEffects) {
            if (hasEffects.contains(effect))
                return true;
        }

        return false;
    }

    @Override
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getVelocityAAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isVelocityACheck();
    }
}
