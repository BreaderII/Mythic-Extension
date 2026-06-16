package me.bread.myth_wizardry.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class WizardData {
    public static final String KEY_MODE = "myth_wizardry_mode";
    public static final String KEY_FIRST = "myth_wizardry_mana_first";
    public static final String KEY_SECOND = "myth_wizardry_mana_second";

    public static void set(ServerPlayer player, boolean first, boolean second, int mode) {
        CompoundTag data = player.getPersistentData();

        data.putBoolean(WizardData.KEY_FIRST, first);
        data.putBoolean(WizardData.KEY_SECOND, second);
        data.putInt(WizardData.KEY_MODE, mode);
    }
    public static int getMode(ServerPlayer player) {
        return player.getPersistentData().getInt(WizardData.KEY_MODE);
    }

    public static boolean getManaFirst(ServerPlayer player) {
        return player.getPersistentData().getBoolean(WizardData.KEY_FIRST);
    }

    public static boolean getManaSecond(ServerPlayer player) {
        return player.getPersistentData().getBoolean(WizardData.KEY_SECOND);
    }
}
