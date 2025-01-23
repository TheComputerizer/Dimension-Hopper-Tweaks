package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.network.PacketQueryGenericClient;
import mods.thecomputerizer.dimhoppertweaks.network.PacketTileEntityClassQuery;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static net.darkhax.gamestages.data.GameStageSaveHandler.EMPTY_STAGE_DATA;
import static net.minecraft.init.Items.SPAWN_EGG;
import static net.minecraft.init.Blocks.AIR;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.BLOCKS;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class DHDebugCommands extends DHTCommand {

    public DHDebugCommands() {
        super("dhd");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int option = 0;
        try {
            option = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            sendMessage(sender,true,"number",args[0]);
        }
        switch(option) {
            case 1: {
                executeBlockData(server,sender,getOrNull(1,args),getOrNull(2,args));
                return;
            }
            case 2: {
                executeTileClass(server,sender,getOrNull(1,args));
                return;
            }
            case 3: {
                executeGamestage(server,sender,getOrNull(1,args));
                return;
            }
            case 4: {
                executeGive(server,sender,getOrNull(1,args),getOrNull(2,args));
                return;
            }
            case 5: {
                executeQuery(server,sender,getOrNull(1,args));
                return;
            }
            case 6: {
                executeFill(server,sender,getOrNull(1,args),getOrNull(2,args),getOrNull(3,args));
                return;
            }
            case 7: {
                executeChunkInfo(server,sender);
                return;
            }
            case 8: {
                executeDimensions(server,sender);
                return;
            }
            default: sendMessage(sender,true,"options."+(sender instanceof Entity ? "entity" : "server"));
        }
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

    private void executeTileClass(MinecraftServer server, ICommandSender sender, @Nullable String arg1)
            throws CommandException {
        EntityPlayerMP player = sender instanceof EntityPlayerMP ? (EntityPlayerMP)sender : null;
        if(Objects.nonNull(arg1)) {
            try {
                player = getPlayer(server,sender,arg1);
            } catch (PlayerNotFoundException ex) {
                sendMessage(sender,true,"tileclass.player");
            }
        }
        if(Objects.nonNull(player)) {
            new PacketTileEntityClassQuery().addPlayers(player).send();
            sendMessage(sender,false,"tileclass");
        }
    }

    private void executeGamestage(MinecraftServer server, ICommandSender sender, @Nullable String stage)
            throws CommandException {
        if(Objects.isNull(stage)) {
            sendMessage(sender,true,"gamestage");
            return;
        }
        EntityPlayerMP player = sender instanceof EntityPlayerMP ? (EntityPlayerMP)sender : null;
        if(Objects.isNull(player)) {
            sendMessage(sender,true,"gamestage.player");
            return;
        }
        IStageData data = GameStageHelper.getPlayerData(player);
        if(Objects.isNull(data) || data==EMPTY_STAGE_DATA) {
            sendMessage(sender, true, "gamestage.data",player.getName());
            return;
        }
        String type = data.hasStage(stage) ? "remove" : "add";
        buildAndExecuteCommand(server,sender,"gamestage",type,"@s",stage);
        sendMessage(sender,false,"gamestage",type,stage);
    }

    private void executeGive(MinecraftServer server, ICommandSender sender, @Nullable String type,
                             @Nullable String qualifier) throws CommandException {
        if(Objects.isNull(type)) {
            sendMessage(sender,true,"give");
            return;
        }
        if(type.equals("mob")) {
            if(Objects.isNull(qualifier)) {
                sendMessage(sender,true,"give.mob");
                return;
            }
            giveMob(stack -> {
                World world = sender.getEntityWorld();
                if(!world.isRemote) {
                    Vec3d posVec = sender.getPositionVector();
                    EntityItem item = new EntityItem(sender.getEntityWorld(),posVec.x,posVec.y,posVec.z,stack);
                    item.setNoPickupDelay();
                    item.setOwner(sender.getName());
                    world.spawnEntity(item);
                }
            },qualifier);
        } //else if(type.equals("enchant")) {

        //}
    }

    private void giveMob(Consumer<ItemStack> itemEntityCreator, String mob) {
        if(!mob.contains(":")) mob = "minecraft:"+mob;
        ItemStack stack = new ItemStack(SPAWN_EGG);
        ItemMonsterPlacer.applyEntityIdToItemStack(stack,new ResourceLocation(mob));
        itemEntityCreator.accept(stack);
    }

    private void executeQuery(MinecraftServer server, ICommandSender sender, @Nullable String type) throws CommandException {
        if(Objects.isNull(type)) {
            sendMessage(sender,true,"query");
            return;
        }
        if(sender instanceof EntityPlayerMP)
            new PacketQueryGenericClient(type).addPlayers((EntityPlayerMP) sender).send();
    }

    private void executeFill(MinecraftServer server, ICommandSender sender, @Nullable String rangeStr,
                             @Nullable String toReplace, @Nullable String replaceWith) throws CommandException {
        double range = 1d;
        Set<Block> replaced = new HashSet<>();
        if(StringUtils.isNotBlank(rangeStr)) {
            try {
                range = Math.abs(Double.parseDouble(rangeStr));
            } catch(NumberFormatException ex) {
                sendMessage(sender,true,"fill.range",rangeStr);
            }
        }
        Vec3d posVec = sender.getPositionVector();
        BlockPos min = getRoundedPos(posVec,-range);
        BlockPos max = getRoundedPos(posVec,range);
        insertMatchingBlocks(replaced,toReplace);
        Block replacement = getEntry(BLOCKS,replaceWith);
        if(Objects.isNull(replacement)) replacement = AIR;
        ResourceLocation replacementRes = replacement.getRegistryName();
        int totalCount = count(min,max);
        LOGGER.info("Beginning block replacements for {} block from set `{}` with replacement `{}`. "+
                "This could take a while!",totalCount,replaced,replacementRes);
        int replacementCount = WorldUtil.replaceBlocks(sender.getEntityWorld(),replaced,replacement.getDefaultState(),min,max);
        sendMessage(sender,false,"fill",replacementCount,totalCount,replacementRes);
    }

    private void executeChunkInfo(MinecraftServer server, ICommandSender sender) throws CommandException {
        if(sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            boolean playerFast = SkillWrapper.makesChunksFast(player);
            boolean chunkFast = WorldUtil.isChunkFast(player.getEntityWorld(),player.chunkCoordX,player.chunkCoordZ);
            sendMessage(sender,false,"chunk",playerFast,chunkFast);
        } else sendMessage(sender,true,"chunk");
    }

    private void executeDimensions(MinecraftServer server, ICommandSender sender) throws CommandException {
        try {
            LOGGER.info("Querying load status of dimensions");
            List<Integer> loadedIDs = Arrays.asList(DimensionManager.getIDs());
            List<Integer> unloadedIDS = new ArrayList<>();
            for (Set<Integer> ids : DimensionManager.getRegisteredDimensions().values())
                for (Integer id : ids)
                    if (!loadedIDs.contains(id)) unloadedIDS.add(id);
            logDimensionSet("Loaded", loadedIDs);
            logDimensionSet("Unloaded", unloadedIDS);
        } catch(Exception ex) {
            LOGGER.error("FAILED TO QUERY 1 OR MORE DIMENSIONS",ex);
            sendMessage(sender,true,"dimension",ex);
        }
    }

    private void logDimensionSet(String prefix, Collection<Integer> ids) {
        LOGGER.info("{} dimension IDs:",prefix);
        for(Integer id : ids) {
            String str = String.valueOf(id);
            DimensionType type = null;
            try {
                type = DimensionManager.getProviderType(id);
            } catch(Exception ex) {
                LOGGER.error("Failed to get type for dimension ID {}!",id);
            }
            if(Objects.nonNull(type))
                str = String.format("ID: `%s` | NAME: `%s` | SUFFIX `%s`",str,type.getName(),type.getSuffix());
            LOGGER.info("Dimension Info ({})",str);
        }
    }

    private int count(BlockPos min, BlockPos max) {
        int xDif = max.getX()-min.getX();
        int yDif = max.getY()-min.getY();
        int zDif = max.getZ()-min.getZ();
        return Math.abs(xDif*yDif*zDif);
    }

    private BlockPos getRoundedPos(Vec3d center, double range) {
        return new BlockPos(round(center.x,range),round(center.y,range),round(center.z,range));
    }

    private int round(double original, double adder) {
        boolean ceil = adder>=0;
        double val = original+adder;
        return ceil ? MathHelper.ceil(val) : MathHelper.floor(val);
    }

    private void insertMatchingBlocks(Collection<Block> blocks, @Nullable String matcher) {
        if(equalsAnyOrBlank(matcher,"air","all","any","minecraft:air")) return;
        if(Objects.nonNull(matcher)) {
            if(matcher.contains(":")) {
                Block matched = getEntry(BLOCKS, matcher);
                if(Objects.nonNull(matched)) blocks.add(matched);
            } else {
                for(Block block : BLOCKS)
                    if(entryMatches(block,matcher)) blocks.add(block);
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private <V extends IForgeRegistryEntry<V>> @Nullable V getEntry(IForgeRegistry<V> registry, @Nullable String keyStr) {
        return Objects.nonNull(keyStr) ? getEntry(registry,new ResourceLocation(keyStr)) : null;
    }

    private <V extends IForgeRegistryEntry<V>> @Nullable V getEntry(IForgeRegistry<V> registry, ResourceLocation key) {
        return registry.containsKey(key) ? registry.getValue(key) : null;
    }

    private boolean entryMatches(IForgeRegistryEntry<?> entry, String matcher) {
        ResourceLocation res = entry.getRegistryName();
        if(Objects.isNull(res) || StringUtils.isBlank(matcher)) return true;
        return res.getNamespace().equals(matcher);
    }

    private boolean equalsAnyOrBlank(@Nullable String matcher, String ... matchThese) {
        if(StringUtils.isBlank(matcher)) return true;
        for(String toMatch : matchThese)
            if(matcher.equals(toMatch)) return true;
        return false;
    }
}