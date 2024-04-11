package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.VelocityUtils;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

/**
 * Velocity Cancel Check
 */
public class VelocityA extends Check {
    public boolean isHurt = false;
    public boolean hasCheck = false;
    public int hasDelayed = 0;  // 单位毫秒
    public Vec3 hurtPos = Vec3.ZERO;

    public VelocityA(@NotNull TRPlayer player) {
        super("VelocityA", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.hurtTime > 0) {  // 客户端无法知道伤害来源，因此我们需要判断。
            if (!isHurt) {  // onHurt
                hurtPos = player.currentPos;  // 如果之后不动 = velocity
                isHurt = true;
                hasCheck = false;
                return;  // 第一个tick检查是没有必要的
            }
        } else {
            isHurt = false;
        }

        if (isHurt && !hasCheck && VelocityUtils.shouldCheck(player, VelocityUtils.VelocityDirection.ALL)) {
            if (player.currentPos == hurtPos) {
                if (hasDelayed > player.latency + CONFIG().getAdvanced().getVelocityAExtraDelayedMs()) {
                    hasCheck = true;
                    flag("latency: %d  hasDelayed: %d".formatted(player.latency, hasDelayed));
                } else {  // wait for internet latency
                    hasDelayed += 50;
                }
            } else {
                hasCheck = true;
                hasDelayed = 0;
                hurtPos = Vec3.ZERO;
            }
        }
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
