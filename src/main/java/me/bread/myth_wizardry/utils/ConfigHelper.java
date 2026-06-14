package me.bread.myth_wizardry.utils;

import me.bread.myth_wizardry.config.MythWizardryClientConfig;
import me.bread.myth_wizardry.config.MythWizardryCommonConfig;

public class ConfigHelper {
    public static boolean isManaSystemEnabled() {
        return MythWizardryCommonConfig.INSTANCE.enableManaSystem.get();
    }

    public static boolean isMageHeartEnabled() {
        return MythWizardryCommonConfig.INSTANCE.enableMageHeartItem.get();
    }
    public static boolean isManaRegenerationEnabled() {
        return MythWizardryCommonConfig.INSTANCE.enableManaRegen.get();
    }

    public static boolean showManaBar() {
        return MythWizardryClientConfig.INSTANCE.showManaBar.get();
    }

    public static boolean showManaNumbers() {
        return MythWizardryClientConfig.INSTANCE.showManaNumbers.get();
    }
}
