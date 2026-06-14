package me.bread.myth_wizardry;

import com.mojang.logging.LogUtils;
import me.bread.myth_wizardry.config.MythWizardryClientConfig;
import me.bread.myth_wizardry.config.MythWizardryCommonConfig;
import me.bread.myth_wizardry.gui.ManaOverlay;
import me.bread.myth_wizardry.spells.magic.advanced.magic_shield.MagicShieldModel;
import me.bread.myth_wizardry.packets.NetworkHandler;
import me.bread.myth_wizardry.registers.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MythWizardryCommonConfig.SPEC, "myth_wizardry-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MythWizardryClientConfig.SPEC, "myth_wizardry-client.toml");
        modEventBus.addListener(this::onConfigReload);

        ModAttributes.ATTRIBUTES.register(modEventBus);
        NetworkHandler.register();
        ModSpells.SPELLS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
    }
    private void onConfigReload(ModConfigEvent event) {
        if (event.getConfig().getSpec() == MythWizardryCommonConfig.SPEC) {
            LOGGER.info("Config reloaded!");
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
  @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(MagicShieldModel.LAYER_LOCATION, MagicShieldModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll(ManaOverlay.OVERLAY_ID, ManaOverlay.INSTANCE);
        }
    }
}