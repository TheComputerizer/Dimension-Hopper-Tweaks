package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sblectric.lightningcraft.init.LCItems;
import sblectric.lightningcraft.integration.jei.JEIPlugin;
import sblectric.lightningcraft.ref.Log;

@SuppressWarnings("deprecation")
@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin {

    /**
     * @author The_Computerizer
     * @reason Remove crusher and infusion categories
     */
    @Overwrite
    public void register(IModRegistry reg) {
        reg.addDescription(new ItemStack(LCItems.ingot,1,0),"lightningcraft.electricium_info");
        reg.addDescription(new ItemStack(LCItems.material,1,11),"lightningcraft.ichor_info");
        reg.addDescription(new ItemStack(LCItems.guide),"lightningcraft.guide_info");
        reg.addDescription(new ItemStack(LCItems.ingot,1,1),"lightningcraft.skyfather_info");
        reg.addDescription(new ItemStack(LCItems.ingot,1,2),"lightningcraft.mystic_info");
        Log.logger.info("JEI integration complete.");
    }
}