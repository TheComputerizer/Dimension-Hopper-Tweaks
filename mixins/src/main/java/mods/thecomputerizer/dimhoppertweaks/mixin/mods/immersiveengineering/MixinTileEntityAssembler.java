package mods.thecomputerizer.dimhoppertweaks.mixin.mods.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockAssembler.instance;

@Mixin(value = TileEntityAssembler.class, remap = false)
public abstract class MixinTileEntityAssembler extends TileEntityMultiblockMetal<TileEntityAssembler,IMultiblockRecipe>
        implements IGuiTile, IConveyorAttachable {

    public MixinTileEntityAssembler() {
        super(instance,new int[]{3,3,3},32000,true);
    }

    @WrapOperation(at=@At(value="INVOKE", target=
            "Lblusunrize/immersiveengineering/common/util/Utils$InventoryCraftingFalse;createFilledCraftingInventory("+
                    "IILnet/minecraft/util/NonNullList;)Lnet/minecraft/inventory/InventoryCrafting;"),
            method="update")
    private InventoryCrafting dimhoppertweaks$inheritStages(int w, int h, NonNullList<ItemStack> stacks,
            Operation<InventoryCrafting> operation) {
        return DelayedModAccess.inheritInventoryStagesAndReturn(this,operation.call(w,h,stacks));
    }
}