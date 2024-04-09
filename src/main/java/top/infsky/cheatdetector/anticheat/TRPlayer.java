package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.utils.TimeTaskManager;
import top.infsky.cheatdetector.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

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
    public Vec3 lastInLiquidPos;
    public Vec3 lastOnLiquidGroundPos;
    public boolean lastOnGround;
    public boolean hasSetback = false;
    public boolean jumping = false;
    public boolean lastUsingItem = false;
    public double speedMul = 1;
    public GameType currentGameType;
    public GameType lastGameType;
    public long upTime = 0;

    public TimeTaskManager timeTask = new TimeTaskManager();

    public static TRPlayer SELF;
    public TRPlayer(@NotNull AbstractClientPlayer player, boolean self) {
        this.fabricPlayer = player;
        if (self)
            this.manager = CheckManager.createSelf(this);
        else
            this.manager = CheckManager.create(this);
        currentPos = fabricPlayer.position();
        currentRot = fabricPlayer.getRotationVector();
        lastOnGround = fabricPlayer.onGround();
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
        currentGameType = fabricPlayer.isCreative() ? GameType.CREATIVE :
                fabricPlayer.isSpectator() ? GameType.SPECTATOR :
                        GameType.SURVIVAL;
        speedMul = (fabricPlayer.getActiveEffectsMap().containsKey(MobEffects.MOVEMENT_SPEED)
                ? fabricPlayer.getActiveEffectsMap().get(MobEffects.MOVEMENT_SPEED).getAmplifier() * 0.2 + 1
                : 1)
                * fabricPlayer.getSpeed() * 10;  // IDK why, but it just works!
        updatePoses();
        if (fabricPlayer.onGround()) {
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
        lastOnGround = fabricPlayer.onGround();
        lastUsingItem = fabricPlayer.isUsingItem();
        lastGameType = currentGameType;
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
        if (upTime % CONFIG().getAntiCheat().getVLClearTime() == 0) {
            for (Check check : manager.checks.values()) {
                check.violations = 0;
            }
            if (CONFIG().getAlert().isAllowAlertVLClear())
                LogUtils.prefix(fabricPlayer.getName().getString(),
                        Component.translatable("chat.cheatdetector.alert.vlclear").withStyle(ChatFormatting.DARK_GRAY).getString());
        }
    }
}
