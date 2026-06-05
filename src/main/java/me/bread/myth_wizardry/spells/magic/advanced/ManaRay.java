package me.bread.myth_wizardry.spells.magic.advanced;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.DeferredObject;
import com.binaris.wizardry.api.content.item.IElementValue;
import com.binaris.wizardry.api.content.item.ITierValue;
import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.api.content.spell.SpellType;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.content.spell.DefaultProperties;
import com.binaris.wizardry.content.spell.abstr.RaySpell;
import com.binaris.wizardry.setup.registries.EBDamageSources;
import com.binaris.wizardry.setup.registries.Elements;
import com.binaris.wizardry.setup.registries.SpellTiers;
import com.binaris.wizardry.setup.registries.client.EBParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class ManaRay extends RaySpell {

    public ManaRay() {
        this.particleSpacing(0.5);
        this.particleJitter(0.15);
        this.particleVelocity(0.2);
        this.soundValues(1.5f, 1, 0);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (entityHit.getEntity() instanceof LivingEntity target) {
            if (ctx instanceof PlayerCastContext context) {
                if (target.distanceTo(context.caster()) <= property(DefaultProperties.RANGE)) {
                    dealDamageToEntity(ctx, target);
                    dealAOEDamage(ctx, target.position());
                }
            }
        }
        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        dealAOEDamage(ctx, blockHit.getLocation());
        return true;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return true;
    }

    @Override
    public boolean isInstantCast() {
        return true;
    }

    private void dealDamageToEntity(CastContext ctx, LivingEntity target) {
        if (ctx instanceof PlayerCastContext context) {
            ItemStack stack = context.caster().getItemInHand(context.hand());
            SpellTier tier = getSpellTierFromStack(stack);

            float damage = property(DefaultProperties.DAMAGE) * ctx.modifiers().get(SpellModifiers.POTENCY) * (tier.getLevel() + 1);
            DamageSource source = MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.MAGIC);

            target.hurt(source, damage);
            spawnHitParticles(ctx, target.position());
        }
    }

    private void dealAOEDamage(CastContext ctx, Vec3 center) {
        if (ctx instanceof PlayerCastContext context) {
            ItemStack stack = context.caster().getItemInHand(context.hand());
            SpellTier tier = getSpellTierFromStack(stack);

            float baseDamage = property(DefaultProperties.DAMAGE) * ctx.modifiers().get(SpellModifiers.POTENCY) * (tier.getLevel() + 1);
            float aoeDamage = baseDamage * 0.5f;
            double radius = property(DefaultProperties.RANGE)/5 + 0.5 * getSpellTierFromStack(stack).getLevel();

            AABB aabb = new AABB(
                    center.x - radius, center.y - radius, center.z - radius,
                    center.x + radius, center.y + radius, center.z + radius
            );

            List<LivingEntity> entities = ctx.world().getEntitiesOfClass(
                    LivingEntity.class, aabb,
                    e -> e != ctx.caster() && e.distanceToSqr(center)<= radius
            );

            DamageSource source = MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.MAGIC);

            for (LivingEntity entity : entities) {
                entity.hurt(source, aoeDamage);
                spawnHitParticles(ctx, entity.position());
            }
        }
    }

    private SpellTier getSpellTierFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ITierValue tierValue) {
            SpellTier tier = tierValue.getTier(stack);
            if (tier != null) return tier;
        }
        return SpellTiers.NOVICE;
    }

    private void spawnHitParticles(CastContext ctx, Vec3 position) {
        for (int i = 0; i < 10; i++) {
            spawnParticle(ctx,position.x,position.y,position.z,position.x,position.y,position.z);
        }
        ctx.world().playSound(null, position.x, position.y, position.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        int[] rgb = null;
        SpellTier tier = null;

        if (ctx instanceof PlayerCastContext context) {
            ItemStack stack = context.caster().getItemInHand(context.hand());
            if (stack.getItem() instanceof IElementValue elementValue) {
                Element element = elementValue.getElement();
                if (element != null) rgb = element.getColors();
            }
            if (stack.getItem() instanceof ITierValue tierValue) {
                tier = tierValue.getTier(stack);
            }
        }

        if (tier == null) tier = SpellTiers.NOVICE;
        if (rgb == null || rgb.length < 3) rgb = Elements.MAGIC.getColors();

        int particleCount = (tier.getLevel() + 1) * 3;

        for (int i = 0; i < particleCount; i++) {
            float randomOffset = (ctx.world().random.nextFloat() - 0.5f) * 0.5f;
            int color = rgb[ctx.world().random.nextInt(rgb.length)];

            ParticleBuilder.create(EBParticles.SPARKLE)
                    .pos(x + randomOffset, y + randomOffset, z + randomOffset)
                    .color(color)
                    .scale(1.5f + ctx.world().random.nextFloat() * 2f)
                    .collide(true)
                    .spawn(ctx.world());
        }
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.MAGIC, SpellType.ATTACK, SpellAction.POINT, 20, 10, 20)
                .add(DefaultProperties.RANGE, 15f)
                .add(DefaultProperties.DAMAGE, 2F)
                .build();
    }
}