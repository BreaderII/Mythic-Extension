package me.bread.myth_wizardry.spells.magic.advanced.magic_shield;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MagicShieldCreateSyncPacketS2C {
    private final int entityId;
    private final int color;
    private final int tierLevel;

    private final float damage;

    public MagicShieldCreateSyncPacketS2C(int entityId, int color, int tierLevel, float maxDamage) {
        this.entityId = entityId;
        this.color = color;
        this.tierLevel = tierLevel;
        damage = maxDamage;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(color);
        buf.writeInt(tierLevel);
        buf.writeFloat(damage);
    }

    public static MagicShieldCreateSyncPacketS2C decode(FriendlyByteBuf buf) {
        return new MagicShieldCreateSyncPacketS2C(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readFloat()
        );
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> net.minecraft.client.Minecraft.getInstance().execute(() -> {
            net.minecraft.world.entity.Entity entity =
                    net.minecraft.client.Minecraft.getInstance().level.getEntity(entityId);

            if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
                livingEntity.getPersistentData().putInt("element_magic_shield", color);
                livingEntity.getPersistentData().putInt("tier_magic_shield", tierLevel);
                livingEntity.getPersistentData().putFloat("health_magic_shield", damage);
            }
        }));
        context.setPacketHandled(true);
    }
}