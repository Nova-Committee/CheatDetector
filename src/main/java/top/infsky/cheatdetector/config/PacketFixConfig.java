package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Setter
@Config(name = "PacketFix")
public class PacketFixConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    boolean packetFixEnabled = false;

    @ConfigEntry.Gui.Tooltip
    PacketFixer packetFixMode = PacketFixer.NORMAL;

    @ConfigEntry.Gui.Tooltip
    boolean vulcanDisablerEnabled = false;
}
