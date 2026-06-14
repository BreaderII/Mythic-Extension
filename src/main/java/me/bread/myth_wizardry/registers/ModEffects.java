package me.bread.myth_wizardry.registers;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.spells.magic.advanced.magic_shield.MagicShieldEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MythWizardry.MOD_ID);

    public static final RegistryObject<MobEffect> MAGIC_SHIELD =
            MOB_EFFECTS.register("magic_shield", MagicShieldEffect::new);

}
