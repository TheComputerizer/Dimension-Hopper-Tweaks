package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.ContainerAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(Container.class)
public class MixinContainer implements ContainerAccess {

    @Unique private Collection<String> dimhoppertweaks$stages = new ArrayList<>();

    @Override
    @Unique public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }

    @Inject(at = @At("HEAD"), method = "slotClick")
    private void dimhoppertweaks$slotClick(int id, int type, ClickType click, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        this.dimhoppertweaks$stages = DelayedModAccess.getGameStages(player);
    }
}
