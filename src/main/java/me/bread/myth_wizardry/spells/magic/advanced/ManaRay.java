package me.bread.myth_wizardry.spells.magic.advanced;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.DeferredObject;
import com.binaris.wizardry.api.content.item.IElementValue;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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

            float damage = property(DefaultProperties.DAMAGE) * ctx.modifiers().get(SpellModifiers.POTENCY);
            DamageSource source = MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.MAGIC);

            target.hurt(source, damage);
        }
        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
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
    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        int[] rgb = null;
        if (ctx instanceof PlayerCastContext context) {
            ItemStack stack = context.caster().getItemInHand(context.hand());
            if (stack.getItem() instanceof IElementValue elementValue) {
                Element element = elementValue.getElement();
                if (element != null) {
                    rgb = element.getColors();
                }
            }
        }
        if (rgb == null || rgb.length < 3) {
            rgb = Elements.MAGIC.getColors();
        }

        float randomOffset = 0.5f - ctx.world().random.nextFloat();

        ParticleBuilder.create(EBParticles.SPARKLE)
                .pos(x + randomOffset, y + randomOffset, z + randomOffset)
                .color(rgb[0])
                .scale(2 + ctx.world().random.nextFloat())
                .collide(true)
                .spawn(ctx.world());

        randomOffset = 0.5f - ctx.world().random.nextFloat();

        ParticleBuilder.create(EBParticles.SPARKLE)
                .pos(x + randomOffset, y + randomOffset, z + randomOffset)
                .color(rgb[1])
                .scale(1 + ctx.world().random.nextFloat())
                .collide(true)
                .spawn(ctx.world());

        randomOffset = 0.5f - ctx.world().random.nextFloat();

        ParticleBuilder.create(EBParticles.SPARKLE)
                .pos(x + randomOffset, y + randomOffset, z + randomOffset)
                .color(rgb[2])
                .scale(3 + ctx.world().random.nextFloat())
                .collide(true)
                .spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.MAGIC, SpellType.ATTACK, SpellAction.POINT, 5, 5, 20)
                .add(DefaultProperties.RANGE, 15f)
                .add(DefaultProperties.DAMAGE, 4F)
                .build();
    }
}