package me.bread.myth_wizardry.event;

import com.binaris.wizardry.api.content.event.SpellCastEvent;
import com.binaris.wizardry.api.content.item.IManaItem;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.core.event.WizardryEventBus;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.gui.InventoryWidget;
import me.bread.myth_wizardry.utils.ConfigHelper;
import me.bread.myth_wizardry.utils.ManaData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CastSpellEvent {

    @SubscribeEvent
    public static void onModSetup(FMLCommonSetupEvent event) {
        registerSpellHandlers();
    }

    private static void registerSpellHandlers() {
        WizardryEventBus bus = WizardryEventBus.getInstance();

        bus.register(SpellCastEvent.Pre.class, event -> {
            if (event.getSource() == SpellCastEvent.Source.WAND &&
                    event.getCaster() instanceof Player player && ManaData.isMage(player) && ConfigHelper.isManaSystemEnabled()) {
                int baseCost = event.getSpell().getCost();
                float costModifier = event.getModifiers().get(SpellModifiers.COST);
                int manaCost = Math.max(1, (int) (baseCost * costModifier));

                if (!hasEnoughManaForSpell(player, manaCost)) {
                    event.setCanceled(true);
                    return;
                }

                consumeManaForSpell(player, manaCost);
            }
        });

        bus.register(SpellCastEvent.Tick.class, event -> {
            if (event.getSource() == SpellCastEvent.Source.WAND &&
                    event.getCaster() instanceof Player player && ManaData.isMage(player) && ConfigHelper.isManaSystemEnabled()) {

                if (event.getTicksCasting() % 20 != 0) {
                    return;
                }

                int baseCost = event.getSpell().getCost();
                float costModifier = event.getModifiers().get(SpellModifiers.COST);
                int manaCost = Math.max(1, (int) (baseCost * costModifier));

                if (!hasEnoughManaForSpell(player, manaCost)) {
                    event.setCanceled(true);
                    return;
                }

                consumeManaForSpell(player, manaCost);
            }
        });
    }

    private static boolean hasEnoughManaForSpell(Player player, int manaCost) {
        boolean manaFirst = InventoryWidget.isManaDrainEnabled();
        boolean manaSecond = InventoryWidget.isAutoRegenEnabled();

        ItemStack wand = player.getMainHandItem();
        int wandMana = 0;
        if (wand.getItem() instanceof IManaItem manaItem) {
            wandMana = manaItem.getMana(wand);
        }
        int playerMana = ManaData.getCurrentMana(player);
        int totalMana = playerMana + wandMana;

        if (manaFirst && manaSecond) {
            return totalMana >= manaCost;
        }

        if (manaFirst) {
            int remaining = manaCost;
            if (playerMana >= remaining) {
                return true;
            }
            remaining -= playerMana;
            return wandMana >= remaining;
        }

        if (manaSecond) {
            int remaining = manaCost;
            if (wandMana >= remaining) {
                return true;
            }
            remaining -= wandMana;
            return playerMana >= remaining;
        }

        return totalMana >= manaCost;
    }

    private static void consumeManaForSpell(Player player, int manaCost) {
        boolean manaFirst = InventoryWidget.isManaDrainEnabled();
        boolean manaSecond = InventoryWidget.isAutoRegenEnabled();

        ItemStack wand = player.getMainHandItem();

        if (manaFirst && manaSecond) {
            int playerMana = ManaData.getCurrentMana(player);
            int fromPlayer = Math.min(playerMana, manaCost);
            int remaining = manaCost - fromPlayer;

            ManaData.removeMana(player, fromPlayer);

            if (remaining > 0 && wand.getItem() instanceof IManaItem manaItem) {
                int wandMana = manaItem.getMana(wand);
                manaItem.setMana(wand, Math.max(wandMana - remaining, 0));
            }
        } else if (manaFirst) {
            int playerMana = ManaData.getCurrentMana(player);
            int fromPlayer = Math.min(playerMana, manaCost);
            int remaining = manaCost - fromPlayer;

            ManaData.removeMana(player, fromPlayer);

            if (remaining > 0 && wand.getItem() instanceof IManaItem manaItem) {
                int wandMana = manaItem.getMana(wand);
                manaItem.setMana(wand, Math.max(wandMana - remaining, 0));
            }
        } else if (manaSecond) {
            int remaining = manaCost;

            if (wand.getItem() instanceof IManaItem manaItem) {
                int wandMana = manaItem.getMana(wand);
                int fromWand = Math.min(wandMana, remaining);
                remaining -= fromWand;
                manaItem.setMana(wand, Math.max(wandMana - fromWand, 0));
            }

            if (remaining > 0) {
                ManaData.removeMana(player, remaining);
            }
        } else {
            int playerMana = ManaData.getCurrentMana(player);
            int fromPlayer = Math.min(playerMana, manaCost);
            int remaining = manaCost - fromPlayer;

            ManaData.removeMana(player, fromPlayer);

            if (remaining > 0 && wand.getItem() instanceof IManaItem manaItem) {
                int wandMana = manaItem.getMana(wand);
                manaItem.setMana(wand, Math.max(wandMana - remaining, 0));
            }
        }
    }
}