package mods.thecomputerizer.dimensionhoppertweaks.client.entity.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

import javax.annotation.Nonnull;
import java.util.List;

public class RenderFinalBoss extends RenderBiped<EntityFinalBoss>
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(DimensionHopperTweaks.MODID, "textures/entity/final_boss.png");

    public RenderFinalBoss(RenderManager manager) {
        super(manager, new ModelPlayer(0.0F, false), 0.0F);
        try {
            DimensionHopperTweaks.LOGGER.info("binding model to final boss render");

            DimensionHopperTweaks.LOGGER.info("bound model to final boss render");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityFinalBoss entity)
    {
        return TEXTURES;
    }

    @Override
    protected void applyRotations(EntityFinalBoss entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    @Override
    public void doRender(EntityFinalBoss entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
