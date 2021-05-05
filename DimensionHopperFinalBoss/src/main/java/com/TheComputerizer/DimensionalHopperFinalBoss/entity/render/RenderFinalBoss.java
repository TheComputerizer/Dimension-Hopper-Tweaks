package com.TheComputerizer.DimensionalHopperFinalBoss.entity.render;

import com.TheComputerizer.DimensionalHopperFinalBoss.entity.EntityFinalBoss;
import com.TheComputerizer.DimensionalHopperFinalBoss.entity.model.ModelFinalBoss;
import com.TheComputerizer.DimensionalHopperFinalBoss.Reference;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderFinalBoss extends RenderBiped<EntityFinalBoss>
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/finalboss.png");

    public RenderFinalBoss(RenderManager manager)
    {
        super(manager, new ModelPlayer(0.0F, false), 0.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityFinalBoss entity)
    {
        return TEXTURES;
    }
    @Override
    protected void applyRotations(EntityFinalBoss entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}
