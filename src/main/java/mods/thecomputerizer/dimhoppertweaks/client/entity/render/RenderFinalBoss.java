package mods.thecomputerizer.dimhoppertweaks.client.entity.render;

import mods.thecomputerizer.dimhoppertweaks.Constants;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RenderFinalBoss extends AnimatedGeoModel<EntityFinalBoss> {


    public static final ResourceLocation MODEL = new ResourceLocation(Constants.MODID, "models/boss/final_boss.geo.json");
    public static final ResourceLocation TEXTURES = new ResourceLocation(Constants.MODID, "textures/entity/final_boss.png");

    public static final ResourceLocation ANIMATIONS = new ResourceLocation(Constants.MODID, "animation/boss/boss_animations.json");

    @Override
    public ResourceLocation getModelLocation(EntityFinalBoss boss) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFinalBoss boss) {
        return TEXTURES;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFinalBoss boss) {
        return ANIMATIONS;
    }
}
