package me.bread.myth_wizardry;

import com.mojang.logging.LogUtils;
import me.bread.myth_wizardry.spells.magic.advanced.magic_shield.MagicShieldModel;
import me.bread.myth_wizardry.packets.NetworkHandler;
import me.bread.myth_wizardry.registers.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;

@Mod(MythWizardry.MOD_ID)
public class MythWizardry {
    public static final String MOD_ID = "myth_wizardry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MythWizardry(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MixinBootstrap.init();

        MinecraftForge.EVENT_BUS.register(this);
        NetworkHandler.register();
        ModSpells.SPELLS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }    @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(MagicShieldModel.LAYER_LOCATION, MagicShieldModel::createBodyLayer);
        }

    }
}