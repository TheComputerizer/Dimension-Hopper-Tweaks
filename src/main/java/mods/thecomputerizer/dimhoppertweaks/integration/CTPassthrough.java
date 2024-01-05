package mods.thecomputerizer.dimhoppertweaks.integration;

import appeng.items.parts.ItemFacade;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ZenRegister
@ZenClass("mods.dimhoppertweaks.CTPassthrough")
public class CTPassthrough {

    @ZenMethod
    public static IItemStack[] getFacadeStacks() {
        Set<IItemStack> stacks = new HashSet<>();
        ItemFacade facade = (ItemFacade) ItemUtil.getItem("appliedenergistics2","facade");
        if(Objects.nonNull(facade)) {
            for(ItemStack facadeStack : facade.getFacades()) {
                NBTTagCompound tag = facadeStack.getTagCompound();
                if(Objects.nonNull(tag)) {
                    ItemStack stack = ItemUtil.getStack(tag.getString("item"),tag.getInteger("damage"));
                    if(!stack.isEmpty()) stacks.add(CraftTweakerMC.getIItemStack(stack));
                }
            }
        }
        return stacks.toArray(new IItemStack[0]);
    }
}
