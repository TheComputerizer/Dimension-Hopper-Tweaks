package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = OreDictionary.class, remap = false)
public class MixinOreDictionary {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLLog;" +
            "bigWarning(Ljava/lang/String;[Ljava/lang/Object;)V"), method = "registerOreImpl")
    private static void dimhoppertweaks$noBigWarningForYou(String i, Object[] format) {
        FMLLog.log.warn("Invalid registration attempt for an Ore Dictionary {}!",format);
    }
}
