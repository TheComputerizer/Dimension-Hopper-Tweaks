package mods.thecomputerizer.dimhoppertweaks.mixin.mods.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IMirrorAble;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IProcessTile;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEInternalFluxHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.llamalad7.mixinextras.sugar.Local;
import de.ellpeck.naturesaura.api.multiblock.IMultiblock;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityMultiblockMetal.class, remap = false)
public abstract class MixinTileEntityMultiblockMetal<T extends TileEntityMultiblockMetal<T,R>, R extends IMultiblockRecipe>
        extends TileEntityMultiblockPart<T> implements IIEInventory, IIEInternalFluxHandler, IHammerInteraction,
        IMirrorAble, IProcessTile, IComparatorOverride {
    
    @SuppressWarnings("unused")
    public MixinTileEntityMultiblockMetal(IMultiblock instance, int[] structureDimensions, int energyCapacity,
            boolean redstoneControl) {
        super(structureDimensions);
    }
    
    /**
     * Make sure the master tile inherits stages from the player assembling the multi
     */
    @Redirect(at=@At(value="INVOKE",
            target="Lblusunrize/immersiveengineering/common/blocks/metal/TileEntityMultiblockMetal;master()"+
                   "Lblusunrize/immersiveengineering/common/blocks/TileEntityMultiblockPart;"),method="hammerUseSide")
    private TileEntityMultiblockPart<T> dimhoppertweaks$setMasterStages(
            TileEntityMultiblockMetal<T,R> instance, @Local(argsOnly=true)EntityPlayer player) {
        TileEntityMultiblockMetal<T,R> master = instance.master();
        DelayedModAccess.inheritPlayerStages(player,master);
        return master;
    }
}