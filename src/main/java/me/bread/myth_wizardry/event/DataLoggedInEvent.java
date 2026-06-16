package me.bread.myth_wizardry.event;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.packets.NetworkHandler;
import me.bread.myth_wizardry.packets.PacketUpdateWidgetStates;
import me.bread.myth_wizardry.registers.ModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID)
public class DataLoggedInEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

        CompoundTag data = player.getPersistentData();

        NetworkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new PacketUpdateWidgetStates(
                        data.getBoolean("myth_wizardry_mana_first"),
                        data.getBoolean("myth_wizardry_mana_second"),
                        data.getInt("myth_wizardry_mode")
                )
        );
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {

            CompoundTag oldData = event.getOriginal().getPersistentData();
            CompoundTag newData = event.getEntity().getPersistentData();

            newData.putBoolean("myth_wizardry_mana_first", oldData.getBoolean("myth_wizardry_mana_first"));
            newData.putBoolean("myth_wizardry_mana_second", oldData.getBoolean("myth_wizardry_mana_second"));
            newData.putInt("myth_wizardry_mode", oldData.getInt("myth_wizardry_mode"));
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();

            for (Attribute attr : List.of(
                    ModAttributes.MANA.get(),
                    ModAttributes.MAX_MANA.get(),
                    ModAttributes.REGEN_MANA.get(),
                    ModAttributes.MAGE.get(),
                    ModAttributes.MAGE_TIER.get()
            )) {
                var oldInstance = oldPlayer.getAttribute(attr);
                var newInstance = newPlayer.getAttribute(attr);

                if (oldInstance != null && newInstance != null) {
                    newInstance.setBaseValue(oldInstance.getBaseValue());
                }
            }
        }
    }

}
