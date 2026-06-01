package me.bread.myth_wizardry.mixins;

import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.api.content.util.RegistryUtils;
import com.binaris.wizardry.setup.registries.Elements;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.registers.ExtSpellTiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryUtils.class)
public class RegistryUtilsMixin {

    @Inject(method = "getWand", at = @At("HEAD"), cancellable = true, remap = false)
    private static void redirectGetWand(SpellTier tier, Element element, CallbackInfoReturnable<Item> cir) {
        String wandName = "wand_" + tier.getOrCreateLocation().getPath();
        if (element != Elements.MAGIC && element !=null) wandName += "_" + element.getLocation().getPath();
        Item wand = BuiltInRegistries.ITEM.get(new ResourceLocation(MythWizardry.MOD_ID, wandName));
        MythWizardry.LOGGER.info(wand.toString());
        if (tier == ExtSpellTiers.MYTHICAL) {

            if (wand != Items.AIR) {
                cir.setReturnValue(wand);
            }
        }
    }
}