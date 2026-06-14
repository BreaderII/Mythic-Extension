package me.bread.myth_wizardry.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.utils.ConfigHelper;
import me.bread.myth_wizardry.utils.ManaData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaOverlay implements IGuiOverlay {

    public static final ManaOverlay INSTANCE = new ManaOverlay();
    private static final ResourceLocation OVERLAY_TEXTURE =
            new ResourceLocation(MythWizardry.MOD_ID, "textures/overlay/magic_eye_mana.png");

    private static final ResourceLocation OVERLAY_TEXTURE_FULL =
            new ResourceLocation(MythWizardry.MOD_ID, "textures/overlay/magic_eye_mana_full.png");

    public static final String OVERLAY_ID = "mana_bar";

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics,
                       float partialTick, int screenWidth, int screenHeight) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        if(ManaData.isMage(mc.player)){
            int currentMana;
            int maxMana;
            try {
                currentMana = ManaData.getCurrentMana(mc.player);
                maxMana = ManaData.getMaxMana(mc.player);
            } catch (NullPointerException e) {
                return;
            }

            int mainTexWidth = 34;
            int mainTexHeight = 34;
            float scale = 1.0f;

            int scaledWidth = (int) (mainTexWidth * scale);
            int scaledHeight = (int) (mainTexHeight * scale);
            int margin = 10;
            int x = screenWidth - scaledWidth - margin;
            int y = screenHeight - scaledHeight - margin;
            if(ConfigHelper.isManaSystemEnabled() && ConfigHelper.showManaBar()){
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();
                poseStack.translate(x, y, 0);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, 0, 0,
                        mainTexWidth, mainTexHeight,
                        mainTexWidth, mainTexHeight);

                float fillPercentage = (float) currentMana / maxMana;

                int fillTexWidth = 6;
                int fillTexFullHeight = 16;

                int fillTexHeight = (int) (fillTexFullHeight * fillPercentage);

                if (fillTexHeight > 0) {
                    int vOffset = fillTexFullHeight - fillTexHeight;

                    int renderX = 14;
                    int renderY = mainTexHeight - fillTexHeight - 9;

                    guiGraphics.blit(
                            OVERLAY_TEXTURE_FULL,
                            renderX,
                            renderY,
                            0,
                            vOffset,
                            fillTexWidth,
                            fillTexHeight,
                            fillTexWidth,
                            fillTexFullHeight
                    );
                }

                poseStack.popPose();
            }
            if(ConfigHelper.isManaSystemEnabled() && ConfigHelper.showManaNumbers()) {
                String currentStr = String.valueOf(currentMana);
                String maxStr = String.valueOf(maxMana);

                int currentWidth = mc.font.width(currentStr);
                int slashWidth = mc.font.width("/");

                int centerX = x + scaledWidth / 2;
                int currentX = centerX - slashWidth / 2 - currentWidth;
                int slashX = centerX - slashWidth / 2;
                int maxX = centerX + slashWidth / 2;
                int textY = y + scaledHeight;

                guiGraphics.drawString(mc.font, currentStr, currentX, textY, 0x55FFFF, true);
                guiGraphics.drawString(mc.font, "/", slashX, textY, 0x55FFFF, true);
                guiGraphics.drawString(mc.font, maxStr, maxX, textY, 0x55FFFF, true);
            }
        }
    }

}