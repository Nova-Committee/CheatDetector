package top.infsky.cheatdetector.anticheat;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.utils.LogUtils;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class Fix extends Check {
    public Fix(String checkName, @NotNull TRPlayer player) {
        super(checkName, player);
    }

    @Override
    public void flag() {
        if (!CONFIG().getFixes().isPacketFixEnabled()) return;
        if (isDisabled()) return;
        violations++;
        if (!CONFIG().getAlert().isDisableBuffer())
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s)", violations));
    }

    public void flag(String extraMsg) {
        if (!CONFIG().getFixes().isPacketFixEnabled()) return;
        if (isDisabled()) return;
        violations++;
        if (!CONFIG().getAlert().isDisableBuffer())
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s) %s%s", violations, ChatFormatting.GRAY, extraMsg));
    }
}
