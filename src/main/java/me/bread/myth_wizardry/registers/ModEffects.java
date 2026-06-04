package me.bread.myth_wizardry.registers;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.effects.MagicShield;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MythWizardry.MOD_ID);

    public static final RegistryObject<MobEffect> MAGIC_SHIELD =
            MOB_EFFECTS.register("custom_effect", MagicShield::new);

}
