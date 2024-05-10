package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.impl.utils.world.ContainerUtils;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.BlockUtils;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.io.IOException;
import java.util.List;

public class Scaffold extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private long lastPlaceTime = 0;
    private int clientSlot = -1;
    private int serverSlot = -1;
    private BlockPos lastGround = null;

    public Scaffold(@NotNull TRSelf player) {
        super("Scaffold", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) {
            lastGround = null;
            if (clientSlot != -1 && clientSlot != serverSlot) {
                ContainerUtils.silentSelectHotBar(clientSlot);
            }
            clientSlot = -1;
            serverSlot = -1;
            return;
        }
        if (TRPlayer.CLIENT.gameMode == null) return;

        BlockPos ground = player.fabricPlayer.blockPosition().below();
        if (Advanced3Config.scaffoldSameY && lastGround != null) ground = ground.atY(lastGround.getY());

        lastGround = ground;

        List<BlockPos> needToPlace = new java.util.ArrayList<>();

        needToPlace.add(ground);
        for (int i = 0; i < Advanced3Config.scaffoldExpend; i++) {
            needToPlace.add(ground.relative(player.fabricPlayer.getDirection(), i));
        }

        for (BlockPos blockPos : needToPlace) {
            try (Level level = TRPlayer.CLIENT.level) {
                if (level == null) return;
                BlockState blockState = level.getBlockState(blockPos);
                if (
                        (!blockState.isAir() && !(blockState.getBlock() instanceof LiquidBlock))
                        || blockState.hasBlockEntity()
                ) continue;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (player.upTime - lastPlaceTime < Advanced3Config.scaffoldPlaceMinDelay) return;

            BlockHitResult hitResult = new BlockHitResult(player.currentPos, BlockUtils.getPlaceSide(blockPos), blockPos, false);

            InteractionHand hand = null;
            if (player.fabricPlayer.getMainHandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.MAIN_HAND;
            else if (player.fabricPlayer.getOffhandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.OFF_HAND;
            else {
                if (Advanced3Config.scaffoldAutoSwitch) {
                    Inventory inventory = player.fabricPlayer.getInventory();
                    try {
                        serverSlot = ContainerUtils.findItem(inventory, BlockItem.class, ContainerUtils.SlotType.HOTBAR);
                        inventory.selected = serverSlot;
                        clientSlot = serverSlot;
                        hand = InteractionHand.MAIN_HAND;
                    } catch (ContainerUtils.ItemNotFoundException e) {
                        return;
                    }
                }
            }

            if (Advanced3Config.scaffoldNoSprint)
                player.fabricPlayer.setSprinting(false);
            if (Advanced3Config.scaffoldDoRotation) {
                if (Advanced3Config.scaffoldSilentRotation)
                    PlayerRotation.silentRotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos), player.currentOnGround);
                else
                    PlayerRotation.rotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos));
            }
            InteractionResult interactionResult = TRPlayer.CLIENT.gameMode.useItemOn(player.fabricPlayer, hand, hitResult);
            if (interactionResult.shouldSwing() && !Advanced3Config.scaffoldNoSwing) player.fabricPlayer.swing(hand);
            lastPlaceTime = player.upTime;
        }
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> basepacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (!Advanced3Config.scaffoldKeepRotation) return false;
        if (basepacket instanceof ServerboundMovePlayerPacket packet)
            return PlayerRotation.cancelRotationPacket(packet, connection, listener, ci);
        return false;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.scaffoldEnabled || !ModuleConfig.aaaPASModeEnabled;
    }
}
