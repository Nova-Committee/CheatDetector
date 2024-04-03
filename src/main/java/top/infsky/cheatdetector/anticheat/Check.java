package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public final void flag(String extraMsg) {
        violations++;
        if (!CONFIG().isDisableBuffer())
            if (violations % CONFIG().getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s) %s", violations, extraMsg));
    }

    public void _onTick() {}
    public void _onTeleport() {}
    public void _onJump() {}
}
