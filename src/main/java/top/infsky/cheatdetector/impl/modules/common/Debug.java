package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.Objects;

public class Debug extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public Debug(@NotNull TRSelf player) {
        super("Debug", player);
        instance = this;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<ClientGamePacketListener> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (basePacket instanceof ClientboundEntityEventPacket packet) {
            customMsg("EntityEvent: entity:%s eventId:%s".formatted(Objects.requireNonNull(packet.getEntity(LevelUtils.getClientLevel())).getName().getString(), packet.getEventId()));
        }
        if (basePacket instanceof ClientboundGameEventPacket packet) {
            customMsg("GameEvent: event:%s param:%s".formatted(Objects.requireNonNull(packet.getEvent()), packet.getParam()));
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.debugEnabled;
    }
}
