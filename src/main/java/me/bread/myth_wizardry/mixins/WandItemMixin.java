package me.bread.myth_wizardry.mixins;

import com.binaris.wizardry.api.content.item.IManaItem;
import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.util.CastItemDataHelper;
import com.binaris.wizardry.api.content.util.CastItemUtils;
import com.binaris.wizardry.api.content.util.RegistryUtils;
import com.binaris.wizardry.content.item.ArcaneTomeItem;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.core.platform.Services;
import me.bread.myth_wizardry.MythWizardry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



import javax.annotation.Nullable;


@Mixin(WandItem.class)
public abstract class WandItemMixin {

    @Shadow @Final private SpellTier tier;
    @Shadow @Final private Element element;

    @Shadow protected abstract void checkLevelUp(PlayerCastContext ctx, ItemStack stack, int progression);
    @Shadow public abstract int getMana(ItemStack stack);

    @Inject(method = "applyTierUpgrade", at = @At("HEAD"), cancellable = true,remap = false)
    private void applyTierUpgrade(@Nullable Player player, ItemStack wand, ItemStack tomeStack,
                                  CallbackInfoReturnable<ItemStack> cir) {
        if (!(tomeStack.getItem() instanceof ArcaneTomeItem tomeItem)) {
            cir.setReturnValue(wand);
            return;
        }

        SpellTier nextTier = tomeItem.getTier(tomeStack);

        if (tier == nextTier) {
            cir.setReturnValue(wand);
            return;
        }

        int currentProgression = CastItemDataHelper.getProgression(wand);
        int requiredProgression = nextTier != null ? nextTier.getProgression() : 0;
        boolean hasEnough = currentProgression >= requiredProgression;

        if (player == null || hasEnough) {

            int newProgression = Math.max(0, currentProgression - requiredProgression);
            CastItemDataHelper.setProgression(wand, newProgression);
            if (player != null) {

                try {
                    Services.OBJECT_DATA.getWizardData(player).setTierReached(tier);
                } catch (Exception e) {
                    MythWizardry.LOGGER.error("Failed to call setTierReached: " + e.getMessage());
                }
            }

            ItemStack newWand = new ItemStack(RegistryUtils.getWand(nextTier, element));
            newWand.setTag(wand.getTag());
            ((IManaItem) newWand.getItem()).setMana(newWand, getMana(wand));
            tomeStack.shrink(1);
            cir.setReturnValue(newWand);
            Services.REGISTRY_UTIL.getTiers().forEach(tier -> MythWizardry.LOGGER.info(tier.toString()));
            return;
        }
        cir.setReturnValue(wand);
    }

    @Inject(method = "handleProgression", at = @At("HEAD"),remap = false)
    protected void handleProgression(PlayerCastContext ctx, Spell spell, ItemStack stack, CallbackInfo ci) {
        boolean shouldAddProgression = spell.isInstantCast()
                ? ctx.castingTicks() == 0
                : ctx.castingTicks() % 20 == 0;

        if (!shouldAddProgression) return;

        int progression = Math.max(1, CastItemUtils.calcCastProgression(spell, ctx.modifiers()));
        CastItemDataHelper.addProgression(stack, progression);
        checkLevelUp(ctx, stack, progression);
    }

    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true)
    private void rewriteIsFoil(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        int currentProgression = CastItemDataHelper.getProgression(stack);
        SpellTier nextTier = tier.next();
        int requiredProgression = nextTier != null ? nextTier.getProgression() : 0;
        boolean hasEnough = currentProgression >= requiredProgression;

        cir.setReturnValue(hasEnough);
    }
}