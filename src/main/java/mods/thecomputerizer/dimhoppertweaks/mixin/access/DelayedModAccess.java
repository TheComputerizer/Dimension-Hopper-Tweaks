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

    private static final Collection<String> BLOCK_BREAKER_CLASS_NAMES = Arrays.asList(
            "openblocks.common.tileentity.TileEntityBlockBreaker",
            "lumien.randomthings.tileentity.TileEntityBlockBreaker",
            "sblectric.lightningcraft.tiles.TileEntityLightningBreaker",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityBreaker",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityPhantomBreaker",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityDirectionalBreaker",
            "com.rwtema.extrautils2.tile.TileMine", "com.rwtema.extrautils2.tile.TileUse",
            "li.cil.oc.common.tileentity.RobotProxy",
            "appeng.tile.networking.TileCableBus");
    private static final Collection<String> BLOCK_PLACER_CLASS_NAMES = Arrays.asList(
            "openblocks.common.tileentity.TileEntityBlockPlacer",
            "com.rwtema.extrautils2.tile.TileUse",
            "li.cil.oc.common.tileentity.RobotProxy",
            "appeng.tile.networking.TileCableBus",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityPlacer",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityPhantomPlacer");
    private static final Set<Class<?>> BLOCK_BREAKER_CLASSES = new HashSet<>();
    private static final Set<Class<?>> BLOCK_PLACER_CLASSES = new HashSet<>();
    private static boolean FOUND_BREAKER_CLASSES = false;
    private static boolean FOUND_PLACER_CLASSES = false;

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
        if(!FOUND_BREAKER_CLASSES) {
            findClassesFromNames(BLOCK_BREAKER_CLASS_NAMES,BLOCK_BREAKER_CLASSES);
            FOUND_BREAKER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_BREAKER_CLASSES);
    }

    public static Set<Class<?>> getPlacerTileClasses() {
        if(!FOUND_PLACER_CLASSES) {
            findClassesFromNames(BLOCK_PLACER_CLASS_NAMES,BLOCK_PLACER_CLASSES);
            FOUND_PLACER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_PLACER_CLASSES);
    }

    private static void findClassesFromNames(Collection<String> classNames, Set<Class<?>> classSet) {
        for(String className : classNames) {
            try {
                Class<?> foundClass = Class.forName(className);
                if(TileEntity.class.isAssignableFrom(foundClass)) {
                    classSet.add(foundClass);
                    Constants.LOGGER.info("Registered tile entity class with name `{}` as an automatic block "+
                            "breaker",className);
                } else Constants.LOGGER.error("Tried to register non tile entity class with name {} as an " +
                        "automatic block breaker!",className);
            } catch (ClassNotFoundException ex) {
                Constants.LOGGER.error("Could not locate class with name `{}`",className);
            }
        }
    }
}
