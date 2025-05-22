package mods.thecomputerizer.dimhoppertweaks.mixin;

import androsa.gaiadimension.world.TeleporterGaia;
import appeng.api.AEApi;
import appeng.items.parts.ItemFacade;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import c4.conarm.client.gui.PreviewPlayer;
import c4.conarm.lib.tinkering.TinkersArmor;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import cofh.thermalexpansion.item.ItemFlorb;
import cofh.thermalexpansion.item.ItemMorb;
import crazypants.enderio.base.item.soulvial.ItemSoulVial;
import crazypants.enderio.base.item.spawner.ItemBrokenSpawner;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityGiantChest;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityAutoCrafter;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.common.events.TickEvents;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IContainer;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import mods.thecomputerizer.dimhoppertweaks.network.DHTNetwork;
import mods.thecomputerizer.dimhoppertweaks.network.PacketSendKeyPressed;
import mods.thecomputerizer.theimpossiblelibrary.api.text.TextHelper;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.FakePlayerData;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.huntingdim.item.ItemBiomeChanger;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import openblocks.common.item.ItemTankBlock;
import org.apache.commons.lang3.StringUtils;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.tools.common.item.ItemBlockTable;

import javax.annotation.Nonnull;
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
import static bedrockcraft.tool.BedrockTool.toolMaterial;
import static de.ellpeck.actuallyadditions.mod.blocks.InitBlocks.blockGiantChest;
import static mariot7.xlfoodmod.init.ItemListxlfoodmod.cheese;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker.CTPassthrough.SPECIALIZED_PARTS;
import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.DIFFICULT_GAMBLE;
import static net.darkhax.dimstages.DimensionStages.DIMENSION_MAP;
import static net.minecraft.init.Blocks.AIR;
import static net.minecraft.init.Blocks.WATER;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static org.burningwave.core.assembler.StaticComponentContainer.Fields;
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
            "com.rwtema.extrautils2.tile.TileMine",
            "com.rwtema.extrautils2.tile.TileUse",
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
    public static final Map<Ingredient,Set<String>> INGREDIENT_STAGES = new HashMap<>();

    public static void appendStageData(NBTTagCompound tag, String type, Collection<String> stages) {
        if(Objects.nonNull(tag) && Objects.nonNull(stages) && !stages.isEmpty()) {
            NBTTagList stageList = new NBTTagList();
            for(String stage : stages) stageList.appendTag(new NBTTagString(stage));
            tag.setTag("GameStage"+(Objects.nonNull(type) ? type : "")+"Data",stageList);
        }
    }

    public static void callInaccessibleMethod(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            if(!method.isAccessible()) method.setAccessible(true);
            method.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            LOGGER.error("Failed to invoke inaccessible method `{}` from class `{}`",methodName,
                    clazz.getName(),ex);
        }
    }
    
    public static boolean canCraft(IRecipe recipe, Container container) {
        return canCraft(recipe,container,false);
    }
    
    /**
     * Check stages of input items to ensure they can be used in crafting
     */
    public static boolean canCraft(IRecipe recipe, Container container, boolean debug) {
        Collection<String> stages = getContainerStages(container);
        if(debug) LOGGER.info("Checking container stages {}",stages);
        for(ItemStack stack : container.getInventory())
            if(!hasStageForItem(stages,stack,debug)) return false;
        return true;
    }
    
    public static boolean canCraft(IRecipe recipe, InventoryCrafting inventory) {
        return canCraft(recipe,inventory,false);
    }
    
    public static boolean canCraft(IRecipe recipe, InventoryCrafting inventory, boolean debug) {
        Collection<String> stages = getInventoryStages(inventory);
        if(debug) LOGGER.info("Checking inventory stages {}",stages);
        for(int i=0;i<inventory.getSizeInventory();i++) {
            if(debug) LOGGER.info("Checking slot {}",i);
            if(!hasStageForItem(stages,inventory.getStackInSlot(i),debug)) return false;
        }
        return true;
    }
    
    public static boolean canTravelToDimension(Entity entity, int dimension) {
        if(!(entity instanceof EntityPlayer)) return true;
        if(dimensionHatesDoors(dimension)) return false;
        return dimension==entity.dimension || hasStageForDimension(entity,dimension);
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
    
    public static Collection<String> getContainerStages(@Nullable Container container) {
        return Objects.isNull(container) ? Collections.emptyList() :
                ((IContainer)container).dimhoppertweaks$getStages();
    }
    
    public static ItemStack getCraftingResult(IRecipe recipe, InventoryCrafting inventory, ItemStack result) {
        return getCraftingResult(recipe,inventory,result,false);
    }
    
    public static ItemStack getCraftingResult(IRecipe recipe, InventoryCrafting inventory, ItemStack result, boolean debug) {
        ItemStack ret = result!=EMPTY && canCraft(recipe,inventory,debug) ? result : EMPTY;
        if(debug) LOGGER.info("Returning = {} | Original = {}",ret,result);
        return ret;
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
    
    public static Collection<String> getEntityStages(Entity entity) {
        return entity instanceof EntityPlayer ? getPlayerStages((EntityPlayer)entity) : Collections.emptyList();
    }

    public static Block getGDKeystoneBlock() {
        return keystone_block;
    }

    public static Block getGDPortalBlock() {
        return gaia_portal;
    }
    
    public static Collection<String> getInventoryStages(@Nullable InventoryCrafting inventory) {
        return Objects.isNull(inventory) ? Collections.emptyList() :
                ((IInventoryCrafting)inventory).dimhoppertweaks$getStages();
    }
    
    public static double getMaxDifficulty(EntityPlayer player) {
        PlayerData data = PlayerDataHandler.get(player);
        if(Objects.nonNull(data) && data.getSkillInfo(SkillWrapper.getSkill("void")).isUnlocked(DIFFICULT_GAMBLE))
            return 5000d;
        return MathHelper.clamp(getDifficultyMultiplier(player)*50d,0d,5000d);
    }
    
    public static double getMinDifficulty(EntityPlayer player) {
        return MathHelper.clamp(getDifficultyMultiplier(player)*25d,0d,5000d);
    }
    
    public static Collection<String> getOrInitFakePlayerStages(@Nullable FakePlayer faker, String ... initialStages) {
        return Objects.isNull(faker) ? handleNullFaker() : getOrInitFakePlayerStages(faker.getName(),initialStages);
    }
    
    private static Collection<String> getOrInitFakePlayerStages(String name, String ... initialStages) {
        IStageData data = GameStageSaveHandler.hasFakePlayer(name) ? GameStageSaveHandler.getFakeData(name) :
                initFakePlayerData(name,initialStages);
        return Objects.isNull(data) ? Collections.emptyList() : data.getStages();
    }
    
    public static Collection<String> getOrInitFakePlayerStages(@Nullable FakePlayer faker, Collection<String> initialStages) {
        if(Objects.isNull(faker)) handleNullFaker();
        return Objects.isNull(faker) ? handleNullFaker() : getOrInitFakePlayerStages(faker.getName(),initialStages);
    }
    
    private static Collection<String> getOrInitFakePlayerStages(String name, Collection<String> initialStages) {
        IStageData data = GameStageSaveHandler.hasFakePlayer(name) ? GameStageSaveHandler.getFakeData(name) :
                initFakePlayerData(name,initialStages);
        return Objects.isNull(data) ? Collections.emptyList() : data.getStages();
    }

    public static Set<Class<?>> getPlacerTileClasses() {
        if(!FOUND_PLACER_CLASSES) {
            findClassesFromNames(BLOCK_PLACER_CLASS_NAMES,BLOCK_PLACER_CLASSES,"block placer");
            FOUND_PLACER_CLASSES = true;
        }
        return Collections.unmodifiableSet(BLOCK_PLACER_CLASSES);
    }
    
    public static Collection<String> getPlayerStages(@Nullable EntityPlayer player) {
        if(Objects.isNull(player)) return new ArrayList<>();
        IStageData data = GameStageHelper.getPlayerData(player);
        return Objects.isNull(data) ? new ArrayList<>() : data.getStages();
    }
    
    @SuppressWarnings("DataFlowIssue")
    public static ItemStack getStack(String name, int count) {
        ResourceLocation res = new ResourceLocation(name);
        return ITEMS.containsKey(res) ? new ItemStack(ITEMS.getValue(res),count) : EMPTY;
    }
    
    public static String getStageForDimension(int dimension) {
        return DIMENSION_MAP.get(dimension);
    }
    
    public static Collection<String> getTileStages(@Nullable TileEntity tile) {
        if(tile instanceof TileEntityMultiblockPart<?>) tile = ((TileEntityMultiblockPart<?>)tile).master();
        return Objects.isNull(tile) ? Collections.emptyList() : ((ITileEntity)tile).dimhoppertweaks$getStages();
    }
    
    public static Collection<String> getTileStages(World world, BlockPos pos) {
        return Objects.isNull(world) || Objects.isNull(pos) ? Collections.emptyList() :
                getTileStages(world.getTileEntity(pos));
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
    
    private static Collection<String> handleNullFaker() {
        LOGGER.error("Tried to call getOrInitFakePlayerStages with null FakePlayer instance!");
        return Collections.emptyList();
    }
    
    public static boolean hasGameStage(Collection<String> stages, String stage) {
        return TextHelper.isBlank(stage) || (Objects.nonNull(stages) && stages.contains(stage));
    }
    
    public static boolean hasGameStage(Container container, String stage) {
        return hasGameStage(getContainerStages(container),stage);
    }

    public static boolean hasGameStage(Entity entity, String stage) {
        return hasGameStage(getEntityStages(entity),stage);
    }
    
    public static boolean hasGameStage(InventoryCrafting inventory, String stage) {
        return hasGameStage(getInventoryStages(inventory),stage);
    }
    
    public static boolean hasGameStage(TileEntity tile, String stage) {
        return hasGameStage(getTileStages(tile),stage);
    }
    
    public static boolean hasGameStage(World world, BlockPos pos, String stage) {
        return hasGameStage(getTileStages(world,pos),stage);
    }
    
    public static boolean hasStageForDimension(Entity entity, int dimension) {
        if(!(entity instanceof EntityPlayer)) return true;
        String stage = getStageForDimension(dimension);
        return StringUtils.isEmpty(stage) || hasGameStage(entity,stage);
    }
    
    /**
     * This is unfortunately a pretty slow check, but hopefully the cache will offset that a bit
     */
    public static boolean hasStageForIngredient(Collection<String> stages, Ingredient ingredient) {
        if(INGREDIENT_STAGES.containsKey(ingredient)) return hasStageForIngredientCache(stages,ingredient);
        Set<String> anyStages = new HashSet<>();
        LOGGER.info("Caching stages for ingredient {}",ingredient);
        for(ItemStack stack : ingredient.getMatchingStacks()) {
            String stage = ItemStages.getStage(stack);
            LOGGER.info("Matching stack {} has stage {}",stack,stage);
            if(Objects.nonNull(stage)) anyStages.add(stage);
        }
        anyStages = anyStages.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(anyStages);
        INGREDIENT_STAGES.put(ingredient,anyStages);
        LOGGER.info("Cached stages for ingredient {} as {}",ingredient,anyStages);
        return hasStageForIngredientCache(stages,ingredient);
    }
    
    /**
     * Assumes the cache as already been checked & calculated if necessary
     */
    private static boolean hasStageForIngredientCache(Collection<String> stages, Ingredient ingredient) {
        Set<String> anyStages = INGREDIENT_STAGES.get(ingredient);
        if(anyStages.isEmpty()) return true;
        for(String stage : anyStages)
            if(stages.contains(stage)) return true;
        return false;
    }

    public static boolean hasStageForItem(EntityPlayer player, ItemStack stack) {
        return hasStageForItem(getPlayerStages(player),stack);
    }
    
    public static boolean hasStageForItem(Collection<String> stages, ItemStack stack) {
        return hasStageForItem(stages,stack,false);
    }
    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasStageForItem(Collection<String> stages, ItemStack stack, boolean debug) {
        if(stack==EMPTY) return true;
        String stage = ItemStages.getStage(stack);
        if(debug) LOGGER.info("ItemStack {} has stage {}",stack,stage);
        boolean ret = Objects.isNull(stage) || stages.contains(stage);
        if(debug) LOGGER.info("Returning {}",ret);
        return ret;
    }

    public static double incrementDifficultyWithStageFactor(EntityPlayer player, double original) {
        return original*getDifficultyMultiplier(player);
    }
    
    public static void inheritContainerStages(EntityPlayer player) {
        setContainerStages(player.openContainer,getEntityStages(player));
    }
    
    public static void inheritContainerStages(EntityPlayer player, Container container) {
        setContainerStages(container,getEntityStages(player));
    }
    
    public static void inheritContainerStages(EntityPlayer player, Container container, boolean clear) {
        setContainerStages(container,getPlayerStages(player),clear);
    }
    
    public static void inheritInventoryStages(EntityPlayer player, InventoryCrafting inventory) {
        setInventoryStages(inventory,getEntityStages(player));
    }
    
    public static void inheritInventoryStages(EntityPlayer player, InventoryCrafting inventory, boolean clear) {
        setInventoryStages(inventory,getEntityStages(player),clear);
    }
    
    public static void inheritInventoryStages(World world, BlockPos pos, InventoryCrafting inventory) {
        setInventoryStages(inventory,getTileStages(world,pos));
    }
    
    public static void inheritInventoryStages(World world, BlockPos pos, InventoryCrafting inventory, boolean clear) {
        setInventoryStages(inventory,getTileStages(world,pos),clear);
    }
    
    public static void inheritInventoryStages(@Nullable TileEntity tile, InventoryCrafting inventory) {
        setInventoryStages(inventory,getTileStages(tile));
    }
    
    public static void inheritInventoryStages(@Nullable TileEntity tile, InventoryCrafting inventory, boolean clear) {
        setInventoryStages(inventory,getTileStages(tile),clear);
    }

    public static <I extends InventoryCrafting> I inheritInventoryStagesAndReturn(EntityPlayer player, I inventory) {
        return inheritInventoryStagesAndReturn(getPlayerStages(player),inventory);
    }

    public static <I extends InventoryCrafting> I inheritInventoryStagesAndReturn(InventoryCrafting source, I inventory) {
        return inheritInventoryStagesAndReturn(getInventoryStages(source),inventory);
    }

    public static <I extends InventoryCrafting> I inheritInventoryStagesAndReturn(InventoryPlayer player,
            I inventory) {
        return inheritInventoryStagesAndReturn(getPlayerStages(player.player),inventory);
    }

    public static <I extends InventoryCrafting> I inheritInventoryStagesAndReturn(ItemStack stack, I inventory) {
        return inheritInventoryStagesAndReturn(retrieveStageData(stack.getTagCompound(),"ItemStack"),inventory);
    }

    public static <I extends InventoryCrafting> I inheritInventoryStagesAndReturn(TileEntity tile, I inventory) {
        return inheritInventoryStagesAndReturn(getTileStages(tile),inventory);
    }

    public static <I extends InventoryCrafting> I inheritInventoryStagesAndReturn(Collection<String> stages,
            I inventory) {
        setInventoryStages(inventory,stages);
        return inventory;
    }
    
    public static void inheritPlayerStages(EntityPlayer player, TileEntity tile) {
        setTileStages(tile,getPlayerStages(player));
    }
    
    public static void inheritTileStages(@Nullable FakePlayer player) {
        if(Objects.nonNull(player)) setFakePlayerStages(player, getTileStages(player.getEntityWorld(), player.getPosition()));
    }
    
    public static void inheritTileStages(FakePlayer player, World world) {
        inheritTileStages(player,world,player.getPosition());
    }
    
    public static void inheritTileStages(FakePlayer player, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(Objects.nonNull(tile)) {
            Collection<String> stages = ((ITileEntity)tile).dimhoppertweaks$getStages();
            if(!stages.isEmpty()) setFakePlayerStages(player, stages);
        }
    }
    
    private static FakePlayerData initFakePlayerData(String name, String ... initialStages) {
        return initFakePlayerData(name,Arrays.asList(initialStages));
    }
    
    private static FakePlayerData initFakePlayerData(String name, Collection<String> initialStages) {
        FakePlayerData data = new FakePlayerData(name,new HashSet<>(initialStages));
        GameStageSaveHandler.addFakePlayer(data);
        return data;
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
                LOGGER.error("Failed to instantiate inaccessible class {} using args `{}`",
                        className,args,ex);
            }
        } else LOGGER.error("The constructor for class {} seems to be null",className);
        return null;
    }

    public static boolean isBedrockMaterial(ToolMaterial material) {
        return material==toolMaterial;
    }

    public static boolean isEncodedPattern(ItemStack stack) {
        return AEApi.instance().definitions().items().encodedPattern().isSameAs(stack);
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
    
    public static boolean isInfernalDistracted(Entity entity) {
        return entity instanceof EntityLivingBase && TickEvents.isInfernalDistractedFor((EntityLivingBase)entity);
    }

    public static ITeleporter makeGaiaTeleporter(int dim) {
        WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
        return new TeleporterGaia(world,getGDPortalBlock(),getGDKeystoneBlock().getDefaultState());
    }
    
    public static void onWorldJoined() {
        INGREDIENT_STAGES.clear();
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
                double ratio = ((double)handler.getSlots())/27d;
                for(Map.Entry<ItemStack, Integer> slotEntry : slotMap.entrySet()) {
                    int crateSlot = Math.min((int)(((double)slotEntry.getValue())*ratio),handler.getSlots()-1);
                    handler.insertItem(crateSlot,slotEntry.getKey(),false);
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
        world.setBlockState(pos,AIR.getDefaultState());
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

    public static Collection<String> retrieveStageData(NBTTagCompound tag, String type) {
        if(Objects.isNull(tag)) return Collections.emptyList();
        String listName = "GameStage"+(Objects.nonNull(type) ? type : "")+"Data";
        if(!tag.hasKey(listName)) return Collections.emptyList();
        try {
            Collection<String> stages = new HashSet<>();
            for(NBTBase based : tag.getTagList(listName,8))
                if(based instanceof NBTTagString) stages.add(((NBTTagString)based).getString());
            return stages;
        } catch(Exception ex) {
            LOGGER.error("Failed to retrieve stage data from {}!",listName,ex);
            return Collections.emptyList();
        }
    }
    
    public static void setContainerStages(@Nullable Container container, @Nullable Collection<String> stages) {
        setContainerStages(container,stages,true);
    }
    
    public static void setContainerStages(@Nullable Container container, @Nullable Collection<String> stages,
            boolean clear) {
        if(Objects.nonNull(container) && Objects.nonNull(stages) && !stages.isEmpty())
            ((IContainer)container).dimhoppertweaks$setStages(stages,clear);
    }
    
    public static void setCrafterStages(@Nonnull CrafterBaseTE crafter, @Nullable Collection<String> stages) {
        if(Objects.nonNull(stages) && !stages.isEmpty()) {
            ((ITileEntity)crafter).dimhoppertweaks$setStages(stages);
            DelayedModAccess.setInventoryStages(Fields.getDirect(crafter,"workInventory"),stages);
            for(int i=0;i<crafter.getSupportedRecipes();i++)
                DelayedModAccess.inheritInventoryStages(crafter,crafter.getRecipe(i).getInventory());
        }
    }

    public static void setEntityStages(EntityPlayer player, Collection<String> stages) {
        if(player instanceof FakePlayer) setFakePlayerStages((FakePlayer)player, stages);
        else if(Objects.nonNull(stages)) for(String stage : stages) setEntityStage(player, stage);
    }
    
    private static void setFakePlayerStages(@Nullable FakePlayer faker, Collection<String> stages) {
        Collection<String> fakerStages = getOrInitFakePlayerStages(faker);
        try {
            fakerStages.clear();
            fakerStages.addAll(stages);
        } catch(UnsupportedOperationException ex) {
            LOGGER.error("Failed to set fake player stages for {} to {}",faker,stages,ex);
        }
    }

    public static void setEntityStage(Entity entity, String stage) {
        if(entity instanceof EntityPlayer) setPlayerStage((EntityPlayer)entity,stage);
    }
    
    public static void setPlayerStage(EntityPlayer player, String stage) {
        if(Objects.isNull(player)) return;
        if(player instanceof FakePlayer) setFakePlayerStages((FakePlayer)player,Collections.singletonList(stage));
        else GameStageHelper.addStage(player,stage);
    }
    
    public static void setInventoryStages(@Nullable InventoryCrafting inventory, @Nullable Collection<String> stages) {
        setInventoryStages(inventory,stages,true);
    }
    
    public static void setInventoryStages(@Nullable InventoryCrafting inventory, @Nullable Collection<String> stages,
            boolean clear) {
        if(Objects.nonNull(inventory) && Objects.nonNull(stages) && !stages.isEmpty())
            ((IInventoryCrafting)inventory).dimhoppertweaks$setStages(stages,clear);
    }
    
    public static void setTileStages(@Nullable TileEntity tile, @Nullable Collection<String> stages) {
        if(tile instanceof CrafterBaseTE) setCrafterStages((CrafterBaseTE)tile,stages);
        else {
            if(tile instanceof TileEntityMultiblockPart<?>) tile = ((TileEntityMultiblockPart<?>)tile).master();
            if(Objects.nonNull(tile) && Objects.nonNull(stages) && !stages.isEmpty())
                ((ITileEntity)tile).dimhoppertweaks$setStages(stages);
        }
    }

    @SideOnly(CLIENT)
    public static void sendKeyPress(int type) {
        DHTNetwork.sendToServer(new PacketSendKeyPressed(type));
    }
    
    public static IBlockState water() {
        return WATER.getDefaultState();
    }
}