package top.infsky.cheatdetector.anticheat.modules;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.io.IOException;

public class AntiFall extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    @Nullable
    private BlockPos fakeBlockPos = null;

    private boolean needToClutch = false;
    public AntiFall(@NotNull TRSelf player) {
        super("AntiFall", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        try (ClientLevel level = TRPlayer.CLIENT.level) {
            if (isDisabled()) {
                if (fakeBlockPos != null && level != null)
                    if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER))
                        level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
                fakeBlockPos = null;
                return;
            }
            if (level == null) return;

            BlockPos current = player.fabricPlayer.blockPosition().below();
            if (!needToClutch && Advanced3Config.antiFallFastClutchOnVoid) {
                boolean isVoid = true;
                try {
                    for (int yPos = -65; yPos <= current.getY(); yPos++) {
                        BlockState blockState = level.getBlockState(current.atY(yPos));
                        if (!blockState.isAir()) {
                            isVoid = false;
                            break;
                        }
                    }
                } catch (Exception e) {
                    customMsg(e.getLocalizedMessage());
                }
                if (isVoid) {
                    needToClutch = true;
                    customMsg(Component.translatable("cheatdetector.chat.alert.clutch").getString());
                }
            }
            if (!needToClutch && !player.fabricPlayer.isFallFlying() && player.fabricPlayer.fallDistance >= Advanced3Config.antiFallFallDistance) {
                needToClutch = true;
                customMsg(Component.translatable("cheatdetector.chat.alert.clutch").getString());
            }

            if (needToClutch) {
                if (player.fabricPlayer.input.shiftKeyDown && player.currentOnGround) current = current.below();
                if (current == fakeBlockPos) return;


                if (fakeBlockPos != null) {
                    if (level.getBlockState(fakeBlockPos).is(Blocks.BARRIER))
                        level.setBlock(fakeBlockPos, Blocks.AIR.defaultBlockState(), 3);
                    fakeBlockPos = null;
                }
                if (level.getBlockState(current).isAir()) {
                    level.setBlock(current, Blocks.BARRIER.defaultBlockState(), 3);
                    fakeBlockPos = current;
                } else {
                    needToClutch = false;
                    if (Advanced3Config.antiFallClutchMsg)
                        customMsg(Component.translatable("cheatdetector.chat.alert.clutchDone").getString());
                    if (Advanced3Config.antiFallAutoDisabled)
                        CheatDetector.CONFIG_HANDLER.configManager.setValue("antiFallEnabled", false);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.antiFallEnabled || ModuleConfig.airWalkEnabled;
    }
}
