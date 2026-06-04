package me.bread.myth_wizardry;

import com.binaris.wizardry.api.content.util.CastItemDataHelper;
import com.binaris.wizardry.content.item.WandItem;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.logging.LogUtils;
import me.bread.myth_wizardry.registers.ModCreativeTabs;
import me.bread.myth_wizardry.registers.ModItems;
import me.bread.myth_wizardry.registers.ModSpells;
import me.bread.myth_wizardry.registers.ModTiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        ModSpells.SPELLS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}