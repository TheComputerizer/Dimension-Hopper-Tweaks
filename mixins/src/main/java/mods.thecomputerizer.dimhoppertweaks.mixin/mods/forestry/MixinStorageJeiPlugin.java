package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.core.utils.JeiUtil;
import forestry.modules.ModuleHelper;
import forestry.storage.ModuleBackpacks;
import forestry.storage.compat.StorageJeiPlugin;
import forestry.storage.items.ItemRegistryBackpacks;
import mezz.jei.api.IModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = StorageJeiPlugin.class, remap = false)
public abstract class MixinStorageJeiPlugin {

    /**
     * @author The_Computerizer
     * @reason Remove default adventuring bag description
     */
    @Overwrite
    public void register(IModRegistry registry) {
        if (ModuleHelper.isEnabled("backpacks")) {
            ItemRegistryBackpacks items = ModuleBackpacks.getItems();
            JeiUtil.addDescription(registry,"miner_bag",items.minerBackpack,items.minerBackpackT2);
            JeiUtil.addDescription(registry,"digger_bag",items.diggerBackpack,items.diggerBackpackT2);
            JeiUtil.addDescription(registry,"forester_bag",items.foresterBackpack,items.foresterBackpackT2);
            JeiUtil.addDescription(registry,"hunter_bag",items.hunterBackpack,items.hunterBackpackT2);
            JeiUtil.addDescription(registry,"builder_bag",items.builderBackpack,items.builderBackpackT2);
            if(ModuleHelper.isEnabled("apiculture") && Objects.nonNull(items.apiaristBackpack))
                JeiUtil.addDescription(registry,items.apiaristBackpack);
            if(ModuleHelper.isEnabled("lepidopterology") && Objects.nonNull(items.lepidopteristBackpack))
                JeiUtil.addDescription(registry,items.lepidopteristBackpack);
        }
    }
}