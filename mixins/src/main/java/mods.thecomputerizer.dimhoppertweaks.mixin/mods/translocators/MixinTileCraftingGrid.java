package mods.thecomputerizer.dimhoppertweaks.mixin.mods.translocators;

import codechicken.translocators.tile.TileCraftingGrid;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

import static net.minecraft.util.EnumHand.MAIN_HAND;

@Mixin(value = TileCraftingGrid.class, remap = false)
public abstract class MixinTileCraftingGrid extends TileEntity {

    @Shadow public ItemStack[] items;
    @Shadow protected abstract InventoryCrafting getCraftMatrix();
    @Shadow protected abstract void doCraft(ItemStack result, InventoryCrafting craftMatrix, EntityPlayer player);
    @Shadow protected abstract void rotateItems(InventoryCrafting inv);
    @Shadow public abstract void dropItems();

    /**
     * @author The_Computerizer
     * @reason Add Recipe Stages support
     */
    @Overwrite
    public void craft(EntityPlayer player) {
        InventoryCrafting craftMatrix = this.getCraftMatrix();
        ((IInventoryCrafting)craftMatrix).dimhoppertweaks$setStages(DelayedModAccess.getGameStages(player));
        for(int i=0;i<4;i++) {
            IRecipe recipe = CraftingManager.findMatchingRecipe(craftMatrix,this.world);
            if(Objects.nonNull(recipe)) {
                this.doCraft(recipe.getCraftingResult(craftMatrix),craftMatrix,player);
                break;
            }
            this.rotateItems(craftMatrix);
        }
        player.swingArm(MAIN_HAND);
        this.dropItems();
        this.world.setBlockToAir(this.getPos());
    }
}