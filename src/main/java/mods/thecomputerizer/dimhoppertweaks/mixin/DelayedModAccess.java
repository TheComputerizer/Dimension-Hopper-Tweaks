package mods.thecomputerizer.dimhoppertweaks.mixin;

import androsa.gaiadimension.world.TeleporterGaia;
import appeng.items.parts.ItemFacade;
import c4.conarm.client.gui.PreviewPlayer;
import c4.conarm.lib.tinkering.TinkersArmor;
import cofh.thermalexpansion.item.ItemFlorb;
import cofh.thermalexpansion.item.ItemMorb;
import crazypants.enderio.base.item.soulvial.ItemSoulVial;
import crazypants.enderio.base.item.spawner.ItemBrokenSpawner;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityGiantChest;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityAutoCrafter;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.network.PacketSendKeyPressed;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.huntingdim.item.ItemBiomeChanger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import openblocks.common.item.ItemTankBlock;
import org.apache.commons.lang3.StringUtils;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.tools.common.item.ItemBlockTable;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import static androsa.gaiadimension.registry.GDBlocks.gaia_portal;
import static androsa.gaiadimension.registry.GDBlocks.keystone_block;
import static de.ellpeck.actuallyadditions.mod.blocks.InitBlocks.blockGiantChest;
import static mariot7.xlfoodmod.init.ItemListxlfoodmod.cheese;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker.CTPassthrough.SPECIALIZED_PARTS;
import static net.darkhax.dimstages.DimensionStages.DIMENSION_MAP;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;
import static slimeknights.tconstruct.library.materials.Material.UNKNOWN;

@SuppressWarnings({"unused", "SameParameterValue"})
public class DelayedModAccess {

    public static final List<ItemStack> ADDED_ITEMS = new LinkedList<>();
    private static final Set<Class<?>> BLOCK_BREAKER_CLASSES = new HashSet<>();
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
    private static final Set<Class<?>> BLOCK_PLACER_CLASSES = new HashSet<>();
    private static final Collection<String> BLOCK_PLACER_CLASS_NAMES = Arrays.asList(
            "openblocks.common.tileentity.TileEntityBlockPlacer",
            "com.rwtema.extrautils2.tile.TileUse",
            "li.cil.oc.common.tileentity.RobotProxy",
            "appeng.tile.networking.TileCableBus",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityPlacer",
            "de.ellpeck.actuallyadditions.mod.tile.TileEntityPhantomPlacer");
    private static final Map<Item,Map<Integer,NBTTagCompound>> CACHED_ITEM_YEETS = new HashMap<>();
    private static final List<Item> NULLED_ITEM_YEET_TAGS = new ArrayList<>();
    public static final AtomicInteger YEET_COUNT = new AtomicInteger(0);
    private static boolean FOUND_BREAKER_CLASSES = false;
    private static boolean FOUND_PLACER_CLASSES = false;

    public static void callInaccessibleMethod(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            if(!method.isAccessible()) method.setAccessible(true);
            method.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            DHTRef.LOGGER.error("Failed to invoke inaccessible method `{}` from class `{}`",methodName,
                    clazz.getName(),ex);
        }
    }
    
    public static boolean canTravelToDimension(Entity entity, int dimension) {
        if(!(entity instanceof EntityPlayer)) return true;
        int entityDim = entity.dimension;
        return hasStageForDimension(entity,dimension) &&
               (dimensionHatesDoors(dimension) || dimensionHatesDoors(entityDim) && dimension!=entityDim);
    }

    public static ItemStack cheese() {
        return new ItemStack(cheese);
    }

    public static void checkForAutoCrafter(TileEntity tile, Collection<String> stages) {
        if(tile instanceof TileEntityAutoCrafter)
            ((IInventoryCrafting)((TileEntityAutoCrafter)tile).crafting).dimhoppertweaks$setStages(stages);
    }

    private static boolean checkTagRemovals(Item item, ResourceLocation res) {
        if(item instanceof ItemMonsterPlacer || item instanceof ItemTankBlock) {
            return true;
        }
        String name = res.toString();
        String mod = res.getNamespace();
        String path = res.getPath();
        return name.equals("rftools:syringe") || name.equals("tconstruct:clay_cast") || path.contains("bucket") ||
                (mod.equals("forestry") && !path.contains("queen") || mod.equals("gendustry"));
    }

    public static void checkYeet(ItemStack stack) {
        Item item = stack.getItem();
        if(CACHED_ITEM_YEETS.containsKey(item)) {
            Map<Integer,NBTTagCompound> metaMap = CACHED_ITEM_YEETS.get(item);
            if(metaMap.containsKey(stack.getMetadata())) {
                stack.setTagCompound(metaMap.get(stack.getMetadata()));
                YEET_COUNT.incrementAndGet();
                return;
            }
        }
        ResourceLocation res = item.getRegistryName();
        if(Objects.isNull(res)) return;
        int meta = stack.getMetadata();
        if(checkTagRemovals(item,res)) {
            stack.setTagCompound(null);
            if(NULLED_ITEM_YEET_TAGS.contains(item)) YEET_COUNT.incrementAndGet();
            else NULLED_ITEM_YEET_TAGS.add(item);
            return;
        }
        if(item instanceof ItemEnchantedBook) {
            stack.setTagCompound(replaceYeetedTag(item,meta,tag -> {
                NBTTagCompound enchTag = new NBTTagCompound();
                enchTag.setShort("lvl",(short)1);
                enchTag.setInteger("id",0);
                tag.setTag("StoredEnchantments",enchTag);
            }));
            return;
        }
        if(item instanceof ItemFacade) {
            stack.setTagCompound(replaceYeetedTag(item,stack.getMetadata(),tag -> {
                tag.setInteger("damage", 0);
                tag.setString("item","minecraft:stone");
            }));
            return;
        }
        if(item instanceof IToolPart) {
            stack.setTagCompound(replaceYeetedTag(item,meta,tag -> {
                Material mat = SPECIALIZED_PARTS.get((IToolPart)item);
                tag.setString("Material",Objects.nonNull(mat) ? mat.identifier : "constantan");
            }));
            return;
        }
        if(item instanceof TinkersItem) {
            stack.setTagCompound(getTinkerToolTag(item, meta, ((TinkersItem) item).getRequiredComponents()));
            return;
        }
        if(item instanceof TinkersArmor) {
            stack.setTagCompound(getTinkerToolTag(item, meta, ((TinkersArmor) item).getRequiredComponents()));
            return;
        }
        if(item instanceof ItemBlockTable) {
            if(meta==1 || meta==2 || res.getPath().endsWith("block")) {
                String block = meta == 1 ? "minecraft:planks" : "minecraft:log";
                if (res.getPath().endsWith("forge")) block = "minecraft:iron_block";
                replaceYeetedTag(item, meta, replaceYeetedTinkerTable(block));
            }
            return;
        }
        if(item instanceof ItemBiomeChanger) {
            stack.setTagCompound(replaceYeetedTag(item,meta,tag -> tag.setInteger("HeldBiome",0)));
            return;
        }
        String mod = res.getNamespace();
        if(mod.equals("enderio")) {
            if((item instanceof ItemSoulVial && stack.getMetadata()==1) || item instanceof ItemBrokenSpawner)
                stack.setTagCompound(replaceYeetedTag(item,stack.getMetadata(),tag -> tag.setString("entityId","minecraft:enderman")));
            return;
        }
        if(mod.startsWith("thermal")) {
            if(item instanceof ItemMorb)
                stack.setTagCompound(replaceYeetedTag(item,stack.getMetadata(),tag -> {
                    tag.setByte("Generic",(byte)1);
                    tag.setString("id","minecraft:enderman");
                }));
            else if(item instanceof ItemFlorb)
                stack.setTagCompound(replaceYeetedTag(item,stack.getMetadata(),tag -> {
                    tag.setString("FluidName","water");
                    tag.setInteger("Amount",1000);
                }));
            return;
        }
        String name = res.toString();
        if(name.equals("huntingdim:frame")) {
            stack.setTagCompound(replaceYeetedTag(item,meta,tag -> {
                NBTTagCompound blockTag = new NBTTagCompound();
                blockTag.setString("id","minecraft:log");
                blockTag.setInteger("Count",1);
                blockTag.setShort("Damage",(short)0);
                tag.setTag("BaseBlock",blockTag);
            }));
            return;
        }
        if(name.equals("aeadditions:pattern.fluid")) {
            stack.setTagCompound(replaceYeetedTag(item,meta,tag -> {
                NBTTagCompound fluidTag = new NBTTagCompound();
                fluidTag.setString("FluidName","water");
                fluidTag.setInteger("Amount",1000);
                tag.setTag("Fluid",fluidTag);
            }));
        }
    }
    
    public static boolean dimensionHatesDoors(int dimension) {
        return dimension==20 || dimension==7;
    }

    public static void finalizeYeeting() {
        LOGGER.info("{} redundant JEI entries have been successfully trimmed!",YEET_COUNT.get()/2);
        NULLED_ITEM_YEET_TAGS.clear();
        for(Map.Entry<Item,Map<Integer,NBTTagCompound>> entry : CACHED_ITEM_YEETS.entrySet()) {
            entry.getValue().clear();
            entry.setValue(null);
        }
        CACHED_ITEM_YEETS.clear();
    }

    private static @Nullable Class<?> findClass(String className) {
        try {
            return Class.forName(className);
        } catch(ClassNotFoundException ex) {
            LOGGER.error("Could not locate class with name `{}`",className);
        }
        return null;
    }

    private static void findClassesFromNames(Collection<String> classNames, Set<Class<?>> classSet, String type) {
        for(String className : classNames) {
            Class<?> foundClass = findClass(className);
            if(Objects.nonNull(foundClass) && TileEntity.class.isAssignableFrom(foundClass)) {
                classSet.add(foundClass);
                LOGGER.info("Registered tile entity class with name `{}` as an automatic {}",
                        className,type);
            } else LOGGER.error("Tried to register non tile entity class with name {} as an " +
                    "automatic {}!",className,type);
        }
    }

    public static Set<Class<?>> getBreakerTileClasses() {
        if(!FOUND_BREAKER_CLASSES) {
            findClassesFromNames(BLOCK_BREAKER_CLASS_NAMES,BLOCK_BREAKER_CLASSES,"block breaker");
            FOUND_BREAKER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_BREAKER_CLASSES);
    }

    public static Block getCrateBlock() {
        return blockGiantChest;
    }
    
    public static double getDifficultyMultiplier(EntityPlayer player) {
        double original = 1d;
        IStageData data = GameStageHelper.getPlayerData(player);
        if(Objects.nonNull(data)) {
            Collection<String> stages = data.getStages();
            if(stages.contains("hardcore")) return 5000d;
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

    public static Collection<String> getGameStages(EntityPlayer player) {
        if(Objects.isNull(player)) return new ArrayList<>();
        IStageData data = GameStageHelper.getPlayerData(player);
        return Objects.isNull(data) ? new ArrayList<>() : data.getStages();
    }

    public static Block getGDKeystoneBlock() {
        return keystone_block;
    }

    public static Block getGDPortalBlock() {
        return gaia_portal;
    }
    
    public static double getMaxDifficulty(EntityPlayer player) {
        return getDifficultyMultiplier(player)*50d;
    }
    
    public static double getMinDifficulty(EntityPlayer player) {
        return getDifficultyMultiplier(player)*25d;
    }

    public static Set<Class<?>> getPlacerTileClasses() {
        if(!FOUND_PLACER_CLASSES) {
            findClassesFromNames(BLOCK_PLACER_CLASS_NAMES,BLOCK_PLACER_CLASSES,"block placer");
            FOUND_PLACER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_PLACER_CLASSES);
    }
    
    @SuppressWarnings("DataFlowIssue")
    public static ItemStack getStack(String name, int count) {
        ResourceLocation res = new ResourceLocation(name);
        return ITEMS.containsKey(res) ? new ItemStack(ITEMS.getValue(res),count) : EMPTY;
    }
    
    public static String getStageForDimension(int dimension) {
        return DIMENSION_MAP.get(dimension);
    }

    private static NBTTagCompound getTinkerToolTag(Item item, int meta, List<PartMaterialType> components) {
        return replaceYeetedTag(item,meta,tag -> {
            NBTTagCompound materialTag = new NBTTagCompound();
            NBTTagList materials = new NBTTagList();
            for(PartMaterialType type : components) {
                IToolPart part = null;
                for(IToolPart maybe : type.getPossibleParts()) {
                    part = maybe;
                    break;
                }
                if(Objects.isNull(part)) {
                    materials.appendTag(new NBTTagString(UNKNOWN.identifier));
                    continue;
                }
                Material mat = SPECIALIZED_PARTS.get(part);
                materials.appendTag(new NBTTagString(Objects.nonNull(mat) ? mat.identifier : "constantan"));
            }
            materialTag.setTag("Materials",materials);
            tag.setTag("TinkerData",materialTag);
        });
    }

    public static boolean hasGameStage(Entity entity, String stage) {
        return entity instanceof EntityPlayer && GameStageHelper.hasStage((EntityPlayer)entity,stage);
    }
    
    public static boolean hasStageForDimension(Entity entity, int dimension) {
        if(!(entity instanceof EntityPlayer)) return true;
        String stage = getStageForDimension(dimension);
        return StringUtils.isEmpty(stage) || hasGameStage(entity,stage);
    }

    public static double incrementDifficultyWithStageFactor(EntityPlayer player, double original) {
        return original*getDifficultyMultiplier(player);
    }

    public static @Nullable Object instantiateInaccessibleClass(
            String className, Function<Class<?>,Constructor<?>> constructorFinder, Object ... args) {
        Class<?> clazz = findClass(className);
        Constructor<?> constructor = Objects.nonNull(clazz) ? constructorFinder.apply(clazz) : null;
        if(Objects.nonNull(constructor)) {
            try {
                if(!constructor.isAccessible()) constructor.setAccessible(true);
                return constructor.newInstance(args);
            } catch(InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                LOGGER.error("Failed to instantiate inaccessible class {} using arge `{}`",
                        className,args,ex);
            }
        } else LOGGER.error("The constructor for class {} seems to be null",className);
        return null;
    }

    public static boolean isFakeEntity(Entity entity) {
        return entity instanceof PreviewPlayer || entity.getEntityData().getBoolean("isFakeEntityForMoBends");
    }

    public static boolean isInFastChunk(TileEntity tile) {
        return isFastChunk(tile.getWorld(),tile.getPos());
    }

    public static boolean isFastChunk(World world, BlockPos pos) {
        return isFastChunk(world.getChunk(pos));
    }

    public static boolean isFastChunk(Chunk chunk) {
        return ExtraChunkData.isChunkFast(chunk);
    }

    public static ITeleporter makeGaiaTeleporter(int dim) {
        WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
        return new TeleporterGaia(world,getGDPortalBlock(),getGDKeystoneBlock().getDefaultState());
    }

    public static void replaceAfterStructureGeneration(Chunk chunk) {
        World world = chunk.getWorld();
        replaceBlocksInDimension(world,chunk);
        replaceTiles(world,chunk);
    }

    public static void replaceBlocksInDimension(World world, Chunk chunk) {
        int dimension = world.provider.getDimension();
    }

    public static void replaceChest(Random rand, TileEntityChest tile, IBlockState state) {
        if(state.getBlock() instanceof BlockChest && DHTConfigHelper.shouldReplaceChest(rand)) {
            Map<ItemStack,Integer> slotMap = new HashMap<>();
            for(int i=0; i<tile.getSizeInventory(); i++) {
                ItemStack stack = tile.getStackInSlot(i);
                if(!stack.isEmpty()) {
                    slotMap.put(stack.copy(),i);
                    stack.setCount(0);
                }
            }
            replaceContainer(tile.getWorld(),tile.getPos(),getCrateBlock().getDefaultState(),(current) -> {
                TileEntityGiantChest crate = (TileEntityGiantChest) current;
                if(slotMap.isEmpty()) {
                    crate.lootTable = tile.getLootTable();
                    return;
                }
                IItemHandler handler = crate.getItemHandler(null);
                double ratio = ((double) handler.getSlots()) / 27d;
                for (Map.Entry<ItemStack, Integer> slotEntry : slotMap.entrySet()) {
                    int crateSlot = Math.min((int)(((double)slotEntry.getValue())*ratio),handler.getSlots()-1);
                    handler.insertItem(crateSlot, slotEntry.getKey(), false);
                }
            });
        }
    }

    public static void replaceContainer(
            World world, BlockPos pos,IBlockState state, @Nullable Consumer<TileEntity> lootTableHandler) {
        world.setBlockState(pos,state);
        if(state.getBlock().hasTileEntity(state) && Objects.nonNull(lootTableHandler))
            lootTableHandler.accept(world.getTileEntity(pos));
    }

    public static void replaceTiles(World world, Chunk chunk) {
        for(Map.Entry<BlockPos,TileEntity> tileEntry : chunk.getTileEntityMap().entrySet()) {
            BlockPos pos = tileEntry.getKey();
            IBlockState state = chunk.getBlockState(pos);
            TileEntity tile = tileEntry.getValue();
            if(tile instanceof TileEntityChest) replaceChest(world.rand,(TileEntityChest)tile,state);
            else if(tile instanceof TileEntityFurnace || tile instanceof TileEntityEnchantmentTable ||
                    tile instanceof TileEntityBrewingStand)
                replaceWithAir(world,pos);
        }
    }

    public static void replaceWithAir(World world, BlockPos pos) {
        world.setBlockState(pos,Blocks.AIR.getDefaultState());
    }

    private static NBTTagCompound replaceYeetedTag(Item item, int meta, Consumer<NBTTagCompound> tagSettings) {
        NBTTagCompound tag = new NBTTagCompound();
        tagSettings.accept(tag);
        CACHED_ITEM_YEETS.putIfAbsent(item,new Int2ObjectLinkedOpenHashMap<>());
        CACHED_ITEM_YEETS.get(item).put(meta,tag);
        return tag;
    }

    private static Consumer<NBTTagCompound> replaceYeetedTinkerTable(String block) {
        return tag -> {
            NBTTagCompound textureTag = new NBTTagCompound();
            textureTag.setString("id",block);
            textureTag.setInteger("Count",1);
            textureTag.setShort("Damage",(short)0);
            tag.setTag("textureBlock",textureTag);
        };
    }

    public static void setGameStages(EntityPlayer player, Collection<String> stages) {
        for(String stage : stages) setGameStage(player,stage);
    }

    public static void setGameStage(EntityPlayer player, String stage) {
        if(Objects.nonNull(player)) GameStageHelper.addStage(player,stage);
    }

    @SideOnly(Side.CLIENT)
    public static void sendKeyPress(int type) {
        new PacketSendKeyPressed(type).send();
    }
}
