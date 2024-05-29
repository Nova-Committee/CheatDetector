package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
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

        if (basePacket instanceof ClientboundSetEntityMotionPacket packet) {
            customMsg("SetMotion: entity:%s x:%.2f y:%.2f z:%.2f".formatted(
                    Objects.requireNonNull(LevelUtils.getClientLevel().getEntity(packet.getId())).getName().getString(),
                    ((double) packet.getXa()) / 8000, ((double) packet.getYa()) / 8000, ((double) packet.getZa()) / 8000));
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.debugEnabled;
    }
}
