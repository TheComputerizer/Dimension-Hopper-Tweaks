package mods.thecomputerizer.dimhoppertweaks.mixin.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.blay09.mods.cookingforblockheads.client.gui.GuiRecipeBook;
import net.blay09.mods.cookingforblockheads.compat.jei.JEIAddon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

import static net.blay09.mods.cookingforblockheads.block.ModBlocks.cuttingBoard;

@Mixin(value = JEIAddon.class, remap = false)
public abstract class MixinJEIAddon {
    
    /**
     * @author The_Computerizer
     * @reason Remove the Cow in a Jar category without removing the item itself
     */
    @Overwrite
    public void register(@Nonnull IModRegistry registry) {
        registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<GuiRecipeBook>() {
            public @Nonnull Class<GuiRecipeBook> getGuiContainerClass() {
                return GuiRecipeBook.class;
            }
            public List<Rectangle> getGuiExtraAreas(@Nonnull GuiRecipeBook container) {
                List<Rectangle> list = Lists.newArrayList();
                for(GuiButton button : container.getSortingButtons())
                    list.add(new Rectangle(button.x,button.y,button.width,button.height));
                return list;
            }
            public @Nullable Object getIngredientUnderMouse(@Nonnull GuiRecipeBook container, int mouseX, int mouseY) {
                return null;
            }
        });
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(cuttingBoard));
    }
    
    /**
     * @author The_Computerizer
     * @reason Remove the Cow in a Jar category without removing the item itself
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {}
}