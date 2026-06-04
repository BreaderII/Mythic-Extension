package me.bread.myth_wizardry.registers;

import com.binaris.wizardry.WizardryMainMod;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.core.registry.EBRegistries;
import com.binaris.wizardry.setup.registries.SpellTiers;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.tiers.MythicalTier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModTiers {
public static final SpellTier MYTHICAL = tier("mythical", MythicalTier::new);
//    public static final SpellTier DIVINE = tier("divine", DivineTier::new);
//    public static final SpellTier PRIMORDIAL = tier("primordial", PrimordialTier::new);


    public static SpellTier tier(String name, Supplier<SpellTier> tierSupplier) {
        var tier = tierSupplier.get();
        tier.setLocation(WizardryMainMod.location(name));
        SpellTiers.TIERS.put(name, tier);
        return tier;
    }
}
