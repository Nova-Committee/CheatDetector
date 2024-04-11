package top.infsky.cheatdetector.anticheat.fixs;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Fix;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class FlagDetector extends Fix {
    public int disableTick = 60;
    public Level lastWorld;
    public boolean shouldReduce = false;  // world change or something else
    public Queue<Runnable> tasks = new LinkedBlockingQueue<>();

    public FlagDetector(@NotNull TRPlayer player) {
        super("FlagDetector", player);
        assert CheatDetector.CLIENT.player != null;
        lastWorld = CheatDetector.CLIENT.player.getCommandSenderWorld();
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
        if (CONFIG().getAdvanced2().isFlagDetectorShouldReduceKnownTeleport()) shouldReduce = true;
        violations = 0;
        disableTick = CONFIG().getAdvanced2().getFlagDetectorWorldChangedDisableTick();
    }

    @Override
    public void _onTeleport() {
        if (disableTick > 0) return;
        flag();
    }

    @Override
    public void flag() {
        if (CheatDetector.CLIENT.player == null) return;

        if (!CONFIG().getFixes().isFlagDetectorEnabled()) return;

        disableTick++;
        tasks.add(() -> {
            violations++;
            CheatDetector.CLIENT.player.refreshDimensions();
            if (!CONFIG().getAlert().isDisableBuffer())
                if (violations % getAlertBuffer() != 0) return;
            customMsg(Component.translatable("chat.cheatdetector.alert.flagDetected").getString() + ChatFormatting.DARK_GRAY + violations);
        });
    }

    @Override
    public long getAlertBuffer() {
        return CONFIG().getAdvanced2().getFlagDetectorAlertBuffer();
    }
}
