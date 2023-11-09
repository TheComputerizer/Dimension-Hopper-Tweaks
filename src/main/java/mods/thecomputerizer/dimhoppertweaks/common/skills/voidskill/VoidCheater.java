package mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill;

import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class VoidCheater extends ExtendedEventsTrait {

    public VoidCheater() {
        super("void_cheater",0,1,VOID,16,"void|64","magic|40","defense|40");
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if(event.getSource()==DamageSource.OUT_OF_WORLD && entity.dimension!=-7877 && event.getAmount()<1000f && entity.posY<-60d) {
            if(!entity.world.isRemote && entity instanceof EntityPlayer) {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                server.getCommandManager().executeCommand(server,DHDebugCommands.buildRawCommand("tpx",
                        entity.getName(),entity.posX,1000d,entity.posZ,entity.dimension));
            }
        }
    }
}
