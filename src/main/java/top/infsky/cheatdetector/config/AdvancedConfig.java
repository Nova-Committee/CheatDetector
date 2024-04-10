package top.infsky.cheatdetector.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Setter
@Config(name = "Advanced")
public class AdvancedConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    boolean blinkCheck = true;
    int blinkAlertBuffer = 20;
    double blinkMaxDistance = 1;

    
    @ConfigEntry.Gui.PrefixText
    boolean flightACheck = true;
    int flightAAlertBuffer = 20;
    int flightAOnGroundJumpTick = 1;
    int flightAInLiquidLiquidTick = 8;
    int flightAInHurtJumpTick = 6;
    double flightAJumpDistance = 1.25219;
    double flightAFromWaterYDistance = 0.5;
    int flightAOnTeleportDisableTick = 2;
    int flightAOnJumpJumpTick = 14;

    
    @ConfigEntry.Gui.PrefixText
    boolean flightBCheck = true;
    int flightBAlertBuffer = 1;

    
    @ConfigEntry.Gui.PrefixText
    boolean gameModeACheck = true;
    int gameModeAAlertBuffer = 1;

    
    @ConfigEntry.Gui.PrefixText
    boolean highJumpACheck = true;
    int highJumpAAlertBuffer = 10;
    double highJumpAJumpDistance = 1.25219;

    
    @ConfigEntry.Gui.PrefixText
    boolean noSlowACheck = true;
    int noSlowAAlertBuffer = 20;
    double noSlowASpeedTick1 = 2.56;
    double noSlowASpeedTick2 = 1.92;
    double noSlowASpeedTick3 = 1.6;
    double noSlowASpeedTick4 = 1.4;
    double noSlowASpeedTick5 = 1.36;
    double noSlowASpeedTick6 = 1.26;
    double noSlowASpeedTick7 = 1.18;
    double noSlowASpeedTick8 = 1.16;
    int noSlowAInJumpDisableTick = 4;

    
    @ConfigEntry.Gui.PrefixText
    boolean speedACheck = true;
    int speedAAlertBuffer = 30;
    int speedAAfterJumpJumpTick = 10;
    double speedAAfterJumpSpeed = 7.4;
    double speedASprintingSpeed = 5.612;
    double speedASilentSpeed = 1.295;
    double speedAWalkSpeed = 4.317;

    
    @ConfigEntry.Gui.PrefixText
    boolean speedBCheck = true;
    int speedBAlertBuffer = 10;

    
    @ConfigEntry.Gui.PrefixText
    boolean velocityACheck = true;
    int velocityAAlertBuffer = 2;
    int velocityAExtraDelayedMs = 50;

    public short getFlightAOnGroundJumpTick() {
        return (short) flightAOnGroundJumpTick;
    }

    public short getFlightAInLiquidLiquidTick() {
        return (short) flightAInLiquidLiquidTick;
    }

    public short getFlightAInHurtJumpTick() {
        return (short) flightAInHurtJumpTick;
    }

    public short getFlightAOnTeleportDisableTick() {
        return (short) flightAOnTeleportDisableTick;
    }

    public short getFlightAOnJumpJumpTick() {
        return (short) flightAOnJumpJumpTick;
    }

    public short getNoSlowAInJumpDisableTick() {
        return (short) noSlowAInJumpDisableTick;
    }

    public short getSpeedAAfterJumpJumpTick() {
        return (short) speedAAfterJumpJumpTick;
    }

}
