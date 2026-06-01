package me.bread.myth_wizardry.tiers;

import com.binaris.wizardry.api.content.spell.SpellTier;
import net.minecraft.ChatFormatting;

public class DivineTier extends SpellTier {
    public DivineTier() {
        super(10000,24,-1,5,ChatFormatting.GOLD,30_000);
    }
}
