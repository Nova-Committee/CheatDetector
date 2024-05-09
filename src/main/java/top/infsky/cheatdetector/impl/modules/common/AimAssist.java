package top.infsky.cheatdetector.impl.modules.common;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.AimSimulator;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.utils.TRSelf;

public class AimAssist extends Module {
    public AimAssist(@NotNull TRSelf player) {
        super("AimAssist", player);
    }

    private boolean checkTarget(@NotNull Entity entity) {
        if (entity.is(player.fabricPlayer)) return false;
        if (Advanced3Config.aimAssistIncludePlayers && entity instanceof Player) return true;
        if (Advanced3Config.aimAssistIncludeArmorStands && entity instanceof ArmorStand) return true;
        return Advanced3Config.aimAssistIncludeEntities && entity instanceof Entity;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        double distance = Advanced3Config.aimAssistRange;
        Entity target = null;
        for (AbstractClientPlayer entity : LevelUtils.getClientLevel().players()) {
            if (checkTarget(entity) && distance > entity.distanceTo(player.fabricPlayer)) {
                target = entity;
                distance = entity.distanceTo(player.fabricPlayer);
            }
        }

        if (target != null)
            rotate(target);
    }

    private void rotate(Entity target) {
        Pair<Double, Double> rot;
        if (Advanced3Config.aimAssistLegitAim) {
            rot = AimSimulator.getLegitAim(target, player, Advanced3Config.aimAssistLegitAimNoise);
        } else {
            rot = new Pair<>(
                    PlayerRotation.getYaw(target.getEyePosition()),
                    PlayerRotation.getPitch(target.getEyePosition())
            );
        }

        PlayerRotation.rotate(rot.getA(), rot.getB());
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.aimAssistEnabled;
    }
}
