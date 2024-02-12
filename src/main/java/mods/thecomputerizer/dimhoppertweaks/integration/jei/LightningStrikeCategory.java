package mods.thecomputerizer.dimhoppertweaks.integration.jei;

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
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LightningStrikeCategory implements IRecipeCategory<LightningStrikeWrapper>, ITooltipCallback<ItemStack> {

    private final IDrawableStatic background;
    private final IDrawableStatic slot;

    public LightningStrikeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150,119);
        this.slot = helper.getSlotDrawable();
    }

    @Override
    public String getUid() {
        return DHTRef.MODID+".lightningStrike";
    }

    @Override
    public String getTitle() {
        return TextUtil.getTranslated("category.dimhoppertweaks.lightning_strike");
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
    public void drawExtras(Minecraft mc) {
        this.slot.draw(mc,135,0);
        this.slot.draw(mc,0,0);
        this.slot.draw(mc,0,19);
        this.slot.draw(mc,0,38);
        this.slot.draw(mc,0,57);
        this.slot.draw(mc,0,76);
        this.slot.draw(mc,0,95);
        this.slot.draw(mc,19,0);
        this.slot.draw(mc,19,19);
        this.slot.draw(mc,19,38);
        this.slot.draw(mc,19,57);
        this.slot.draw(mc,19,76);
        this.slot.draw(mc,19,95);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, LightningStrikeWrapper wrapper, IIngredients ingr) {
        layout.getItemStacks().init(0,true,135,0);
        layout.getItemStacks().init(1,true,0,0);
        layout.getItemStacks().init(2,true,0,19);
        layout.getItemStacks().init(3,true,0,38);
        layout.getItemStacks().init(4,true,0,57);
        layout.getItemStacks().init(5,true,0,76);
        layout.getItemStacks().init(6,true,0,95);
        layout.getItemStacks().init(7,false,19,0);
        layout.getItemStacks().init(8,false,19,19);
        layout.getItemStacks().init(9,false,19,38);
        layout.getItemStacks().init(10,false,19,57);
        layout.getItemStacks().init(11,false,19,76);
        layout.getItemStacks().init(12,false,19,95);
        layout.getItemStacks().addTooltipCallback(this);
        layout.getItemStacks().set(ingr);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack stack, List<String> list) {
        String last = list.get(list.size()-1);
        list.remove(list.size()-1);
        String extra = slotIndex>=0 ? (slotIndex==0 ? "catalyst" : (slotIndex<7 ? "input" : "output")) : null;
        if(Objects.nonNull(extra)) list.add(TextUtil.getTranslated("category.dimhoppertweaks.lightning_strike."+extra));
        list.add(last);
    }
}
