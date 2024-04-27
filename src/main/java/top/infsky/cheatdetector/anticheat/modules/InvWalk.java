package top.infsky.cheatdetector.anticheat.modules;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import top.infsky.cheatdetector.anticheat.Module;
import top.infsky.cheatdetector.anticheat.TRPlayer;
import top.infsky.cheatdetector.anticheat.TRSelf;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.KeyMappingInvoker;

import java.util.ArrayList;
import java.util.Arrays;

public class InvWalk extends Module {
    public InvWalk(@NotNull TRSelf player) {
        super("InvWalk", player);
    }

    @Override
    public void _onTick() {
        Screen screen = TRPlayer.CLIENT.screen;
        if (screen == null)
            return;

        if (screen instanceof ChatScreen) return;

        ArrayList<KeyMapping> keys =
                new ArrayList<>(Arrays.asList(TRPlayer.CLIENT.options.keyUp,
                        TRPlayer.CLIENT.options.keyDown, TRPlayer.CLIENT.options.keyLeft, TRPlayer.CLIENT.options.keyRight));

        if (Advanced3Config.invWalkAllowSneak)
            keys.add(TRPlayer.CLIENT.options.keyShift);

        if (Advanced3Config.invWalkAllowSprint)
            keys.add(TRPlayer.CLIENT.options.keySprint);

        if (Advanced3Config.invWalkAllowJump)
            keys.add(TRPlayer.CLIENT.options.keyJump);

        long window = TRPlayer.CLIENT.getWindow().getWindow();
        for (KeyMapping key : keys)
            key.setDown(InputConstants.isKeyDown(window, ((KeyMappingInvoker) key).getKey().getValue()));
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.invWalkEnabled;
    }
}
