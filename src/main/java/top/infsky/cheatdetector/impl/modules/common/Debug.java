package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

public class Debug extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    public Debug(@NotNull TRSelf player) {
        super("Debug", player);
        instance = this;
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<ServerGamePacketListener> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (basePacket instanceof ServerboundEditBookPacket packet) {
            customMsg("slot:%s page:%s title:%s".formatted(packet.getSlot(), packet.getPages(), packet.getTitle()));
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.debugEnabled;
    }
}
