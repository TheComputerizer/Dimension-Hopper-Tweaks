package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mekanism;

import mekanism.common.block.BlockBasic;
import mekanism.common.block.BlockBasic.NeighborListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(value = NeighborListener.class, remap = false)
public abstract class MixinNeighborListener {

    /**
     * @author The_Computerizer
     * @reason Custom Nether portal implementation
     */
    @Overwrite
    @SubscribeEvent
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent e) {
        if(dimhoppertweaks$isCompressedObsidian(e.getState().getBlock())) {
            World world = e.getWorld();
            Block newBlock = world.getBlockState(e.getPos()).getBlock();
            if(newBlock instanceof BlockFire)
                BlockBasic.BlockPortalOverride.instance.trySpawnPortal(world,e.getPos());
        }
    }

    @Unique
    private boolean dimhoppertweaks$isCompressedObsidian(Block block) {
        return Objects.nonNull(block.getRegistryName()) && block.getRegistryName().toString().equals("overloaded:compressed_obsidian");
    }
}