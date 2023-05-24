package mods.thecomputerizer.dimhoppertweaks.network;

import mods.thecomputerizer.dimhoppertweaks.client.entity.render.RenderDelayedAOE;
import mods.thecomputerizer.dimhoppertweaks.client.entity.render.RenderEvents;
import mods.thecomputerizer.dimhoppertweaks.common.objects.DimensionHopperSounds;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

public class ClientPacketHandlers {

    @SideOnly(Side.CLIENT)
    public static void handleUpdateBossRender(int bossID, int phase, boolean shield, String animation, int chargeTime) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(Objects.nonNull(player)) {
            Entity entity = player.world.getEntityByID(bossID);
            if (entity instanceof EntityFinalBoss) {
                EntityFinalBoss boss = (EntityFinalBoss) entity;
                boss.phase = phase;
                boss.updateShieldClient(shield);
                boss.setAnimation(animation,false);
                boss.setProjectileCharge(chargeTime);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void handleBossClientEffects() {

    }

    @SideOnly(Side.CLIENT)
    public static void handleRenderBossAttack(int start, int bossID, List<Vec3d> vecList, int size, int phase) {
        Minecraft mc = Minecraft.getMinecraft();
        if(start>0) {
            Entity entity = mc.player.world.getEntityByID(bossID);
            if(entity instanceof EntityFinalBoss) {
                EntityFinalBoss boss = (EntityFinalBoss)entity;
                for (Vec3d posVec : vecList)
                    RenderEvents.ATTACKS.add(new RenderDelayedAOE(posVec,start,size,boss,phase));
            }
        } else mc.getSoundHandler().playSound(PositionedSoundRecord.getMusicRecord(DimensionHopperSounds.MUSIC));
    }
}
