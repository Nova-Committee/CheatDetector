package top.infsky.cheatdetector.impl.utils.packet;

import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.mixins.ConnectionAccessor;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketHandler {
    protected TRSelf player;
    private final Queue<OutgoingPacket> outgoingPackets;
    private final Queue<IncomingPacket> incomingPackets;

    public PacketHandler(TRSelf player) {
        this.player = player;
        this.outgoingPackets = new LinkedBlockingQueue<>();
        this.incomingPackets = new LinkedBlockingQueue<>();
    }

    public void tick(int delay) {
        if (!CheatDetector.inWorld || player.fabricPlayer.isDeadOrDying()) {
            releaseAll(true);
            return;
        }

        while (!outgoingPackets.isEmpty()) {
            final OutgoingPacket packet = outgoingPackets.poll();
            if (player.getUpTime() < packet.sentTime() + Math.round(delay / 50.0)) {
                break;
            }
            ((ConnectionAccessor) packet.connection()).sendPacket(packet.packet(), packet.listener(), true);
            outgoingPackets.remove(packet);
        }
        while (!incomingPackets.isEmpty()) {
            final IncomingPacket packet = incomingPackets.poll();
            if (player.getUpTime() < packet.sentTime() + Math.round(delay / 50.0)) {
                break;
            }
            ((ConnectionAccessor) packet.connection()).channelRead0(packet.context(), packet.packet());
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
                ((ConnectionAccessor) packet.connection()).sendPacket(packet.packet(), packet.listener(), true);
            }
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.poll();
                ((ConnectionAccessor) packet.connection()).channelRead0(packet.context(), packet.packet());
            }
        }
        outgoingPackets.clear();
        incomingPackets.clear();
    }

    public void add(OutgoingPacket packet) {
        outgoingPackets.offer(packet);
    }

    public void add(IncomingPacket packet) {
        incomingPackets.offer(packet);
    }

    public int getDelayedIncomingCount() {
        return incomingPackets.size();
    }

    public int getDelayedOutgoingCount() {
        return outgoingPackets.size();
    }
}
