package top.infsky.cheatdetector.mixins;

import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.modules.danger.AirStuck;
import top.infsky.cheatdetector.impl.modules.danger.Fly;
import top.infsky.cheatdetector.impl.modules.pas.Speed;
import top.infsky.cheatdetector.impl.utils.world.PlayerMove;

@Mixin(KeyMapping.class)
public abstract class MixinKeyMapping {
    @Inject(method = "isDown", at = @At("HEAD"), cancellable = true)
    public void isDown(@NotNull CallbackInfoReturnable<Boolean> ci) {
        if (ci.isCancelled()) return;

        KeyMapping keyMapping = (KeyMapping) (Object) this;
        try {
            if (keyMapping.equals(CheatDetector.CLIENT.options.keyJump)) {
                Speed speed = (Speed) Speed.getInstance();
                if (speed != null) {
                    if (speed.isNoJump()) ci.setReturnValue(false);
                }

                if (ci.isCancelled()) return;

                AirStuck airStuck = (AirStuck) (AirStuck.getInstance());
                if (airStuck != null) {
                    if (airStuck.isShouldStuck()) {
                        ci.cancel();
                    }
                }

                if (ci.isCancelled()) return;

                Fly fly = (Fly) (Fly.getInstance());
                if (fly != null) {
                    if (fly.isNoJump() && PlayerMove.isMove()) {
                        ci.cancel();
                    }
                }
            }
        } catch (Exception ignored) {}
    }
}
