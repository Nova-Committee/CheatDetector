package top.infsky.cheatdetector.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.config.AlertConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;
import top.infsky.cheatdetector.utils.LogUtils;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

@Getter
public abstract class Check {
    protected final @NotNull TRPlayer player;
    public String checkName;
    public int violations;

    public Check(String checkName, @NotNull TRPlayer player) {
        this.checkName = checkName;
        this.player = player;
    }

    public int getAlertBuffer() {
        return 1;
    }
    public boolean isDisabled() {
        return false;
    }

    public void flag() {
        if (!AntiCheatConfig.antiCheatEnabled) return;
        if (isDisabled()) return;
        if (AntiCheatConfig.disableSelfCheck && player.equals(TRSelf.getInstance())) return;
        violations++;
        if (!AlertConfig.disableBuffer)
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s)", violations));
    }

    public void flag(String extraMsg) {
        if (!AntiCheatConfig.antiCheatEnabled) return;
        if (isDisabled()) return;
        if (AntiCheatConfig.disableSelfCheck && player.equals(TRSelf.getInstance())) return;
        violations++;
        if (!AlertConfig.disableBuffer)
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s) %s%s", violations, ChatFormatting.GRAY, extraMsg));
    }

    public void moduleMsg(String msg) {
        LogUtils.prefix(checkName, msg);
    }

    public void customMsg(String msg) {
        LogUtils.custom(msg);
    }

    public void _onTick() {}
    public void _onTeleport() {}
    public void _onJump() {}
    public void _onGameTypeChange() {}
    public <T> boolean _handleStartDestroyBlock(CallbackInfoReturnable<T> cir, T fallbackReturn) { return false; }
    public boolean _handleStopDestroyBlock(CallbackInfo ci) { return false; }
    public boolean _onPacketSend(@NotNull Packet<?> basePacket, Connection connection, PacketSendListener listener, CallbackInfo ci) { return false; }
    public boolean _onPacketReceive(@NotNull Packet<?> basePacket, Connection connection, ChannelHandlerContext channelHandlerContext, CallbackInfo ci) { return false; }
    public boolean _handleAttack(Entity entity) { return false; }
}
