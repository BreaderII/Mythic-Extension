package me.bread.myth_wizardry.mixins;

import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.client.gui.elements.TooltipElementProgressionBar;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.setup.registries.SpellTiers;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipElementProgressionBar.class)
public class TooltipElementProgressionBarMixin {

    @Inject(method = "getNextTier",at ={@At("HEAD")},cancellable = true,remap = false)
    private void getNextTier(ItemStack stack, CallbackInfoReturnable<SpellTier> cir) {
        SpellTier tier = ((WandItem) stack.getItem()).getTier(stack);
        cir.setReturnValue(SpellTiers.getNextByLevel(tier.getLevel() + 1));
        return;
    }
}
