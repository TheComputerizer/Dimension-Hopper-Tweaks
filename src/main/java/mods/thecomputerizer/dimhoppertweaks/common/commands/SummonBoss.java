package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mods.thecomputerizer.dimhoppertweaks.common.events.ServerEvents;
import mods.thecomputerizer.dimhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketRenderBossAttack;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

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
                BlockPos pos = new BlockPos(sender.getPosition().getX(), sender.getPosition().getY() + 5, sender.getPosition().getZ());
                PacketHandler.NETWORK.sendTo(new PacketRenderBossAttack.PacketRenderBossAttackMessage(pos, -125, 4), (EntityPlayerMP) sender);
                ServerEvents.startSummonBoss(world, pos);
            } else notifyCommandListener(sender, this, "It did not work");
        }
    }
}
