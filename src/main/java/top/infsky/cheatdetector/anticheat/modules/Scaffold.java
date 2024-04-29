package top.infsky.cheatdetector.anticheat.modules;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.anticheat.utils.BlockUtils;
import top.infsky.cheatdetector.anticheat.utils.PlayerRotation;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.io.IOException;
import java.util.List;

public class Scaffold extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private long lastPlaceTime = 0;

    public Scaffold(@NotNull TRSelf player) {
        super("Scaffold", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;
        if (TRPlayer.CLIENT.gameMode == null) return;

        BlockPos ground = player.fabricPlayer.blockPosition().below();

        List<BlockPos> needToPlace = new java.util.ArrayList<>();

        needToPlace.add(ground);
        for (int i = 0; i < Advanced3Config.scaffoldExpend; i++) {
            needToPlace.add(ground.relative(player.fabricPlayer.getDirection(), i));
        }

        for (BlockPos blockPos : needToPlace) {
            try (Level level = TRPlayer.CLIENT.level) {
                if (level == null) return;
                if (!level.getBlockState(blockPos).isAir()) continue;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (player.upTime - lastPlaceTime < Advanced3Config.scaffoldPlaceMinDelay) return;

            BlockHitResult hitResult = new BlockHitResult(player.fabricPlayer.position(), BlockUtils.getPlaceSide(blockPos), blockPos, false);

            InteractionHand hand = null;
            if (player.fabricPlayer.getMainHandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.MAIN_HAND;
            else if (player.fabricPlayer.getOffhandItem().getItem() instanceof BlockItem)
                hand = InteractionHand.OFF_HAND;
            else {
                if (Advanced3Config.scaffoldAutoSwitch) {
                    Inventory inventory = player.fabricPlayer.getInventory();
                    for (int i = 0; i <= 8; i++) {
                        if (inventory.getItem(i).getItem() instanceof BlockItem) {
                            inventory.selected = i;
                            hand = InteractionHand.MAIN_HAND;
                            break;
                        }
                    }
                }
            }
            if (hand == null) return;

            if (Advanced3Config.scaffoldNoSprint)
                player.fabricPlayer.setSprinting(false);
            if (Advanced3Config.scaffoldDoRotation) {
                if (Advanced3Config.scaffoldSilentRotation)
                    PlayerRotation.silentRotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos), player.currentOnGround);
                else
                    PlayerRotation.rotate(PlayerRotation.getYaw(blockPos), PlayerRotation.getPitch(blockPos));
            }
            TRPlayer.CLIENT.gameMode.useItemOn(player.fabricPlayer, hand, hitResult);
            if (!Advanced3Config.scaffoldNoSwing) player.fabricPlayer.swing(hand);
            lastPlaceTime = player.upTime;
        }
    }

    @Override
    public boolean _handleMovePlayer(@NotNull ServerboundMovePlayerPacket packet, @NotNull Connection connection, PacketSendListener listener, @NotNull CallbackInfo ci) {
        if (isDisabled()) return false;
        if (!Advanced3Config.scaffoldSilentKeepRotation) return false;
        return PlayerRotation.cancelRotationPacket(packet, connection, listener, ci);
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.scaffoldEnabled;
    }
}
