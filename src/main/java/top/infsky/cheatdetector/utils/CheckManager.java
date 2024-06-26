package top.infsky.cheatdetector.utils;

import lombok.Getter;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.impl.checks.aim.AimA;
import top.infsky.cheatdetector.impl.checks.aim.AimB;
import top.infsky.cheatdetector.impl.checks.aim.InvalidPitch;
import top.infsky.cheatdetector.impl.checks.combat.*;
import top.infsky.cheatdetector.impl.checks.exploits.GameModeA;
import top.infsky.cheatdetector.impl.checks.exploits.TimerA;
import top.infsky.cheatdetector.impl.checks.movement.*;
import top.infsky.cheatdetector.impl.checks.scaffolding.ScaffoldA;
import top.infsky.cheatdetector.impl.fixes.ServerFreeze;
import top.infsky.cheatdetector.impl.fixes.grimac.InvalidYaw;
import top.infsky.cheatdetector.impl.modules.*;
import top.infsky.cheatdetector.impl.fixes.vulcan.*;
import top.infsky.cheatdetector.impl.fixes.pas.*;
import top.infsky.cheatdetector.impl.modules.common.*;
import top.infsky.cheatdetector.impl.modules.danger.*;
import top.infsky.cheatdetector.impl.modules.pas.fakelag.FakelagDynamic;
import top.infsky.cheatdetector.impl.modules.pas.fakelag.FakelagLatency;
import top.infsky.cheatdetector.impl.modules.pas.*;
import top.infsky.cheatdetector.impl.utils.world.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class CheckManager {
    private final TRPlayer player;
    private final @NotNull Map<Class<? extends Check>, Check> checks;
    private final @NotNull Map<Class<? extends Check>, Check> preChecks;
    private final @NotNull Map<Class<? extends Check>, Check> normalChecks;
    private final @NotNull Map<Class<? extends Check>, Check> postChecks;
    public short disableTick;
    public boolean bypassCheck = true;
    public CheckManager(@NotNull Map<Class<? extends Check>, Check> preChecks,
                        @NotNull Map<Class<? extends Check>, Check> normalChecks,
                        @NotNull Map<Class<? extends Check>, Check> postChecks, TRPlayer player) {
        this.player = player;
        this.preChecks = new HashMap<>(preChecks);
        this.normalChecks = new HashMap<>(normalChecks);
        this.postChecks = new HashMap<>(postChecks);
        this.checks = new HashMap<>();
        this.checks.putAll(this.preChecks);
        this.checks.putAll(this.normalChecks);
        this.checks.putAll(this.postChecks);
        this.disableTick = 30;
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(@NotNull TRPlayer player) {
        final Map<Class<? extends Check>, Check> pre = new HashMap<>();
        final Map<Class<? extends Check>, Check> normal = new HashMap<>();
        final Map<Class<? extends Check>, Check> post = new HashMap<>();
        pre.put(GroundSpoofA.class, new GroundSpoofA(player));
        pre.put(GroundSpoofB.class, new GroundSpoofB(player));
        normal.put(FlyA.class, new FlyA(player));
        normal.put(FlyB.class, new FlyB(player));
        normal.put(BlinkA.class, new BlinkA(player));
        normal.put(SpeedA.class, new SpeedA(player));
        normal.put(SpeedB.class, new SpeedB(player));
        normal.put(SpeedC.class, new SpeedC(player));
        normal.put(HighJumpA.class, new HighJumpA(player));
        normal.put(NoSlowA.class, new NoSlowA(player));
        normal.put(GameModeA.class, new GameModeA(player));
        normal.put(VelocityA.class, new VelocityA(player));
        normal.put(AutoBlockA.class, new AutoBlockA(player));
        normal.put(MotionA.class, new MotionA(player));
        normal.put(ReachA.class, new ReachA(player));
        normal.put(HitBoxA.class, new HitBoxA(player));
        normal.put(AutoClickerA.class, new AutoClickerA(player));
        normal.put(FlyC.class, new FlyC(player));
        normal.put(BoatFlyA.class, new BoatFlyA(player));
        normal.put(StrafeA.class, new StrafeA(player));
        normal.put(TimerA.class, new TimerA(player));
        normal.put(InvalidPitch.class, new InvalidPitch(player));
        normal.put(AimA.class, new AimA(player));
        normal.put(ScaffoldA.class, new ScaffoldA(player));
        normal.put(AimB.class, new AimB(player));
        normal.put(MotionB.class, new MotionB(  player));

        return new CheckManager(pre, normal, post, player);
    }

    public static @NotNull CheckManager createSelf(@NotNull TRSelf player) {
        final Map<Class<? extends Check>, Check> pre = new HashMap<>();
        final Map<Class<? extends Check>, Check> normal = new HashMap<>();
        final Map<Class<? extends Check>, Check> post = new HashMap<>();
        pre.put(GroundSpoofA.class, new GroundSpoofA(player));
        pre.put(GroundSpoofB.class, new GroundSpoofB(player));
        normal.put(FlyA.class, new FlyA(player));
        normal.put(FlyB.class, new FlyB(player));
        normal.put(BlinkA.class, new BlinkA(player));
        normal.put(SpeedA.class, new SpeedA(player));
        normal.put(SpeedB.class, new SpeedB(player));
        normal.put(SpeedC.class, new SpeedC(player));
        normal.put(HighJumpA.class, new HighJumpA(player));
        normal.put(NoSlowA.class, new NoSlowA(player));
        normal.put(GameModeA.class, new GameModeA(player));
        normal.put(VelocityA.class, new VelocityA(player));
        normal.put(AutoBlockA.class, new AutoBlockA(player));
        normal.put(MotionA.class, new MotionA(player));
        normal.put(ReachA.class, new ReachA(player));
        normal.put(HitBoxA.class, new HitBoxA(player));
        normal.put(AutoClickerA.class, new AutoClickerA(player));
        normal.put(FlyC.class, new FlyC(player));
        normal.put(BoatFlyA.class, new BoatFlyA(player));
        normal.put(StrafeA.class, new StrafeA(player));
        normal.put(TimerA.class, new TimerA(player));
        normal.put(InvalidPitch.class, new InvalidPitch(player));
        normal.put(AimA.class, new AimA(player));
        normal.put(ScaffoldA.class, new ScaffoldA(player));
        normal.put(AimB.class, new AimB(player));
        normal.put(MotionB.class, new MotionB(player));

        pre.put(BadPacket1.class, new BadPacket1(player));
        pre.put(BadPacket2.class, new BadPacket2(player));
        pre.put(FastPlace.class, new FastPlace(player));
        pre.put(ServerFreeze.class, new ServerFreeze(player));
        pre.put(InvalidYaw.class, new InvalidYaw(player));

        post.put(Spin.class, new Spin(player));
        post.put(FlagDetector.class, new FlagDetector(player));
        pre.put(NoRotateSet.class, new NoRotateSet(player));
        post.put(ClickGUI.class, new ClickGUI(player));
        post.put(AntiVanish.class, new AntiVanish(player));
        post.put(Blink.class, new Blink(player));
        post.put(AntiFall.class, new AntiFall(player));
        post.put(FakelagLatency.class, new FakelagLatency(player));
        post.put(FakelagDynamic.class, new FakelagDynamic(player));
        post.put(AirPlace.class, new AirPlace(player));
        post.put(InvWalk.class, new InvWalk(player));
        post.put(NoteBot.class, new NoteBot(player));
        post.put(SayHacker.class, new SayHacker(player));
        post.put(JumpReset.class, new JumpReset(player));
        post.put(Scaffold.class, new Scaffold(player));
        post.put(AimAssist.class, new AimAssist(player));
        post.put(HandSpin.class, new HandSpin(player));
        post.put(Debug.class, new Debug(player));
        post.put(Nuker.class, new Nuker(player));
        post.put(AirStuck.class, new AirStuck(player));
        pre.put(ClientSpoof.class, new ClientSpoof(player));
        post.put(Fly.class, new Fly(player));
        pre.put(NoJumpDelay.class, new NoJumpDelay(player));
        post.put(Writer.class, new Writer(player));
        post.put(AntiBot.class, new AntiBot(player));
        post.put(Rotation.class, new Rotation(player));
        post.put(Sprint.class, new Sprint(player));
        post.put(SlowMotion.class, new SlowMotion(player));
        post.put(AutoCatch.class, new AutoCatch(player));
        post.put(Speed.class, new Speed(player));
        post.put(Velocity.class, new Velocity(player));
        post.put(NoStopBreak.class, new NoStopBreak(player));
        post.put(Surround.class, new Surround(player));

        return new CheckManager(pre, normal, post, player);
    }

    public void update() {
        if (bypassCheck) {
            if (player.currentPos == player.lastPos && !player.currentOnGround) {
                disableTick = 30;
                return;
            } else {
                bypassCheck = false;
            }
        }
        if (disableTick > 0) {
            disableTick--;
        }
        if (player.currentGameType != player.lastGameType) {
            for (Check check : preChecks.values()) check._onGameTypeChange();
            for (Check check : normalChecks.values()) check._onGameTypeChange();
            for (Check check : postChecks.values()) check._onGameTypeChange();
        }

        if (player.currentGameType == GameType.CREATIVE || player.currentGameType == GameType.SPECTATOR) return;
        if (player.lastOnGround && !player.currentOnGround) onJump();
        if (player instanceof TRSelf self)
            if (!self.lastLeftPressed && self.currentLeftPressed && TRPlayer.CLIENT.crosshairPickEntity != null)
                onCustomAction(check -> check._handleAttack(TRPlayer.CLIENT.crosshairPickEntity));

        EntityUtils.isOnPlaceBlock(player).ifPresent(result -> onCustomAction(check -> check._onPlaceBlock(result.getA(), result.getB())));

        for (Check check : preChecks.values()) check._onTick();
        for (Check check : normalChecks.values()) check._onTick();
        for (Check check : postChecks.values()) check._onTick();
    }

    public void onTeleport() {
        if (player == null || !CheatDetector.inWorld) return;
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

    public void onCustomAction(Consumer<Check> action) {
        if (player == null || !CheatDetector.inWorld) return;
        for (Check check : preChecks.values()) action.accept(check);
        for (Check check : normalChecks.values()) action.accept(check);
        for (Check check : postChecks.values()) action.accept(check);
    }
}
