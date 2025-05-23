package mods.thecomputerizer.dimhoppertweaks.mixin.mods.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler;
import blusunrize.immersiveengineering.common.gui.ContainerAssembler;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

import static org.objectweb.asm.Opcodes.PUTFIELD;

@Mixin(value = ContainerAssembler.class, remap = false)
public abstract class MixinContainerAssembler extends ContainerIEBase<TileEntityAssembler> {

    public MixinContainerAssembler(InventoryPlayer player, TileEntityAssembler tile) {
        super(player,tile);
    }

    @WrapOperation(at=@At(value="FIELD", target="Lblusunrize/immersiveengineering/common/gui/ContainerAssembler;tile:" +
            "Lnet/minecraft/tileentity/TileEntity;",opcode=PUTFIELD),method="<init>")
    private void dimhoppertweaks$inheritStages(ContainerAssembler assembler, TileEntity tile,
            Operation<Void> operation, @Local(argsOnly=true)InventoryPlayer player) {
        Collection<String> stages = DelayedModAccess.getPlayerStages(player.player);
        DelayedModAccess.setTileStages(tile,stages);
        operation.call(assembler,tile);
        DelayedModAccess.setContainerStages(this,stages);
    }
}