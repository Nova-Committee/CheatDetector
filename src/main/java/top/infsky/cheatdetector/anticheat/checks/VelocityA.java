package top.infsky.cheatdetector.anticheat.checks;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;
import top.infsky.cheatdetector.anticheat.utils.VelocityUtils;
import top.infsky.cheatdetector.config.AdvancedConfig;

/**
 * Velocity Cancel Check
 */
public class VelocityA extends Check {
    public boolean isHurt = false;
    public boolean hasCheck = false;
    public int hasDelayed = 0;  // 单位毫秒
    public Vec3 hurtPos = Vec3.ZERO;
    public boolean hurtOnGround = false;
    public int disableTick = 0;

    public VelocityA(@NotNull TRPlayer player) {
        super("VelocityA", player);
    }

    @Override
    public void _onTick() {
        if (VelocityUtils.shouldBypassNextHurt(player)) {
            disableTick = (int) (Math.ceil((double) player.latency / 50) + AdvancedConfig.velocityAAfterFallExtraDisableTick);
//            customMsg("bypass. disableTick:%s".formatted(disableTick));
            return;
        }

        if (disableTick > 0) {
            disableTick--;
        }

        if (player.fabricPlayer.hurtTime > 0) {
            if (!isHurt) {  // onHurt
                reset();
                hurtPos = player.currentPos;  // 如果之后不动 = velocity
                hurtOnGround = player.fabricPlayer.onGround();
                isHurt = true;
                hasCheck = disableTick > 0;  // disable = no check
                return;  // 第一个tick检查是没有必要的
            }
        } else {
            isHurt = false;
        }

        if (isHurt && !hasCheck) {

            // Velocity 0 0
            if (VelocityUtils.shouldCheck(player, VelocityUtils.VelocityDirection.ALL)  // 客户端无法知道伤害来源，因此我们需要判断。
                    && player.currentPos == hurtPos) {
                latencyFlag("Knock-back has cancelled.");
                return;
            }

            // Velocity x 0
            if (VelocityUtils.shouldCheck(player, VelocityUtils.VelocityDirection.VERTICAL)
                    && player.currentPos.y() <= hurtPos.y() && hurtOnGround && player.fabricPlayer.onGround()) {
                latencyFlag("Vertical knock-back has cancelled.");
                return;
            }

            // Velocity 0 x
            if (VelocityUtils.shouldCheck(player, VelocityUtils.VelocityDirection.HORIZON)
                    && PlayerMove.getXZOnlyPos(player.currentPos).distanceTo(PlayerMove.getXZOnlyPos(hurtPos)) == 0
                    && hurtOnGround) {
                latencyFlag("Horizon knock-back has cancelled.");
                return;
            }

            reset();
        }
    }

    private void latencyFlag(String msg) {
        if (hasDelayed > player.latency + AdvancedConfig.velocityAExtraDelayedMs) {
            flag("latency: %d  hasDelayed: %d  %s".formatted(player.latency, hasDelayed, msg));
            reset();
        } else {  // wait for internet latency
            hasDelayed += 50;
        }
    }

    private void reset() {
        hasCheck = true;
        hasDelayed = 0;
        hurtPos = Vec3.ZERO;
    }

    @Override
    protected int getAlertBuffer() {
        return AdvancedConfig.velocityAAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.velocityACheck;
    }
}
