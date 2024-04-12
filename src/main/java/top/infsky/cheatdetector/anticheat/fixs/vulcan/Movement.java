package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class Movement extends Check {
    public short waitTicks = -1;
    public boolean lastEnabled;  // 上个tick启用状态

    public Movement(@NotNull TRPlayer player) {
        super("Movement", player);
        lastEnabled = CONFIG().getFixes().isVulcanDisablerEnabled();
    }

    @Override
    public void _onTick() {
        // tick before
        if (isDisabled()) {
            if (lastEnabled)
                moduleMsg(ChatFormatting.DARK_RED + "已禁用");
            lastEnabled = false;
            return;
        }
        if (TRPlayer.CLIENT.player == null || TRPlayer.CLIENT.getConnection() == null) { lastEnabled = true; return; }

        // on tick
        // copy from liquid-bounce nextgen :D
        if (checkItem()) {
            if (!lastEnabled) moduleMsg(ChatFormatting.AQUA + "已启用，一秒后生效。");

            if (waitTicks == 0) {
                final Entity entity = TRPlayer.CLIENT.player;
                TRPlayer.CLIENT.getConnection().send(
                        ServerboundInteractPacket.createInteractionPacket(entity, false, InteractionHand.OFF_HAND)
                );
                TRPlayer.CLIENT.getConnection().send(
                        new ServerboundPlayerActionPacket(
                                ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), Direction.DOWN
                        )
                );

                if (CONFIG().getAdvanced2().isMovementShowPacketSend())
                    moduleMsg(ChatFormatting.GRAY + "send packet.");
            }
            waitTicks = 20;
        } else if (!lastEnabled) {  // 玩家刚刚启用这个选项
            CONFIG().getFixes().setVulcanDisablerEnabled(false);
            moduleMsg(ChatFormatting.RED + "模块不可用，请检查是否满足要求。");
        }

        // after tick
        if (waitTicks >= 0) waitTicks--;
        lastEnabled = CONFIG().getFixes().isVulcanDisablerEnabled();
    }

    private boolean checkItem() {
        final ItemStack offhand = player.fabricPlayer.getOffhandItem();
        return offhand.getItem() == Items.TRIDENT && offhand.isEnchanted()
                && EnchantmentHelper.getEnchantments(offhand).containsKey(Enchantments.RIPTIDE);
    }

    @Override
    public boolean isDisabled() {
        return !CONFIG().getFixes().isVulcanDisablerEnabled();
    }
}