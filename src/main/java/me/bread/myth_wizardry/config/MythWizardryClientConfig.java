package me.bread.myth_wizardry.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MythWizardryClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static final MythWizardryClientConfig INSTANCE;

    static {
        final Pair<MythWizardryClientConfig, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(MythWizardryClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    public final ForgeConfigSpec.BooleanValue showManaBar;
    public final ForgeConfigSpec.BooleanValue showManaNumbers;

    private MythWizardryClientConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Myth Wizardry Client Configuration")
                .push("client");

        showManaBar = builder
                .comment("Show mana bar overlay")
                .define("showManaBar", true);

        showManaNumbers = builder
                .comment("Show mana numbers on overlay")
                .define("showManaNumbers", true);


        builder.pop();
    }
}