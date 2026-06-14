package me.bread.myth_wizardry.items.mage;

import com.binaris.wizardry.api.content.item.IManaItem;
import me.bread.myth_wizardry.registers.ModItems;
import me.bread.myth_wizardry.utils.ConfigHelper;
import me.bread.myth_wizardry.utils.ManaData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class MageHeartItem extends Item {
    public static final String MAX_MANA_TAG = "max_mana";
    public static final String REGEN_MANA_TAG = "regen_mana";
    public static final String RARITY_TAG = "rarity";

    public MageHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player && ConfigHelper.isManaSystemEnabled() && ConfigHelper.isMageHeartEnabled()) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                int maxMana = tag.getInt(MAX_MANA_TAG);
                int regenMana = tag.getInt(REGEN_MANA_TAG);

                ManaData.setMaxMana(player, maxMana);
                ManaData.setRegenMana(player,regenMana);
                ManaData.setMage(player);

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }

        return result;
    }
    @Override
    public Rarity getRarity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(RARITY_TAG)) {
            int rarityOrdinal = tag.getInt(RARITY_TAG);
            return switch (rarityOrdinal) {
                case 0 -> Rarity.COMMON;
                case 1 -> Rarity.UNCOMMON;
                case 2 -> Rarity.RARE;
                case 3 -> Rarity.EPIC;
                default -> Rarity.COMMON;
            };
        }
        return Rarity.COMMON;
    }
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null && ConfigHelper.isManaSystemEnabled() && ConfigHelper.isMageHeartEnabled()) {
            if (tag.contains(MAX_MANA_TAG)) {
                tooltip.add(Component.translatable("tooltip.myth_wizardry.mage_heart.max_mana",
                        String.format("%d", tag.getInt(MAX_MANA_TAG))).withStyle(ChatFormatting.BLUE));
            }
            if (tag.contains(REGEN_MANA_TAG)) {
                tooltip.add(Component.translatable("tooltip.myth_wizardry.mage_heart.reg_mana",
                        String.format("%d", tag.getInt(REGEN_MANA_TAG))).withStyle(ChatFormatting.BLUE));
            }
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public int getMaxMana(ItemStack stack) {
        return stack.getOrCreateTag().getInt(MAX_MANA_TAG);
    }

    public int getRegenMana(ItemStack stack) {
        return stack.getOrCreateTag().getInt(REGEN_MANA_TAG);
    }

    public static ItemStack createHeart(int maxMana, int regenMana, Rarity rarity) {
        ItemStack stack = new ItemStack(ModItems.MAGE_HEART.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(MAX_MANA_TAG, maxMana);
        tag.putInt(REGEN_MANA_TAG, regenMana);

        tag.putInt(RARITY_TAG, rarity.ordinal());

        return stack;
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 80;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }
    public static ItemStack createHeart(int maxMana, int regenMana) {
        return createHeart(maxMana, regenMana, Rarity.COMMON);
    }
}
