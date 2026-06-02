package me.bread.myth_wizardry.registers;

import com.binaris.wizardry.WizardryMainMod;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.core.registry.EBRegistries;
import com.binaris.wizardry.setup.registries.RegisterFunction;
import com.binaris.wizardry.setup.registries.Spells;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.spells.magic.mythical.ManaRay;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModSpells {
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(EBRegistries.SPELL, MythWizardry.MOD_ID);

    public static final RegistryObject<Spell> MANA_RAY = SPELLS.register("mana_ray", ManaRay::new);

}
