package top.infsky.cheatdetector.anticheat.fixs.vulcan;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

/**
 * Fix BadPacket1 Check.
 * for Mixin.
 */
public class BadPacket1 extends Check {
    public boolean hasSend = false;

    public BadPacket1() {
        super("BadPacket (Type 1)", TRPlayer.SELF);
    }

    /**
     * @return 是否取消
     */
    public <T> boolean _onAction(CallbackInfoReturnable<T> cir, T fallbackReturn) {
        if (!CONFIG().isPacketFixEnabled()) return false;

        if (!hasSend) {
            hasSend = true;
            return false;
        }

        if (CONFIG().isAllowAlertPacketFix()) flag("Too Many Destroy Packet.");
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
