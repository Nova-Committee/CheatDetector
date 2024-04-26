package top.infsky.cheatdetector.anticheat.modules;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.anticheat.utils.OutgoingPacket;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class Blink extends Module {
    private int lastPacketUnSend = 0;
    private final Deque<OutgoingPacket> outgoingPackets = new LinkedBlockingDeque<>();

    public Blink( @NotNull TRSelf player) {
        super("Blink", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            while (!outgoingPackets.isEmpty()) {
                final OutgoingPacket packet = outgoingPackets.poll();
                if (!Advanced3Config.blinkCancelPacket) packet.connection().send(packet.packet(), packet.listener());
            }
        } else {
            if (Advanced3Config.blinkAutoSendMs >= 0) {
                while (!outgoingPackets.isEmpty()) {
                    final OutgoingPacket packet = outgoingPackets.getLast();
                    if (player.getUpTime() < packet.sentTime() + Math.round(Advanced3Config.blinkAutoSendMs / 50.0)) {
                        break;
                    }
                    if (!Advanced3Config.blinkCancelPacket) {
                        ((ConnectionInvoker) packet.connection()).sendPacket(packet.packet(), packet.listener());
                    }
                    outgoingPackets.remove(packet);
                }
            }
        }
    }

    @Override
    public boolean _handlePacketSend(Packet<?> packet, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;

        if (Advanced3Config.blinkIncludeOutgoing) {
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
    public void _onTeleport() {
        if (isDisabled()) return;

        if (Advanced3Config.blinkAutoDisable) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("blinkEnabled", false);
            ClickGUI.update();
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.blinkEnabled;
    }
}
