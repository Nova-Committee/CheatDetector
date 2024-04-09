package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

/**
 * Fix BadPacket1 Check.
 */
public class BadPacket1 extends Check {
    public boolean hasSend = false;

    public BadPacket1(@NotNull TRPlayer player) {
        super("BadPacket (Type 1)", player);
    }

    /**
     * @return 是否取消
     */
    @Override
    public <T> boolean _onAction(CallbackInfoReturnable<T> cir, T fallbackReturn) {
        if (!CONFIG().getFixes().isPacketFixEnabled()) return false;

        if (!hasSend) {
            hasSend = true;
            return false;
        }

        if (CONFIG().getAlert().isAllowAlertPacketFix()) flag("Too many start dig Packet.");
        hasSend = false;
        cir.setReturnValue(fallbackReturn);
        cir.cancel();
        return true;
    }

    @Override
    public void _onTick() {
        hasSend = false;
    }
}
