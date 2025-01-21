package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sblectric.lightningcraft.items.blocks.ItemBlockAirTerminal;
import sblectric.lightningcraft.items.blocks.ItemBlockMeta;

import javax.annotation.Nonnull;

import static net.minecraft.item.EnumRarity.EPIC;
import static net.minecraft.item.EnumRarity.RARE;
import static net.minecraft.item.EnumRarity.UNCOMMON;

@Mixin(value = ItemBlockAirTerminal.class, remap = false)
public abstract class MixinItemBlockAirTerminal extends ItemBlockMeta {
    
    public MixinItemBlockAirTerminal(Block block) {
        super(block);
    }
    
    /**
     * @author The_Computerizer
     * @reason Add support for more terminal types
     */
    @SuppressWarnings("deprecation") @Overwrite
    public @Nonnull EnumRarity getRarity(ItemStack stack) {
        switch(stack.getItemDamage()) {
            case 7: return UNCOMMON;
            case 8: return RARE;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13: return EPIC;
            default: return super.getRarity(stack);
        }
    }
}
