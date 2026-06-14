package me.bread.myth_wizardry.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MythWizardryCommonConfig {
    public static final ForgeConfigSpec SPEC;
    public static final MythWizardryCommonConfig INSTANCE;

    static {
        final Pair<MythWizardryCommonConfig, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(MythWizardryCommonConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    public final ForgeConfigSpec.BooleanValue enableManaSystem;
    public final ForgeConfigSpec.BooleanValue enableMageHeartItem;
    public final ForgeConfigSpec.BooleanValue enableManaRegen;

    private MythWizardryCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Myth Wizardry Common Configuration")
                .push("common");

        enableManaSystem = builder
                .comment("Enable mana system")
                .define("enableManaSystem", true);

        enableMageHeartItem = builder
                .comment("Enable Mage Heart item")
                .define("enableMageHeartItem", true);

        enableManaRegen = builder
                .comment("Enable mana regeneration")
                .define("enableManaRegen",true);

        builder.pop();
    }
}