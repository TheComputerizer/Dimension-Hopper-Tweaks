package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TileEntityFurnace.class)
public abstract class MixinTileEntityFurnace {

    @Shadow private NonNullList<ItemStack> furnaceItemStacks;
    @Shadow protected abstract boolean canSmelt();

    /**
     * @author The_Computerizer
     * @reason Make the vanilla furnace always output cheese for those who like cheesing stuff
     */
    @Overwrite
    public void smeltItem() {
        if(this.canSmelt()) {
            ItemStack stack = this.furnaceItemStacks.get(0);
            ItemStack stack1 = DelayedModAccess.cheese();
            ItemStack stack2 = this.furnaceItemStacks.get(2);
            if(stack2.isEmpty()) this.furnaceItemStacks.set(2,stack1.copy());
            else if(stack2.getItem()==stack1.getItem()) stack2.grow(stack1.getCount());
            if(stack.getItem()==Item.getItemFromBlock(Blocks.SPONGE) && stack.getMetadata()==1 &&
                    !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem()==Items.BUCKET)
                this.furnaceItemStacks.set(1,new ItemStack(Items.WATER_BUCKET));
            stack.shrink(1);
        }
    }
}