package top.infsky.cheatdetector.anticheat;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.utils.TimeTaskManager;

import java.util.LinkedList;
import java.util.List;

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
    @Range(from = 0, to = 19) public List<Vec3> posHistory;
    public Vec3 lastOnGroundPos;
    public Vec3 lastInLiquidPos;
    public Vec3 lastOnLiquidGroundPos;
    public boolean lastOnGround;
    public boolean hasSetback = false;
    public boolean jumping = false;
    public boolean lastUsingItem = false;

    public TimeTaskManager timeTask = new TimeTaskManager();

    public TRPlayer(@NotNull AbstractClientPlayer player) {
        this.fabricPlayer = player;
        this.manager = CheckManager.create(this);
        currentPos = fabricPlayer.position();
        lastOnGround = fabricPlayer.onGround();
        posHistory = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            posHistory.add(currentPos);
        }
    }

    public void update(AbstractClientPlayer player) {
        fabricPlayer = player;
        if (fabricPlayer == null) return;

        currentPos = fabricPlayer.position();
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
        lastOnGround = fabricPlayer.onGround();
        lastUsingItem = fabricPlayer.isUsingItem();
    }

    private void updatePoses() {
        if (posHistory.size() >= 20) {
            posHistory.remove(posHistory.size() - 1);
        }
        posHistory.add(0, currentPos);
    }
}
