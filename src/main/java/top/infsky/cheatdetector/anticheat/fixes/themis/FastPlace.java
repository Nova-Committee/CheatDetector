package top.infsky.cheatdetector.anticheat.fixes.themis;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Fix;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.anticheat.utils.UseItemOn;
import top.infsky.cheatdetector.config.Advanced2Config;
import top.infsky.cheatdetector.config.AlertConfig;
import top.infsky.cheatdetector.config.FixesConfig;

import java.util.Objects;

public class FastPlace extends Fix {
    public short minDelay = 1;
    public long lastPlaceTick = 0;
    public @Nullable UseItemOn lastAction = null;

    public FastPlace(@NotNull TRSelf player) {
        super("FastPlace", player);
    }

    @Override
    public void _onTick() {
        minDelay = Advanced2Config.getFastPlaceSamePlaceMinDelay();
    }

    @Override
    public boolean _handleUseItemOn(@NotNull ServerboundUseItemOnPacket packet, @NotNull CallbackInfo ci) {
        if (!FixesConfig.packetFixEnabled) return false;
        final InteractionHand interactionHand = packet.getHand();
        final BlockHitResult blockHitResult = packet.getHitResult();

        final ItemStack itemStack = getItemStack(player.fabricPlayer, interactionHand);
        if (!(Objects.requireNonNull(itemStack).getItem() instanceof BlockItem)) return false;

        final UseItemOn currentAction = new UseItemOn(itemStack.getItem(), blockHitResult.getBlockPos(), blockHitResult.getDirection(), blockHitResult.getType());
        if (player.upTime - lastPlaceTick < minDelay) {  // 在受制约的tick里
            if (currentAction.equals(lastAction)) {  // 是spam
                if (AlertConfig.allowAlertFixes) flag();
                if (Advanced2Config.fastPlaceEnabled)
                    ci.cancel();
                lastAction = currentAction;
                return true;
            }
        }

        lastAction = currentAction;
        lastPlaceTick = player.upTime;
        return false;
    }

    public static @Nullable ItemStack getItemStack(@NotNull AbstractClientPlayer player, @NotNull InteractionHand interactionHand) {
        ItemStack result = null;
        switch (interactionHand) {
            case MAIN_HAND -> result = player.getMainHandItem();
            case OFF_HAND -> result = player.getOffhandItem();
        }
        return result;
    }

    @Override
    public boolean isDisabled() {
        return !Advanced2Config.fastPlaceEnabled;
    }

    @Override
    public int getAlertBuffer() {
        return Advanced2Config.fastPlaceAlertBuffer;
    }
}
