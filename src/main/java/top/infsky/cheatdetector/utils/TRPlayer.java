package top.infsky.cheatdetector.utils;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Check;
import top.infsky.cheatdetector.config.AlertConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 管理玩家信息的类。每个玩家都应有一个TRPlayer实例。
 */
@Getter
public class TRPlayer {
    public AbstractClientPlayer fabricPlayer;
    public CheckManager manager;
    public static Minecraft CLIENT = CheatDetector.CLIENT;
    public Vec3 currentPos = Vec3.ZERO;
    public Vec3 currentMotion = Vec3.ZERO;
    public Vec3 currentVehicleMotion = Vec3.ZERO;
    public boolean currentSprint = false;
    public boolean currentSwing = false;
    public Vec3 lastPos = Vec3.ZERO;
    public Vec3 lastMotion = Vec3.ZERO;
    public Vec3 lastVehicleMotion = Vec3.ZERO;
    public boolean lastSprint = false;
    public boolean lastSwing = false;
    public Vec2 currentRot = Vec2.ZERO;
    public Vec2 lastRot = Vec2.ZERO;
    @Range(from = 0, to = 19) public List<Vec3> posHistory = new ArrayList<>(20);
    @Range(from = 0, to = 19) public List<Vec3> motionHistory = new ArrayList<>(20);
    @Range(from = 0, to = 19) public List<Vec3> vehicleMotionHistory = new ArrayList<>(20);
    @Range(from = 0, to = 19) public List<Boolean> sprintHistory = new ArrayList<>(20);
    public Vec3 lastOnGroundPos = Vec3.ZERO;
    public Vec3 lastOnGroundPos2 = Vec3.ZERO;
    public Vec3 lastInLiquidPos = Vec3.ZERO;
    public Vec3 lastOnLiquidGroundPos = Vec3.ZERO;
    public boolean currentOnGround = true;
    public boolean lastOnGround = true;
    public boolean lastOnGround2 = true;
    public boolean hasSetback = false;
    public boolean jumping = false;
    public boolean lastUsingItem = false;
    public double speedMul = 1;
    public GameType currentGameType = GameType.SURVIVAL;
    public GameType lastGameType = GameType.SURVIVAL;
    public long upTime = 0;
    public int latency = 0;
    public float lastFallDistance = 0;
    public short offGroundTicks = 0;

    public @NotNull ScheduledExecutorService timeTask = Executors.newScheduledThreadPool(4);
    @Contract("_ -> new")
    public static @NotNull TRPlayer create(@NotNull AbstractClientPlayer player) {
        return new TRPlayer(player, false);
    }

    public TRPlayer(AbstractClientPlayer player, boolean self) {
        this.fabricPlayer = player;
        this.manager = self ? CheckManager.createSelf((TRSelf) this) : CheckManager.create(this);

        currentPos = fabricPlayer.position();
        currentMotion = fabricPlayer.getDeltaMovement();
        currentVehicleMotion = fabricPlayer.getVehicle() != null ? fabricPlayer.getVehicle().getDeltaMovement() : Vec3.ZERO;
        currentSprint = fabricPlayer.isSprinting();
        currentSwing = fabricPlayer.swinging;
        currentRot = fabricPlayer.getRotationVector();
        currentOnGround = lastOnGround = lastOnGround2 = fabricPlayer.onGround();
        currentGameType = lastGameType =
                fabricPlayer.isCreative() ? GameType.CREATIVE :
                        fabricPlayer.isSpectator() ? GameType.SPECTATOR :
                                GameType.SURVIVAL;
        for (int i = 0; i < 20; i++) {
            posHistory.add(currentPos);
        }
        for (int i = 0; i < 20; i++) {
            motionHistory.add(currentMotion);
        }
        for (int i = 0; i < 20; i++) {
            vehicleMotionHistory.add(currentVehicleMotion);
        }
        for (int i = 0; i < 20; i++) {
            sprintHistory.add(currentSprint);
        }
    }

    public void update(AbstractClientPlayer player) {
        fabricPlayer = player;
        if (fabricPlayer == null) return;

        currentPos = fabricPlayer.position();
        currentMotion = fabricPlayer.getDeltaMovement();
        currentVehicleMotion = fabricPlayer.getVehicle() != null ? fabricPlayer.getVehicle().getDeltaMovement() : Vec3.ZERO;
        currentSprint = fabricPlayer.isSprinting();
        currentSwing = fabricPlayer.swinging;
        currentRot = fabricPlayer.getRotationVector();
        currentOnGround = fabricPlayer.onGround();
        if (currentOnGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }
        speedMul = (fabricPlayer.getActiveEffectsMap().containsKey(MobEffects.MOVEMENT_SPEED)
                ? fabricPlayer.getActiveEffectsMap().get(MobEffects.MOVEMENT_SPEED).getAmplifier() * 0.2 + 1
                : 1)
                * fabricPlayer.getSpeed() * 10;  // IDK why, but it just works!
        updateHistory();
        try {
            final PlayerInfo playerInfo = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(CheatDetector.CLIENT).getConnection()).getPlayerInfo(fabricPlayer.getUUID()));
            currentGameType = playerInfo.getGameMode();
            latency = playerInfo.getLatency();
        } catch (NullPointerException ignored) {
        }
        if (fabricPlayer.onGround()) {
            lastOnGroundPos2 = lastOnGroundPos;
            lastOnGroundPos = currentPos;
            jumping = false;
            if (fabricPlayer.isInWater())
                lastOnLiquidGroundPos = currentPos;
        }
        if (fabricPlayer.isInWater() || fabricPlayer.isInLava()) {
            lastInLiquidPos = currentPos;
        }

        manager.update();

        lastPos = currentPos;
        lastMotion = currentMotion;
        lastVehicleMotion = currentVehicleMotion;
        lastSprint = currentSprint;
        lastSwing = currentSwing;
        lastRot = currentRot;
        lastOnGround2 = lastOnGround;
        lastOnGround = currentOnGround;
        lastUsingItem = fabricPlayer.isUsingItem();
        lastGameType = currentGameType;
        lastFallDistance = fabricPlayer.fallDistance;
        upTime++;
        tryToClearVL();
    }

    private void updateHistory() {
        if (posHistory.size() >= 20) {
            posHistory.remove(posHistory.size() - 1);
        }
        posHistory.add(0, currentPos);

        if (motionHistory.size() >= 20) {
            motionHistory.remove(motionHistory.size() - 1);
        }
        motionHistory.add(0, currentMotion);

        if (vehicleMotionHistory.size() >= 20) {
            vehicleMotionHistory.remove(vehicleMotionHistory.size() - 1);
        }
        vehicleMotionHistory.add(0, currentVehicleMotion);

        if (sprintHistory.size() >= 20) {
            sprintHistory.remove(sprintHistory.size() - 1);
        }
        sprintHistory.add(0, currentSprint);
    }

    public void tryToClearVL() {
        if (upTime % AntiCheatConfig.VLClearTime == 0) {
            for (Check check : manager.getChecks().values()) {
                check.violations = 0;
            }
            if (AlertConfig.allowAlertVLClear)
                LogUtils.prefix(fabricPlayer.getName().getString(),
                        Component.translatable("cheatdetector.chat.alert.vlclear").withStyle(ChatFormatting.DARK_GRAY).getString());
        }
    }
}
