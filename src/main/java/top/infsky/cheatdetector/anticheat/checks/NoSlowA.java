package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;

import java.util.List;

public class NoSlowA extends Check {
    public static final List<Double> SLOW_SPEED = List.of(
            AdvancedConfig.noSlowASpeedTick1,
            AdvancedConfig.noSlowASpeedTick2,
            AdvancedConfig.noSlowASpeedTick3,
            AdvancedConfig.noSlowASpeedTick4,
            AdvancedConfig.noSlowASpeedTick5,
            AdvancedConfig.noSlowASpeedTick6,
            AdvancedConfig.noSlowASpeedTick7,
            AdvancedConfig.noSlowASpeedTick8
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
            disableTick = AdvancedConfig.getNoSlowAInJumpDisableTick();
            return;
        }
        if (disableTick > 0) {
            disableTick--;
            return;
        }

        final double secSpeed = PlayerMove.getXzSecSpeed(player.lastPos, player.currentPos);
        final double possibleSpeed = SLOW_SPEED.get(itemUseTick) * player.speedMul + AntiCheatConfig.threshold;
        if (secSpeed > possibleSpeed) {
            flag(String.format("Current: %.2f  Max: %.2f", secSpeed, possibleSpeed));
        }
        if (itemUseTick < SLOW_SPEED.size() - 1) itemUseTick++;
    }

    @Override
    protected int getAlertBuffer() {
        return AdvancedConfig.noSlowAAlertBuffer;
    }

    @Override
    protected boolean isDisabled() {
        return !AdvancedConfig.noSlowACheck;
    }
}
