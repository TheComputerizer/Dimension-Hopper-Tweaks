package mods.thecomputerizer.dimhoppertweaks.client.model;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelFinalBoss extends AnimatedGeoModel<EntityFinalBoss> {

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFinalBoss entity) {
        return Constants.res("animations/boss.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(EntityFinalBoss entity) {
        return Constants.res("geo/final_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFinalBoss entity) {
        return Constants.res("textures/entity/final_boss.png");
    }
}
