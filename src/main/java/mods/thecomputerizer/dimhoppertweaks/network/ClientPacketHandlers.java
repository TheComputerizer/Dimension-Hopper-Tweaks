package mods.thecomputerizer.dimhoppertweaks.network;

import mods.thecomputerizer.dimhoppertweaks.client.ClientEvents;
import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
import mods.thecomputerizer.dimhoppertweaks.client.render.RenderDelayedAOE;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects.COLOR_CORRECTION_OVERRIDE;
import static mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects.MINING_SPEED;
import static mods.thecomputerizer.dimhoppertweaks.client.render.RenderEvents.ATTACKS;
import static net.minecraft.util.math.RayTraceResult.Type.BLOCK;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static net.silentchaos512.scalinghealth.config.Config.Player.Health.allowModify;
import static net.tslat.aoa3.client.event.KeyBinder.statusResourceGuiMessage;
import static net.tslat.aoa3.client.event.KeyBinder.statusSkillGuiMessage;

public class ClientPacketHandlers {

    @SideOnly(CLIENT)
    public static void handleBossClientEffects() {

    }

    @SideOnly(CLIENT)
    public static void handleRenderBossAttack(int start, int bossID, List<Vec3d> vecList, int size, int phase) {
        Minecraft mc = Minecraft.getMinecraft();
        if(start>0) {
            Entity entity = mc.player.world.getEntityByID(bossID);
            if(entity instanceof EntityFinalBoss) {
                EntityFinalBoss boss = (EntityFinalBoss)entity;
                for(Vec3d posVec : vecList) ATTACKS.add(new RenderDelayedAOE(posVec,start,size,boss,phase));
            }
        }
    }

    @SideOnly(CLIENT)
    public static void handleSyncPlayerHealth(boolean isActive) {
        allowModify = isActive;
    }

    @SideOnly(CLIENT)
    public static void handleGrayScaleOverride(float scale) {
        COLOR_CORRECTION_OVERRIDE = scale;
    }

    @SideOnly(CLIENT)
    public static void handleCapData(
            float miningSpeed, boolean skillKey, boolean resourcesKey, Set<Item> autoFeedItems,
            List<Tuple<Potion,Integer>> autoPotionItems) {
        MINING_SPEED = miningSpeed;
        statusSkillGuiMessage = !skillKey;
        statusResourceGuiMessage = !resourcesKey;
        ClientEvents.autoFeedItems = autoFeedItems;
        ClientEvents.autoPotionItems = autoPotionItems;
    }

    @SideOnly(CLIENT)
    public static void handleTileEntityClassQuery() {
        Minecraft mc = Minecraft.getMinecraft();
        if(Objects.nonNull(mc.world)) {
            RayTraceResult res = mc.objectMouseOver;
            if(Objects.nonNull(res) && res.typeOfHit==BLOCK) {
                TileEntity tile = mc.world.getTileEntity(res.getBlockPos());
                mc.player.sendChatMessage("You are looking at a tile of class "+(Objects.nonNull(tile) ?
                        tile.getClass().getName() : "NULL"));
            }
        }
    }

    @SideOnly(CLIENT)
    public static void handleGenericClientQuery(String type) {
        Minecraft mc = Minecraft.getMinecraft();
        switch(type) {
            case "client":
                DHTClient.queryClientEffects(mc);
                break;
            case "color_0":
                DHTClient.setColorCorrection(mc,0f);
                break;
            case "color_1":
                DHTClient.setColorCorrection(mc,1f);
                break;
            case "color_override_0":
                DHTClient.setColorOverride(mc,0f);
                break;
            case "color_override_1":
                DHTClient.setColorOverride(mc,1f);
                break;
            case "fix":
                DHTClient.fixFog(mc);
                break;
            case "fog":
                DHTClient.queryFog(mc);
                break;
            case "game":
                DHTClient.queryGame(mc);
                break;
            case "shader":
                DHTClient.queryShader(mc);
                break;
            case "world":
                DHTClient.queryWorld(mc);
                break;
        }
    }

    @SideOnly(CLIENT)
    public static void handleExtraChunkData(int chunkX, int chunkZ, boolean isFast) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            WorldClient world = Minecraft.getMinecraft().world;
            if(world.isChunkGeneratedAt(chunkX,chunkZ))
                ExtraChunkData.setFastChunk(world.getChunk(chunkX,chunkZ),isFast);
        });
    }
}
