package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.util.XLFoodUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityFurnace.class)
public abstract class MixinTileEntityFurnace {

    @Shadow
    private NonNullList<ItemStack> furnaceItemStacks;

    @Shadow
    abstract boolean canSmelt();

    /**
     * @author The_Computerizer
     * @reason Make the vanilla furnace always output cheese for those who like cheesing stuff
     */
    @Overwrite
    public void smeltItem() {
        if (this.canSmelt()) {
            Block block = ((TileEntity)(Object)this).getWorld().getBlockState(((TileEntity)(Object)this).getPos()).getBlock();
            ItemStack itemstack = this.furnaceItemStacks.get(0);
            ItemStack itemstack1 = Loader.isModLoaded("xlfoodmod") && (block==Blocks.FURNACE || block==Blocks.LIT_FURNACE) ?
                    XLFoodUtil.cheese() : FurnaceRecipes.instance().getSmeltingResult(itemstack);
            ItemStack itemstack2 = this.furnaceItemStacks.get(2);
            if (itemstack2.isEmpty()) this.furnaceItemStacks.set(2, itemstack1.copy());
            else if (itemstack2.getItem() == itemstack1.getItem()) itemstack2.grow(itemstack1.getCount());
            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 &&
                    !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem() == Items.BUCKET)
                this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            itemstack.shrink(1);
        }
    }
}
