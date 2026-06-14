package me.bread.myth_wizardry.registers;

import me.bread.myth_wizardry.MythWizardry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MythWizardry.MOD_ID);

    public static final RegistryObject<Attribute> MANA = ATTRIBUTES.register(
            "mana",
            () -> new RangedAttribute("attribute.myth_wizardry.mana", 1000.0D, 0.0D, 10000.0D).setSyncable(true)
    );

    public static final RegistryObject<Attribute> MAX_MANA = ATTRIBUTES.register(
            "max_mana",
            () -> new RangedAttribute("attribute.myth_wizardry.max_mana", 1000.0D, 1.0D, 10000.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> REGEN_MANA = ATTRIBUTES.register(
            "regen_mana",
            () -> new RangedAttribute("attribute.myth_wizardry.regen_mana", 1.0D, 1.0D, 10000.0D).setSyncable(true)
    );

    public static final RegistryObject<Attribute> MAGE = ATTRIBUTES.register(
            "mage",
            () -> new RangedAttribute("attribute.myth_wizardry.mage", 0.0D, 0.0D, 1.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> MAGE_TIER = ATTRIBUTES.register(
            "mage_tier",
            () -> new RangedAttribute("attribute.myth_wizardry.mage_tier", 0.0D, 0.0D, 100000.0D).setSyncable(true)
    );

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, MANA.get());
        event.add(EntityType.PLAYER, MAX_MANA.get());
        event.add(EntityType.PLAYER, REGEN_MANA.get());
        event.add(EntityType.PLAYER, MAGE.get());
        event.add(EntityType.PLAYER, MAGE_TIER.get());

    }
}