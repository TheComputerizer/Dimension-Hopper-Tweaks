package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import org.dave.compactmachines3.misc.RenderTickCounter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class StargatePreviewWrapper implements IRecipeWrapper {

    private static final BlockPos MIN = new BlockPos(-3,0,-3);
    private static final BlockPos MAX = new BlockPos(3,5,3);
    private final StargateRecipe recipe;

    public StargatePreviewWrapper(StargateRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingr) {
        ingr.setInputs(ItemStack.class,this.recipe.getInputs());
        ingr.setOutput(ItemStack.class,this.recipe.getOutput());
    }
    
    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        AncientStargateRenderer renderer = AncientStargateRenderer.getForDimension(this.recipe.getDimension());
        if(Objects.isNull(renderer)) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0f, 216.5f);
        FontRenderer font = mc.fontRenderer;
        String dimensions = getDimensionsString();
        font.drawString(dimensions,153-font.getStringWidth(dimensions),105,4473924);
        String dimName = getDimensionName(this.recipe.getDimension());
        font.drawString(dimName,0,105,4473924);
        GlStateManager.popMatrix();
        float angle = (float)RenderTickCounter.renderTicks*25f/128f;
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1f,1f,1f,1f);
        GlStateManager.disableFog();
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        if(Minecraft.isAmbientOcclusionEnabled()) GlStateManager.shadeModel(7425);
        else GlStateManager.shadeModel(7424);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(recipeWidth/2), (float)(recipeHeight / 2), 100f);
        GlStateManager.rotate(-25f, 1f, 0f, 0f);
        GlStateManager.rotate(angle, 0f, 1f, 0f);
        GlStateManager.scale(16f, -16f, 16f);
        int diffX = MAX.getX()-MIN.getX();
        int diffY = MAX.getY()-MIN.getY();
        int diffZ = MAX.getZ()-MIN.getZ();
        int maxDiff = Math.max(Math.max(diffZ,diffX),diffY)+1;
        float scale = 1f/((float)maxDiff/4f);
        GlStateManager.enableCull();
        GlStateManager.scale(scale,scale,scale);
        renderer.render(0f);
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.popMatrix();
    }

    private String getDimensionName(int id) {
        try {
            return DimensionType.getById(id).getName();
        } catch (IllegalArgumentException ex) {
            return TextUtil.getTranslated("category.dimhoppertweaks.ancient_stargate.unknown_dimension",id);
        }
    }

    private String getDimensionsString() {
        return String.format("%dx%dx%d",7,6,7);
    }
}
