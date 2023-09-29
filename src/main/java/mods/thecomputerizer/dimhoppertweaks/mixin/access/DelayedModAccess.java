package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

public class DelayedModAccess {

    private static final Collection<String> BLOCK_BREAKER_CLASS_NAME = Arrays.asList(
            "openblocks.common.tileentity.TileEntityBlockBreaker",
            "lumien.randomthings.tileentity.TileEntityBlockBreaker",
            "sblectric.lightningcraft.tiles.TileEntityLightningBreaker",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityBreaker",
            "com.rwtema.extrautils2.tile.TileMine",
            "li.cil.oc.common.tileentity.RobotProxy",
            "appeng.tile.networking.TileCableBus");
    private static final Set<Class<?>> BLOCK_BREAKER_CLASSES = new HashSet<>();
    private static boolean FOUND_BREAKER_CLASSES = false;

    public static Collection<String> getGameStages(EntityPlayer player) {
        if(Objects.isNull(player)) return new ArrayList<>();
        IStageData data = GameStageHelper.getPlayerData(player);
        return Objects.isNull(data) ? new ArrayList<>() : data.getStages();
    }

    public static void setGameStages(EntityPlayer player, Collection<String> stages) {
        for(String stage : stages) setGameStage(player,stage);
    }

    public static void setGameStage(EntityPlayer player, String stage) {
        if(Objects.nonNull(player)) GameStageHelper.addStage(player,stage);
    }

    public static boolean hasGameStage(EntityPlayer player, String stage) {
        return Objects.nonNull(player) && GameStageHelper.hasStage(player,stage);
    }
    public static ItemStack cheese() {
        return new ItemStack(ItemListxlfoodmod.cheese);
    }

    public static Set<Class<?>> getBreakerTileClasses() {
        if(!FOUND_BREAKER_CLASSES) findBreakerClasses();
        return Collections.unmodifiableSet(BLOCK_BREAKER_CLASSES);
    }

    private static void findBreakerClasses() {
        for(String className : BLOCK_BREAKER_CLASS_NAME) {
            try {
                Class<?> foundClass = Class.forName(className);
                if(TileEntity.class.isAssignableFrom(foundClass)) {
                    BLOCK_BREAKER_CLASSES.add(foundClass);
                    Constants.LOGGER.info("Registered tile entity class with name `{}` as an automatic block "+
                            "breaker",className);
                } else Constants.LOGGER.error("Tried to register non tile entity class with name {} as an " +
                        "automatic block breaker!",className);
            } catch (ClassNotFoundException ex) {
                Constants.LOGGER.error("Could not locate class with name `{}`",className);
            }
        }
        FOUND_BREAKER_CLASSES = true;
    }
}
