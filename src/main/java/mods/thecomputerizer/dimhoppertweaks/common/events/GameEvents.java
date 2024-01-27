package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.network.PacketHandler;
import de.ellpeck.nyx.network.PacketNyxWorld;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkDataProvider;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillCapabilityProvider;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.network.PacketQueryGenericClient;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DHTRef.MODID)
public class GameEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP && !(event.getObject() instanceof FakePlayer))
            event.addCapability(SkillWrapper.SKILL_CAPABILITY,new SkillCapabilityProvider());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event) {
       event.addCapability(ExtraChunkData.CHUNK_CAPABILITY,new ExtraChunkDataProvider());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerJoin(PlayerLoggedInEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            checkDimStage(player,false,false,"bridgeone",7,20,684);
            if(!GameStageHelper.hasStage(player,"bedrockFinal"))
                player.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.BEDROCK),-1,0,null);
            SkillWrapper.onPlayerJoin(player);
            World world = player.getEntityWorld();
            WorldUtil.iterateChunks(world,player.chunkCoordX,player.chunkCoordZ,1,
                    chunk -> WorldUtil.setFastChunk(world,chunk.x,chunk.z,player));
            new PacketQueryGenericClient("fix").addPlayers(player).send();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) SkillWrapper.forceTwilightRespawn(event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void pickUpItem(ItemPickupEvent event) {
        if(event.isCanceled()) return;
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            ItemStack stack = event.getStack();
            if(!GameStageHelper.hasStage(player,"bedrockFinal") &&
                    stack.getItem()==Item.getItemFromBlock(Blocks.BEDROCK))
                stack.setCount(0);
            if(stack.getCount()>0) {
                ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                if(Objects.nonNull(cap) && cap.checkGatheringItem(stack.getItem())) {
                    int sizeFactor = stack.getCount()>1 ? (int)(Math.log(stack.getCount())/Math.log(2)) : 1;
                    SkillWrapper.addActionSP(player,"gathering",sizeFactor);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onChangedDimensions(PlayerChangedDimensionEvent event) {
        if(event.isCanceled()) return;
        PlayerData data = PlayerDataHandler.get(event.player);
        if(Objects.nonNull(data)) {
            SkillWrapper.executeOnSkills(data, h -> {
                if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onChangeDimensions(event);
            });
        }
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            checkDimStage(player,false,true,"twilight",7);
            checkDimStage(player,true,false,"nether",-1);
            checkDimStage(player,true,false,"finalfrontier",-19);
            SkillWrapper.addActionSP(player,"void",5f);
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) cap.resetDreamTimer();
            NyxWorld world = NyxWorld.get(player.getEntityWorld());
            if(Objects.nonNull(world.currentEvent)) PacketHandler.sendTo(player,new PacketNyxWorld(world));
        }
    }

    private static void checkDimStage(EntityPlayer player, boolean goodDim, boolean goodStage, String stage, int ... dimensions) {
        boolean passedDimCheck = dimensions.length==0 || !goodDim;
        for(int dim : dimensions) {
            if(player.dimension==dim) {
                if(!goodDim) return;
                else {
                    passedDimCheck = true;
                    break;
                }
            }
        }
        if(passedDimCheck && goodStage== GameStageHelper.hasStage(player,stage)) {
            if(!goodStage) GameStageHelper.addStage(player,stage);
            else GameStageHelper.removeStage(player,stage);
        }
    }
}
