package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.packet.PacketHandler;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.modules.ClickGUI;
import top.infsky.cheatdetector.impl.utils.packet.IncomingPacket;
import top.infsky.cheatdetector.impl.utils.packet.OutgoingPacket;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

public class Blink extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private int lastPacketUnSend = 0;
    private int lastPacketUnReceive = 0;
    private int disablePackets = 0;
    private final PacketHandler packetHandler;

    public Blink( @NotNull TRSelf player) {
        super("Blink", player);
        packetHandler = new PacketHandler(player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            packetHandler.releaseAll(Advanced3Config.blinkCancelPacket);
        }
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> packet, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (disablePackets > 0) {
            disablePackets--;
            return false;
        }
        if (isDisabled() || isDied() || ci.isCancelled()) return false;

        if (Advanced3Config.blinkIncludeOutgoing) {
            ci.cancel();
            packetHandler.add(new OutgoingPacket(packet, connection, listener, player.getUpTime()));
            if (Advanced3Config.blinkShowCount) {
                final int currentPacketUnSend = packetHandler.getDelayedOutgoingCount();
                if (currentPacketUnSend != lastPacketUnSend) moduleMsg(lastPacketUnSend + " packets to send.");
                lastPacketUnSend = currentPacketUnSend;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> packet, Connection connection, ChannelHandlerContext context, CallbackInfo ci) {
        if (disablePackets > 0) {
            disablePackets--;
            return false;
        }
        if (isDisabled() || isDied() || ci.isCancelled()) return false;

        if (Advanced3Config.blinkIncludeInComing) {
            ci.cancel();
            packetHandler.add(new IncomingPacket(packet, connection, context, player.getUpTime()));
            if (Advanced3Config.blinkShowCount) {
                final int currentPacketUnReceive = packetHandler.getDelayedIncomingCount();
                if (currentPacketUnReceive != lastPacketUnReceive) moduleMsg(lastPacketUnReceive + " packets to receive.");
                lastPacketUnReceive = currentPacketUnReceive;
            }
            return true;
        }
        return false;
    }

    @Override
    public void _onTeleport() {
        if (isDisabled()) return;

        if (Advanced3Config.blinkAutoDisable) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("blinkEnabled", false);
            ClickGUI.update();
        }
    }

    public boolean isDied() {
        if (player.fabricPlayer.isDeadOrDying() || player.fabricPlayer.getHealth() <= 0) {
            onDied();
            return true;
        }
        return false;
    }

    private void onDied() {
        lastPacketUnSend = 0;
        lastPacketUnReceive = 0;
        disablePackets = 20;
    }
    @Override
    public boolean isDisabled() {
        return !ModuleConfig.blinkEnabled;
    }
}
