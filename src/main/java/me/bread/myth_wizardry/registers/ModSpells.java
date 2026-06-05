package me.bread.myth_wizardry.registers;

import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.core.registry.EBRegistries;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.spells.magic.advanced.magic_shield.MagicShield;
import me.bread.myth_wizardry.spells.magic.advanced.ManaRay;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSpells {
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(EBRegistries.SPELL, MythWizardry.MOD_ID);

    public static final RegistryObject<Spell> MANA_RAY = SPELLS.register("mana_ray", ManaRay::new);

    public static final RegistryObject<Spell> MAGIC_SHIELD = SPELLS.register("magic_shield", MagicShield::new);

}
