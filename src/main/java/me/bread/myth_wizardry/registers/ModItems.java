package me.bread.myth_wizardry.registers;

import com.binaris.wizardry.content.item.ArcaneTomeItem;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.setup.registries.Elements;
import com.binaris.wizardry.setup.registries.SpellTiers;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.items.staff.StaffItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MythWizardry.MOD_ID);

    public static final RegistryObject<Item> ARCANE_TOME_MYTHICAL = ITEMS.register("arcane_tome_mythical",
            () -> new ArcaneTomeItem(ExtSpellTiers.MYTHICAL){
                @Override
                public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
                    return Rarity.EPIC;
                }
            });

    public static final RegistryObject<WandItem> MYTHICAL_WAND = ITEMS.register("wand_mythical",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,null));

    public static final RegistryObject<WandItem> NOVICE_STAFF = ITEMS.register("staff_novice",
            () -> new StaffItem(SpellTiers.NOVICE, null));

    public static final RegistryObject<WandItem> APPRENTICE_STAFF = ITEMS.register("staff_apprentice",
            () -> new StaffItem(SpellTiers.APPRENTICE, null));

    public static final RegistryObject<WandItem> ADVANCED_STAFF = ITEMS.register("staff_advanced",
            () -> new StaffItem(SpellTiers.ADVANCED, null));

    public static final RegistryObject<WandItem> MASTER_STAFF = ITEMS.register("staff_master",
            () -> new StaffItem(SpellTiers.MASTER, null));

    public static final RegistryObject<WandItem> MYTHICAL_STAFF = ITEMS.register("staff_mythical",
            () -> new StaffItem(ExtSpellTiers.MYTHICAL, null));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_FIRE = ITEMS.register("wand_mythical_fire",
            () -> new WandItem(ExtSpellTiers.MYTHICAL, Elements.FIRE));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_ICE = ITEMS.register("wand_mythical_ice",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,Elements.ICE));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_EARTH = ITEMS.register("wand_mythical_earth",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,Elements.EARTH));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_HEALING = ITEMS.register("wand_mythical_healing",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,Elements.HEALING));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_LIGHTNING = ITEMS.register("wand_mythical_lightning",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,Elements.LIGHTNING));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_NECROMANCY = ITEMS.register("wand_mythical_necromancy",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,Elements.NECROMANCY));

    public static final RegistryObject<WandItem> MYTHICAL_WAND_SORCERY = ITEMS.register("wand_mythical_sorcery",
            () -> new WandItem(ExtSpellTiers.MYTHICAL,Elements.SORCERY));





//    public static final RegistryObject<Item> ARCANE_TOME_DIVINE = ITEMS.register("arcane_tome_divine",
//            () -> new ArcaneTomeItem(ExtSpellTiers.DIVINE));
//    public static final RegistryObject<Item> ARCANE_TOME_PRIMORDIAL = ITEMS.register("arcane_tome_primordial",
//            () -> new ArcaneTomeItem(ExtSpellTiers.PRIMORDIAL));

}
