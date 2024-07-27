package mods.thecomputerizer.dimhoppertweaks.mixin.mods.bedrockcraft;

import bedrockcraft.tool.BedrockSword;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BedrockSword.class, remap = false)
public abstract class MixinBedrockSword extends ItemSword {
    
    public MixinBedrockSword(ToolMaterial material) {
        super(material);
    }
    
    @Redirect(at =@At(value="INVOKE",
            target="Lbedrockcraft/tool/BedrockSword;setMaxDamage(I)Lnet/minecraft/item/Item;"), method = "<init>")
    private Item dimhoppertweaks$fixDamage(BedrockSword sword, int i) {
        return sword.setMaxDamage(1000);
    }
}