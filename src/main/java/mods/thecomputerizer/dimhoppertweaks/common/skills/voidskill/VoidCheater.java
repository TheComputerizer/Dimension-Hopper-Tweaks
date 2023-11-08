package mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class VoidCheater extends Trait {

    public VoidCheater() {
        super(Constants.res("void_cheater"),0,1,Constants.res("void"),16,
                "dimhoppertweaks:void|64","reskillable:magic|40","reskillable:defense|40");
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
