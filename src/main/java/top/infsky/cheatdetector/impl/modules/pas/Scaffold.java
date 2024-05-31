package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.modules.common.Rotation;
import top.infsky.cheatdetector.impl.utils.world.ContainerUtils;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.BlockUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.List;

public class Scaffold extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private long lastPlaceTime = 0;
    private BlockPos lastGround = null;

    public Scaffold(@NotNull TRSelf player) {
        super("Scaffold", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            lastGround = null;
            return;
        }
        if (TRPlayer.CLIENT.gameMode == null) return;

        BlockPos ground = player.fabricPlayer.blockPosition().below();
        if (Advanced3Config.scaffoldSameY && lastGround != null) ground = ground.atY(lastGround.getY());

        lastGround = ground;

        ClientLevel level = LevelUtils.getClientLevel();
        List<BlockPos> needToPlace = new java.util.ArrayList<>();

        needToPlace.add(ground);
        for (int i = 0; i < Advanced3Config.scaffoldExpend; i++) {
            needToPlace.add(ground.relative(player.fabricPlayer.getDirection(), i));
        }

        for (BlockPos blockPos : needToPlace) {
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.isAir() && !blockState.canBeReplaced()) continue;

            if (player.upTime - lastPlaceTime < Advanced3Config.scaffoldPlaceMinDelay) return;

            InteractionHand hand = null;
            if (player.fabricPlayer.getMainHandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.MAIN_HAND;
            else if (player.fabricPlayer.getOffhandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.OFF_HAND;
            else {
                if (Advanced3Config.scaffoldAutoSwitch) {
                    try {
                        ContainerUtils.selectHotBar(ContainerUtils.findItem(player.fabricPlayer.getInventory(), BlockItem.class, ContainerUtils.SlotType.HOTBAR));
                        hand = InteractionHand.MAIN_HAND;
                    } catch (ContainerUtils.ItemNotFoundException e) {
                        return;
                    }
                }
            }

            if (hand == null) return;

            if (Advanced3Config.scaffoldNoSprint)
                player.fabricPlayer.setSprinting(false);
            if (Advanced3Config.scaffoldDoRotation) {
                if (Advanced3Config.scaffoldSilentRotation)
                    Rotation.silentRotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos));
                else
                    PlayerRotation.rotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos));
            }
            BlockHitResult hitResult = new BlockHitResult(player.currentPos, BlockUtils.getPlaceSide(blockPos), blockPos, false);
            InteractionResult interactionResult = TRPlayer.CLIENT.gameMode.useItemOn(player.fabricPlayer, hand, hitResult);
            if (interactionResult.shouldSwing() && !Advanced3Config.scaffoldNoSwing) player.fabricPlayer.swing(hand);
            lastPlaceTime = player.upTime;
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.scaffoldEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
