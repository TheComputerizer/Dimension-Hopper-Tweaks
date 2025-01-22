package mods.thecomputerizer.dimhoppertweaks.mixin.mods.multipart;

import codechicken.multipart.minecraft.RedstoneTorchPart;
import codechicken.multipart.minecraft.TorchPart;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(value = RedstoneTorchPart.class, remap = false)
public abstract class MixinRedstoneTorchPart extends TorchPart {
    
    @Shadow public abstract boolean isBeingPowered();
    @Shadow public abstract boolean active();
    
    @Override
    public void onNeighborChanged() {
        World world = this.world();
        if(Objects.nonNull(world) && !world.isRemote && !dropIfCantStay() && Objects.nonNull(this.state) &&
           isBeingPowered()==active()) this.scheduleTick(2);
    }
}