package top.infsky.cheatdetector.impl.modules.common;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.AlertConfig;
import top.infsky.cheatdetector.config.ModuleConfig;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FlagDetector extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public int disableTick = 60;
    public Level lastWorld;
    public boolean shouldReduce = false;  // world change or something else
    public @NotNull Queue<Runnable> tasks = new LinkedBlockingQueue<>();

    public FlagDetector(@NotNull TRSelf player) {
        super("FlagDetector", player);
        assert CheatDetector.CLIENT.player != null;
        lastWorld = CheatDetector.CLIENT.player.getCommandSenderWorld();
        instance = this;
    }

    @Override
    public void _onTick() {
        if (CheatDetector.CLIENT.player == null) return;

        for (Runnable task : tasks) {
            if (!shouldReduce) task.run();
        }
        tasks.clear();
        shouldReduce = false;

        if (disableTick > 0) {
            disableTick--;
            return;
        }

        Level currentWorld = CheatDetector.CLIENT.player.getCommandSenderWorld();
        if (currentWorld != lastWorld) {
            _onWorldChange();
        }
        lastWorld = currentWorld;
    }

    public void _onWorldChange() {
        assert CheatDetector.CLIENT.player != null;
        if (Advanced3Config.flagDetectorShouldReduceKnownTeleport) shouldReduce = true;
        violations = 0;
        disableTick = Advanced3Config.flagDetectorWorldChangedDisableTick;
    }

    @Override
    public void _onTeleport() {
        if (disableTick > 0) return;
        flag();
    }

    @Override
    public void flag() {
        if (CheatDetector.CLIENT.player == null) return;
        if (isDisabled()) return;

        disableTick++;
        tasks.add(() -> {
            violations++;
            CheatDetector.CLIENT.player.refreshDimensions();
            if (!AlertConfig.disableBuffer)
                if (violations % getAlertBuffer() != 0) return;
            customMsg(Component.translatable("cheatdetector.chat.alert.flagDetected").getString() + ChatFormatting.DARK_GRAY + violations);
        });
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.flagDetectorEnabled;
    }
    @Override
    public int getAlertBuffer() {
        return Advanced3Config.flagDetectorAlertBuffer;
    }
}
