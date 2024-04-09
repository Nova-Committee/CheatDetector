package top.infsky.cheatdetector.anticheat.fixs.themis;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.utils.UseItemOn;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class FastPlace extends Check {
    public final short minDelay = 1;
    public long lastPlaceTick = 0;
    public UseItemOn lastAction = null;

    public FastPlace(@NotNull TRPlayer player) {
        super("FastPlace", player);
    }

    @Override
    public boolean _onAction(InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfo ci) {
        if (!CONFIG().getFixes().isPacketFixEnabled()) return false;

        final ItemStack itemStack = getItemStack(player.fabricPlayer, interactionHand);
        if (!(itemStack.getItem() instanceof BlockItem)) return false;

        final UseItemOn currentAction = new UseItemOn(itemStack.getItem(), blockHitResult.getBlockPos(), blockHitResult.getDirection(), blockHitResult.getType());
        if (player.upTime - lastPlaceTick < minDelay) {  // 在受制约的tick里
            if (currentAction.equals(lastAction)) {  // 是spam
                if (CONFIG().getAlert().isAllowAlertPacketFix()) flag();
                ci.cancel();
                lastAction = currentAction;
                return true;
            }
        }

        lastAction = currentAction;
        lastPlaceTick = player.upTime;
        return false;
    }

    public static ItemStack getItemStack(AbstractClientPlayer player, @NotNull InteractionHand interactionHand) {
        ItemStack result = null;
        switch (interactionHand) {
            case MAIN_HAND -> result = player.getMainHandItem();
            case OFF_HAND -> result = player.getOffhandItem();
        }
        return result;
    }
}
