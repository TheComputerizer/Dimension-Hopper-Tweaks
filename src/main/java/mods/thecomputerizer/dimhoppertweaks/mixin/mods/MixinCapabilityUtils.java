package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import com.tiviacz.travelersbackpack.capability.ITravelersBackpack;
import com.tiviacz.travelersbackpack.items.ItemTravelersBackpack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = CapabilityUtils.class, remap = false)
public abstract class MixinCapabilityUtils {

    @Shadow public static ITravelersBackpack getCapability(EntityPlayer player) {
        return null;
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @Overwrite
    public static boolean isWearingBackpack(EntityPlayer player) {
        ITravelersBackpack cap = getCapability(player);
        if(Objects.isNull(cap)) return false;
        ItemStack backpack = cap.getWearable();
        return cap.hasWearable() && backpack.getItem() instanceof ItemTravelersBackpack;
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @Overwrite
    public static ItemStack getWearingBackpack(EntityPlayer player) {
        ITravelersBackpack cap = getCapability(player);
        if(Objects.isNull(cap)) return ItemStack.EMPTY;
        ItemStack backpack = cap.getWearable();
        return isWearingBackpack(player) ? backpack : ItemStack.EMPTY;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/tiviacz/travelersbackpack/capability/ITravelersBackpack;synchronise()V"), method = "synchronise")
    private static void dimhoppertweaks$synchronise(ITravelersBackpack backpack) {
        if(Objects.nonNull(backpack)) backpack.synchronise();
    }
}
