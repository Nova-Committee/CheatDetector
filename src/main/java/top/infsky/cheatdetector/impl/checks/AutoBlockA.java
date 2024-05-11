package top.infsky.cheatdetector.impl.checks;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.AdvancedConfig;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.utils.TRPlayer;

public class AutoBlockA extends Check {
    public AutoBlockA(@NotNull TRPlayer player) {
        super("AutoBlockA", player);
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (basePacket instanceof ClientboundAnimatePacket packet) {
            if (packet.getId() != player.fabricPlayer.getId()) return false;
            if (packet.getAction() != ClientboundAnimatePacket.SWING_MAIN_HAND) return false;

            if (player.fabricPlayer.pick(4.5, 0, false).getType() != HitResult.Type.MISS) return false;  // 1.7/Visual 1.7允许玩家对着方块一边挥手一边使用

            if (player.fabricPlayer.getMainHandItem().getItem() instanceof SwordItem && player.fabricPlayer.getOffhandItem().is(Items.SHIELD))  // viaVersion一般会把其他玩家显示为持有盾牌，而不是格挡
                flag("impossible hit.");
        }
        return false;
    }

    @Override
    public int getAlertBuffer() {
        return AdvancedConfig.autoBlockAAlertBuffer;
    }

    @Override
    public boolean isDisabled() {
        return !AdvancedConfig.autoBlockACheck;
    }
}
