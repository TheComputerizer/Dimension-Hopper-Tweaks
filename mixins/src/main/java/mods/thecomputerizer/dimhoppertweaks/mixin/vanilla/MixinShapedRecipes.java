package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;

@Mixin(ShapedRecipes.class)
public class MixinShapedRecipes {
    
    @Inject(at = @At("RETURN"), method = "matches", cancellable = true)
    private void dimhoppertweaks$matches(InventoryCrafting inv, World world, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValueZ()) {
            boolean server = world instanceof WorldServer;
            LOGGER.info("(SHAPED MATCHES) Server world = {} | Inventory stages {} | Container stages {}",server,
                        DelayedModAccess.getCraftingStages(inv),DelayedModAccess.getCraftingStages(inv.eventHandler));
            if(!DelayedModAccess.canCraft(inv,true)) {
                LOGGER.info("(SHAPED MATCHES) Cannot craft with staged input(s)!");
                cir.setReturnValue(false);
            }
        }
    }
    
    @Inject(at = @At("RETURN"), method = "getCraftingResult", cancellable = true)
    private void dimhoppertweaks$getCraftingResult(InventoryCrafting inv, CallbackInfoReturnable<ItemStack> cir) {
        LOGGER.info("(SHAPED CRAFTING RESULT) Inventory stages {} | Container stages {}",
                    DelayedModAccess.getCraftingStages(inv),DelayedModAccess.getCraftingStages(inv.eventHandler));
        cir.setReturnValue(DelayedModAccess.getCraftingResult(inv,cir.getReturnValue(),true));
    }
}
