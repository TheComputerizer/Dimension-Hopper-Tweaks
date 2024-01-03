package mods.thecomputerizer.dimhoppertweaks.common.jei;

import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StargateCategory implements IRecipeCategory<StargatePreviewWrapper>, ITooltipCallback<ItemStack> {

    private final IDrawableStatic background;
    private final IDrawableStatic slot;

    public StargateCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150,110);
        this.slot = helper.getSlotDrawable();
    }
    @Override
    public String getUid() {
        return DHTRef.MODID+".ancientStargate";
    }

    @Override
    public String getTitle() {
        return TextUtil.getTranslated("category.dimhoppertweaks.ancient_stargate");
    }

    @Override
    public String getModName() {
        return DHTRef.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.slot.draw(minecraft, 0, 0);
        this.slot.draw(minecraft, 0, 19);
        this.slot.draw(minecraft, 0, 38);
        this.slot.draw(minecraft, 135, 0);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, StargatePreviewWrapper wrapper, IIngredients ingr) {
        layout.getItemStacks().init(0, true, 0, 0);
        layout.getItemStacks().init(1, true, 0, 19);
        layout.getItemStacks().init(2, true, 0, 38);
        layout.getItemStacks().init(3, false, 135, 0);
        layout.getItemStacks().addTooltipCallback(this);
        layout.getItemStacks().set(ingr);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack stack, List<String> list) {
        String last = list.get(list.size()-1);
        list.remove(list.size()-1);
        String base = "category.dimhoppertweaks.ancient_stargate.";
        if(slotIndex==0) list.add(TextUtil.getTranslated(base+"schematic"));
        else if(slotIndex==1) list.add(TextUtil.getTranslated(base+"heavy_duty"));
        else if(slotIndex==2) list.add(TextUtil.getTranslated(base+"corner"));
        else if(slotIndex==3) list.add(TextUtil.getTranslated(base+"addresser"));
        list.add(last);
    }
}
