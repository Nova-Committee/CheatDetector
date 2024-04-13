package top.infsky.cheatdetector.anticheat;

import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.checks.*;
import top.infsky.cheatdetector.anticheat.fixs.FlagDetector;
import top.infsky.cheatdetector.anticheat.fixs.Spin;
import top.infsky.cheatdetector.anticheat.fixs.themis.FastPlace;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket1;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.BadPacket2;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.Movement;
import top.infsky.cheatdetector.anticheat.fixs.vulcan.OmniSprint;

import java.util.HashMap;
import java.util.Map;

public class CheckManager {
    public final TRPlayer player;
    public Map<Class<? extends Check>, Check> checks = new HashMap<>();
    public Map<Class<? extends Check>, Check> preChecks = new HashMap<>();
    public Map<Class<? extends Check>, Check> normalChecks = new HashMap<>();
    public Map<Class<? extends Check>, Check> postChecks = new HashMap<>();
    public short disableTick;
    public CheckManager(Map<Class<? extends Check>, Check> preChecks,
                        Map<Class<? extends Check>, Check> normalChecks,
                        Map<Class<? extends Check>, Check> postChecks, TRPlayer player) {
        this.player = player;
        this.preChecks.putAll(preChecks);
        this.normalChecks.putAll(normalChecks);
        this.postChecks.putAll(postChecks);
        this.checks.putAll(preChecks);
        this.checks.putAll(normalChecks);
        this.checks.putAll(postChecks);
        this.disableTick = 10;
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(TRPlayer player) {
        final Map<Class<? extends Check>, Check> pre = new HashMap<>();
        final Map<Class<? extends Check>, Check> normal = new HashMap<>();
        final Map<Class<? extends Check>, Check> post = new HashMap<>();
        pre.put(GroundSpoofA.class, new GroundSpoofA(player));
        normal.put(FlightA.class, new FlightA(player));
        normal.put(BlinkA.class, new BlinkA(player));
        normal.put(SpeedA.class, new SpeedA(player));
        normal.put(SpeedB.class, new SpeedB(player));
        normal.put(HighJumpA.class, new HighJumpA(player));
        normal.put(NoSlowA.class, new NoSlowA(player));
        normal.put(GameModeA.class, new GameModeA(player));
        normal.put(FlightB.class, new FlightB(player));
        normal.put(VelocityA.class, new VelocityA(player));

        final CheckManager checkManager = new CheckManager(pre, normal, post, player);
        checkManager.onTeleport();
        return checkManager;
    }

    public static @NotNull CheckManager createSelf(TRPlayer player) {
        final Map<Class<? extends Check>, Check> pre = new HashMap<>();
        final Map<Class<? extends Check>, Check> normal = new HashMap<>();
        final Map<Class<? extends Check>, Check> post = new HashMap<>();
        pre.put(GroundSpoofA.class, new GroundSpoofA(player));
        normal.put(FlightA.class, new FlightA(player));
        normal.put(BlinkA.class, new BlinkA(player));
        normal.put(SpeedA.class, new SpeedA(player));
        normal.put(SpeedB.class, new SpeedB(player));
        normal.put(HighJumpA.class, new HighJumpA(player));
        normal.put(NoSlowA.class, new NoSlowA(player));
        normal.put(GameModeA.class, new GameModeA(player));
        normal.put(FlightB.class, new FlightB(player));
        normal.put(VelocityA.class, new VelocityA(player));
        pre.put(BadPacket1.class, new BadPacket1(player));
        pre.put(BadPacket2.class, new BadPacket2(player));
        pre.put(Movement.class, new Movement(player));
        post.put(FlagDetector.class, new FlagDetector(player));
        pre.put(FastPlace.class, new FastPlace(player));
        pre.put(OmniSprint.class, new OmniSprint(player));
        post.put(Spin.class, new Spin(player));

        final CheckManager checkManager = new CheckManager(pre, normal, post, player);
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
        if (player.lastOnGround && !player.currentOnGround) onJump();

        for (Check check : preChecks.values()) check._onTick();
        for (Check check : normalChecks.values()) check._onTick();
        for (Check check : postChecks.values()) check._onTick();
    }

    public void onTeleport() {
        for (Check check : preChecks.values()) check._onTeleport();
        for (Check check : normalChecks.values()) check._onTeleport();
        for (Check check : postChecks.values()) check._onTeleport();
    }

    public void onJump() {
        player.jumping = true;
        for (Check check : preChecks.values()) check._onJump();
        for (Check check : normalChecks.values()) check._onJump();
        for (Check check : postChecks.values()) check._onJump();
    }
}
