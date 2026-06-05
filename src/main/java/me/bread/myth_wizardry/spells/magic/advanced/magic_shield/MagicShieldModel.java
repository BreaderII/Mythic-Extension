package me.bread.myth_wizardry.spells.magic.advanced.magic_shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.bread.myth_wizardry.MythWizardry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;


public class MagicShieldModel extends EntityModel<Entity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation(MythWizardry.MOD_ID, "magic_shield"), "main"
	);

	private final ModelPart bb_main;
	private float rotationSpeed = 1.0f;
	private float rotationOffset = 0;

	public MagicShieldModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main",
				CubeListBuilder.create()
						.texOffs(0, 34).addBox(-11.0F, -13.0F, -11.0F, 22.0F, 5.0F, 22.0F, new CubeDeformation(0.0F))
						.texOffs(0, 88).addBox(-10.0F, -8.0F, -10.0F, 20.0F, 6.0F, 20.0F, new CubeDeformation(0.0F))
						.texOffs(88, 60).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-12.0F, -23.0F, -12.0F, 24.0F, 10.0F, 24.0F, new CubeDeformation(0.0F))
						.texOffs(0, 61).addBox(-11.0F, -28.0F, -11.0F, 22.0F, 5.0F, 22.0F, new CubeDeformation(0.0F))
						.texOffs(88, 34).addBox(-10.0F, -34.0F, -10.0F, 20.0F, 6.0F, 20.0F, new CubeDeformation(0.0F))
						.texOffs(80, 88).addBox(-9.0F, -36.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F)
		);

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	public void setRotationSpeed(float speed) {
		this.rotationSpeed = speed;
	}

	public void setRotationOffset(float offset) {
		this.rotationOffset = offset;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
						  float ageInTicks, float netHeadYaw, float headPitch) {
		float rotation = (ageInTicks * rotationSpeed + rotationOffset) % 360;
		bb_main.yRot = (float) Math.toRadians(rotation);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
							   int packedLight, int packedOverlay, float red,
							   float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}