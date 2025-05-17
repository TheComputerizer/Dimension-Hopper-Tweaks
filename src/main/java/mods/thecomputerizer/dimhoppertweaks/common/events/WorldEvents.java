package mods.thecomputerizer.dimhoppertweaks.common.events;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import gcewing.sg.block.SGBlock;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mekanism.common.security.ISecurityTile;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent.*;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.tools.TinkerToolCore;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.BLOCK_FOR_BLOCK;
import static net.minecraft.init.Blocks.COAL_ORE;
import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST;
import static org.burningwave.core.assembler.StaticComponentContainer.Fields;

@EventBusSubscriber(modid = MODID)
public class WorldEvents {

    @SubscribeEvent(priority = LOWEST)
    public static void blockBreak(BreakEvent event) {
        if(event.isCanceled()) return;
        if(event.getState().getBlock() instanceof SGBlock<?>) event.setCanceled(true);
        else if(event.getPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) {
                Item heldItem = player.getHeldItemMainhand().getItem();
                if (heldItem instanceof ItemTool || heldItem instanceof TinkerToolCore) {
                    IBlockState state = event.getState();
                    int harvestLevel = state.getBlock()==COAL_ORE ? 1 : state.getBlock().getHarvestLevel(state);
                    harvestLevel = harvestLevel<=0 && player.world.rand.nextFloat()<=((float)(((int)(Math.log(
                            cap.getSkillLevel("mining"))/Math.log(2)))+1))/10f ? 1 : harvestLevel;
                    if(harvestLevel>0) {
                        int hardness = (int) state.getBlockHardness(event.getWorld(),event.getPos());
                        int hardnessPower = Math.min(hardness > 1 ? (int)(Math.log(hardness)/Math.log(2)) : 0, 10);
                        SkillWrapper.addActionSP(player,"mining",Math.max(1,(hardnessPower+harvestLevel)/2));
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = LOWEST)
    public static void blockPlace(PlaceEvent event) {
        if(event.isCanceled()) return;
        EntityPlayer player = event.getPlayer();
        if(player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
            PlayerData data = PlayerDataHandler.get(player);
            if(Objects.nonNull(data)) {
                SkillWrapper.executeOnSkills(data,h -> {
                    if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onBlockPlaced(event);
                });
            }
            IBlockState state = event.getState();
            Block block = state.getBlock();
            if(!(block instanceof BlockFarmland))
                SkillWrapper.addActionSP((EntityPlayerMP)event.getPlayer(),"building",1f);
            ResourceLocation res = block.getRegistryName();
            if(Objects.nonNull(res) && SkillWrapper.hasTrait(player,"building",BLOCK_FOR_BLOCK)) {
                int time = 0;
                String path = res.getPath();
                if(path.contains("obsidian")) time = 1;
                if(res.getPath().equals("compressed_obsidian")) time+=(block.getMetaFromState(state)+1)*2;
                time*=20;
                if(time>0) TickEvents.addInfernalDistractor(player,time);
            }
        }
        if(Objects.nonNull(player)) {
            TileEntity tile = event.getWorld().getTileEntity(event.getPos());
            if(Objects.nonNull(tile) && !(tile instanceof ISecurityTile)) {
                Collection<String> stages = getPotentialFakePlayerStages(player,event.getPos());
                if(Objects.nonNull(stages) && !stages.isEmpty()) {
                    ((ITileEntity)tile).dimhoppertweaks$setStages();
                    if(tile instanceof CrafterBaseTE)
                        DelayedModAccess.inheritInventoryStages(tile,Fields.get(tile,"workInventory"));
                }
            }
        }
    }

    @SubscribeEvent(priority = LOWEST)
    public static void onExplosionDetonate(Detonate event) {
        if(event.isCanceled()) return;
        if(!event.getWorld().isRemote) {
            Vec3d center = event.getExplosion().getPosition();
            EntityPlayer harvester = null;
            for(EntityPlayer player : event.getWorld().playerEntities) {
                if(center.distanceTo(player.getPositionVector())<=56d) {
                    harvester = player;
                    break;
                }
            }
            if(Objects.nonNull(harvester)) {
                PlayerData data = PlayerDataHandler.get(harvester);
                if(Objects.nonNull(data)) {
                    SkillWrapper.executeOnSkills(data,h -> {
                        if(h instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)h).onExplosionDetonate(event);
                    });
                }
            }
        }
    }

    private static Collection<String> getPotentialFakePlayerStages(EntityPlayer player, BlockPos pos) {
        if(player instanceof FakePlayer) {
            Set<Class<?>> placerClasses = DelayedModAccess.getPlacerTileClasses();
            World world = player.getEntityWorld();
            TileEntity tile = WorldUtil.checkValidTile(world,player.getPosition(),placerClasses);
            if(Objects.nonNull(tile)) return ((ITileEntity)tile).dimhoppertweaks$getStages();
            tile = WorldUtil.getTileOrAdjacent(world,pos,false,placerClasses);
            if(Objects.nonNull(tile)) return ((ITileEntity)tile).dimhoppertweaks$getStages();
        }
        return DelayedModAccess.getGameStages(player);
    }
}