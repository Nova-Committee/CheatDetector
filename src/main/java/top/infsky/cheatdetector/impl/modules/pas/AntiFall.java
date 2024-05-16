package top.infsky.cheatdetector.impl.modules.pas;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.utils.world.LevelUtils;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;

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
        ClientLevel level = LevelUtils.getClientLevel();
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
    }

    @Override
    public boolean isDisabled() {
        if (!ModuleConfig.antiFallEnabled || player.fabricPlayer.isFallFlying()) return true;
        if (ModuleConfig.noFallEnabled) {
            customMsg(Component.translatable("cheatdetector.chat.alert.couldNotWorkWith").withStyle(ChatFormatting.DARK_RED).getString().formatted(
                    Component.translatable("cheatdetector.config.modules.antiFallEnabled.pretty_name").getString(),
                    Component.translatable("cheatdetector.config.modules.noFallEnabled.pretty_name").getString()
            ));
            CheatDetector.CONFIG_HANDLER.configManager.setValue("antiFallEnabled", false);
            return true;
        }
        return !ModuleConfig.aaaPASModeEnabled;
    }
}
