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
import top.infsky.cheatdetector.impl.utils.TimeTaskManager;
import top.infsky.cheatdetector.config.AlertConfig;
import top.infsky.cheatdetector.config.AntiCheatConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 管理玩家信息的类。每个玩家都应有一个TRPlayer实例。
 */
@Getter
public class TRPlayer {
    public AbstractClientPlayer fabricPlayer;
    public CheckManager manager;
    public static Minecraft CLIENT = CheatDetector.CLIENT;
    public Vec3 currentPos;
    public Vec3 lastPos;
    public Vec2 currentRot;
    public Vec2 lastRot;
    @Range(from = 0, to = 19) public List<Vec3> posHistory;
    public Vec3 lastOnGroundPos;
    public Vec3 lastOnGroundPos2;
    public Vec3 lastInLiquidPos;
    public Vec3 lastOnLiquidGroundPos;
    public boolean currentOnGround;
    public boolean lastOnGround;
    public boolean lastOnGround2;
    public boolean hasSetback = false;
    public boolean jumping = false;
    public boolean lastUsingItem = false;
    public double speedMul = 1;
    public GameType currentGameType;
    public GameType lastGameType;
    public long upTime = 0;
    public int latency = 0;
    public float lastFallDistance = 0;

    public @NotNull TimeTaskManager timeTask = new TimeTaskManager();
    @Contract("_ -> new")
    public static @NotNull TRPlayer create(@NotNull AbstractClientPlayer player) {
        return new TRPlayer(player, false);
    }

    public TRPlayer(AbstractClientPlayer player, boolean self) {
        this.fabricPlayer = player;
        this.manager = self ? CheckManager.createSelf((TRSelf) this) : CheckManager.create(this);

        currentPos = fabricPlayer.position();
        currentRot = fabricPlayer.getRotationVector();
        currentOnGround = lastOnGround = lastOnGround2 = fabricPlayer.onGround();
        currentGameType = lastGameType =
                fabricPlayer.isCreative() ? GameType.CREATIVE :
                        fabricPlayer.isSpectator() ? GameType.SPECTATOR :
                                GameType.SURVIVAL;
        posHistory = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            posHistory.add(currentPos);
        }
    }

    public void update(AbstractClientPlayer player) {
        fabricPlayer = player;
        if (fabricPlayer == null) return;

        currentPos = fabricPlayer.position();
        currentRot = fabricPlayer.getRotationVector();
        currentOnGround = fabricPlayer.onGround();
        speedMul = (fabricPlayer.getActiveEffectsMap().containsKey(MobEffects.MOVEMENT_SPEED)
                ? fabricPlayer.getActiveEffectsMap().get(MobEffects.MOVEMENT_SPEED).getAmplifier() * 0.2 + 1
                : 1)
                * fabricPlayer.getSpeed() * 10;  // IDK why, but it just works!
        updatePoses();
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
        timeTask.onTick();

        manager.update();

        lastPos = currentPos;
        lastRot = currentRot;
        lastOnGround2 = lastOnGround;
        lastOnGround = currentOnGround;
        lastUsingItem = fabricPlayer.isUsingItem();
        lastGameType = currentGameType;
        lastFallDistance = fabricPlayer.fallDistance;
        upTime++;
        tryToClearVL();
    }

    private void updatePoses() {
        if (posHistory.size() >= 20) {
            posHistory.remove(posHistory.size() - 1);
        }
        posHistory.add(0, currentPos);
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
