package top.infsky.cheatdetector.impl.modules.pas.fakelag;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.modules.ClickGUI;
import top.infsky.cheatdetector.impl.utils.packet.IncomingPacket;
import top.infsky.cheatdetector.impl.utils.packet.OutgoingPacket;
import top.infsky.cheatdetector.impl.utils.packet.PacketHandler;
import top.infsky.cheatdetector.utils.TRSelf;

public class FakelagDynamic extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private final PacketHandler packetHandler;
    private int disableTicks = 0;
    private boolean shouldBlink = false;
    private boolean lastHurt = false;
    private long lastStartBlinkTime = -1;
    @Nullable
    private Player target;

    public FakelagDynamic(@NotNull TRSelf player) {
        super("Fakelag", player);
        packetHandler = new PacketHandler(player);
        instance = this;
    }

    private void releasePackets() {
        if (Advanced3Config.fakelagDebug) {
            int currentOutgoing = packetHandler.getDelayedOutgoingCount();
            int currentIncoming = packetHandler.getDelayedIncomingCount();
            if (currentOutgoing != 0) customMsg("send %s packets".formatted(currentOutgoing));
            if (currentIncoming != 0 && !Advanced3Config.fakelagOnlyOutgoing) customMsg("receive %s packets".formatted(currentIncoming));
        }
        packetHandler.releaseAll();
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isDeadOrDying()) {
            onDied();
        }

        if (disableTicks > 0) {
            disableTicks--;
        }

        if (isDisabled()) {
            releasePackets();
            return;
        }

        if (shouldBlink) {
            if (player.upTime - lastStartBlinkTime > Advanced3Config.fakelagMaxLagTicks) {
                if (Advanced3Config.fakelagDebug) customMsg("stop lag: time out.");
                lastStartBlinkTime = player.upTime;
                releasePackets();
            } else if (!lastHurt && player.fabricPlayer.hurtTime > 0 && Advanced3Config.fakelagStopOnHurt) {
                if (Advanced3Config.fakelagDebug) customMsg("stop lag: hurt.");
                shouldBlink = false;
                disableTicks = Advanced3Config.fakelagPauseTicksOnHurt;
                releasePackets();
            }
        }

        if (target != null) {
            if (shouldBlink && player.fabricPlayer.distanceTo(target) < Advanced3Config.fakelagStopRange) {
                if (Advanced3Config.fakelagDebug) customMsg("stop lag: too low range.");
                shouldBlink = false;
                releasePackets();
            } else if (!shouldBlink && player.fabricPlayer.distanceTo(target) > Advanced3Config.fakelagStopRange
                    && player.fabricPlayer.distanceTo(target) < Advanced3Config.fakelagStartRange) {
                if (Advanced3Config.fakelagDebug) customMsg("start lag: in range.");
                lastStartBlinkTime = player.upTime;
                shouldBlink = true;
            } else if (shouldBlink && player.fabricPlayer.distanceTo(target) > Advanced3Config.fakelagStartRange) {
                if (Advanced3Config.fakelagDebug) customMsg("stop lag: out of range.");
                shouldBlink = false;
                releasePackets();
            } else if (player.fabricPlayer.distanceTo(target) > Advanced3Config.fakelagMaxTargetRange) {
                if (Advanced3Config.fakelagDebug) customMsg("release target: %s".formatted(target.getName().getString()));
                target = null;
                shouldBlink = false;
                releasePackets();
            }
        } else shouldBlink = false;

        lastHurt = player.fabricPlayer.hurtTime > 0;
    }

    @Override
    public boolean _handleAttack(Entity entity) {
        if (isDisabled()) return false;

        if (entity instanceof Player targetPlayer) {
            if (!targetPlayer.equals(target)) {
                target = targetPlayer;
                if (Advanced3Config.fakelagDebug) customMsg("new target: %s".formatted(target.getName().getString()));
            } else {
                releasePackets();
            }
        }
        return false;
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> packet, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled() || ci.isCancelled() || !shouldBlink) return false;

        ci.cancel();
        packetHandler.add(new OutgoingPacket(packet, connection, listener, player.getUpTime()));
        return true;
    }

    @Override
    public boolean _onPacketReceive(@NotNull Packet<?> packet, Connection connection, ChannelHandlerContext context, CallbackInfo ci) {
        if (isDisabled() || ci.isCancelled() || !shouldBlink) return false;

        if (!Advanced3Config.fakelagOnlyOutgoing) {
            ci.cancel();
            packetHandler.add(new IncomingPacket(packet, connection, context, player.getUpTime()));
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

    private void onDied() {
        disableTicks = 20;
        shouldBlink = false;
        lastStartBlinkTime = -1;
    }

    @Override
    public boolean isDisabled() {
        if (Advanced3Config.getFakelagMode() != Advanced3Config.FakelagMode.DYNAMIC) return true;
        if (!ModuleConfig.fakelagEnabled || player.fabricPlayer.isPassenger() || disableTicks > 0) return true;
        if (ModuleConfig.backtrackEnabled) {
            customMsg(Component.translatable("cheatdetector.chat.alert.fakelagAndBacktrack").withStyle(ChatFormatting.DARK_RED).getString());
            CheatDetector.CONFIG_HANDLER.configManager.setValue("fakelagEnabled", false);
            return true;
        }
        return false;
    }
}
