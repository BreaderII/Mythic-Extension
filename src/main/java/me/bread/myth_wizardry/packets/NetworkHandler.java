package me.bread.myth_wizardry.packets;


import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.spells.magic.advanced.magic_shield.MagicShieldCreateSyncPacketS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "0.1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MythWizardry.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(
                packetId++,
                MagicShieldCreateSyncPacketS2C.class,
                MagicShieldCreateSyncPacketS2C::encode,
                MagicShieldCreateSyncPacketS2C::decode,
                MagicShieldCreateSyncPacketS2C::handle
        );
        INSTANCE.registerMessage(
                packetId++,
                PacketUpdateWidgetStates.class,
                PacketUpdateWidgetStates::encode,
                PacketUpdateWidgetStates::decode,
                PacketUpdateWidgetStates::handle
        );
    }
}