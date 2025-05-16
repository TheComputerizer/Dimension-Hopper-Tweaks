package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.thecomputerizer.dimhoppertweaks.recipes.LightningStrikeRecipe;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import org.dave.compactmachines3.misc.RenderTickCounter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.client.renderer.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.SRC_ALPHA;
import static net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit;
import static net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit;
import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;

@SuppressWarnings("deprecation")
public class LightningStrikeWrapper implements IRecipeWrapper {

    private static final BlockPos MIN = new BlockPos(-1,0,-1);
    private static final BlockPos MAX = new BlockPos(1,2,1);

    private final LightningStrikeRecipe recipe;

    public LightningStrikeWrapper(LightningStrikeRecipe recipe) {
        this.recipe = recipe;
    }

    @Override public void getIngredients(IIngredients ingr) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(this.recipe.getCatalystStack());
        stacks.add(this.recipe.getEntitySpawnEgg());
        stacks.addAll(this.recipe.getInputStacks());
        ingr.setInputs(ItemStack.class,stacks);
        ingr.setOutput(ItemStack.class,this.recipe.getOutputStacks());
    }

    @Override public void drawInfo(@Nonnull Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        LightningStrikeRenderer renderer = LightningStrikeRenderer.getForRecipe(this.recipe);
        if(Objects.isNull(renderer)) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0f, 216.5f);
        FontRenderer font = mc.fontRenderer;
        String range = String.format("Range = %,.2f Blocks",this.recipe.getRange());
        font.drawString(range,153-font.getStringWidth(range),105,4473924);
        String dimName = getDimensionName(this.recipe.getDimension());
        font.drawString(dimName,0,105+font.FONT_HEIGHT,4473924);
        GlStateManager.popMatrix();
        float angle = (float) RenderTickCounter.renderTicks*25f/128f;
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.setActiveTexture(lightmapTexUnit);
        GlStateManager.setActiveTexture(defaultTexUnit);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516,0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SRC_ALPHA,ONE_MINUS_SRC_ALPHA);
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
        GlStateManager.translate(((float)(recipeWidth/2))*1.1f,(float)(recipeHeight/2),100f);
        GlStateManager.rotate(-25f,1f,0f,0f);
        GlStateManager.rotate(angle,0f,1f,0f);
        GlStateManager.scale(16f,-16f,16f);
        int diffX = MAX.getX()-MIN.getX();
        int diffY = MAX.getY()-MIN.getY();
        int diffZ = MAX.getZ()-MIN.getZ();
        int maxDiff = Math.max(Math.max(diffZ,diffX),diffY)+1;
        float scale = 1f/((float)maxDiff/4f);
        GlStateManager.enableCull();
        GlStateManager.scale(scale,scale,scale);
        renderer.render(0f);
        textureManager.bindTexture(LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.popMatrix();
    }

    private String getDimensionName(int id) {
        try {
            return DimensionType.getById(id).getName();
        } catch (IllegalArgumentException ex) {
            return TextUtil.getTranslated("category.dimhoppertweaks.all.unknown_dimension",id);
        }
    }
}