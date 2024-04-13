package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Setter
@Config(name = "PacketFix")
public class FixesConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    boolean packetFixEnabled = false;

    @ConfigEntry.Gui.Tooltip
    Fixes packetFixMode = Fixes.NORMAL;

    @ConfigEntry.Gui.Tooltip
    boolean vulcanDisablerEnabled = false;

    @ConfigEntry.Gui.Tooltip
    boolean flagDetectorEnabled = false;

    @ConfigEntry.Gui.Tooltip
    boolean vulcanOmniSprintEnabled = false;

    @ConfigEntry.Gui.Tooltip
    boolean spinEnabled = false;
}
