package top.infsky.cheatdetector.mixins;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityInvoker {
    @Accessor("xRot")
    void doSetXRot(float xRot);

    @Accessor("yRot")
    void doSetYRot(float yRot);
}
