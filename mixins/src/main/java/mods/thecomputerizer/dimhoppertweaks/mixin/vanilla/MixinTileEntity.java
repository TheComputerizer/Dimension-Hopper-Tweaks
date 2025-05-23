package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IGameStageExtension;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mixin(TileEntity.class)
public abstract class MixinTileEntity implements IGameStageExtension {

    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Unique
    private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Override public void dimhoppertweaks$setStages(boolean clear, String ... stages) {
        if(clear) this.dimhoppertweaks$stages.clear();
        for(String stage : stages) this.dimhoppertweaks$addStage(stage);
    }

    @Override public void dimhoppertweaks$setStages(Collection<String> stages, boolean clear) {
        if(clear) this.dimhoppertweaks$stages.clear();
        for(String stage : stages) this.dimhoppertweaks$addStage(stage);
    }

    @Override public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }

    @Override public void dimhoppertweaks$addStage(String stage) {
        if(Objects.nonNull(stage) && !stage.isEmpty()) this.dimhoppertweaks$stages.add(stage);
        DelayedModAccess.checkForAutoCrafter(dimhoppertweaks$cast(),this.dimhoppertweaks$stages);
    }

    @Override public boolean dimhoppertweaks$hasStage(String stage) {
        return this.dimhoppertweaks$stages.contains(stage);
    }

    @Inject(at = @At("TAIL"), method = "readFromNBT")
    private void dimhoppertweaks$readFromNBT(NBTTagCompound tag, CallbackInfo ci) {
        for(String stage : DelayedModAccess.retrieveStageData(tag,"Tile")) dimhoppertweaks$addStage(stage);
    }

    @Inject(at = @At("TAIL"), method = "writeToNBT")
    private void dimhoppertweaks$writeToNBT(NBTTagCompound tag, CallbackInfoReturnable<NBTTagCompound> cir) {
        DelayedModAccess.appendStageData(tag,"Tile",this.dimhoppertweaks$stages);
    }
}