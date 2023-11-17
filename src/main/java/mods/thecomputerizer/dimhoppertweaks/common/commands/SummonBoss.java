package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.events.TickEvents;
import mods.thecomputerizer.dimhoppertweaks.network.PacketRenderBossAttack;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SummonBoss extends DHTCommand {

    public SummonBoss() {
        super("summoncommandtest","Summon Test");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP)sender;
                new PacketRenderBossAttack(new ArrayList<>(),-1,4,0,0).addPlayers(player).send();
                TickEvents.startSummonBoss(server.getWorld((player.dimension)),player.getPosition().add(2,0,0));
                sendMessage(sender,false,null);
            } catch (Exception ex) {
                sendMessage(sender,true,null);
            }
        }
    }
}
