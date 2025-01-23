package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public abstract class DHTCommand extends CommandBase {

    private static final List<String> DIMENSION_TAB_NAMES = Arrays.stream(DimensionType.values())
            .filter(Objects::nonNull).map(DimensionType::getId).map(Objects::toString).collect(Collectors.toList());
    private static final List<String> BIOME_TAB_NAMES = BIOMES.getValuesCollection().stream()
            .map(Biome::getRegistryName).filter(Objects::nonNull).map(Objects::toString).collect(Collectors.toList());

    public static String buildRawCommand(Object ... args) {
        StringBuilder builder = new StringBuilder("/");
        for(Object arg : args) builder.append(arg).append(" ");
        return builder.toString().trim();
    }

    protected final String commandName;
    protected final Random rand;

    protected DHTCommand(String commandName) {
        this.commandName = commandName;
        this.rand = new Random();
    }

    @Override
    public String getName() {
        return this.commandName;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands."+MODID+"."+this.commandName+"."+"usage";
    }

    protected @Nullable String getOrNull(int index, String[] args) {
        return args.length>=index+1 ? args[index] : null;
    }

    protected List<String> getDimensionsTabCompletion() {
        return Collections.unmodifiableList(DIMENSION_TAB_NAMES);
    }

    protected List<String> getBiomesTabCompletion() {
        return Collections.unmodifiableList(BIOME_TAB_NAMES);
    }

    protected void sendBuiltMessage(ICommandSender sender, boolean isException, String lang, Object ... parameters) throws CommandException {
        if(isException) throw new CommandException(lang,parameters);
        else notifyCommandListener(sender,this,lang,parameters);
    }

    protected void sendMessage(ICommandSender sender, boolean isException, @Nullable String extraLang,
                               Object ... parameters) throws CommandException {
        String lang = buildLangKey(isException,extraLang);
        sendBuiltMessage(sender,isException,lang,parameters);
    }

    private String buildLangKey(boolean isError, @Nullable String extraLang) {
        String lang = "commands."+MODID+"."+this.commandName+"."+(isError ? "error" : "success");
        return Objects.nonNull(extraLang) && !extraLang.isEmpty() ? lang+"."+extraLang : lang;
    }

    protected void buildAndExecuteCommand(MinecraftServer server, ICommandSender sender, Object ... args) {
        server.getCommandManager().executeCommand(sender,buildRawCommand(args));
    }
}