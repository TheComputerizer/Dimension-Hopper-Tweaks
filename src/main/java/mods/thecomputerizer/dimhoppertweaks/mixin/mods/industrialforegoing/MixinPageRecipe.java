package mods.thecomputerizer.dimhoppertweaks.mixin.mods.industrialforegoing;

import com.buuz135.industrial.api.book.CategoryEntry;
import com.buuz135.industrial.api.book.gui.GUIBookBase;
import com.buuz135.industrial.api.book.page.PageRecipe;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.NORMAL;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@Mixin(value = PageRecipe.class, remap = false)
public class MixinPageRecipe {
    
    @Shadow private IRecipe recipe;
    @Unique private boolean dimhoppertweaks$caughtError;
    
    /**
     * @author The_Computerizer
     * @reason Fix crash
     */
    @Overwrite
    @SideOnly(CLIENT)
    public void drawScreenPost(CategoryEntry entry, GUIBookBase base, int mouseX, int mouseY, float partialTicks, FontRenderer renderer) {
        if(this.dimhoppertweaks$caughtError) return;
        try {
            for(int pos=0; pos<9; pos++) {
                if(this.recipe.getIngredients().get(pos).getMatchingStacks().length==0) continue;
                if(mouseX>=base.getGuiLeft()+25+(pos%3)*18 && mouseX<=base.getGuiLeft()+25+(pos%3)*18+16 &&
                   mouseY>=base.getGuiTop()+69+(pos/3)*18 && mouseY<=base.getGuiTop()+69+(pos/3)*18+16) {
                    ItemStack stack = recipe.getIngredients().get(pos).getMatchingStacks()[0];
                    base.drawHoveringText(stack.getTooltip(null,NORMAL),mouseX,mouseY);
                }
            }
            if(mouseX>=base.getGuiLeft()+25+94 && mouseX<=base.getGuiLeft()+25+94+18 &&
               mouseY>=base.getGuiTop()+69+18 && mouseY<=base.getGuiTop()+69+18+18)
                base.drawHoveringText(recipe.getRecipeOutput().getTooltip(null,NORMAL),mouseX,mouseY);
        } catch(Exception ex) {
            LOGGER.warn("Ignoring exception from Industrial Forgegoing recipe page {}",ex.getMessage());
            this.dimhoppertweaks$caughtError = true;
        }
    }
}