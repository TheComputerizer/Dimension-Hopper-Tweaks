package mods.thecomputerizer.dimhoppertweaks.mixin.mods.avaritia;

import codechicken.lib.texture.TextureUtils;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModelArmorInfinityAccess;
import morph.avaritia.client.ColourHelper;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@Mixin(value = ModelArmorInfinity.class, remap = false)
public abstract class MixinModelArmorInfinity extends ModelBiped implements ModelArmorInfinityAccess {

    @Shadow private boolean invulnRender;

    @Shadow public abstract void setEyes();

    @Shadow private ModelArmorInfinity.Overlay invulnOverlay;

    @Shadow private ModelArmorInfinity.Overlay overlay;

    @Shadow public abstract void setGems();

    @Shadow public static ResourceLocation eyeTex;

    @Shadow private Random randy;

    @Shadow public static ResourceLocation wingGlowTex;

    @Shadow public static ResourceLocation wingTex;

    @Shadow public abstract void setWings();

    /**
     * @author The_Computerizer
     * @reason Add MoBends animations to overlay renders
     */
    @Overwrite
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean isFlying = entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isFlying && entity.isAirBorne;
        super.render(entity, f, f1, f2, f3, f4, f5);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        CosmicShaderHelper.useShader();
        TextureUtils.bindBlockTexture();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        if (this.invulnRender) {
            GlStateManager.color(1f,1f,1f,0.2f);
            ArmorModelFactory.getArmorModel(this.invulnOverlay,true).render(entity,f,f1,f2,f3,f4,f5);
        }
        ModelBiped betterOverlay = ArmorModelFactory.getArmorModel(this.overlay,true);
        betterOverlay.render(entity, f, f1, f2, f3, f4, f5);
        CosmicShaderHelper.releaseShader();
        mc.renderEngine.bindTexture(eyeTex);
        GlStateManager.disableLighting();
        mc.entityRenderer.disableLightmap();
        long time = mc.player.world.getWorldTime();
        this.setGems();
        double pulse = Math.sin((double)time / 10.0) * 0.5 + 0.5;
        double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
        GlStateManager.color(0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5));
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        super.render(entity, f, f1, f2, f3, f4, f5);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        if(this.invulnRender) {
            long frame = time / 3L;
            this.randy.setSeed(frame * 1723609L);
            float o = this.randy.nextFloat() * 6.0F;
            float[] col = ColourHelper.HSVtoRGB(o, 1.0F, 1.0F);
            GlStateManager.color(col[0], col[1], col[2], 1.0F);
            this.setEyes();
            super.render(entity, f, f1, f2, f3, f4, f5);
        }

        if (!CosmicShaderHelper.inventoryRender) {
            mc.entityRenderer.enableLightmap();
        }

        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (isFlying && !CosmicShaderHelper.inventoryRender) {
            this.setWings();
            mc.renderEngine.bindTexture(wingTex);
            super.render(entity, f, f1, f2, f3, f4, f5);
            CosmicShaderHelper.useShader();
            TextureUtils.bindBlockTexture();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.depthMask(false);
            betterOverlay.render(entity, f, f1, f2, f3, f4, f5);
            CosmicShaderHelper.releaseShader();
            mc.renderEngine.bindTexture(wingGlowTex);
            GlStateManager.disableLighting();
            mc.entityRenderer.disableLightmap();
            GlStateManager.color(0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5));
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            super.render(entity, f, f1, f2, f3, f4, f5);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            if(!CosmicShaderHelper.inventoryRender) mc.entityRenderer.enableLightmap();
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ModelArmorInfinity.Overlay dimhoppertweaks$getOverlay() {
        return this.overlay;
    }

    @Override
    public ModelArmorInfinity.Overlay dimhoppertweaks$getInvulOverlay() {
        return this.invulnOverlay;
    }
}
