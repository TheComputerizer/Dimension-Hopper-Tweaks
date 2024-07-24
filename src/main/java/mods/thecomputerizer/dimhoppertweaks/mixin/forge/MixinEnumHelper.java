package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(value = EnumHelper.class, remap = false)
public abstract class MixinEnumHelper {
    
    @Shadow @Nullable private static <T extends Enum<?>> T addEnum(
            Class<T> enumType, String enumName, Object... paramValues) {
        return null;
    }
    
    /**
     * @author The_Computerizer
     * @reason I can't mixin into an interface so how else was I supposed to do this
     */
    @Overwrite
    public static @Nullable ToolMaterial addToolMaterial(String name, int harvestLevel, int maxUses, float efficiency,
            float damage, int enchantability) {
        if(name.equals("tool_material_bedrock")) {
            harvestLevel = 12;
            efficiency*=250f;
            damage*=250;
        }
        return addEnum(ToolMaterial.class,name,harvestLevel,maxUses,efficiency,damage,enchantability);
    }
}
