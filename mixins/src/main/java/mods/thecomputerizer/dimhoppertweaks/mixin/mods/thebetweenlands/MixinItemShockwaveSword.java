package mods.thecomputerizer.dimhoppertweaks.mixin.mods.thebetweenlands;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.item.tools.ItemBLSword;
import thebetweenlands.common.item.tools.ItemShockwaveSword;


@Mixin(value = ItemShockwaveSword.class, remap = false)
public abstract class MixinItemShockwaveSword extends ItemBLSword {

    public MixinItemShockwaveSword(ToolMaterial material) {
        super(material);
    }

    /**
     * @author The_Computerizer
     * @reason No more damaged shockwave swords
     */
    @Overwrite
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack,0);
    }
}