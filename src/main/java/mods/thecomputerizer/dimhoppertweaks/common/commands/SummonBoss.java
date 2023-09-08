package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mods.thecomputerizer.dimhoppertweaks.common.events.ServerEvents;
import mods.thecomputerizer.dimhoppertweaks.network.PacketRenderBossAttack;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SummonBoss extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "summoncommandtest";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "Summon Test Command initiated";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if(sender instanceof EntityPlayer) {
            WorldServer world = server.getWorld(((EntityPlayer)sender).dimension);
            if (!world.isRemote && sender instanceof EntityPlayerMP) {
                BlockPos pos = new BlockPos(sender.getPosition().getX()+2, sender.getPosition().getY(), sender.getPosition().getZ());
                new PacketRenderBossAttack(new ArrayList<>(),-1,4,0,0).addPlayers((EntityPlayerMP)sender).send();
                ServerEvents.startSummonBoss(world, pos);
            } else notifyCommandListener(sender, this, "It did not work");
        }
    }
}
