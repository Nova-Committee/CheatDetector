package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import top.infsky.cheatdetector.CheatDetector;

@Getter
@Setter
@Config(name = CheatDetector.MOD_ID)
public class ModConfig implements ConfigData {
    boolean enabled = true;
    boolean disableSelfCheck = false;
    boolean allowAlertPlayers = true;
    boolean disableBuffer = false;
    long alertBuffer = 5;
    double threshold = 0.001;
}
