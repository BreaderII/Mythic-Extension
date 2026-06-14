package me.bread.myth_wizardry.event;

import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.registers.ModAttributes;
import me.bread.myth_wizardry.utils.ConfigHelper;
import me.bread.myth_wizardry.utils.ManaData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID)
public class ManaRegenEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;
        if(!ManaData.isMage(event.player)) return;


        if (event.player.level().getGameTime() % 20 == 0 && ConfigHelper.isManaSystemEnabled() && ConfigHelper.isManaRegenerationEnabled()) {
            ManaData.regen(event.player);
        }
    }
}
