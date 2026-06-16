package me.bread.myth_wizardry.gui;


import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.packets.NetworkHandler;
import me.bread.myth_wizardry.packets.PacketUpdateWidgetStates;
import me.bread.myth_wizardry.utils.ConfigHelper;
import me.bread.myth_wizardry.utils.ManaData;
import me.bread.myth_wizardry.utils.WizardData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;


@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID, value = Dist.CLIENT)
public class InventoryWidget {
    private static final ResourceLocation MODE_BUTTON_TEXTURE_0 =
            new ResourceLocation(MythWizardry.MOD_ID, "textures/gui/mode_button_0.png");
    private static final ResourceLocation MODE_BUTTON_TEXTURE_1 =
            new ResourceLocation(MythWizardry.MOD_ID, "textures/gui/mode_button_1.png");
    private static final ResourceLocation MODE_BUTTON_TEXTURE_2 =
            new ResourceLocation(MythWizardry.MOD_ID, "textures/gui/mode_button_2.png");
    private static final ResourceLocation WIDGET_BACKGROUND =
            new ResourceLocation(MythWizardry.MOD_ID, "textures/gui/widget_background.png");

    private static int currentMode = 0;
    private static boolean manaFirstEnabled = false;
    private static boolean manaSecondEnabled = false;
    private static Checkbox checkboxManaFirst;
    private static Checkbox checkboxAutoSecond;
    private static ImageButton modeButton;

    private static final Component MANA_DRAIN_TOOLTIP = Component.translatable("tooltip.myth_wizardry.mana_first");
    private static final Component AUTO_REGEN_TOOLTIP = Component.translatable("tooltip.myth_wizardry.mana_second");
    private static final Component MODE_TOOLTIP_0 = Component.translatable("tooltip.myth_wizardry.mode_0");
    private static final Component MODE_TOOLTIP_1 = Component.translatable("tooltip.myth_wizardry.mode_1");
    private static final Component MODE_TOOLTIP_2 = Component.translatable("tooltip.myth_wizardry.mode_2");

    private static int backgroundX, backgroundY;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen invScreen && ConfigHelper.isManaSystemEnabled() && ManaData.isMage(Minecraft.getInstance().player)) {
            Player player = Minecraft.getInstance().player;
            if (player == null || player.isCreative() || player.isSpectator()) {
                return;
            }

            int elementWidth = 16;
            int elementHeight = 20;
            int spacing = 6;
            int padding = 4;

            int startX = invScreen.getGuiLeft() + padding;
            int startY = invScreen.getGuiTop() - 24;

            backgroundX = startX - padding;
            backgroundY = startY - padding;

            checkboxManaFirst = new Checkbox(
                    startX, startY,
                    elementWidth, elementHeight,
                    Component.empty(),
                    manaFirstEnabled
            );

            checkboxAutoSecond = new Checkbox(
                    startX + elementWidth + spacing, startY,
                    elementWidth, elementHeight,
                    Component.empty(),
                    manaSecondEnabled
            );

            modeButton = new ImageButton(
                    startX + (elementWidth + spacing) * 2, startY + 2,
                    elementWidth, elementWidth,
                    0, 0,
                    elementWidth,
                    getModeTexture(),
                    elementWidth, elementWidth,
                    (button) -> {
                        currentMode = (currentMode + 1) % 3;
                        updateModeButtonTexture();
                        syncToServer();
                    },
                    Component.empty()
            );

            event.addListener(checkboxManaFirst);
            event.addListener(checkboxAutoSecond);
            event.addListener(modeButton);
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen && ConfigHelper.isManaSystemEnabled() && ManaData.isMage(Minecraft.getInstance().player)) {
            Player player = Minecraft.getInstance().player;
            if (player == null || player.isCreative() || player.isSpectator()) {
                return;
            }

            GuiGraphics guiGraphics = event.getGuiGraphics();
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();

            guiGraphics.blit(WIDGET_BACKGROUND, backgroundX, backgroundY, 0, 0, 68, 28, 68, 28);

            if (checkboxManaFirst != null) {
                checkboxManaFirst.render(guiGraphics, mouseX, mouseY, event.getPartialTick());
                if (checkboxManaFirst.isMouseOver(mouseX, mouseY)) {
                    guiGraphics.renderTooltip(
                            Minecraft.getInstance().font,
                            MANA_DRAIN_TOOLTIP,
                            mouseX,
                            mouseY
                    );
                }
            }

            if (checkboxAutoSecond != null) {
                checkboxAutoSecond.render(guiGraphics, mouseX, mouseY, event.getPartialTick());
                if (checkboxAutoSecond.isMouseOver(mouseX, mouseY)) {
                    guiGraphics.renderTooltip(
                            Minecraft.getInstance().font,
                            AUTO_REGEN_TOOLTIP,
                            mouseX,
                            mouseY
                    );
                }
            }

            if (modeButton != null) {
                modeButton.render(guiGraphics, mouseX, mouseY, event.getPartialTick());
                Component tooltip = switch (currentMode) {
                    case 1 -> MODE_TOOLTIP_1;
                    case 2 -> MODE_TOOLTIP_2;
                    default -> MODE_TOOLTIP_0;
                };
                if (modeButton.isMouseOver(mouseX, mouseY)) {
                    guiGraphics.renderTooltip(
                            Minecraft.getInstance().font,
                            tooltip,
                            mouseX,
                            mouseY
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen && ConfigHelper.isManaSystemEnabled() && ManaData.isMage(Minecraft.getInstance().player)) {
            double mouseX = event.getMouseX();
            double mouseY = event.getMouseY();
            int button = event.getButton();

            boolean handled = false;

            if (checkboxManaFirst != null && checkboxManaFirst.isMouseOver(mouseX, mouseY) && button == 0) {
                manaFirstEnabled = !manaFirstEnabled;
                checkboxManaFirst.onPress();
                handled = true;
                syncToServer();
            }
            if (checkboxAutoSecond != null && checkboxAutoSecond.isMouseOver(mouseX, mouseY) && button == 0) {
                manaSecondEnabled = !manaSecondEnabled;
                checkboxAutoSecond.onPress();
                handled = true;
                syncToServer();
            }
            if (modeButton != null && modeButton.mouseClicked(mouseX, mouseY, button)) {
                handled = true;
            }

            if (handled) {
                event.setCanceled(true);
            }
        }
    }

    private static void syncToServer() {
        NetworkHandler.INSTANCE.sendToServer(
                new PacketUpdateWidgetStates(
                        manaFirstEnabled,
                        manaSecondEnabled,
                        currentMode
                )
        );
    }



    public static void updateFromPacket(boolean manaFirst, boolean manaSecond, int mode) {
        manaFirstEnabled = manaFirst;
        manaSecondEnabled = manaSecond;
        currentMode = mode;
    }

    private static ResourceLocation getModeTexture() {
        return switch (currentMode) {
            case 1 -> MODE_BUTTON_TEXTURE_1;
            case 2 -> MODE_BUTTON_TEXTURE_2;
            default -> MODE_BUTTON_TEXTURE_0;
        };
    }

    private static void updateModeButtonTexture() {
        if (modeButton != null) {
            try {
                Field textureField = ImageButton.class.getDeclaredField("resourceLocation");
                textureField.setAccessible(true);
                textureField.set(modeButton, getModeTexture());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean isManaDrainEnabledServer(Player player) {
        if (!(player instanceof ServerPlayer)) return false;

        CompoundTag data = player.getPersistentData();
        return data.getBoolean("myth_wizardry_mana_first");
    }
    public static boolean isAutoRegenEnabledServer(Player player) {
        if (!(player instanceof ServerPlayer)) return false;

        CompoundTag data = player.getPersistentData();
        return data.getBoolean("myth_wizardry_mana_second");
    }
    public static int getCurrentModeServer(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return 0;
        }

        CompoundTag data = serverPlayer.getPersistentData();


        return data.getInt("myth_wizardry_mode");
    }

    public enum Mode {
        WAND(0),
        MIXED(2),
        PLAYER(1);

        private final int value;

        Mode(int i) {
            this.value = i;
        }

        public int get() {
            return value;
        }
    }
}