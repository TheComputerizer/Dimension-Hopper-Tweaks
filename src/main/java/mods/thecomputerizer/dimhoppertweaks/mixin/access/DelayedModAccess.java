package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityAutoCrafter;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import goblinbob.mobends.standard.client.model.armor.MalformedArmorModelException;
import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomBipedArmor;
import goblinbob.mobends.standard.data.BipedEntityData;
import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            "appeng.tile.networking.TileCableBus",
            "org.cyclops.integrateddynamics.core.tileentity.TileMultipartTicking");
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

    public static void checkForAutoCrafter(TileEntity tile, Collection<String> stages) {
        if(tile instanceof TileEntityAutoCrafter)
            ((InventoryCraftingAccess)((TileEntityAutoCrafter)tile).crafting).dimhoppertweaks$setStages(stages);
    }

    public static Set<Class<?>> getBreakerTileClasses() {
        if(!FOUND_BREAKER_CLASSES) {
            findClassesFromNames(BLOCK_BREAKER_CLASS_NAMES,BLOCK_BREAKER_CLASSES,"block breaker");
            FOUND_BREAKER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_BREAKER_CLASSES);
    }

    public static Set<Class<?>> getPlacerTileClasses() {
        if(!FOUND_PLACER_CLASSES) {
            findClassesFromNames(BLOCK_PLACER_CLASS_NAMES,BLOCK_PLACER_CLASSES,"block placer");
            FOUND_PLACER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_PLACER_CLASSES);
    }

    private static void findClassesFromNames(Collection<String> classNames, Set<Class<?>> classSet, String type) {
        for(String className : classNames) {
            try {
                Class<?> foundClass = Class.forName(className);
                if(TileEntity.class.isAssignableFrom(foundClass)) {
                    classSet.add(foundClass);
                    Constants.LOGGER.info("Registered tile entity class with name `{}` as an automatic {}",
                            className,type);
                } else Constants.LOGGER.error("Tried to register non tile entity class with name {} as an " +
                        "automatic {}!",className,type);
            } catch (ClassNotFoundException ex) {
                Constants.LOGGER.error("Could not locate class with name `{}`",className);
            }
        }
    }

    public static double incrementDifficultyWithStageFactor(EntityPlayer player, double original) {
        IStageData data = GameStageHelper.getPlayerData(player);
        if(Objects.nonNull(data)) {
            Collection<String> stages = data.getStages();
            if(stages.contains("bedrockfinal")) original*=20d;
            else if(stages.contains("finalfrontier")) original*=18d;
            else if(stages.contains("deepdown")) original*=16d;
            else if(stages.contains("deepspace")) original*=14d;
            else if(stages.contains("advent")) original*=12d;
            else if(stages.contains("planets")) original*=10d;
            else if(stages.contains("swamp")) original*=8d;
            else if(stages.contains("cavern")) original*=6d;
            else if(stages.contains("labyrinth")) original*=4d;
            else if(stages.contains("overworld")) original*=2d;
            if(stages.contains("shopper")) original*=1.5d;
            if(stages.contains("emc")) original*=1.5d;
        }
        return original;
    }

    public static boolean isFakeEntity(Entity entity) {
        return entity.getEntityData().getBoolean("isFakeEntityForMoBends");
    }

    @SideOnly(Side.CLIENT)
    public static <M extends ModelBase> boolean isInfinityArmorModel(M model) {
        return model instanceof ModelArmorInfinity;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isMoBendsArmorLayer(LayerArmorBase<?> layer) {
        return layer instanceof LayerCustomBipedArmor;
    }

    @SideOnly(Side.CLIENT)
    public static ModelBiped fixOverlay(EntityLivingBase entity, ModelBiped overlay) {
        EntityData<?> entityData = EntityDatabase.instance.get(entity);
        boolean shouldBeMutated = Objects.nonNull(entityData) && entityData instanceof BipedEntityData;
        try {
            return ArmorModelFactory.getArmorModel(overlay,shouldBeMutated);
        } catch(MalformedArmorModelException ex) {
            Constants.LOGGER.error("Could not transform infinity armor :(",ex);
            return overlay;
        }
    }
}
