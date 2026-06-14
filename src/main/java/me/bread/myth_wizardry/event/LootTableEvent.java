package me.bread.myth_wizardry.event;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.items.mage.MageHeartItem;
import me.bread.myth_wizardry.registers.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;

import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID)
public class LootTableEvent {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(BuiltInLootTables.ANCIENT_CITY)) {
            event.getTable().addPool(
                    LootPool.lootPool()
                            .add(LootItem.lootTableItem(ModItems.MAGE_HEART.get())
                                    .setWeight(5)
                                    .apply(new LootItemConditionalFunction.Builder() {
                                        @Override
                                        protected Builder getThis() {
                                            return this;
                                        }

                                        @Override
                                        public LootItemFunction build() {
                                            return new LootItemConditionalFunction(getConditions()) {
                                                @Override
                                                protected ItemStack run(ItemStack stack, LootContext context) {
                                                    RandomSource random = context.getLevel().getRandom();
                                                    double chance = random.nextDouble();
                                                    if (chance < 0.30) {
                                                        return MageHeartItem.createHeart(250, 1, Rarity.COMMON);
                                                    } else if (chance < 0.50) {
                                                        return MageHeartItem.createHeart(250, 2, Rarity.COMMON);
                                                    } else if (chance < 0.65) {
                                                        return MageHeartItem.createHeart(500, 2, Rarity.UNCOMMON);
                                                    } else if (chance < 0.78) {
                                                        return MageHeartItem.createHeart(500, 3, Rarity.UNCOMMON);
                                                    } else if (chance < 0.88) {
                                                        return MageHeartItem.createHeart(850, 3, Rarity.RARE);
                                                    } else if (chance < 0.95) {
                                                        return MageHeartItem.createHeart(850, 4, Rarity.RARE);
                                                    } else if (chance < 0.99) {
                                                        return MageHeartItem.createHeart(1000, 4, Rarity.EPIC);
                                                    } else {
                                                        return MageHeartItem.createHeart(1000, 5, Rarity.EPIC);
                                                    }
                                                }

                                                @Override
                                                public LootItemFunctionType getType() {
                                                    return LootItemFunctions.SET_NBT;
                                                }
                                            };
                                        }
                                    })
                            )
                            .build()
            );
        }
    }
}