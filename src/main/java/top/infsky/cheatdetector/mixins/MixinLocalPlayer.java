package top.infsky.cheatdetector.mixins;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.modules.common.AirStuck;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    public void move(MoverType moverType, Vec3 vec3, CallbackInfo ci) {
        AirStuck airStuck = (AirStuck) (AirStuck.getInstance());
        if (airStuck != null) {
            if (vec3.equals(airStuck.getModMoveTo())) return;
            if ((airStuck).isShouldStuck()) {
                ci.cancel();
                ((LocalPlayer) (Object) this).move(moverType, airStuck.getModMoveTo());
            }
        }
    }
}
