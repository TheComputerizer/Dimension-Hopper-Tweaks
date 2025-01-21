package mods.thecomputerizer.dimhoppertweaks.registry.tiles;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

@Getter
public class LightningEnhancerEntity extends TileEntity implements ITickable {
    
    protected boolean enabled;
    
    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        this.enabled = tag.getBoolean("isEnabled");
        super.readFromNBT(tag);
    }
    
    @Override
    public void update() {
    
    }
    
    @Override
    public @Nonnull NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        tag.setBoolean("isEnabled",this.enabled);
        return super.writeToNBT(tag);
    }
}
