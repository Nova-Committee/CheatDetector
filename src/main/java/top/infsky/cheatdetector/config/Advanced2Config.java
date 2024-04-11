package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Setter
@Config(name = "Advanced2")
public class Advanced2Config implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    long flagDetectorAlertBuffer = 1;
    int flagDetectorWorldChangedDisableTick = 60;
    boolean flagDetectorShouldReduceKnownTeleport = true;

    @ConfigEntry.Gui.PrefixText
    boolean badPacket1Enabled = true;
    long badPacket1AlertBuffer = 20;

    @ConfigEntry.Gui.PrefixText
    boolean badPacket2Enabled = true;
    long badPacket2AlertBuffer = 20;

    @ConfigEntry.Gui.PrefixText
    boolean movementDisablerShowPacketSend = false;

    @ConfigEntry.Gui.PrefixText
    boolean fastPlaceEnabled = true;
    long fastPlaceAlertBuffer = 100;
    int fastPlaceSamePlaceMinDelay = 1;

    public short getFastPlaceSamePlaceMinDelay() {
        return (short) fastPlaceSamePlaceMinDelay;
    }
}
