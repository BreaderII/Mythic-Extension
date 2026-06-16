package me.bread.myth_wizardry.event;

import com.binaris.wizardry.api.content.event.SpellCastEvent;
import com.binaris.wizardry.api.content.item.IManaItem;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.core.event.WizardryEventBus;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.gui.InventoryWidget;
import me.bread.myth_wizardry.utils.ConfigHelper;
import me.bread.myth_wizardry.utils.ManaData;
import me.bread.myth_wizardry.utils.WizardData;
import net.minecraft.server.level.ServerPlayer;
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
            if (event.getSource() != SpellCastEvent.Source.WAND) return;
            if (!(event.getCaster() instanceof Player player)) return;
            if (!ManaData.isMage(player) || !ConfigHelper.isManaSystemEnabled()) return;

            int baseCost = event.getSpell().getCost();
            float costModifier = event.getModifiers().get(SpellModifiers.COST);
            int manaCost = Math.max(1, (int) (baseCost * costModifier));

            if (!canEventuallyPay(player, manaCost)) {
                event.setCanceled(true);
            }
        });

        bus.register(SpellCastEvent.Tick.class, event -> {
            if (event.getSource() != SpellCastEvent.Source.WAND) return;
            if (!(event.getCaster() instanceof Player player)) return;
            if (!ManaData.isMage(player) || !ConfigHelper.isManaSystemEnabled()) return;

            if (event.getTicksCasting() % 20 != 0) return;

            int baseCost = event.getSpell().getCost();
            float costModifier = event.getModifiers().get(SpellModifiers.COST);
            int manaCost = Math.max(1, (int) (baseCost * costModifier));

            if (!tryConsumeSmart(player, manaCost)) {
                event.setCanceled(true);
            }
        });
    }

    private static boolean canEventuallyPay(Player player, int cost) {
        if (!(player instanceof ServerPlayer sp)) return true;

        ItemStack wand = sp.getMainHandItem();

        int wandMana = (wand.getItem() instanceof IManaItem m) ? m.getMana(wand) : 0;
        int playerMana = ManaData.getCurrentMana(sp);

        return (wandMana + playerMana) > 0;
    }


    private static boolean tryConsumeSmart(Player player, int cost) {
        if (!(player instanceof ServerPlayer sp)) return true;

        boolean manaFirst = InventoryWidget.isManaDrainEnabledServer(sp);
        boolean manaSecond = InventoryWidget.isAutoRegenEnabledServer(sp);

        ItemStack wand = sp.getMainHandItem();
        IManaItem manaItem = (wand.getItem() instanceof IManaItem m) ? m : null;

        int wandMana = manaItem != null ? manaItem.getMana(wand) : 0;
        int playerMana = ManaData.getCurrentMana(sp);

        int remaining = cost;

        if (manaSecond) {
            int fromPlayer = Math.min(playerMana, remaining);
            ManaData.removeMana(sp, fromPlayer);
            remaining -= fromPlayer;
        }

        if (remaining > 0 && manaItem != null) {
            if (manaFirst || manaSecond) {
                int fromWand = Math.min(wandMana, remaining);
                manaItem.setMana(wand, wandMana - fromWand);
                remaining -= fromWand;
            }
        }

        if (remaining > 0 && manaItem != null && !manaFirst && !manaSecond) {

            int total = wandMana + playerMana;
            if (total < cost) return false;

            int fromWand = Math.min(wandMana, remaining);
            manaItem.setMana(wand, wandMana - fromWand);
            remaining -= fromWand;

            int fromPlayer = Math.min(playerMana, remaining);
            ManaData.removeMana(sp, fromPlayer);
            remaining -= fromPlayer;
        }

        return remaining <= 0;
    }
}