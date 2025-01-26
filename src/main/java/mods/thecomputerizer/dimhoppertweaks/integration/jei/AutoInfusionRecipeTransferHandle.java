package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mcjty.lib.varia.ItemStackList;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mcjty.rftools.compat.jei.PacketSendRecipe;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mods.thecomputerizer.dimhoppertweaks.common.containers.AutoInfusionContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static mcjty.rftools.network.RFToolsMessages.INSTANCE;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class AutoInfusionRecipeTransferHandle implements IRecipeTransferHandler<AutoInfusionContainer> {
    
    public static void register(IRecipeTransferRegistry registry) {
        registry.addRecipeTransferHandler(new AutoInfusionRecipeTransferHandle(),"aoa3.infusion");
    }
    
    @Override public Class<AutoInfusionContainer> getContainerClass() {
        return AutoInfusionContainer.class;
    }
    
    @Nullable @Override
    public IRecipeTransferError transferRecipe(
            AutoInfusionContainer container, IRecipeLayout layout, EntityPlayer player, boolean b, boolean b1) {
        Map<Integer,? extends IGuiIngredient<ItemStack>> ingredients = layout.getItemStacks().getGuiIngredients();
        CrafterBaseTE inventory = container.getCrafterTE();
        BlockPos pos = inventory.getPos();
        if(b1) {
            ItemStackList items = ItemStackList.create(11);
            for(Entry<Integer,? extends IGuiIngredient<ItemStack>> entry : ingredients.entrySet()) {
                int recipeSlot = entry.getKey();
                List<ItemStack> allIngredients = entry.getValue().getAllIngredients();
                if(!allIngredients.isEmpty()) items.set(recipeSlot,allIngredients.get(0));
            }
            INSTANCE.sendToServer(new PacketSendRecipe(items, pos));
        }
        return null;
    }
}