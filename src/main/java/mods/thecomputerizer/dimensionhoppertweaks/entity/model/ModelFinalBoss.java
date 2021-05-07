package mods.thecomputerizer.dimensionhoppertweaks.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;


public class ModelFinalBoss extends ModelBase {
    public ModelRenderer brain;
    public ModelRenderer leftarm;
    public ModelRenderer rightleg;
    public ModelRenderer head;
    public ModelRenderer leftleg;
    public ModelRenderer rightarm;
    public ModelRenderer head_1;
    public ModelRenderer body;
    public ModelRenderer body_1;

    public ModelFinalBoss() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.brain = new ModelRenderer(this, 24, 0);
        this.brain.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.brain.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, 0.0F);
        this.body_1 = new ModelRenderer(this, 16, 32);
        this.body_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body_1.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
        this.leftleg = new ModelRenderer(this, 0, 48);
        this.leftleg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
        this.head_1 = new ModelRenderer(this, 0, 0);
        this.head_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head_1.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.leftarm = new ModelRenderer(this, 48, 48);
        this.leftarm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
        this.rightleg = new ModelRenderer(this, 0, 32);
        this.rightleg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
        this.rightarm = new ModelRenderer(this, 40, 16);
        this.rightarm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.head = new ModelRenderer(this, 32, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body.render(f5);
        this.brain.render(f5);
        this.body_1.render(f5);
        this.leftleg.render(f5);
        this.head_1.render(f5);
        this.leftarm.render(f5);
        this.rightleg.render(f5);
        this.rightarm.render(f5);
        this.head.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
        this.leftarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;

        this.rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
        this.rightarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
    }
}
