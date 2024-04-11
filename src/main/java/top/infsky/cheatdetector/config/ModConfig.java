package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import top.infsky.cheatdetector.CheatDetector;

@Getter
@Setter
@Config(name = CheatDetector.MOD_ID)
public class ModConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("AntiCheat")
    @ConfigEntry.Gui.TransitiveObject
    AntiCheatConfig antiCheat = new AntiCheatConfig();

    @ConfigEntry.Category("Fixes")
    @ConfigEntry.Gui.TransitiveObject
    fixesConfig fixes = new fixesConfig();

    @ConfigEntry.Category("Alert")
    @ConfigEntry.Gui.TransitiveObject
    AlertConfig alert = new AlertConfig();

    @ConfigEntry.Category("Advanced")
    @ConfigEntry.Gui.TransitiveObject
    AdvancedConfig advanced = new AdvancedConfig();

    @ConfigEntry.Category("Advanced2")
    @ConfigEntry.Gui.TransitiveObject
    Advanced2Config advanced2 = new Advanced2Config();
}
