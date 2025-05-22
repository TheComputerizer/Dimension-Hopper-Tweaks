package mods.thecomputerizer.dimhoppertweaks.mixin.mods.rftools;

import mcjty.lib.api.Infusable;
import mcjty.lib.crafting.INBTPreservingIngredient;
import mcjty.rftools.blocks.GenericRFToolsBlock;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mcjty.rftools.blocks.crafter.CrafterBlock;
import mcjty.rftools.blocks.crafter.CrafterContainer;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.material.Material.IRON;

@Mixin(value = CrafterBlock.class, remap = false)
public abstract class MixinCrafterBlock extends GenericRFToolsBlock<CrafterBaseTE,CrafterContainer>
        implements Infusable, INBTPreservingIngredient {

    public MixinCrafterBlock(String blockName, Class<? extends CrafterBaseTE> tileEntityClass) {
        super(IRON,tileEntityClass,CrafterContainer::new,blockName,true);
    }

    @Inject(at=@At("RETURN"),method="createServerContainer")
    private void dimhoppertweaks$inheritStages(EntityPlayer player, TileEntity tile,
            CallbackInfoReturnable<Container> cir) {
        DelayedModAccess.inheritPlayerStages(player,tile);
        DelayedModAccess.inheritContainerStages(player,cir.getReturnValue());
    }
}