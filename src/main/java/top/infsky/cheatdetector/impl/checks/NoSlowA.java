package top.infsky.cheatdetector.impl.checks;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
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

        if (player.fabricPlayer.isSilent()
                && EnchantmentHelper.getEnchantments(player.fabricPlayer.getInventory().getArmor(4)).containsKey(Enchantments.SWIFT_SNEAK))
            return;

        final double secSpeed = PlayerMove.getXzSecSpeed(player.lastPos, player.currentPos);
        final double possibleSpeed = SLOW_SPEED.get(itemUseTick) * player.speedMul + AntiCheatConfig.threshold;
        if (secSpeed > possibleSpeed) {
            flag(String.format("Current: %.2f  Max: %.2f", secSpeed, possibleSpeed));
        }
        if (itemUseTick < SLOW_SPEED.size() - 1) itemUseTick++;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.noSlowAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.noSlowACheck;
    }
}
