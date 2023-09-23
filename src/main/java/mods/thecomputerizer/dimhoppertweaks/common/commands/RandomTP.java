package mods.thecomputerizer.dimhoppertweaks.common.commands;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import org.dimdev.ddutils.TeleportUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class RandomTP extends CommandBase {
    Random rand = new Random();

    @Nonnull
    public String getName() {
        return "dimrandomtp";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "Random TP initiated";
    }

    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) {
        if (args.length > 3) {
            long arg1=0;
            long arg2=0;
            EntityPlayerMP player=null;
            try {
                arg1 = parseLong(args[1]);
                arg2 = parseLong(args[2]);
                player = getPlayer(server, sender, args[0]);
            }
            catch(Exception e) {
                Constants.LOGGER.error("Failed to get player",e);
            }
            if(Objects.nonNull(player)) {
                DimensionManager.initDimension((int) arg1);
                WorldServer world = DimensionManager.getWorld((int) arg1);
                if (!world.isRemote) {
                    boolean success = true;
                    while (success) {
                        float x = rand.nextFloat() * arg2;
                        float y = 1000;
                        float z = rand.nextFloat() * arg2;
                        BlockPos pos = new BlockPos(x, y, z);
                        Biome biome = world.getBiome(pos);
                        for (int i=0; i<args.length-3; i++) {
                            if (Objects.requireNonNull(biome.getRegistryName()).toString().toLowerCase().contains(args[3 + i])) {
                                TeleportUtils.teleport(player, (int)arg1,x,y,z,player.rotationYaw,player.rotationPitch);
                                ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                                if((int) arg1==7 && Objects.nonNull(cap)) {
                                    cap.setTwilightRespawn(new BlockPos(x,100,z));
                                    SkillWrapper.forceTwilightRespawn(player);
                                }
                                notifyCommandListener(sender, this, "\u00A74\u00A7oYour consciousness " +
                                        "fades for a second as you hear a nearly indistinguishable voice in your mind\n" +
                                        "\uu00A7l\u00A74D\u00A7ko\u00A7r \u00A74\u00A7ln\u00A7ko\u00A7r\u00A74\u00A7lt " +
                                        "tr\u00A7kus\u00A7r\u00A74\u00A7lt \u00A7ktha\u00A7r\u00A74\u00A7lt b\u00A7kook");
                                success = false;
                                break;
                            }
                        }
                    }
                }
            } else notifyCommandListener(sender, this, "The player was null and cannot be teleported!");
        }
        else notifyCommandListener(sender, this, "Please whitelist some biomes");
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return index == 0;
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
