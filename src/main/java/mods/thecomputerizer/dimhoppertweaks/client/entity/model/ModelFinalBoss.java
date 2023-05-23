package mods.thecomputerizer.dimhoppertweaks.client.entity.model;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelFinalBoss extends AnimatedGeoModel<EntityFinalBoss> {
    @Override
    public ResourceLocation getAnimationFileLocation(EntityFinalBoss entity) {
        return new ResourceLocation(Constants.MODID, "animations/boss.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(EntityFinalBoss entity) {
        return new ResourceLocation(Constants.MODID, "geo/final_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFinalBoss entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/final_boss.png");
    }
}
