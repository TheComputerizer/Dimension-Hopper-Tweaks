package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aether;

import com.gildedgames.aether.common.entities.tiles.TileEntityHolystoneFurnace;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityHolystoneFurnace.class,remap = false)
public abstract class MixinTileEntityHolystoneFurnace {


    @Shadow protected abstract boolean canSmelt();

    @Shadow private NonNullList<ItemStack> furnaceItemStacks;

    /**
     * @author The_Computerizer
     * @reason Make the holystone furnace always output cheese for those who like cheesing stuff
     */
    @Overwrite
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack smeltStack = this.furnaceItemStacks.get(0);
            ItemStack result = DelayedModAccess.cheese();
            ItemStack existingResult = this.furnaceItemStacks.get(2);
            if(existingResult.isEmpty()) this.furnaceItemStacks.set(2,result.copy());
            else if(existingResult.getItem()==result.getItem()) existingResult.grow(result.getCount());
            if(smeltStack.getItem()==Item.getItemFromBlock(Blocks.SPONGE) && smeltStack.getMetadata()==1 &&
                    !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem()==Items.BUCKET)
                this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            smeltStack.shrink(1);
        }

    }
}