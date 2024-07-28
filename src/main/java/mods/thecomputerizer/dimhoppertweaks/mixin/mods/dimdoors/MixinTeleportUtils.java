package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import com.brandon3055.brandonscore.lib.DummyTeleporter;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.dimdev.ddutils.TeleportUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.EnumSet;

@Mixin(value = TeleportUtils.class, remap = false)
public abstract class MixinTeleportUtils {
    
    @Shadow private static void setInvulnerableDimensionChange(EntityPlayerMP player, boolean value) {}
    @Shadow private static void captureCurrentPosition(NetHandlerPlayServer connection) {}
    @Shadow private static void setEnteredNetherPosition(EntityPlayerMP player, Vec3d value) {}
    @Shadow private static void updateplayers(DragonFightManager dragonFightManager) {}
    @Shadow private static void setThrower(EntityThrowable entity, EntityLivingBase value) {}
    @Shadow private static void copyDataFromOld(Entity newEntity, Entity oldEntity) {}
    @Shadow private static void searchForOtherItemsNearby(EntityItem item) {}
    
    /**
     * @author The_Computerizer
     * @reason Idk
     */
    @Overwrite
    public static Entity teleport(Entity entity, int newDimension, double x, double y, double z, float yaw, float pitch) {
        DHTRef.LOGGER.info("Teleporting to ({},{},{}) in dimension {}",x,y,z,newDimension);
        if (entity instanceof FakePlayer) return entity;
        if (entity.world.isRemote || entity.isDead) return null; // dead means inactive, not a dead player
        
        yaw = MathHelper.wrapDegrees(yaw);
        pitch = MathHelper.wrapDegrees(pitch);
        
        entity.dismountRidingEntity(); // TODO: would be nice to teleport them too
        entity.removePassengers();
        
        int oldDimension = entity.dimension;
        // int newDimension = dim;
        
        boolean noClip = entity.noClip;
        
        if (entity instanceof EntityPlayerMP) {
            // Workaround for https://bugs.mojang.com/browse/MC-123364. Disables player-in-block checking, but doesn't seem
            // to make the player actually noclip.
            entity.noClip = true;
            
            // Prevent Minecraft from cancelling the position change being too big if the player is not in creative
            // This has to be done when the teleport is done from the player moved function (so any block collision event too)
            // Not doing this will cause the player to be invisible for others.
            setInvulnerableDimensionChange((EntityPlayerMP)entity,true);
        }
        
        if (oldDimension == newDimension) { // Based on CommandTeleport.doTeleport
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                player.connection.setPlayerLocation(x,y,z,yaw,pitch,EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class));
                // Fix for https://bugs.mojang.com/browse/MC-98153. See this comment: https://bugs.mojang.com/browse/MC-98153#comment-411524
                captureCurrentPosition(player.connection);
            } else entity.setLocationAndAngles(x,y,z,yaw,pitch);
            entity.setRotationYawHead(yaw);
            return entity;
        } else { // Based on Entity.changeDimension
            MinecraftServer server = entity.getServer();
            WorldServer oldWorld = server.getWorld(oldDimension);
            WorldServer newWorld = server.getWorld(newDimension);
            // Allow other mods to cancel the event
            if (!ForgeHooks.onTravelToDimension(entity,newDimension)) {
                // Fixes invulnerability issues when the teleport is canceled
                if(entity instanceof EntityPlayerMP) {
                    setInvulnerableDimensionChange((EntityPlayerMP)entity,false);
                    entity.noClip = noClip;
                }
                return entity;
            }
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                PlayerList list = server.getPlayerList();
                // Setting this field seems to be useful for advancements. Adjusted dimension checks for non-vanilla
                // dimension support (entering the nether from any dimension should trigger it now).
                if(newDimension==-1) setEnteredNetherPosition(player,new Vec3d(player.posX,player.posY,player.posZ));
                else if (oldDimension!=-1 && newDimension!=0) setEnteredNetherPosition(player, null);
                // Send respawn packets to the player
                player.dimension = newDimension;
                player.connection.sendPacket(new SPacketRespawn(player.dimension,newWorld.getDifficulty(),
                        newWorld.getWorldInfo().getTerrainType(),player.interactionManager.getGameType()));
                list.updatePermissionLevel(player); // Sends an SPacketEntityStatus
                // Remove player entity from the old world
                oldWorld.removeEntityDangerously(player);
                // Move the player entity to new world
                player.isDead = false;
                list.transferEntityToWorld(player,oldDimension,oldWorld,newWorld,new DummyTeleporter(newWorld,x,y,z,pitch,yaw));
                // Sync the player
                list.preparePlayer(player,oldWorld);
                player.connection.setPlayerLocation(player.posX,player.posY,player.posZ,player.rotationYaw,player.rotationPitch);
                // Fix for https://bugs.mojang.com/browse/MC-98153. See this comment: https://bugs.mojang.com/browse/MC-98153#comment-411524
                captureCurrentPosition(player.connection);
                player.interactionManager.setWorld(newWorld);
                player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
                list.updateTimeAndWeatherForPlayer(player, newWorld);
                list.syncPlayerInventory(player);
                for(PotionEffect potioneffect : player.getActivePotionEffects())
                    player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
                // Resend attributes
                AttributeMap map = (AttributeMap)player.getAttributeMap();
                Collection<IAttributeInstance> attributes = map.getWatchedAttributes();
                if(!attributes.isEmpty()) player.connection.sendPacket(new SPacketEntityProperties(player.getEntityId(), attributes));
                // Force WorldProviderEnd to check if end dragon bars should be removed. Duplicate end dragon bars even
                // happen when leaving the end using an end portal while the dragon is alive, so this might be a vanilla
                // or Forge bug (maybe the world is unloaded before checking players?). In vanilla, updateplayers is normally
                // called every second.
                if(oldWorld.provider instanceof WorldProviderEnd) {
                    DragonFightManager dragonFightManager = ((WorldProviderEnd)oldWorld.provider).getDragonFightManager();
                    updateplayers(dragonFightManager);
                }
                // Vanilla also plays SoundEvents.BLOCK_PORTAL_TRAVEL, we won't do this.
                FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player,oldDimension,newDimension);
                //player.prevBlockpos = null; // For frost walk. Is this needed? What about other fields?
                /*player.lastExperience = -1;
                player.lastHealth = -1.0F;
                player.lastFoodLevel = -1;*/
                return entity;
            } else {
                if (entity instanceof EntityMinecartContainer) ((EntityMinecartContainer) entity).dropContentsWhenDead = false;
                if (entity instanceof EntityEnderPearl) setThrower((EntityThrowable) entity, null); // Otherwise the player will be teleported to the hit position but in the same dimension
                entity.world.profiler.startSection("changeDimension");
                entity.dimension = newDimension;
                entity.world.removeEntity(entity);
                entity.isDead = false;
                entity.world.profiler.startSection("reposition");
                oldWorld.updateEntityWithOptionalForce(entity, false);
                entity.world.profiler.endStartSection("reloading");
                Entity newEntity = EntityList.newEntity(entity.getClass(), newWorld);
                if(newEntity != null) {
                    copyDataFromOld(newEntity, entity);
                    newEntity.setPositionAndRotation(x, y, z, yaw, pitch);
                    boolean oldForceSpawn = newEntity.forceSpawn;
                    newEntity.forceSpawn = true;
                    newWorld.spawnEntity(newEntity);
                    newEntity.forceSpawn = oldForceSpawn;
                    newWorld.updateEntityWithOptionalForce(newEntity, false);
                }
                entity.isDead = true;
                entity.world.profiler.endSection();
                oldWorld.resetUpdateEntityTick();
                newWorld.resetUpdateEntityTick();
                entity.world.profiler.endSection();
                if(newEntity instanceof EntityItem) searchForOtherItemsNearby((EntityItem) newEntity); // TODO: This isn't in same-dimension teleportation in vanilla, but why?
                return newEntity;
            }
        }
    }
}