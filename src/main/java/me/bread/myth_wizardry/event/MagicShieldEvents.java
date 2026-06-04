package me.bread.myth_wizardry.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.models.MagicShieldModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3fc;

@Mod.EventBusSubscriber(modid = MythWizardry.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicShieldEvents {

    private static final ResourceLocation TEXTURE = new ResourceLocation(MythWizardry.MOD_ID, "textures/entity/magic_shield.png");
    private static MagicShieldModel model;

    private static MagicShieldModel getModel() {
        if (model == null) {
            model = new MagicShieldModel(
                    Minecraft.getInstance().getEntityModels().bakeLayer(MagicShieldModel.LAYER_LOCATION)
            );
        }
        return model;
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();

        // Проверяем, нужно ли рендерить щит (например, есть ли эффект)
        if (shouldRenderShield(player)) {
            renderShield(event.getPoseStack(), event.getMultiBufferSource(),
                    event.getPartialTick(), player,event);
        }
    }

    private static boolean shouldRenderShield(Player player) {
        return true;
    }

    private static void renderShield(PoseStack poseStack, MultiBufferSource bufferSource,
                                     float partialTick, Player player,RenderPlayerEvent.Post event) {
        poseStack.pushPose();

        // Центрируем на игроке
        poseStack.translate(0, player.getBbHeight() / 2, 0);
        poseStack.scale(0.8f, 0.8f, 0.8f);

        // Включаем правильные настройки для прозрачности
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false); // Отключаем запись в буфер глубины для прозрачных объектов

        MagicShieldModel model = getModel();
        model.setupAnim(player, 0, 0, player.tickCount + partialTick, 0, 0);

        // Используем правильный RenderType для прозрачных объектов
        VertexConsumer vertexConsumer = bufferSource.getBuffer(
                RenderType.entityTranslucent(TEXTURE)
        );

        model.renderToBuffer(poseStack, vertexConsumer,
                event.getPackedLight(), OverlayTexture.NO_OVERLAY,
                1.0f, 1.0f, 1.0f, 0.8f);

        // Возвращаем настройки
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }
}
