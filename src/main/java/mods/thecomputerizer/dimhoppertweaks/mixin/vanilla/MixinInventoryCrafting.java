package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.ContainerAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.InventoryCraftingAccess;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Mixin(InventoryCrafting.class)
public class MixinInventoryCrafting implements InventoryCraftingAccess {
    @Shadow @Final private Container eventHandler;

    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return Objects.isNull(this.eventHandler) ? new ArrayList<>() :
                ((ContainerAccess)this.eventHandler).dimhoppertweaks$getStages();
    }
}
