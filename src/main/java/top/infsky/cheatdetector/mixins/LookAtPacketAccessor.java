package top.infsky.cheatdetector.mixins;


import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundPlayerLookAtPacket.class)
public interface LookAtPacketAccessor {
    @Accessor("x")
    double getX();

    @Accessor("y")
    double getY();

    @Accessor("z")
    double getZ();

    @Accessor("fromAnchor")
    EntityAnchorArgument.Anchor getFromAnchor();
}
