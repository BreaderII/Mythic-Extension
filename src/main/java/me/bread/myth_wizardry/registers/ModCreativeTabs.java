package me.bread.myth_wizardry.registers;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.items.mage.MageHeartItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MythWizardry.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MYTH_WIZARDRY_ITEMS_TAB =
            CREATIVE_TABS.register("myth_wizardry_items", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.myth_wizardry"))
                    .icon(() -> new ItemStack(ModItems.ARCANE_TOME_MYTHICAL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ARCANE_TOME_MYTHICAL.get());
                        output.accept(MageHeartItem.createHeart(250, 1, Rarity.COMMON));
                        output.accept(MageHeartItem.createHeart(250, 2, Rarity.COMMON));
                        output.accept(MageHeartItem.createHeart(500, 2, Rarity.UNCOMMON));
                        output.accept(MageHeartItem.createHeart(500, 3, Rarity.UNCOMMON));
                        output.accept(MageHeartItem.createHeart(850, 3, Rarity.RARE));
                        output.accept(MageHeartItem.createHeart(850, 4, Rarity.RARE));
                        output.accept(MageHeartItem.createHeart(1000, 4, Rarity.EPIC));
                        output.accept(MageHeartItem.createHeart(1000, 5, Rarity.EPIC));
//                        output.accept(ModItems.ARCANE_TOME_DIVINE.get());
//                        output.accept(ModItems.ARCANE_TOME_PRIMORDIAL.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> MYTH_WIZARDRY_WANDS_TAB =
            CREATIVE_TABS.register("myth_wizardry_wands", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.myth_wizardry_wands"))
                    .icon(() -> new ItemStack(ModItems.MYTHICAL_WAND.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.MYTHICAL_WAND.get());
                        output.accept(ModItems.MYTHICAL_WAND_FIRE.get());
                        output.accept(ModItems.MYTHICAL_WAND_ICE.get());
                        output.accept(ModItems.MYTHICAL_WAND_EARTH.get());
                        output.accept(ModItems.MYTHICAL_WAND_LIGHTNING.get());
                        output.accept(ModItems.MYTHICAL_WAND_HEALING.get());
                        output.accept(ModItems.MYTHICAL_WAND_NECROMANCY.get());
                        output.accept(ModItems.MYTHICAL_WAND_SORCERY.get());
                        output.accept(ModItems.NOVICE_STAFF.get());
                        output.accept(ModItems.APPRENTICE_STAFF.get());
                        output.accept(ModItems.ADVANCED_STAFF.get());
                        output.accept(ModItems.MASTER_STAFF.get());
                        output.accept(ModItems.MYTHICAL_STAFF.get());
                    })
                    .build());
}