package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import ru.liahim.mist.api.block.IMistStone;
import ru.liahim.mist.api.block.MistBlocks;
import ru.liahim.mist.api.item.MistItems;
import ru.liahim.mist.block.MistSoil;
import ru.liahim.mist.block.MistTreeTrunk;
import ru.liahim.mist.capability.handler.ISkillCapaHandler;
import ru.liahim.mist.handlers.ServerEventHandler;

@Mixin(value = ServerEventHandler.class, remap = false)
public class MixinServerEventHandler {

    /**
     * @author The_Computerizer
     * @reason Fix the niobium pickaxe not mixing stone fast
     */
    @Overwrite
    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getState().getBlock() instanceof IMistStone) {
            ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
            float speed = stack.getItem()==MistItems.NIOBIUM_PICKAXE ? event.getOriginalSpeed()*2f : event.getOriginalSpeed()/2f;
            int skill = ISkillCapaHandler.Skill.getLevel(event.getEntityPlayer(), ISkillCapaHandler.Skill.MASON);
            speed *= (float)(1 + Math.max(skill - 1, 0));
            event.setNewSpeed(speed);
        } else if (event.getState().getBlock() instanceof MistTreeTrunk) {
            int size = event.getState().getValue(MistTreeTrunk.SIZE);
            event.setNewSpeed((event.getOriginalSpeed() + 4.0F - (float)size) / 2.0F);
        } else if (event.getState().getBlock() instanceof MistSoil) {
            World world = event.getEntityPlayer().world;
            IBlockState state = world.getBlockState(event.getPos().up());
            if (state.getBlock() instanceof MistTreeTrunk && ((MistTreeTrunk)state.getBlock()).getDir(state) == EnumFacing.UP)
                event.setNewSpeed(Math.min(event.getOriginalSpeed(), event.getEntityPlayer().getDigSpeed(state, event.getPos().up()) / 8.0F));
        } else if (event.getState().getBlock() == MistBlocks.FLOATING_MAT && !event.getEntityPlayer().onGround)
            event.setNewSpeed(event.getOriginalSpeed() / 3.0F);
    }
}
