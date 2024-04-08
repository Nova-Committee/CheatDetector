package top.infsky.cheatdetector.anticheat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.checks.*;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket1;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket2;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.MovementDisabler;

import java.util.HashMap;
import java.util.Map;

public class CheckManager {
    public final TRPlayer player;
    public Map<Class<? extends Check>, Check> checks = new HashMap<>();
    public short disableTick;
    public CheckManager(Map<Class<? extends Check>, Check> checks, TRPlayer player) {
        this.player = player;
        this.checks.putAll(checks);
        this.disableTick = 10;
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(TRPlayer player) {
        final CheckManager checkManager = new CheckManager(Map.of(
                FlightA.class, new FlightA(player),
                BlinkA.class, new BlinkA(player),
                SpeedA.class, new SpeedA(player),
                SpeedB.class, new SpeedB(player),
                HighJumpA.class, new HighJumpA(player),
                NoSlowA.class, new NoSlowA(player),
                CreativeModeA.class, new CreativeModeA(player)
        ), player);
        checkManager.onTeleport();
        return checkManager;
    }

    public static @NotNull CheckManager createSelf(TRPlayer player) {
        final CheckManager checkManager = new CheckManager(Map.of(
                FlightA.class, new FlightA(player),
                BlinkA.class, new BlinkA(player),
                SpeedA.class, new SpeedA(player),
                SpeedB.class, new SpeedB(player),
                HighJumpA.class, new HighJumpA(player),
                NoSlowA.class, new NoSlowA(player),
                CreativeModeA.class, new CreativeModeA(player),
                BadPacket1.class, new BadPacket1(player),
                BadPacket2.class, new BadPacket2(player),
                MovementDisabler.class, new MovementDisabler(player)
        ), player);
        checkManager.onTeleport();
        return checkManager;
    }

    public void update() {
        if (disableTick > 0) {
            disableTick--;
            return;
        }
        if (player.currentGameType != player.lastGameType) {
            for (Check check : checks.values()) check._onGameTypeChange();
        }

        if (player.fabricPlayer.isSpectator() || player.fabricPlayer.isCreative()) return;
        if (player.lastOnGround && !player.fabricPlayer.onGround()) onJump();

        for (Check check : checks.values()) {
            check._onTick();
        }
    }

    public void onTeleport() {
        for (Check check : checks.values()) {
            check._onTeleport();
        }
    }

    public void onJump() {
        player.jumping = true;
        for (Check check : checks.values()) {
            check._onJump();
        }
    }
}
