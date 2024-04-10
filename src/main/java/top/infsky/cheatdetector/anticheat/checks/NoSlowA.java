package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;

import java.util.List;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class NoSlowA extends Check {
    public static final List<Double> SLOW_SPEED = List.of(
            CONFIG().getAdvanced().getNoSlowASpeedTick1(),
            CONFIG().getAdvanced().getNoSlowASpeedTick2(),
            CONFIG().getAdvanced().getNoSlowASpeedTick3(),
            CONFIG().getAdvanced().getNoSlowASpeedTick4(),
            CONFIG().getAdvanced().getNoSlowASpeedTick5(),
            CONFIG().getAdvanced().getNoSlowASpeedTick6(),
            CONFIG().getAdvanced().getNoSlowASpeedTick7(),
            CONFIG().getAdvanced().getNoSlowASpeedTick8()
    );
    public short itemUseTick = 0;
    public short disableTick = 0;  // 跳跃弱检测
    public NoSlowA(@NotNull TRPlayer player) {
        super("NoSlowA", player);
    }

    @Override
    public void _onTick() {
        if (!player.fabricPlayer.isUsingItem() || !player.lastUsingItem) {
            itemUseTick = 0;
            return;  // 当连续两个tick使用物品才检查
        }
        if (player.jumping) {
            disableTick = CONFIG().getAdvanced().getNoSlowAInJumpDisableTick();
            return;
        }
        if (disableTick > 0) {
            disableTick--;
            return;
        }

        final double secSpeed = PlayerMove.getXzSecSpeed(player.lastPos, player.currentPos);
        final double possibleSpeed = SLOW_SPEED.get(itemUseTick) * player.speedMul + CONFIG().getAntiCheat().getThreshold();
        if (secSpeed > possibleSpeed) {
            flag(String.format("Current: %.2f  Max: %.2f", secSpeed, possibleSpeed));
        }
        if (itemUseTick < SLOW_SPEED.size() - 1) itemUseTick++;
    }

    @Override
    protected long getAlertBuffer() {
        return CONFIG().getAdvanced().getNoSlowAAlertBuffer();
    }

    @Override
    protected boolean isDisabled() {
        return !CONFIG().getAdvanced().isNoSlowACheck();
    }
}
