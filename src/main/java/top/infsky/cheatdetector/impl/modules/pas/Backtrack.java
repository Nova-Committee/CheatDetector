package top.infsky.cheatdetector.impl.modules.pas;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.modules.ClickGUI;
import top.infsky.cheatdetector.impl.utils.world.FakePlayer;
import top.infsky.cheatdetector.impl.utils.packet.IncomingPacket;
import top.infsky.cheatdetector.impl.utils.packet.PacketUtils;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.ConnectionInvoker;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class Backtrack extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private int lastPacketUnReceive = 0;
    private int disablePackets = 0;
    private final Deque<IncomingPacket> incomingPackets = new LinkedBlockingDeque<>();
    @Nullable
    private Player target;
    @Nullable
    private FakePlayer targetVisual;
    @Nullable
    private Player lastTarget;

    public Backtrack(@NotNull TRSelf player) {
        super("Backtrack", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.poll();
                if (!Advanced3Config.backtrackCancelPacket) ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
            }
        } else {
            if (target != null && target != lastTarget)  // on attack new target
                player.fabricPlayer.connection.handleSetEntityMotion(new ClientboundSetEntityMotionPacket(target.getId(), new Vec3(0, 0, 0)));
            while (!incomingPackets.isEmpty()) {
                final IncomingPacket packet = incomingPackets.getLast();
                if (player.getUpTime() < packet.sentTime() + Math.round(Advanced3Config.backtrackDelayMs / 50.0)) {
                    break;
                }
                ((ConnectionInvoker) packet.connection()).channelRead0(packet.context(), packet.packet());
                incomingPackets.remove(packet);
            }
        }
        lastTarget = target;
    }

    @Override
    public boolean _handleAttack(Entity entity) {
        if (isDisabled() || isDied()) return false;

        if (entity instanceof Player targetPlayer) {
            if (!targetPlayer.equals(target)) {
                target = targetPlayer;
                if (Advanced3Config.backtrackRenderRealPosition) {
                    if (targetVisual != null)
                        targetVisual.hide();
                    targetVisual = null;
                    targetVisual = new FakePlayer(target);
                    targetVisual.show();
                }
            }
        }
        return false;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> packet, Connection connection, ChannelHandlerContext context, CallbackInfo ci) {
        if (targetVisual != null && (isDisabled() || !Advanced3Config.backtrackRenderRealPosition)) {
            targetVisual.hide();
            targetVisual = null;
        }
        if (disablePackets > 0) {
            disablePackets--;
            return false;
        }
        if (isDisabled() || isDied() || ci.isCancelled()) return false;
        if (!PacketUtils.Backtrack.INCLUDE_PACKETS.contains(packet.getClass())) return false;

        if (Advanced3Config.backtrackRenderRealPosition && targetVisual != null) {
            targetVisual.handlePacket(packet);
        }

        if (PacketUtils.Backtrack.DELAY_PACKETS.contains(packet.getClass())) {
            ci.cancel();
            incomingPackets.addFirst(new IncomingPacket(packet, connection, context, player.getUpTime()));
            if (Advanced3Config.backtrackShowCount) {
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
        incomingPackets.clear();
        lastPacketUnReceive = 0;
        disablePackets = 20;
    }

    @Override
    public boolean isDisabled() {
        if (!ModuleConfig.backtrackEnabled) return true;
        if (ModuleConfig.fakelagEnabled) {
            customMsg(Component.translatable("cheatdetector.chat.alert.fakelagAndBacktrack").withStyle(ChatFormatting.DARK_RED).getString());
            CheatDetector.CONFIG_HANDLER.configManager.setValue("backtrackEnabled", false);
            return true;
        }
        return !ModuleConfig.aaaPASModeEnabled;
    }
}
