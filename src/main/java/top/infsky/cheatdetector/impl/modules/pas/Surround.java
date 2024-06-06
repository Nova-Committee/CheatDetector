package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.modules.common.Rotation;
import top.infsky.cheatdetector.impl.utils.world.*;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;

import java.util.NoSuchElementException;
import java.util.Set;

public class Surround extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    private long lastPlaceTime = -1;
    public Surround(@NotNull TRSelf player) {
        super("Surround", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;

        try {
            AbstractClientPlayer target = EntityUtils.getClosestPlayer(Advanced3Config.surroundName, player.fabricPlayer).orElseThrow();

            if (target.distanceTo(player.fabricPlayer) > Advanced3Config.surroundDistance) return;
            doSurround(target);
        } catch (NoSuchElementException ignored) {
        }
    }

    private void doSurround(AbstractClientPlayer target) {
        if (TRPlayer.CLIENT.gameMode == null) return;
        if (player.upTime - lastPlaceTime < Advanced3Config.surroundPlaceDelay) return;

        ClientLevel level = LevelUtils.getClientLevel();
        Set<BlockPos> surroundBlocks = BlockUtils.getSurroundBlocks(target);

        for (BlockPos blockPos : surroundBlocks) {
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.isAir() && !blockState.canBeReplaced()) continue;


            int selectFrom = player.fabricPlayer.getInventory().selected;
            int toSlot;
            InteractionHand hand = null;
            if (player.fabricPlayer.getMainHandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.MAIN_HAND;
            else if (player.fabricPlayer.getOffhandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.OFF_HAND;
            else {
                if (Advanced3Config.surroundAutoSwitch) {
                    toSlot = ContainerUtils.findItem(player.fabricPlayer.getInventory(), BlockItem.class, ContainerUtils.SlotType.HOTBAR);
                    ContainerUtils.selectHotBar(toSlot);
                    hand = InteractionHand.MAIN_HAND;
                }
            }
            if (hand == null) return;

            if (Advanced3Config.surroundDoRotation) {
                if (Advanced3Config.surroundSilentRotation)
                    Rotation.silentRotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos));
                else
                    PlayerRotation.rotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos));
            }

            InteractionResult interactionResult = LevelUtils.placeBlock(blockPos, hand);
            if (interactionResult.shouldSwing() && Advanced3Config.surroundSwing) player.fabricPlayer.swing(hand);
            lastPlaceTime = player.upTime;

            if (Advanced3Config.surroundAutoSwitchBack) {
                player.fabricPlayer.getInventory().selected = selectFrom;
            }

            if (Advanced3Config.surroundSmartPlaceDelay && surroundBlocks.size() <= 10) continue;
            if (Advanced3Config.surroundPlaceDelay > 0) return;
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.surroundEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
