package top.infsky.cheatdetector.impl.modules.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.modules.ClickGUI;
import top.infsky.cheatdetector.impl.utils.packet.IncomingPacket;
import top.infsky.cheatdetector.impl.utils.packet.OutgoingPacket;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class Fakelag extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private int lastPacketUnSend = 0;
    private int lastPacketUnReceive = 0;
    private int disablePackets = 0;
    private final Deque<OutgoingPacket> outgoingPackets = new LinkedBlockingDeque<>();
    private final Deque<IncomingPacket> incomingPackets = new LinkedBlockingDeque<>();

    public Fakelag(@NotNull TRSelf player) {
        super("Fakelag", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            while (!outgoingPackets.isEmpty()) {
                final OutgoingPacket packet = outgoingPackets.poll();
                packet.connection().send(packet.packet(), packet.listener());
            }
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.poll();
                ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
            }
        } else {
            while (!outgoingPackets.isEmpty()) {
                final OutgoingPacket packet = outgoingPackets.getLast();
                if (player.getUpTime() < packet.sentTime() + Math.round(Advanced3Config.fakelagDelayMs / 50.0)) {
                    break;
                }
                ((ConnectionInvoker) packet.connection()).sendPacket(packet.packet(), packet.listener(), true);
                outgoingPackets.remove(packet);
            }
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.getLast();
                if (player.getUpTime() < packet.sentTime() + Math.round(Advanced3Config.fakelagDelayMs / 50.0)) {
                    break;
                }
                ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
                incomingPackets.remove(packet);
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

        if (Advanced3Config.fakelagIncludeOutgoing) {
            if (packet instanceof ServerboundClientCommandPacket packet1) {
                if (packet1.getAction().equals(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN)) {
                    onDied();
                    return false;
                }
            }

            ci.cancel();
            outgoingPackets.addFirst(new OutgoingPacket(packet, connection, listener, player.getUpTime()));
            if (Advanced3Config.fakelagShowCount) {
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

        if (Advanced3Config.fakelagIncludeIncoming) {
            ci.cancel();
            incomingPackets.addFirst(new IncomingPacket(packet, connection, context, player.getUpTime()));
            if (Advanced3Config.fakelagShowCount) {
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

        if (Advanced3Config.fakelagAutoDisable) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("fakelagEnabled", false);
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
        if (!ModuleConfig.fakelagEnabled) return true;
        if (ModuleConfig.backtrackEnabled) {
            customMsg(Component.translatable("cheatdetector.chat.alert.fakelagAndBacktrack").withStyle(ChatFormatting.DARK_RED).getString());
            CheatDetector.CONFIG_HANDLER.configManager.setValue("fakelagEnabled", false);
            return true;
        }
        return false;
    }
}
