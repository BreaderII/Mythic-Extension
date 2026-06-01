package me.bread.myth_wizardry.items.staff;

import com.binaris.wizardry.api.content.item.IManaItem;
import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.util.CastItemDataHelper;
import com.binaris.wizardry.content.item.ArcaneTomeItem;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.core.platform.Services;
import me.bread.myth_wizardry.utils.ExtRegistryUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import me.bread.myth_wizardry.MythWizardry;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;

import java.util.List;

public class StaffItem extends WandItem {

    public StaffItem(SpellTier tier, Element element) {
        super(tier, element);


    }

    @Override
    public boolean cast(ItemStack stack, Spell spell, PlayerCastContext ctx) {
        if (!ctx.caster().getOffhandItem().isEmpty()) {
            return false;
        }
        return super.cast(stack, spell, ctx);
    }

    @Override
    protected PlayerCastContext createContext(Level level, Player player, InteractionHand hand,
                                              int tick, ItemStack stack, Spell spell) {
        PlayerCastContext ctx = super.createContext(level, player, hand, tick, stack, spell);
        SpellModifiers modifiers = ctx.modifiers();

        int tierLevel = this.getTier(stack).getLevel();
        int bonusPercent = 20 * (tierLevel + 1);
        float multiplier = (100 + bonusPercent) / 100.0f;
        int costBonusPercent = 10 * (tierLevel + 1);
        float costMultiplier = (100 + costBonusPercent) / 100.0f;

        modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * multiplier);
        modifiers.set(SpellModifiers.RANGE, modifiers.get(SpellModifiers.RANGE) * multiplier);
        modifiers.set(SpellModifiers.BLAST, modifiers.get(SpellModifiers.BLAST) * multiplier);
        modifiers.set(SpellModifiers.HEALTH_MODIFIER, modifiers.get(SpellModifiers.HEALTH_MODIFIER) * multiplier);
        modifiers.set(SpellModifiers.DURATION, modifiers.get(SpellModifiers.DURATION) * multiplier);
        modifiers.set(SpellModifiers.COST, modifiers.get(SpellModifiers.COST) * costMultiplier);

        return new PlayerCastContext(level, player, hand, tick, modifiers);
    }

    @Override
    protected ItemStack applyTierUpgrade(@Nullable Player player, ItemStack wand, ItemStack tomeStack) {
        if (!(tomeStack.getItem() instanceof ArcaneTomeItem tomeItem)) {
            return wand;
        }

        SpellTier nextTier = tomeItem.getTier(tomeStack);

        if (this.getTier(wand).getLevel() == nextTier.getLevel()) {
            return wand;
        }

        if (player == null || CastItemDataHelper.getProgression(wand) >= nextTier.getProgression()) {
            int newProgression = Math.max(0, CastItemDataHelper.getProgression(wand) - nextTier.getProgression());
            CastItemDataHelper.setProgression(wand, newProgression);

            if (player != null) {
                Services.OBJECT_DATA.getWizardData(player).setTierReached(this.getTier(wand));
            }

            Item newStaffItem = ExtRegistryUtils.getStaff(nextTier, this.getElement(), MythWizardry.MOD_ID);
            if (newStaffItem == null || newStaffItem == net.minecraft.world.item.Items.AIR) {
                return wand;
            }

            ItemStack newWand = new ItemStack(newStaffItem);
            newWand.setTag(wand.getTag());
            ((IManaItem) newWand.getItem()).setMana(newWand, getMana(wand));
            tomeStack.shrink(1);
            return newWand;
        }
        return wand;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        int tierLevel = this.getTier(stack).getLevel();
        int bonusPercent = 20 * (tierLevel + 1);
        int costPercent = 10 * (tierLevel + 1);

        tooltip.add(Component.translatable("tooltip.myth_wizardry.staff.bonus",
                String.format("%+d%%", bonusPercent)).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.myth_wizardry.staff.cost",
                String.format("%+d%%", costPercent)).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.myth_wizardry.staff.two_handed").withStyle(ChatFormatting.DARK_GRAY));
    }
}