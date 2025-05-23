package mods.thecomputerizer.dimhoppertweaks.mixin.mods.galacticraft;

import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(value = PersistantInventoryCrafting.class, remap = false)
public abstract class MixinPersistantInventoryCrafting extends InventoryCrafting {
    
    public MixinPersistantInventoryCrafting(Container container, int i1, int i2) {
        super(container,i1,i2);
    }
    
    /**
     * @author The_Computerizer
     * @reason Update stages from the player opening the inventory
     */
    @Overwrite(remap = true)
    public void openInventory(@Nonnull EntityPlayer player) {
        DelayedModAccess.inheritInventoryStages(player,this);
    }
}
