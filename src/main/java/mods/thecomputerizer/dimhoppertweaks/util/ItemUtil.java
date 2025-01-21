package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemUtil {

    public static @Nullable Item getItem(String modid, String name) {
        return getItem(new ResourceLocation(modid,name));
    }

    public static @Nullable Item getItem(String itemRes) {
        return getItem(new ResourceLocation(itemRes));
    }

    public static @Nullable Item getItem(ResourceLocation res) {
        return ForgeRegistries.ITEMS.containsKey(res) ? ForgeRegistries.ITEMS.getValue(res) : null;
    }
    
    public static ItemStack getStack(@Nullable String itemRes) {
        return getStack(itemRes,0);
    }

    public static ItemStack getStack(@Nullable String itemRes, int meta) {
        if(StringUtils.isBlank(itemRes)) return ItemStack.EMPTY;
        Item item = getItem(itemRes);
        return Objects.nonNull(item) ? new ItemStack(item,1,meta) : ItemStack.EMPTY;
    }
    
    public static ItemStack getStack(@Nullable ResourceLocation itemRes, int meta) {
        if(Objects.isNull(itemRes)) return ItemStack.EMPTY;
        Item item = getItem(itemRes);
        return Objects.nonNull(item) ? new ItemStack(item,1,meta) : ItemStack.EMPTY;
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static boolean isHolding(EntityPlayer player, Item item) {
        return player.getHeldItemMainhand().getItem()==item || player.getHeldItemOffhand().getItem()==item;
    }
}
