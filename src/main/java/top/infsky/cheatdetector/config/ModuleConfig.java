package top.infsky.cheatdetector.config;

import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;
import top.infsky.cheatdetector.config.utils.ConfigCategory;
import top.infsky.cheatdetector.config.utils.ConfigPredicate;

public class ModuleConfig {
    @Config(category = ConfigCategory.MODULES)
    public static boolean aaaPASModeEnabled = false;

    @Hotkey(hotkey = "INSERT")
    @Config(category = ConfigCategory.MODULES)
    public static boolean clickGUIEnabled = false;
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
    @Config(category = ConfigCategory.MODULES, predicate = ConfigPredicate.PASMode.class)
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
    @Config(category = ConfigCategory.MODULES, predicate = ConfigPredicate.PASMode.class)
    public static boolean backtrackEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean noteBotEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean sayHackerEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES, predicate = ConfigPredicate.PASMode.class)
    public static boolean jumpResetEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES, predicate = ConfigPredicate.PASMode.class)
    public static boolean scaffoldEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean aimAssistEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean handSpinEnabled = false;

    @Hotkey
    @Config(category = ConfigCategory.MODULES)
    public static boolean debugEnabled = false;
}
