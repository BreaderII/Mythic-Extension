package me.bread.myth_wizardry.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateWidgetStates {
    private final boolean mana_first;
    private final boolean mana_second;
    private final int mode;

    public PacketUpdateWidgetStates(boolean manaDrain, boolean autoRegen, int mode) {
        this.mana_first = manaDrain;
        this.mana_second = autoRegen;
        this.mode = mode;
    }

    public static void encode(PacketUpdateWidgetStates msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.mana_first);
        buf.writeBoolean(msg.mana_second);
        buf.writeInt(msg.mode);
    }

    public static PacketUpdateWidgetStates decode(FriendlyByteBuf buf) {
        return new PacketUpdateWidgetStates(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readInt()
        );
    }

    public static void handle(PacketUpdateWidgetStates msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    player.getPersistentData().putBoolean("myth_wizardry_mana_first", msg.mana_first);
                    player.getPersistentData().putBoolean("myth_wizardry_auto_second", msg.mana_second);
                    player.getPersistentData().putInt("myth_wizardry_mode", msg.mode);
                }
            } else {
                Minecraft.getInstance().player.getPersistentData()
                        .putBoolean("myth_wizardry_mana_first", msg.mana_first);
                Minecraft.getInstance().player.getPersistentData()
                        .putBoolean("myth_wizardry_auto_second", msg.mana_second);
                Minecraft.getInstance().player.getPersistentData()
                        .putInt("myth_wizardry_mode", msg.mode);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}