package top.infsky.cheatdetector.anticheat;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.AlertConfig;
import top.infsky.cheatdetector.config.FixesConfig;
import top.infsky.cheatdetector.utils.LogUtils;

public class Fix extends Check {
    public TRSelf player;
    public Fix(String fixName, @NotNull TRSelf player) {
        super(fixName, player);
        this.player = player;
    }

    @Override
    public void flag() {
        if (!FixesConfig.packetFixEnabled) return;
        if (isDisabled()) return;
        violations++;
        if (!AlertConfig.disableBuffer)
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s)", violations));
    }

    @Override
    public void flag(String extraMsg) {
        if (!FixesConfig.packetFixEnabled) return;
        if (isDisabled()) return;
        violations++;
        if (!AlertConfig.disableBuffer)
            if (violations % getAlertBuffer() != 0) return;
        LogUtils.alert(player.fabricPlayer.getName().getString(), checkName, String.format("(VL:%s) %s%s", violations, ChatFormatting.GRAY, extraMsg));
    }
}
