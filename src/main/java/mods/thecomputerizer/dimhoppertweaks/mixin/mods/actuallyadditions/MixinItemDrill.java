package mods.thecomputerizer.dimhoppertweaks.mixin.mods.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.items.ItemDrill;
import de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(value = ItemDrill.class, remap = false)
@ParametersAreNonnullByDefault
public abstract class MixinItemDrill extends ItemEnergy {
    
    public MixinItemDrill(int maxPower, int transfer, String name) {
        super(maxPower,transfer,name);
    }
    
    @Override
    public int getHarvestLevel(ItemStack stack, String tool, @Nullable EntityPlayer player, @Nullable IBlockState state) {
        return SkillWrapper.hasMiningLevelUprade(player) ? 5 : 4;
    }
}
