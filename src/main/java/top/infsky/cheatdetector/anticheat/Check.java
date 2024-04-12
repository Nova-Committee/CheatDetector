package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.utils.LogUtils;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

@Getter
public abstract class Check {
    protected final TRPlayer player;
    public String checkName;
    public long violations;

    public Check(String checkName, @NotNull TRPlayer player) {
        this.checkName = checkName;
        this.player = player;
    }

    protected long getAlertBuffer() {
        return 1;
    }
    protected boolean isDisabled() {
        return false;
    }

    public void flag() {
        if (!CONFIG().getAntiCheat().isAntiCheatEnabled()) return;
        if (isDisabled()) return;
        if (CONFIG().getAntiCheat().isDisableSelfCheck() && player.equals(TRPlayer.SELF)) return;
        violations++;
        if (!CONFIG().getAlert().isDisableBuffer())
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s)", violations));
    }

    public void flag(String extraMsg) {
        if (!CONFIG().getAntiCheat().isAntiCheatEnabled()) return;
        if (isDisabled()) return;
        if (CONFIG().getAntiCheat().isDisableSelfCheck() && player.equals(TRPlayer.SELF)) return;
        violations++;
        if (!CONFIG().getAlert().isDisableBuffer())
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
    public boolean _handleUseItemOn(ServerboundUseItemOnPacket packet, CallbackInfo ci) { return false; }
    public boolean _handleMovePlayer(ServerboundMovePlayerPacket packet, Connection connection, PacketSendListener listener, CallbackInfo ci) { return false; }
}
