package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.dimdev.ddutils.TeleportUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RandomTP extends DHTCommand {

    public RandomTP() {
        super("dimrandomtp");
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length>3) {
            long dim=0;
            long diameter=0;
            EntityPlayerMP player=null;
            try {
                dim = parseLong(args[1]);
                diameter = parseLong(args[2]);
                player = getPlayer(server,sender,args[0]);
            }
            catch(Exception e) {
                sendMessage(sender,true,"player");
            }
            if(Objects.nonNull(player)) {
                DimensionManager.initDimension((int)dim);
                WorldServer world = DimensionManager.getWorld((int)dim);
                if(!world.isRemote) {
                    boolean success = true;
                    while (success) {
                        float offset = ((float)diameter)/2f;
                        float x = (rand.nextFloat()*diameter)-offset;
                        float y = 1000;
                        float z = (rand.nextFloat()*diameter)-offset;
                        ResourceLocation biomeName = world.getBiome(new BlockPos(x,y,z)).getRegistryName();
                        if(Objects.nonNull(biomeName)) {
                            for(int i=0; i<args.length-3; i++) {
                                if(biomeName.toString().toLowerCase().contains(args[3+i])) {
                                    TeleportUtils.teleport(player,(int)dim,x,y,z,player.rotationYaw,player.rotationPitch);
                                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                                    if((int)dim==7 && Objects.nonNull(cap)) {
                                        cap.setTwilightRespawn(new BlockPos(x,100,z));
                                        SkillWrapper.forceTwilightRespawn(player);
                                    }
                                    sendMessage(sender,false,null);
                                    success = false;
                                    break;
                                }
                            }
                        } else sendMessage(sender,true,"biome",x,y,z);
                    }
                }
            }
        } else sendMessage(sender,true,null);
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return index == 0;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          @Nullable BlockPos targetPos) {
        if(args.length==1) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        if(args.length==2) return getDimensionsTabCompletion();
        return args.length==3 ? Collections.emptyList() : getBiomesTabCompletion();
    }
}
