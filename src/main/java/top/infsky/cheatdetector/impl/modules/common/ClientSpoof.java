package top.infsky.cheatdetector.impl.modules.common;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.mixins.ConnectionAccessor;
import top.infsky.cheatdetector.utils.TRSelf;

public class ClientSpoof extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public ClientSpoof(@NotNull TRSelf player) {
        super("ClientSpoof", player);
        instance = this;
    }

    /**
     * {@link ClientPacketListener#handleLogin(ClientboundLoginPacket)}
     * 修改第444行的发包行为
     */
    @Override
    public boolean _onPacketSend(@NotNull Packet<?> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (basePacket instanceof ServerboundCustomPayloadPacket packet
                && packet.getIdentifier() == ServerboundCustomPayloadPacket.BRAND) {
            ci.cancel();
            ((ConnectionAccessor) connection).sendPacket(
                    new ServerboundCustomPayloadPacket(
                            ServerboundCustomPayloadPacket.BRAND,
                            new FriendlyByteBuf(Unpooled.buffer()).writeUtf(Advanced3Config.clientSpoofBrand)
                    ), listener
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.clientSpoofEnabled;
    }
}
