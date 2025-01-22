package mods.thecomputerizer.dimhoppertweaks.mixin.mods.sgcraft;

import gcewing.sg.BaseTileInventory;
import gcewing.sg.tileentity.SGBaseTE;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ISGBaseTE;
import org.dave.compactmachines3.world.ProxyWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = SGBaseTE.class, remap = false)
public abstract class MixinSGBaseTE extends BaseTileInventory implements ISGBaseTE {

    @Shadow public boolean isMerged;
    @Shadow public abstract void updateIrisEntity();

    @Shadow protected abstract String tryToGetHomeAddress();

    @Override
    public void dimhoppertweaks$mergeWithoutAddressing() {
        if(!this.isMerged) {
            this.isMerged = true;
            this.markBlockChanged();
            this.updateIrisEntity();
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lgcewing/sg/tileentity/SGBaseTE;"+
            "tryToGetHomeAddress()Ljava/lang/String;"), method = "setMerged")
    private String dimhoppertweaks$checkIgnoreHomeAddress(SGBaseTE tile) {
        return Objects.isNull(this.world) || this.world instanceof ProxyWorld ? null : tryToGetHomeAddress();
    }
}