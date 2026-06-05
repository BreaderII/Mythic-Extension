package me.bread.myth_wizardry.spells.magic.advanced.magic_shield;

import com.binaris.wizardry.api.content.item.IElementValue;
import com.binaris.wizardry.api.content.item.ITierValue;
import com.binaris.wizardry.api.content.spell.*;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spell.abstr.BuffSpell;
import com.binaris.wizardry.setup.registries.Elements;
import com.binaris.wizardry.setup.registries.SpellTiers;
import me.bread.myth_wizardry.packets.NetworkHandler;
import me.bread.myth_wizardry.registers.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class MagicShield extends BuffSpell {
    public MagicShield() {
        super(1, 1, 1, ModEffects.MAGIC_SHIELD);
    }

    @Override
    protected boolean applyEffects(CastContext ctx, LivingEntity target) {
        Element element = null;
        SpellTier tier = null;

        if (ctx.caster().getUseItem().getItem() instanceof IElementValue elementValue) {
            element = elementValue.getElement();
        }
        if (ctx.caster().getUseItem().getItem() instanceof ITierValue tierValue) {
            tier = tierValue.getTier(ctx.caster().getUseItem());
        }

        if (element == null) element = Elements.MAGIC;
        if (tier == null) tier = SpellTiers.NOVICE;

        int elementColor = element.getColors()[1];
        int color = 0xFF000000 | (elementColor & 0x00FFFFFF);

        target.getPersistentData().putInt("element_magic_shield", color);
        target.getPersistentData().putInt("tier_magic_shield", tier.getLevel());
        target.getPersistentData().putFloat("health_magic_shield", ctx.modifiers().get(SpellModifiers.POTENCY) * tier.getLevel() * 10);
        if (target instanceof ServerPlayer serverPlayer) {
            MagicShieldCreateSyncPacketS2C packet = new MagicShieldCreateSyncPacketS2C(
                    target.getId(),
                    color,
                    tier.getLevel(),
                    ctx.modifiers().get(SpellModifiers.POTENCY) * tier.getLevel() * 10
            );

            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
        }
        System.out.println(ctx.modifiers().get(SpellModifiers.POTENCY) * tier.getLevel() * 10);
        return super.applyEffects(ctx, target);
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.MAGIC, SpellType.BUFF, SpellAction.POINT, 50, 10, 40)
                .add(BuffSpell.getEffectDurationProperty(ModEffects.MAGIC_SHIELD.get()), 4000)
                .build();
    }
}