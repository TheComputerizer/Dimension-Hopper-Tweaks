package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import gcewing.sg.BaseTileInventory;
import gcewing.sg.tileentity.SGBaseTE;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.SGBaseTEAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SGBaseTE.class, remap = false)
public abstract class MixinSGBaseTE extends BaseTileInventory implements SGBaseTEAccess {

    @Shadow public boolean isMerged;

    @Shadow public abstract void updateIrisEntity();

    @Override
    public void dimhoppertweaks$mergeWithoutAddressing() {
        if(!this.isMerged) {
            this.isMerged = true;
            this.markBlockChanged();
            this.updateIrisEntity();
        }
    }
}
