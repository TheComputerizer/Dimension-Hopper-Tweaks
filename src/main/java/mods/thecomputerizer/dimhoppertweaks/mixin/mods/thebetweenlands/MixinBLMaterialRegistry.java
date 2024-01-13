package mods.thecomputerizer.dimhoppertweaks.mixin.mods.thebetweenlands;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.item.BLMaterialRegistry;

@Mixin(value = BLMaterialRegistry.class, remap = false)
public class MixinBLMaterialRegistry {

    @Final @Shadow public static final Item.ToolMaterial TOOL_LEGEND = EnumHelper.addToolMaterial("legend",6,100000,16f,6f,0);
}
