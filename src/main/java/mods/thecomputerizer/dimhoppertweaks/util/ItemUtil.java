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
import java.util.function.BiFunction;

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

    public static ItemStack getStack(@Nullable String itemRes, int meta) {
        if(StringUtils.isBlank(itemRes)) return ItemStack.EMPTY;
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

    public static boolean tagsMatch(@Nullable NBTTagCompound tag, @Nullable NBTTagCompound toMatch, boolean nullMatchesAny) {
        if(Objects.isNull(toMatch)) return nullMatchesAny || Objects.isNull(tag);
        if(Objects.isNull(tag)) return false;
        for(String key : toMatch.getKeySet()) {
            if(!tag.hasKey(key) || !innerMatch(tag.getTag(key),toMatch.getTag(key),nullMatchesAny)) return false;
        }
        return true;
    }

    private static boolean innerMatch(NBTBase tag, NBTBase toMatch, boolean nullMatchesAny) {
        if(toMatch.toString().equals(tag.toString())) return true;
        if(toMatch instanceof NBTPrimitive) {
            if(!(tag instanceof NBTPrimitive)) return false;
            return ((NBTPrimitive)toMatch).getDouble()==((NBTPrimitive)tag).getDouble();
        }
        if(toMatch instanceof NBTTagCompound) {
            if(!(tag instanceof NBTTagCompound)) return false;
            return tagsMatch((NBTTagCompound)tag,(NBTTagCompound)toMatch,nullMatchesAny);
        }
        return tag.equals(toMatch);
    }
}
