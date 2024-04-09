package top.infsky.cheatdetector.anticheat.fixs;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.anticheat.Check;
import top.infsky.cheatdetector.anticheat.TRPlayer;

import static top.infsky.cheatdetector.CheatDetector.CONFIG;

public class FlagDetector extends Check {
    public int disableTick = 10;
    public Level lastWorld;

    public FlagDetector(@NotNull TRPlayer player) {
        super("FlagDetector", player);
        assert CheatDetector.CLIENT.player != null;
        lastWorld = CheatDetector.CLIENT.player.getCommandSenderWorld();
    }

    @Override
    public void _onTick() {
        if (CheatDetector.CLIENT.player == null) return;

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
        violations = 0;
        disableTick = CheatDetector.CLIENT.player.getDimensionChangingDelay();
    }

    @Override
    public void _onTeleport() {
        if (disableTick > 0) return;
        if (CheatDetector.CLIENT.player == null) return;

        if (!CONFIG().getFixes().isFlagDetectorEnabled()) return;

        violations++;
        disableTick++;
        CheatDetector.CLIENT.player.refreshDimensions();
        customMsg(Component.translatable("chat.cheatdetector.alert.flagDetected").getString() + ChatFormatting.DARK_GRAY + violations);
    }
}
