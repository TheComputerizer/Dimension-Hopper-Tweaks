package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(CommandTP.class)
public abstract class MixinCommandTP extends CommandBase {

    @Shadow
    private static void teleportEntityToCoordinates(Entity entity, CoordinateArg argX, CoordinateArg argY,
                                                    CoordinateArg argZ, CoordinateArg argYaw, CoordinateArg argPitch) {}

    /**
     * @author The_Computerizer
     * @reason Fix world comparison
     */
    @SuppressWarnings("ConstantValue")
    @Overwrite
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length<1) throw new WrongUsageException("commands.tp.usage");
        else {
            int i = 0;
            Entity entity;
            if(args.length!=2 && args.length!=4 && args.length!=6) entity = getCommandSenderAsPlayer(sender);
            else {
                entity = getEntity(server,sender,args[0]);
                i = 1;
            }
            if(args.length!=1 && args.length!=2) {
                if(args.length<i+3) throw new WrongUsageException("commands.tp.usage");
                else if (entity.world != null)
                {
                    int j = 4096;
                    int k = i + 1;
                    CommandBase.CoordinateArg arg = parseCoordinate(entity.posX,args[i],true);
                    CommandBase.CoordinateArg arg1 = parseCoordinate(entity.posY,args[k++],-4096,4096,false);
                    CommandBase.CoordinateArg arg2 = parseCoordinate(entity.posZ,args[k++],true);
                    CommandBase.CoordinateArg arg3 = parseCoordinate(entity.rotationYaw,args.length>k ? args[k++] : "~", false);
                    CommandBase.CoordinateArg arg4 = parseCoordinate(entity.rotationPitch,args.length>k ? args[k] : "~", false);
                    teleportEntityToCoordinates(entity,arg,arg1,arg2,arg3,arg4);
                    notifyCommandListener(sender,this,"commands.tp.success.coordinates",
                            entity.getName(),arg.getResult(),arg1.getResult(),arg2.getResult());
                }
            }
            else {
                Entity entity1 = getEntity(server, sender, args[args.length - 1]);
                if(entity.world.provider.getDimension()!=entity1.world.provider.getDimension())
                    throw new CommandException("commands.tp.notSameDimension");
                else {
                    entity.dismountRidingEntity();
                    if(entity instanceof EntityPlayerMP)
                        ((EntityPlayerMP)entity).connection.setPlayerLocation(entity1.posX,entity1.posY,entity1.posZ,entity1.rotationYaw,entity1.rotationPitch);
                    else entity.setLocationAndAngles(entity1.posX,entity1.posY,entity1.posZ,entity1.rotationYaw,entity1.rotationPitch);
                    notifyCommandListener(sender,this,"commands.tp.success",entity.getName(),entity1.getName());
                }
            }
        }
    }

}