package me.bread.myth_wizardry.spells.magic.advanced.magic_shield;

import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.bread.myth_wizardry.MythWizardry;
import me.bread.myth_wizardry.registers.ModEffects;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

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
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (hasShield(player)) {
            renderShieldInWorld(event.getPoseStack(), event.getCamera(),
                    event.getPartialTick(), player);
        }
    }
    @SubscribeEvent
    public static void onHurtEntity(LivingHurtEvent event){
        if(hasShield(event.getEntity())){
            float health = event.getEntity().getPersistentData().getFloat("health_magic_shield");
            if(health >= event.getAmount()){
                event.getEntity().getPersistentData().putFloat("health_magic_shield",health - event.getAmount());
                event.setAmount(0);
            }else {
                event.setAmount(event.getAmount() - health);
                event.getEntity().getPersistentData().putFloat("health_magic_shield",0);
                event.getEntity().removeEffect(ModEffects.MAGIC_SHIELD.get());
            }
        }
    }


    private static boolean hasShield(LivingEntity entity) {
        return entity.hasEffect(ModEffects.MAGIC_SHIELD.get());
    }

    private static void renderShieldInWorld(PoseStack poseStack, Camera camera,
                                            float partialTick, Player player) {
        poseStack.pushPose();

        Vec3 cameraPos = camera.getPosition();
        Vec3 playerPos = player.getPosition(partialTick);

        poseStack.translate(
                playerPos.x - cameraPos.x,
                playerPos.y + player.getBbHeight() / 2 - cameraPos.y - (double) 3 / 16,
                playerPos.z - cameraPos.z
        );

        int tier = player.getPersistentData().getInt("tier_magic_shield");
        poseStack.scale(0.8f + 0.1f * tier, 0.8f + 0.1f * tier, 0.8f + 0.1f * tier);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);

        MagicShieldModel model = getModel();
        model.setupAnim(player, 0, 0, player.tickCount + partialTick, 0, 0);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));

        int light = LevelRenderer.getLightColor(player.level(), player.blockPosition());

        int color = player.getPersistentData().getInt("element_magic_shield");

        Color color_v = new Color(color);

        model.renderToBuffer(poseStack, vertexConsumer,
                light, OverlayTexture.NO_OVERLAY,
                color_v.getRed()/255f, color_v.getGreen()/255f, color_v.getBlue()/255f, 1);

        bufferSource.endBatch(RenderType.entityTranslucent(TEXTURE));

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }
}