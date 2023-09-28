package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mekanism.common.Mekanism;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.tile.TileEntityDigitalMiner;
import mekanism.common.tile.prefab.TileEntityElectricBlock;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(value = TileEntityDigitalMiner.class, remap = false)
public abstract class MixinTileEntityDigitalMiner extends TileEntityElectricBlock {

    public MixinTileEntityDigitalMiner(String name, double baseMaxEnergy) {
        super(name,baseMaxEnergy);
    }

    /**
     * @author The_Computerizer
     * @reason Add Ore stages compat
     */
    @Overwrite
    public void openInventory(@Nonnull EntityPlayer player) {
        super.openInventory(player);
        if (!this.world.isRemote) {
            ((TileEntityAccess)this).dimhoppertweaks$setStages(DelayedModAccess.getGameStages(player));
            Mekanism.packetHandler.sendTo(new PacketTileEntity.TileEntityMessage(this),(EntityPlayerMP)player);
        }
    }
}
