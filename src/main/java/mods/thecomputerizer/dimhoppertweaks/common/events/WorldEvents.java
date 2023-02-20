package mods.thecomputerizer.dimhoppertweaks.common.events;

import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.tools.TinkerToolCore;

@Mod.EventBusSubscriber(modid = DimHopperTweaks.MODID)
public class WorldEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void blockBreak(BlockEvent.BreakEvent event) {
        if(event.getPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
            Item heldItem = player.getHeldItemMainhand().getItem();
            if(heldItem instanceof ItemTool || heldItem instanceof TinkerToolCore) {
                IBlockState state = event.getState();
                int harvestLevel = state.getBlock().getHarvestLevel(state);
                if (harvestLevel > 0) {
                    int hardness = (int) state.getBlockHardness(event.getWorld(), event.getPos());
                    int hardnessPower = Math.min(hardness > 1 ? (int) (Math.log(hardness) / Math.log(2)) : 0, 10);
                    SkillWrapper.addSP(player, "mining", Math.max(1, (hardnessPower + harvestLevel) / 2), false);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void blockPlace(BlockEvent.PlaceEvent event) {
        if(event.getPlayer() instanceof EntityPlayerMP && event.getState().getBlock()!=Blocks.FARMLAND)
            SkillWrapper.addSP((EntityPlayerMP)event.getPlayer(),"building",1f,false);
    }
}
