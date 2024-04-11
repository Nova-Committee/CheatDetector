package top.infsky.cheatdetector.anticheat;

import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.checks.*;
import top.infsky.cheatdetector.anticheat.fixs.FlagDetector;
import top.infsky.cheatdetector.anticheat.fixs.themis.FastPlace;
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
        final Map<Class<? extends Check>, Check> checks = new HashMap<>();
        checks.put(FlightA.class, new FlightA(player));
        checks.put(BlinkA.class, new BlinkA(player));
        checks.put(SpeedA.class, new SpeedA(player));
        checks.put(SpeedB.class, new SpeedB(player));
        checks.put(HighJumpA.class, new HighJumpA(player));
        checks.put(NoSlowA.class, new NoSlowA(player));
        checks.put(GameModeA.class, new GameModeA(player));
        checks.put(FlightB.class, new FlightB(player));
        checks.put(VelocityA.class, new VelocityA(player));

        final CheckManager checkManager = new CheckManager(checks, player);
        checkManager.onTeleport();
        return checkManager;
    }

    public static @NotNull CheckManager createSelf(TRPlayer player) {
        final Map<Class<? extends Check>, Check> checks = new HashMap<>();
        checks.put(FlightA.class, new FlightA(player));
        checks.put(BlinkA.class, new BlinkA(player));
        checks.put(SpeedA.class, new SpeedA(player));
        checks.put(SpeedB.class, new SpeedB(player));
        checks.put(HighJumpA.class, new HighJumpA(player));
        checks.put(NoSlowA.class, new NoSlowA(player));
        checks.put(GameModeA.class, new GameModeA(player));
        checks.put(FlightB.class, new FlightB(player));
        checks.put(VelocityA.class, new VelocityA(player));
        checks.put(BadPacket1.class, new BadPacket1(player));
        checks.put(BadPacket2.class, new BadPacket2(player));
        checks.put(MovementDisabler.class, new MovementDisabler(player));
        checks.put(FlagDetector.class, new FlagDetector(player));
        checks.put(FastPlace.class, new FastPlace(player));

        final CheckManager checkManager = new CheckManager(checks, player);
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

        if (player.currentGameType == GameType.CREATIVE || player.currentGameType == GameType.SPECTATOR) return;
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
