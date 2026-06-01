package me.bread.myth_wizardry.mixins;

import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import com.binaris.wizardry.content.menu.slot.SlotItemList;
import me.bread.myth_wizardry.registers.ModItems;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(ArcaneWorkbenchMenu.class)
public class ArcaneWorkbenchMenuMixin {

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)V",
            at = @At("RETURN"),remap = false)
    private void addCustomTomesToUpgradeSlot(int id, Inventory inventory, Container container, CallbackInfo ci) {
        ArcaneWorkbenchMenu self = (ArcaneWorkbenchMenu)(Object)this;

        if (self.slots.size() <= 10) return;

        SlotItemList upgradeSlot = (SlotItemList) self.slots.get(10);

        try {
            Field itemsField = SlotItemList.class.getDeclaredField("items");
            itemsField.setAccessible(true);

            Item[] currentItems = (Item[]) itemsField.get(upgradeSlot);
            List<Item> newItems = new ArrayList<>(Arrays.asList(currentItems));

            newItems.add(ModItems.ARCANE_TOME_MYTHICAL.get());

            itemsField.set(upgradeSlot, newItems.toArray(new Item[0]));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}