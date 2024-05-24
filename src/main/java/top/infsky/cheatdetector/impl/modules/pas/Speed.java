package top.infsky.cheatdetector.impl.modules.pas;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

public class Speed extends Module {
    private short offGroundTicks = 0;

    public Speed(@NotNull TRSelf player) {
        super("Speed", player);
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            offGroundTicks = 0;
            return;
        }

        if (player.currentOnGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }

        switch (offGroundTicks) {
            case 0 -> {
                if (!TRPlayer.CLIENT.options.keyJump.isDown() && PlayerMove.isMove())
                    player.fabricPlayer.jumpFromGround();
            }
            case 5 -> {
                Vec3 currentMotion = player.fabricPlayer.getDeltaMovement();
                player.fabricPlayer.setDeltaMovement(currentMotion.x(), PlayerMove.predictedMotion(currentMotion.y(), Advanced3Config.speedBoost), currentMotion.z());
            }
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.speedEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
