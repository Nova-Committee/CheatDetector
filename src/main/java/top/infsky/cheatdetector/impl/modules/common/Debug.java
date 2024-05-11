package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.ArrayList;
import java.util.List;

public class Debug extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private Vec3 lastMotion = Vec3.ZERO;
    private List<Vec3> motionRecording = null;

    public Debug(@NotNull TRSelf player) {
        super("Debug", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        Vec3 motion = player.fabricPlayer.getDeltaMovement();
        Vec3 offset = new Vec3(motion.x() - lastMotion.x(), motion.y() - lastMotion.y(), motion.z() - lastMotion.z());

        if (player.jumping) {
            if (motionRecording == null) motionRecording = new ArrayList<>();
            motionRecording.add(offset);
        } else if (motionRecording != null) {
            StringBuilder stringBuilder = new StringBuilder("Y Motion: ");
            for (Vec3 vec3 : motionRecording) {
                stringBuilder.append(vec3.y()).append(" ");
            }
            customMsg(stringBuilder.toString());
            motionRecording = null;
        }

        lastMotion = motion;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.debugEnabled;
    }
}
