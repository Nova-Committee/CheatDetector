package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;

public class ModuleConfig {
    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean clickGUIEnabled = true;
    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean flagDetectorEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean spinEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean noRotateSetEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean antiVanishEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean blinkEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean airWalkEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean antiFallEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean fakelagEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean airPlaceEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean invWalkEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean backtrackEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean noteBotEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean sayHackerEnabled = false;
}
