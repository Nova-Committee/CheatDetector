package top.infsky.cheatdetector.anticheat.modules;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.anticheat.utils.IncomingPacket;
import top.infsky.cheatdetector.anticheat.utils.OutgoingPacket;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class Blink extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private int lastPacketUnSend = 0;
    private int lastPacketUnReceive = 0;
    private int disablePackets = 0;
    private final Deque<OutgoingPacket> outgoingPackets = new LinkedBlockingDeque<>();
    private final Deque<IncomingPacket> incomingPackets = new LinkedBlockingDeque<>();

    public Blink( @NotNull TRSelf player) {
        super("Blink", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            while (!outgoingPackets.isEmpty()) {
                final OutgoingPacket packet = outgoingPackets.poll();
                if (!Advanced3Config.blinkCancelPacket) packet.connection().send(packet.packet(), packet.listener());
            }
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.poll();
                ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
            }
        }
    }

    @Override
    public boolean _onPacketSend(Packet<?> packet, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (disablePackets > 0) {
            disablePackets--;
            return false;
        }
        if (isDisabled() || isDied() || ci.isCancelled()) return false;

        if (Advanced3Config.blinkIncludeOutgoing) {
            if (packet instanceof ServerboundClientCommandPacket packet1) {
                if (packet1.getAction().equals(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN)) {
                    onDied();
                    return false;
                }
            }

            ci.cancel();
            outgoingPackets.addFirst(new OutgoingPacket(packet, connection, listener, player.getUpTime()));
            if (Advanced3Config.blinkShowCount) {
                final int currentPacketUnSend = outgoingPackets.size();
                if (currentPacketUnSend != lastPacketUnSend) moduleMsg(lastPacketUnSend + " packets to send.");
                lastPacketUnSend = currentPacketUnSend;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean _onPacketReceive(Packet<?> packet, Connection connection, ChannelHandlerContext context, CallbackInfo ci) {
        if (disablePackets > 0) {
            disablePackets--;
            return false;
        }
        if (isDisabled() || isDied() || ci.isCancelled()) return false;

        if (Advanced3Config.blinkIncludeInComing) {
            ci.cancel();
            incomingPackets.addFirst(new IncomingPacket(packet, connection, context, player.getUpTime()));
            if (Advanced3Config.blinkShowCount) {
                final int currentPacketUnReceive = incomingPackets.size();
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
        outgoingPackets.clear();
        incomingPackets.clear();
        lastPacketUnSend = 0;
        lastPacketUnReceive = 0;
        disablePackets = 20;
    }
    @Override
    public boolean isDisabled() {
        return !ModuleConfig.blinkEnabled;
    }
}
