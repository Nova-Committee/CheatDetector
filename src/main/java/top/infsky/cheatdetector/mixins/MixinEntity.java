package top.infsky.cheatdetector.mixins;


import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.modules.pas.AutoCatch;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "stopRiding", at = @At("TAIL"))
    public void stopRiding(CallbackInfo ci) {
        AutoCatch autoCatch = (AutoCatch) AutoCatch.getInstance();

        if (autoCatch != null) {
            autoCatch.onStopRiding((Entity) (Object) this);
        }
    }
}
