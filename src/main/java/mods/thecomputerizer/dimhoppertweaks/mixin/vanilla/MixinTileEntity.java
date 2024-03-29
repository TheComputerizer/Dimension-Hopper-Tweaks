package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
public abstract class MixinTileEntity implements ITileEntity {

    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Unique
    private TileEntity dimhoppertweaks$cast() {
        return (TileEntity)(Object)this;
    }

    @Override
    public void dimhoppertweaks$setStages(String ... stages) {
        for(String stage : stages) this.dimhoppertweaks$addStage(stage);
    }

    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages) {
        for(String stage : stages) this.dimhoppertweaks$addStage(stage);
    }

    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }

    @Override
    public void dimhoppertweaks$addStage(String stage) {
        if(Objects.nonNull(stage) && !stage.isEmpty()) this.dimhoppertweaks$stages.add(stage);
        DelayedModAccess.checkForAutoCrafter(dimhoppertweaks$cast(),this.dimhoppertweaks$stages);
    }

    @Override
    public boolean dimhoppertweaks$hasStage(String stage) {
        return this.dimhoppertweaks$stages.contains(stage);
    }

    @Inject(at = @At("TAIL"), method = "readFromNBT")
    private void dimhoppertweaks$readFromNBT(NBTTagCompound tag, CallbackInfo ci) {
        if(tag.hasKey("GameStageTileData"))
            for(NBTBase stageTag : tag.getTagList("GameStageTileData",8))
                this.dimhoppertweaks$addStage(((NBTTagString) stageTag).getString());
    }

    @Inject(at = @At("TAIL"), method = "writeToNBT")
    private void dimhoppertweaks$writeToNBT(NBTTagCompound tag, CallbackInfoReturnable<NBTTagCompound> cir) {
        if(!this.dimhoppertweaks$stages.isEmpty()) {
            NBTTagList stagesTag = new NBTTagList();
            for(String stage : this.dimhoppertweaks$stages)
                stagesTag.appendTag(new NBTTagString(stage));
            tag.setTag("GameStageTileData",stagesTag);
        }
    }
}