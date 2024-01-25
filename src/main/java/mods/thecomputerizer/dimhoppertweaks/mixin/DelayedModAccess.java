package mods.thecomputerizer.dimhoppertweaks.mixin;

import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.world.TeleporterGaia;
import appeng.items.parts.ItemFacade;
import c4.conarm.client.gui.PreviewPlayer;
import c4.conarm.lib.tinkering.TinkersArmor;
import cofh.thermalexpansion.item.ItemFlorb;
import cofh.thermalexpansion.item.ItemMorb;
import crazypants.enderio.base.item.soulvial.ItemSoulVial;
import crazypants.enderio.base.item.spawner.ItemBrokenSpawner;
import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityGiantChest;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityAutoCrafter;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker.CTPassthrough;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IChunk;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.network.PacketSendKeyPressed;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.huntingdim.item.ItemBiomeChanger;
import net.darkhax.orestages.api.OreTiersAPI;
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
import net.minecraft.util.Tuple;
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
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.tools.common.item.ItemBlockTable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "SameParameterValue"})
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
    public static final List<ItemStack> ADDED_ITEMS = new LinkedList<>();
    private static final Map<Item,Map<Integer,NBTTagCompound>> CACHED_ITEM_YEETS = new HashMap<>();
    private static  final List<Item> NULLED_ITEM_YEET_TAGS = new ArrayList<>();
    public static final AtomicInteger YEET_COUNT = new AtomicInteger(0);

    public static void finalizeYeeting() {
        DHTRef.LOGGER.info("{} redundant JEI entries have been successfully trimmed!",DelayedModAccess.YEET_COUNT.get()/2);
        NULLED_ITEM_YEET_TAGS.clear();
        for(Map.Entry<Item,Map<Integer,NBTTagCompound>> entry : CACHED_ITEM_YEETS.entrySet()) {
            entry.getValue().clear();
            entry.setValue(null);
        }
        CACHED_ITEM_YEETS.clear();
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
                Material mat = CTPassthrough.SPECIALIZED_PARTS.get((IToolPart)item);
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
                    materials.appendTag(new NBTTagString(Material.UNKNOWN.identifier));
                    continue;
                }
                Material mat = CTPassthrough.SPECIALIZED_PARTS.get(part);
                materials.appendTag(new NBTTagString(Objects.nonNull(mat) ? mat.identifier : "constantan"));
            }
            materialTag.setTag("Materials",materials);
            tag.setTag("TinkerData",materialTag);
        });
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

    private static NBTTagCompound replaceYeetedTag(Item item, int meta, Consumer<NBTTagCompound> tagSettings) {
        NBTTagCompound tag = new NBTTagCompound();
        tagSettings.accept(tag);
        CACHED_ITEM_YEETS.putIfAbsent(item,new Int2ObjectLinkedOpenHashMap<>());
        CACHED_ITEM_YEETS.get(item).put(meta,tag);
        return tag;
    }

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

    public static boolean hasGameStage(Entity entity, String stage) {
        return entity instanceof EntityPlayer && GameStageHelper.hasStage((EntityPlayer)entity,stage);
    }
    public static IBlockState getWithOreStage(@Nullable Entity entity, IBlockState original) {
        Tuple<String,IBlockState> stageInfo = OreTiersAPI.getStageInfo(original);
        return Objects.isNull(stageInfo) || hasGameStage(entity,stageInfo.getFirst()) ? original : stageInfo.getSecond();
    }

    public static ItemStack cheese() {
        return new ItemStack(ItemListxlfoodmod.cheese);
    }

    public static void checkForAutoCrafter(TileEntity tile, Collection<String> stages) {
        if(tile instanceof TileEntityAutoCrafter)
            ((IInventoryCrafting)((TileEntityAutoCrafter)tile).crafting).dimhoppertweaks$setStages(stages);
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
                    DHTRef.LOGGER.info("Registered tile entity class with name `{}` as an automatic {}",
                            className,type);
                } else DHTRef.LOGGER.error("Tried to register non tile entity class with name {} as an " +
                        "automatic {}!",className,type);
            } catch (ClassNotFoundException ex) {
                DHTRef.LOGGER.error("Could not locate class with name `{}`",className);
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
        return entity instanceof PreviewPlayer || entity.getEntityData().getBoolean("isFakeEntityForMoBends");
    }

    public static boolean isInFastChunk(TileEntity tile) {
        return isFastChunk(tile.getWorld(),tile.getPos());
    }

    public static boolean isFastChunk(World world, BlockPos pos) {
        return isFastChunk(world.getChunk(pos));
    }

    public static boolean isFastChunk(Chunk chunk) {
        return ((IChunk)chunk).dimhoppertweaks$isFast();
    }

    public static Block getGDKeystoneBlock() {
        return GDBlocks.keystone_block;
    }

    public static Block getGDPortalBlock() {
        return GDBlocks.gaia_portal;
    }

    public static Block getCrateBlock() {
        return InitBlocks.blockGiantChest;
    }

    public static ITeleporter makeGaiaTeleporter(int dim) {
        WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
        return new TeleporterGaia(world,getGDPortalBlock(),getGDKeystoneBlock().getDefaultState());
    }

    private static boolean randDouble(Random rand, double d) {
        return rand.nextDouble()<=d;
    }

    private static boolean randFloat(Random rand, float f) {
        return rand.nextFloat()<=f;
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
        if(state.getBlock() instanceof BlockChest && randFloat(rand,DHTRef.CHEST_REPLACEMENT_CHANCE)) {
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

    @SideOnly(Side.CLIENT)
    public static void sendKeyPress(int type) {
        new PacketSendKeyPressed(type).send();
    }
}
