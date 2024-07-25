package mods.thecomputerizer.dimhoppertweaks.mixin.mods.bedrockcraft;

import bedrockcraft.tool.BedrockSword;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BedrockSword.class, remap = false)
public abstract class MixinBedrockSword extends ItemSword {
    
    public MixinBedrockSword(ToolMaterial material) {
        super(material);
    }
    
    @Override
    public float getAttackDamage() {
        return super.getAttackDamage()*4f;
    }
}