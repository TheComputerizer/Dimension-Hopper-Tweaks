package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.init.Blocks.SPONGE;
import static net.minecraft.init.Items.BUCKET;
import static net.minecraft.init.Items.WATER_BUCKET;

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
            if(stack.getItem()==Item.getItemFromBlock(SPONGE) && stack.getMetadata()==1 &&
                    !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem()==BUCKET)
                this.furnaceItemStacks.set(1,new ItemStack(WATER_BUCKET));
            stack.shrink(1);
        }
    }
}