package top.infsky.cheatdetector.mixins;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.modules.common.AirStuck;
import top.infsky.cheatdetector.impl.modules.pas.Fly;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    public void move(MoverType moverType, Vec3 vec3, @NotNull CallbackInfo ci) {
        if (ci.isCancelled()) return;

        AirStuck airStuck = (AirStuck) (AirStuck.getInstance());
        if (airStuck != null) {
            if (!vec3.equals(airStuck.getModMoveTo())) {
                if ((airStuck).isShouldStuck()) {
                    ci.cancel();
                    ((LocalPlayer) (Object) this).move(moverType, airStuck.getModMoveTo());
                }
            }
        }

        if (ci.isCancelled()) return;

        Fly fly = (Fly) (Fly.getInstance());
        if (fly != null) {
            if (fly.isNoJump()) {
                if (vec3.y() > 0) {
                    ci.cancel();
                }
            }
        }
    }
}
