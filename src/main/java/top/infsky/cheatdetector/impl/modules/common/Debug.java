package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.HashSet;
import java.util.Set;

public class Debug extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public Debug(@NotNull TRSelf player) {
        super("Debug", player);
        instance = this;
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (basePacket instanceof ServerboundInteractPacket packet) {
            customMsg("send ServerboundInteractPacket.");
        } else if (basePacket instanceof ServerboundSelectTradePacket packet) {
            customMsg("send ServerboundSelectTradePacket(item=%s)".formatted(packet.getItem()));
        } else if (basePacket instanceof ServerboundContainerClickPacket packet) {
            customMsg("send ServerboundContainerClickPacket(containerId=%s,stateId=%s,slotNum=%s,clickType=%s,itemStack=%s,changedSlots%s)".formatted(
                    packet.getContainerId(), packet.getStateId(), packet.getButtonNum(), packet.getClickType(),
                    packet.getCarriedItem().getItem().toString(), packet.getChangedSlots()
            ));
        }
        return false;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> packet, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.debugEnabled;
    }
}
