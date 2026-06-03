package me.bread.myth_wizardry.registers;

import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.core.registry.EBRegistries;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.tiers.MythicalTier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTiers {
//    public static final SpellTier DIVINE = tier("divine", DivineTier::new);
//    public static final SpellTier PRIMORDIAL = tier("primordial", PrimordialTier::new);
    public static final DeferredRegister<SpellTier> TIERS = DeferredRegister.create(EBRegistries.TIER, MythWizardry.MOD_ID);

    public static final RegistryObject<SpellTier> MYTHICAL = TIERS.register("mythical", MythicalTier::new);

}
