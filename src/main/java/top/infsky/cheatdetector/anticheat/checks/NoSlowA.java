package top.infsky.cheatdetector.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.PlayerMove;

import java.util.List;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class NoSlowA extends Check {
    public static final List<Double> SLOW_SPEED = List.of(2.56, 1.92, 1.6, 1.4, 1.36, 1.26, 1.18, 1.16);
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
            disableTick = 4;
            return;
        }
        if (disableTick > 0) {
            disableTick--;
            return;
        }

        final double secSpeed = PlayerMove.getXzSecSpeed(player.lastPos, player.currentPos);
        final double possibleSpeed = SLOW_SPEED.get(itemUseTick) * player.speedMul + CONFIG().getThreshold();
        if (secSpeed > possibleSpeed) {
            flag(String.format("Current: %.2f  Max: %.2f", secSpeed, possibleSpeed));
        }
        if (itemUseTick < SLOW_SPEED.size() - 1) itemUseTick++;
    }
}
