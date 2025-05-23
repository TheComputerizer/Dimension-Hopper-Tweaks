package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.STARGATE_ADDRESSER;
import static net.minecraft.init.Items.IRON_AXE;
import static net.minecraft.init.Items.WATER_BUCKET;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ENCHANTMENTS;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@SuppressWarnings("DataFlowIssue")
public class RecipeFunction extends EpicItem {

    @SideOnly(CLIENT)
    public static ModelResourceLocation getModelLocation(ItemStack stack) {
        String id = null;
        String variant = "";
        if(stack.hasTagCompound() && stack.getItem() instanceof RecipeFunction) {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag.hasKey("display")) {
                NBTTagCompound displayTag = tag.getCompoundTag("display");
                id = displayTag.getString("id");
                variant = displayTag.getString("variant");
            } else {
                RecipeFunction item = (RecipeFunction)stack.getItem();
                switch(tag.getString("type")) {
                    case "item": {
                        ItemStack transformed = item.transformToItem(tag.getCompoundTag("item"));
                        id = transformed.getItem().getRegistryName().toString();
                        break;
                    }
                    case "oredict": {
                        id = IRON_AXE.getRegistryName().toString();
                        break;
                    }
                    case "liquid": {
                        id = WATER_BUCKET.getRegistryName().toString();
                        break;
                    }
                }
            }
        }
        ResourceLocation res = StringUtils.isNotBlank(id) ? new ResourceLocation(id) : STARGATE_ADDRESSER.getRegistryName();
        if(StringUtils.isBlank(variant)) variant = "inventory";
        return getModelLocation(res,variant);
    }

    private static @Nullable ModelResourceLocation getModelLocation(@Nullable ResourceLocation res, String variant) {
        return Objects.nonNull(res) ? new ModelResourceLocation(res,variant) : null;
    }

    public void addInputs(ItemStack stack, Collection<ItemStack> inputs) {
        if(!(stack.getItem() instanceof RecipeFunction)) return;
        NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
        NBTTagList inputsTag = new NBTTagList();
        for(ItemStack input : inputs) {
            NBTTagCompound inputTag = new NBTTagCompound();
            ResourceLocation res = input.getItem().getRegistryName();
            if(Objects.nonNull(res)) {
                inputTag.setString("id",res.toString());
                inputTag.setInteger("meta",input.getMetadata());
                if(input.hasTagCompound()) inputTag.setTag("itemTag",input.getTagCompound());
                inputsTag.appendTag(inputTag);
            }
        }
        tag.setTag("inputs",inputsTag);
        stack.setTagCompound(tag);
    }

    public ItemStack transformStack(ItemStack stack) {
        if(!(stack.getItem() instanceof RecipeFunction)) return stack;
        NBTTagCompound tag = stack.getTagCompound();
        if(Objects.isNull(tag)) return EMPTY;
        switch(tag.getString("type")) {
            case "item": return transformToItem(tag.getCompoundTag("item"));
            case "oredict": return transformToOredict(tag);
            case "liquid": return transformToLiquid(tag);
        }
        return EMPTY;
    }

    private NBTTagList convertDelayedEnchTag(ItemStack stack, NBTTagList delayedTag, int maxEnchants) {
        NBTTagList enchList = new NBTTagList();
        int enchCounter = checkMaxEnchants(stack,enchList,maxEnchants);
        for(NBTBase based : delayedTag) {
            if(enchCounter>0) {
                NBTTagCompound enchTag = convertEnchTagElement((NBTTagCompound)based);
                if(Objects.nonNull(enchTag)) {
                    enchList.appendTag(enchTag);
                    enchCounter--;
                }
            } else break;
        }
        return enchList;
    }

    private @Nullable NBTTagCompound convertEnchTagElement(NBTTagCompound delayedElement) {
        ResourceLocation enchRes = new ResourceLocation(delayedElement.getString("name"));
        if(ENCHANTMENTS.containsKey(enchRes)) {
            NBTTagCompound enchTag = new NBTTagCompound();
            int level = delayedElement.getInteger("level");
            enchTag.setInteger("lvl",level);
            enchTag.setInteger("id",Enchantment.getEnchantmentID(ENCHANTMENTS.getValue(enchRes)));
            return enchTag;
        }
        return null;
    }

    protected ItemStack transformToItem(NBTTagCompound itemTag) {
        ItemStack stack = EMPTY;
        Item item = ItemUtil.getItem(itemTag.getString("id"));
        if(Objects.nonNull(item)) {
            int amount = itemTag.hasKey("amount") ? itemTag.getInteger("amount") : 1;
            stack = new ItemStack(item,amount,itemTag.getInteger("meta"));
            if(itemTag.hasKey("itemTag")) stack.setTagCompound(itemTag.getCompoundTag("itemTag"));
            if(stack.hasTagCompound()) {
                NBTTagCompound tag = stack.getTagCompound();
                if(tag.hasKey("delayedEnch")) {
                    int maxEnchants = tag.getInteger("maxEnchants");
                    NBTTagList enchTags = convertDelayedEnchTag(stack,tag.getTagList("delayedEnch",10),maxEnchants);
                    tag.setTag("ench",enchTags);
                    tag.removeTag("delayedEnch");
                }
            }
        }
        return stack;
    }

    private int checkMaxEnchants(ItemStack stack, NBTTagList enchTags, int maxEnchants) {
        NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
        if(!tag.hasKey("ench")) return maxEnchants;
        int counter = maxEnchants;
        for(NBTBase based : tag.getTagList("ench",10)) {
            enchTags.appendTag(based);
            counter--;
        }
        return counter;
    }

    private ItemStack findMatchingOredictItem(String oredict, NBTTagList list) {
        ItemStack stack = EMPTY;
        int oreID = OreDictionary.getOreID(oredict);
        match: {
            for(NBTBase based : list) {
                stack = transformToItem((NBTTagCompound)based);
                if(!stack.isEmpty())
                    for(int id : OreDictionary.getOreIDs(stack))
                        if(id==oreID) break match;
            }
        }
        return stack;
    }

    private ItemStack transformToOredict(NBTTagCompound tag) {
        ItemStack stack = EMPTY;
        String oredict = tag.getString("oredict");
        if(StringUtils.isNotBlank(oredict) && tag.hasKey("inputs")) {
            stack = findMatchingOredictItem(oredict,tag.getTagList("inputs",10));
            if(!stack.isEmpty()) {
                NBTTagCompound outputTag = tag.hasKey("itemTag") ? tag.getCompoundTag("itemTag") : null;
                if(Objects.nonNull(outputTag) && outputTag.hasKey("delayedEnch")) {
                    int maxEnchants = outputTag.getInteger("maxEnchants");
                    NBTTagList enchTags = convertDelayedEnchTag(stack,outputTag.getTagList("delayedEnch",10),maxEnchants);
                    NBTTagCompound stackTag = ItemUtil.getOrCreateTag(stack);
                    stackTag.setTag("ench",enchTags);
                }
            }
        }
        return stack;
    }

    private ItemStack transformToLiquid(NBTTagCompound tag) {
        ItemStack container = tag.hasKey("container") ? transformToItem(tag.getCompoundTag("container")) : EMPTY;
        FluidStack stack = null;
        NBTTagCompound fluidTag = tag.getCompoundTag("fluid");
        Fluid fluid = FluidRegistry.getFluid(fluidTag.getString("name"));
        if(Objects.nonNull(fluid))
            stack = new FluidStack(fluid,fluidTag.hasKey("amount") ? fluidTag.getInteger("amount") : 1000);
        return container;
    }

    @Override
    @SideOnly(CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if(stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            switch(tag.getString("type")) {
                case "item": {
                    tooltip.add("Item: "+transformStack(stack).getDisplayName());
                    break;
                }
                case "oredict": {
                    tooltip.add("Oredict: "+tag.getString("oredict"));
                    break;
                }
                case "liquid": {
                    if(tag.hasKey("fluid")) {
                        NBTTagCompound fluidTag = tag.getCompoundTag("fluid");
                        tooltip.add("Fluid name: "+fluidTag.getString("name"));
                        tooltip.add("Fluid amount: "+fluidTag.getString("amount")+"mb");
                    }
                }
            }
            if(tag.hasKey("itemTag")) {
                NBTTagCompound itemTag = tag.getCompoundTag("itemTag");
                if(itemTag.hasKey("delayedEnch")) {
                    NBTTagList enchTagList = itemTag.getTagList("delayedEnch",10);
                    for(NBTBase based : enchTagList) {
                        NBTTagCompound enchTag = (NBTTagCompound)based;
                        ResourceLocation enchRes = new ResourceLocation(enchTag.getString("name"));
                        Enchantment enchantment = ENCHANTMENTS.getValue(enchRes);
                        if(Objects.nonNull(enchantment))
                            tooltip.add("Enchantment: "+enchantment.getTranslatedName(enchTag.getInteger("level")));
                    }
                }
            }
        }
    }
}