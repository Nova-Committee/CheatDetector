package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.config.*;

import java.util.HashSet;
import java.util.Set;

public class Spin extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    @Range(from = -180, to = 180)
    public float yaw;
    @Range(from = -90, to = 90)
    public float pitch;
    public boolean pitchReserve = false;

    private static final Set<Class<? extends Screen>> ALLOW_SCREENS = new HashSet<>(Set.of(
            ChatScreen.class,
            PauseScreen.class
    ));
    private int disableTicks = 0;
    public Spin(@NotNull TRSelf player) {
        super("Spin", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        // 😅这若智代码写了一天还没写出来
        // xRot竟然是pitch我难绷了
        // TODO move-fix
        if (isDisabled()) return;

        updateRot();

        if (Advanced3Config.spinAutoPause &&
                TRPlayer.CLIENT.screen != null && ALLOW_SCREENS.stream().noneMatch(aClass -> aClass.isInstance(TRPlayer.CLIENT.screen))) {
            disableTicks = Advanced3Config.spinAutoPauseTime;
        }

        if (disableTicks > 0) {
            disableTicks--;
            return;
        }

        if (Advanced3Config.spinOnlyPacket) {
            Rotation.silentRotate(yaw, pitch);
        } else {
            PlayerRotation.rotate(yaw, pitch);
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.spinEnabled;
    }

    public void updateRot() {
        check();
        if (Advanced3Config.spinDoSpinYaw)
            yaw += (float) Advanced3Config.spinYawStep;
        else
            yaw = (float) Advanced3Config.spinDefaultYaw;
        if (Advanced3Config.spinDoSpinPitch)
            pitch += (float) (Advanced3Config.spinPitchStep * (pitchReserve ? -1 : 1));
        else
            pitch = (float) Advanced3Config.spinDefaultPitch;
        check();
    }

    public void check() {
        // pitch
        if (Advanced3Config.spinAllowBadPitch) {
            pitchReserve = false;
            // blatant转头
            if (pitch >= 180) {  // 向下转了一圈
                pitch = -180;
            } else if (yaw <= -180) {  // 向上转了一圈
                pitch = 180;
            }
        } else {
            // legit转头
            if (pitch >= 90) {  // 低头太低
                pitch = 90;
                pitchReserve = true;
            } else if (pitch <= -90) {  // 抬头太高
                pitch = -90;
                pitchReserve = false;
            }
        }
    }

    @Override
    public void _onTeleport() {
        pitch = player.fabricPlayer.getXRot();
        yaw = player.fabricPlayer.getYRot();
    }
}
