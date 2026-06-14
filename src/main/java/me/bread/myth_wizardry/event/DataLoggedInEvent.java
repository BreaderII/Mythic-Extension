package me.bread.myth_wizardry.event;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.packets.NetworkHandler;
import me.bread.myth_wizardry.packets.PacketUpdateWidgetStates;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID)
public class DataLoggedInEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            boolean manaDrain = player.getPersistentData().getBoolean("myth_wizardry_mana_drain");
            boolean autoRegen = player.getPersistentData().getBoolean("myth_wizardry_auto_regen");
            int mode = player.getPersistentData().getInt("myth_wizardry_mode");

            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new PacketUpdateWidgetStates(manaDrain, autoRegen, mode)
            );
        }
    }
}
