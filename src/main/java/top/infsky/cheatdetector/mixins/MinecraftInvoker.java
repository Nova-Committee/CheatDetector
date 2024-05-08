package top.infsky.cheatdetector.mixins;


import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftInvoker {
    @Accessor("rightClickDelay")
    void setRightClickDelay(int rightClickDelay);
}
