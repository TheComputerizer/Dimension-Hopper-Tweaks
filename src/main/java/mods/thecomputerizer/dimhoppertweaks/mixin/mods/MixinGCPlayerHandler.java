package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemArmorAsteroids;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings({"ConstantValue", "ConditionalExpressionWithIdenticalBranches", "CallToPrintStackTrace"})
@Mixin(value = GCPlayerHandler.class, remap = false)
public abstract class MixinGCPlayerHandler {

    @Shadow protected abstract void checkCurrentItem(EntityPlayerMP player);

    @Shadow protected abstract void sendPlanetList(EntityPlayerMP player, GCPlayerStats stats);

    @Shadow protected abstract void sendDungeonDirectionPacket(EntityPlayerMP player, GCPlayerStats stats);

    @Shadow protected abstract void sendThermalLevelPacket(EntityPlayerMP player, GCPlayerStats stats);

    @Shadow public abstract void preventFlyingKicks(EntityPlayerMP player);

    @Shadow public static void checkGear(EntityPlayerMP player, GCPlayerStats stats, boolean forceSend) {}

    @Shadow protected abstract void checkThermalStatus(EntityPlayerMP player, GCPlayerStats playerStats);

    @Shadow protected abstract void checkOxygen(EntityPlayerMP player, GCPlayerStats stats);

    @Shadow protected abstract void checkShield(EntityPlayerMP playerMP, GCPlayerStats playerStats);

    @Shadow protected abstract void throwMeteors(EntityPlayerMP player);

    @Shadow protected abstract void updateSchematics(EntityPlayerMP player, GCPlayerStats stats);

    @Shadow protected static void sendAirRemainingPacket(EntityPlayerMP player, GCPlayerStats stats) {
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @Overwrite
    private void onPlayerLogin(EntityPlayerMP player) {
        GCPlayerStats stats = GCPlayerStats.get(player);
        if(Objects.nonNull(stats)) {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, GCCoreUtil.getDimensionID(player.world), new Object[]{}), player);
            int repeatCount = stats.getBuildFlags() >> 9;
            if (repeatCount < 3) stats.setBuildFlags(stats.getBuildFlags() & 1536);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATS, GCCoreUtil.getDimensionID(player.world), stats.getMiscNetworkedStats()), player);
            ColorUtil.sendUpdatedColorsToPlayer(stats);
        }
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @Overwrite
    public void onPlayerUpdate(EntityPlayerMP player) {
        int tick = player.ticksExisted - 1;
        GCPlayerStats stats = GCPlayerStats.get(player);
        if (Objects.nonNull(stats)) {
            if ((ConfigManagerCore.challengeSpawnHandling) && stats.getUnlockedSchematics().isEmpty()) {
                if (!stats.getStartDimension().isEmpty()) stats.setStartDimension("");
                else {
                    WorldServer worldOld = (WorldServer) player.world;
                    try {
                        worldOld.getPlayerChunkMap().removePlayer(player);
                    } catch (Exception ignored) {
                    }
                    worldOld.playerEntities.remove(player);
                    worldOld.updateAllPlayersSleepingFlag();
                    worldOld.loadedEntityList.remove(player);
                    worldOld.onEntityRemoved(player);
                    worldOld.getEntityTracker().untrack(player);
                    if (player.addedToChunk && worldOld.getChunkProvider().chunkExists(player.chunkCoordX, player.chunkCoordZ)) {
                        Chunk chunkOld = worldOld.getChunk(player.chunkCoordX, player.chunkCoordZ);
                        chunkOld.removeEntity(player);
                        chunkOld.setModified(true);
                    }
                    WorldServer worldNew = WorldUtil.getStartWorld(worldOld);
                    int dimID = GCCoreUtil.getDimensionID(worldNew);
                    player.dimension = dimID;
                    GalacticraftCore.logger.debug("DEBUG: Sending respawn packet to player for dim " + dimID);
                    player.connection.sendPacket(new SPacketRespawn(dimID, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
                    if (worldNew.provider instanceof WorldProviderSpaceStation)
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(player.world), new Object[]{}), player);
                    worldNew.spawnEntity(player);
                    player.setWorld(worldNew);
                    player.server.getPlayerList().preparePlayer(player, worldOld);
                }
                player.interactionManager.setWorld((WorldServer) player.world);
                final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(player.world.provider.getClass());
                Vector3 spawnPos = type.getPlayerSpawnLocation((WorldServer) player.world, player);
                ChunkPos pair = player.world.getChunk(spawnPos.intX() >> 4, spawnPos.intZ() >> 4).getPos();
                GalacticraftCore.logger.debug("Loading first chunk in new dimension.");
                ((WorldServer) player.world).getChunkProvider().loadChunk(pair.x, pair.z);
                player.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, player.rotationYaw, player.rotationPitch);
                type.setupAdventureSpawn(player);
                type.onSpaceDimensionChanged(player.world, player, false);
                player.setSpawnChunk(new BlockPos(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ()), true, GCCoreUtil.getDimensionID(player.world));
                stats.setNewAdventureSpawn(true);
            }
            final boolean isInGCDimension = player.world.provider instanceof IGalacticraftWorldProvider;
            if (tick >= 25) {
                if (ConfigManagerCore.enableSpaceRaceManagerPopup && !stats.hasOpenedSpaceRaceManager()) {
                    SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(player));
                    if (Objects.isNull(race) || race.teamName.equals(SpaceRace.DEFAULT_NAME))
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_OPEN_SPACE_RACE_GUI, GCCoreUtil.getDimensionID(player.world), new Object[]{}), player);
                    stats.setOpenedSpaceRaceManager(true);
                }
                if (!stats.hasSentFlags()) {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATS, GCCoreUtil.getDimensionID(player.world), stats.getMiscNetworkedStats()), player);
                    stats.setSentFlags(true);
                }
            }
            if (stats.getCryogenicChamberCooldown() > 0)
                stats.setCryogenicChamberCooldown(stats.getCryogenicChamberCooldown() - 1);
            if (!player.onGround && stats.isLastOnGround())
                stats.setTouchedGround(true);
            if (stats.getTeleportCooldown() > 0)
                stats.setTeleportCooldown(stats.getTeleportCooldown() - 1);
            if (stats.getChatCooldown() > 0)
                stats.setChatCooldown(stats.getChatCooldown() - 1);
            if (stats.getOpenPlanetSelectionGuiCooldown() > 0) {
                stats.setOpenPlanetSelectionGuiCooldown(stats.getOpenPlanetSelectionGuiCooldown() - 1);
                if (stats.getOpenPlanetSelectionGuiCooldown() == 1 && !stats.hasOpenedPlanetSelectionGui()) {
                    WorldUtil.toCelestialSelection(player, stats, stats.getSpaceshipTier());
                    stats.setHasOpenedPlanetSelectionGui(true);
                }
            }
            if (stats.isUsingParachute()) {
                if (!stats.getLastParachuteInSlot().isEmpty()) {
                    player.fallDistance = 0f;
                    if (player.onGround) GCPlayerHandler.setUsingParachute(player, stats, false);
                }
                this.checkCurrentItem(player);
                if (stats.isUsingPlanetSelectionGui()) this.sendPlanetList(player, stats);
                if (stats.getDamageCounter() > 0) stats.setDamageCounter(stats.getDamageCounter() - 1);
                if (isInGCDimension) {
                    if (tick % 10 == 0) {
                        boolean doneDungeon = false;
                        ItemStack current = player.inventory.getCurrentItem();
                        if (Objects.nonNull(current) && current.getItem() == GCItems.dungeonFinder) {
                            this.sendDungeonDirectionPacket(player, stats);
                            doneDungeon = true;
                        }
                        if (tick % 30 == 0) {
                            sendAirRemainingPacket(player, stats);
                            this.sendThermalLevelPacket(player, stats);
                            if (!doneDungeon) {
                                for (ItemStack stack : player.inventory.mainInventory) {
                                    if (Objects.nonNull(stack) && stack.getItem() == GCItems.dungeonFinder) {
                                        this.sendDungeonDirectionPacket(player, stats);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (player.getRidingEntity() instanceof EntityLanderBase) {
                        stats.setInLander(true);
                        stats.setJustLanded(false);
                    } else {
                        if (stats.isInLander()) stats.setJustLanded(true);
                        stats.setInLander(false);
                    }
                    if (player.onGround && stats.hasJustLanded()) {
                        stats.setJustLanded(false);
                        if (player.getBedLocation(GCCoreUtil.getDimensionID(player.world)) == null || stats.isNewAdventureSpawn()) {
                            int i = 30000000;
                            int j = Math.min(i, Math.max(-i, MathHelper.floor(player.posX + 0.5D)));
                            int k = Math.min(256, Math.max(0, MathHelper.floor(player.posY + 1.5D)));
                            int l = Math.min(i, Math.max(-i, MathHelper.floor(player.posZ + 0.5D)));
                            BlockPos coords = new BlockPos(j, k, l);
                            player.setSpawnChunk(coords, true, GCCoreUtil.getDimensionID(player.world));
                            stats.setNewAdventureSpawn(false);
                        }
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(player.world), new Object[]{}), player);
                    }
                    if (player.world.provider instanceof WorldProviderSpaceStation || player.world.provider instanceof IZeroGDimension || player.world.provider instanceof WorldProviderAsteroids) {
                        this.preventFlyingKicks(player);
                        if (player.world.provider instanceof WorldProviderSpaceStation && stats.isNewInOrbit()) {
                            ((WorldProviderSpaceStation) player.world.provider).getSpinManager().sendPackets(player);
                            stats.setNewInOrbit(false);
                        }
                    } else stats.setNewInOrbit(true);
                } else stats.setNewInOrbit(true);
                checkGear(player, stats, false);
                if (stats.getChestSpawnCooldown() > 0) {
                    stats.setChestSpawnCooldown(stats.getChestSpawnCooldown() - 1);
                    if (stats.getChestSpawnCooldown() == 180) {
                        if (stats.getChestSpawnVector() != null) {
                            EntityParachest chest = new EntityParachest(player.world, stats.getRocketStacks(), stats.getFuelLevel());
                            chest.setPosition(stats.getChestSpawnVector().x, stats.getChestSpawnVector().y, stats.getChestSpawnVector().z);
                            chest.color = stats.getParachuteInSlot().isEmpty() ? EnumDyeColor.WHITE : ItemParaChute.getDyeEnumFromParachuteDamage(stats.getParachuteInSlot().getItemDamage());
                            if (!player.world.isRemote) player.world.spawnEntity(chest);
                        }
                    }
                }
                if (stats.getLaunchAttempts() > 0 && player.getRidingEntity() == null) stats.setLaunchAttempts(0);

                this.checkThermalStatus(player, stats);
                this.checkOxygen(player, stats);
                this.checkShield(player, stats);
                if (isInGCDimension && (stats.isOxygenSetupValid() != stats.isLastOxygenSetupValid() || tick % 100 == 0))
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, player.world.provider.getDimension(), new Object[]{stats.isOxygenSetupValid()}), player);
                this.throwMeteors(player);
                this.updateSchematics(player, stats);
                if (tick % 250 == 0 && stats.getFrequencyModuleInSlot().isEmpty() && !stats.hasReceivedSoundWarning() && isInGCDimension && player.onGround && tick > 0 && ((IGalacticraftWorldProvider) player.world.provider).getSoundVolReductionAmount() > 1.0F) {
                    String[] string2 = GCCoreUtil.translate("gui.frequencymodule.warning1").split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (String aString2 : string2) sb.append(" ").append(EnumColor.YELLOW).append(aString2);
                    player.sendMessage(new TextComponentString(EnumColor.YELLOW +
                            GCCoreUtil.translate("gui.frequencymodule.warning0") + " " + EnumColor.AQUA +
                            GCItems.basicItem.getItemStackDisplayName(new ItemStack(GCItems.basicItem, 1, 19)) + sb));
                    stats.setReceivedSoundWarning(true);
                }
                if (tick % 40 == 1 && player.inventory != null) {
                    int titaniumCount = 0;
                    for (ItemStack armorPiece : player.getArmorInventoryList())
                        if (Objects.nonNull(armorPiece) && armorPiece.getItem() instanceof ItemArmorAsteroids)
                            titaniumCount++;
                    if (stats.getSavedSpeed() == 0F) {
                        if (titaniumCount == 4) {
                            float speed = player.capabilities.getWalkSpeed();
                            if (speed < 0.118F) {
                                try {
                                    Field f = player.capabilities.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "walkSpeed" : "walkSpeed");
                                    f.setAccessible(true);
                                    f.set(player.capabilities, 0.118F);
                                    stats.setSavedSpeed(speed);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if (titaniumCount < 4) {
                        try {
                            Field f = player.capabilities.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "walkSpeed" : "walkSpeed");
                            f.setAccessible(true);
                            f.set(player.capabilities, stats.getSavedSpeed());
                            stats.setSavedSpeed(0F);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                stats.setLastOxygenSetupValid(stats.isOxygenSetupValid());
                stats.setLastUnlockedSchematics(stats.getUnlockedSchematics());
                stats.setLastOnGround(player.onGround);
            }
        }
    }
}
