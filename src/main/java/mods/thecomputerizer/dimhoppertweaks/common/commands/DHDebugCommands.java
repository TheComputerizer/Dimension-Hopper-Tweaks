package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DHDebugCommands extends DHTCommand {

    protected DHDebugCommands() {
        super("dhd","Dimension Hopper Debug");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int option = 0;
        try {
            option = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            sendMessage(sender,true,"number",args[0]);
        }
        if(option==1) {
            executeBlockData(server,sender,getOrNull(1,args),getOrNull(2,args));
            return;
        }
        sendMessage(sender,true,"options."+(sender instanceof Entity ? "entity" : "server"));
    }

    private void executeBlockData(MinecraftServer server, ICommandSender sender, @Nullable String arg1,
                                  @Nullable String arg2) throws CommandException {
        int offset = -1;
        if(Objects.nonNull(arg1)) {
            try {
                offset = Integer.parseInt(arg1);
            } catch (NumberFormatException ex) {
                sendMessage(sender,true, "number",arg1);
            }
        }
        Entity entity = sender instanceof Entity ? (Entity)sender : null;
        if(Objects.nonNull(arg2)) {
            try {
                entity = getEntity(server,sender,arg2);
            } catch (EntityNotFoundException ex) {
                sendMessage(sender,true, "entity",arg2);
            }
        }
        if(Objects.isNull(entity)) {
            sendMessage(sender,true,"blockdata");
            return;
        }
        int x = entity.getPosition().getX();
        int y = entity.getPosition().getY()+offset;
        int z = entity.getPosition().getZ();
        buildAndExecuteCommand(server,sender,"blockdata",x,y,z,"{}");
        sendMessage(sender,false,"blockdata",x,y,z);
    }
}
