package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tconstruct;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.tools.common.inventory.ContainerCraftingStation;
import slimeknights.tconstruct.tools.common.inventory.ContainerTinkerStation;
import slimeknights.tconstruct.tools.common.tileentity.TileCraftingStation;

import java.util.Objects;

@Mixin(value = ContainerCraftingStation.class, remap = false)
public abstract class MixinContainerCraftingStation extends ContainerTinkerStation<TileCraftingStation> {
    
    @Shadow @Final private EntityPlayer player;
    
    public MixinContainerCraftingStation(TileCraftingStation tile) {
        super(tile);
    }
    
    @Inject(at=@At("TAIL"),method="<init>")
    private void dimhoppertweaks$afterInit(InventoryPlayer inventory, TileCraftingStation tile, CallbackInfo ci) {
        if(Objects.nonNull(this.player)) DelayedModAccess.inheritContainerStages(this.player,this);
    }
}
