package mods.thecomputerizer.dimhoppertweaks.registry.traits.farming;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static net.minecraft.block.material.Material.CACTUS;
import static net.minecraft.block.material.Material.GOURD;
import static net.minecraft.block.material.Material.PLANTS;
import static net.minecraft.init.Blocks.LEAVES;
import static net.minecraft.init.Blocks.LEAVES2;
import static net.minecraft.init.Items.DYE;

public class GreenThumb extends ExtendedEventsTrait {

    public GreenThumb() {
        super("green_thumb",3,1,FARMING,10,"farming|24","magic|16");
        setIcon("reskillable","unlockables","green_thumb");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        BlockPos pos = player.getPosition();
        if(player.ticksExisted%20==0) {
            int range = 6;
            int x = pos.getX()+player.world.rand.nextInt(range*2+1)-range;
            int z = pos.getZ()+player.world.rand.nextInt(range*2+1)-range;
            for(int i=4; i>-2; i--) {
                int y = pos.getY() + i;
                BlockPos offPos = new BlockPos(x,y,z);
                if(!player.world.isAirBlock(offPos) && this.isPlant(player.world,offPos)) {
                    ItemStack item = new ItemStack(DYE,1,15);
                    ItemDye.applyBonemeal(item,player.world,offPos);
                    player.world.playEvent(2005,offPos,6+player.world.rand.nextInt(4));
                    break;
                }
            }
        }
    }

    private boolean isPlant(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block==LEAVES || block==LEAVES2 || block instanceof BlockBush &&
                !(block instanceof BlockCrops) && !(block instanceof BlockSapling)) return false;
        else {
            Material mat = state.getMaterial();
            return (mat==PLANTS || mat==CACTUS || mat==Material.LEAVES || mat==GOURD) &&
                   block instanceof IGrowable &&
                   ((IGrowable)block).canGrow(world,pos,world.getBlockState(pos),world.isRemote);
        }
    }
}
