package top.infsky.cheatdetector.impl.utils.packet;

import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class PacketHandler {
    protected TRSelf player;
    private final Deque<OutgoingPacket> outgoingPackets;
    private final Deque<IncomingPacket> incomingPackets;

    public PacketHandler(TRSelf player) {
        this.player = player;
        this.outgoingPackets = new LinkedBlockingDeque<>();
        this.incomingPackets = new LinkedBlockingDeque<>();
    }

    public void tick(int delay) {
        if (!CheatDetector.inWorld || player.fabricPlayer.isDeadOrDying()) {
            releaseAll(true);
            return;
        }

        while (!outgoingPackets.isEmpty()) {
            final OutgoingPacket packet = outgoingPackets.getLast();
            if (player.getUpTime() < packet.sentTime() + Math.round(delay / 50.0)) {
                break;
            }
            ((ConnectionInvoker) packet.connection()).sendPacket(packet.packet(), packet.listener());
            outgoingPackets.remove(packet);
        }
        while (!incomingPackets.isEmpty()) {
            final IncomingPacket packet = incomingPackets.getLast();
            if (player.getUpTime() < packet.sentTime() + Math.round(delay / 50.0)) {
                break;
            }
            ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
            incomingPackets.remove(packet);
        }
    }

    public void releaseAll() {
        releaseAll(false);
    }

    public void releaseAll(boolean cancel) {
        if (!cancel) {
            while (!outgoingPackets.isEmpty()) {
                final OutgoingPacket packet = outgoingPackets.poll();
                ((ConnectionInvoker) packet.connection()).sendPacket(packet.packet(), packet.listener());
            }
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.poll();
                ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
            }
        }
        outgoingPackets.clear();
        incomingPackets.clear();
    }

    public void add(OutgoingPacket packet) {
        outgoingPackets.add(packet);
    }

    public void add(IncomingPacket packet) {
        incomingPackets.add(packet);
    }

    public int getDelayedIncomingCount() {
        return incomingPackets.size();
    }

    public int getDelayedOutgoingCount() {
        return outgoingPackets.size();
    }
}
