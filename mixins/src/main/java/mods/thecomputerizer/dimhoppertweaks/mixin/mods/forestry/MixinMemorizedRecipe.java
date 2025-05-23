package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;
import forestry.core.network.IStreamable;
import forestry.core.network.PacketBufferForestry;
import forestry.worktable.inventory.InventoryCraftingForestry;
import forestry.worktable.recipes.MemorizedRecipe;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IGameStageExtension;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = MemorizedRecipe.class, remap = false)
public abstract class MixinMemorizedRecipe implements INbtWritable, INbtReadable, IStreamable, IGameStageExtension {

    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Shadow private InventoryCraftingForestry craftMatrix;

    @Inject(at=@At("TAIL"),method="<init>(Lforestry/core/network/PacketBufferForestry;)V")
    private void dimhoppertweaks$inheritStages(PacketBufferForestry data, CallbackInfo ci) {
        DelayedModAccess.inheritStages(this,this.craftMatrix);
    }

    @Inject(at=@At("TAIL"),method="<init>(Lnet/minecraft/nbt/NBTTagCompound;)V")
    private void dimhoppertweaks$inheritStages(NBTTagCompound tag, CallbackInfo ci) {
        DelayedModAccess.inheritStages(this,this.craftMatrix);
    }

    @Inject(at=@At("TAIL"),method="<init>(Lforestry/worktable/inventory/InventoryCraftingForestry;Ljava/util/List;)V")
    private void dimhoppertweaks$inheritStages(InventoryCraftingForestry matrix, List<IRecipe> recipes, CallbackInfo ci) {
        DelayedModAccess.inheritStages(matrix,this);
        DelayedModAccess.inheritStages(matrix,this.craftMatrix);
    }

    @Inject(at=@At("TAIL"),method="readData")
    private void dimhoppertweaks$readStageData(PacketBufferForestry data, CallbackInfo ci) {
        dimhoppertweaks$setStages(DelayedModAccess.readStageData(data),true);
    }

    @Inject(at=@At("TAIL"),method="readFromNBT")
    private void dimhoppertweaks$readStageNBT(NBTTagCompound tag, CallbackInfo ci) {
        dimhoppertweaks$setStages(DelayedModAccess.retrieveStageData(tag,"MemorizedRecipe"),true);
    }

    @Inject(at=@At("TAIL"),method="writeData")
    private void dimhoppertweaks$writeStageData(PacketBufferForestry data, CallbackInfo ci) {
        DelayedModAccess.writeStageData(data,this.dimhoppertweaks$stages);
    }

    @Inject(at=@At("RETURN"),method="writeToNBT")
    private void dimhoppertweaks$writeStageNBT(NBTTagCompound tag, CallbackInfoReturnable<NBTTagCompound> cir) {
        DelayedModAccess.appendStageData(tag,"MemorizedRecipe",this.dimhoppertweaks$stages);
    }

    @Override public void dimhoppertweaks$setStages(boolean clear, String ... stages) {
        if(clear) this.dimhoppertweaks$stages.clear();
        for(String stage : stages) dimhoppertweaks$addStage(stage);
    }

    @Override public void dimhoppertweaks$setStages(Collection<String> stages, boolean clear) {
        if(clear) this.dimhoppertweaks$stages.clear();
        for(String stage : stages) dimhoppertweaks$addStage(stage);
    }

    @Override public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }

    @Override public void dimhoppertweaks$addStage(String stage) {
        if(Objects.nonNull(stage) && !stage.isEmpty()) this.dimhoppertweaks$stages.add(stage);
    }

    @Override public boolean dimhoppertweaks$hasStage(String stage) {
        return this.dimhoppertweaks$stages.contains(stage);
    }
}