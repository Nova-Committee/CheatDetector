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
    boolean antiCheatEnabled = true;
    boolean disableSelfCheck = false;
    double threshold = 0.1;

    boolean packetFixEnabled = true;
    PacketFixer packetFixMode = PacketFixer.NORMAL;

    boolean allowAlert = true;
    boolean allowAlertPacketFix = false;
    long alertBuffer = 10;
    boolean disableBuffer = false;
}
