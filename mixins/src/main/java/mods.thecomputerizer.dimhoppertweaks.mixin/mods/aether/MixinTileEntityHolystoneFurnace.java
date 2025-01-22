package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aether;

import com.gildedgames.aether.common.entities.tiles.TileEntityHolystoneFurnace;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.init.Blocks.SPONGE;
import static net.minecraft.init.Items.BUCKET;
import static net.minecraft.init.Items.WATER_BUCKET;

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
        if(this.canSmelt()) {
            ItemStack smeltStack = this.furnaceItemStacks.get(0);
            ItemStack result = DelayedModAccess.cheese();
            ItemStack existingResult = this.furnaceItemStacks.get(2);
            if(existingResult.isEmpty()) this.furnaceItemStacks.set(2,result.copy());
            else if(existingResult.getItem()==result.getItem()) existingResult.grow(result.getCount());
            if(smeltStack.getItem()==Item.getItemFromBlock(SPONGE) && smeltStack.getMetadata()==1 &&
                    !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem()==BUCKET)
                this.furnaceItemStacks.set(1, new ItemStack(WATER_BUCKET));
            smeltStack.shrink(1);
        }
    }
}