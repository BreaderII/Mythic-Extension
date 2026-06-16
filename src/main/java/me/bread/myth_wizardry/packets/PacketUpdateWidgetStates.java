package me.bread.myth_wizardry.packets;

import me.bread.myth_wizardry.gui.InventoryWidget;
import me.bread.myth_wizardry.utils.WizardData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketUpdateWidgetStates {
    private final boolean manaDrain;
    private final boolean autoRegen;
    private final int mode;

    public PacketUpdateWidgetStates(boolean manaDrain, boolean autoRegen, int mode) {
        this.manaDrain = manaDrain;
        this.autoRegen = autoRegen;
        this.mode = mode;
    }

    public static void encode(PacketUpdateWidgetStates msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.manaDrain);
        buf.writeBoolean(msg.autoRegen);
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
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {

            if (context.getDirection().getReceptionSide().isServer()) {

                ServerPlayer player = context.getSender();

                if (player == null) return;

                WizardData.set(player,
                        msg.manaDrain,
                        msg.autoRegen,
                        msg.mode
                );

                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new PacketUpdateWidgetStates(
                                msg.manaDrain,
                                msg.autoRegen,
                                msg.mode
                        )
                );
            }

            else {
                InventoryWidget.updateFromPacket(
                        msg.manaDrain,
                        msg.autoRegen,
                        msg.mode
                );
            }
        });

        context.setPacketHandled(true);
    }
}