package me.bread.myth_wizardry.event;

import com.binaris.wizardry.WizardryMainMod;
import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.setup.registries.Spells;
import com.mojang.blaze3d.systems.RenderSystem;
import me.bread.myth_wizardry.MythWizardry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;


@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WandTooltipRenderer {

    private static final int ICON_SIZE = 16;
    private static final int ICON_SPACING = 2;
    private static final int ICONS_PER_ROW = 5;
    private static final int PADDING = 4;
    private static final int TEXT_LINE_SPACING = 2;

    private static final ResourceLocation SPELL_NONE =
            new ResourceLocation(WizardryMainMod.MOD_ID, "textures/spells/none.png");

    private static final int VANILLA_BG_TOP = 0xF0101010;
    private static final int VANILLA_BG_BOTTOM = 0xF0101010;

    private static final int GRAY_BORDER = 0x4F4F4F;
    private static final int FULL_ALPHA = 0xFF;
    private static final int INNER_BORDER_ALPHA = 0x50;

    private static final float VANILLA_WEIGHT = 0.90f;
    private static final float ELEMENT_WEIGHT = 0.10f;
    private static final float OUTER_GRAY_WEIGHT = 0.70f;
    private static final float OUTER_TIER_WEIGHT = 0.30f;

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof WandItem wand)) return;

        Spell[] spells = wand.getSpells(stack);
        if (spells == null || spells.length == 0) return;

        Spell currentSpell = wand.getCurrentSpell(stack);
        boolean hasActiveSpell = currentSpell != null && currentSpell != Spells.NONE;

        event.setCanceled(true);

        GuiGraphics graphics = event.getGraphics();
        Minecraft mc = Minecraft.getInstance();
        var font = mc.font;

        List<Component> tooltipLines = stack.getTooltipLines(mc.player, TooltipFlag.Default.NORMAL);
        if (tooltipLines.isEmpty()) {
            tooltipLines = List.of(stack.getHoverName());
        }

        int textWidth = 0;
        for (Component line : tooltipLines) {
            textWidth = Math.max(textWidth, font.width(line));
        }
        int textHeight = tooltipLines.size() * (font.lineHeight + TEXT_LINE_SPACING) - TEXT_LINE_SPACING;
        textWidth = Math.max(textWidth, 80);

        int spellCount = spells.length;
        int rows = (spellCount + ICONS_PER_ROW - 1) / ICONS_PER_ROW;
        int gridWidth = ICONS_PER_ROW * (ICON_SIZE + ICON_SPACING) - ICON_SPACING;
        int gridHeight = rows * (ICON_SIZE + ICON_SPACING) - ICON_SPACING;

        int baseHeight = PADDING + textHeight + (gridHeight > 0 ? TEXT_LINE_SPACING + gridHeight : 0) + PADDING;
        int tooltipHeight = baseHeight;

        boolean showAdvancedId = mc.options.advancedItemTooltips;
        int idHeight = 0;
        String idString = null;
        int idWidth = 0;
        if (showAdvancedId) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (itemId != null) {
                idString = itemId.toString();
                idWidth = font.width(idString);
                idHeight = font.lineHeight + 2;
                tooltipHeight += idHeight;
            }
        }

        int tooltipWidth = Math.max(textWidth, gridWidth);
        if (showAdvancedId && idString != null) {
            tooltipWidth = Math.max(tooltipWidth, idWidth);
        }
        tooltipWidth += PADDING * 2;

        int x = event.getX() + 12;
        int y = event.getY() - 12;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        if (x + tooltipWidth > screenWidth) x = event.getX() - 12 - tooltipWidth;
        if (y + tooltipHeight > screenHeight) y = event.getY() + 12;
        if (y < 0) y = 0;

        int zLevel = 400;

        int[] bgGradient = getMixedBackgroundGradient(wand.getElement());
        int[] outerBorderGradient = getOuterBorderGradient(wand.getTier(stack));
        int[] innerBorderGradient = getInnerBorderGradient(wand.getTier(stack));

        renderCustomTooltipBackground(graphics, x, y, tooltipWidth, tooltipHeight, zLevel,
                outerBorderGradient[0], outerBorderGradient[1],
                bgGradient[0], bgGradient[1],
                innerBorderGradient[0], innerBorderGradient[1]);

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, zLevel + 10);

        int textX = x + PADDING - 4;
        int textY = y + PADDING - 4;
        for (Component line : tooltipLines) {
            graphics.drawString(font, line, textX, textY, 0xFFFFFF, true);
            textY += font.lineHeight + TEXT_LINE_SPACING;
        }

        if (gridHeight > 0) {
            int startIconY = textY;
            int iconXStart = x + PADDING - 4;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            for (int i = 0; i < spellCount; i++) {
                Spell spell = spells[i];
                ResourceLocation icon;
                if (spell == null || spell == Spells.NONE) {
                    icon = SPELL_NONE;
                } else {
                    icon = spell.getIcon();
                    if (icon == null) continue;
                }

                int row = i / ICONS_PER_ROW;
                int col = i % ICONS_PER_ROW;
                int iconX = iconXStart + col * (ICON_SIZE + ICON_SPACING);
                int iconYPos = startIconY + row * (ICON_SIZE + ICON_SPACING);

                graphics.blit(icon, iconX, iconYPos, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

                if (hasActiveSpell && spell != null && spell != Spells.NONE && spell == currentSpell) {
                    drawWhiteBorder(graphics, iconX, iconYPos, ICON_SIZE);
                }
            }
            RenderSystem.disableBlend();
        }

        if (showAdvancedId && idString != null) {
            int idX = x;
            int idY = y + tooltipHeight - font.lineHeight - 2;
            graphics.drawString(font, idString, idX, idY, ChatFormatting.DARK_GRAY.getColor(), false);
        }

        graphics.pose().popPose();
    }

    private static int mixColor(int colorA, int colorB, float weightA, float weightB) {
        int a = (colorA >> 24) & 0xFF;
        int r = (int)(((colorA >> 16) & 0xFF) * weightA + ((colorB >> 16) & 0xFF) * weightB);
        int g = (int)(((colorA >> 8) & 0xFF) * weightA + ((colorB >> 8) & 0xFF) * weightB);
        int b = (int)((colorA & 0xFF) * weightA + (colorB & 0xFF) * weightB);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int[] getMixedBackgroundGradient(Element element) {
        if (element == null) return new int[]{VANILLA_BG_TOP, VANILLA_BG_BOTTOM};
        int[] elemColors = element.getColors();
        if (elemColors == null || elemColors.length < 2) return new int[]{VANILLA_BG_TOP, VANILLA_BG_BOTTOM};
        int elemTop = 0xFF000000 | (elemColors[0] & 0x00FFFFFF);
        int elemBottom = 0xFF000000 | (elemColors[1] & 0x00FFFFFF);
        int mixedTop = mixColor(VANILLA_BG_TOP, elemTop, VANILLA_WEIGHT, ELEMENT_WEIGHT);
        int mixedBottom = mixColor(VANILLA_BG_BOTTOM, elemBottom, VANILLA_WEIGHT, ELEMENT_WEIGHT);
        return new int[]{mixedTop, mixedBottom};
    }

    private static int[] getOuterBorderGradient(SpellTier tier) {
        int grayColor = (FULL_ALPHA << 24) | GRAY_BORDER;
        if (tier == null) return new int[]{grayColor, grayColor};
        ChatFormatting colorFormat = tier.getColor();
        if (colorFormat == null || colorFormat.getColor() == null) return new int[]{grayColor, grayColor};
        int rgb = colorFormat.getColor();
        int tierColor = (FULL_ALPHA << 24) | (rgb & 0x00FFFFFF);
        int mixed = mixColor(grayColor, tierColor, OUTER_GRAY_WEIGHT, OUTER_TIER_WEIGHT);
        return new int[]{mixed, mixed};
    }

    private static int[] getInnerBorderGradient(SpellTier tier) {
        int defaultColor = (INNER_BORDER_ALPHA << 24) | GRAY_BORDER;
        if (tier == null) return new int[]{defaultColor, defaultColor};
        ChatFormatting colorFormat = tier.getColor();
        if (colorFormat == null || colorFormat.getColor() == null) return new int[]{defaultColor, defaultColor};
        int rgb = colorFormat.getColor();
        int tierColor = (INNER_BORDER_ALPHA << 24) | (rgb & 0x00FFFFFF);
        return new int[]{tierColor, tierColor};
    }

    private static void renderCustomTooltipBackground(GuiGraphics graphics,
                                                      int x, int y, int width, int height, int zLevel,
                                                      int outerBorderTop, int outerBorderBottom,
                                                      int bgTop, int bgBottom,
                                                      int innerBorderTop, int innerBorderBottom) {
        int left = x - 3;
        int top = y - 3;
        int right = left + width + 6;
        int bottom = top + height + 6;

        renderHorizontalLine(graphics, left, top - 1, right - left, zLevel, outerBorderTop);
        renderHorizontalLine(graphics, left, bottom, right - left, zLevel, outerBorderBottom);
        renderRectangle(graphics, left, top, right - left, bottom - top, zLevel, bgTop, bgBottom);
        renderVerticalLineGradient(graphics, left - 1, top, bottom - top, zLevel, outerBorderTop, outerBorderBottom);
        renderVerticalLineGradient(graphics, right, top, bottom - top, zLevel, outerBorderTop, outerBorderBottom);
        renderInnerFrame(graphics, left, top + 1, right - left, bottom - top, zLevel, innerBorderTop, innerBorderBottom);
    }

    private static void renderHorizontalLine(GuiGraphics graphics, int x, int y, int width, int zLevel, int color) {
        graphics.fill(x, y, x + width, y + 1, zLevel, color);
    }

    private static void renderVerticalLineGradient(GuiGraphics graphics, int x, int y, int height, int zLevel, int colorTop, int colorBottom) {
        graphics.fillGradient(x, y, x + 1, y + height, zLevel, colorTop, colorBottom);
    }

    private static void renderRectangle(GuiGraphics graphics, int x, int y, int width, int height, int zLevel, int colorTop, int colorBottom) {
        graphics.fillGradient(x, y, x + width, y + height, zLevel, colorTop, colorBottom);
    }

    private static void renderInnerFrame(GuiGraphics graphics, int left, int top, int width, int height, int zLevel, int colorTop, int colorBottom) {
        renderVerticalLineGradient(graphics, left, top, height - 2, zLevel, colorTop, colorBottom);
        renderVerticalLineGradient(graphics, left + width - 1, top, height - 2, zLevel, colorTop, colorBottom);
        renderHorizontalLine(graphics, left, top - 1, width, zLevel, colorTop);
        renderHorizontalLine(graphics, left, top + height - 2, width, zLevel, colorBottom);
    }

    private static void drawWhiteBorder(GuiGraphics graphics, int x, int y, int size) {
        graphics.fill(x - 1, y - 1, x + size + 1, y, 0xFFFFFFFF);
        graphics.fill(x - 1, y + size, x + size + 1, y + size + 1, 0xFFFFFFFF);
        graphics.fill(x - 1, y, x, y + size, 0xFFFFFFFF);
        graphics.fill(x + size, y, x + size + 1, y + size, 0xFFFFFFFF);
        graphics.fill(x - 1, y - 1, x, y, 0xFFFFFFFF);
        graphics.fill(x + size, y - 1, x + size + 1, y, 0xFFFFFFFF);
        graphics.fill(x - 1, y + size, x, y + size + 1, 0xFFFFFFFF);
        graphics.fill(x + size, y + size, x + size + 1, y + size + 1, 0xFFFFFFFF);
    }
}