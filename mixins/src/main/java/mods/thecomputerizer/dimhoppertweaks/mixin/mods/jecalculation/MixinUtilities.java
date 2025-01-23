package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jecalculation;

import me.towdium.jecalculation.utils.Utilities;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = Utilities.class, remap = false)
public abstract class MixinUtilities {

    /**
     * @author The_Computerizer
     * @reason Fix crash with items that have custom mod ids
     */
    @Overwrite
    public static @Nullable String getModName(Item item) {
        ResourceLocation res = item.getRegistryName();
        if(Objects.isNull(res)) return null;
        String modid = res.getNamespace();
        if(modid.equals("minecraft")) return "Minecraft";
        if(modid.equals("dimensionhopper")) return "Dimension Hopper";
        ModContainer mod = Loader.instance().getIndexedModList().get(modid);
        return Objects.nonNull(mod) ? mod.getName() : null;
    }
}