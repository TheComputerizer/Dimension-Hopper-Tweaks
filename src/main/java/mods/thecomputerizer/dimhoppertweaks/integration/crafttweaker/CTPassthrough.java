package mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker;

import appeng.items.parts.ItemFacade;
import c4.conarm.lib.tinkering.TinkersArmor;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.thecomputerizer.dimhoppertweaks.integration.jei.JeiActionSupplier;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import mods.thecomputerizer.theimpossiblelibrary.api.text.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.tools.ranged.item.BoltCore;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.value.IntRange;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass("mods.dimhoppertweaks.CTPassthrough")
public class CTPassthrough {

    private static final List<String> ADVENTURE_BACKPACK_IDS = Arrays.asList("contenttweaker:twilight_key_winter",
            "contenttweaker:twilight_key_swamp","contenttweaker:twilight_key_forest","contenttweaker:twilight_key",
            "twilightforest:meef_stroganoff","twilightforest:steeleaf_ingot","minecraft:coal","minecraft:coal_block",
            "psi:material","psicosts:psi_cell","dimdoors:world_thread");
    public static final Map<IToolPart,Material> SPECIALIZED_PARTS = createSpecialPartMap();
    private static List<String> ADVENTURE_BACKPACK_NAMES = new ArrayList<>();
    private static boolean isBackpackCached = false;

    private static void createBackpackItems() {
        List<Item> items = new ArrayList<>();
        for(String id : ADVENTURE_BACKPACK_IDS) {
            Item item = ItemUtil.getItem(id);
            if(Objects.nonNull(item)) items.add(item);
        }
        ADVENTURE_BACKPACK_NAMES = Collections.unmodifiableList(items.stream()
                .map(item -> item.getItemStackDisplayName(new ItemStack(item))).collect(Collectors.toList()));
        isBackpackCached = true;
    }

    private static Map<IToolPart,Material> createSpecialPartMap() {
        Map<IToolPart,Material> map = new HashMap<>();
        createSpecialPartEntry(map,"tconstruct:bow_string","string");
        createSpecialPartEntry(map,"tconstruct:fletching","feather");
        createSpecialPartEntry(map,"tconstruct:arrow_shaft","wood");
        createSpecialPartEntry(map,"plustic:laser_medium","prismarine");
        createSpecialPartEntry(map,"plustic:battery_cell","manyullyn");
        return map;
    }

    private static IItemStack convertStack(ItemStack stack) {
        IItemStack ctStack = CraftTweakerMC.getIItemStack(stack);
        return stack.hasTagCompound() ? ctStack.withTag(CraftTweakerMC.getIData(stack.getTagCompound()),true) : ctStack;
    }

    private static void createSpecialPartEntry(Map<IToolPart,Material> map, String partName, String matName) {
        IToolPart part = getToolPart(partName);
        if(Objects.isNull(part)) return;
        Material mat = TinkerRegistry.getMaterial(matName);
        if(mat!=Material.UNKNOWN) map.put(part,mat);
    }

    @ZenMethod
    public static String getAdventuringBackpackItems() {
        if(!isBackpackCached) createBackpackItems();
        String ret = TextHelper.fromIterable(ADVENTURE_BACKPACK_NAMES,", ");
        return Objects.nonNull(ret) ? ret : "?";
    }

    @ZenMethod
    public static IItemStack[] getFacadeStacks() {
        return makeArray(IItemStack.class,HashSet::new,stacks -> {
            ItemFacade facade = (ItemFacade) ItemUtil.getItem("appliedenergistics2","facade");
            if(Objects.nonNull(facade)) {
                for(ItemStack facadeStack : facade.getFacades()) {
                    NBTTagCompound tag = facadeStack.getTagCompound();
                    if(Objects.nonNull(tag)) {
                        ItemStack stack = ItemUtil.getStack(tag.getString("item"),tag.getInteger("damage"));
                        if(!stack.isEmpty()) stacks.add(convertStack(stack));
                    }
                }
            }
        });
    }

    @ZenMethod
    public static int[] getIntRangeArray(IntRange range) {
        int[] aint = new int[range.getTo()-range.getFrom()];
        int counter = range.getFrom(), index = 0;
        while(counter<range.getFrom()) {
            aint[index] = counter;
            index++;
        }
        return aint;
    }

    @ZenMethod
    public static String[] getLivingEntityIDs() {
        return makeArray(String.class,HashSet::new,entities -> {
            for(EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection()) {
                if(EntityLiving.class.isAssignableFrom(entry.getEntityClass())) {
                    ResourceLocation res = entry.getRegistryName();
                    if(Objects.nonNull(res)) entities.add(res.toString());
                }
            }
        });
    }

    private static @Nullable String[] getMaterialArray(List<PartMaterialType> components, Material mat) {
        List<String> materials = new ArrayList<>();
        for(PartMaterialType type : components) {
            Optional<IToolPart> part = type.getPossibleParts().stream().findFirst();
            part.ifPresent(p -> materials.add(SPECIALIZED_PARTS.getOrDefault(p,mat).identifier));
        }
        return materials.size()==components.size() ? materials.toArray(new String[0]) : null;
    }

    @ZenMethod
    public static IItemStack[] getTinkerBoltCores() {
        return makeArray(IItemStack.class,HashSet::new,stacks -> {
            Item item = ItemUtil.getItem("tconstruct:bolt_core");
            if(item instanceof BoltCore) {
                BoltCore bolt = (BoltCore)item;
                for(Material mat : TinkerRegistry.getAllMaterials()) {
                    if(bolt.canUseMaterial(mat)) {
                        NBTTagCompound tag = new NBTTagCompound();
                        NBTTagCompound materialTag = new NBTTagCompound();
                        NBTTagList materials = new NBTTagList();
                        materials.appendTag(new NBTTagString(mat.identifier));
                        materials.appendTag(new NBTTagString("iron"));
                        materialTag.setTag("Materials",materials);
                        tag.setTag("TinkerData",materialTag);
                        ItemStack stack = new ItemStack(bolt);
                        stack.setTagCompound(tag);
                        stacks.add(convertStack(stack));
                    }
                }
            }
        });
    }

    @ZenMethod
    public static String[] getTinkerPartMaterials(IItemDefinition def) {
        return makeArray(String.class,HashSet::new,materials -> {
            Item type = ItemUtil.getItem(def.getId());
            if(Objects.nonNull(type))
                for(Material mat : TinkerRegistry.getAllMaterials())
                    if(type instanceof IToolPart && ((IToolPart) type).canUseMaterial(mat))
                        materials.add(mat.identifier);
        });
    }

    @ZenMethod
    public static IItemStack[] getTinkerTableBlocks(IBlockState state) {
        return makeArray(IItemStack.class,HashSet::new,tables -> {
            Block block = CraftTweakerMC.getBlock(state.getBlock());
            NonNullList<ItemStack> stacks = NonNullList.create();
            block.getSubBlocks(CreativeTabs.MISC,stacks);
            for(ItemStack stack : stacks)
                if(!stack.isEmpty()) tables.add(convertStack(stack));
        });
    }

    @ZenMethod
    public static String[][] getTinkerToolMaterials(IItemDefinition def) {
        return makeArray(String[].class,HashSet::new,materials -> {
            Item type = ItemUtil.getItem(def.getId());
            if(Objects.nonNull(type)) {
                for(Material mat : TinkerRegistry.getAllMaterials()) {
                    if(type instanceof TinkersItem) {
                        String[] mats = getMaterialArray(((TinkersItem)type).getRequiredComponents(),mat);
                        if(Objects.nonNull(mats)) materials.add(mats);
                    }
                    else if(type instanceof TinkersArmor) {
                        String[] mats = getMaterialArray(((TinkersArmor)type).getRequiredComponents(),mat);
                        if(Objects.nonNull(mats)) materials.add(mats);
                    }
                }
            }
        });
    }

    private static @Nullable IToolPart getToolPart(String type) {
        Item part = ItemUtil.getItem(type);
        return part instanceof IToolPart ? (IToolPart)part : null;
    }

    @SuppressWarnings("unchecked")
    private static <E> E[] makeArray(Class<E> clazz, Supplier<Collection<E>> supplier, Consumer<Collection<E>> injector) {
        Collection<E> c = supplier.get();
        injector.accept(c);
        E[] array = (E[])Array.newInstance(clazz,c.size());
        int index = 0;
        for(E element : c) {
            array[index] = element;
            index++;
        }
        return array;
    }

    @ZenMethod
    public static void queueJEIAdditions(IItemSupplier supplier) {
        JeiActionSupplier.queueAdditions(supplier::get);
    }

    @ZenMethod
    public static void queueJEIRemovals(IItemSupplier supplier) {
        JeiActionSupplier.queueRemovals(supplier::get);
    }

    @ZenMethod
    public static void removeJEIDescriptions(IIngredientSupplier supplier) {
        JeiActionSupplier.queueDescriptionRemovals(supplier::get);
    }

    @ZenMethod
    public static int getActualEnchantmentID(String registryName) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(registryName));
        return Objects.nonNull(ench) ? Enchantment.getEnchantmentID(ench) : 0;
    }
}
