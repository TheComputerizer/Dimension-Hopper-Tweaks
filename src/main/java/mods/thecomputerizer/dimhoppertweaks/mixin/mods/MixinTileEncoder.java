package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.RecipeInfoTieredAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thelm.packagedauto.inventory.InventoryEncoderPattern;
import thelm.packagedauto.tile.TileEncoder;
import thelm.packagedexcrafting.recipe.IRecipeInfoTiered;

@Mixin(value = TileEncoder.class, remap = false)
public class MixinTileEncoder {

    @Shadow @Final public InventoryEncoderPattern[] patternInventories;

    @Unique private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Unique private void dimhoppertweaks$updateStagedRecipeInfo() {
        for(InventoryEncoderPattern pattern : this.patternInventories)
            if(pattern.recipeInfo instanceof IRecipeInfoTiered)
                ((RecipeInfoTieredAccess)pattern.recipeInfo).dimhoppertweaks$setStages(
                        ((TileEntityAccess)dimhoppertweaks$cast()).dimhoppertweaks$getStages());
    }

    @Inject(at = @At("RETURN"), method = "readSyncNBT")
    private void dimhoppertweaks$readSyncNBT(NBTTagCompound tag, CallbackInfo ci) {
        this.dimhoppertweaks$updateStagedRecipeInfo();
    }

    @Inject(at = @At("RETURN"), method = "loadRecipeList")
    private void dimhoppertweaks$readSyncNBT(CallbackInfo ci) {
        this.dimhoppertweaks$updateStagedRecipeInfo();
    }
}
