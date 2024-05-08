package top.infsky.cheatdetector.impl.modules.common;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.mixins.KeyMappingInvoker;

import java.util.ArrayList;
import java.util.Arrays;

public class InvWalk extends Module {
    @Getter
    @Nullable
    private static Module instance = null;
    public InvWalk(@NotNull TRSelf player) {
        super("InvWalk", player);
        instance = this;
    }

    @Override
    public void _onTick() {
        if (isDisabled()) return;


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
