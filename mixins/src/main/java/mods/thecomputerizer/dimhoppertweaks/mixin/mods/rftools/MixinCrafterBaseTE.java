package mods.thecomputerizer.dimhoppertweaks.mixin.mods.rftools;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mcjty.lib.tileentity.GenericEnergyReceiverTileEntity;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(value = CrafterBaseTE.class, remap = false)
public abstract class MixinCrafterBaseTE extends GenericEnergyReceiverTileEntity {
    
    @Shadow private InventoryCrafting workInventory;
    
    public MixinCrafterBaseTE(long maxEnergy, long maxReceive) {
        super(maxEnergy,maxReceive);
    }
    
    @ModifyExpressionValue(method="checkStateServer",at=@At(value="FIELD",
            target="Lmcjty/rftools/blocks/crafter/CrafterBaseTE;noRecipesWork:Z",opcode=GETFIELD))
    private boolean dimhoppertweaks$inheritStages(boolean original) {
        if(!original) DelayedModAccess.inheritInventoryStages(this,this.workInventory);
        return original;
    }
}