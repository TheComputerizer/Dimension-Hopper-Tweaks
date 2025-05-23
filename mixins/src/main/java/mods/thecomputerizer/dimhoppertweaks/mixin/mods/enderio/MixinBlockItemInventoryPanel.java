package mods.thecomputerizer.dimhoppertweaks.mixin.mods.enderio;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import crazypants.enderio.invpanel.invpanel.BlockItemInventoryPanel;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockItemInventoryPanel.class, remap = false)
public abstract class MixinBlockItemInventoryPanel {

    @WrapOperation(at=@At(value="INVOKE",target="Lnet/minecraft/world/World;getTileEntity("+
            "Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;",remap=true),
            method="placeBlockAt")
    private TileEntity dimhoppertweaks$inheritStages(World world, BlockPos pos,
            Operation<TileEntity> operation, @Local(argsOnly=true)EntityPlayer player) {
        TileEntity tile = operation.call(world,pos);
        DelayedModAccess.inheritPlayerStages(player,tile);
        return tile;
    }
}