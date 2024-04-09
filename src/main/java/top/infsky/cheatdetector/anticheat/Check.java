package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
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

    public final void flag() {
        if (!CONFIG().getAntiCheat().isAntiCheatEnabled()) return;
        if (CONFIG().getAntiCheat().isDisableSelfCheck() && player.equals(TRPlayer.SELF)) return;
        violations++;
        if (!CONFIG().getAlert().isDisableBuffer())
            if (violations % CONFIG().getAlert().getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s)", violations));
    }

    public final void flag(boolean bypassBuffer) {
        if (!CONFIG().getAntiCheat().isAntiCheatEnabled()) return;
        if (CONFIG().getAntiCheat().isDisableSelfCheck() && player.equals(TRPlayer.SELF)) return;
        if (bypassBuffer) {
            violations++;
            LogUtils.alert(player.fabricPlayer.getName().getString(), ChatFormatting.DARK_RED + checkName, String.format("(VL:%s)", violations));
        }
    }

    public final void flag(String extraMsg) {
        if (!CONFIG().getAntiCheat().isAntiCheatEnabled()) return;
        if (CONFIG().getAntiCheat().isDisableSelfCheck() && player.equals(TRPlayer.SELF)) return;
        violations++;
        if (!CONFIG().getAlert().isDisableBuffer())
            if (violations % CONFIG().getAlert().getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s) %s", violations, extraMsg));
    }

    public final void moduleMsg(String msg) {
        LogUtils.prefix(checkName, msg);
    }

    public final void customMsg(String msg) {
        LogUtils.custom(msg);
    }

    public void _onTick() {}
    public void _onTeleport() {}
    public void _onJump() {}
    public void _onGameTypeChange() {}
    public <T> boolean _onAction(CallbackInfoReturnable<T> cir, T fallbackReturn) {return false;}
    public boolean _onAction(CallbackInfo ci) {return false;}
    public boolean _onAction(InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfo ci) {return false;}
}
