package mods.thecomputerizer.dimensionhoppertweaks.common.commands;

import mods.thecomputerizer.dimensionhoppertweaks.common.CommonEvents;
import mods.thecomputerizer.dimensionhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimensionhoppertweaks.network.packets.PacketRenderBossAttack;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class SummonBoss extends CommandBase {
    @Override
    public String getName() {
        return "summoncommandtest";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Summon Test Command initiated";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayer) {
            WorldServer world = server.getWorld(((EntityPlayer)sender).dimension);
            if (!world.isRemote && sender instanceof EntityPlayerMP) {
                BlockPos pos = new BlockPos(sender.getPosition().getX(), sender.getPosition().getY() + 5, sender.getPosition().getZ());
                PacketHandler.NETWORK.sendTo(new PacketRenderBossAttack.PacketRenderBossAttackMessage(pos, -275, 4), (EntityPlayerMP) sender);
                CommonEvents.startSummonBoss(world, pos);
            } else notifyCommandListener(sender, this, "It did not work");
        }
    }
}
