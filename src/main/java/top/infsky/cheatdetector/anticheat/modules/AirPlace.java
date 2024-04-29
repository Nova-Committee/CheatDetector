package top.infsky.cheatdetector.anticheat.modules;

import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.MinecraftInvoker;

public class AirPlace extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public AirPlace(@NotNull TRSelf player) {
        super("AirPlace", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        if (!TRPlayer.CLIENT.mouseHandler.isRightPressed()) return;
        if (TRPlayer.CLIENT.gameMode == null) return;

        HitResult hitResult = player.fabricPlayer.pick(Advanced3Config.airPlaceReach, 0, false);
        if(hitResult.getType() != HitResult.Type.MISS)
            return;

        if(!(hitResult instanceof BlockHitResult blockHitResult))
            return;

        ((MinecraftInvoker) TRPlayer.CLIENT).setRightClickDelay(4);
        if(player.fabricPlayer.isHandsBusy())
            return;

        final InteractionHand hand = player.fabricPlayer.getMainHandItem().getItem() instanceof BlockItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        TRPlayer.CLIENT.gameMode.useItemOn(player.fabricPlayer, hand, blockHitResult);
        if (!Advanced3Config.airPlaceNoSwing) player.fabricPlayer.swing(hand);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.airPlaceEnabled;
    }
}
