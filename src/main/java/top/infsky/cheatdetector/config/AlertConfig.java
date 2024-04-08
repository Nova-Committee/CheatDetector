package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Setter
@Config(name = "Alert")
public class AlertConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    boolean allowAlert = true;

    @ConfigEntry.Gui.Tooltip
    boolean allowAlertPacketFix = false;

    @ConfigEntry.Gui.Tooltip
    long alertBuffer = 20;

    @ConfigEntry.Gui.Tooltip
    boolean disableBuffer = false;
}
