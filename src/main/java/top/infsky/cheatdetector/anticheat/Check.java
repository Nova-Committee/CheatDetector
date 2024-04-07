package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
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
        violations++;
        if (!CONFIG().isDisableBuffer())
            if (violations % CONFIG().getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s)", violations));
    }

    public final void flag(boolean bypassBuffer) {
        if (bypassBuffer) {
            violations++;
            LogUtils.alert(player.fabricPlayer.getName().getString(), ChatFormatting.DARK_RED + checkName, String.format("(VL:%s)", violations));
        }
    }

    public final void flag(String extraMsg) {
        violations++;
        if (!CONFIG().isDisableBuffer())
            if (violations % CONFIG().getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s) %s", violations, extraMsg));
    }

    public void _onTick() {}
    public void _onTeleport() {}
    public void _onJump() {}
    public void _onGameTypeChange() {}
}
