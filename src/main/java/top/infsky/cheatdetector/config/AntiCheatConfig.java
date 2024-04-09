package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Setter
@Config(name = "AntiCheat")
public class AntiCheatConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    boolean antiCheatEnabled = true;

    @ConfigEntry.Gui.Tooltip
    boolean disableSelfCheck = false;

    @ConfigEntry.Gui.Tooltip
    double threshold = 1.0;

    @ConfigEntry.Gui.Tooltip
    long VLClearTime = 6000;
}
