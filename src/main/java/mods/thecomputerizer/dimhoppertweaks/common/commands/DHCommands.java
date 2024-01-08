package mods.thecomputerizer.dimhoppertweaks.common.commands;

import bedrockcraft.util.TriFunction;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DHCommands extends DHTCommand {

    private static final List<DHSubCmd> VALID_SUBCOMMANDS = makeSubCommands("discord","help","project","tip");

    private static List<DHSubCmd> makeSubCommands(String ... names) {
        List<DHSubCmd> subCommands = new ArrayList<>();
        for(String name : names) {
            DHSubCmd cmd = new DHSubCmd(name);
            subCommands.add(cmd);
        }
        return Collections.unmodifiableList(subCommands);
    }

    protected DHCommands() {
        super("dimensionhopper");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==0) sendBuiltMessage(sender,true,getUsage(sender),VALID_SUBCOMMANDS);
        else {
            List<DHSubCmd> matchingSubs = getMatchingSubCmds(args[0]);
            if(matchingSubs.size()!=1) sendBuiltMessage(sender,true,getUsage(sender),VALID_SUBCOMMANDS);
            matchingSubs.get(0).execute(server,sender,Arrays.copyOfRange(args,1,args.length));
        }
    }

    private List<DHSubCmd> getMatchingSubCmds(String toMatch) {
        List<DHSubCmd> matches = new ArrayList<>();
        for(DHSubCmd subCmd : VALID_SUBCOMMANDS) {
            if(subCmd.name.equals(toMatch)) {
                matches.clear();
                return Collections.singletonList(subCmd);
            }
            if(subCmd.name.startsWith(toMatch))
                matches.add(subCmd);
        }
        return matches;
    }

    public List<String> getTabCompletions(
            MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length==0) return Collections.emptyList();
        List<DHSubCmd> matchingSubs = getMatchingSubCmds(args[0]);
        if(args.length==1) return matchingSubs.stream().map(Object::toString).collect(Collectors.toList());
        if(matchingSubs.size()!=1) return Collections.emptyList();
        return matchingSubs.get(0).getTabCompletions(server,sender,Arrays.copyOfRange(args,1,args.length));
    }

    private static class DHSubCmd {

        private final String name;
        private final TriFunction<MinecraftServer,ICommandSender,String[],CommandException> execute;
        private final TriFunction<MinecraftServer,ICommandSender,String[],List<String>> tabCompletions;

        private DHSubCmd(String name) {
            this.name = name;
            this.execute = (server,sender,args) -> {
                return null;
            };
            this.tabCompletions = (server,sender,args) -> {
                return Collections.emptyList();
            };
        }

        private void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            CommandException ex = this.execute.apply(server,sender,args);
            if(Objects.nonNull(ex)) throw ex;
        }

        private List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args) {
            return this.tabCompletions.apply(server,sender,args);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
