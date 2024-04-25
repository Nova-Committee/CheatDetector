package top.infsky.cheatdetector.anticheat;

import org.jetbrains.annotations.NotNull;

public class Module extends Fix {
    public Module(String checkName, @NotNull TRSelf player) {
        super(checkName, player);
    }

    @Override
    public void flag() {
    }

    public void flag(String extraMsg) {
    }
}
